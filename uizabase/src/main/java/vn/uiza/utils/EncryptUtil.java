package vn.uiza.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;

import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import timber.log.Timber;

public final class EncryptUtil {

    private EncryptUtil() {
    }

    private static final String ALGORITHM = "HmacSHA256"; // HmacMD5, HmacSHA1, HmacSHA256, HmacSHA512

    public static String hmacSHA256(String key, String data) {
        try {
            Mac sha = Mac.getInstance(ALGORITHM);
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(Charset.forName("UTF-8")), ALGORITHM);
            sha.init(secretKey);
            return bytesToHex(sha.doFinal(data.getBytes(Charset.forName("UTF-8"))));
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            Timber.e(e);
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
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                return md5(md.digest());
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
            Timber.e(e);
        }
        return null;
    }

    public static String md5(byte[] input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(input);
            return bytesToHex(md.digest());
        } catch (NoSuchAlgorithmException e) {
            Timber.e(e);
        }
        return null;
    }

    public static String sha1(byte[] input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(input);
            return bytesToHex(md.digest());
        } catch (NoSuchAlgorithmException e) {
            Timber.e(e);
        }
        return null;
    }

    public static String sha256(byte[] input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
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

}
