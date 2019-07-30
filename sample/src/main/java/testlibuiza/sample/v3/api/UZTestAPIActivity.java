package testlibuiza.sample.v3.api;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import testlibuiza.R;
import testlibuiza.app.LSApplication;
import uizacoresdk.util.UZData;
import vn.uiza.core.common.Constants;
import vn.uiza.core.utilities.LLog;
import vn.uiza.core.utilities.LUIUtil;
import vn.uiza.restapi.UZAPIMaster;
import vn.uiza.restapi.restclient.UZRestClient;
import vn.uiza.restapi.restclient.UZRestClientGetLinkPlay;
import vn.uiza.restapi.uiza.UZService;
import vn.uiza.restapi.uiza.model.v3.ad.AdWrapper;
import vn.uiza.restapi.uiza.model.v3.linkplay.getlinkplay.ResultGetLinkPlay;
import vn.uiza.restapi.uiza.model.v3.linkplay.gettokenstreaming.ResultGetTokenStreaming;
import vn.uiza.restapi.uiza.model.v3.linkplay.gettokenstreaming.SendGetTokenStreaming;
import vn.uiza.restapi.uiza.model.v3.livestreaming.gettimestartlive.ResultTimeStartLive;
import vn.uiza.restapi.uiza.model.v3.livestreaming.getviewalivefeed.ResultGetViewALiveFeed;
import vn.uiza.restapi.uiza.model.v3.livestreaming.retrievealiveevent.ResultRetrieveALiveEvent;
import vn.uiza.restapi.uiza.model.v3.metadata.createmetadata.CreateMetadata;
import vn.uiza.restapi.uiza.model.v3.metadata.createmetadata.ResultCreateMetadata;
import vn.uiza.restapi.uiza.model.v3.metadata.deleteanmetadata.ResultDeleteAnMetadata;
import vn.uiza.restapi.uiza.model.v3.metadata.getdetailofmetadata.ResultGetDetailOfMetadata;
import vn.uiza.restapi.uiza.model.v3.metadata.getlistmetadata.ResultGetListMetadata;
import vn.uiza.restapi.uiza.model.v3.metadata.updatemetadata.ResultUpdateMetadata;
import vn.uiza.restapi.uiza.model.v3.skin.listskin.ResultGetListSkin;
import vn.uiza.restapi.uiza.model.v3.usermanagement.createanuser.CreateUser;
import vn.uiza.restapi.uiza.model.v3.usermanagement.updatepassword.UpdatePassword;
import vn.uiza.restapi.uiza.model.v3.videoondeman.listallentity.ResultListEntity;
import vn.uiza.restapi.uiza.model.v3.videoondeman.retrieveanentity.ResultRetrieveAnEntity;
import vn.uiza.rxandroid.ApiSubscriber;
import vn.uiza.views.LToast;

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
        tv = (TextView) findViewById(R.id.tv);

        findViewById(R.id.bt_create_an_user).setOnClickListener(this);
        findViewById(R.id.bt_retrieve_an_user).setOnClickListener(this);
        findViewById(R.id.bt_list_all_user).setOnClickListener(this);
        findViewById(R.id.bt_update_an_user).setOnClickListener(this);
        findViewById(R.id.bt_delete_an_user).setOnClickListener(this);
        findViewById(R.id.bt_update_password).setOnClickListener(this);

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
            case R.id.bt_create_an_user:
                createAnUser();
                break;
            case R.id.bt_retrieve_an_user:
                retrieveAnUser();
                break;
            case R.id.bt_list_all_user:
                listAllUser();
                break;
            case R.id.bt_update_an_user:
                updateAnUser();
                break;
            case R.id.bt_delete_an_user:
                deleteAnUser();
                break;
            case R.id.bt_update_password:
                updatePassword();
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
        LUIUtil.printBeautyJson(o, tv);
    }

    private void createAnUser() {
        UZService service = UZRestClient.createService(UZService.class);
        CreateUser createUser = new CreateUser();
        createUser.setStatus(1);
        createUser.setUsername("username " + System.currentTimeMillis());
        createUser.setEmail("email " + System.currentTimeMillis());
        createUser.setPassword("123456789");
        createUser.setDob("11/11/1111");
        createUser.setFullname("fullname");
        createUser.setAvatar("path");
        UZAPIMaster.getInstance().subscribe(service.createAnUser(UZData.getInstance().getAPIVersion(), createUser), new ApiSubscriber<Object>() {
            @Override
            public void onSuccess(Object o) {
                LLog.d(TAG, "createAnUser onSuccess: " + LSApplication.getInstance().getGson().toJson(o));
                showTv(o);
            }

            @Override
            public void onFail(Throwable e) {
                LLog.e(TAG, "createAnUser onFail " + e.toString());
                showTv(e.getMessage());
            }
        });
    }

    private void retrieveAnUser() {
        UZService service = UZRestClient.createService(UZService.class);
        UZAPIMaster.getInstance().subscribe(service.retrieveAnUser(UZData.getInstance().getAPIVersion(), "9fd8984b-497f-4f7c-85af-e6abfcd5c83e"), new ApiSubscriber<Object>() {
            @Override
            public void onSuccess(Object o) {
                LLog.d(TAG, "createAnUser onSuccess: " + LSApplication.getInstance().getGson().toJson(o));
                showTv(o);
            }

            @Override
            public void onFail(Throwable e) {
                LLog.e(TAG, "createAnUser onFail " + e.toString());
                showTv(e.getMessage());
            }
        });
    }

    private void listAllUser() {
        UZService service = UZRestClient.createService(UZService.class);
        UZAPIMaster.getInstance().subscribe(service.listAllUser(UZData.getInstance().getAPIVersion()), new ApiSubscriber<Object>() {
            @Override
            public void onSuccess(Object o) {
                LLog.d(TAG, "createAnUser onSuccess: " + LSApplication.getInstance().getGson().toJson(o));
                showTv(o);
            }

            @Override
            public void onFail(Throwable e) {
                LLog.e(TAG, "createAnUser onFail " + e.toString());
                showTv(e.getMessage());
            }
        });
    }

    private void updateAnUser() {
        UZService service = UZRestClient.createService(UZService.class);
        CreateUser user = new CreateUser();
        user.setId("489260ed-c306-4e31-ad4b-ebde50d5bec4");
        user.setStatus(1);
        user.setUsername("username " + System.currentTimeMillis());
        user.setEmail("email " + System.currentTimeMillis());
        user.setPassword("123456789");
        user.setDob("11/11/1111");
        user.setFullname("fullname");
        user.setAvatar("path");
        UZAPIMaster.getInstance().subscribe(service.updateAnUser(UZData.getInstance().getAPIVersion(), user), new ApiSubscriber<Object>() {
            @Override
            public void onSuccess(Object o) {
                LLog.d(TAG, "createAnUser onSuccess: " + LSApplication.getInstance().getGson().toJson(o));
                showTv(o);
            }

            @Override
            public void onFail(Throwable e) {
                LLog.e(TAG, "createAnUser onFail " + e.toString());
                showTv(e.getMessage());
            }
        });
    }

    private void deleteAnUser() {
        UZService service = UZRestClient.createService(UZService.class);
        CreateUser user = new CreateUser();
        user.setId("9fd8984b-497f-4f7c-85af-e6abfcd5c83e");
        UZAPIMaster.getInstance().subscribe(service.deleteAnUser(user), new ApiSubscriber<Object>() {
            @Override
            public void onSuccess(Object o) {
                LLog.d(TAG, "createAnUser onSuccess: " + LSApplication.getInstance().getGson().toJson(o));
                showTv(o);
            }

            @Override
            public void onFail(Throwable e) {
                LLog.e(TAG, "createAnUser onFail " + e.toString());
                showTv(e.getMessage());
            }
        });
    }

    private void updatePassword() {
        UZService service = UZRestClient.createService(UZService.class);
        UpdatePassword updatePassword = new UpdatePassword();
        updatePassword.setId("9fd8984b-497f-4f7c-85af-e6abfcd5c83e");
        updatePassword.setOldPassword("oldpassword");
        updatePassword.setNewPassword("newpassword");
        UZAPIMaster.getInstance().subscribe(service.updatePassword(UZData.getInstance().getAPIVersion(), updatePassword), new ApiSubscriber<Object>() {
            @Override
            public void onSuccess(Object o) {
                LLog.d(TAG, "createAnUser onSuccess: " + LSApplication.getInstance().getGson().toJson(o));
                showTv(o);
            }

            @Override
            public void onFail(Throwable e) {
                LLog.e(TAG, "createAnUser onFail " + e.toString());
                showTv(e.getMessage());
            }
        });
    }

    private void getListMetadata() {
        UZService service = UZRestClient.createService(UZService.class);
        UZAPIMaster.getInstance().subscribe(service.getListMetadata(UZData.getInstance().getAPIVersion()), new ApiSubscriber<ResultGetListMetadata>() {
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
        UZService service = UZRestClient.createService(UZService.class);
        CreateMetadata createMetadata = new CreateMetadata();
        createMetadata.setName("Loitp " + System.currentTimeMillis());
        createMetadata.setType(CreateMetadata.TYPE_FOLDER);
        createMetadata.setDescription("This is a description sentences");
        createMetadata.setOrderNumber(1);
        createMetadata.setIcon("/exemple.com/icon.png");
        UZAPIMaster.getInstance().subscribe(service.createMetadata(UZData.getInstance().getAPIVersion(), createMetadata), new ApiSubscriber<ResultCreateMetadata>() {
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
        UZService service = UZRestClient.createService(UZService.class);
        String metadataId = "ce1a4735-99f4-4968-bf2a-3ba8063441f4";
        UZAPIMaster.getInstance().subscribe(service.getDetailOfMetadata(UZData.getInstance().getAPIVersion(), metadataId), new ApiSubscriber<ResultGetDetailOfMetadata>() {
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
        UZService service = UZRestClient.createService(UZService.class);
        CreateMetadata createMetadata = new CreateMetadata();
        createMetadata.setId("ce1a4735-99f4-4968-bf2a-3ba8063441f4");
        createMetadata.setName("@@@Loitp Suzuki GSX S1000");
        createMetadata.setType(CreateMetadata.TYPE_PLAYLIST);
        createMetadata.setDescription("Update description");
        createMetadata.setOrderNumber(69);
        createMetadata.setIcon("/exemple.com/icon_002.png");
        UZAPIMaster.getInstance().subscribe(service.updateMetadata(UZData.getInstance().getAPIVersion(), createMetadata), new ApiSubscriber<ResultUpdateMetadata>() {
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
        UZService service = UZRestClient.createService(UZService.class);
        String deleteMetadataId = "37b865b3-cf75-4faa-8507-180a9436d95d";
        UZAPIMaster.getInstance().subscribe(service.deleteAnMetadata(UZData.getInstance().getAPIVersion(), deleteMetadataId), new ApiSubscriber<ResultDeleteAnMetadata>() {
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
        UZService service = UZRestClient.createService(UZService.class);
        String metadataId = "";
        int limit = 50;
        int page = 0;
        String orderBy = "createdAt";
        String orderType = "DESC";
        UZAPIMaster.getInstance().subscribe(service.getListAllEntity(UZData.getInstance().getAPIVersion(), metadataId, limit, page, orderBy, orderType, "success", UZData.getInstance().getAppId()), new ApiSubscriber<ResultListEntity>() {
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
        UZService service = UZRestClient.createService(UZService.class);
        String metadataId = "74cac724-968c-4e6d-a6e1-6c2365e41d9d";
        int limit = 50;
        int page = 0;
        String orderBy = "createdAt";
        String orderType = "DESC";
        UZAPIMaster.getInstance().subscribe(service.getListAllEntity(UZData.getInstance().getAPIVersion(), metadataId, limit, page, orderBy, orderType, "success", UZData.getInstance().getAppId()), new ApiSubscriber<ResultListEntity>() {
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
        UZService service = UZRestClient.createService(UZService.class);
        String id = "7789b7cc-9fd8-499b-bd35-745d133b6089";
        UZAPIMaster.getInstance().subscribe(service.retrieveAnEntity(UZData.getInstance().getAPIVersion(), id, UZData.getInstance().getAppId()), new ApiSubscriber<ResultRetrieveAnEntity>() {
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
        UZService service = UZRestClient.createService(UZService.class);
        String keyword = "a";
        UZAPIMaster.getInstance().subscribe(service.searchEntity(UZData.getInstance().getAPIVersion(), keyword), new ApiSubscriber<ResultListEntity>() {
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
        UZService service = UZRestClient.createService(UZService.class);
        SendGetTokenStreaming sendGetTokenStreaming = new SendGetTokenStreaming();
        sendGetTokenStreaming.setAppId(UZData.getInstance().getAppId());
        sendGetTokenStreaming.setEntityId(entityIdDefaultVOD);
        sendGetTokenStreaming.setContentType(SendGetTokenStreaming.STREAM);
        UZAPIMaster.getInstance().subscribe(service.getTokenStreaming(UZData.getInstance().getAPIVersion(), sendGetTokenStreaming), new ApiSubscriber<ResultGetTokenStreaming>() {
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
        UZRestClientGetLinkPlay.addAuthorization(tokenStreaming);
        UZService service = UZRestClientGetLinkPlay.createService(UZService.class);
        String appId = UZData.getInstance().getAppId();
        String typeContent = SendGetTokenStreaming.STREAM;
        UZAPIMaster.getInstance().subscribe(service.getLinkPlay(appId, entityIdDefaultVOD, typeContent), new ApiSubscriber<ResultGetLinkPlay>() {
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
        UZService service = UZRestClient.createService(UZService.class);
        int limit = 50;
        int page = 0;
        String orderBy = "createdAt";
        String orderType = "DESC";
        UZAPIMaster.getInstance().subscribe(service.retrieveALiveEvent(UZData.getInstance().getAPIVersion(), limit, page, orderBy, orderType, UZData.getInstance().getAppId()), new ApiSubscriber<ResultRetrieveALiveEvent>() {
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
        UZService service = UZRestClient.createService(UZService.class);
        SendGetTokenStreaming sendGetTokenStreaming = new SendGetTokenStreaming();
        sendGetTokenStreaming.setAppId(UZData.getInstance().getAppId());
        sendGetTokenStreaming.setEntityId(entityIdDefaultLIVE);
        sendGetTokenStreaming.setContentType(SendGetTokenStreaming.LIVE);
        UZAPIMaster.getInstance().subscribe(service.getTokenStreaming(UZData.getInstance().getAPIVersion(), sendGetTokenStreaming), new ApiSubscriber<ResultGetTokenStreaming>() {
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
        UZRestClientGetLinkPlay.addAuthorization(tokenStreamingLive);
        UZService service = UZRestClientGetLinkPlay.createService(UZService.class);
        String appId = UZData.getInstance().getAppId();
        String streamName = "ffdfdfdfd";
        UZAPIMaster.getInstance().subscribe(service.getLinkPlayLive(appId, streamName), new ApiSubscriber<ResultGetLinkPlay>() {
            @Override
            public void onSuccess(ResultGetLinkPlay result) {
                LLog.d(TAG, "getLinkPlayLive onSuccess: " + LSApplication.getInstance().getGson().toJson(result));
                showTv(result);
            }

            @Override
            public void onFail(Throwable e) {
                LLog.e(TAG, "getLinkPlayLive onFail " + e.getMessage());
                showTv(e.getMessage());
            }
        });
    }

    private void getViewALiveFeed() {
        UZService service = UZRestClient.createService(UZService.class);
        String id = "8e133d0d-5f67-45e8-8812-44b2ddfd9fe2";
        UZAPIMaster.getInstance().subscribe(service.getViewALiveFeed(UZData.getInstance().getAPIVersion(), id, UZData.getInstance().getAppId()), new ApiSubscriber<ResultGetViewALiveFeed>() {
            @Override
            public void onSuccess(ResultGetViewALiveFeed result) {
                LLog.d(TAG, "getViewALiveFeed onSuccess: " + LSApplication.getInstance().getGson().toJson(result));
                showTv(result);
            }

            @Override
            public void onFail(Throwable e) {
                LLog.e(TAG, "getViewALiveFeed onFail " + e.getMessage());
                showTv(e.getMessage());
            }
        });
    }

    private void getTimeStartLive() {
        UZService service = UZRestClient.createService(UZService.class);
        String entityId = "8e133d0d-5f67-45e8-8812-44b2ddfd9fe2";
        String feedId = "46fc46f4-8bc0-4d7f-a380-9515d8259af3";
        UZAPIMaster.getInstance().subscribe(service.getTimeStartLive(UZData.getInstance().getAPIVersion(), entityId, feedId, UZData.getInstance().getAppId()), new ApiSubscriber<ResultTimeStartLive>() {
            @Override
            public void onSuccess(ResultTimeStartLive result) {
                LLog.d(TAG, "getTimeStartLive onSuccess: " + LSApplication.getInstance().getGson().toJson(result));
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
        UZService service = UZRestClient.createService(UZService.class);
        UZAPIMaster.getInstance().subscribe(service.getListSkin(UZData.getInstance().getAPIVersion(), Constants.PLATFORM_ANDROID), new ApiSubscriber<ResultGetListSkin>() {
            @Override
            public void onSuccess(ResultGetListSkin result) {
                LLog.d(TAG, "getListSkin onSuccess: " + LSApplication.getInstance().getGson().toJson(result));
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
        UZService service = UZRestClient.createService(UZService.class);
        UZAPIMaster.getInstance().subscribe(service.getSkinConfig(UZData.getInstance().getAPIVersion(), "645cd2a2-9216-4f5d-a73b-37d3e3034798"), new ApiSubscriber<Object>() {
            @Override
            public void onSuccess(Object result) {
                LLog.d(TAG, "getSkinConfig onSuccess: " + LSApplication.getInstance().getGson().toJson(result));
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
        UZService service = UZRestClient.createService(UZService.class);
        UZAPIMaster.getInstance().subscribe(service.getCuePoint(UZData.getInstance().getAPIVersion(), "0e8254fa-afa1-491f-849b-5aa8bc7cce52", UZData.getInstance().getAppId()), new ApiSubscriber<AdWrapper>() {
            @Override
            public void onSuccess(AdWrapper result) {
                LLog.d(TAG, "getIMAAd onSuccess: " + LSApplication.getInstance().getGson().toJson(result));
                showTv(result);
            }

            @Override
            public void onFail(Throwable e) {
                LLog.e(TAG, "getIMAAd onFail " + e.getMessage());
                showTv(e.getMessage());
            }
        });
    }
}
