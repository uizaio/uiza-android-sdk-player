package vn.uiza.uzv3.view.dlg.listentityrelation;

import vn.uiza.restapi.uiza.model.v2.listallentity.Item;

/**
 * Created by LENOVO on 3/30/2018.
 */

public interface CallbackPlayList {
    public void onClickItem(Item item, int position);

    public void onDismiss();
}
