package uizacoresdk.view.dlg.playlistfolder;

import io.uiza.core.api.response.video.VideoData;

/**
 * Created by loitp on 3/30/2018.
 */

public interface CallbackPlaylistFolder {
    void onClickItem(VideoData data, int position);

    void onFocusChange(VideoData data, int position);

    void onDismiss();
}
