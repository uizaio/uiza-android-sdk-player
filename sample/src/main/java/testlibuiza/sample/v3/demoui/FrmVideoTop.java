package testlibuiza.sample.v3.demoui;

/**
 * Created by www.muathu@gmail.com on 27/2/2019.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.exoplayer2.video.VideoListener;
import io.uiza.core.api.response.linkplay.LinkPlay;
import io.uiza.core.api.response.video.VideoData;
import io.uiza.core.exception.UzException;
import io.uiza.core.util.UzDisplayUtil;
import io.uiza.core.util.constant.Constants;
import testlibuiza.R;
import uizacoresdk.interfaces.UZCallback;
import uizacoresdk.interfaces.UZItemClick;
import uizacoresdk.util.UZUtil;
import uizacoresdk.view.UZPlayerView;
import uizacoresdk.view.rl.video.UZVideo;

public class FrmVideoTop extends Fragment implements UZCallback, UZItemClick, UZPlayerView.ControllerStateCallback {
    private final String TAG = getClass().getSimpleName();
    private UZVideo uzVideo;

    public UZVideo getUZVideo() {
        return uzVideo;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        uzVideo = (UZVideo) view.findViewById(R.id.uiza_video);
        uzVideo.setAutoSwitchItemPlaylistFolder(false);
        uzVideo.addUZCallback(this);
        uzVideo.addItemClick(this);
        uzVideo.addControllerStateCallback(this);
        uzVideo.addVideoListener(new VideoListener() {
            @Override
            public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
                calSize(width, height);
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.v4_frm_top, container, false);
    }

    @Override
    public void onDestroy() {
        uzVideo.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onResume() {
        uzVideo.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        uzVideo.onPause();
        super.onPause();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        uzVideo.onActivityResult(resultCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void isInitResult(boolean isInitSuccess, boolean isGetDataSuccess, LinkPlay linkPlay, VideoData data) {
        ((HomeCanSlideActivity) getActivity()).isInitResult(isGetDataSuccess, linkPlay, data);
    }

    @Override
    public void onItemClick(View view) {
        switch (view.getId()) {
            case R.id.exo_back_screen:
                if (!uzVideo.isLandscape()) {
                    ((HomeCanSlideActivity) getActivity()).getDraggablePanel().minimize();
                }
                break;
        }
    }

    @Override
    public void onStateMiniPlayer(boolean isInitMiniPlayerSuccess) {
        if (isInitMiniPlayerSuccess) {
            uzVideo.pauseVideo();
            ((HomeCanSlideActivity) getActivity()).getDraggablePanel().minimize();
            UzDisplayUtil.setDelay(500, new UzDisplayUtil.DelayCallback() {
                @Override
                public void doAfter(int mls) {
                    ((HomeCanSlideActivity) getActivity()).getDraggablePanel().closeToRight();
                }
            });
        }
    }

    @Override
    public void onSkinChange() {
    }

    public boolean isLandscape;

    @Override
    public void onScreenRotate(boolean isLandscape) {
        this.isLandscape = isLandscape;
        if (isLandscape) {
            ((HomeCanSlideActivity) getActivity()).getDraggablePanel().setEnableSlide(false);
        } else {
            ((HomeCanSlideActivity) getActivity()).getDraggablePanel().setEnableSlide(true);
        }
        calSize(uzVideo.getVideoW(), uzVideo.getVideoH());
    }

    @Override
    public void onError(UzException e) {
    }

    public void initEntity(String entityId) {
        int w = UzDisplayUtil.getScreenWidth();
        int h = (int) (w * Constants.RATIO_9_16);
        resizeView(w, h);
        if (uzVideo != null) {
            uzVideo.pauseVideo();
        }
        UZUtil.initEntity(getActivity(), uzVideo, entityId);
    }

    public void initPlaylistFolder(String metadataId) {
        int w = UzDisplayUtil.getScreenWidth();
        int h = (int) (w * Constants.RATIO_9_16);
        resizeView(w, h);
        if (uzVideo != null) {
            uzVideo.pauseVideo();
        }
        UZUtil.initPlaylistFolder(getActivity(), uzVideo, metadataId);
    }

    private void calSize(int width, int height) {
        int screenW;
        int screenH;
        if (isLandscape) {
            screenW = UzDisplayUtil.getScreenWidth();
            screenH = UzDisplayUtil.getScreenHeight();
        } else {
            if (width >= height) {
                screenW = UzDisplayUtil.getScreenWidth();
                screenH = height * screenW / width;
            } else {
                screenW = UzDisplayUtil.getScreenWidth();
                screenH = screenW;
            }
        }
        resizeView(screenW, screenH);
    }

    private void resizeView(int w, int h) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((HomeCanSlideActivity) getActivity()).setTopViewHeightApllyNow(h);
            }
        });
        /*uzVideo.post(new Runnable() {
            @Override
            public void run() {
                uzVideo.setSize(w, h);
            }
        });*/
    }

    @Override
    public void onVisibilityChange(boolean isShow) {
        if (((HomeCanSlideActivity) getActivity()).getDraggablePanel() != null && !isLandscape) {
            if (((HomeCanSlideActivity) getActivity()).getDraggablePanel().isMaximized()) {
                if (isShow) {
                    ((HomeCanSlideActivity) getActivity()).getDraggablePanel().setEnableSlide(false);
                } else {
                    ((HomeCanSlideActivity) getActivity()).getDraggablePanel().setEnableSlide(true);
                }
            } else {
                ((HomeCanSlideActivity) getActivity()).getDraggablePanel().setEnableSlide(true);
            }
        }
    }
}
