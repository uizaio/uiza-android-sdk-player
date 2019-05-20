package uizacoresdk.view.dlg.playlistfolder;

import vn.uiza.restapi.uiza.model.v3.metadata.getdetailofmetadata.Data;

/**
 * Created by loitp on 3/30/2018.
 */

public interface CallbackPlaylistFolder {
    void onClickItem(Data data, int position);

    void onFocusChange(Data data, int position);

    void onDismiss();
}
