package testlibuiza.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import com.daimajia.androidanimations.library.Techniques;
import io.uiza.core.api.response.linkplay.LinkPlay;
import io.uiza.core.api.response.video.VideoData;
import io.uiza.core.exception.UzException;
import io.uiza.core.util.LLog;
import io.uiza.core.util.UzAnimationUtil;
import io.uiza.core.util.UzDisplayUtil;
import io.uiza.core.util.constant.Constants;
import io.uiza.core.view.autosize.UzImageButton;
import java.util.List;
import testlibuiza.app.R;
import uizacoresdk.interfaces.UZCallback;
import uizacoresdk.interfaces.UZItemClick;
import uizacoresdk.interfaces.UZTVCallback;
import uizacoresdk.util.UZUtil;
import uizacoresdk.view.UZPlayerView;
import uizacoresdk.view.dlg.hq.UZItem;
import uizacoresdk.view.rl.video.UZVideo;

public class PlayerActivity extends AppCompatActivity implements UZCallback, UZTVCallback, UZPlayerView.ControllerStateCallback, UZItemClick {
    private final String TAG = getClass().getSimpleName();
    private Activity activity;
    private UZVideo uzVideo;
    private UzImageButton uzibCustomHq;
    private UzImageButton uzibCustomAudio;
    private ScrollView sv;
    private LinearLayout llListHq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activity = this;
        UZUtil.setCasty(this);

        //init skin
        UZUtil.setCurrentPlayerId(R.layout.uz_player_skin_tv_custom);
        //UZUtil.setCurrentPlayerId(R.layout.uz_player_skin_tv_0);
        //UZUtil.setCurrentPlayerId(R.layout.uz_player_skin_0);
        //UZUtil.setCurrentPlayerId(R.layout.uz_player_skin_1);
        //UZUtil.setCurrentPlayerId(R.layout.uz_player_skin_2);
        //UZUtil.setCurrentPlayerId(R.layout.uz_player_skin_3);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        sv = (ScrollView) findViewById(R.id.sv);
        llListHq = (LinearLayout) findViewById(R.id.ll_list_hq);
        uzVideo = (UZVideo) findViewById(R.id.uiza_video);
        uzibCustomHq = (UzImageButton) uzVideo.findViewById(R.id.uzib_custom_hq);
        uzibCustomAudio = (UzImageButton) uzVideo.findViewById(R.id.uzib_custom_audio);

