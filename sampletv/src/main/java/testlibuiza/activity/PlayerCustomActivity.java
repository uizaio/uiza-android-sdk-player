package testlibuiza.activity;

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
import java.util.ArrayList;
import java.util.List;
import testlibuiza.app.LSApplication;
import testlibuiza.app.R;
import uizacoresdk.interfaces.UZItemClickListener;
import uizacoresdk.interfaces.UZPlayerStateChangedListener;
import uizacoresdk.interfaces.UZVideoStateChangedListener;
import uizacoresdk.interfaces.UZVideoTVListener;
import uizacoresdk.util.UZUtil;
import uizacoresdk.view.UZPlayerView;
import uizacoresdk.view.rl.video.UZVideo;
import vn.uiza.core.exception.UZException;
import vn.uiza.core.utilities.LAnimationUtil;
import vn.uiza.core.utilities.LDialogUtil;
import vn.uiza.core.utilities.LLog;
import vn.uiza.restapi.uiza.model.v3.linkplay.getlinkplay.ResultGetLinkPlay;
import vn.uiza.restapi.uiza.model.v3.metadata.getdetailofmetadata.Data;
import vn.uiza.views.recyclerview.snappysmoothscroller.SnapType;
import vn.uiza.views.recyclerview.snappysmoothscroller.SnappyLinearLayoutManager;

public class PlayerCustomActivity extends AppCompatActivity implements UZVideoStateChangedListener,
        UZPlayerStateChangedListener, UZVideoTVListener, UZItemClickListener {
    private final String TAG = getClass().getSimpleName();
    private Activity activity;
    private UZVideo uzVideo;
    private RelativeLayout rl;
    private RecyclerView recyclerView;
    private AdapterDummy adapterDummy;
    private List<Dummy> dummyList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        UZUtil.setCasty(this);
        activity = this;
        //UZUtil.setCurrentPlayerId(R.layout.uz_player_skin_1);
        UZUtil.setCurrentPlayerId(R.layout.uz_player_skin_tv_custom);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_custom);
        rl = findViewById(R.id.rl);
        recyclerView = findViewById(R.id.recycler_view);
        uzVideo = findViewById(R.id.uiza_video);
        uzVideo.setUzVideoStateChangedListener(this);
        uzVideo.setUzPlayerStateChangedListener(this);
        uzVideo.setUzVideoTVListener(this);
        uzVideo.setUzItemClickListener(this);
        uzVideo.setDefaultUseController(false);
        uzVideo.addOnTouchEvent(new UZPlayerView.OnTouchEvent() {
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
                LAnimationUtil.play(rl, Techniques.SlideOutDown, new LAnimationUtil.Callback() {
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
                LAnimationUtil.play(rl, Techniques.SlideInUp);
                recyclerView.requestFocus();
            }
        });
        UZUtil.initEntity(activity, uzVideo, LSApplication.entityIdDefaultVOD);
        setupUI();
        setupData();
    }

    @Override
    public void isInitResult(boolean isInitSuccess, boolean isGetDataSuccess, ResultGetLinkPlay resultGetLinkPlay, Data data) {
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
    public void onStateMiniPlayer(boolean isInitMiniPlayerSuccess) {
    }

    @Override
    public void onSkinChanged() {
    }

    @Override
    public void onScreenRotated(boolean isLandscape) {
    }

    @Override
    public void onError(UZException e) {
        e.printStackTrace();
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
        //LLog.d(TAG, "onKeyUp " + keyCode);
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
    public void onFocusChanged(View view, boolean isFocus) {
        uzVideo.updateUIFocusChange(view, isFocus);
    }

    private void setupUI() {
        //recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));

        SnappyLinearLayoutManager layoutManager = new SnappyLinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
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
        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (recyclerView == null || recyclerView.findViewHolderForAdapterPosition(currentPositionOfDataList) == null || recyclerView.findViewHolderForAdapterPosition(currentPositionOfDataList).itemView == null) {
                    return;
                }
                recyclerView.findViewHolderForAdapterPosition(currentPositionOfDataList).itemView.requestFocus();
            }
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
