package testlibuiza.sample.api;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import testlibuiza.R;
import testlibuiza.app.LSApplication;
import vn.loitp.core.base.BaseActivity;
import vn.loitp.core.common.Constants;
import vn.loitp.core.utilities.LLog;
import vn.loitp.core.utilities.LUIUtil;
import vn.loitp.restapi.restclient.RestClientTracking;
import vn.loitp.restapi.restclient.RestClientV2;
import vn.loitp.restapi.uiza.UizaService;
import vn.loitp.restapi.uiza.model.tracking.UizaTracking;
import vn.loitp.restapi.uiza.model.v2.auth.Auth;
import vn.loitp.restapi.uiza.model.v2.auth.JsonBodyAuth;
import vn.loitp.restapi.uiza.model.v2.getdetailentity.GetDetailEntity;
import vn.loitp.restapi.uiza.model.v2.getdetailentity.JsonBodyGetDetailEntity;
import vn.loitp.restapi.uiza.model.v2.getlinkdownload.Mpd;
import vn.loitp.restapi.uiza.model.v2.getlinkplay.GetLinkPlay;
import vn.loitp.restapi.uiza.model.v2.listallentity.JsonBodyListAllEntity;
import vn.loitp.restapi.uiza.model.v2.listallentity.ListAllEntity;
import vn.loitp.restapi.uiza.model.v2.listallentityrelation.JsonBodyListAllEntityRelation;
import vn.loitp.restapi.uiza.model.v2.listallentityrelation.ListAllEntityRelation;
import vn.loitp.restapi.uiza.model.v2.listallmetadata.JsonBodyMetadataList;
import vn.loitp.restapi.uiza.model.v2.listallmetadata.ListAllMetadata;
import vn.loitp.restapi.uiza.model.v2.search.JsonBodySearch;
import vn.loitp.restapi.uiza.model.v2.search.Search;
import vn.loitp.rxandroid.ApiSubscriber;

