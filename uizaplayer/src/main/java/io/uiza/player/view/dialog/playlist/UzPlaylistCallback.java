package io.uiza.player.view.dialog.playlist;

import io.uiza.core.api.response.video.VideoData;

public interface UzPlaylistCallback {
    void onClickItem(VideoData data, int position);

    void onFocusChange(VideoData data, int position);

    void onDismiss();
}
