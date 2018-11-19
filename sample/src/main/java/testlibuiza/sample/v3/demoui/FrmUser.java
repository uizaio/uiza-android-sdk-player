package testlibuiza.sample.v3.demoui;

/**
 * Created by www.muathu@gmail.com on 12/24/2017.
 */

import testlibuiza.R;
import uizacoresdk.interfaces.IOnBackPressed;
import vn.uiza.core.base.BaseFragment;

public class FrmUser extends BaseFragment implements IOnBackPressed {

    @Override
    protected String setTag() {
        return getClass().getSimpleName();
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.v4_frm_user;
    }

    @Override
    public boolean onBackPressed() {
        //LLog.d(TAG, "onBackPressed " + TAG);
        return false;
    }
}
