package uiza.v4;

/**
 * Created by www.muathu@gmail.com on 12/24/2017.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import uiza.R;
import uizacoresdk.interfaces.UZCallback;
import uizacoresdk.interfaces.UZItemClick;
import uizacoresdk.util.UZUtil;
import uizacoresdk.view.UZPlayerView;
import uizacoresdk.view.rl.video.UZVideo;
import vn.uiza.core.base.BaseFragment;
import vn.uiza.core.exception.UZException;
import vn.uiza.core.utilities.LUIUtil;
import vn.uiza.restapi.uiza.model.v3.linkplay.getlinkplay.ResultGetLinkPlay;
import vn.uiza.restapi.uiza.model.v3.metadata.getdetailofmetadata.Data;

public class FrmVideoTop extends BaseFragment implements UZCallback, UZItemClick {
    private UZVideo uzVideo;

    public UZVideo getUZVideo() {
        return uzVideo;
    }

    @Override
    protected String setTag() {
        return getClass().getSimpleName();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        uzVideo = (UZVideo) view.findViewById(R.id.uiza_video);
        uzVideo.addUZCallback(this);
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
        uzVideo.onActivityResult(resultCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setListener() {
        if (uzVideo == null || uzVideo.getPlayer() == null) {
            return;
        }
        uzVideo.addControllerStateCallback(new UZPlayerView.ControllerStateCallback() {
            @Override
            public void onVisibilityChange(boolean isShow) {
                if (((HomeV4CanSlideActivity) getActivity()).getDraggablePanel() != null
                        && !((HomeV4CanSlideActivity) getActivity()).isLandscapeScreen()) {
                    if (((HomeV4CanSlideActivity) getActivity()).getDraggablePanel().isMaximized()) {
                        if (isShow) {
                            ((HomeV4CanSlideActivity) getActivity()).getDraggablePanel().setEnableSlide(false);
                        } else {
                            ((HomeV4CanSlideActivity) getActivity()).getDraggablePanel().setEnableSlide(true);
                        }
                    } else {
                        ((HomeV4CanSlideActivity) getActivity()).getDraggablePanel().setEnableSlide(true);
                    }
                }
            }
        });
    }

    @Override
    public void isInitResult(boolean isInitSuccess, boolean isGetDataSuccess, ResultGetLinkPlay resultGetLinkPlay, Data data) {
        ((HomeV4CanSlideActivity) getActivity()).isInitResult(isGetDataSuccess, resultGetLinkPlay, data);
        if (isInitSuccess) {
            setListener();
            uzVideo.setEventBusMsgFromActivityIsInitSuccess();
        }
    }

    @Override
    public void onItemClick(View view) {
        switch (view.getId()) {
            case R.id.exo_back_screen:
                if (!uzVideo.isLandscape()) {
                    ((HomeV4CanSlideActivity) getActivity()).getDraggablePanel().minimize();
                }
                break;
        }
    }

    @Override
    public void onClickPipVideoInitSuccess(boolean isInitSuccess) {
        if (isInitSuccess) {
            uzVideo.pauseVideo();
            ((HomeV4CanSlideActivity) getActivity()).getDraggablePanel().minimize();
            LUIUtil.setDelay(500, new LUIUtil.DelayCallback() {
                @Override
                public void doAfter(int mls) {
                    ((HomeV4CanSlideActivity) getActivity()).getDraggablePanel().closeToRight();
                }
            });
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
        ((HomeV4CanSlideActivity) getActivity()).getDraggablePanel().minimize();
        LUIUtil.setDelay(500, new LUIUtil.DelayCallback() {
            @Override
            public void doAfter(int mls) {
                ((HomeV4CanSlideActivity) getActivity()).getDraggablePanel().closeToRight();
            }
        });
    }

    public void initEntity(String entityId) {
        UZUtil.initEntity(getActivity(), uzVideo, entityId);
    }

    public void initPlaylistFolder(String metadataId) {
        UZUtil.initPlaylistFolder(getActivity(), uzVideo, metadataId);
    }
}
