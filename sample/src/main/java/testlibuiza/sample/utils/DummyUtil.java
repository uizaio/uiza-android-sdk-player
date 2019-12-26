package testlibuiza.sample.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.provider.Settings;
import android.util.Base64;

import org.apache.commons.codec.DecoderException;

import testlibuiza.R;
import vn.uiza.restapi.model.v3.drm.LicenseAcquisitionUrl;
import vn.uiza.utils.EncryptUtil;
import vn.uiza.utils.StringUtil;

public class DummyUtil {

    public static LicenseAcquisitionUrl decrypt(Context context, String input) throws
            DecoderException {
        if (context == null || input == null || input.isEmpty() || input.length() <= 16) {
            return null;
        }
        String key = getKey(context.getString(R.string.loitp_best_dev_ever));
        input = input.trim();
        String hexIv = input.substring(0, 16);
        String hexText = input.substring(16);
        byte[] decodedHex = org.apache.commons.codec.binary.Hex.decodeHex(hexText.toCharArray());
        String base64 = Base64.encodeToString(decodedHex, Base64.NO_WRAP);
        String decrypt = EncryptUtil.decrypt(key, hexIv, base64);
        return StringUtil.toObject(decrypt, LicenseAcquisitionUrl.class);
    }

    private static String getKey(String str) {
        return str.replace("getKey", "");
    }

    @SuppressLint("HardwareIds")
    public static String getDeviceId(Context context) {
        if (context == null) {
            return "";
        }
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }
}
