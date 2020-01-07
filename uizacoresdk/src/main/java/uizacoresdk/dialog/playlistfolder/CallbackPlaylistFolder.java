package uizacoresdk.dialog.playlistfolder;


import vn.uiza.models.PlaybackInfo;

/**
 * Created by loitp on 3/30/2018.
 */

public interface CallbackPlaylistFolder {
    void onClickItem(PlaybackInfo playback, int position);

    void onFocusChange(PlaybackInfo playback, int position);

    void onDismiss();
}
