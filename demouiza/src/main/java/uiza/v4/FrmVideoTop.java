package uiza.v4;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import io.uiza.core.api.response.linkplay.LinkPlay;
import io.uiza.core.api.response.video.VideoData;
import io.uiza.core.exception.UzException;
import io.uiza.core.util.UzCommonUtil;
import io.uiza.player.UzPlayer;
import io.uiza.player.UzPlayerConfig;
import io.uiza.player.interfaces.UzItemClickListener;
import io.uiza.player.interfaces.UzPlayerEventListener;
import io.uiza.player.interfaces.UzPlayerUiEventListener;
import uiza.R;

public class FrmVideoTop extends Fragment implements UzPlayerEventListener, UzPlayerUiEventListener,
        UzItemClickListener {

    private UzPlayer uzPlayer;

    public UzPlayer getUZVideo() {
        return uzPlayer;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        uzPlayer = view.findViewById(R.id.uiza_video);
        uzPlayer.setUzPlayerEventListener(this);
        uzPlayer.setUzPlayerUiEventListener(this);
        uzPlayer.setUzItemClickListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.v4_frm_top, container, false);
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

    private void setListener() {
        if (uzPlayer == null || uzPlayer.getExoPlayer() == null) {
            return;
        }
        uzPlayer.setControllerStateCallback(isShow -> {
            if (((HomeV4CanSlideActivity) getActivity()).getDraggablePanel() != null
                    && !((HomeV4CanSlideActivity) getActivity()).isLandscapeScreen()) {
                if (((HomeV4CanSlideActivity) getActivity()).getDraggablePanel()
                        .isMaximized()) {
                    if (isShow) {
                        ((HomeV4CanSlideActivity) getActivity()).getDraggablePanel()
                                .setEnableSlide(false);
                    } else {
                        ((HomeV4CanSlideActivity) getActivity()).getDraggablePanel()
                                .setEnableSlide(true);
                    }
                } else {
                    ((HomeV4CanSlideActivity) getActivity()).getDraggablePanel()
                            .setEnableSlide(true);
                }
            }
        });
    }

    @Override
    public void onDataInitialized(boolean initSuccess, boolean getDataSuccess, LinkPlay linkPlay,
            VideoData data) {
        ((HomeV4CanSlideActivity) getActivity()).isInitResult(getDataSuccess, linkPlay, data);
        if (initSuccess) {
            setListener();
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
    public void onPlayerError(UzException exception) {
        if (exception == null) {
            return;
        }
        Toast.makeText(getContext(), "Error while playing this video !", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSkinChanged() {

    }

    @Override
    public void onPlayerRotated(boolean isLandscape) {

    }

    @Override
    public void onItemClick(View view) {
        switch (view.getId()) {
            case R.id.exo_back_screen:
                if (!uzPlayer.isLandscape()) {
                    ((HomeV4CanSlideActivity) getActivity()).getDraggablePanel().minimize();
                }
                break;
        }
    }

    @Override
    public void onStateMiniPlayer(boolean isInitMiniPlayerSuccess) {
        if (isInitMiniPlayerSuccess) {
            uzPlayer.pause();
            ((HomeV4CanSlideActivity) getActivity()).getDraggablePanel().minimize();
            UzCommonUtil.actionWithDelayed(500,
                    mls -> ((HomeV4CanSlideActivity) getActivity()).getDraggablePanel()
                            .closeToRight());
        }
    }


    public void initEntity(String entityId) {
        UzPlayerConfig.initVodEntity(uzPlayer, entityId);
    }

    public void initPlaylistFolder(String metadataId) {
        UzPlayerConfig.initPlaylistFolder(uzPlayer, metadataId);
    }
}
