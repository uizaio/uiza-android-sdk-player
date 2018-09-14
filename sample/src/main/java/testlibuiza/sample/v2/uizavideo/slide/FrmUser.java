package testlibuiza.sample.v2.uizavideo.slide;

/**
 * Created by www.muathu@gmail.com on 12/24/2017.
 */

import testlibuiza.R;
import vn.loitp.core.base.BaseFragment;
import vn.loitp.uzv1.view.IOnBackPressed;

public class FrmUser extends BaseFragment implements IOnBackPressed {
    @Override
    protected String setTag() {
        return null;
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.v2_frm_user;
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }
}
