package testlibuiza.sample.v3.utube;

/**
 * Created by www.muathu@gmail.com on 12/24/2017.
 */

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import testlibuiza.R;
import testlibuiza.app.LSApplication;
import uizacoresdk.interfaces.IOnBackPressed;
import uizacoresdk.util.UZUtil;
import vn.uiza.core.utilities.LActivityUtil;
import vn.uiza.core.utilities.LScreenUtil;
import vn.uiza.views.LToast;

public class FrmUTHome extends Fragment implements IOnBackPressed {
    private final String entityIdDefaultVOD = LSApplication.entityIdDefaultVOD;
    private final String entityIdDefaultVOD219 = LSApplication.entityIdDefaultVOD_21_9;
    private final String entityIdDefaultVODPortrait = LSApplication.entityIdDefaultVODportrait;
    private final String entityIdDefaultLIVE = LSApplication.entityIdDefaultLIVE;
    private final String metadataId = LSApplication.metadataDefault0;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.bt_entity_vod).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((CustomSkinCodeUZTimebarUTubeWithSlideActivity) getActivity()).playEntityId(entityIdDefaultVOD);
            }
        });
        view.findViewById(R.id.bt_entity_vod_21_9).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((CustomSkinCodeUZTimebarUTubeWithSlideActivity) getActivity()).playEntityId(entityIdDefaultVOD219);
            }
        });
        view.findViewById(R.id.bt_entity_vod_portrait).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((CustomSkinCodeUZTimebarUTubeWithSlideActivity) getActivity()).playEntityId(entityIdDefaultVODPortrait);
            }
        });
        view.findViewById(R.id.bt_entity_live).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((CustomSkinCodeUZTimebarUTubeWithSlideActivity) getActivity()).playEntityId(entityIdDefaultLIVE);
            }
        });
        view.findViewById(R.id.bt_playlist_folder).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((CustomSkinCodeUZTimebarUTubeWithSlideActivity) getActivity()).playPlaylistFolder(metadataId);
            }
        });
        view.findViewById(R.id.bt_switch_screen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((CustomSkinCodeUZTimebarUTubeWithSlideActivity) getActivity()).replaceFragment(new FrmUTLogin());
            }
        });

        if (UZUtil.getClickedPip(getActivity())) {
            if (UZUtil.isInitPlaylistFolder(getActivity())) {
                ((CustomSkinCodeUZTimebarUTubeWithSlideActivity) getActivity()).playPlaylistFolder(null);
            } else {
                ((CustomSkinCodeUZTimebarUTubeWithSlideActivity) getActivity()).playEntityId(null);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.ut_frm_home, container, false);
    }

    private long backPressed;

    @Override
    public boolean onBackPressed() {
        if (backPressed + 2000 > System.currentTimeMillis()) {
            return false;
        } else {
            boolean isLandscapeScreen = LScreenUtil.isFullScreen(getActivity());
            if (isLandscapeScreen) {
                LActivityUtil.toggleScreenOrientation(getActivity());
            } else {
                if (((CustomSkinCodeUZTimebarUTubeWithSlideActivity) getActivity()).getDraggablePanel().getVisibility() == View.VISIBLE) {
                    if (((CustomSkinCodeUZTimebarUTubeWithSlideActivity) getActivity()).getDraggablePanel().isMaximized()) {
                        ((CustomSkinCodeUZTimebarUTubeWithSlideActivity) getActivity()).getDraggablePanel().minimize();
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
