package testlibuiza.sample.v3.fb;

import android.app.Activity;
import android.app.ActivityManager;
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
import uizacoresdk.util.UZUtil;
import vn.uiza.core.common.Constants;
import vn.uiza.core.utilities.LLog;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LLog.d(TAG, "fuck onCreate");
        activity = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fb_list_video);
        NestedScrollView nsv = (NestedScrollView) findViewById(R.id.nsv);
        nsv.setNestedScrollingEnabled(false);
        recyclerView = (RecyclerView) findViewById(R.id.rv);
        cvPlaylistFolder = (CardView) findViewById(R.id.cv_playlist_folder);
        cvPlaylistFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, FBVideoActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
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
                startActivity(intent);
            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(fbVideoAdapter);
        listAllEntity();
    }

    private boolean isMiniPlayerInitSuccess;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        isMiniPlayerInitSuccess = intent.getBooleanExtra(FBVideoActivity.TAG_IS_MINI_PLAYER_INIT_SUCCESS, false);
        LLog.d(TAG, "fuck onNewIntent isMiniPlayerInitSuccess: " + isMiniPlayerInitSuccess);
        if ((intent.getFlags() | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT) > 0) {
            mIsRestoredToTop  = true;
        }
    }
    private boolean mIsRestoredToTop;

    @Override
    public void finish() {
        super.finish();
        if (android.os.Build.VERSION.SDK_INT >= 19 && !isTaskRoot() && mIsRestoredToTop) {
            // 4.4.2 platform issues for FLAG_ACTIVITY_REORDER_TO_FRONT,
            // reordered activity back press will go to home unexpectly,
            // Workaround: move reordered activity current task to front when it's finished.
            ActivityManager tasksManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            tasksManager.moveTaskToFront(getTaskId(), ActivityManager.MOVE_TASK_NO_USER_ACTION);
        }
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
        UZAPIMaster.getInstance().subscribe(service.getListAllEntity(metadataId, limit, page, orderBy, orderType, "success"), new ApiSubscriber<ResultListEntity>() {
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
        LLog.d(TAG, "fuck onDestroy");
        UZUtil.stopMiniPlayer(activity);//stop mini player
        super.onDestroy();
    }
}
