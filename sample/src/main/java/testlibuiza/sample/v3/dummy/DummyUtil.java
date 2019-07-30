package testlibuiza.sample.v3.dummy;

import android.annotation.SuppressLint;
import android.content.Context;
import android.provider.Settings;
import android.util.Base64;
import com.google.gson.Gson;
import org.apache.commons.codec.DecoderException;
import testlibuiza.R;
import vn.uiza.restapi.uiza.model.v3.drm.LicenseAcquisitionUrl;

public class DummyUtil {
    public static LicenseAcquisitionUrl decrypt(Context context, String input) throws DecoderException {
        return decrypt(context, input, new Gson());
    }

    public static LicenseAcquisitionUrl decrypt(Context context, String input, Gson gson) throws
            DecoderException {
        if (context == null || input == null || input.isEmpty() || input.length() <= 16) {
            return null;
        }
        if (gson == null) {
            gson = new Gson();
        }
        String key = getKey(context.getString(R.string.loitp_best_dev_ever));
        input = input.trim();
        String hexIv = input.substring(0, 16);
        String hexText = input.substring(16);
        byte[] decodedHex = org.apache.commons.codec.binary.Hex.decodeHex(hexText.toCharArray());
        String base64 = Base64.encodeToString(decodedHex, android.util.Base64.NO_WRAP);
        String decrypt = Encryptor.decrypt(key, hexIv, base64);
        return gson.fromJson(decrypt, LicenseAcquisitionUrl.class);
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
