package vn.uiza.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.pm.SigningInfo;
import android.os.Build;
import android.util.Base64;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import timber.log.Timber;
import vn.uiza.BuildConfig;
import vn.uiza.core.common.Constants;

public final class EncryptUtils {

    private EncryptUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    @StringDef({HMAC_MD5, HMAC_SHA_1, HMAC_SHA_256, HMAC_SHA_512})
    @Retention(RetentionPolicy.SOURCE)
    public @interface AlgorithmValues {
    }

    public static final String MD5 = "MD5";
    public static final String SHA_1 = "SHA1";
    public static final String SHA_256 = "SHA256";
    public static final String SHA_512 = "SHA512";
    public static final String HMAC_MD5 = "HmacMD5";
    public static final String HMAC_SHA_1 = "HmacSHA1";
    public static final String HMAC_SHA_256 = "HmacSHA256";
    private static final String HMAC_SHA_512 = "HmacSHA512";

    public static final String UTF_8 = "UTF-8";
    private static final Charset CHARSET_UTF8 = Charset.forName("UTF-8");
    public static final String AES_ALGORITHM = "AES";
    public static final String AES_CTR_NO_PADDING = "AES/CTR/NoPadding";

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

    /**
     * @param key       the key material of the secret key. The contents of
     *                  the array are copied to protect against subsequent modification.
     * @param data      data in bytes
     * @param algorithm the name of the secret-key algorithm to be associated
     *                  with the given key material.
     *                  See {@link AlgorithmValues}
     * @return hmac string, null if exception
     */
    @Nullable
    public static String hmac(String key, byte[] data, @AlgorithmValues String algorithm) {
        try {
            Mac sha = Mac.getInstance(algorithm);
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(CHARSET_UTF8), algorithm);
            sha.init(secretKey);
            return bytesToHex(sha.doFinal(data));
        } catch (NoSuchAlgorithmException | InvalidKeyException | IllegalStateException e) {
            Timber.e(e);
        }
        return null;
    }

    /**
     * @param key       the key material of the secret key. The contents of
     *                  the array are copied to protect against subsequent modification.
     * @param data      data in string
     * @param algorithm the name of the secret-key algorithm to be associated
     *                  with the given key material.
     *                  See {@link AlgorithmValues}
     * @return String of hmac, null if exception
     */
    @Nullable
    public static String hmac(String key, String data, @AlgorithmValues String algorithm) {
        try {
            Mac sha = Mac.getInstance(algorithm);
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(CHARSET_UTF8), algorithm);
            sha.init(secretKey);
            return bytesToHex(sha.doFinal(data.getBytes(CHARSET_UTF8)));
        } catch (NoSuchAlgorithmException | InvalidKeyException | IllegalStateException e) {
            Timber.e(e);
        }
        return null;
    }

    /**
     * @param key  the key material of the secret key. The contents of
     *             the array are copied to protect against subsequent modification.
     * @param data data in bytes
     * @return String of hmac 256, null if exception
     */
    @Nullable
    public static String hmacSHA256(String key, byte[] data) {
        return hmac(key, data, HMAC_SHA_256);
    }

    /**
     * @param key  the key material of the secret key. The contents of
     *             the array are copied to protect against subsequent modification.
     * @param data data in string
     * @return String of hmac 256, null if exception
     */
    @Nullable
    public static String hmacSHA256(String key, String data) {
        return hmac(key, data, HMAC_SHA_256);
    }

    /**
     * Use cmd:
     * keytool -exportcert -alias androiddebugkey -keystore ~/.android/debug.keystore | openssl sha1 -binary | openssl md5
     *
     * @param context
     * @return
     */
    @SuppressLint("PackageManagerGetSignatures")
    public static String getAppSigned(@NonNull Context context) {
        PackageInfo info;
        Signature[] signatures;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNING_CERTIFICATES);
                if (info.signingInfo.hasMultipleSigners()) {
                    signatures = info.signingInfo.getApkContentsSigners();
                } else {
                    signatures = info.signingInfo.getSigningCertificateHistory();
                }
            } else {
                info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
                signatures = info.signatures;
            }
            if (signatures != null && signatures.length > 0) {
                return signFromSignature(signatures);
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
            Timber.e(e);
        }
        return null;
    }

    @Nullable
    private static String signFromSignature(Signature[] signatures) throws NoSuchAlgorithmException {
        for (Signature signature : signatures) {
            MessageDigest md = MessageDigest.getInstance(SHA_1);
            md.update(signature.toByteArray());
            return md5(md.digest());
        }
        return null;
    }

    public static String base64Encode(byte[] input) {
        return new String(Base64.encode(input, Base64.NO_WRAP), CHARSET_UTF8);
    }

    @Nullable
    public static String md5(byte[] sig) {
        try {
            MessageDigest md = MessageDigest.getInstance(MD5);
            md.update(sig);
            return bytesToHex(md.digest());
        } catch (NoSuchAlgorithmException e) {
            Timber.e(e);
        }
        return null;
    }

    @Nullable
    public static String sha1(byte[] input) {
        try {
            MessageDigest md = MessageDigest.getInstance(SHA_1);
            md.update(input);
            return bytesToHex(md.digest());
        } catch (NoSuchAlgorithmException e) {
            Timber.e(e);
        }
        return null;
    }

    @Nullable
    public static String sha256(byte[] input) {
        try {
            MessageDigest md = MessageDigest.getInstance(SHA_256);
            md.update(input);
            return bytesToHex(md.digest());
        } catch (NoSuchAlgorithmException e) {
            Timber.e(e);
        }
        return null;
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) result.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        return result.toString();
    }

    @Nullable
    public static String encrypt(String key, String initVector, String value) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes(CHARSET_UTF8));
            SecretKeySpec sKeySpec = new SecretKeySpec(key.getBytes(CHARSET_UTF8), AES_ALGORITHM);
            Cipher cipher = Cipher.getInstance(AES_CTR_NO_PADDING);
            cipher.init(Cipher.ENCRYPT_MODE, sKeySpec, iv);
            byte[] encrypted = cipher.doFinal(value.getBytes());
            return Base64.encodeToString(encrypted, Base64.DEFAULT);
        } catch (Exception ex) {
            Timber.e(ex);
        }
        return null;
    }

    @Nullable
    public static String decrypt(String key, String initVector, String encrypted) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes(CHARSET_UTF8));
            SecretKeySpec sKeySpec = new SecretKeySpec(key.getBytes(CHARSET_UTF8), AES_ALGORITHM);
            Cipher cipher = Cipher.getInstance(AES_CTR_NO_PADDING);
            cipher.init(Cipher.DECRYPT_MODE, sKeySpec, iv);
            byte[] original = cipher.doFinal(Base64.decode(encrypted, Base64.DEFAULT));
            return new String(original);
        } catch (Exception ex) {
            Timber.e(ex);
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
