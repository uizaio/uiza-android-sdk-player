package testlibuiza.sample.v3.demoui;

/**
 * Created by www.muathu@gmail.com on 12/24/2017.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.google.android.exoplayer2.video.VideoListener;

import testlibuiza.R;
import uizacoresdk.interfaces.UZCallback;
import uizacoresdk.interfaces.UZItemClick;
import uizacoresdk.util.UZUtil;
import uizacoresdk.view.UZPlayerView;
import uizacoresdk.view.rl.video.UZVideo;
import vn.uiza.core.base.BaseFragment;
import vn.uiza.core.common.Constants;
import vn.uiza.core.exception.UZException;
import vn.uiza.core.utilities.LScreenUtil;
import vn.uiza.core.utilities.LUIUtil;
import vn.uiza.restapi.uiza.model.v3.linkplay.getlinkplay.ResultGetLinkPlay;
import vn.uiza.restapi.uiza.model.v3.metadata.getdetailofmetadata.Data;
import vn.uiza.views.LToast;

public class FrmVideoTop extends BaseFragment implements UZCallback, UZItemClick {
    private UZVideo uzVideo;

    public UZVideo getUZVideo() {
        return uzVideo;
    }

    @Override
    protected String setTag() {
        return "TAG" + getClass().getSimpleName();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        uzVideo = (UZVideo) view.findViewById(R.id.uiza_video);
        uzVideo.setAutoSwitchItemPlaylistFolder(false);
        uzVideo.addUZCallback(this);
        uzVideo.addVideoListener(new VideoListener() {
            @Override
            public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
                calSize(width, height);
            }
        });
        uzVideo.addItemClick(this);
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.v4_frm_top;
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
                LToast.show(getActivity(), "Draw over other app permission not available");
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void setListener() {
        if (uzVideo == null || uzVideo.getPlayer() == null) {
            return;
        }
        uzVideo.addControllerStateCallback(new UZPlayerView.ControllerStateCallback() {
            @Override
            public void onVisibilityChange(boolean isShow) {
                if (((HomeCanSlideActivity) getActivity()).getDraggablePanel() != null
                        && !((HomeCanSlideActivity) getActivity()).isLandscapeScreen()) {
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
        });
    }

    @Override
    public void isInitResult(boolean isInitSuccess, boolean isGetDataSuccess, ResultGetLinkPlay resultGetLinkPlay, Data data) {
        ((HomeCanSlideActivity) getActivity()).isInitResult(isGetDataSuccess, resultGetLinkPlay, data);
        if (isInitSuccess) {
            setListener();
        }
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
    public void onClickPipVideoInitSuccess(boolean isInitSuccess) {
        if (isInitSuccess) {
            uzVideo.pauseVideo();
            ((HomeCanSlideActivity) getActivity()).getDraggablePanel().minimize();
            LUIUtil.setDelay(500, new LUIUtil.DelayCallback() {
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

    @Override
    public void onScreenRotate(boolean isLandscape) {
        //LLog.d(TAG, "onScreenRotate isLandscape " + isLandscape);
        if (!isLandscape) {
            int width = uzVideo.getVideoW();
            int height = uzVideo.getVideoH();
            //LLog.d(TAG, "onScreenRotate " + width + "x" + height);
            calSize(width, height);
        }
    }

    @Override
    public void onError(UZException e) {
    }

    public void initEntity(String entityId) {
        int w = LScreenUtil.getScreenWidth();
        int h = (int) (w * Constants.RATIO_9_16);
        resizeView(w, h);
        UZUtil.initEntity(getActivity(), uzVideo, entityId);
    }

    public void initPlaylistFolder(String metadataId) {
        int w = LScreenUtil.getScreenWidth();
        int h = (int) (w * Constants.RATIO_9_16);
        resizeView(w, h);
        UZUtil.initPlaylistFolder(getActivity(), uzVideo, metadataId);
    }

    private void calSize(int width, int height) {
        if (width >= height) {
            int screenW = LScreenUtil.getScreenWidth();
            int screenH = height * screenW / width;
            //LLog.d(TAG, "calSize >=: " + screenW + "x" + screenH);
            resizeView(screenW, screenH);
        } else {
            int screenW = LScreenUtil.getScreenWidth();
            int screenH = screenW;
            //LLog.d(TAG, "calSize <: " + screenW + "x" + screenH);
            resizeView(screenW, screenH);
        }
    }

    private void resizeView(int w, int h) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((HomeCanSlideActivity) getActivity()).setTopViewHeightApllyNow(h);
            }
        });
        uzVideo.post(new Runnable() {
            @Override
            public void run() {
                uzVideo.setSize(w, h);
            }
        });
    }
}
