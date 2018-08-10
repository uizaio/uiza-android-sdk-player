package uiza.v4;

/**
 * Created by www.muathu@gmail.com on 12/24/2017.
 */

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import uiza.R;
import uiza.v3.canslide.HomeV3CanSlideActivity;
import vn.loitp.core.base.BaseActivity;
import vn.loitp.core.base.BaseFragment;
import vn.loitp.core.utilities.LActivityUtil;
import vn.loitp.core.utilities.LLog;
import vn.loitp.core.utilities.LScreenUtil;
import vn.loitp.uizavideo.view.IOnBackPressed;
import vn.loitp.uizavideov3.util.UizaDataV3;
import vn.loitp.uizavideov3.util.UizaUtil;
import vn.loitp.views.LToast;

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
            if (UizaDataV3.getInstance().isPlayWithPlaylistFolder()) {
                frmRootView.findViewById(R.id.bt_playlist_folder).performClick();
            } else {
                frmRootView.findViewById(R.id.bt_entity).performClick();
            }
        }
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.v4_frm_home;
    }

    private long backPressed;

    @Override
    public boolean onBackPressed() {
        LLog.d(TAG, "onBackPressed " + TAG);
        if (backPressed + 2000 > System.currentTimeMillis()) {
            return false;
        } else {
            boolean isLandscapeScreen = LScreenUtil.isFullScreen(getActivity());
            if (isLandscapeScreen) {
                LActivityUtil.toggleScreenOritation((BaseActivity) getContext());
            } else {
                if (((HomeV4CanSlideActivity) getActivity()).getDraggablePanel().getVisibility() == View.VISIBLE) {
                    if (((HomeV4CanSlideActivity) getActivity()).getDraggablePanel().isMaximized()) {
                        ((HomeV4CanSlideActivity) getActivity()).getDraggablePanel().minimize();
                        return true;
                    } else {
                    }
                } else {
                }
            }
            LToast.show(getActivity(), getString(R.string.press_again_to_exit));
        }
        backPressed = System.currentTimeMillis();
        return true;
    }
}
