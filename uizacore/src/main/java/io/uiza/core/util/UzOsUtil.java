package io.uiza.core.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.provider.Settings;
import io.uiza.core.util.SentryUtil;
import io.uiza.core.util.constant.Constants;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class UzOsUtil {

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
            BufferedReader localBufferedReader = new BufferedReader(
                    new FileReader(Constants.CPU_INFO_FILENAME));
            if (localBufferedReader.readLine().contains(Constants.AARCH64)) {
                isArm64 = true;
            }
            localBufferedReader.close();
            return isArm64 ? 64 : 32;
        } catch (IOException e) {
            SentryUtil.captureException(e);
        }
        return 0;
    }
}
