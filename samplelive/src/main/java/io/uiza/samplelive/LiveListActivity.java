package io.uiza.samplelive;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.PopupMenu;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import io.reactivex.disposables.CompositeDisposable;
import io.uiza.extensions.SampleUtils;
import timber.log.Timber;
import vn.uiza.restapi.RxBinder;
import vn.uiza.restapi.restclient.UizaClientFactory;
import vn.uiza.restapi.uiza.model.ListWrap;
import vn.uiza.restapi.uiza.model.v5.CreateLiveEntityBody;

public class LiveListActivity extends AppCompatActivity
        implements LiveEntityAdapter.OnActionListener, PopupMenu.OnMenuItemClickListener {

    ProgressBar progressBar;
    RecyclerView recyclerView;
    String currentEntityId;
    LiveEntityAdapter adapter = new LiveEntityAdapter();
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    String region;

    @Override
    protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.activity_live_list);
        progressBar = findViewById(R.id.progress_bar);
        recyclerView = findViewById(R.id.contentList);
        findViewById(R.id.fb_btn).setOnClickListener(v -> showCreateLiveDialog());
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        region = preferences.getString("region_key", "asia-south1");
        SampleUtils.setVertical(recyclerView);
        recyclerView.setAdapter(adapter);
        adapter.setListener(this);
        loadEntities();
    }

    private void showCreateLiveDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("New LiveStream");
        View root = getLayoutInflater().inflate(R.layout.dlg_create_live, null);
        builder.setView(root);
        final AppCompatEditText streamIp = root.findViewById(R.id.stream_name);
        streamIp.setHint("Stream Name");
        streamIp.setText("live_");
        builder.setPositiveButton(R.string.ok, (dialog, which) -> {
            String endpoint = streamIp.getText().toString();
            boolean isValid = true;
            if (TextUtils.isEmpty(endpoint)) {
                streamIp.setError("Error");
                isValid = false;
            }
            if (isValid) {
                dialog.dismiss();
                createLive(endpoint);
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }

    private void loadEntities() {
        Timber.e("loadEntities");
        progressBar.setVisibility(View.VISIBLE);
        compositeDisposable.add(RxBinder.bind(
                UizaClientFactory.getLiveService()
                        .getEntities()
                        .map(ListWrap::getData),
                entities -> {
                    if (entities != null) {
                        adapter.setEntities(entities);
                    }
                    progressBar.setVisibility(View.GONE);
                }, throwable -> {
                    progressBar.setVisibility(View.GONE);
                    showMessage(throwable.getLocalizedMessage());
                    Timber.e(throwable);
                }
        ));
    }

    private void showPopup(View v) {
        PopupMenu popup = new PopupMenu(v.getContext(), v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.del_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(this);
        popup.show();
    }


    @Override
    public void onMoreClick(View v, String entityId) {
        currentEntityId = entityId;
        showPopup(v);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.del_entity) {
            removeEntity();
        }
        return false;
    }

    private void createLive(String streamName) {
        progressBar.setVisibility(View.VISIBLE);
        CreateLiveEntityBody body = new CreateLiveEntityBody(
                streamName,
                "Demo of $streamName",
                region,
                SampleLiveApplication.APP_ID,
                SampleLiveApplication.USER_ID
        );
        compositeDisposable.add(RxBinder.bind(
                UizaClientFactory.getLiveService().createEntity(body),
                res -> {
                    Intent intent = new Intent(LiveListActivity.this, CheckLiveActivity.class);
                    intent.putExtra(CheckLiveActivity.EXTRA_ENTITY, res);
                    startActivity(intent);
                }, throwable -> {
                    showMessage(throwable.getLocalizedMessage());
                    Timber.e(throwable);
                }
        ));
    }

    private void showMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void removeEntity() {
        if (currentEntityId != null) {
            compositeDisposable.add(RxBinder.bind(
                    UizaClientFactory.getLiveService()
                            .deleteEntity(currentEntityId),
                    res -> {
                        if (res != null && res.isDeleted()) {
                            adapter.removeItem(res.getId());
                        }
                    },  throwable -> {
                        showMessage(throwable.getLocalizedMessage());
                        Timber.e(throwable);
                    }

            ));
        }
    }
}
