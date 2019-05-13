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
    private static final String FB_MESSENGER_TEST_ID = "947139732073591";
    private static final String PLAY_STORE_TEST_DEV = "NgonTinh KangKang";
    private static final String SHARE_TITLE = "Share via";

    /*
     * rate app
     * @param packageName: the packageName
     */
    public static void rateApp(Activity activity, String packageName) {
        try {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(MARKET_URI + packageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(
                    PLAY_STORE_DETAIL_URI + packageName)));
            SentryUtils.captureException(anfe);
        }
    }

    public static void moreApp(Activity activity) {
        String uri = PLAY_STORE_DEV_URI + PLAY_STORE_TEST_DEV;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        activity.startActivity(intent);
    }

    public static void share(Activity activity, boolean isLandscape, String msg) {
        try {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType(Constants.TEXT_TYPE);
            intent.putExtra(Intent.EXTRA_SUBJECT, AppUtils.getAppName());
            intent.putExtra(Intent.EXTRA_TEXT, msg);
            activity.startActivity(Intent.createChooser(intent, SHARE_TITLE));
        } catch (Exception e) {
            LLog.d(TAG, "Exception shareApp: " + e.toString());
            SentryUtils.captureException(e);
        }
    }

    public static void share(Activity activity, boolean isLandscape) {
        /*UZDlgShare uzDlgShare = new UZDlgShare(activity, isLandscape);
        UZUtil.showUizaDialog(activity, uzDlgShare);*/
    }

    public static void sharingToSocialMedia(Activity activity, String application, String subject, String message) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType(Constants.TEXT_TYPE);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, message);
        boolean installed = checkAppInstall(activity, application);
        LLog.d(TAG, "share sharingToSocialMedia installed " + installed);
        if (installed) {
            intent.setPackage(application);
            activity.startActivity(intent);
        } else {
            LToast.show(activity, "Không tìm thấy ứng dụng này trên thiết bị của bạn.");
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

    /*public static void shareViaFb(final Activity activity) {
        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, UZDlgShare.SUBJECT);
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, UZDlgShare.MESSAGE);
        PackageManager pm = activity.getPackageManager();
        List<ResolveInfo> activityList = pm.queryIntentActivities(shareIntent, 0);
        for (final ResolveInfo app : activityList) {
            if ((app.activityInfo.name).contains("facebook")) {
                final ActivityInfo activityInfo = app.activityInfo;
                final ComponentName name = new ComponentName(activityInfo.applicationInfo.packageName, activityInfo.name);
                shareIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                shareIntent.setComponent(name);
                activity.startActivity(shareIntent);
                break;
            }
        }
    }*/

    //like fanpage
    public static void likeFacebookFanpage(Activity activity) {
        Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
        String facebookUrl = getFacebookPageURL(activity);
        facebookIntent.setData(Uri.parse(facebookUrl));
        activity.startActivity(facebookIntent);
    }

    /*
    get url fb fanpage
     */
    private static String getFacebookPageURL(Context context) {
        String FACEBOOK_URL = "https://www.facebook.com/hoidammedocsach";
        String FACEBOOK_PAGE_ID = "hoidammedocsach";
        PackageManager packageManager = context.getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo(FB_PACKAGE, 0).versionCode;
            if (versionCode >= 3002850) {
                return FB_WEB_MODEL_URI + FACEBOOK_URL;
            } else {
                return FB_PAGE_URI + FACEBOOK_PAGE_ID;
            }
        } catch (PackageManager.NameNotFoundException e) {
            SentryUtils.captureException(e);
            return FACEBOOK_URL;
        }
    }

    /*
    chat with fanpage Thugiannao
     */
    public static void chatMessenger(Activity activity) {
        PackageManager packageManager = activity.getPackageManager();
        boolean isFBInstalled = false;
        try {
            int versionCode = packageManager.getPackageInfo(FB_MESSENGER_PACKAGE, 0).versionCode;
            if (versionCode >= 0) isFBInstalled = true;
        } catch (PackageManager.NameNotFoundException e) {
            LLog.d(TAG, "packageManager com.facebook.orca: " + e.toString());
            SentryUtils.captureException(e);
        }
        if (!isFBInstalled) {
            LDialogUtil.showDialog1(activity, "Error", UZException.ERR_22, activity.getString(R.string.ok), null);
        } else {
            Uri uri = Uri.parse(FB_MESSENGER_URI);
            uri = ContentUris.withAppendedId(uri, Long.valueOf(FB_MESSENGER_TEST_ID));
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            try {
                activity.startActivity(intent);
            } catch (Exception e) {
                LDialogUtil.showDialog1(activity, UZException.ERR_20, UZException.ERR_22, activity.getString(R.string.ok), null);
                SentryUtils.captureException(e);
            }
        }
    }

    /*
     * send email support
     */
    /*public void sendEmail() {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{context.getResources().getString(R.string.myEmailDev)});
        i.putExtra(Intent.EXTRA_SUBJECT, context.getResources().getString(R.string.mail_subject_support));
        i.putExtra(Intent.EXTRA_TEXT, context.getResources().getString(R.string.mail_text_support));
        try {
            context.startActivity(Intent.createChooser(i, context.getString(R.string.send_mail_via)));
        } catch (android.content.ActivityNotFoundException ex) {

        }
    }*/

    public static void openUrlInBrowser(Context context, String url) {
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }
}
