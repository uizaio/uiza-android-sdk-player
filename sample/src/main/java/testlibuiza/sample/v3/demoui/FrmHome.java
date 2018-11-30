package testlibuiza.sample.v3.demoui;

/**
 * Created by www.muathu@gmail.com on 12/24/2017.
 */

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import testlibuiza.R;
import testlibuiza.app.LSApplication;
import uizacoresdk.interfaces.IOnBackPressed;
import uizacoresdk.util.UZUtil;
import vn.uiza.core.base.BaseActivity;
import vn.uiza.core.base.BaseFragment;
import vn.uiza.core.utilities.LActivityUtil;
import vn.uiza.core.utilities.LScreenUtil;
import vn.uiza.views.LToast;

public class FrmHome extends BaseFragment implements IOnBackPressed {
    private final String entityIdDefaultVOD = LSApplication.entityIdDefaultVOD;
    private final String entityIdDefaultVOD219 = LSApplication.entityIdDefaultVOD_21_9;
    private final String entityIdDefaultLIVE = LSApplication.entityIdDefaultLIVE;
    private final String metadataId = LSApplication.metadataDefault0;

    @Override
    protected String setTag() {
        return getClass().getSimpleName();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        frmRootView.findViewById(R.id.bt_entity_vod).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeCanSlideActivity) getActivity()).playEntityId(entityIdDefaultVOD);
            }
        });
        frmRootView.findViewById(R.id.bt_entity_vod_21_9).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeCanSlideActivity) getActivity()).playEntityId(entityIdDefaultVOD219);
            }
        });
        frmRootView.findViewById(R.id.bt_entity_live).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeCanSlideActivity) getActivity()).playEntityId(entityIdDefaultLIVE);
            }
        });
        frmRootView.findViewById(R.id.bt_playlist_folder).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeCanSlideActivity) getActivity()).playPlaylistFolder(metadataId);
            }
        });
        frmRootView.findViewById(R.id.bt_switch_screen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeCanSlideActivity) getActivity()).replaceFragment(new FrmLogin());
            }
        });

        if (UZUtil.getClickedPip(getActivity())) {
            if (UZUtil.isInitPlaylistFolder(getActivity())) {
                ((HomeCanSlideActivity) getActivity()).playPlaylistFolder(null);
            } else {
                ((HomeCanSlideActivity) getActivity()).playEntityId(null);
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
        //LLog.d(TAG, "onBackPressed " + TAG);
        if (backPressed + 2000 > System.currentTimeMillis()) {
            return false;
        } else {
            boolean isLandscapeScreen = LScreenUtil.isFullScreen(getActivity());
            if (isLandscapeScreen) {
                LActivityUtil.toggleScreenOritation((BaseActivity) getContext());
            } else {
                if (((HomeCanSlideActivity) getActivity()).getDraggablePanel().getVisibility() == View.VISIBLE) {
                    if (((HomeCanSlideActivity) getActivity()).getDraggablePanel().isMaximized()) {
                        ((HomeCanSlideActivity) getActivity()).getDraggablePanel().minimize();
                        return true;
                    }
                }
            }
            LToast.show(getActivity(), getString(R.string.press_again_to_exit));
        }
        backPressed = System.currentTimeMillis();
        return true;
    }
}
