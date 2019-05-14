package uizacoresdk.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.provider.Settings;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import vn.uiza.core.common.Constants;
import vn.uiza.utils.util.SentryUtils;

public class UZOsUtil {
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
            BufferedReader localBufferedReader = new BufferedReader(new FileReader(Constants.CPU_INFO_FILENAME));
            if (localBufferedReader.readLine().contains(Constants.AARCH64)) {
                isArm64 = true;
            }
            localBufferedReader.close();
            return isArm64 ? 64 : 32;
        } catch (IOException e) {
            SentryUtils.captureException(e);
        }
        return 0;
    }
}
