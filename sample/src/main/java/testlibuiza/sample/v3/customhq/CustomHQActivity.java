package testlibuiza.sample.v3.customhq;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;

import com.daimajia.androidanimations.library.Techniques;
import com.google.android.exoplayer2.video.VideoListener;

import java.util.List;

import testlibuiza.R;
import testlibuiza.app.LSApplication;
import uizacoresdk.interfaces.UZCallback;
import uizacoresdk.interfaces.UZItemClick;
import uizacoresdk.interfaces.UZItemClickListener;
import uizacoresdk.interfaces.UZPlayerStateChangedListener;
import uizacoresdk.interfaces.UZVideoStateChangedListener;
import uizacoresdk.util.UZUtil;
import uizacoresdk.view.dlg.hq.UZItem;
import uizacoresdk.view.rl.video.UZVideo;
import vn.uiza.core.exception.UZException;
import vn.uiza.core.utilities.LAnimationUtil;
import vn.uiza.core.utilities.LDialogUtil;
import vn.uiza.core.utilities.LLog;
import vn.uiza.core.utilities.LScreenUtil;
import vn.uiza.core.utilities.LUIUtil;
import vn.uiza.restapi.uiza.model.v3.linkplay.getlinkplay.ResultGetLinkPlay;
import vn.uiza.restapi.uiza.model.v3.metadata.getdetailofmetadata.Data;
import vn.uiza.views.autosize.UZImageButton;

/**
 * Created by loitp on 12/10/2018.
 */

public class CustomHQActivity extends AppCompatActivity implements         UZItemClickListener,
        UZVideoStateChangedListener, UZPlayerStateChangedListener {
    private Activity activity;
    private UZVideo uzVideo;
    private Button btCustomHq;
    private UZImageButton uzibCustomAudio;
    private LinearLayout llListHq;
    private final String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        activity = this;
        UZUtil.setCasty(this);
        UZUtil.setCurrentPlayerId(R.layout.uiza_controller_hq_custom_main);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uiza_custom_hq);
        uzVideo = findViewById(R.id.uiza_video);
        btCustomHq = uzVideo.findViewById(R.id.uzib_custom_hq);
        uzibCustomAudio = uzVideo.findViewById(R.id.uzib_custom_audio);
        llListHq = findViewById(R.id.ll_list_hq);
        uzVideo.setUzVideoStateChangedListener(this);
        uzVideo.setUzPlayerStateChangedListener(this);
        uzVideo.setUzItemClickListener(this);
        uzVideo.addVideoListener(new VideoListener() {
            @Override
            public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
                btCustomHq.setText("Auto " + width + "x" + height + " - " + UZUtil.getFormatVideo(width, height).getProfile());
            }
        });

        final String entityId = LSApplication.entityIdDefaultVOD;
        UZUtil.initEntity(activity, uzVideo, entityId);
        btCustomHq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayUI(uzVideo.getHQList());
            }
        });
        uzibCustomAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayUI(uzVideo.getAudioList());
            }
        });
    }

    private void displayUI(List<UZItem> uzItemList) {
        llListHq.removeAllViews();
        for (int i = 0; i < uzItemList.size(); i++) {
            UZItem uzItem = uzItemList.get(i);
            final CheckedTextView c = uzItem.getCheckedTextView();
            LLog.d(TAG, i + ", getDescription: " + uzItem.getDescription() + ", isChecked: " + c.isChecked() + ", getFormat: " + uzItem.getFormat());

            //add space
            View view = new View(activity);
            view.setBackgroundColor(Color.WHITE);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(200, 5);
            view.setLayoutParams(layoutParams);
            llListHq.addView(view);

            //customize here
            final Button bt = new Button(activity);
            bt.setAllCaps(false);
            if (c.isChecked()) {
                bt.setText(c.getText().toString() + " -> format: " + uzItem.getFormat().getFormat() + ", getProfile: " + uzItem.getFormat().getProfile() + " (✔)");
                bt.setBackgroundColor(Color.GREEN);
            } else {
                bt.setText(c.getText().toString() + " -> format: " + uzItem.getFormat().getFormat() + ", getProfile: " + uzItem.getFormat().getProfile());
                bt.setBackgroundColor(Color.WHITE);
            }
            bt.setSoundEffectsEnabled(true);
            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LAnimationUtil.play(view, Techniques.Pulse);
                    c.performClick();
                    LUIUtil.setDelay(300, new LUIUtil.DelayCallback() {
                        @Override
                        public void doAfter(int mls) {
                            llListHq.removeAllViews();
                            llListHq.invalidate();
                        }
                    });
                }
            });
            llListHq.addView(bt);
        }
        llListHq.invalidate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uzVideo.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        uzVideo.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        uzVideo.onPause();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        uzVideo.onActivityResult(resultCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

    }

    @Override
    public void onVideoProgress(long currentMls, int s, long duration, int percent) {

    }

    @Override
    public void onBufferProgress(long bufferedPosition, int bufferedPercentage, long duration) {

    }

    @Override
    public void onVideoEnded() {

    }

    @Override
    public void onSkinChanged() {

    }

    @Override
    public void onStateMiniPlayer(boolean isInitMiniPlayerSuccess) {
        if (isInitMiniPlayerSuccess) {
            onBackPressed();
        }
    }

    @Override
    public void isInitResult(boolean isInitSuccess, boolean isGetDataSuccess,
            ResultGetLinkPlay resultGetLinkPlay, Data data) {

    }

    @Override
    public void onScreenRotated(boolean isLandscape) {

    }

    @Override
    public void onError(UZException e) {
        if (e == null) {
            return;
        }
        LDialogUtil.showDialog1(activity, e.getMessage(), new LDialogUtil.Callback1() {
            @Override
            public void onClick1() {
                onBackPressed();
            }

            @Override
            public void onCancel() {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (LScreenUtil.isFullScreen(activity)) {
            uzVideo.toggleFullscreen();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onItemClick(View view) {
        if (view.getId() == R.id.exo_back_screen) {
            if (!uzVideo.isLandscape()) {
                onBackPressed();
            }
        }
    }
}
