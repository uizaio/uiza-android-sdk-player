package testlibuiza.sample.v3.api;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import testlibuiza.R;
import testlibuiza.app.LSApplication;
import vn.loitp.core.base.BaseActivity;
import vn.loitp.core.utilities.LLog;
import vn.loitp.core.utilities.LUIUtil;
import vn.loitp.restapi.restclient.RestClientV3;
import vn.loitp.restapi.restclient.RestClientV3GetLinkPlay;
import vn.loitp.restapi.uiza.UizaServiceV3;
import vn.loitp.restapi.uiza.model.v3.UizaWorkspaceInfo;
import vn.loitp.restapi.uiza.model.v3.authentication.gettoken.ResultGetToken;
import vn.loitp.restapi.uiza.model.v3.linkplay.getlinkplay.ResultGetLinkPlay;
import vn.loitp.restapi.uiza.model.v3.linkplay.gettokenstreaming.ResultGetTokenStreaming;
import vn.loitp.restapi.uiza.model.v3.linkplay.gettokenstreaming.SendGetTokenStreaming;
import vn.loitp.restapi.uiza.model.v3.livestreaming.retrievealiveevent.ResultRetrieveALiveEvent;
import vn.loitp.restapi.uiza.model.v3.metadata.createmetadata.CreateMetadata;
import vn.loitp.restapi.uiza.model.v3.metadata.createmetadata.ResultCreateMetadata;
import vn.loitp.restapi.uiza.model.v3.metadata.deleteanmetadata.ResultDeleteAnMetadata;
import vn.loitp.restapi.uiza.model.v3.metadata.getdetailofmetadata.ResultGetDetailOfMetadata;
import vn.loitp.restapi.uiza.model.v3.metadata.getlistmetadata.ResultGetListMetadata;
import vn.loitp.restapi.uiza.model.v3.metadata.updatemetadata.ResultUpdateMetadata;
import vn.loitp.restapi.uiza.model.v3.videoondeman.listallentity.ResultListEntity;
import vn.loitp.restapi.uiza.model.v3.videoondeman.retrieveanentity.ResultRetrieveAnEntity;
import vn.loitp.restapi.uiza.util.UizaV3Util;
import vn.loitp.rxandroid.ApiSubscriber;
import vn.loitp.views.LToast;

