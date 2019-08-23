package com.uiza.sample.screen.demoui;

/**
 * Created by www.muathu@gmail.com on 9/1/2019.
 */

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.uiza.sample.R;
import io.uiza.player.interfaces.IOnBackPressed;

public class FrmLogin extends Fragment implements IOnBackPressed {

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.v4_frm_login, container, false);
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }
}
