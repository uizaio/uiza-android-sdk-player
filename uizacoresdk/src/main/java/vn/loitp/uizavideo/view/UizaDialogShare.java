package vn.loitp.uizavideo.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import java.util.List;

import loitp.core.R;
import vn.loitp.core.utilities.LDeviceUtil;
import vn.loitp.core.utilities.LLog;
import vn.loitp.core.utilities.LScreenUtil;
import vn.loitp.core.utilities.LSocialUtil;
import vn.loitp.views.layout.flowlayout.FlowLayout;

/**
 * Created by LENOVO on 5/2/2018.
 */

public class UizaDialogShare extends Dialog {
    private final String TAG = getClass().getSimpleName();
    private Activity activity;
    private Dialog dialog;
    private FlowLayout ll;
    //TODO change this
    public static final String SUBJECT = "Uiza Sharing";
    public static final String MESSAGE = "https://play.google.com/store/apps/details?id=io.uiza.app";
    private boolean isLandscape;

    public UizaDialogShare(Activity activity, boolean isLandscape) {
        super(activity);
        this.activity = activity;
        this.isLandscape = isLandscape;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_share);

        ll = (FlowLayout) findViewById(R.id.ll);
        ll.setChildSpacing(FlowLayout.SPACING_AUTO);
        ll.setChildSpacingForLastRow(FlowLayout.SPACING_ALIGN);
        ll.setRowSpacing(10f);
        genUI();
    }

    private void genUI() {
        int screenW = LScreenUtil.getScreenWidth();
        int sizeIv;
        if (isLandscape) {
            sizeIv = screenW / 12;
        } else {
            sizeIv = screenW / 7;
        }
        Intent template = new Intent(Intent.ACTION_SEND);
        template.setType("text/plain");
        List<ResolveInfo> resolveInfoList = activity.getPackageManager().queryIntentActivities(template, 0);

        //LLog.d(TAG, "resolveInfoList size: " + resolveInfoList.size());
        for (final ResolveInfo resolveInfo : resolveInfoList) {
            //LLog.d(TAG, "resolveInfo.activityInfo loadLabel -> " + resolveInfo.loadLabel(activity.getPackageManager()));
            //LLog.d(TAG, "resolveInfo.activityInfo.packageName -> " + resolveInfo.activityInfo.packageName);

            ImageView imageView = new ImageView(activity);
            imageView.setImageDrawable(resolveInfo.loadIcon(activity.getPackageManager()));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    click(resolveInfo);
                }
            });
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(sizeIv, sizeIv);
            imageView.setLayoutParams(layoutParams);
            ll.addView(imageView);
        }
    }

    private void click(ResolveInfo resolveInfo) {
        LLog.d(TAG, "click resolveInfo.activityInfo loadLabel -> " + resolveInfo.loadLabel(activity.getPackageManager()));
        LLog.d(TAG, "click resolveInfo.activityInfo.packageName -> " + resolveInfo.activityInfo.packageName);
        String pkgName = resolveInfo.activityInfo.packageName;
        if (pkgName.equals("com.google.android.apps.docs")) {
            LDeviceUtil.setClipboard(activity, MESSAGE);
        } else if (pkgName.equals("com.facebook.katana")) {
            //TODO fb not work
            LSocialUtil.sharingToSocialMedia(activity, resolveInfo.activityInfo.packageName, SUBJECT, MESSAGE);
        } else {
            LSocialUtil.sharingToSocialMedia(activity, resolveInfo.activityInfo.packageName, SUBJECT, MESSAGE);
        }
        dismiss();
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