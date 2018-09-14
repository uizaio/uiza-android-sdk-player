package uiza.v4.live;

/**
 * Created by www.muathu@gmail.com on 12/24/2017.
 */

import android.content.Intent;
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
import vn.loitp.core.base.BaseFragment;
import vn.loitp.core.common.Constants;
import vn.loitp.core.utilities.LActivityUtil;
import vn.loitp.core.utilities.LDialogUtil;
import vn.loitp.core.utilities.LLog;
import vn.loitp.core.utilities.LUIUtil;
import vn.loitp.restapi.restclient.RestClientV3;
import vn.loitp.restapi.uiza.UizaServiceV3;
import vn.loitp.restapi.uiza.model.v3.livestreaming.retrievealiveevent.ResultRetrieveALiveEvent;
import vn.loitp.restapi.uiza.model.v3.metadata.getdetailofmetadata.Data;
import vn.loitp.rxandroid.ApiSubscriber;
import vn.loitp.uizavideov3.util.UizaDataV3;
import vn.loitp.uizavideov3.util.UizaUtil;
import vn.loitp.uzv1.view.IOnBackPressed;
import vn.loitp.views.LToast;

public class FrmLive extends BaseFragment implements IOnBackPressed {
    private final int limit = 50;
    private final String orderBy = "createdAt";
    private final String orderType = "DESC";
    private final String publishToCdn = "success";
    private RecyclerView recyclerView;
    private LiveAdapter mAdapter;
    private TextView tvMsg;
    private List<Data> dataList = new ArrayList<>();
    private int page = 1;
    private int totalPage = Integer.MAX_VALUE;
    private ProgressBar pb;

    @Override
    protected String setTag() {
        return "Livestreaming";
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LLog.d(TAG, "onViewCreated");
        if (UizaUtil.getClickedPip(getActivity())) {
            if (UizaDataV3.getInstance().isPlayWithPlaylistFolder()) {
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

        mAdapter = new LiveAdapter(getActivity(), dataList, new LiveAdapter.Callback() {
            @Override
            public void onClick(Data data, int position) {
                if (data != null && data.getLastProcess() != null && data.getLastProcess().trim().equals(Constants.LAST_PROCESS_START)) {
                    UizaUtil.setClickedPip(getActivity(), false);
                    ((HomeV4CanSlideActivity) getActivity()).playEntityId(data.getId());
                } else {
                    showDialogError("This content is not streaming now");
                }
            }

            @Override
            public void onClickLivestream(Data data, int position) {
                Intent intent = new Intent(getActivity(), LivestreamBroadcasterActivity.class);
                intent.putExtra(Constants.KEY_UIZA_ENTITY_ID, data.getId());
                startActivity(intent);
                LActivityUtil.tranIn(getActivity());
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
        return R.layout.v4_frm_live;
    }

    @Override
    public boolean onBackPressed() {
        LLog.d(TAG, "onBackPressed " + TAG);
        return false;
        //return ((HomeV4CanSlideActivity) getActivity()).handleOnbackpressFrm();
    }

    private void getListAllEntities() {
        if (page > totalPage) {
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
        subscribe(service.retrieveALiveEvent(limit, page, orderBy, orderType), new ApiSubscriber<ResultRetrieveALiveEvent>() {
            @Override
            public void onSuccess(ResultRetrieveALiveEvent result) {
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
        page++;
        getListAllEntities();
    }
}
