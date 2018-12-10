package testlibuiza.sample.v3.fb;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import testlibuiza.R;
import testlibuiza.app.LSApplication;
import vn.uiza.core.base.BaseActivity;
import vn.uiza.core.common.Constants;
import vn.uiza.core.utilities.LActivityUtil;
import vn.uiza.restapi.restclient.UZRestClient;
import vn.uiza.restapi.uiza.UZService;
import vn.uiza.restapi.uiza.model.v3.metadata.getdetailofmetadata.Data;
import vn.uiza.restapi.uiza.model.v3.videoondeman.listallentity.ResultListEntity;
import vn.uiza.rxandroid.ApiSubscriber;

public class FBListVideoActivity extends BaseActivity {
    private List<Data> dataList = new ArrayList<>();
    private RecyclerView recyclerView;
    private FBVideoAdapter fbVideoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NestedScrollView nsv = (NestedScrollView) findViewById(R.id.nsv);
        nsv.setNestedScrollingEnabled(false);
        recyclerView = (RecyclerView) findViewById(R.id.rv);
        findViewById(R.id.ll_playlist_folder).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, FBVideoActivity.class);
                intent.putExtra(Constants.KEY_UIZA_METADATA_ENTITY_ID, LSApplication.metadataDefault0);
                startActivity(intent);
                LActivityUtil.tranIn(activity);
            }
        });

        fbVideoAdapter = new FBVideoAdapter(activity, dataList, new FBVideoAdapter.Callback() {
            @Override
            public void onClick(Data data, int position) {
                Intent intent = new Intent(activity, FBVideoActivity.class);
                intent.putExtra(Constants.KEY_UIZA_ENTITY_ID, data.getId());
                startActivity(intent);
                LActivityUtil.tranIn(activity);
            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(fbVideoAdapter);
        listAllEntity();
    }

    @Override
    protected boolean setFullScreen() {
        return false;
    }

    @Override
    protected String setTag() {
        return "TAG" + getClass().getSimpleName();
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.activity_fb_list_video;
    }

    private void listAllEntity() {
        UZService service = UZRestClient.createService(UZService.class);
        String metadataId = "";
        int limit = 50;
        int page = 0;
        String orderBy = "createdAt";
        String orderType = "DESC";
        subscribe(service.getListAllEntity(metadataId, limit, page, orderBy, orderType, "success"), new ApiSubscriber<ResultListEntity>() {
            @Override
            public void onSuccess(ResultListEntity result) {
                dataList.addAll(result.getData());
                fbVideoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFail(Throwable e) {
            }
        });
    }
}
