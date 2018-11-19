package testlibuiza.sample.v3.customhq;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;

import com.daimajia.androidanimations.library.Techniques;

import java.util.List;

import testlibuiza.R;
import testlibuiza.app.LSApplication;
import uizacoresdk.interfaces.UZCallback;
import uizacoresdk.util.UZUtil;
import uizacoresdk.view.dlg.hq.UZItem;
import uizacoresdk.view.rl.video.UZVideo;
import vn.uiza.core.base.BaseActivity;
import vn.uiza.core.common.Constants;
import vn.uiza.core.exception.UZException;
import vn.uiza.core.utilities.LAnimationUtil;
import vn.uiza.core.utilities.LDialogUtil;
import vn.uiza.core.utilities.LLog;
import vn.uiza.core.utilities.LScreenUtil;
import vn.uiza.core.utilities.LUIUtil;
import vn.uiza.restapi.uiza.model.v2.listallentity.Item;
import vn.uiza.restapi.uiza.model.v3.linkplay.getlinkplay.ResultGetLinkPlay;
import vn.uiza.restapi.uiza.model.v3.metadata.getdetailofmetadata.Data;
import vn.uiza.views.LToast;
import vn.uiza.views.autosize.UZImageButton;

/**
 * Created by loitp on 7/16/2018.
 */

public class CustomHQActivity extends BaseActivity implements UZCallback {
    private UZVideo uzVideo;
    private UZImageButton uzibCustomHq;
    private UZImageButton uzibCustomAudio;
    private LinearLayout llListHq;

    @Override
    protected boolean setFullScreen() {
        return false;
    }

    @Override
    protected String setTag() {
        return "TAG" + getClass().getSimpleName();
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.activity_uiza_custom_hq;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        UZUtil.setCasty(this);
        UZUtil.setCurrentPlayerId(R.layout.uiza_controller_hq_custom_main);
        super.onCreate(savedInstanceState);
        uzVideo = (UZVideo) findViewById(R.id.uiza_video);
        uzibCustomHq = (UZImageButton) uzVideo.findViewById(R.id.uzib_custom_hq);
        uzibCustomAudio = (UZImageButton) uzVideo.findViewById(R.id.uzib_custom_audio);
        llListHq = (LinearLayout) findViewById(R.id.ll_list_hq);
        uzVideo.setControllerShowTimeoutMs(5000);
        uzVideo.addUZCallback(this);

        final String entityId = LSApplication.entityIdDefaultVOD;
        UZUtil.initEntity(activity, uzVideo, entityId);
        uzibCustomHq.setOnClickListener(new View.OnClickListener() {
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
    public void onStart() {
        super.onStart();
        uzVideo.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        uzVideo.onStop();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.CODE_DRAW_OVER_OTHER_APP_PERMISSION) {
            if (resultCode == Activity.RESULT_OK) {
                uzVideo.initializePiP();
            } else {
                LToast.show(activity, "Draw over other app permission not available");
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void isInitResult(boolean isInitSuccess, boolean isGetDataSuccess, ResultGetLinkPlay resultGetLinkPlay, Data data) {
    }

    @Override
    public void onClickListEntityRelation(Item item, int position) {
    }

    @Override
    public void onClickBack() {
        onBackPressed();
    }

    @Override
    public void onClickPip(Intent intent) {
    }

    @Override
    public void onClickPipVideoInitSuccess(boolean isInitSuccess) {
        if (isInitSuccess) {
            onBackPressed();
        }
    }

    @Override
    public void onSkinChange() {
    }

    @Override
    public void onScreenRotate(boolean isLandscape) {
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
}
