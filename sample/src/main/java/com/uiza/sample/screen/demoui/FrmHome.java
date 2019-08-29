package com.uiza.sample.screen.demoui;

/**
 * Created by www.muathu@gmail.com on 17/1/2019.
 */

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.uiza.sample.LSApplication;
import com.uiza.sample.R;
import io.uiza.core.util.UzDisplayUtil;
import io.uiza.core.view.LToast;
import io.uiza.player.interfaces.IOnBackPressed;
import io.uiza.player.mini.pip.PipHelper;
import io.uiza.player.util.UzPlayerData;

public class FrmHome extends Fragment implements IOnBackPressed {

    private final String entityIdDefaultVOD = LSApplication.entityIdDefaultVOD;
    private final String entityIdDefaultVOD219 = LSApplication.entityIdDefaultVOD_21_9;
    private final String entityIdDefaultVODPortrait = LSApplication.entityIdDefaultVODportrait;
    private final String entityIdDefaultLIVE = LSApplication.entityIdDefaultLIVE;
    private final String metadataId = LSApplication.metadataDefault0;
    private long backPressed;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.bt_entity_vod).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeCanSlideActivity) getActivity()).playEntityId(entityIdDefaultVOD);
            }
        });
        view.findViewById(R.id.bt_entity_vod_21_9).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeCanSlideActivity) getActivity()).playEntityId(entityIdDefaultVOD219);
            }
        });
        view.findViewById(R.id.bt_entity_vod_portrait)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((HomeCanSlideActivity) getActivity())
                                .playEntityId(entityIdDefaultVODPortrait);
                    }
                });
        view.findViewById(R.id.bt_entity_live).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeCanSlideActivity) getActivity()).playEntityId(entityIdDefaultLIVE);
            }
        });
        view.findViewById(R.id.bt_playlist_folder).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeCanSlideActivity) getActivity()).playPlaylistFolder(metadataId);
            }
        });
        view.findViewById(R.id.bt_switch_screen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeCanSlideActivity) getActivity()).replaceFragment(new FrmLogin());
            }
        });

        if (PipHelper.getClickedPip(getActivity())) {
            if (UzPlayerData.isInitPlaylistFolder(getActivity())) {
                ((HomeCanSlideActivity) getActivity()).playPlaylistFolder(null);
            } else {
                ((HomeCanSlideActivity) getActivity()).playEntityId(null);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.v4_frm_home, container, false);
    }

    @Override
    public boolean onBackPressed() {
        //LLog.d(TAG, "onBackPressed " + TAG);
        if (backPressed + 2000 > System.currentTimeMillis()) {
            return false;
        } else {
            boolean isLandscapeScreen = UzDisplayUtil.isFullScreen(getActivity());
            if (isLandscapeScreen) {
                UzDisplayUtil.toggleScreenOrientation(getActivity());
            } else {
                if (((HomeCanSlideActivity) getActivity()).getDraggablePanel().getVisibility()
                        == View.VISIBLE) {
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
