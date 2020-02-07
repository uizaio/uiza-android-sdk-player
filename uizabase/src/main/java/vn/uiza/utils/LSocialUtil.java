package vn.uiza.utils;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import androidx.annotation.NonNull;

import timber.log.Timber;
import vn.uiza.R;
import vn.uiza.core.common.Constants;
import vn.uiza.core.exception.UizaException;


/**
 * File created on 11/14/2016.
 *
 * @author loitp
 */
public final class LSocialUtil {
    private static final String MARKET_URI = "market://details?id=";
    private static final String PLAY_STORE_DETAIL_URI =
            "http://play.google.com/store/apps/details?id=";
    private static final String PLAY_STORE_DEV_URI =
            "https://play.google.com/store/apps/developer?id=";
    private static final String FB_PACKAGE = "com.facebook.katana";
    private static final String FB_WEB_MODEL_URI = "fb://facewebmodal/f?href=";
    private static final String FB_PAGE_URI = "fb://page/";
    private static final String FB_MESSENGER_PACKAGE = "com.facebook.orca";
    private static final String FB_MESSENGER_URI = "fb-messenger://user/";
    private static final String SHARE_TITLE = "Share via";

    private LSocialUtil() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static void rateApp(@NonNull Context context, String packageName) {
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(MARKET_URI + packageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(
                    PLAY_STORE_DETAIL_URI + packageName)));
            Timber.e(anfe);
        }
    }

    public static void moreApp(@NonNull Context context, String devName) {
        String uri = PLAY_STORE_DEV_URI + devName;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        context.startActivity(intent);
    }

    public static void share(@NonNull Context context, String msg) {
        try {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType(Constants.TEXT_TYPE);
            intent.putExtra(Intent.EXTRA_SUBJECT, AppUtils.getAppName(context));
            intent.putExtra(Intent.EXTRA_TEXT, msg);
            context.startActivity(Intent.createChooser(intent, SHARE_TITLE));
        } catch (Exception e) {
            Timber.e(e);
        }
    }

    public static void sharingToSocialMedia(@NonNull Context context, String application, String subject, String message) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType(Constants.TEXT_TYPE);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, message);
        boolean installed = checkAppInstall(context, application);
        if (installed) {
            intent.setPackage(application);
            context.startActivity(intent);
        } else {
            Timber.e(context.getString(R.string.can_not_find_share_app));
        }
    }

    private static boolean checkAppInstall(@NonNull Context context, String uri) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            Timber.e(e);
        }

        return false;
    }

    public static void likeFacebookFanpage(@NonNull Context context, @NonNull String fbFanPageUrl, @NonNull String fbFanPageId) {
        Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
        String facebookUrl = getFacebookPageURL(context, fbFanPageUrl, fbFanPageId);
        facebookIntent.setData(Uri.parse(facebookUrl));
        context.startActivity(facebookIntent);
    }

    private static String getFacebookPageURL(@NonNull Context context, String fbFanPageUrl, String fbFanPageId) {
        PackageManager packageManager = context.getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo(FB_PACKAGE, 0).versionCode;
            if (versionCode >= 3002850) {
                return FB_WEB_MODEL_URI + fbFanPageUrl;
            } else {
                return FB_PAGE_URI + fbFanPageId;
            }
        } catch (PackageManager.NameNotFoundException e) {
            Timber.e(e);
            if (fbFanPageUrl != null)
                return fbFanPageUrl;
            else return fbFanPageId;
        }
    }

    public static void chatMessenger(@NonNull Context context, String messengerId) {
        PackageManager packageManager = context.getPackageManager();
        boolean isFBInstalled = false;
        try {
            int versionCode = packageManager.getPackageInfo(FB_MESSENGER_PACKAGE, 0).versionCode;
            if (versionCode >= 0) isFBInstalled = true;
        } catch (PackageManager.NameNotFoundException e) {
            Timber.e(e);
        }
        if (!isFBInstalled) {
            LDialogUtil.showDialog1(context, context.getString(R.string.error), UizaException.ERR_22, context.getString(R.string.ok), null);
        } else {
            Uri uri = Uri.parse(FB_MESSENGER_URI);
            uri = ContentUris.withAppendedId(uri, Long.valueOf(messengerId));
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            try {
                context.startActivity(intent);
            } catch (Exception e) {
                LDialogUtil.showDialog1(context, UizaException.ERR_20, UizaException.ERR_22, context.getString(R.string.ok), null);
                Timber.e(e);
            }
        }
    }

    public static void openUrlInBrowser(@NonNull Context context, String url) {
        Uri webPage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webPage);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }
}
