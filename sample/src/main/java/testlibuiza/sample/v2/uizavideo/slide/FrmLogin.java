package testlibuiza.sample.v2.uizavideo.slide;

/**
 * Created by www.muathu@gmail.com on 12/24/2017.
 */

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import testlibuiza.R;
import vn.loitp.core.base.BaseFragment;
import vn.loitp.uzv1.view.IOnBackPressed;

public class FrmLogin extends BaseFragment implements IOnBackPressed {
    private final String TAG = getClass().getSimpleName();

    @Override
    protected String setTag() {
        return null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        frmRootView.findViewById(R.id.bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((V2UizaVideoIMActivitySlide) getActivity()).replaceFragment(new FrmUser());
            }
        });
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.v2_frm_login;
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }
}