public class V3TestAPIActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tv = (TextView) findViewById(R.id.tv);
        findViewById(R.id.bt_get_token).setOnClickListener(this);
        findViewById(R.id.bt_check_token).setOnClickListener(this);
        findViewById(R.id.bt_get_list_metadata).setOnClickListener(this);
        findViewById(R.id.bt_create_metadata).setOnClickListener(this);
        findViewById(R.id.bt_get_detail_of_metadata).setOnClickListener(this);
        findViewById(R.id.bt_update_metadata).setOnClickListener(this);
        findViewById(R.id.bt_delete_an_metadata).setOnClickListener(this);
        findViewById(R.id.bt_list_all_entity).setOnClickListener(this);
        findViewById(R.id.bt_list_all_entity_metadata).setOnClickListener(this);
        findViewById(R.id.bt_retrieve_an_entity).setOnClickListener(this);
        findViewById(R.id.bt_search_entity).setOnClickListener(this);
        findViewById(R.id.bt_get_token_streaming).setOnClickListener(this);
        findViewById(R.id.bt_get_link_play).setOnClickListener(this);
        findViewById(R.id.bt_retrieve_a_live_event).setOnClickListener(this);
        findViewById(R.id.bt_get_token_streaming_live).setOnClickListener(this);
        findViewById(R.id.bt_get_link_play_live).setOnClickListener(this);

        LinearLayout rootView = (LinearLayout) findViewById(R.id.root_view);
        viewList = rootView.getTouchables();
        setEnableAllButton(false);
    }

    private List<View> viewList = new ArrayList<>();

    private void setEnableAllButton(boolean isEnable) {
        LLog.d(TAG, "size: " + viewList.size());
        if (isEnable) {
            for (View view : viewList) {
                view.setEnabled(true);
            }
        } else {
            for (View view : viewList) {
                if (view.getId() != R.id.bt_get_token) {
                    view.setEnabled(false);
                }
            }
        }
    }

    @Override
    protected boolean setFullScreen() {
        return false;
    }

    @Override
    protected String setTag() {
        return getClass().getSimpleName();
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.v3_activity_test_api;
    }

    @Override
    public void onClick(View v) {
        tv.setText("Loading...");
        switch (v.getId()) {
            case R.id.bt_get_token:
                getToken();
                break;
            case R.id.bt_check_token:
                checkToken();
                break;
            case R.id.bt_get_list_metadata:
                getListMetadata();
                break;
            case R.id.bt_create_metadata:
                createMetadata();
                break;
            case R.id.bt_get_detail_of_metadata:
                getDetailOfMetadata();
                break;
            case R.id.bt_update_metadata:
                updateMetadata();
                break;
            case R.id.bt_delete_an_metadata:
                deleteAnMetadata();
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
        }
    }

    private void showTv(Object o) {
        LUIUtil.printBeautyJson(o, tv);
    }

    private void getToken() {
        UizaServiceV3 service = RestClientV3.createService(UizaServiceV3.class);
        UizaWorkspaceInfo uizaWorkspaceInfo = UizaV3Util.getUizaWorkspace(activity);
        if (uizaWorkspaceInfo == null) {
            return;
        }
        subscribe(service.getToken(uizaWorkspaceInfo), new ApiSubscriber<ResultGetToken>() {
            @Override
            public void onSuccess(ResultGetToken resultGetToken) {
                LLog.d(TAG, "getToken " + LSApplication.getInstance().getGson().toJson(resultGetToken));
                UizaV3Util.setResultGetToken(activity, resultGetToken);
                String token = resultGetToken.getData().getToken();
                LLog.d(TAG, "token: " + token);
                RestClientV3.addAuthorization(token);
                showTv(resultGetToken);
                setEnableAllButton(true);
            }

            @Override
            public void onFail(Throwable e) {
                LLog.e(TAG, "getToken onFail " + e.getMessage());
                showTv(e.getMessage());
            }
        });
    }

    private void checkToken() {
        UizaServiceV3 service = RestClientV3.createService(UizaServiceV3.class);
        subscribe(service.checkToken(), new ApiSubscriber<Object>() {
            @Override
            public void onSuccess(Object resultGetToken) {
                LLog.d(TAG, "checkToken onSuccess: " + LSApplication.getInstance().getGson().toJson(resultGetToken));
                showTv(resultGetToken);
            }

            @Override
            public void onFail(Throwable e) {
                LLog.e(TAG, "checkToken onFail " + e.getMessage());
                showTv(e.getMessage());
            }
        });
    }

    private void getListMetadata() {
        UizaServiceV3 service = RestClientV3.createService(UizaServiceV3.class);
        subscribe(service.getListMetadata(), new ApiSubscriber<ResultGetListMetadata>() {
            @Override
            public void onSuccess(ResultGetListMetadata resultGetListMetadata) {
                LLog.d(TAG, "getListMetadata onSuccess: " + LSApplication.getInstance().getGson().toJson(resultGetListMetadata));
                showTv(resultGetListMetadata);
            }

            @Override
            public void onFail(Throwable e) {
                LLog.e(TAG, "checkToken onFail " + e.getMessage());
                showTv(e.getMessage());
            }
        });
    }

    private void createMetadata() {
        UizaServiceV3 service = RestClientV3.createService(UizaServiceV3.class);
        CreateMetadata createMetadata = new CreateMetadata();
        createMetadata.setName("Loitp " + System.currentTimeMillis());
        createMetadata.setType(CreateMetadata.TYPE_FOLDER);
        createMetadata.setDescription("This is a description sentences");
        createMetadata.setOrderNumber(1);
        createMetadata.setIcon("/exemple.com/icon.png");
        subscribe(service.createMetadata(createMetadata), new ApiSubscriber<ResultCreateMetadata>() {
            @Override
            public void onSuccess(ResultCreateMetadata resultCreateMetadata) {
                LLog.d(TAG, "createMetadata onSuccess: " + LSApplication.getInstance().getGson().toJson(resultCreateMetadata));
                showTv(resultCreateMetadata);
            }

            @Override
            public void onFail(Throwable e) {
                LLog.e(TAG, "createMetadata onFail " + e.getMessage());
                showTv(e.getMessage());
            }
        });
    }

    private void getDetailOfMetadata() {
        UizaServiceV3 service = RestClientV3.createService(UizaServiceV3.class);
        String metadataId = "ce1a4735-99f4-4968-bf2a-3ba8063441f4";
        subscribe(service.getDetailOfMetadata(metadataId), new ApiSubscriber<ResultGetDetailOfMetadata>() {
            @Override
            public void onSuccess(ResultGetDetailOfMetadata resultGetDetailOfMetadata) {
                LLog.d(TAG, "getDetailOfMetadata onSuccess: " + LSApplication.getInstance().getGson().toJson(resultGetDetailOfMetadata));
                showTv(resultGetDetailOfMetadata);
            }

            @Override
            public void onFail(Throwable e) {
                LLog.e(TAG, "getDetailOfMetadata onFail " + e.getMessage());
                showTv(e.getMessage());
            }
        });
    }

    private void updateMetadata() {
        UizaServiceV3 service = RestClientV3.createService(UizaServiceV3.class);
        CreateMetadata createMetadata = new CreateMetadata();
        createMetadata.setId("ce1a4735-99f4-4968-bf2a-3ba8063441f4");
        createMetadata.setName("@@@Loitp Suzuki GSX S1000");
        createMetadata.setType(CreateMetadata.TYPE_PLAYLIST);
        createMetadata.setDescription("Update description");
        createMetadata.setOrderNumber(69);
        createMetadata.setIcon("/exemple.com/icon_002.png");
        subscribe(service.updateMetadata(createMetadata), new ApiSubscriber<ResultUpdateMetadata>() {
            @Override
            public void onSuccess(ResultUpdateMetadata result) {
                LLog.d(TAG, "updateMetadata onSuccess: " + LSApplication.getInstance().getGson().toJson(result));
                showTv(result);
            }

            @Override
            public void onFail(Throwable e) {
                LLog.e(TAG, "updateMetadata onFail " + e.getMessage());
                showTv(e.getMessage());
            }
        });
    }

    private void deleteAnMetadata() {
        UizaServiceV3 service = RestClientV3.createService(UizaServiceV3.class);
        String deleteMetadataId = "37b865b3-cf75-4faa-8507-180a9436d95d";
        subscribe(service.deleteAnMetadata(deleteMetadataId), new ApiSubscriber<ResultDeleteAnMetadata>() {
            @Override
            public void onSuccess(ResultDeleteAnMetadata result) {
                LLog.d(TAG, "updateMetadata onSuccess: " + LSApplication.getInstance().getGson().toJson(result));
                showTv(result);
            }

            @Override
            public void onFail(Throwable e) {
                LLog.e(TAG, "updateMetadata onFail " + e.getMessage());
                showTv(e.getMessage());
            }
        });
    }


    private void listAllEntity() {
        UizaServiceV3 service = RestClientV3.createService(UizaServiceV3.class);
        String metadataId = "";
        int limit = 50;
        int page = 0;
        String orderBy = "createdAt";
        String orderType = "DESC";
        subscribe(service.getListAllEntity(metadataId, limit, page, orderBy, orderType), new ApiSubscriber<ResultListEntity>() {
            @Override
            public void onSuccess(ResultListEntity result) {
                LLog.d(TAG, "getListAllEntity onSuccess: " + LSApplication.getInstance().getGson().toJson(result));
                showTv(result);
            }

            @Override
            public void onFail(Throwable e) {
                LLog.e(TAG, "getListAllEntity onFail " + e.getMessage());
                showTv(e.getMessage());
            }
        });
    }

    private void listAllEntityMetadata() {
        UizaServiceV3 service = RestClientV3.createService(UizaServiceV3.class);
        String metadataId = "74cac724-968c-4e6d-a6e1-6c2365e41d9d";
        int limit = 50;
        int page = 0;
        String orderBy = "createdAt";
        String orderType = "DESC";
        subscribe(service.getListAllEntity(metadataId, limit, page, orderBy, orderType), new ApiSubscriber<ResultListEntity>() {
            @Override
            public void onSuccess(ResultListEntity result) {
                LLog.d(TAG, "getListAllEntity onSuccess: " + LSApplication.getInstance().getGson().toJson(result));
                showTv(result);
            }

            @Override
            public void onFail(Throwable e) {
                LLog.e(TAG, "getListAllEntity onFail " + e.getMessage());
                showTv(e.getMessage());
            }
        });
    }

    private void retrieveAnEntity() {
        UizaServiceV3 service = RestClientV3.createService(UizaServiceV3.class);
        String id = "7789b7cc-9fd8-499b-bd35-745d133b6089";
        subscribe(service.retrieveAnEntity(id), new ApiSubscriber<ResultRetrieveAnEntity>() {
            @Override
            public void onSuccess(ResultRetrieveAnEntity result) {
                LLog.d(TAG, "retrieveAnEntity onSuccess: " + LSApplication.getInstance().getGson().toJson(result));
                showTv(result);
            }

            @Override
            public void onFail(Throwable e) {
                LLog.e(TAG, "retrieveAnEntity onFail " + e.getMessage());
                showTv(e.getMessage());
            }
        });
    }

    private void searchAnEntity() {
        UizaServiceV3 service = RestClientV3.createService(UizaServiceV3.class);
        String keyword = "a";
        subscribe(service.searchEntity(keyword), new ApiSubscriber<ResultListEntity>() {
            @Override
            public void onSuccess(ResultListEntity result) {
                LLog.d(TAG, "searchAnEntity onSuccess: " + LSApplication.getInstance().getGson().toJson(result));
                showTv(result);
            }

            @Override
            public void onFail(Throwable e) {
                LLog.e(TAG, "searchAnEntity onFail " + e.getMessage());
                showTv(e.getMessage());
            }
        });
    }

    private void getTokenStreaming() {
        UizaServiceV3 service = RestClientV3.createService(UizaServiceV3.class);
        SendGetTokenStreaming sendGetTokenStreaming = new SendGetTokenStreaming();
        sendGetTokenStreaming.setAppId(UizaV3Util.getAppId(activity));
        sendGetTokenStreaming.setEntityId("1ca56834-4c6f-4008-9c1f-2ca2a67c6814");
        sendGetTokenStreaming.setContentType(SendGetTokenStreaming.STREAM);
        subscribe(service.getTokenStreaming(sendGetTokenStreaming), new ApiSubscriber<ResultGetTokenStreaming>() {
            @Override
            public void onSuccess(ResultGetTokenStreaming result) {
                LLog.d(TAG, "getTokenStreaming onSuccess: " + LSApplication.getInstance().getGson().toJson(result));
                showTv(result);
                tokenStreaming = result.getData().getToken();
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
        RestClientV3GetLinkPlay.addAuthorization(tokenStreaming);
        UizaServiceV3 service = RestClientV3GetLinkPlay.createService(UizaServiceV3.class);
        String appId = UizaV3Util.getAppId(activity);
        String entityId = "1ca56834-4c6f-4008-9c1f-2ca2a67c6814";
        String typeContent = SendGetTokenStreaming.STREAM;
        subscribe(service.getLinkPlay(appId, entityId, typeContent), new ApiSubscriber<ResultGetLinkPlay>() {
            @Override
            public void onSuccess(ResultGetLinkPlay result) {
                LLog.d(TAG, "getLinkPlay onSuccess: " + LSApplication.getInstance().getGson().toJson(result));
                showTv(result);
            }

            @Override
            public void onFail(Throwable e) {
                LLog.e(TAG, "getLinkPlay onFail " + e.getMessage());
                showTv(e.getMessage());
            }
        });
    }

    private void retrieveALiveEvent() {
        UizaServiceV3 service = RestClientV3.createService(UizaServiceV3.class);
        int limit = 50;
        int page = 0;
        String orderBy = "createdAt";
        String orderType = "DESC";
        subscribe(service.retrieveALiveEvent(limit, page, orderBy, orderType), new ApiSubscriber<ResultRetrieveALiveEvent>() {
            @Override
            public void onSuccess(ResultRetrieveALiveEvent result) {
                LLog.d(TAG, "retrieveALiveEvent onSuccess: " + LSApplication.getInstance().getGson().toJson(result));
                showTv(result);
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
        UizaServiceV3 service = RestClientV3.createService(UizaServiceV3.class);
        SendGetTokenStreaming sendGetTokenStreaming = new SendGetTokenStreaming();
        sendGetTokenStreaming.setAppId(UizaV3Util.getAppId(activity));
        sendGetTokenStreaming.setEntityId("8e133d0d-5f67-45e8-8812-44b2ddfd9fe2");
        sendGetTokenStreaming.setContentType(SendGetTokenStreaming.LIVE);
        subscribe(service.getTokenStreaming(sendGetTokenStreaming), new ApiSubscriber<ResultGetTokenStreaming>() {
            @Override
            public void onSuccess(ResultGetTokenStreaming result) {
                LLog.d(TAG, "getTokenStreamingLive onSuccess: " + LSApplication.getInstance().getGson().toJson(result));
                showTv(result);
                tokenStreamingLive = result.getData().getToken();
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
            LToast.show(activity, "Token streaming not found, pls call getTokenStreamingLive before.");
            return;
        }
        RestClientV3GetLinkPlay.addAuthorization(tokenStreamingLive);
        UizaServiceV3 service = RestClientV3GetLinkPlay.createService(UizaServiceV3.class);
        String appId = UizaV3Util.getAppId(activity);
        String streamName = "ffdfdfdfd";
        subscribe(service.getLinkPlayLive(appId, streamName), new ApiSubscriber<Object>() {
            @Override
            public void onSuccess(Object result) {
                LLog.d(TAG, "getLinkPlay onSuccess: " + LSApplication.getInstance().getGson().toJson(result));
                showTv(result);
            }

            @Override
            public void onFail(Throwable e) {
                LLog.e(TAG, "getLinkPlay onFail " + e.getMessage());
                showTv(e.getMessage());
            }
        });
    }
}
