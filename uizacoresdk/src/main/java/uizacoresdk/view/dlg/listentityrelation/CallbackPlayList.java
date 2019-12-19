package uizacoresdk.view.dlg.listentityrelation;

import vn.uiza.restapi.model.v2.listallentity.Item;

/**
 * Created by LENOVO on 3/30/2018.
 */

public interface CallbackPlayList {
    void onClickItem(Item item, int position);

    void onDismiss();
}
