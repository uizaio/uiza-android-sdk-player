package vn.loitp.uizavideo.view;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import loitp.core.R;
import vn.loitp.core.utilities.LSocialUtil;

/**
 * Created by LENOVO on 5/2/2018.
 */

public class UizaDialogShare extends Dialog implements android.view.View.OnClickListener {
    private Activity activity;
    private Dialog dialog;

    public static final String SUBJECT = "Uiza SUBJECT";
    public static final String MESSAGE = "Uiza MESSAGE";

    private ImageView btFb;
    private ImageView btInstagram;
    private ImageView btTwiter;

    public UizaDialogShare(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_share);
        btFb = (ImageView) findViewById(R.id.bt_fb);
        btInstagram = (ImageView) findViewById(R.id.bt_instagram);
        btTwiter = (ImageView) findViewById(R.id.bt_twiter);

        btFb.setOnClickListener(this);
        btInstagram.setOnClickListener(this);
        btTwiter.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btFb) {
            onClickFb();
        } else if (v == btInstagram) {
            onClickInstagram();
        } else if (v == btTwiter) {
            onClickTwiter();
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
}