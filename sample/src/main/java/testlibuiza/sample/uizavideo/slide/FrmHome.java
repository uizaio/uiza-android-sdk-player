package testlibuiza.sample.uizavideo.slide;

/**
 * Created by www.muathu@gmail.com on 12/24/2017.
 */

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import testlibuiza.R;
import vn.loitp.core.base.BaseFragment;
import vn.loitp.core.utilities.LLog;
import vn.loitp.uizavideo.view.IOnBackPressed;

public class FrmHome extends BaseFragment implements IOnBackPressed {
    private final String TAG = getClass().getSimpleName();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        frmRootView.findViewById(R.id.bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TestUizaVideoIMActivityRlSlide) getActivity()).play();
            }
        });
        frmRootView.findViewById(R.id.bt_switch_screen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TestUizaVideoIMActivityRlSlide) getActivity()).replaceFragment(new FrmLogin());
            }
        });
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.frm_home;
    }

    @Override
    public boolean onBackPressed() {
        LLog.d(TAG, "onBackPressed");
        return false;
    }
}
