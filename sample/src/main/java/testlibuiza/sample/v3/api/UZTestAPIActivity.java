package testlibuiza.sample.v3.api;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import testlibuiza.R;
import timber.log.Timber;
import uizacoresdk.util.UZData;
import vn.uiza.core.common.Constants;
import vn.uiza.core.utilities.LUIUtil;
import vn.uiza.restapi.RxBinder;
import vn.uiza.restapi.restclient.UZRestClient;
import vn.uiza.restapi.restclient.UZRestClientGetLinkPlay;
import vn.uiza.restapi.uiza.UZService;
import vn.uiza.restapi.uiza.model.v3.linkplay.gettokenstreaming.SendGetTokenStreaming;
import vn.uiza.restapi.uiza.model.v3.metadata.createmetadata.CreateMetadata;
import vn.uiza.restapi.uiza.model.v3.usermanagement.createanuser.CreateUser;
import vn.uiza.restapi.uiza.model.v3.usermanagement.updatepassword.UpdatePassword;
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
        RxBinder.getInstance().bind(service.createAnUser(UZData.getInstance().getAPIVersion(), createUser),
                this::showTv, throwable -> {
                    Timber.e(throwable, "createAnUser onFail");
                    showTv(throwable.getMessage());
                });
    }

    private void retrieveAnUser() {
        UZService service = UZRestClient.createService(UZService.class);
        RxBinder.getInstance().bind(service.retrieveAnUser(UZData.getInstance().getAPIVersion(), "9fd8984b-497f-4f7c-85af-e6abfcd5c83e"),
                this::showTv, throwable -> {
                    Timber.e(throwable, "retrieveAnUser onFail");
                    showTv(throwable.getMessage());
                });
    }

    private void listAllUser() {
        UZService service = UZRestClient.createService(UZService.class);
        RxBinder.getInstance().bind(service.listAllUser(UZData.getInstance().getAPIVersion()),
                this::showTv, throwable -> {
                    Timber.e(throwable, "retrieveAnUser onFail");
                    showTv(throwable.getMessage());
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
        RxBinder.getInstance().bind(service.updateAnUser(UZData.getInstance().getAPIVersion(), user),
                this::showTv, throwable -> {
                    Timber.e(throwable, "updateAnUser onFail");
                    showTv(throwable.getMessage());
                });
    }

    private void deleteAnUser() {
        UZService service = UZRestClient.createService(UZService.class);
        CreateUser user = new CreateUser();
        user.setId("9fd8984b-497f-4f7c-85af-e6abfcd5c83e");
        RxBinder.getInstance().bind(service.deleteAnUser(user),
                this::showTv, throwable -> {
                    Timber.e(TAG, "deleteAnUser onFail");
                    showTv(throwable.getMessage());
                });
    }

    private void updatePassword() {
        UZService service = UZRestClient.createService(UZService.class);
        UpdatePassword updatePassword = new UpdatePassword();
        updatePassword.setId("9fd8984b-497f-4f7c-85af-e6abfcd5c83e");
        updatePassword.setOldPassword("oldpassword");
        updatePassword.setNewPassword("newpassword");
        RxBinder.getInstance().bind(service.updatePassword(UZData.getInstance().getAPIVersion(), updatePassword),
                this::showTv, throwable -> {
                    Timber.e(throwable, "updatePassword onFail");
                    showTv(throwable.getMessage());
                });
    }

    private void getListMetadata() {
        UZService service = UZRestClient.createService(UZService.class);
        RxBinder.getInstance().bind(service.getListMetadata(UZData.getInstance().getAPIVersion()),
                this::showTv, throwable -> {
                    Timber.e(throwable, "getListMetadata onFail");
                    showTv(throwable.getMessage());
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
        RxBinder.getInstance().bind(service.createMetadata(UZData.getInstance().getAPIVersion(), createMetadata),
                this::showTv, throwable -> {
                    Timber.e(throwable, "createMetadata onFail");
                    showTv(throwable.getMessage());
                });
    }

    private void getDetailOfMetadata() {
        UZService service = UZRestClient.createService(UZService.class);
        String metadataId = "ce1a4735-99f4-4968-bf2a-3ba8063441f4";
        RxBinder.getInstance().bind(service.getDetailOfMetadata(UZData.getInstance().getAPIVersion(), metadataId),
                this::showTv, throwable -> {
                    Timber.e(throwable, "getDetailOfMetadata onFail ");
                    showTv(throwable.getMessage());
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
        RxBinder.getInstance().bind(service.updateMetadata(UZData.getInstance().getAPIVersion(), createMetadata),
                this::showTv, throwable -> {
                    Timber.e(throwable, "updateMetadata onFail");
                    showTv(throwable.getMessage());
                });
    }

    private void deleteAnMetadata() {
        UZService service = UZRestClient.createService(UZService.class);
        String deleteMetadataId = "37b865b3-cf75-4faa-8507-180a9436d95d";
        RxBinder.getInstance().bind(service.deleteAnMetadata(UZData.getInstance().getAPIVersion(), deleteMetadataId),
                this::showTv, throwable -> {
                    Timber.e(throwable, "deleteAnMetadata onFail");
                    showTv(throwable.getMessage());
                });
    }


    private void listAllEntity() {
        UZService service = UZRestClient.createService(UZService.class);
        String metadataId = "";
        int limit = 50;
        int page = 0;
        String orderBy = "createdAt";
        String orderType = "DESC";
        RxBinder.getInstance().bind(service.getListAllEntity(UZData.getInstance().getAPIVersion(), metadataId, limit, page, orderBy, orderType, "success", UZData.getInstance().getAppId()),
                this::showTv, throwable -> {
                    Timber.e(throwable, "getListAllEntity onFail");
                    showTv(throwable.getMessage());
                });
    }

    private void listAllEntityMetadata() {
        UZService service = UZRestClient.createService(UZService.class);
        String metadataId = "74cac724-968c-4e6d-a6e1-6c2365e41d9d";
        int limit = 50;
        int page = 0;
        String orderBy = "createdAt";
        String orderType = "DESC";
        RxBinder.getInstance().bind(service.getListAllEntity(UZData.getInstance().getAPIVersion(), metadataId, limit, page, orderBy, orderType, "success", UZData.getInstance().getAppId()),
                this::showTv, throwable -> {
                    Timber.e(throwable, "getListAllEntity onFail");
                    showTv(throwable.getMessage());
                });
    }

    private void retrieveAnEntity() {
        UZService service = UZRestClient.createService(UZService.class);
        String id = "7789b7cc-9fd8-499b-bd35-745d133b6089";
        RxBinder.getInstance().bind(service.retrieveAnEntity(UZData.getInstance().getAPIVersion(), id, UZData.getInstance().getAppId()),
                this::showTv, throwable -> {
                    Timber.e(throwable, "retrieveAnEntity onFail");
                    showTv(throwable.getMessage());
                });
    }

    private void searchAnEntity() {
        UZService service = UZRestClient.createService(UZService.class);
        String keyword = "a";
        RxBinder.getInstance().bind(service.searchEntity(UZData.getInstance().getAPIVersion(), keyword),
                this::showTv, throwable -> {
                    Timber.e(throwable, "searchEntity onFail");
                    showTv(throwable.getMessage());
                });
    }

    private void getTokenStreaming() {
        UZService service = UZRestClient.createService(UZService.class);
        SendGetTokenStreaming sendGetTokenStreaming = new SendGetTokenStreaming();
        sendGetTokenStreaming.setAppId(UZData.getInstance().getAppId());
        sendGetTokenStreaming.setEntityId(entityIdDefaultVOD);
        sendGetTokenStreaming.setContentType(SendGetTokenStreaming.STREAM);
        RxBinder.getInstance().bind(service.getTokenStreaming(UZData.getInstance().getAPIVersion(), sendGetTokenStreaming),
                this::showTv, throwable -> {
                    Timber.e(throwable, "getTokenStreaming onFail ");
                    showTv(throwable.getMessage());
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
        RxBinder.getInstance().bind(service.getLinkPlay(appId, entityIdDefaultVOD, typeContent),
                this::showTv, throwable -> {
                    Timber.e(throwable, "getLinkPlay onFail");
                    showTv(throwable.getMessage());
                });
    }

    private void retrieveALiveEvent() {
        UZService service = UZRestClient.createService(UZService.class);
        int limit = 50;
        int page = 0;
        String orderBy = "createdAt";
        String orderType = "DESC";
        RxBinder.getInstance().bind(service.retrieveALiveEvent(UZData.getInstance().getAPIVersion(), limit, page, orderBy, orderType, UZData.getInstance().getAppId()),
                this::showTv, throwable -> {
                    Timber.e(throwable, "retrieveALiveEvent onFail");
                    showTv(throwable.getMessage());
                });
    }

    private String tokenStreamingLive;//value received from api getTokenStreamingLive

    private void getTokenStreamingLive() {
        UZService service = UZRestClient.createService(UZService.class);
        SendGetTokenStreaming sendGetTokenStreaming = new SendGetTokenStreaming();
        sendGetTokenStreaming.setAppId(UZData.getInstance().getAppId());
        sendGetTokenStreaming.setEntityId(entityIdDefaultLIVE);
        sendGetTokenStreaming.setContentType(SendGetTokenStreaming.LIVE);
        RxBinder.getInstance().bind(service.getTokenStreaming(UZData.getInstance().getAPIVersion(), sendGetTokenStreaming),
                this::showTv, throwable -> {
                    Timber.e(throwable, "getTokenStreaming onFail");
                    showTv(throwable.getMessage());
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
        RxBinder.getInstance().bind(service.getLinkPlayLive(appId, streamName),
                this::showTv, throwable -> {
                    Timber.e(throwable, "getLinkPlayLive onFail");
                    showTv(throwable.getMessage());
                });
    }

    private void getViewALiveFeed() {
        UZService service = UZRestClient.createService(UZService.class);
        String id = "8e133d0d-5f67-45e8-8812-44b2ddfd9fe2";
        RxBinder.getInstance().bind(service.getViewALiveFeed(UZData.getInstance().getAPIVersion(), id, UZData.getInstance().getAppId()),
                this::showTv, throwable -> {
                    Timber.e(throwable, "getViewALiveFeed onFail");
                    showTv(throwable.getMessage());
                });
    }

    private void getTimeStartLive() {
        UZService service = UZRestClient.createService(UZService.class);
        String entityId = "8e133d0d-5f67-45e8-8812-44b2ddfd9fe2";
        String feedId = "46fc46f4-8bc0-4d7f-a380-9515d8259af3";
        RxBinder.getInstance().bind(service.getTimeStartLive(UZData.getInstance().getAPIVersion(), entityId, feedId, UZData.getInstance().getAppId()),
                this::showTv, throwable -> {
                    Timber.e(throwable, "getTimeStartLive onFail");
                    showTv(throwable.getMessage());
                });
    }

    private void getListSkin() {
        UZService service = UZRestClient.createService(UZService.class);
        RxBinder.getInstance().bind(service.getListSkin(UZData.getInstance().getAPIVersion(), Constants.PLATFORM_ANDROID),
                this::showTv, throwable -> {
                    Timber.e(throwable, "getListSkin onFail");
                    showTv(throwable.getMessage());
                });
    }

    private void getSkinConfig() {
        UZService service = UZRestClient.createService(UZService.class);
        RxBinder.getInstance().bind(service.getSkinConfig(UZData.getInstance().getAPIVersion(), "645cd2a2-9216-4f5d-a73b-37d3e3034798"),
                this::showTv, throwable -> {
                    Timber.e(throwable, "getSkinConfig onFail");
                    showTv(throwable.getMessage());
                });
    }

    private void getIMAAd() {
        UZService service = UZRestClient.createService(UZService.class);
        RxBinder.getInstance().bind(service.getCuePoint(UZData.getInstance().getAPIVersion(), "0e8254fa-afa1-491f-849b-5aa8bc7cce52", UZData.getInstance().getAppId()),
                this::showTv, throwable -> {
                    Timber.e(throwable, "getCuePoint onFail");
                    showTv(throwable.getMessage());
                });
    }
}
