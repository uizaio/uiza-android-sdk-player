package uiza.v4;

/**
 * Created by www.muathu@gmail.com on 12/24/2017.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import uiza.R;
import uizacoresdk.interfaces.UZItemClickListener;
import uizacoresdk.interfaces.UZPlayerStateChangedListener;
import uizacoresdk.interfaces.UZVideoStateChangedListener;
import uizacoresdk.util.UZUtil;
import uizacoresdk.view.UZPlayerView;
import uizacoresdk.view.rl.video.UZVideo;
import vn.uiza.core.exception.UZException;
import vn.uiza.core.utilities.LUIUtil;
import vn.uiza.restapi.uiza.model.v3.linkplay.getlinkplay.ResultGetLinkPlay;
import vn.uiza.restapi.uiza.model.v3.metadata.getdetailofmetadata.Data;

public class FrmVideoTop extends Fragment implements UZVideoStateChangedListener,
        UZPlayerStateChangedListener, UZItemClickListener {
    private final String TAG = getClass().getSimpleName();
    private UZVideo uzVideo;

    public UZVideo getUZVideo() {
        return uzVideo;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        uzVideo = view.findViewById(R.id.uiza_video);
        uzVideo.setUzVideoStateChangedListener(this);
        uzVideo.setUzPlayerStateChangedListener(this);
        uzVideo.setUzItemClickListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.v4_frm_top, container, false);
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
            //uzVideo.setEventBusMsgFromActivityIsInitSuccess();
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
    public void onStateMiniPlayer(boolean isInitMiniPlayerSuccess) {
        if (isInitMiniPlayerSuccess) {
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
    public void onScreenRotated(boolean isLandscape) {
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
