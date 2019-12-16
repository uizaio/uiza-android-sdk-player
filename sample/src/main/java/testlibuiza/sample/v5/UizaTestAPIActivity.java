package testlibuiza.sample.v5;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import testlibuiza.R;

public class UizaTestAPIActivity extends AppCompatActivity implements View.OnClickListener {

//    UizaService v5Service;

    @Override
    protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.activity_test_v5_api);
//        v5Service = UizaClientFactory.getUizaService();
        findViewById(R.id.bt_get_list_metadata).setOnClickListener(this);
        findViewById(R.id.bt_create_metadata).setOnClickListener(this);
        findViewById(R.id.bt_get_detail_of_metadata).setOnClickListener(this);
        findViewById(R.id.bt_update_metadata).setOnClickListener(this);
        findViewById(R.id.bt_delete_an_metadata).setOnClickListener(this);

        findViewById(R.id.bt_list_all_entity).setOnClickListener(this);
        findViewById(R.id.bt_retrieve_an_entity).setOnClickListener(this);
        findViewById(R.id.bt_create_entity).setOnClickListener(this);
        findViewById(R.id.bt_update_entity).setOnClickListener(this);

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
