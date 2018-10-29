package testlibuiza.sample.v3.demoui;

/**
 * Created by www.muathu@gmail.com on 12/24/2017.
 */

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import testlibuiza.R;
import vn.uiza.core.base.BaseFragment;
import vn.uiza.core.utilities.LLog;
import uizacoresdk.view.IOnBackPressed;

public class FrmLogin extends BaseFragment implements IOnBackPressed {

    @Override
    protected String setTag() {
        return getClass().getSimpleName();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        frmRootView.findViewById(R.id.bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeCanSlideActivity) getActivity()).replaceFragment(new FrmUser());
            }
        });
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.v4_frm_login;
    }

    @Override
    public boolean onBackPressed() {
        //LLog.d(TAG, "onBackPressed " + TAG);
        return false;
    }
}
