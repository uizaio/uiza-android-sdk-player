package testlibuiza.sample.v3.dummy;

import android.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import vn.uiza.core.common.Constants;

public class Encryptor {
    private final static String TAG = Encryptor.class.getSimpleName();

    public static String encrypt(String key, String initVector, String value) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes(Constants.UTF_8_CHARSET));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(Constants.UTF_8_CHARSET), Constants.AES_ALGORITHM);
            Cipher cipher = Cipher.getInstance(Constants.AES_CTR_NO_PADDING);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            byte[] encrypted = cipher.doFinal(value.getBytes());
            return Base64.encodeToString(encrypted, Base64.DEFAULT);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static String decrypt(String key, String initVector, String encrypted) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes(Constants.UTF_8_CHARSET));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(Constants.UTF_8_CHARSET), Constants.AES_ALGORITHM);
            Cipher cipher = Cipher.getInstance(Constants.AES_CTR_NO_PADDING);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] original = cipher.doFinal(Base64.decode(encrypted, Base64.DEFAULT));
            return new String(original);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}