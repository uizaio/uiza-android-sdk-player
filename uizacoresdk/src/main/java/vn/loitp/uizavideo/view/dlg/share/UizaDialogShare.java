package vn.loitp.uizavideo.view.dlg.share;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ScrollView;

import java.util.List;

import loitp.core.R;
import vn.loitp.core.utilities.LDeviceUtil;
import vn.loitp.core.utilities.LScreenUtil;
import vn.loitp.core.utilities.LSocialUtil;
import vn.loitp.core.utilities.LUIUtil;
import vn.loitp.uizavideo.view.util.UizaData;
import vn.loitp.views.layout.flowlayout.FlowLayout;

/**
 * Created by LENOVO on 5/2/2018.
 */

public class UizaDialogShare extends Dialog {
    private final String TAG = getClass().getSimpleName();
    private Activity activity;
    private AlertDialog dialog;
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
        ll.setRowSpacing(20f);

        findViewById(R.id.bt_exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        ScrollView scrollView = (ScrollView) findViewById(R.id.scroll_view);
        LUIUtil.setPullLikeIOSVertical(scrollView);

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

        List<ResolveInfo> resolveInfoList;
        if (UizaData.getInstance().getResolveInfoList() == null) {
            //LLog.d(TAG, "queryIntentActivities");
            Intent template = new Intent(Intent.ACTION_SEND);
            template.setType("text/plain");
            resolveInfoList = activity.getPackageManager().queryIntentActivities(template, 0);
            UizaData.getInstance().setResolveInfoList(resolveInfoList);
        } else {
            //LLog.d(TAG, "!queryIntentActivities");
            resolveInfoList = UizaData.getInstance().getResolveInfoList();
        }

        //LLog.d(TAG, "resolveInfoList size: " + resolveInfoList.size());
        for (final ResolveInfo resolveInfo : resolveInfoList) {
            //LLog.d(TAG, "resolveInfo.activityInfo loadLabel -> " + resolveInfo.loadLabel(activity.getPackageManager()));
            //LLog.d(TAG, "resolveInfo.activityInfo.packageName -> " + resolveInfo.activityInfo.packageName);

            ImageView imageView = new ImageView(activity);
            imageView.setImageDrawable(resolveInfo.loadIcon(activity.getPackageManager()));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setPadding(sizeIv / 10, sizeIv / 10, sizeIv / 10, sizeIv / 10);
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
        String pkgName = resolveInfo.activityInfo.packageName;
        String label = (String) resolveInfo.loadLabel(activity.getPackageManager());
        //LLog.d(TAG, "click resolveInfo.activityInfo loadLabel -> " + label);
        //LLog.d(TAG, "click resolveInfo.activityInfo.packageName -> " + pkgName);
        if (pkgName.equals("com.google.android.apps.docs") && label.toLowerCase().contains("clipboard")) {
            LDeviceUtil.setClipboard(activity, MESSAGE);
        } else if (pkgName.equals("com.facebook.katana")) {
            //TODO fb not work
            LSocialUtil.sharingToSocialMedia(activity, resolveInfo.activityInfo.packageName, SUBJECT, MESSAGE);
        } else {
            LSocialUtil.sharingToSocialMedia(activity, resolveInfo.activityInfo.packageName, SUBJECT, MESSAGE);
        }
        dismiss();
    }
}