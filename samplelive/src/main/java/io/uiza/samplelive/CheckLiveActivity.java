package io.uiza.samplelive;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.widget.NestedScrollView;
import androidx.preference.PreferenceManager;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;
import vn.uiza.restapi.RxBinder;
import vn.uiza.restapi.restclient.UizaClientFactory;
import vn.uiza.restapi.uiza.model.v5.CreateLiveEntityBody;
import vn.uiza.restapi.uiza.model.v5.LiveEntity;
import vn.uiza.utils.StringUtil;

public class CheckLiveActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String EXTRA_ENTITY = "uiza_extra_entity";
    AppCompatEditText streamNameEdt;
    NestedScrollView contentScroll;
    TextView content;
    AppCompatButton liveBtn;
    ProgressBar progressBar;
    LiveEntity entity;
    Handler handler = new Handler();
    String region;
    private static final int MAX_RETRY = 10;
    int currentRetry = 0;

    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_live);
        streamNameEdt = findViewById(R.id.stream_name);
        contentScroll = findViewById(R.id.scrollView);
        content = findViewById(R.id.content);
        liveBtn = findViewById(R.id.live_btn);
        progressBar = findViewById(R.id.progress_bar);
        liveBtn.setOnClickListener(this);
        entity = getIntent().getParcelableExtra(EXTRA_ENTITY);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        region = preferences.getString("region_key", "asia-south1");
        if (entity != null) {
            content.setText(StringUtil.toBeautyJson(entity));
        }
        updateLiveStats();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateLiveStats();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.live_btn) {
            if (streamNameEdt.getVisibility() == View.VISIBLE) {
                createLive(streamNameEdt.getText().toString());
            } else {
                if (entity != null) {
                    if (entity.canLive()) {
                        Intent liveIntent = new Intent(CheckLiveActivity.this, UizaLiveActivity.class);
                        liveIntent.putExtra(SampleLiveApplication.EXTRA_STREAM_ENDPOINT, entity.getIngest().getStreamLink());
                        startActivity(liveIntent);
                    } else {
                        currentRetry = 0;
                        liveBtn.setEnabled(false);
                        getEntity(entity.getId());
                    }

                } else {
                    Toast.makeText(this, "No Action", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    private void updateLiveStats() {
        if (entity == null) {
            streamNameEdt.setVisibility(View.VISIBLE);
            contentScroll.setVisibility(View.GONE);
            liveBtn.setText("Create Live");
        } else {
            streamNameEdt.setVisibility(View.GONE);
            contentScroll.setVisibility(View.VISIBLE);
            if (entity.canLive()) {
                liveBtn.setText("Go Live");
            } else {
                liveBtn.setText("Update Status");
            }
        }
    }

    private void createLive(String streamName) {
        progressBar.setVisibility(View.VISIBLE);
        CreateLiveEntityBody body = new CreateLiveEntityBody(streamName, "Uiza Demo Live Stream", region, SampleLiveApplication.APP_ID, SampleLiveApplication.USER_ID);
        Observable<LiveEntity> obs = UizaClientFactory.getLiveService().createEntity(body);
        compositeDisposable.add(RxBinder.bind(obs, res -> {
            entity = res;
            content.setText(res.toString());
            updateLiveStats();
            progressBar.setVisibility(View.GONE);
        }, throwable -> {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(this, throwable.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            updateLiveStats();
        }));
    }

    private void getEntity(String entityId) {
        progressBar.setVisibility(View.VISIBLE);
        Observable<LiveEntity> obs = UizaClientFactory.getLiveService().getEntity(entityId);
        compositeDisposable.add(RxBinder.bind(obs, ent -> {
            entity = ent;
            content.setText(StringUtil.toBeautyJson(ent));
            if (!entity.canLive() && currentRetry < MAX_RETRY) {
                Timber.e("currentRetry: %d", currentRetry);
                currentRetry += 1;
                handler.postDelayed(() -> getEntity(entityId), 3000);
            } else {
                updateLiveStats();
                progressBar.setVisibility(View.GONE);
                liveBtn.setEnabled(true);
            }
        }, throwable -> {
            progressBar.setVisibility(View.GONE);
            liveBtn.setEnabled(true);
            Toast.makeText(this, throwable.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            updateLiveStats();
        }));
    }

}
