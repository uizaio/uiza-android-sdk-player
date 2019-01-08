package testlibuiza.sample.v3.demoui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import testlibuiza.R;
import uizacoresdk.interfaces.IOnBackPressed;

public class FrmLogin extends Fragment implements IOnBackPressed {
    private final String TAG = getClass().getSimpleName();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.v4_frm_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeCanSlideActivity) getActivity()).replaceFragment(new FrmUser());
            }
        });
    }

    @Override
    public boolean onBackPressed() {
        //LLog.d(TAG, "onBackPressed " + TAG);
        return false;
    }
}
