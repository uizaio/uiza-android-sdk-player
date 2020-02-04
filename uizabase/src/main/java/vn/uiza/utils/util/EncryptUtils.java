package vn.uiza.utils.util;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;

import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import vn.uiza.BuildConfig;
import vn.uiza.core.common.Constants;
import vn.uiza.core.utilities.LLog;

public final class EncryptUtils {

    private static final String TAG = "EncryptUtils";

    private EncryptUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    private static final String ALGORITHM = "HmacSHA256"; // HmacMD5, HmacSHA1, HmacSHA256, HmacSHA512

    // Function for alternatively merging two strings
    public static String merge(String s1, String s2) {
        // To store the final string
        StringBuilder result = new StringBuilder();
        // For every index in the strings
        for (int i = 0; i < s1.length() || i < s2.length(); i++) {
            // First choose the ith character of the
            // first string if it exists
            if (i < s1.length())
                result.append(s1.charAt(i));
            // Then choose the ith character of the
            // second string if it exists
            if (i < s2.length())
                result.append(s2.charAt(i));
        }

        return result.toString();
    }

    public static String hmacSHA256(String key, byte[] data) {
        try {
            Mac sha = Mac.getInstance(ALGORITHM);
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(Charset.forName("UTF-8")), ALGORITHM);
            sha.init(secretKey);
            return bytesToHex(sha.doFinal(data));
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            LLog.e(TAG,e.getLocalizedMessage());
        }
        return null;
    }

    public static String hmacSHA256(String key, String data) {
        try {
            Mac sha = Mac.getInstance(ALGORITHM);
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(Charset.forName("UTF-8")), ALGORITHM);
            sha.init(secretKey);
            return bytesToHex(sha.doFinal(data.getBytes(Charset.forName("UTF-8"))));
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            LLog.e(TAG, e.getLocalizedMessage());
        }
        return null;
    }

    /**
     * Use cmd:
     * keytool -exportcert -alias androiddebugkey -keystore ~/.android/debug.keystore | openssl sha1 -binary | openssl md5
     *
     * @param context
     * @return
     */
    @SuppressLint("PackageManagerGetSignatures")
    public static String getAppSigned(Context context) {
        PackageInfo info;
        try {
            info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            Signature[] signatures = info.signatures;
            for (Signature signature : signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                return md5(md.digest());
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
            LLog.e(TAG,e.getLocalizedMessage());
        }
        return null;
    }

    public static String base64Encode(byte[] input) {
        return new String(Base64.encode(input, Base64.NO_WRAP));
    }

    public static String md5(byte[] input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(input);
            return bytesToHex(md.digest());
        } catch (NoSuchAlgorithmException e) {
            LLog.e(TAG, e.getLocalizedMessage());
        }
        return null;
    }

    public static String sha1(byte[] input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(input);
            return bytesToHex(md.digest());
        } catch (NoSuchAlgorithmException e) {
            LLog.e(TAG, e.getLocalizedMessage());
        }
        return null;
    }

    public static String sha256(byte[] input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(input);
            return bytesToHex(md.digest());
        } catch (NoSuchAlgorithmException e) {
            LLog.e(TAG, e.getLocalizedMessage());
        }
        return null;
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) result.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        return result.toString();
    }

    public static String encrypt(String key, String initVector, String value) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes(Charset.forName("UTF-8")));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(Charset.forName("UTF-8")), Constants.AES_ALGORITHM);
            Cipher cipher = Cipher.getInstance(Constants.AES_CTR_NO_PADDING);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            byte[] encrypted = cipher.doFinal(value.getBytes());
            return Base64.encodeToString(encrypted, Base64.DEFAULT);
        } catch (Exception ex) {
            LLog.e(TAG, ex.getLocalizedMessage());
        }
        return null;
    }

    public static String decrypt(String key, String initVector, String encrypted) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes(Charset.forName("UTF-8")));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(Charset.forName("UTF-8")), Constants.AES_ALGORITHM);
            Cipher cipher = Cipher.getInstance(Constants.AES_CTR_NO_PADDING);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] original = cipher.doFinal(Base64.decode(encrypted, Base64.DEFAULT));
            return new String(original);
        } catch (Exception ex) {
            LLog.e(TAG, ex.getLocalizedMessage());
        }
        return null;
    }

    public static byte[] decodeHex(final char[] data) throws Exception {
        final int len = data.length;
        if ((len & 0x01) != 0) {
            throw new Exception("Odd number of characters.");
        }
        final byte[] out = new byte[len >> 1];
        // two characters form the hex value.
        for (int i = 0, j = 0; j < len; i++) {
            int f = toDigit(data[j], j) << 4;
            j++;
            f = f | toDigit(data[j], j);
            j++;
            out[i] = (byte) (f & 0xFF);
        }
        return out;
    }

    private static int toDigit(final char ch, final int index) throws Exception {
        final int digit = Character.digit(ch, 16);
        if (digit == -1) {
            throw new Exception("Illegal hexadecimal character " + ch + " at index " + index);
        }
        return digit;
    }

}