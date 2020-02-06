package testlibuiza.sample;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import io.reactivex.disposables.CompositeDisposable;
import testlibuiza.R;
import testlibuiza.app.LSApplication;
import timber.log.Timber;
import vn.uiza.restapi.RxBinder;
import vn.uiza.restapi.UizaLiveService;
import vn.uiza.restapi.UizaVideoService;
import vn.uiza.models.ListWrap;
import vn.uiza.models.live.CreateLiveBody;
import vn.uiza.models.live.UpdateLiveBody;
import vn.uiza.models.vod.CreateVODBody;
import vn.uiza.models.vod.UpdateVODBody;
import vn.uiza.models.vod.VODInputType;
import vn.uiza.restapi.UizaClientFactory;
import vn.uiza.utils.ListUtils;
import vn.uiza.utils.StringUtils;

public class UizaTestAPIActivity extends AppCompatActivity implements View.OnClickListener {

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    TextView tv;

    @Override
    protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.activity_test_v5_api);
        tv = findViewById(R.id.tv);
        findViewById(R.id.bt_list_all_entity).setOnClickListener(this);
        findViewById(R.id.bt_retrieve_an_entity).setOnClickListener(this);
        findViewById(R.id.bt_create_entity).setOnClickListener(this);
        findViewById(R.id.bt_update_entity).setOnClickListener(this);

        findViewById(R.id.bt_get_live_entities).setOnClickListener(this);
        findViewById(R.id.bt_get_live_entity).setOnClickListener(this);
        findViewById(R.id.bt_create_live_entity).setOnClickListener(this);
        findViewById(R.id.bt_update_live_entity).setOnClickListener(this);
        findViewById(R.id.bt_delete_live_entity).setOnClickListener(this);

        findViewById(R.id.bt_get_sessions).setOnClickListener(this);

        findViewById(R.id.bt_list_skin).setOnClickListener(this);
        findViewById(R.id.bt_skin_config).setOnClickListener(this);
        findViewById(R.id.bt_ad).setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }

    private void showMessage(String message) {
        tv.setText(message);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_list_all_entity:
                getVODEntities();
                break;
            case R.id.bt_create_entity:
                createVODEntity();
                break;
            case R.id.bt_retrieve_an_entity:
                getVODEntity();
                break;
            case R.id.bt_update_entity:
                updateVODEntity();
                break;
            case R.id.bt_get_live_entities:
                getLiveEntities();
                break;
            case R.id.bt_get_live_entity:
                getLiveEntity();
                break;
            case R.id.bt_create_live_entity:
                createLiveEntity();
                break;
            case R.id.bt_update_live_entity:
                updateLiveEntity();
                break;
            case R.id.bt_delete_live_entity:
                deleteLiveEntity();
                break;
            case R.id.bt_get_sessions:
                getLiveSessions();
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


    private void getLiveEntities() {
        UizaLiveService service = UizaClientFactory.getLiveService();
        compositeDisposable.add(RxBinder.bind(service.getEntities().map(ListWrap::getData),
                liveEntities ->
                        showMessage(ListUtils.toBeautyJson(liveEntities))
                , throwable -> {
                    showMessage(throwable.getLocalizedMessage());
                    Timber.e(throwable);
                }));
    }

    private void getLiveEntity() {
        UizaLiveService service = UizaClientFactory.getLiveService();
        compositeDisposable.add(RxBinder.bind(service.getEntity("045fbb86-be25-4f72-8351-dc7a72cae1d9"),
                entity -> showMessage(StringUtils.toBeautyJson(entity)),
                throwable -> {
                    showMessage(throwable.getLocalizedMessage());
                    Timber.e(throwable);
                }));
    }

    private void createLiveEntity() {
        UizaLiveService service = UizaClientFactory.getLiveService();
        CreateLiveBody liveBody = new CreateLiveBody("test_live", "Description of live", LSApplication.DEFAULT_REGION);
        compositeDisposable.add(RxBinder.bind(service.createEntity(liveBody),
                entity -> showMessage(StringUtils.toBeautyJson(entity)),
                throwable -> {
                    showMessage(throwable.getLocalizedMessage());
                    Timber.e(throwable);
                }));
    }

    private void updateLiveEntity() {
        UizaLiveService service = UizaClientFactory.getLiveService();
        String entityId = "045fbb86-be25-4f72-8351-dc7a72cae1d9";
        UpdateLiveBody liveBody = new UpdateLiveBody("test live again", "Description of live again");
        compositeDisposable.add(RxBinder.bind(service.updateEntity(entityId, liveBody),
                entity -> showMessage(StringUtils.toBeautyJson(entity)),
                throwable -> {
                    showMessage(throwable.getLocalizedMessage());
                    Timber.e(throwable);
                }));
    }

    private void deleteLiveEntity() {
        UizaLiveService service = UizaClientFactory.getLiveService();
        String entityId = "";
        compositeDisposable.add(RxBinder.bind(service.deleteEntity(entityId),
                response -> showMessage(StringUtils.toBeautyJson(response)),
                throwable -> {
                    showMessage(throwable.getLocalizedMessage());
                    Timber.e(throwable);
                }));
    }


    private void getLiveSessions() {
        UizaLiveService service = UizaClientFactory.getLiveService();
        String entityId = "045fbb86-be25-4f72-8351-dc7a72cae1d9";
        compositeDisposable.add(RxBinder.bind(service.getSessions(entityId).map(ListWrap::getData),
                sessions -> showMessage(StringUtils.toBeautyJson(sessions)),
                throwable -> {
                    showMessage(throwable.getLocalizedMessage());
                    Timber.e(throwable);
                }));

    }

    private void getVODEntities() {
        UizaVideoService service = UizaClientFactory.getVideoService();
        compositeDisposable.add(RxBinder.bind(service.getEntities().map(ListWrap::getData),
                liveEntities ->
                        showMessage(ListUtils.toBeautyJson(liveEntities))
                , throwable -> {
                    showMessage(throwable.getLocalizedMessage());
                    Timber.e(throwable);
                }));
    }

    private void createVODEntity() {
        UizaVideoService service = UizaClientFactory.getVideoService();
        CreateVODBody vodBody = new CreateVODBody("test_live", "https://s3-ap-southeast-1.amazonaws.com/cdnetwork-test/drm_sample_byterange/manifest.mpd", VODInputType.S3);
        compositeDisposable.add(RxBinder.bind(service.createEntity(vodBody),
                entity -> showMessage(StringUtils.toBeautyJson(entity)),
                throwable -> {
                    showMessage(throwable.getLocalizedMessage());
                    Timber.e(throwable);
                }));
    }

    private void getVODEntity() {
        UizaVideoService service = UizaClientFactory.getVideoService();
        compositeDisposable.add(RxBinder.bind(service.getEntity("4255b949-f967-4d4a-abb5-ebdd8ecb2f69"),
                entity -> showMessage(StringUtils.toBeautyJson(entity)),
                throwable -> {
                    showMessage(throwable.getLocalizedMessage());
                    Timber.e(throwable);
                }));
    }

    private void updateVODEntity() {
        UizaVideoService service = UizaClientFactory.getVideoService();
        String entityId = "4255b949-f967-4d4a-abb5-ebdd8ecb2f69";
        UpdateVODBody vodBody = new UpdateVODBody("Test vod from NamNd", "Test update VOD");
        compositeDisposable.add(RxBinder.bind(service.updateEntity(entityId, vodBody),
                entity -> showMessage(StringUtils.toBeautyJson(entity)),
                throwable -> {
                    showMessage(throwable.getLocalizedMessage());
                    Timber.e(throwable);
                }));
    }

    private void getListSkin() {
    }

    private void getSkinConfig() {
    }

    private void getIMAAd() {
    }
}
