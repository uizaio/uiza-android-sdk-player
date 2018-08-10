package uiza.v4;

/**
 * Created by www.muathu@gmail.com on 12/24/2017.
 */

import uiza.R;
import vn.loitp.core.base.BaseFragment;
import vn.loitp.uizavideo.view.IOnBackPressed;

public class FrmVideoBottom extends BaseFragment implements IOnBackPressed {

    @Override
    protected int setLayoutResourceId() {
        return R.layout.v4_frm_bottom;
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }
}
