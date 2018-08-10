package uiza.v4;

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
import vn.loitp.uizavideov3.util.UizaUtil;

public class FrmHome extends BaseFragment implements IOnBackPressed {
    private final String TAG = getClass().getSimpleName();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        frmRootView.findViewById(R.id.bt_entity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeV4CanSlideActivity) getActivity()).playEntityId("b7297b29-c6c4-4bd6-a74f-b60d0118d275");
            }
        });
        frmRootView.findViewById(R.id.bt_playlist_folder).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeV4CanSlideActivity) getActivity()).playPlaylistFolder("00932b61-1d39-45d2-8c7d-3d99ad9ea95a");
            }
        });
        frmRootView.findViewById(R.id.bt_switch_screen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeV4CanSlideActivity) getActivity()).replaceFragment(new FrmLogin());
            }
        });

        if (UizaUtil.getClickedPip(getActivity())) {
            frmRootView.findViewById(R.id.bt).performClick();
        }
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.v4_frm_home;
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }
}
