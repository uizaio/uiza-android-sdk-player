package testlibuiza.sample.v5;

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
import vn.uiza.restapi.model.ListWrap;
import vn.uiza.restapi.model.v5.live.CreateLiveBody;
import vn.uiza.restapi.model.v5.live.UpdateLiveBody;
import vn.uiza.restapi.restclient.UizaClientFactory;
import vn.uiza.utils.ListUtil;
import vn.uiza.utils.StringUtil;

public class UizaTestAPIActivity extends AppCompatActivity implements View.OnClickListener {

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    TextView tv;

    @Override
    protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.activity_test_v5_api);
//        v5Service = UizaClientFactory.getUizaService();
        tv = findViewById(R.id.tv);
        findViewById(R.id.bt_get_list_metadata).setOnClickListener(this);
        findViewById(R.id.bt_create_metadata).setOnClickListener(this);
        findViewById(R.id.bt_get_detail_of_metadata).setOnClickListener(this);
        findViewById(R.id.bt_update_metadata).setOnClickListener(this);
        findViewById(R.id.bt_delete_an_metadata).setOnClickListener(this);

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
                getEntities();
                break;
            case R.id.bt_create_entity:
                createEntity();
                break;
            case R.id.bt_retrieve_an_entity:
                getEntity();
                break;
            case R.id.bt_update_entity:
                updateEntity();
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


    private void getLiveEntities() {
        UizaLiveService service = UizaClientFactory.getLiveService();
        compositeDisposable.add(RxBinder.bind(service.getEntities().map(ListWrap::getData),
                liveEntities ->
                        showMessage(ListUtil.toBeautyJson(liveEntities))
                , throwable -> {
                    showMessage(throwable.getLocalizedMessage());
                    Timber.e(throwable);
                }));
    }

    private void getLiveEntity() {
        UizaLiveService service = UizaClientFactory.getLiveService();
        compositeDisposable.add(RxBinder.bind(service.getEntity("045fbb86-be25-4f72-8351-dc7a72cae1d9"),
                entity -> showMessage(StringUtil.toBeautyJson(entity)),
                throwable -> {
                    showMessage(throwable.getLocalizedMessage());
                    Timber.e(throwable);
                }));
    }

    private void createLiveEntity() {
        UizaLiveService service = UizaClientFactory.getLiveService();
        CreateLiveBody liveBody = new CreateLiveBody("test_live", "Description of live", LSApplication.DEFAULT_REGION);
        compositeDisposable.add(RxBinder.bind(service.createEntity(liveBody),
                entity -> showMessage(StringUtil.toBeautyJson(entity)),
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
                entity -> showMessage(StringUtil.toBeautyJson(entity)),
                throwable -> {
                    showMessage(throwable.getLocalizedMessage());
                    Timber.e(throwable);
                }));
    }

    private void deleteLiveEntity() {
        UizaLiveService service = UizaClientFactory.getLiveService();
        String entityId = "";
        compositeDisposable.add(RxBinder.bind(service.deleteEntity(entityId),
                response -> showMessage(StringUtil.toBeautyJson(response)),
                throwable -> {
                    showMessage(throwable.getLocalizedMessage());
                    Timber.e(throwable);
                }));
    }


    private void getLiveSessions() {
        UizaLiveService service = UizaClientFactory.getLiveService();
        String entityId = "045fbb86-be25-4f72-8351-dc7a72cae1d9";
        compositeDisposable.add(RxBinder.bind(service.getSessions(entityId).map(ListWrap::getData),
                sessions -> showMessage(StringUtil.toBeautyJson(sessions)),
                throwable -> {
                    showMessage(throwable.getLocalizedMessage());
                    Timber.e(throwable);
                }));

    }

    private void getDetailOfMetadata() {
    }

    private void updateMetadata() {
    }

    private void deleteAnMetadata() {
    }

    private void getEntities() {
    }

    private void createEntity() {
    }

    private void getEntity() {
    }

    private void updateEntity() {
    }

    private void getTokenStreaming() {
    }

    private void getLinkPlay() {
    }

    private void retrieveALiveEvent() {
    }

    private void getTokenStreamingLive() {
    }

    private void getLinkPlayLive() {
    }

    private void getViewALiveFeed() {
    }

    private void getTimeStartLive() {
    }

    private void getListSkin() {
    }

    private void getSkinConfig() {
    }

    private void getIMAAd() {
    }

    private void createMetadata() {
    }

    private void getListMetadata() {

    }
}
