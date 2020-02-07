package vn.uiza.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Telephony;

import androidx.annotation.NonNull;

import vn.uiza.core.common.Constants;

/**
 * Created by www.muathu@gmail.com on 1/29/2018.
 */

public final class LSMSUtil {
    private LSMSUtil() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    //https://gist.github.com/mustafasevgi/8c6b638ffd5fca90d45d
    public static void sendSMS(@NonNull Context context, String text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) // At least KitKat
        {
            String defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(context); // Need to change the build to API 19

            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.setType(Constants.TEXT_TYPE);
            sendIntent.putExtra(Intent.EXTRA_TEXT, text);

            if (defaultSmsPackageName != null)// Can be null in case that there is no default, then the user would be able to choose
            // any app that support this intent.
            {
                sendIntent.setPackage(defaultSmsPackageName);
            }
            context.startActivity(sendIntent);
        } else // For early versions, do what worked for you before.
        {
            Intent sendIntent = new Intent(Intent.ACTION_VIEW);
            sendIntent.setData(Uri.parse(Constants.SMS_URI));
            sendIntent.putExtra(Constants.SMS_BODY, text);
            context.startActivity(sendIntent);
        }
    }
}
