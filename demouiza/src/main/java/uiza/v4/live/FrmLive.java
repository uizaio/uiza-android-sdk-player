package uiza.v4.live;

/**
 * Created by www.muathu@gmail.com on 12/24/2017.
 */

import android.content.Intent;
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
import java.util.ArrayList;
import java.util.List;
import uiza.R;
import uiza.app.LSApplication;
import uiza.v4.HomeV4CanSlideActivity;
import uizacoresdk.interfaces.IOnBackPressed;
import uizacoresdk.util.UZData;
import uizacoresdk.util.UZUtil;
import vn.uiza.core.common.Constants;
import vn.uiza.core.utilities.LDialogUtil;
import vn.uiza.core.utilities.LLog;
import vn.uiza.core.utilities.LUIUtil;
import vn.uiza.restapi.UZAPIMaster;
import vn.uiza.restapi.restclient.UZRestClient;
import vn.uiza.restapi.uiza.UZService;
import vn.uiza.restapi.uiza.model.v3.livestreaming.retrievealiveevent.ResultRetrieveALiveEvent;
import vn.uiza.restapi.uiza.model.v3.metadata.getdetailofmetadata.Data;
import vn.uiza.rxandroid.ApiSubscriber;
import vn.uiza.views.LToast;

public class FrmLive extends Fragment implements IOnBackPressed {
    private final String TAG = getClass().getSimpleName();
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.v4_frm_live, container, false);
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
        LUIUtil.setColorProgressBar(pb, Color.WHITE);
        LDialogUtil.hide(pb);

        mAdapter = new LiveAdapter(getActivity(), dataList, new LiveAdapter.Callback() {
            @Override
            public void onClick(Data data, int position) {
                if (data != null && data.getLastProcess() != null && data.getLastProcess().trim().equals(Constants.LAST_PROCESS_START)) {
                    ((HomeV4CanSlideActivity) getActivity()).playEntityId(data.getId());
                } else {
                    LDialogUtil.showDialog1(getActivity(), "This content is not streaming now", new LDialogUtil.Callback1() {
                        @Override
                        public void onClick1() {
                            onBackPressed();
                        }

                        @Override
                        public void onCancel() {
                            onBackPressed();
                        }
                    });
                }
            }

            @Override
            public void onClickLivestream(Data data, int position) {
                Intent intent = new Intent(getActivity(), LivestreamBroadcasterActivity.class);
                intent.putExtra(Constants.KEY_UIZA_ENTITY_ID, data.getId());
                startActivity(intent);
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
        getListAllEntities();
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
        UZService service = UZRestClient.createService(UZService.class);
        UZAPIMaster.getInstance().subscribe(service.retrieveALiveEvent(UZData.getInstance().getAPIVersion(), limit, page, orderBy, orderType, UZData.getInstance().getAppId()), new ApiSubscriber<ResultRetrieveALiveEvent>() {
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
        page++;
        getListAllEntities();
    }
}
