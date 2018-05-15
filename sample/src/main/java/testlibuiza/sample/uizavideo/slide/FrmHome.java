package testlibuiza.sample.uizavideo.slide;

/**
 * Created by www.muathu@gmail.com on 12/24/2017.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import testlibuiza.R;
import vn.loitp.core.base.BaseFragment;
import vn.loitp.core.utilities.LLog;
import vn.loitp.uizavideo.view.IOnBackPressed;

public class FrmHome extends BaseFragment implements IOnBackPressed {
    private final String TAG = getClass().getSimpleName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frm_home, container, false);

        view.findViewById(R.id.bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TestUizaVideoIMActivityRlSlide) getActivity()).play();
            }
        });
        view.findViewById(R.id.bt_switch_screen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TestUizaVideoIMActivityRlSlide) getActivity()).replaceFragment(new FrmLogin());
            }
        });
        return view;
    }

    @Override
    public boolean onBackPressed() {
        LLog.d(TAG, "onBackPressed");
        return false;
    }
}
