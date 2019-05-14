package vn.uiza.core.utilities;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import vn.uiza.R;
import vn.uiza.core.common.Constants;
import vn.uiza.core.exception.UZException;
import vn.uiza.utils.util.AppUtils;
import vn.uiza.utils.util.SentryUtils;
import vn.uiza.views.LToast;


/**
 * File created on 11/14/2016.
 *
 * @author loitp
 */
public class LSocialUtil {
    private static String TAG = LSocialUtil.class.getSimpleName();
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

    public static void rateApp(Activity activity, String packageName) {
        try {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(MARKET_URI + packageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(
                    PLAY_STORE_DETAIL_URI + packageName)));
            SentryUtils.captureException(anfe);
        }
    }

    public static void moreApp(Activity activity, String devName) {
        String uri = PLAY_STORE_DEV_URI + devName;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        activity.startActivity(intent);
    }

    public static void share(Activity activity, String msg) {
        try {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType(Constants.TEXT_TYPE);
            intent.putExtra(Intent.EXTRA_SUBJECT, AppUtils.getAppName());
            intent.putExtra(Intent.EXTRA_TEXT, msg);
            activity.startActivity(Intent.createChooser(intent, SHARE_TITLE));
        } catch (Exception e) {
            SentryUtils.captureException(e);
        }
    }

    public static void sharingToSocialMedia(Activity activity, String application, String subject, String message) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType(Constants.TEXT_TYPE);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, message);
        boolean installed = checkAppInstall(activity, application);
        if (installed) {
            intent.setPackage(application);
            activity.startActivity(intent);
        } else {
            LToast.show(activity, activity.getString(R.string.can_not_find_share_app));
        }
    }

    private static boolean checkAppInstall(Activity activity, String uri) {
        PackageManager pm = activity.getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            SentryUtils.captureException(e);
        }

        return false;
    }

    public static void likeFacebookFanpage(Activity activity, String fbFanPageUrl, String fbFanPageId) {
        if (fbFanPageUrl == null && fbFanPageId == null) {
            throw new IllegalArgumentException("You must provide FanPageURL or FanPageId");
        }
        Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
        String facebookUrl = getFacebookPageURL(activity, fbFanPageUrl, fbFanPageId);
        facebookIntent.setData(Uri.parse(facebookUrl));
        activity.startActivity(facebookIntent);
    }

    private static String getFacebookPageURL(Context context, String fbFanPageUrl, String fbFanPageId) {
        PackageManager packageManager = context.getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo(FB_PACKAGE, 0).versionCode;
            if (versionCode >= 3002850) {
                return FB_WEB_MODEL_URI + fbFanPageUrl;
            } else {
                return FB_PAGE_URI + fbFanPageId;
            }
        } catch (PackageManager.NameNotFoundException e) {
            SentryUtils.captureException(e);
            if (fbFanPageUrl != null)
                return fbFanPageUrl;
            else return fbFanPageId;
        }
    }

    public static void chatMessenger(Activity activity, String messengerId) {
        PackageManager packageManager = activity.getPackageManager();
        boolean isFBInstalled = false;
        try {
            int versionCode = packageManager.getPackageInfo(FB_MESSENGER_PACKAGE, 0).versionCode;
            if (versionCode >= 0) isFBInstalled = true;
        } catch (PackageManager.NameNotFoundException e) {
            SentryUtils.captureException(e);
        }
        if (!isFBInstalled) {
            LDialogUtil.showDialog1(activity, activity.getString(R.string.error), UZException.ERR_22, activity.getString(R.string.ok), null);
        } else {
            Uri uri = Uri.parse(FB_MESSENGER_URI);
            uri = ContentUris.withAppendedId(uri, Long.valueOf(messengerId));
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            try {
                activity.startActivity(intent);
            } catch (Exception e) {
                LDialogUtil.showDialog1(activity, UZException.ERR_20, UZException.ERR_22, activity.getString(R.string.ok), null);
                SentryUtils.captureException(e);
            }
        }
    }

    public static void openUrlInBrowser(Context context, String url) {
        Uri webPage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webPage);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }
}
