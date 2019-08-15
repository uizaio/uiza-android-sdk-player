package testlibuiza.sample.v3.fb;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import io.uiza.core.api.UzApiMaster;
import io.uiza.core.api.UzServiceApi;
import io.uiza.core.api.client.UzRestClient;
import io.uiza.core.api.response.BasePaginationResponse;
import io.uiza.core.api.response.linkplay.LinkPlay;
import io.uiza.core.api.response.video.VideoData;
import io.uiza.core.api.util.ApiSubscriber;
import io.uiza.core.exception.UzException;
import io.uiza.core.util.UzDisplayUtil;
import io.uiza.core.util.UzImageUtil;
import io.uiza.core.util.constant.Constants;
import io.uiza.player.UzPlayer;
import io.uiza.player.UzPlayerConfig;
import io.uiza.player.interfaces.UzItemClickListener;
import io.uiza.player.interfaces.UzPlayerEventListener;
import io.uiza.player.interfaces.UzPlayerUiEventListener;
import io.uiza.player.mini.pip.PipHelper;
import io.uiza.player.util.UzPlayerData;
import java.util.List;
import testlibuiza.R;

public class FBVideoActivity extends AppCompatActivity implements UzPlayerUiEventListener,
        UzPlayerEventListener, UzItemClickListener {

    public static final String TAG_IS_MINI_PLAYER_INIT_SUCCESS = "TAG_IS_MINI_PLAYER_INIT_SUCCESS";
    private static FBVideoActivity activity;
    private UzPlayer uzPlayer;
    private Button btMini;
    private TextView tvLoadingMiniPlayer;
    private TextView tv;
    private ImageView iv;
    private boolean mIsRestoredToTop;

    public static FBVideoActivity getInstance() {
        return activity;
    }

    private void findViews() {
        uzPlayer = findViewById(R.id.uiza_video);
        tvLoadingMiniPlayer = findViewById(R.id.tv_loading_mini_player);
        tv = findViewById(R.id.tv);
        iv = findViewById(R.id.iv);
        btMini = findViewById(R.id.bt_mini);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        UzPlayerConfig.setCasty(this);
        activity = this;
        UzPlayerConfig.setCurrentSkinRes(R.layout.fb_skin_main);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fb);
        findViews();
        uzPlayer.setAutoSwitchItemPlaylistFolder(true);
        uzPlayer.setAutoStart(true);
        uzPlayer.setUzPlayerUiEventListener(this);
        uzPlayer.setUzPlayerEventListener(this);
        uzPlayer.setUzItemClickListener(this);
        UzDisplayUtil.setTextShadow(tvLoadingMiniPlayer);
        // Sample for set size PiP
        PipHelper.setMiniPlayerSizeDp(this, false, 140, 220);
        // Sample for single click to full player
        // UzLivestreamUtil.setMiniPlayerTapToFullPlayer(this, false);
        // Sample for control buttons skin
        uzPlayer.setPipControlSkin(R.layout.layout_floating_controls_skin);
        btMini.setOnClickListener(view -> uzPlayer.showPip());
        checkId(getIntent());
        getDummyData();
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView()
                    .setOnApplyWindowInsetsListener((v, insets) -> {
                        getWindow().getDecorView().setOnApplyWindowInsetsListener(null);
                        int stableInsetTop = insets.getStableInsetTop();
                        PipHelper.setStablePipTopPosition(FBVideoActivity.this, stableInsetTop);
                        return v.onApplyWindowInsets(insets);
                    });
        } else {
            PipHelper.setStablePipTopPosition(FBVideoActivity.this,
                    UzDisplayUtil.getStatusBarHeight(this));
        }
    }

    private void checkId(Intent intent) {
        if (intent == null) {
            return;
        }
        String thumb = intent.getStringExtra(Constants.KEY_UIZA_THUMBNAIL);
        uzPlayer.setUrlImgThumbnail(thumb);
        String metadataId = intent.getStringExtra(Constants.KEY_UIZA_METADATA_ENTITY_ID);
        if (metadataId == null) {
            String entityId = intent.getStringExtra(Constants.KEY_UIZA_ENTITY_ID);
            if (entityId == null) {
                boolean isInitWithPlaylistFolder = UzPlayerData.isInitPlaylistFolder(activity);
                if (isInitWithPlaylistFolder) {
                    UzPlayerConfig.initPlaylistFolder(uzPlayer, null);
                } else {
                    UzPlayerConfig.initVodEntity(uzPlayer, null);
                }
            } else {
                UzPlayerConfig.initVodEntity(uzPlayer, entityId);
            }
        } else {
            UzPlayerConfig.initPlaylistFolder(uzPlayer, metadataId);
        }
    }

    @Override
    public void onDestroy() {
        uzPlayer.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onResume() {
        uzPlayer.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        uzPlayer.onPause();
        super.onPause();
    }

    @Override
    public void onDataInitialized(boolean initSuccess, boolean getDataSuccess, LinkPlay linkPlay,
            VideoData data) {
        if (initSuccess) {
            initDone();
        } else {
            btMini.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

    }

    @Override
    public void onVideoProgress(long currentMls, int s, long duration, int percent) {

    }

    @Override
    public void onBufferProgress(long bufferedPosition, int bufferedPercentage, long duration) {

    }

    @Override
    public void onVideoEnded() {

    }

    @Override
    public void onPlayerError(UzException exception) {

    }

    @Override
    public void onSkinChanged() {

    }

    @Override
    public void onPlayerRotated(boolean isLandscape) {

    }

    private void initDone() {
        btMini.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemClick(View view) {
        switch (view.getId()) {
            case R.id.exo_back_screen:
                if (!uzPlayer.isLandscape()) {
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
        if (uzPlayer.isInitNewItem(thumb)) {
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
        PipHelper.moveTaskToFront(activity, mIsRestoredToTop);
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
        uzPlayer.onActivityResult(resultCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        if (uzPlayer.isLandscape()) {
            uzPlayer.toggleFullscreen();
        } else {
            super.onBackPressed();
        }
    }

    //only for testing
    private void getDummyData() {
        UzServiceApi service = UzRestClient.createService(UzServiceApi.class);
        String metadataId = "";
        int limit = 50;
        int page = 0;
        String orderBy = "createdAt";
        String orderType = "DESC";
        UzApiMaster.getInstance().subscribe(
                service.getListAllEntity(UzPlayerData.getInstance().getApiVersion(), metadataId,
                        limit,
                        page, orderBy, orderType, "success", UzPlayerData.getInstance().getAppId()),
                new ApiSubscriber<BasePaginationResponse<List<VideoData>>>() {
                    @Override
                    public void onSuccess(BasePaginationResponse<List<VideoData>> response) {
                        UzDisplayUtil.printBeautyJson(response, tv);
                        UzImageUtil.load(activity,
                                "https://motosaigon.vn/wp-content/uploads/2018/08/Kawasaki-Z1000-2019-Z1000R-2019-MotoSaigon.vn-2.jpg",
                                iv);
                    }

                    @Override
                    public void onFail(Throwable e) {
                    }
                });
    }
}
