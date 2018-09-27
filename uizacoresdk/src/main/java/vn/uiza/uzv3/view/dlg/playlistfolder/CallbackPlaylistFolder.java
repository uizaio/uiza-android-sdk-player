package vn.uiza.uzv3.view.dlg.playlistfolder;

import vn.uiza.restapi.uiza.model.v3.metadata.getdetailofmetadata.Data;

/**
 * Created by loitp on 3/30/2018.
 */

public interface CallbackPlaylistFolder {
    public void onClickItem(Data data, int position);

    public void onFocusChange(Data data, int position);

    public void onDismiss();
}
