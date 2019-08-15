package uiza.v4.categories;

/**
 * Created by www.muathu@gmail.com on 12/24/2017.
 */

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import io.uiza.core.api.UzApiMaster;
import io.uiza.core.api.UzServiceApi;
import io.uiza.core.api.client.UzRestClient;
import io.uiza.core.api.response.BasePaginationResponse;
import io.uiza.core.api.response.video.VideoData;
import io.uiza.core.api.util.ApiSubscriber;
import io.uiza.core.util.UzDialogUtil;
import io.uiza.core.util.LLog;
import io.uiza.core.util.UzDisplayUtil;
import io.uiza.core.util.constant.Constants;
import io.uiza.core.view.LToast;
import java.util.ArrayList;
import java.util.List;
import uiza.R;
import uiza.app.LSApplication;
import uiza.v4.HomeV4CanSlideActivity;
import uiza.v4.entities.EntitiesAdapter;
import uizacoresdk.interfaces.IOnBackPressed;
import uizacoresdk.util.UZData;
import uizacoresdk.util.UZUtil;

public class FrmEntitiesOfCategory extends Fragment implements IOnBackPressed {
    private String TAG = getClass().getSimpleName();
    private final int limit = 20;
    private final String orderBy = "createdAt";
    private final String orderType = "DESC";
    private final String publishToCdn = "success";
    private RecyclerView recyclerView;
    private EntitiesAdapter mAdapter;
    private TextView tvMsg;
    private List<VideoData> dataList = new ArrayList<>();
    private int page = 0;
    private int totalPage = Integer.MAX_VALUE;
    private ProgressBar pb;
    private String metadataId = "";

    public void setTag(String tag) {
        TAG = tag;
    }

    public void setMetadataId(String metadataId) {
        this.metadataId = metadataId;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (UZUtil.getClickedPip(getActivity())) {
            if (UZData.getInstance().isPlayWithPlaylistFolder()) {
                LLog.d(TAG, "Called if user click pip fullscreen playPlaylistFolder");
                ((HomeV4CanSlideActivity) getActivity()).playPlaylistFolder(null);
            } else {
                LLog.d(TAG, "Called if user click pip fullscreen playEntityId");
                ((HomeV4CanSlideActivity) getActivity()).playEntityId(null);
            }
        }
        tvMsg = (TextView) view.findViewById(R.id.tv_msg);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv);
        pb = (ProgressBar) view.findViewById(R.id.pb);
        UzDisplayUtil.setColorProgressBar(pb, Color.WHITE);
        UzDialogUtil.hide(pb);

        mAdapter = new EntitiesAdapter(getActivity(), dataList, new EntitiesAdapter.Callback() {
            @Override
            public void onClick(VideoData data, int position) {
                ((HomeV4CanSlideActivity) getActivity()).playEntityId(data.getId());
            }

            @Override
            public void onLongClick(VideoData data, int position) {
            }

            @Override
            public void onLoadMore() {
                loadMore();
            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);
        getListAllEntities();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.v4_frm_entities, container, false);
    }

    @Override
    public boolean onBackPressed() {
        LLog.d(TAG, "onBackPressed " + TAG);
        return false;
        //return ((HomeV4CanSlideActivity) getActivity()).handleOnbackpressFrm();
    }

    private void getListAllEntities() {
        LLog.d(TAG, "getListAllEntities metadataId: " + metadataId);
        page++;
        if (page >= totalPage) {
            if (Constants.IS_DEBUG) {
                LToast.show(getActivity(), getString(R.string.this_is_last_page));
            }
            return;
        }
        if (Constants.IS_DEBUG) {
            LToast.show(getActivity(), getString(R.string.load_page) + page);
        }
        LLog.d(TAG, "getListAllEntities " + page + "/" + totalPage);
        UzDialogUtil.show(pb);
        tvMsg.setVisibility(View.GONE);
        UzServiceApi service = UzRestClient.createService(UzServiceApi.class);
        UzApiMaster.getInstance().subscribe(
                service.getListAllEntity(UZData.getInstance().getAPIVersion(), metadataId, limit,
                        page, orderBy, orderType, publishToCdn, UZData.getInstance().getAppId()),
                new ApiSubscriber<BasePaginationResponse<List<VideoData>>>() {
                    @Override
                    public void onSuccess(BasePaginationResponse<List<VideoData>> response) {
                        LLog.d(TAG, "getListAllEntities " + LSApplication.getInstance().getGson()
                                .toJson(response));
                        if (response == null || response.getMetadata() == null || response.getData()
                                .isEmpty()) {
                            tvMsg.setVisibility(View.VISIBLE);
                            tvMsg.setText(getString(R.string.empty_list));
                            UzDialogUtil.hide(pb);
                            return;
                        }
                        if (totalPage == Integer.MAX_VALUE) {
                            int totalItem = (int) response.getMetadata().getTotal();
                            float ratio = (float) (totalItem / limit);
                            if (ratio == 0) {
                                totalPage = (int) ratio;
                            } else if (ratio > 0) {
                                totalPage = (int) ratio + 1;
                            } else {
                                totalPage = (int) ratio;
                            }
                        }
                        LLog.d(TAG, "-> totalPage: " + totalPage + ", size: " + response.getData()
                                .size());
                        dataList.addAll(response.getData());
                        mAdapter.notifyDataSetChanged();
                        UzDialogUtil.hide(pb);
                    }

                    @Override
                    public void onFail(Throwable e) {
                        LLog.e(TAG, "getListAllEntity onFail " + e.getMessage());
                        tvMsg.setVisibility(View.VISIBLE);
                        tvMsg.setText("onFail: " + e.getMessage());
                        UzDialogUtil.hide(pb);
                    }
                });
    }

    private void loadMore() {
        LLog.d(TAG, "loadMore");
        getListAllEntities();
    }
}
