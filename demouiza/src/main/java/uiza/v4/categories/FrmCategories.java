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
import io.uiza.core.util.LLog;
import io.uiza.core.util.UzDisplayUtil;
import io.uiza.core.util.constant.Constants;
import io.uiza.core.view.LToast;
import java.util.ArrayList;
import java.util.List;
import uiza.R;
import uiza.app.LSApplication;
import uiza.v4.HomeV4CanSlideActivity;
import uizacoresdk.interfaces.IOnBackPressed;
import uizacoresdk.util.UZData;

public class FrmCategories extends Fragment implements IOnBackPressed {
    private final String TAG = getClass().getSimpleName();
    private RecyclerView recyclerView;
    private TextView tvMsg;
    private ProgressBar pb;
    private CategoriesAdapter mAdapter;
    private List<VideoData> dataList = new ArrayList<>();
    private int currentPage = 1;
    private int totalPage = Integer.MAX_VALUE;
    private final int limit = 100;

    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.v4_frm_categories, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv);
        tvMsg = (TextView) view.findViewById(R.id.tv_msg);
        pb = (ProgressBar) view.findViewById(R.id.pb);
        UzDisplayUtil.setColorProgressBar(pb, Color.WHITE);
        mAdapter = new CategoriesAdapter(getActivity(), dataList, new CategoriesAdapter.Callback() {
            @Override
            public void onClick(VideoData data, int position) {
                FrmEntitiesOfCategory frmEntitiesOfCategory = new FrmEntitiesOfCategory();
                frmEntitiesOfCategory.setTag(data.getName());
                frmEntitiesOfCategory.setMetadataId(data.getId());
                ((HomeV4CanSlideActivity) getActivity()).replaceFragment(frmEntitiesOfCategory);
            }

            @Override
            public void onClickPlaylistFolder(VideoData data, int position) {
                ((HomeV4CanSlideActivity) getActivity()).playPlaylistFolder(data.getId());
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
        getListMetadata();
    }

    private void getListMetadata() {
        if (currentPage > totalPage) {
            LLog.d(TAG, "getListMetadata This is the last page");
            if (Constants.IS_DEBUG) {
                LToast.show(getActivity(), "This is the last page");
            }
            UzDisplayUtil.hideProgressBar(pb);
            return;
        }
        LLog.d(TAG, "getListMetadata " + currentPage + "/" + totalPage);
        tvMsg.setVisibility(View.GONE);
        UzDisplayUtil.showProgressBar(pb);
        UzServiceApi service = UzRestClient.createService(UzServiceApi.class);
        UzApiMaster.getInstance().subscribe(service.getListMetadata(UZData.getInstance().getAPIVersion(), limit, currentPage), new ApiSubscriber<BasePaginationResponse<List<VideoData>>>() {
            @Override
            public void onSuccess(BasePaginationResponse<List<VideoData>> response) {
                if (response == null || response.getData() == null || response.getData().isEmpty() || response.getMetadata() == null) {
                    tvMsg.setText("Error ResultGetListMetadata is null or empty");
                    UzDisplayUtil.hideProgressBar(pb);
                    return;
                }
                LLog.d(TAG, "onSuccess " + LSApplication.getInstance().getGson().toJson(response));

                int total = (int) response.getMetadata().getTotal();
                if (total / limit == 0) {
                    totalPage = total / limit;
                } else {
                    totalPage = total / limit + 1;
                }
                LLog.d(TAG, "totalPage " + totalPage);

                dataList.addAll(response.getData());
                mAdapter.notifyDataSetChanged();
                UzDisplayUtil.hideProgressBar(pb);
            }

            @Override
            public void onFail(Throwable e) {
                LLog.e(TAG, "getListAllMetadata onFail " + e.getMessage());
                tvMsg.setText("Error ResultGetListMetadata: " + e.getMessage());
                UzDisplayUtil.hideProgressBar(pb);
            }
        });
    }

    private void loadMore() {
        LLog.d(TAG, "loadMore");
        currentPage++;
        getListMetadata();
    }
}
