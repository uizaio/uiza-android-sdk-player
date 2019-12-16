package io.uiza.samplelive;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

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

    String region;

    @Override
    protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.activity_live_list);
        progressBar = findViewById(R.id.progress_bar);
        recyclerView = findViewById(R.id.contentList);
        findViewById(R.id.fb_btn).setOnClickListener(v -> showCreateLiveDialog());
        region = getIntent().getStringExtra(MainActivity.CURRENT_REGION_KEY);
        if (TextUtils.isEmpty(region)) {
            region = "asia-south1";
        }
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
    protected void onDestroy() {
        super.onDestroy();
        RxBinder.getInstance().dispose();
    }

    private void loadEntities() {
        progressBar.setVisibility(View.VISIBLE);
        RxBinder.getInstance().bind(
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
                    Timber.e(throwable);
                }
        );
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
        RxBinder.getInstance().bind(
                UizaClientFactory.getLiveService().createEntity(body),
                res -> {
                    Intent intent = new Intent(LiveListActivity.this, CheckLiveActivity.class);
                    intent.putExtra(MainActivity.CURRENT_REGION_KEY, region);
                    intent.putExtra(CheckLiveActivity.EXTRA_ENTITY, res);
                    startActivity(intent);
                }, Timber::e
        );
    }

    private void removeEntity() {
        if (currentEntityId != null) {
            RxBinder.getInstance().bind(
                    UizaClientFactory.getLiveService()
                            .deleteEntity(currentEntityId),
                    res -> {
                        if (res != null && res.isDeleted()) {
                            adapter.removeItem(res.getId());
                        }
                    }, Timber::e

            );
        }
    }
}