        uzibCustomHq.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                uzVideo.updateUIFocusChange(view, b);
            }
        });
        uzibCustomAudio.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                uzVideo.updateUIFocusChange(view, b);
            }
        });

        uzVideo.addUZCallback(this);
        uzVideo.addUZTVCallback(this);
        uzVideo.addControllerStateCallback(this);
        uzVideo.addItemClick(this);

        String entityId = getIntent().getStringExtra(Constants.KEY_UIZA_ENTITY_ID);
        if (entityId == null) {
            String metadataId = getIntent().getStringExtra(Constants.KEY_UIZA_METADATA_ENTITY_ID);
            UZUtil.initPlaylistFolder(activity, uzVideo, metadataId);
        } else {
            UZUtil.initEntity(activity, uzVideo, entityId);
        }

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

    @Override
    public void isInitResult(boolean isInitSuccess, boolean isGetDataSuccess, LinkPlay linkPlay, VideoData data) {
        /*if (isInitSuccess) {
            uzVideo.setEventBusMsgFromActivityIsInitSuccess();
        }*/
    }

    @Override
    public void onItemClick(View view) {
        switch (view.getId()) {
            case R.id.exo_back_screen:
                if (!uzVideo.isLandscape()) {
                    onBackPressed();
                }
                break;
        }
    }

    @Override
    public void onStateMiniPlayer(boolean isInitMiniPlayerSuccess) {
    }

    @Override
    public void onSkinChange() {
    }

    @Override
    public void onScreenRotate(boolean isLandscape) {
    }

    @Override
    public void onError(UzException e) {
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
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_MEDIA_REWIND:
                LLog.d(TAG, "onKeyUp KEYCODE_MEDIA_REWIND");
                uzVideo.seekToBackward(uzVideo.getDefaultValueBackwardForward());
                return true;
            case KeyEvent.KEYCODE_MEDIA_FAST_FORWARD:
                LLog.d(TAG, "onKeyUp KEYCODE_MEDIA_FAST_FORWARD");
                uzVideo.seekToForward(uzVideo.getDefaultValueBackwardForward());
                return true;
            case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
                LLog.d(TAG, "onKeyUp KEYCODE_MEDIA_PLAY_PAUSE");
                uzVideo.togglePlayPause();
                return true;
            case KeyEvent.KEYCODE_MEDIA_STOP:
                LLog.d(TAG, "onKeyUp KEYCODE_MEDIA_STOP");
                uzVideo.pauseVideo();
                return true;
            case KeyEvent.KEYCODE_BACK:
                LLog.d(TAG, "onKeyUp KEYCODE_BACK");
                onBackPressed();
                return true;
            case KeyEvent.KEYCODE_VOLUME_UP:
                LLog.d(TAG, "onKeyUp KEYCODE_VOLUME_UP");
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                LLog.d(TAG, "onKeyUp KEYCODE_VOLUME_DOWN");
                return true;
            case KeyEvent.KEYCODE_MENU:
                LLog.d(TAG, "onKeyUp KEYCODE_MENU");
                uzVideo.toggleShowHideController();
                return true;
            case KeyEvent.KEYCODE_DPAD_UP:
                LLog.d(TAG, "onKeyUp KEYCODE_DPAD_UP");
                return true;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                LLog.d(TAG, "onKeyUp KEYCODE_DPAD_LEFT");
                return true;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                LLog.d(TAG, "onKeyUp KEYCODE_DPAD_RIGHT");
                return true;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                LLog.d(TAG, "onKeyUp KEYCODE_DPAD_DOWN");
                return true;
            case KeyEvent.KEYCODE_DPAD_CENTER:
                LLog.d(TAG, "onKeyUp KEYCODE_DPAD_CENTER");
                uzVideo.showController();
                return true;
            default:
                return super.onKeyUp(keyCode, event);
        }
    }

    @Override
    public void onFocusChange(View view, boolean isFocus) {
        //LLog.d(TAG, "onFocusChange isFocus " + isFocus);
        /*if (isFocus) {
            if (view == uzVideo.getIbBackScreenIcon()) {
                LLog.d(TAG, "onFocusChange ibSettingIcon");
            } else if (view == uzVideo.getIbSettingIcon()) {
                LLog.d(TAG, "onFocusChange ibSettingIcon");
            } else if (view == uzVideo.getIbCcIcon()) {
                LLog.d(TAG, "onFocusChange ibCcIcon");
            } else if (view == uzVideo.getIbPlaylistRelationIcon()) {
                LLog.d(TAG, "onFocusChange ibPlaylistRelationIcon");
            } else if (view == uzVideo.getIbRewIcon()) {
                LLog.d(TAG, "onFocusChange ibRewIcon");
            } else if (view == uzVideo.getIbPlayIcon()) {
                LLog.d(TAG, "onFocusChange ibPlayIcon");
            } else if (view == uzVideo.getIbPauseIcon()) {
                LLog.d(TAG, "onFocusChange ibPauseIcon");
            } else if (view == uzVideo.getIbReplayIcon()) {
                LLog.d(TAG, "onFocusChange ibReplayIcon");
            } else if (view == uzVideo.getIbSkipNextIcon()) {
                LLog.d(TAG, "onFocusChange ibSkipNextIcon");
            } else if (view == uzVideo.getIbSkipPreviousIcon()) {
                LLog.d(TAG, "onFocusChange ibSkipPreviousIcon");
            } else if (view == uzVideo.getUZTimeBar()) {
                LLog.d(TAG, "onFocusChange uzTimebar");
            }
        }*/
        uzVideo.updateUIFocusChange(view, isFocus);
    }

    private void displayUI(List<UZItem> uzItemList) {
        sv.setVisibility(View.VISIBLE);
        llListHq.removeAllViews();
        for (int i = 0; i < uzItemList.size(); i++) {
            UZItem uzItem = uzItemList.get(i);
            final CheckedTextView c = uzItem.getCheckedTextView();

            //customize here
            final Button bt = new Button(activity);
            bt.setAllCaps(false);
            bt.setFocusable(true);
            bt.setSoundEffectsEnabled(true);
            if (c.isChecked()) {
                bt.setText(c.getText().toString() + " -> format: " + uzItem.getFormat().getFormat() + ", getProfile: " + uzItem.getFormat().getProfile() + " (✔)");
                bt.setBackgroundColor(Color.GREEN);
                bt.requestFocus();
            } else {
                bt.setText(c.getText().toString() + " -> format: " + uzItem.getFormat().getFormat() + ", getProfile: " + uzItem.getFormat().getProfile());
                bt.setBackgroundColor(Color.WHITE);
            }
            bt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if (b) {
                        bt.setBackgroundColor(Color.GREEN);
                        uzVideo.showController();
                    } else {
                        bt.setBackgroundColor(Color.WHITE);
                    }
                }
            });
            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UzAnimationUtil.play(view, Techniques.Pulse);
                    c.performClick();
                    UzDisplayUtil.setDelay(300, new UzDisplayUtil.DelayCallback() {
                        @Override
                        public void doAfter(int mls) {
                            llListHq.removeAllViews();
                            llListHq.invalidate();
                            sv.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            });
            llListHq.addView(bt);
        }
        llListHq.invalidate();
    }

    @Override
    public void onBackPressed() {
        if (sv.getVisibility() == View.VISIBLE) {
            sv.setVisibility(View.INVISIBLE);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onVisibilityChange(boolean isShow) {
        if (!isShow) {
            if (sv.getVisibility() == View.VISIBLE) {
                sv.setVisibility(View.INVISIBLE);
            }
        }
    }
}
