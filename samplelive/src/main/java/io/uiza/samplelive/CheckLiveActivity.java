package io.uiza.samplelive;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.res.ResourcesCompat;
import androidx.preference.PreferenceManager;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;
import vn.uiza.restapi.RxBinder;
import vn.uiza.models.live.LiveEntity;
import vn.uiza.models.live.LiveStatus;
import vn.uiza.restapi.UizaClientFactory;

public class CheckLiveActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String EXTRA_ENTITY = "uiza_extra_entity";
    TextView tvStatus, tvTitle, tvDesc;
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
        tvTitle = findViewById(R.id.tv_title);
        tvDesc = findViewById(R.id.tv_desc);
        tvStatus = findViewById(R.id.tv_status);
        liveBtn = findViewById(R.id.live_btn);
        progressBar = findViewById(R.id.progress_bar);
        liveBtn.setOnClickListener(this);
        entity = getIntent().getParcelableExtra(EXTRA_ENTITY);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        region = preferences.getString("region_key", "asia-south1");
        if (entity == null) {
            Toast.makeText(this, "Live Entity is null", Toast.LENGTH_LONG).show();
            (new Handler()).postDelayed(() -> finish(), 1000);
        }
        updateLiveStats();
        if (!entity.canLive())
            new Handler().postDelayed(() -> getEntity(entity.getId()), 100);
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

    private void updateLiveStats() {
        tvTitle.setText(entity.getName());
        tvDesc.setText(entity.getDescription());
        setStatusView(entity.getStatus());
        if (entity.canLive()) {
            liveBtn.setText("Go Live");
        } else {
            liveBtn.setText("Check Status");
        }
    }

    private void setStatusView(LiveStatus status) {
        if (status == null) {
            tvStatus.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.background_err, null));
            tvStatus.setText("Error");
            return;
        }
        switch (status) {
            case INIT:
                tvStatus.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.background_init, null));
                break;
            case READY:
                tvStatus.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.background_ready, null));
                break;
            case BROADCASTING:
                tvStatus.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.background_live, null));
                break;
        }
        tvStatus.setText(status.getValue());
    }

    private void getEntity(String entityId) {
        progressBar.setVisibility(View.VISIBLE);
        liveBtn.setEnabled(false);
        Observable<LiveEntity> obs = UizaClientFactory.getLiveService().getEntity(entityId);
        compositeDisposable.add(RxBinder.bind(obs, ent -> {
            entity = ent;
            updateLiveStats();
            if (!entity.canLive() && currentRetry < MAX_RETRY) {
                Timber.e("currentRetry: %d", currentRetry);
                currentRetry += 1;
                handler.postDelayed(() -> getEntity(entityId), 3000);
            } else {
                progressBar.setVisibility(View.GONE);
                liveBtn.setEnabled(true);
                liveBtn.setEnabled(true);
            }
        }, throwable -> {
            progressBar.setVisibility(View.GONE);
            liveBtn.setEnabled(true);
            liveBtn.setEnabled(true);
            Toast.makeText(this, throwable.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            updateLiveStats();
        }));
    }

}
