package uiza.v4.home;

/**
 * Created by www.muathu@gmail.com on 12/24/2017.
 */

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import uiza.R;
import vn.loitp.core.base.BaseFragment;
import vn.loitp.uizavideo.view.IOnBackPressed;

public class FrmHome extends BaseFragment implements IOnBackPressed {

    @Override
    protected String setTag() {
        return "Home";
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.v4_frm_home;
    }

    private void login() {
        //TODO login later
    }

    @Override
    public boolean onBackPressed() {
        //return ((HomeV4CanSlideActivity) getActivity()).handleOnbackpressFrm();
        return false;
    }
}