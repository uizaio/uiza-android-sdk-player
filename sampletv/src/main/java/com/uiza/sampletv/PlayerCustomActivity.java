package com.uiza.sampletv;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;
import com.daimajia.androidanimations.library.Techniques;
import io.uiza.core.api.response.linkplay.LinkPlay;
import io.uiza.core.api.response.video.VideoData;
import io.uiza.core.exception.UzException;
import io.uiza.core.util.LLog;
import io.uiza.core.util.UzAnimationUtil;
import io.uiza.core.util.UzDialogUtil;
import io.uiza.core.view.snappysmoothscroller.SnapType;
import io.uiza.core.view.snappysmoothscroller.SnappyLinearLayoutManager;
import io.uiza.player.UzPlayer;
import io.uiza.player.UzPlayerConfig;
import io.uiza.player.interfaces.UzItemClickListener;
import io.uiza.player.interfaces.UzPlayerEventListener;
import io.uiza.player.interfaces.UzPlayerTvListener;
import io.uiza.player.view.UzPlayerView;
import java.util.ArrayList;
import java.util.List;

public class PlayerCustomActivity extends AppCompatActivity implements UzPlayerEventListener,
        UzPlayerTvListener, UzItemClickListener {

    private final String TAG = getClass().getSimpleName();
    private Activity activity;
    private UzPlayer uzPlayer;
    private RelativeLayout rl;
    private RecyclerView recyclerView;
    private AdapterDummy adapterDummy;
    private List<Dummy> dummyList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        UzPlayerConfig.setCasty(this);
        activity = this;
        UzPlayerConfig.setCurrentSkinRes(R.layout.uz_player_skin_tv_custom);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_custom);
        rl = findViewById(R.id.rl);
        recyclerView = findViewById(R.id.recycler_view);
        uzPlayer = findViewById(R.id.uiza_video);
        uzPlayer.setUzPlayerEventListener(this);
        uzPlayer.setUzPlayerTvListener(this);
        uzPlayer.setUzItemClickListener(this);
        uzPlayer.setDefaultUseController(false);
        uzPlayer.setOnTouchEvent(new UzPlayerView.OnTouchEvent() {
            @Override
            public void onSingleTapConfirmed(float x, float y) {
            }

            @Override
            public void onLongPress(float x, float y) {
            }

            @Override
            public void onDoubleTap(float x, float y) {
            }

            @Override
            public void onSwipeRight() {
            }

            @Override
            public void onSwipeLeft() {
            }

            @Override
            public void onSwipeBottom() {
                UzAnimationUtil.play(rl, Techniques.SlideOutDown, new UzAnimationUtil.Callback() {
                    @Override
                    public void onCancel() {
                    }

                    @Override
                    public void onEnd() {
                        rl.setVisibility(View.GONE);
                    }

                    @Override
                    public void onRepeat() {
                    }

                    @Override
                    public void onStart() {
                    }
                });
            }

            @Override
            public void onSwipeTop() {
                rl.setVisibility(View.VISIBLE);
                UzAnimationUtil.play(rl, Techniques.SlideInUp);
                recyclerView.requestFocus();
            }
        });
        UzPlayerConfig.initVodEntity(uzPlayer, LSApplication.entityIdDefaultVOD);
        setupUI();
        setupData();
    }

    @Override
    public void onDataInitialized(boolean initSuccess, boolean getDataSuccess, LinkPlay linkPlay,
            VideoData data) {

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
    public void onPlayerError(UzException exception) {
        exception.printStackTrace();
        UzDialogUtil.showDialog1(activity, exception.getMessage(), new UzDialogUtil.Callback1() {
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
    public void onItemClick(View view) {
        switch (view.getId()) {
            case R.id.exo_back_screen:
                if (!uzPlayer.isLandscape()) {
                    onBackPressed();
                }
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uzPlayer.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        uzPlayer.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        uzPlayer.onPause();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        uzPlayer.onActivityResult(resultCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        //LLog.d(TAG, "onKeyUp " + keyCode);
        switch (keyCode) {
            case KeyEvent.KEYCODE_MEDIA_REWIND:
                LLog.d(TAG, "onKeyUp KEYCODE_MEDIA_REWIND");
                uzPlayer.seekToBackward(uzPlayer.getDefaultValueBackwardForward());
                return true;
            case KeyEvent.KEYCODE_MEDIA_FAST_FORWARD:
                LLog.d(TAG, "onKeyUp KEYCODE_MEDIA_FAST_FORWARD");
                uzPlayer.seekToForward(uzPlayer.getDefaultValueBackwardForward());
                return true;
            case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
                LLog.d(TAG, "onKeyUp KEYCODE_MEDIA_PLAY_PAUSE");
                uzPlayer.togglePlayPause();
                return true;
            case KeyEvent.KEYCODE_MEDIA_STOP:
                LLog.d(TAG, "onKeyUp KEYCODE_MEDIA_STOP");
                uzPlayer.pause();
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
                uzPlayer.toggleShowHideController();
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
                uzPlayer.showController();
                return true;
            default:
                return super.onKeyUp(keyCode, event);
        }
    }

    @Override
    public void onFocusChange(View view, boolean isFocus) {
        uzPlayer.updateUiFocusChanged(view, isFocus);
    }

    private void setupUI() {
        SnappyLinearLayoutManager layoutManager = new SnappyLinearLayoutManager(activity,
                LinearLayoutManager.HORIZONTAL, false);
        layoutManager.setSnapType(SnapType.CENTER);
        layoutManager.setSnapInterpolator(new DecelerateInterpolator());
        recyclerView.setLayoutManager(layoutManager);

        adapterDummy = new AdapterDummy(activity, dummyList, new AdapterDummy.Callback() {
            @Override
            public void onClickItem(Dummy dummy, int position) {
            }

            @Override
            public void onFocusChange(Dummy dummy, int position) {
                if (recyclerView != null) {
                    recyclerView.smoothScrollToPosition(position);
                }
            }
        });
        recyclerView.setAdapter(adapterDummy);

        final int currentPositionOfDataList = 0;
        recyclerView.postDelayed(() -> {
            if (recyclerView == null
                    || recyclerView.findViewHolderForAdapterPosition(currentPositionOfDataList)
                    == null || recyclerView
                    .findViewHolderForAdapterPosition(currentPositionOfDataList).itemView
                    == null) {
                return;
            }
            recyclerView.findViewHolderForAdapterPosition(currentPositionOfDataList).itemView
                    .requestFocus();
        }, 50);
    }

    private void setupData() {
        for (int i = 0; i < 30; i++) {
            Dummy dummy = new Dummy();
            dummy.setName("Name " + i);
            dummy.setUrl("https://kenh14cdn.com/2018/9/2/irene1-15359057199821852847485.jpg");
            dummyList.add(dummy);
        }
        adapterDummy.notifyDataSetChanged();
    }
}
