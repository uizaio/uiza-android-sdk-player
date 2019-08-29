package com.uiza.demo.v4.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.uiza.demo.R;
import io.uiza.core.util.UzImageUtil;
import io.uiza.core.util.constant.Constants;
import io.uiza.player.interfaces.IOnBackPressed;

public class FrmHome extends Fragment implements IOnBackPressed {
    private final String TAG = getClass().getSimpleName();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView iv = view.findViewById(R.id.iv);
        UzImageUtil.load(getActivity(), Constants.URL_IMG_THUMBNAIL_2, iv);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.v4_frm_home, container, false);
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }
}