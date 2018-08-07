package vn.loitp.uizavideov3.view.dlg.playlistfolder;

import vn.loitp.restapi.uiza.model.v2.listallentity.Item;

/**
 * Created by loitp on 3/30/2018.
 */

public interface CallbackPlaylistFolder {
    public void onClickItem(Item item, int position);

    public void onDismiss();
}
