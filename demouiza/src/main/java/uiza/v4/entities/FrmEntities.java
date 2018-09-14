package uiza.v4.entities;

/**
 * Created by www.muathu@gmail.com on 12/24/2017.
 */

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import uiza.R;
import uiza.app.LSApplication;
import uiza.v4.HomeV4CanSlideActivity;
import vn.uiza.core.base.BaseFragment;
import vn.uiza.core.common.Constants;
import vn.uiza.core.utilities.LDialogUtil;
import vn.uiza.core.utilities.LLog;
import vn.uiza.core.utilities.LUIUtil;
import vn.uiza.restapi.restclient.RestClientV3;
import vn.uiza.restapi.uiza.UizaServiceV3;
import vn.uiza.restapi.uiza.model.v3.metadata.getdetailofmetadata.Data;
import vn.uiza.restapi.uiza.model.v3.videoondeman.listallentity.ResultListEntity;
import vn.uiza.rxandroid.ApiSubscriber;
import vn.uiza.uzv1.view.IOnBackPressed;
import vn.uiza.uzv3.util.UZData;
import vn.uiza.uzv3.util.UZUtil;
import vn.uiza.views.LToast;

public class FrmEntities extends BaseFragment implements IOnBackPressed {
    private final int limit = 50;
    private final String orderBy = "createdAt";
    private RecyclerView recyclerView;
    private EntitiesAdapter mAdapter;
    private final String orderType = "DESC";
    private final String publishToCdn = "success";
    private TextView tvMsg;
    private List<Data> dataList = new ArrayList<>();
    private int page = 0;
    private int totalPage = Integer.MAX_VALUE;
    private ProgressBar pb;
    private String metadataId = "";

    @Override
    protected String setTag() {
        return "Entities";
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
        tvMsg = (TextView) frmRootView.findViewById(R.id.tv_msg);
        recyclerView = (RecyclerView) frmRootView.findViewById(R.id.rv);
        pb = (ProgressBar) frmRootView.findViewById(R.id.pb);
        LUIUtil.setColorProgressBar(pb, Color.WHITE);
        LDialogUtil.hide(pb);

        mAdapter = new EntitiesAdapter(getActivity(), dataList, new EntitiesAdapter.Callback() {
            @Override
            public void onClick(Data data, int position) {
                UZUtil.setClickedPip(getActivity(), false);
                ((HomeV4CanSlideActivity) getActivity()).playEntityId(data.getId());
            }

            @Override
            public void onLongClick(Data data, int position) {
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
        getListAllEntities();
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.v4_frm_entities;
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
        LDialogUtil.show(pb);
        tvMsg.setVisibility(View.GONE);
        UizaServiceV3 service = RestClientV3.createService(UizaServiceV3.class);
        subscribe(service.getListAllEntity(metadataId, limit, page, orderBy, orderType, publishToCdn), new ApiSubscriber<ResultListEntity>() {
            @Override
            public void onSuccess(ResultListEntity result) {
                LLog.d(TAG, "getListAllEntities " + LSApplication.getInstance().getGson().toJson(result));
                if (result == null || result.getMetadata() == null || result.getData().isEmpty()) {
                    tvMsg.setVisibility(View.VISIBLE);
                    tvMsg.setText(getString(R.string.empty_list));
                    return;
                }
                if (totalPage == Integer.MAX_VALUE) {
                    int totalItem = (int) result.getMetadata().getTotal();
                    float ratio = (float) (totalItem / limit);
                    if (ratio == 0) {
                        totalPage = (int) ratio;
                    } else if (ratio > 0) {
                        totalPage = (int) ratio + 1;
                    } else {
                        totalPage = (int) ratio;
                    }
                }
                LLog.d(TAG, "-> totalPage: " + totalPage + ", size: " + result.getData().size());
                dataList.addAll(result.getData());
                mAdapter.notifyDataSetChanged();
                LDialogUtil.hide(pb);
            }

            @Override
            public void onFail(Throwable e) {
                LLog.e(TAG, "getListAllEntity onFail " + e.getMessage());
                tvMsg.setVisibility(View.VISIBLE);
                tvMsg.setText("onFail: " + e.getMessage());
                LDialogUtil.hide(pb);
            }
        });
    }

    private void loadMore() {
        LLog.d(TAG, "loadMore");
        getListAllEntities();
    }
}
