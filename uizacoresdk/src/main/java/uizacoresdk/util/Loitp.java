package uizacoresdk.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.provider.Settings;

import com.google.gson.Gson;

import org.apache.commons.codec.DecoderException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

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

    @SuppressLint("HardwareIds")
    public static String getDeviceId(Context context) {
        if (context == null) {
            return "";
        }
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static int getViewerOsArchitecture() {
        try {
            boolean isArm64 = false;
            BufferedReader localBufferedReader = new BufferedReader(new FileReader("/proc/cpuinfo"));
            if (localBufferedReader.readLine().contains("aarch64")) {
                isArm64 = true;
            }
            localBufferedReader.close();
            return isArm64 ? 64 : 32;
        } catch (IOException e) {
        }
        return 0;
    }
}