public class TestAPIActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tv = (TextView) findViewById(R.id.tv);
        RestClientV2.init(Constants.URL_DEV_UIZA_VERSION_2);
        findViewById(R.id.bt_get_token).setOnClickListener(this);
        findViewById(R.id.bt_check_token).setOnClickListener(this);
        findViewById(R.id.bt_list_metadata).setOnClickListener(this);
        findViewById(R.id.bt_search).setOnClickListener(this);
        findViewById(R.id.bt_list_entity).setOnClickListener(this);
        findViewById(R.id.bt_get_detail_entity).setOnClickListener(this);
        findViewById(R.id.bt_entity_ralation).setOnClickListener(this);
        findViewById(R.id.bt_get_link_play).setOnClickListener(this);

        //for track
        findViewById(R.id.bt_track_1_dev).setOnClickListener(this);
        findViewById(R.id.bt_track_2_dev).setOnClickListener(this);
        findViewById(R.id.bt_track_3_dev).setOnClickListener(this);
        findViewById(R.id.bt_track_4_dev).setOnClickListener(this);
        findViewById(R.id.bt_track_5_dev).setOnClickListener(this);
        findViewById(R.id.bt_track_6_dev).setOnClickListener(this);
        findViewById(R.id.bt_track_7_dev).setOnClickListener(this);
        findViewById(R.id.bt_track_8_dev).setOnClickListener(this);
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
        return R.layout.activity_test_api;
    }

    @Override
    public void onClick(View v) {
        String json;
        switch (v.getId()) {
            case R.id.bt_get_token:
                auth();
                break;
            case R.id.bt_check_token:
                checkToken();
                break;
            case R.id.bt_list_metadata:
                listMetadata();
                break;
            case R.id.bt_search:
                search();
                break;
            case R.id.bt_list_entity:
                listEntity();
                break;
            case R.id.bt_get_detail_entity:
                getDetailEntity();
                break;
            case R.id.bt_entity_ralation:
                getListAllEntityRelation();
                break;
            case R.id.bt_get_link_play:
                getLinkPlay();
                break;
            case R.id.bt_track_1_dev:
                json = "{\"app_id\":\"a204e9cdeca44948a33e0d012ef74e90\",\"page_type\":\"iframe\",\"viewer_user_id\":\"\",\"user_agent\":\"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.84 Safari/537.36\",\"referrer\":\"\",\"device_id\":\"5cb442458ae4cca6ceda2f541c718cd8\",\"player_id\":\"658347e9-e516-4f7d-b4f2-02b23e640724\",\"player_name\":\"DuyQT's Player\",\"player_version\":\"1.0.4\",\"entity_id\":\"513c49db-7b91-4485-949e-80bd0c57d189\",\"entity_name\":\"20170406_031552000_iOS\",\"entity_series\":\"\",\"entity_producer\":\"\",\"entity_content_type\":\"video\",\"entity_language_code\":\"\",\"entity_variant_name\":\"\",\"entity_variant_id\":\"\",\"entity_duration\":\"0\",\"entity_stream_type\":\"on-demand\",\"entity_encoding_variant\":\"\",\"entity_cdn\":\"\",\"play_through\":\"0\",\"event_type\":\"display\",\"timestamp\":\"2018-01-12T05:57:55.735Z\"}";
                init(create(json));
                break;
            case R.id.bt_track_2_dev:
                json = "{\"app_id\":\"a204e9cdeca44948a33e0d012ef74e90\",\"page_type\":\"iframe\",\"viewer_user_id\":\"\",\"user_agent\":\"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.84 Safari/537.36\",\"referrer\":\"\",\"device_id\":\"5cb442458ae4cca6ceda2f541c718cd8\",\"player_id\":\"658347e9-e516-4f7d-b4f2-02b23e640724\",\"player_name\":\"DuyQT's Player\",\"player_version\":\"1.0.4\",\"entity_id\":\"513c49db-7b91-4485-949e-80bd0c57d189\",\"entity_name\":\"20170406_031552000_iOS\",\"entity_series\":\"\",\"entity_producer\":\"\",\"entity_content_type\":\"video\",\"entity_language_code\":\"\",\"entity_variant_name\":\"\",\"entity_variant_id\":\"\",\"entity_duration\":\"35\",\"entity_stream_type\":\"on-demand\",\"entity_encoding_variant\":\"\",\"entity_cdn\":\"\",\"play_through\":\"0\",\"event_type\":\"plays_requested\",\"timestamp\":\"2018-01-12T05:57:56.546Z\"}";
                init(create(json));
                break;
            case R.id.bt_track_3_dev:
                json = "{\"app_id\":\"a204e9cdeca44948a33e0d012ef74e90\",\"page_type\":\"iframe\",\"viewer_user_id\":\"\",\"user_agent\":\"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.84 Safari/537.36\",\"referrer\":\"\",\"device_id\":\"5cb442458ae4cca6ceda2f541c718cd8\",\"player_id\":\"658347e9-e516-4f7d-b4f2-02b23e640724\",\"player_name\":\"DuyQT's Player\",\"player_version\":\"1.0.4\",\"entity_id\":\"513c49db-7b91-4485-949e-80bd0c57d189\",\"entity_name\":\"20170406_031552000_iOS\",\"entity_series\":\"\",\"entity_producer\":\"\",\"entity_content_type\":\"video\",\"entity_language_code\":\"\",\"entity_variant_name\":\"\",\"entity_variant_id\":\"\",\"entity_duration\":\"35\",\"entity_stream_type\":\"on-demand\",\"entity_encoding_variant\":\"\",\"entity_cdn\":\"\",\"play_through\":\"0\",\"event_type\":\"video_starts\",\"timestamp\":\"2018-01-12T05:58:14.563Z\"}";
                init(create(json));
                break;
            case R.id.bt_track_4_dev:
                json = "{\"app_id\":\"a204e9cdeca44948a33e0d012ef74e90\",\"page_type\":\"iframe\",\"viewer_user_id\":\"\",\"user_agent\":\"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.84 Safari/537.36\",\"referrer\":\"\",\"device_id\":\"5cb442458ae4cca6ceda2f541c718cd8\",\"player_id\":\"658347e9-e516-4f7d-b4f2-02b23e640724\",\"player_name\":\"DuyQT's Player\",\"player_version\":\"1.0.4\",\"entity_id\":\"513c49db-7b91-4485-949e-80bd0c57d189\",\"entity_name\":\"20170406_031552000_iOS\",\"entity_series\":\"\",\"entity_producer\":\"\",\"entity_content_type\":\"video\",\"entity_language_code\":\"\",\"entity_variant_name\":\"\",\"entity_variant_id\":\"\",\"entity_duration\":\"35\",\"entity_stream_type\":\"on-demand\",\"entity_encoding_variant\":\"\",\"entity_cdn\":\"\",\"play_through\":\"25\",\"event_type\":\"play_through\",\"timestamp\":\"2018-01-12T05:58:17.817Z\"}";
                init(create(json));
                break;
            case R.id.bt_track_5_dev:
                json = "{\"app_id\":\"a204e9cdeca44948a33e0d012ef74e90\",\"page_type\":\"iframe\",\"viewer_user_id\":\"\",\"user_agent\":\"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.84 Safari/537.36\",\"referrer\":\"\",\"device_id\":\"5cb442458ae4cca6ceda2f541c718cd8\",\"player_id\":\"658347e9-e516-4f7d-b4f2-02b23e640724\",\"player_name\":\"DuyQT's Player\",\"player_version\":\"1.0.4\",\"entity_id\":\"513c49db-7b91-4485-949e-80bd0c57d189\",\"entity_name\":\"20170406_031552000_iOS\",\"entity_series\":\"\",\"entity_producer\":\"\",\"entity_content_type\":\"video\",\"entity_language_code\":\"\",\"entity_variant_name\":\"\",\"entity_variant_id\":\"\",\"entity_duration\":\"35\",\"entity_stream_type\":\"on-demand\",\"entity_encoding_variant\":\"\",\"entity_cdn\":\"\",\"play_through\":\"50\",\"event_type\":\"play_through\",\"timestamp\":\"2018-01-12T05:58:17.816Z\"}";
                init(create(json));
                break;
            case R.id.bt_track_6_dev:
                json = "{\"app_id\":\"a204e9cdeca44948a33e0d012ef74e90\",\"page_type\":\"iframe\",\"viewer_user_id\":\"\",\"user_agent\":\"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.84 Safari/537.36\",\"referrer\":\"\",\"device_id\":\"5cb442458ae4cca6ceda2f541c718cd8\",\"player_id\":\"658347e9-e516-4f7d-b4f2-02b23e640724\",\"player_name\":\"DuyQT's Player\",\"player_version\":\"1.0.4\",\"entity_id\":\"513c49db-7b91-4485-949e-80bd0c57d189\",\"entity_name\":\"20170406_031552000_iOS\",\"entity_series\":\"\",\"entity_producer\":\"\",\"entity_content_type\":\"video\",\"entity_language_code\":\"\",\"entity_variant_name\":\"\",\"entity_variant_id\":\"\",\"entity_duration\":\"35\",\"entity_stream_type\":\"on-demand\",\"entity_encoding_variant\":\"\",\"entity_cdn\":\"\",\"play_through\":\"75\",\"event_type\":\"play_through\",\"timestamp\":\"2018-01-12T05:58:17.815Z\"}";
                init(create(json));
                break;
            case R.id.bt_track_7_dev:
                json = "{\"app_id\":\"a204e9cdeca44948a33e0d012ef74e90\",\"page_type\":\"iframe\",\"viewer_user_id\":\"\",\"user_agent\":\"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.84 Safari/537.36\",\"referrer\":\"\",\"device_id\":\"5cb442458ae4cca6ceda2f541c718cd8\",\"player_id\":\"658347e9-e516-4f7d-b4f2-02b23e640724\",\"player_name\":\"DuyQT's Player\",\"player_version\":\"1.0.4\",\"entity_id\":\"513c49db-7b91-4485-949e-80bd0c57d189\",\"entity_name\":\"20170406_031552000_iOS\",\"entity_series\":\"\",\"entity_producer\":\"\",\"entity_content_type\":\"video\",\"entity_language_code\":\"\",\"entity_variant_name\":\"\",\"entity_variant_id\":\"\",\"entity_duration\":\"35\",\"entity_stream_type\":\"on-demand\",\"entity_encoding_variant\":\"\",\"entity_cdn\":\"\",\"play_through\":\"100\",\"event_type\":\"play_through\",\"timestamp\":\"2018-01-12T05:58:17.815Z\"}";
                init(create(json));
                break;
            case R.id.bt_track_8_dev:
                json = "{\"app_id\":\"a204e9cdeca44948a33e0d012ef74e90\",\"page_type\":\"iframe\",\"viewer_user_id\":\"\",\"user_agent\":\"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.84 Safari/537.36\",\"referrer\":\"\",\"device_id\":\"5cb442458ae4cca6ceda2f541c718cd8\",\"player_id\":\"658347e9-e516-4f7d-b4f2-02b23e640724\",\"player_name\":\"DuyQT's Player\",\"player_version\":\"1.0.4\",\"entity_id\":\"513c49db-7b91-4485-949e-80bd0c57d189\",\"entity_name\":\"20170406_031552000_iOS\",\"entity_series\":\"\",\"entity_producer\":\"\",\"entity_content_type\":\"video\",\"entity_language_code\":\"\",\"entity_variant_name\":\"\",\"entity_variant_id\":\"\",\"entity_duration\":\"35\",\"entity_stream_type\":\"on-demand\",\"entity_encoding_variant\":\"\",\"entity_cdn\":\"\",\"play_through\":\"0\",\"event_type\":\"replay\",\"timestamp\":\"2018-01-12T05:58:17.834Z\"}";
                init(create(json));
                break;
        }
    }

    private void showTv(Object o) {
        LUIUtil.printBeautyJson(o, tv);
    }

    private String appId;

    private void auth() {
        UizaService service = RestClientV2.createService(UizaService.class);
        String accessKeyId = "Y0ZW0XM7HZL2CB8ODNDV";
        String secretKeyId = "qtQWc9Ut1SAfWK2viFJHBgViYCZYthSTjEJMlR9S";

        JsonBodyAuth jsonBodyAuth = new JsonBodyAuth();
        jsonBodyAuth.setAccessKeyId(accessKeyId);
        jsonBodyAuth.setSecretKeyId(secretKeyId);

        subscribe(service.auth(jsonBodyAuth), new ApiSubscriber<Auth>() {
            @Override
            public void onSuccess(Auth auth) {
                showTv(auth);
                RestClientV2.addAuthorization(auth.getData().getToken());
                appId = auth.getData().getAppId();
            }

            @Override
            public void onFail(Throwable e) {
                LLog.e(TAG, "auth onFail " + e.getMessage());
                handleException(e);
            }
        });
    }

    private void checkToken() {
        UizaService service = RestClientV2.createService(UizaService.class);
        subscribe(service.checkToken(), new ApiSubscriber<Auth>() {
            @Override
            public void onSuccess(Auth auth) {
                showTv(auth);
            }

            @Override
            public void onFail(Throwable e) {
                LLog.e(TAG, "auth onFail " + e.getMessage());
                handleException(e);
            }
        });
    }

    private void listMetadata() {
        UizaService service = RestClientV2.createService(UizaService.class);
        int limit = 999;
        String orderBy = "name";
        String orderType = "ASC";
        JsonBodyMetadataList jsonBodyMetadataList = new JsonBodyMetadataList();
        jsonBodyMetadataList.setLimit(limit);
        jsonBodyMetadataList.setOrderBy(orderBy);
        jsonBodyMetadataList.setOrderType(orderType);
        subscribe(service.listAllMetadataV2(jsonBodyMetadataList), new ApiSubscriber<ListAllMetadata>() {
            @Override
            public void onSuccess(ListAllMetadata listAllMetadata) {
                showTv(listAllMetadata);
            }

            @Override
            public void onFail(Throwable e) {
                LLog.e(TAG, "auth onFail " + e.getMessage());
                handleException(e);
            }
        });
    }

    private void search() {
        UizaService service = RestClientV2.createService(UizaService.class);
        JsonBodySearch jsonBodySearch = new JsonBodySearch();
        jsonBodySearch.setKeyword("lol");
        jsonBodySearch.setLimit(50);
        jsonBodySearch.setPage(0);

        subscribe(service.searchEntityV2(jsonBodySearch), new ApiSubscriber<Search>() {
            @Override
            public void onSuccess(Search search) {
                showTv(search);
            }

            @Override
            public void onFail(Throwable e) {
                handleException(e);
            }
        });
    }

    private void listEntity() {
        UizaService service = RestClientV2.createService(UizaService.class);
        JsonBodyListAllEntity jsonBodyListAllEntity = new JsonBodyListAllEntity();
        //jsonBodyListAllEntity.setMetadataId(metadataId);
        jsonBodyListAllEntity.setLimit(50);
        jsonBodyListAllEntity.setPage(0);
        jsonBodyListAllEntity.setOrderBy("createdAt");
        jsonBodyListAllEntity.setOrderType("DESC");
        subscribe(service.listAllEntityV2(jsonBodyListAllEntity), new ApiSubscriber<ListAllEntity>() {
            @Override
            public void onSuccess(ListAllEntity listAllEntity) {
                showTv(listAllEntity);
            }

            @Override
            public void onFail(Throwable e) {
                handleException(e);
            }
        });
    }

    private void getDetailEntity() {
        UizaService service = RestClientV2.createService(UizaService.class);
        JsonBodyGetDetailEntity jsonBodyGetDetailEntity = new JsonBodyGetDetailEntity();
        jsonBodyGetDetailEntity.setId("5bd11904-07b8-4859-bdc8-9fee0b2199b2");
        subscribe(service.getDetailEntityV2(jsonBodyGetDetailEntity), new ApiSubscriber<GetDetailEntity>() {
            @Override
            public void onSuccess(GetDetailEntity getDetailEntityV2) {
                showTv(getDetailEntityV2);
            }

            @Override
            public void onFail(Throwable e) {
                handleException(e);
            }
        });
    }

    private void getListAllEntityRelation() {
        UizaService service = RestClientV2.createService(UizaService.class);
        JsonBodyListAllEntityRelation jsonBodyListAllEntityRelation = new JsonBodyListAllEntityRelation();
        jsonBodyListAllEntityRelation.setId("5bd11904-07b8-4859-bdc8-9fee0b2199b2");
        subscribe(service.getListAllEntityRalationV2(jsonBodyListAllEntityRelation), new ApiSubscriber<ListAllEntityRelation>() {
            @Override
            public void onSuccess(ListAllEntityRelation listAllEntityRelation) {
                showTv(listAllEntityRelation);
            }

            @Override
            public void onFail(Throwable e) {
                handleException(e);
            }
        });
    }

    private void getLinkPlay() {
        UizaService service = RestClientV2.createService(UizaService.class);
        LLog.d(TAG, "appId " + appId);
        subscribe(service.getLinkPlayV2("e01c8c6c-c372-4fee-9f31-cb6d5b7fefe7", appId), new ApiSubscriber<GetLinkPlay>() {
            @Override
            public void onSuccess(GetLinkPlay getLinkPlay) {
                List<String> listLinkPlay = new ArrayList<>();
                List<Mpd> mpdList = getLinkPlay.getMpd();
                for (Mpd mpd : mpdList) {
                    if (mpd.getUrl() != null) {
                        listLinkPlay.add(mpd.getUrl());
                    }
                }
                LLog.d(TAG, "getLinkPlayV2 toJson: " + LSApplication.getInstance().getGson().toJson(listLinkPlay));
                showTv(listLinkPlay);
            }

            @Override
            public void onFail(Throwable e) {
                handleException(e);
            }
        });
    }

    //for track
    private UizaTracking create(String json) {
        return LSApplication.getInstance().getGson().fromJson(json, UizaTracking.class);
    }

    private String s;

    public void init(final UizaTracking uizaTracking) {
        RestClientTracking.init(Constants.URL_TRACKING_DEV);
        s = LSApplication.getInstance().getGson().toJson(uizaTracking);
        UizaService service = RestClientTracking.createService(UizaService.class);
        subscribe(service.track(uizaTracking), new ApiSubscriber<Object>() {
            @Override
            public void onSuccess(Object tracking) {
                s += "\n\n\n->>>>>>\n\n\n" + LSApplication.getInstance().getGson().toJson(tracking);
                tv.setText(s);
            }

            @Override
            public void onFail(Throwable e) {
                handleException(e);
            }
        });
    }
}
