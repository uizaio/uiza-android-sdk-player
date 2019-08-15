package testlibuiza.sample.v3.api;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import io.uiza.core.api.UzApiMaster;
import io.uiza.core.api.UzServiceApi;
import io.uiza.core.api.client.UzRestClient;
import io.uiza.core.api.client.UzRestClientGetLinkPlay;
import io.uiza.core.api.request.streaming.StreamingTokenRequest;
import io.uiza.core.api.response.BasePaginationResponse;
import io.uiza.core.api.response.BaseResponse;
import io.uiza.core.api.response.ad.Ad;
import io.uiza.core.api.response.linkplay.LinkPlay;
import io.uiza.core.api.response.skin.Skin;
import io.uiza.core.api.response.streaming.LiveFeedViews;
import io.uiza.core.api.response.streaming.StreamingToken;
import io.uiza.core.api.response.video.VideoData;
import io.uiza.core.api.util.ApiSubscriber;
import io.uiza.core.util.LLog;
import io.uiza.core.util.UzDisplayUtil;
import io.uiza.core.util.constant.Constants;
import io.uiza.core.view.LToast;
import java.util.List;
import testlibuiza.R;
import testlibuiza.app.LSApplication;
import uizacoresdk.util.UZData;

public class UZTestAPIActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = getClass().getSimpleName();
    private Activity activity;
    private TextView tv;
    private final String entityIdDefaultVOD = "b7297b29-c6c4-4bd6-a74f-b60d0118d275";
    private final String entityIdDefaultLIVE = "45a908f7-a62e-4eaf-8ce2-dc5699f33406";
    private final String metadataDefault0 = "00932b61-1d39-45d2-8c7d-3d99ad9ea95a";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activity = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.v3_activity_test_api);
        tv = findViewById(R.id.tv);

        findViewById(R.id.bt_get_list_metadata).setOnClickListener(this);

        findViewById(R.id.bt_list_all_entity).setOnClickListener(this);
        findViewById(R.id.bt_list_all_entity_metadata).setOnClickListener(this);
        findViewById(R.id.bt_retrieve_an_entity).setOnClickListener(this);
        findViewById(R.id.bt_search_entity).setOnClickListener(this);

        findViewById(R.id.bt_get_token_streaming).setOnClickListener(this);
        findViewById(R.id.bt_get_link_play).setOnClickListener(this);
        findViewById(R.id.bt_retrieve_a_live_event).setOnClickListener(this);
        findViewById(R.id.bt_get_token_streaming_live).setOnClickListener(this);
        findViewById(R.id.bt_get_link_play_live).setOnClickListener(this);
        findViewById(R.id.bt_get_view_a_live_feed).setOnClickListener(this);
        findViewById(R.id.bt_get_time_start_live).setOnClickListener(this);

        findViewById(R.id.bt_list_skin).setOnClickListener(this);
        findViewById(R.id.bt_skin_config).setOnClickListener(this);
        findViewById(R.id.bt_ad).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        tv.setText("Loading...");
        switch (v.getId()) {
            case R.id.bt_get_list_metadata:
                getListMetadata();
                break;
            case R.id.bt_list_all_entity:
                listAllEntity();
                break;
            case R.id.bt_list_all_entity_metadata:
                listAllEntityMetadata();
                break;
            case R.id.bt_retrieve_an_entity:
                retrieveAnEntity();
                break;
            case R.id.bt_search_entity:
                searchAnEntity();
                break;
            case R.id.bt_get_token_streaming:
                getTokenStreaming();
                break;
            case R.id.bt_get_link_play:
                getLinkPlay();
                break;
            case R.id.bt_retrieve_a_live_event:
                retrieveALiveEvent();
                break;
            case R.id.bt_get_token_streaming_live:
                getTokenStreamingLive();
                break;
            case R.id.bt_get_link_play_live:
                getLinkPlayLive();
                break;
            case R.id.bt_get_view_a_live_feed:
                getViewALiveFeed();
                break;
            case R.id.bt_get_time_start_live:
                getTimeStartLive();
                break;
            case R.id.bt_list_skin:
                getListSkin();
                break;
            case R.id.bt_skin_config:
                getSkinConfig();
                break;
            case R.id.bt_ad:
                getIMAAd();
                break;
        }
    }

    private void showTv(Object o) {
        UzDisplayUtil.printBeautyJson(o, tv);
    }

    private void getListMetadata() {
        UzServiceApi service = UzRestClient.createService(UzServiceApi.class);
        UzApiMaster.getInstance()
                .subscribe(service.getListMetadata(UZData.getInstance().getAPIVersion(), 100, 1),
                        new ApiSubscriber<BasePaginationResponse<List<VideoData>>>() {
                            @Override
                            public void onSuccess(
                                    BasePaginationResponse<List<VideoData>> response) {
                                LLog.d(TAG,
                                        "getListMetadata onSuccess: " + LSApplication.getInstance()
                                                .getGson().toJson(response));
                                showTv(response);
                            }

                            @Override
                            public void onFail(Throwable e) {
                                LLog.e(TAG, "checkToken onFail " + e.getMessage());
                                showTv(e.getMessage());
                            }
                        });
    }

    private void listAllEntity() {
        UzServiceApi service = UzRestClient.createService(UzServiceApi.class);
        String metadataId = "";
        int limit = 50;
        int page = 0;
        String orderBy = "createdAt";
        String orderType = "DESC";
        UzApiMaster.getInstance().subscribe(
                service.getListAllEntity(UZData.getInstance().getAPIVersion(), metadataId, limit,
                        page, orderBy, orderType, "success", UZData.getInstance().getAppId()),
                new ApiSubscriber<BasePaginationResponse<List<VideoData>>>() {
                    @Override
                    public void onSuccess(BasePaginationResponse<List<VideoData>> response) {
                        LLog.d(TAG, "getListAllEntity onSuccess: " + LSApplication.getInstance()
                                .getGson().toJson(response));
                        showTv(response);
                    }

                    @Override
                    public void onFail(Throwable e) {
                        LLog.e(TAG, "getListAllEntity onFail " + e.getMessage());
                        showTv(e.getMessage());
                    }
                });
    }

    private void listAllEntityMetadata() {
        UzServiceApi service = UzRestClient.createService(UzServiceApi.class);
        String metadataId = "74cac724-968c-4e6d-a6e1-6c2365e41d9d";
        int limit = 50;
        int page = 0;
        String orderBy = "createdAt";
        String orderType = "DESC";
        UzApiMaster.getInstance().subscribe(
                service.getListAllEntity(UZData.getInstance().getAPIVersion(), metadataId, limit,
                        page, orderBy, orderType, "success", UZData.getInstance().getAppId()),
                new ApiSubscriber<BasePaginationResponse<List<VideoData>>>() {
                    @Override
                    public void onSuccess(BasePaginationResponse<List<VideoData>> response) {
                        LLog.d(TAG, "getListAllEntity onSuccess: " + LSApplication.getInstance()
                                .getGson().toJson(response));
                        showTv(response);
                    }

                    @Override
                    public void onFail(Throwable e) {
                        LLog.e(TAG, "getListAllEntity onFail " + e.getMessage());
                        showTv(e.getMessage());
                    }
                });
    }

    private void retrieveAnEntity() {
        UzServiceApi service = UzRestClient.createService(UzServiceApi.class);
        String id = "7789b7cc-9fd8-499b-bd35-745d133b6089";
        UzApiMaster.getInstance().subscribe(
                service.retrieveAnEntity(UZData.getInstance().getAPIVersion(), id,
                        UZData.getInstance().getAppId()),
                new ApiSubscriber<BaseResponse<VideoData>>() {
                    @Override
                    public void onSuccess(BaseResponse<VideoData> response) {
                        LLog.d(TAG, "retrieveAnEntity onSuccess: " + LSApplication.getInstance()
                                .getGson().toJson(response));
                        showTv(response);
                    }

                    @Override
                    public void onFail(Throwable e) {
                        LLog.e(TAG, "retrieveAnEntity onFail " + e.getMessage());
                        showTv(e.getMessage());
                    }
                });
    }

    private void searchAnEntity() {
        UzServiceApi service = UzRestClient.createService(UzServiceApi.class);
        String keyword = "a";
        UzApiMaster.getInstance()
                .subscribe(service.searchEntity(UZData.getInstance().getAPIVersion(), keyword),
                        new ApiSubscriber<BasePaginationResponse<List<VideoData>>>() {
                            @Override
                            public void onSuccess(
                                    BasePaginationResponse<List<VideoData>> response) {
                                LLog.d(TAG,
                                        "searchAnEntity onSuccess: " + LSApplication.getInstance()
                                                .getGson().toJson(response));
                                showTv(response);
                            }

                            @Override
                            public void onFail(Throwable e) {
                                LLog.e(TAG, "searchAnEntity onFail " + e.getMessage());
                                showTv(e.getMessage());
                            }
                        });
    }

    private void getTokenStreaming() {
        UzServiceApi service = UzRestClient.createService(UzServiceApi.class);
        StreamingTokenRequest streamingTokenRequest = new StreamingTokenRequest();
        streamingTokenRequest.setAppId(UZData.getInstance().getAppId());
        streamingTokenRequest.setEntityId(entityIdDefaultVOD);
        streamingTokenRequest.setContentType(StreamingTokenRequest.STREAM);
        UzApiMaster.getInstance().subscribe(
                service.getTokenStreaming(UZData.getInstance().getAPIVersion(),
                        streamingTokenRequest), new ApiSubscriber<BaseResponse<StreamingToken>>() {
                    @Override
                    public void onSuccess(BaseResponse<StreamingToken> response) {
                        LLog.d(TAG, "getTokenStreaming onSuccess: " + LSApplication.getInstance()
                                .getGson().toJson(response));
                        showTv(response);
                        tokenStreaming = response.getData().getToken();
                    }

                    @Override
                    public void onFail(Throwable e) {
                        LLog.e(TAG, "getTokenStreaming onFail " + e.getMessage());
                        showTv(e.getMessage());
                    }
                });
    }

    private String tokenStreaming;//value received from api getTokenStreaming

    private void getLinkPlay() {
        if (tokenStreaming == null || tokenStreaming.isEmpty()) {
            LToast.show(activity, "Token streaming not found, pls call getTokenStreaming before.");
            return;
        }
        UzRestClientGetLinkPlay.addAuthorization(tokenStreaming);
        UzServiceApi service = UzRestClientGetLinkPlay.createService(UzServiceApi.class);
        String appId = UZData.getInstance().getAppId();
        String typeContent = StreamingTokenRequest.STREAM;
        UzApiMaster.getInstance()
                .subscribe(service.getLinkPlay(appId, entityIdDefaultVOD, typeContent),
                        new ApiSubscriber<BaseResponse<LinkPlay>>() {
                            @Override
                            public void onSuccess(BaseResponse<LinkPlay> response) {
                                LLog.d(TAG, "getLinkPlay onSuccess: " + LSApplication.getInstance()
                                        .getGson().toJson(response));
                                showTv(response);
                            }

                            @Override
                            public void onFail(Throwable e) {
                                LLog.e(TAG, "getLinkPlay onFail " + e.getMessage());
                                showTv(e.getMessage());
                            }
                        });
    }

    private void retrieveALiveEvent() {
        UzServiceApi service = UzRestClient.createService(UzServiceApi.class);
        int limit = 50;
        int page = 0;
        String orderBy = "createdAt";
        String orderType = "DESC";
        UzApiMaster.getInstance().subscribe(
                service.retrieveALiveEvent(UZData.getInstance().getAPIVersion(), limit, page,
                        orderBy, orderType, UZData.getInstance().getAppId()),
                new ApiSubscriber<BasePaginationResponse<List<VideoData>>>() {
                    @Override
                    public void onSuccess(BasePaginationResponse<List<VideoData>> response) {
                        LLog.d(TAG, "retrieveALiveEvent onSuccess: " + LSApplication.getInstance()
                                .getGson().toJson(response));
                        showTv(response);
                    }

                    @Override
                    public void onFail(Throwable e) {
                        LLog.e(TAG, "retrieveALiveEvent onFail " + e.getMessage());
                        showTv(e.getMessage());
                    }
                });
    }

    private String tokenStreamingLive;//value received from api getTokenStreamingLive

    private void getTokenStreamingLive() {
        UzServiceApi service = UzRestClient.createService(UzServiceApi.class);
        StreamingTokenRequest streamingTokenRequest = new StreamingTokenRequest();
        streamingTokenRequest.setAppId(UZData.getInstance().getAppId());
        streamingTokenRequest.setEntityId(entityIdDefaultLIVE);
        streamingTokenRequest.setContentType(StreamingTokenRequest.LIVE);
        UzApiMaster.getInstance().subscribe(
                service.getTokenStreaming(UZData.getInstance().getAPIVersion(),
                        streamingTokenRequest), new ApiSubscriber<BaseResponse<StreamingToken>>() {
                    @Override
                    public void onSuccess(BaseResponse<StreamingToken> response) {
                        LLog.d(TAG,
                                "getTokenStreamingLive onSuccess: " + LSApplication.getInstance()
                                        .getGson().toJson(response));
                        showTv(response);
                        tokenStreamingLive = response.getData().getToken();
                    }

                    @Override
                    public void onFail(Throwable e) {
                        LLog.e(TAG, "getTokenStreamingLive onFail " + e.getMessage());
                        showTv(e.getMessage());
                    }
                });
    }

    private void getLinkPlayLive() {
        if (tokenStreamingLive == null || tokenStreamingLive.isEmpty()) {
            LToast.show(activity,
                    "Token streaming not found, pls call getTokenStreamingLive before.");
            return;
        }
        UzRestClientGetLinkPlay.addAuthorization(tokenStreamingLive);
        UzServiceApi service = UzRestClientGetLinkPlay.createService(UzServiceApi.class);
        String appId = UZData.getInstance().getAppId();
        String streamName = "ffdfdfdfd";
        UzApiMaster.getInstance().subscribe(service.getLinkPlayLive(appId, streamName),
                new ApiSubscriber<BaseResponse<LinkPlay>>() {
                    @Override
                    public void onSuccess(BaseResponse<LinkPlay> response) {
                        LLog.d(TAG, "getLinkPlayLive onSuccess: " + LSApplication.getInstance()
                                .getGson().toJson(response));
                        showTv(response);
                    }

                    @Override
                    public void onFail(Throwable e) {
                        LLog.e(TAG, "getLinkPlayLive onFail " + e.getMessage());
                        showTv(e.getMessage());
                    }
                });
    }

    private void getViewALiveFeed() {
        UzServiceApi service = UzRestClient.createService(UzServiceApi.class);
        String id = "8e133d0d-5f67-45e8-8812-44b2ddfd9fe2";
        UzApiMaster.getInstance().subscribe(
                service.getViewALiveFeed(UZData.getInstance().getAPIVersion(), id,
                        UZData.getInstance().getAppId()),
                new ApiSubscriber<BaseResponse<LiveFeedViews>>() {
                    @Override
                    public void onSuccess(BaseResponse<LiveFeedViews> response) {
                        if (response == null) {
                            return;
                        }
                        LLog.d(TAG, "getViewALiveFeed onSuccess: " + LSApplication.getInstance()
                                .getGson().toJson(response));
                        showTv(response);
                    }

                    @Override
                    public void onFail(Throwable e) {
                        LLog.e(TAG, "getViewALiveFeed onFail " + e.getMessage());
                        showTv(e.getMessage());
                    }
                });
    }

    private void getTimeStartLive() {
        UzServiceApi service = UzRestClient.createService(UzServiceApi.class);
        String entityId = "8e133d0d-5f67-45e8-8812-44b2ddfd9fe2";
        String feedId = "46fc46f4-8bc0-4d7f-a380-9515d8259af3";
        UzApiMaster.getInstance().subscribe(
                service.getTimeStartLive(UZData.getInstance().getAPIVersion(), entityId, feedId,
                        UZData.getInstance().getAppId()),
                new ApiSubscriber<BaseResponse<VideoData>>() {
                    @Override
                    public void onSuccess(BaseResponse<VideoData> result) {
                        LLog.d(TAG, "getTimeStartLive onSuccess: " + LSApplication.getInstance()
                                .getGson().toJson(result));
                        showTv(result);
                    }

                    @Override
                    public void onFail(Throwable e) {
                        LLog.e(TAG, "getTimeStartLive onFail " + e.getMessage());
                        showTv(e.getMessage());
                    }
                });
    }

    private void getListSkin() {
        UzServiceApi service = UzRestClient.createService(UzServiceApi.class);
        UzApiMaster.getInstance().subscribe(
                service.getListSkin(UZData.getInstance().getAPIVersion(),
                        Constants.PLATFORM_ANDROID),
                new ApiSubscriber<BasePaginationResponse<List<Skin>>>() {
                    @Override
                    public void onSuccess(BasePaginationResponse<List<Skin>> result) {
                        LLog.d(TAG,
                                "getListSkin onSuccess: " + LSApplication.getInstance().getGson()
                                        .toJson(result));
                        showTv(result);
                    }

                    @Override
                    public void onFail(Throwable e) {
                        LLog.e(TAG, "getListSkin onFail " + e.getMessage());
                        showTv(e.getMessage());
                    }
                });
    }

    private void getSkinConfig() {
        UzServiceApi service = UzRestClient.createService(UzServiceApi.class);
        UzApiMaster.getInstance().subscribe(
                service.getSkinConfig(UZData.getInstance().getAPIVersion(),
                        "645cd2a2-9216-4f5d-a73b-37d3e3034798"), new ApiSubscriber<Object>() {
                    @Override
                    public void onSuccess(Object result) {
                        LLog.d(TAG,
                                "getSkinConfig onSuccess: " + LSApplication.getInstance().getGson()
                                        .toJson(result));
                        showTv(result);
                    }

                    @Override
                    public void onFail(Throwable e) {
                        LLog.e(TAG, "getSkinConfig onFail " + e.getMessage());
                        showTv(e.getMessage());
                    }
                });
    }

    private void getIMAAd() {
        UzServiceApi service = UzRestClient.createService(UzServiceApi.class);
        UzApiMaster.getInstance().subscribe(
                service.getCuePoint(UZData.getInstance().getAPIVersion(),
                        "0e8254fa-afa1-491f-849b-5aa8bc7cce52", UZData.getInstance().getAppId()),
                new ApiSubscriber<BasePaginationResponse<List<Ad>>>() {
                    @Override
                    public void onSuccess(BasePaginationResponse<List<Ad>> response) {
                        LLog.d(TAG, "getIMAAd onSuccess: " + LSApplication.getInstance().getGson()
                                .toJson(response));
                        showTv(response);
                    }

                    @Override
                    public void onFail(Throwable e) {
                        LLog.e(TAG, "getIMAAd onFail " + e.getMessage());
                        showTv(e.getMessage());
                    }
                });
    }
}
