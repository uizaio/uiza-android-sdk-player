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
import java.util.ArrayList;
import java.util.List;
import uiza.R;
import uiza.app.LSApplication;
import uiza.v4.HomeV4CanSlideActivity;
import uizacoresdk.interfaces.IOnBackPressed;
import uizacoresdk.util.UZData;
import vn.uiza.core.common.Constants;
import vn.uiza.core.utilities.LLog;
import vn.uiza.core.utilities.LUIUtil;
import vn.uiza.restapi.UZAPIMaster;
import vn.uiza.restapi.restclient.UZRestClient;
import vn.uiza.restapi.uiza.UZService;
import vn.uiza.restapi.uiza.model.v3.metadata.getdetailofmetadata.Data;
import vn.uiza.restapi.uiza.model.v3.metadata.getlistmetadata.ResultGetListMetadata;
import vn.uiza.rxandroid.ApiSubscriber;
import vn.uiza.views.LToast;

public class FrmCategories extends Fragment implements IOnBackPressed {
    private final String TAG = getClass().getSimpleName();
    private RecyclerView recyclerView;
    private TextView tvMsg;
    private ProgressBar pb;
    private CategoriesAdapter mAdapter;
    private List<Data> dataList = new ArrayList<>();
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
        LUIUtil.setColorProgressBar(pb, Color.WHITE);
        mAdapter = new CategoriesAdapter(getActivity(), dataList, new CategoriesAdapter.Callback() {
            @Override
            public void onClick(Data data, int position) {
                FrmEntitiesOfCategory frmEntitiesOfCategory = new FrmEntitiesOfCategory();
                frmEntitiesOfCategory.setTag(data.getName());
                frmEntitiesOfCategory.setMetadataId(data.getId());
                ((HomeV4CanSlideActivity) getActivity()).replaceFragment(frmEntitiesOfCategory);
            }

            @Override
            public void onClickPlaylistFolder(Data data, int position) {
                ((HomeV4CanSlideActivity) getActivity()).playPlaylistFolder(data.getId());
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
        getListMetadata();
    }

    private void getListMetadata() {
        if (currentPage > totalPage) {
            LLog.d(TAG, "getListMetadata This is the last page");
            if (Constants.IS_DEBUG) {
                LToast.show(getActivity(), "This is the last page");
            }
            LUIUtil.hideProgressBar(pb);
            return;
        }
        LLog.d(TAG, "getListMetadata " + currentPage + "/" + totalPage);
        tvMsg.setVisibility(View.GONE);
        LUIUtil.showProgressBar(pb);
        UZService service = UZRestClient.createService(UZService.class);
        UZAPIMaster.getInstance().subscribe(service.getListMetadata(UZData.getInstance().getAPIVersion(), limit, currentPage), new ApiSubscriber<ResultGetListMetadata>() {
            @Override
            public void onSuccess(ResultGetListMetadata resultGetListMetadata) {
                if (resultGetListMetadata == null || resultGetListMetadata.getData() == null || resultGetListMetadata.getData().isEmpty() || resultGetListMetadata.getMetadata() == null) {
                    tvMsg.setText("Error ResultGetListMetadata is null or empty");
                    LUIUtil.hideProgressBar(pb);
                    return;
                }
                LLog.d(TAG, "onSuccess " + LSApplication.getInstance().getGson().toJson(resultGetListMetadata));

                int total = (int) resultGetListMetadata.getMetadata().getTotal();
                if (total / limit == 0) {
                    totalPage = total / limit;
                } else {
                    totalPage = total / limit + 1;
                }
                LLog.d(TAG, "totalPage " + totalPage);

                dataList.addAll(resultGetListMetadata.getData());
                mAdapter.notifyDataSetChanged();
                LUIUtil.hideProgressBar(pb);
            }

            @Override
            public void onFail(Throwable e) {
                LLog.e(TAG, "getListAllMetadata onFail " + e.getMessage());
                tvMsg.setText("Error ResultGetListMetadata: " + e.getMessage());
                LUIUtil.hideProgressBar(pb);
            }
        });
    }

    private void loadMore() {
        LLog.d(TAG, "loadMore");
        currentPage++;
        getListMetadata();
    }
}
