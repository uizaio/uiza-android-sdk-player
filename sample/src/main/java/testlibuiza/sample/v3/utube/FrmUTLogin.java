package testlibuiza.sample.v3.utube;

/**
 * Created by www.muathu@gmail.com on 12/24/2017.
 */

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import testlibuiza.R;
import uizacoresdk.interfaces.IOnBackPressed;
import vn.uiza.core.base.BaseFragment;

public class FrmUTLogin extends BaseFragment implements IOnBackPressed {

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
                ((CustomSkinCodeUZTimebarUTubeWithSlideActivity) getActivity()).replaceFragment(new FrmUTUser());
            }
        });
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.v4_frm_login;
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }
}
