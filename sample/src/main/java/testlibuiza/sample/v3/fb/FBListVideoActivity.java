package testlibuiza.sample.v3.fb;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import java.util.ArrayList;
import java.util.List;
import testlibuiza.R;
import testlibuiza.app.LSApplication;
import uizacoresdk.util.UZData;
import uizacoresdk.util.UZUtil;
import vn.uiza.core.common.Constants;
import vn.uiza.restapi.UZAPIMaster;
import vn.uiza.restapi.restclient.UZRestClient;
import vn.uiza.restapi.uiza.UZService;
import vn.uiza.restapi.uiza.model.v3.metadata.getdetailofmetadata.Data;
import vn.uiza.restapi.uiza.model.v3.videoondeman.listallentity.ResultListEntity;
import vn.uiza.rxandroid.ApiSubscriber;

public class FBListVideoActivity extends AppCompatActivity {
    private Activity activity;
    private final String TAG = getClass().getSimpleName();
    private List<Data> dataList = new ArrayList<>();
    private RecyclerView recyclerView;
    private FBVideoAdapter fbVideoAdapter;
    private CardView cvPlaylistFolder;
    private NestedScrollView nsv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activity = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fb_list_video);
        findViews();
        nsv.setNestedScrollingEnabled(false);
        cvPlaylistFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, FBVideoActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra(Constants.KEY_UIZA_THUMBNAIL, Constants.URL_IMG_THUMBNAIL);
                intent.putExtra(Constants.KEY_UIZA_METADATA_ENTITY_ID, LSApplication.metadataDefault0);
                startActivity(intent);
            }
        });
        findViewById(R.id.bt_setting_mini_player).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, MiniPlayerSettingActivity.class);
                startActivity(intent);
            }
        });
        fbVideoAdapter = new FBVideoAdapter(activity, dataList, new FBVideoAdapter.Callback() {
            @Override
            public void onClick(Data data, int position) {
                Intent intent = new Intent(activity, FBVideoActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra(Constants.KEY_UIZA_ENTITY_ID, data.getId());
                intent.putExtra(Constants.KEY_UIZA_THUMBNAIL, data.getThumbnail());
                startActivity(intent);
            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(fbVideoAdapter);
        listAllEntity();
    }

    private void findViews() {
        nsv = (NestedScrollView) findViewById(R.id.nsv);
        recyclerView = (RecyclerView) findViewById(R.id.rv);
        cvPlaylistFolder = (CardView) findViewById(R.id.cv_playlist_folder);
    }

    private boolean isMiniPlayerInitSuccess;
    private boolean mIsRestoredToTop;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        isMiniPlayerInitSuccess = intent.getBooleanExtra(FBVideoActivity.TAG_IS_MINI_PLAYER_INIT_SUCCESS, false);
        if ((intent.getFlags() | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT) > 0) {
            mIsRestoredToTop = true;
        }
    }

    @Override
    public void finish() {
        super.finish();
        UZUtil.moveTaskToFront(activity, mIsRestoredToTop);
    }

    @Override
    public void onBackPressed() {
        if (isMiniPlayerInitSuccess) {
            //in this example, I use static FBVideoActivity instance for faster calling.
            //you must find a better way to finish FBVideoActivity in your production
            FBVideoActivity.getInstance().finish();
        }
        super.onBackPressed();
    }

    private void listAllEntity() {
        UZService service = UZRestClient.createService(UZService.class);
        String metadataId = "";
        int limit = 50;
        int page = 0;
        String orderBy = "createdAt";
        String orderType = "DESC";
        UZAPIMaster.getInstance().subscribe(service.getListAllEntity(UZData.getInstance().getAPIVersion(), metadataId, limit, page, orderBy, orderType, "success", UZData.getInstance().getAppId()), new ApiSubscriber<ResultListEntity>() {
            @Override
            public void onSuccess(ResultListEntity result) {
                dataList.addAll(result.getData());
                cvPlaylistFolder.setVisibility(View.VISIBLE);
                fbVideoAdapter.notifyDataSetChanged();
                findViewById(R.id.pb).setVisibility(View.GONE);
            }

            @Override
            public void onFail(Throwable e) {
            }
        });
    }

    @Override
    protected void onDestroy() {
        UZUtil.stopMiniPlayer(activity);//stop mini player
        super.onDestroy();
    }
}
