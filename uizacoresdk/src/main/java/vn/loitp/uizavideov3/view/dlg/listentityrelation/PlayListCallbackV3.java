package vn.loitp.uizavideov3.view.dlg.listentityrelation;

import vn.loitp.restapi.uiza.model.v2.listallentity.Item;

/**
 * Created by LENOVO on 3/30/2018.
 */

public interface PlayListCallbackV3 {
    public void onClickItem(Item item, int position);

    public void onDismiss();
}
