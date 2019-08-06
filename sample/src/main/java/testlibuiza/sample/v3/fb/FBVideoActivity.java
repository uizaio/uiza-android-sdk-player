package testlibuiza.sample.v3.fb;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowInsets;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import testlibuiza.R;
import uizacoresdk.interfaces.UZCallback;
import uizacoresdk.interfaces.UZItemClick;
import uizacoresdk.util.UZData;
import uizacoresdk.util.UZUtil;
import uizacoresdk.view.rl.video.UZVideo;
import vn.uiza.core.common.Constants;
import vn.uiza.core.exception.UZException;
import vn.uiza.core.utilities.LImageUtil;
import vn.uiza.core.utilities.LScreenUtil;
import vn.uiza.core.utilities.LUIUtil;
import vn.uiza.restapi.UZAPIMaster;
import vn.uiza.restapi.restclient.UZRestClient;
import vn.uiza.restapi.uiza.UZService;
import vn.uiza.restapi.uiza.model.v3.linkplay.getlinkplay.ResultGetLinkPlay;
import vn.uiza.restapi.uiza.model.v3.metadata.getdetailofmetadata.Data;
import vn.uiza.restapi.uiza.model.v3.videoondeman.listallentity.ResultListEntity;
import vn.uiza.rxandroid.ApiSubscriber;

/**
 * Created by loitp on 4/1/2019.
 */

public class FBVideoActivity extends AppCompatActivity implements UZCallback, UZItemClick {
    private final String TAG = getClass().getSimpleName();
    public final static String TAG_IS_MINI_PLAYER_INIT_SUCCESS = "TAG_IS_MINI_PLAYER_INIT_SUCCESS";
    private static FBVideoActivity activity;
    private UZVideo uzVideo;
    private Button btMini;
    private TextView tvLoadingMiniPlayer;
    private TextView tv;
    private ImageView iv;
    private boolean mIsRestoredToTop;

    public static FBVideoActivity getInstance() {
        return activity;
    }

    private void findViews() {
        uzVideo = findViewById(R.id.uiza_video);
        tvLoadingMiniPlayer = findViewById(R.id.tv_loading_mini_player);
        tv = findViewById(R.id.tv);
        iv = findViewById(R.id.iv);
        btMini = findViewById(R.id.bt_mini);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        UZUtil.setCasty(this);
        activity = this;
        UZUtil.setCurrentPlayerId(R.layout.fb_skin_main);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fb);
        findViews();
        uzVideo.setAutoSwitchItemPlaylistFolder(true);
        uzVideo.setAutoStart(true);
        uzVideo.addUZCallback(this);
        uzVideo.addItemClick(this);
        LUIUtil.setTextShadow(tvLoadingMiniPlayer);
        // Sample for set size PiP
        UZUtil.setMiniPlayerSizeDp(this, false, 140, 220);
        // Sample for single click to full player
        // UzLivestreamUtil.setMiniPlayerTapToFullPlayer(this, false);
        // Sample for control buttons skin
        uzVideo.setPipControlSkin(R.layout.layout_floating_controls_skin);
        btMini.setOnClickListener(view -> uzVideo.showPip());
        checkId(getIntent());
        getDummyData();
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
                @Override
                public WindowInsets onApplyWindowInsets(View v, WindowInsets insets) {
                    getWindow().getDecorView().setOnApplyWindowInsetsListener(null);
                    int stableInsetTop = insets.getStableInsetTop();
                    UZUtil.setStablePipTopPosition(FBVideoActivity.this, stableInsetTop);
                    return v.onApplyWindowInsets(insets);
                }
            });
        } else {
            UZUtil.setStablePipTopPosition(FBVideoActivity.this, LScreenUtil.getStatusBarHeight(this));
        }
    }

    private void checkId(Intent intent) {
        if (intent == null) {
            return;
        }
        String thumb = intent.getStringExtra(Constants.KEY_UIZA_THUMBNAIL);
        uzVideo.setUrlImgThumbnail(thumb);
        String metadataId = intent.getStringExtra(Constants.KEY_UIZA_METADATA_ENTITY_ID);
        if (metadataId == null) {
            String entityId = intent.getStringExtra(Constants.KEY_UIZA_ENTITY_ID);
            if (entityId == null) {
                boolean isInitWithPlaylistFolder = UZUtil.isInitPlaylistFolder(activity);
                if (isInitWithPlaylistFolder) {
                    UZUtil.initPlaylistFolder(activity, uzVideo, null);
                } else {
                    UZUtil.initEntity(activity, uzVideo, null);
                }
            } else {
                UZUtil.initEntity(activity, uzVideo, entityId);
            }
        } else {
            UZUtil.initPlaylistFolder(activity, uzVideo, metadataId);
        }
    }

    @Override
    public void onDestroy() {
        uzVideo.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onResume() {
        uzVideo.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        uzVideo.onPause();
        super.onPause();
    }

    @Override
    public void isInitResult(boolean isInitSuccess, boolean isGetDataSuccess, ResultGetLinkPlay resultGetLinkPlay, Data data) {
        if (isInitSuccess) {
            initDone();
        } else {
            btMini.setVisibility(View.INVISIBLE);
        }
    }

    private void initDone() {
        btMini.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemClick(View view) {
        switch (view.getId()) {
            case R.id.exo_back_screen:
                if (!uzVideo.isLandscape()) {
                    onBackPressed();
                }
                break;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        btMini.setVisibility(View.INVISIBLE);
        String thumb = intent.getStringExtra(Constants.KEY_UIZA_THUMBNAIL);
        if (uzVideo.isInitNewItem(thumb)) {
            checkId(intent);
        } else {
            initDone();
        }
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
    public void onStateMiniPlayer(boolean isInitMiniPlayerSuccess) {
        if (isInitMiniPlayerSuccess) {
            //mini player is init success
            tvLoadingMiniPlayer.setVisibility(View.GONE);
            Intent intent = new Intent(activity, FBListVideoActivity.class);
            intent.putExtra(TAG_IS_MINI_PLAYER_INIT_SUCCESS, true);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        } else {
            //open mini player
            btMini.setVisibility(View.INVISIBLE);
            tvLoadingMiniPlayer.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        uzVideo.onActivityResult(resultCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSkinChange() {
    }

    @Override
    public void onScreenRotate(boolean isLandscape) {
    }

    @Override
    public void onError(UZException e) {
    }

    @Override
    public void onBackPressed() {
        if (uzVideo.isLandscape()) {
            uzVideo.toggleFullscreen();
        } else {
            super.onBackPressed();
        }
    }

    //only for testing
    private void getDummyData() {
        UZService service = UZRestClient.createService(UZService.class);
        String metadataId = "";
        int limit = 50;
        int page = 0;
        String orderBy = "createdAt";
        String orderType = "DESC";
        UZAPIMaster.getInstance().subscribe(service.getListAllEntity(UZData.getInstance().getAPIVersion(), metadataId, limit, page, orderBy, orderType, "success", UZData.getInstance().getAppId()), new ApiSubscriber<ResultListEntity>() {
            @Override
            public void onSuccess(ResultListEntity result) {
                LUIUtil.printBeautyJson(result, tv);
                LImageUtil.load(activity, "https://motosaigon.vn/wp-content/uploads/2018/08/Kawasaki-Z1000-2019-Z1000R-2019-MotoSaigon.vn-2.jpg", iv);
            }

            @Override
            public void onFail(Throwable e) {
            }
        });
    }
}
