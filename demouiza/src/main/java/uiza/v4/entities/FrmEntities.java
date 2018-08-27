package uiza.v4.entities;

/**
 * Created by www.muathu@gmail.com on 12/24/2017.
 */

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import uiza.R;
import uiza.v4.HomeV4CanSlideActivity;
import vn.loitp.core.base.BaseActivity;
import vn.loitp.core.base.BaseFragment;
import vn.loitp.core.common.Constants;
import vn.loitp.core.utilities.LActivityUtil;
import vn.loitp.core.utilities.LLog;
import vn.loitp.core.utilities.LScreenUtil;
import vn.loitp.core.utilities.LUIUtil;
import vn.loitp.uizavideo.view.IOnBackPressed;
import vn.loitp.views.LToast;

public class FrmEntities extends BaseFragment implements IOnBackPressed {
    private final String TAG = getClass().getSimpleName();

    //private final String entityId = "b7297b29-c6c4-4bd6-a74f-b60d0118d275";
    //private final String metadataId = "00932b61-1d39-45d2-8c7d-3d99ad9ea95a";
    private long backPressed;


    private List<Entity> entityList = new ArrayList<>();
    private RecyclerView recyclerView;
    private EntitiesAdapter mAdapter;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /*frmRootView.findViewById(R.id.bt_entity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UizaUtil.setClickedPip(getActivity(), false);
                ((HomeV4CanSlideActivity) getActivity()).playEntityId(entityId);
            }
        });
        frmRootView.findViewById(R.id.bt_playlist_folder).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UizaUtil.setClickedPip(getActivity(), false);
                ((HomeV4CanSlideActivity) getActivity()).playPlaylistFolder(metadataId);
            }
        });
        if (UizaUtil.getClickedPip(getActivity())) {
            if (UizaDataV3.getInstance().isPlayWithPlaylistFolder()) {
                LLog.d(TAG, "Called if user click pip fullscreen playPlaylistFolder");
                //frmRootView.findViewById(R.id.bt_playlist_folder).performClick();
                ((HomeV4CanSlideActivity) getActivity()).playPlaylistFolder(metadataId);
            } else {
                //frmRootView.findViewById(R.id.bt_entity).performClick();
                LLog.d(TAG, "Called if user click pip fullscreen playEntityId");
                ((HomeV4CanSlideActivity) getActivity()).playEntityId(entityId);
            }
        }*/

        recyclerView = (RecyclerView) frmRootView.findViewById(R.id.rv);

        mAdapter = new EntitiesAdapter(getActivity(), entityList, new EntitiesAdapter.Callback() {
            @Override
            public void onClick(Entity entity, int position) {
                LToast.show(getActivity(), "Click " + entity.getTitle());
            }

            @Override
            public void onLongClick(Entity entity, int position) {
                boolean isRemoved = entityList.remove(entity);
                if (isRemoved) {
                    mAdapter.notifyItemRemoved(position);
                    mAdapter.notifyItemRangeChanged(position, entityList.size());
                }
            }

            @Override
            public void onLoadMore() {
                loadMore();
            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);
        LUIUtil.setPullLikeIOSVertical(recyclerView);
        prepareMovieData();
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.v4_frm_home;
    }

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

    private void prepareMovieData() {
        for (int i = 0; i < 100; i++) {
            Entity entity = new Entity("Loitp " + i, "Action & Adventure " + i, "Year: " + i, Constants.URL_IMG);
            entityList.add(entity);
        }
        mAdapter.notifyDataSetChanged();
    }

    private void loadMore() {

    }
}
