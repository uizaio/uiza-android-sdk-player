package uizacoresdk.util;

import android.content.Context;

import com.google.gson.Gson;

import org.apache.commons.codec.DecoderException;

import uizacoresdk.R;
import vn.uiza.restapi.uiza.model.v3.drm.LicenseAcquisitionUrl;
import vn.uiza.utils.util.Encryptor;

public class Loitp {
    public static LicenseAcquisitionUrl decrypt(Context context, String input) throws DecoderException {
        return decrypt(context, input, new Gson());
    }

    public static LicenseAcquisitionUrl decrypt(Context context, String input, Gson gson) throws DecoderException {
        if (context == null || input == null || input.isEmpty() || input.length() <= 16) {
            return null;
        }
        if (gson == null) {
            gson = new Gson();
        }
        String key = loitp(context.getString(R.string.loitp_best_dev_ever));
        input = input.trim();
        String hexIv = input.substring(0, 16);
        //LLog.d(TAG, "hexIv: " + hexIv);
        String hexText = input.substring(16, input.length());
        //LLog.d(TAG, "hexText: " + hexText);

        byte[] decodedHex = org.apache.commons.codec.binary.Hex.decodeHex(hexText.toCharArray());
        String base64 = android.util.Base64.encodeToString(decodedHex, android.util.Base64.NO_WRAP);
        //LLog.d(TAG, "base64 " + base64);
        String decrypt = Encryptor.decrypt(key, hexIv, base64);
        //LLog.d(TAG, "decrypt " + decrypt);
        return gson.fromJson(decrypt, LicenseAcquisitionUrl.class);
    }

    private static String loitp(String loitp) {
        return loitp.replace("loitp", "");
    }
}
