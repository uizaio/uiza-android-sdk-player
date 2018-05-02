package vn.loitp.uizavideo.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.obf.ac;

import java.util.ArrayList;
import java.util.List;

import loitp.core.R;
import vn.loitp.core.utilities.LLog;
import vn.loitp.core.utilities.LSocialUtil;

/**
 * Created by LENOVO on 5/2/2018.
 */

public class UizaDialogShare extends Dialog {
    private final String TAG = getClass().getSimpleName();
    private Activity activity;
    private Dialog dialog;
    private LinearLayout ll;
    public static final String SUBJECT = "Uiza SUBJECT";
    public static final String MESSAGE = "Uiza MESSAGE";

    public UizaDialogShare(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_share);

        ll = (LinearLayout) findViewById(R.id.ll);
        genUI();
    }

    private void genUI() {
        Intent template = new Intent(Intent.ACTION_SEND);
        template.setType("text/plain");
        List<ResolveInfo> resolveInfoList = activity.getPackageManager().queryIntentActivities(template, 0);

        LLog.d(TAG, "resolveInfoList size: " + resolveInfoList.size());
        for (ResolveInfo resolveInfo : resolveInfoList) {
            LLog.d(TAG, "resolveInfo.activityInfo loadLabel -> " + resolveInfo.loadLabel(activity.getPackageManager()));
            LLog.d(TAG, "resolveInfo.activityInfo.packageName -> " + resolveInfo.activityInfo.packageName);

            ImageView imageView = new ImageView(activity);
            imageView.setImageDrawable(resolveInfo.loadIcon(activity.getPackageManager()));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(100, 100);
            imageView.setLayoutParams(layoutParams);
            ll.addView(imageView);
        }
    }

    /*@Override
    public void onClick(View v) {
        if (v == btFb) {
            onClickFb();
        } else if (v == btInstagram) {
            onClickInstagram();
        } else if (v == btTwiter) {
            onClickTwiter();
        } else if (v == bt_pinterest) {
            onClickPinterest();
        }
        dismiss();
    }

    private void onClickFb() {
        //TODO fb not work
        LSocialUtil.sharingToSocialMedia(activity, "com.facebook.katana", SUBJECT, MESSAGE);
    }

    private void onClickInstagram() {
        LSocialUtil.sharingToSocialMedia(activity, "com.instagram.android", SUBJECT, MESSAGE);
    }

    private void onClickTwiter() {
        LSocialUtil.sharingToSocialMedia(activity, "com.twitter.android", SUBJECT, MESSAGE);
    }

    private void onClickPinterest() {
        LSocialUtil.sharingToSocialMedia(activity, "com.pinterest", SUBJECT, MESSAGE);
    }*/
}