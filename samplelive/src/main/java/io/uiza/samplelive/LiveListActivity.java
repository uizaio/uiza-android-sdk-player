package io.uiza.samplelive;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
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
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import io.reactivex.disposables.CompositeDisposable;
import io.uiza.extensions.PaginationScrollListener;
import io.uiza.extensions.SampleUtils;
import timber.log.Timber;
import vn.uiza.restapi.RxBinder;
import vn.uiza.restapi.model.v5.live.CreateLiveBody;
import vn.uiza.restapi.restclient.UizaClientFactory;

public class LiveListActivity extends AppCompatActivity
        implements LiveEntityAdapter.OnActionListener, PopupMenu.OnMenuItemClickListener {

    ProgressBar progressBar;
    RecyclerView recyclerView;
    String currentEntityId;
    LiveEntityAdapter adapter = new LiveEntityAdapter();
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    String region;

    String pageToken = null;
    private boolean isLoading = false;

    @Override
    protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.activity_live_list);
        progressBar = findViewById(R.id.progress_bar);
        recyclerView = findViewById(R.id.contentList);
        FloatingActionButton actionButton = findViewById(R.id.fb_btn);
        actionButton.setOnClickListener(v -> showCreateLiveDialog());
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        region = preferences.getString("region_key", "asia-south1");
        SampleUtils.setVertical(recyclerView);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.setListener(this);
        recyclerView.addOnScrollListener(new PaginationScrollListener((LinearLayoutManager) recyclerView.getLayoutManager(), actionButton) {
            @Override
            protected void loadMoreItems() {
                new Handler().postDelayed(() -> loadEntities(), 100);
            }

            @Override
            public boolean isLastPage() {
                return pageToken == null && !adapter.isEmpty();
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
        // load first network delay for API call
        new Handler().postDelayed(this::loadEntities, 100);
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
        if(adapter.isEmpty())
            progressBar.setVisibility(View.VISIBLE);
        adapter.addLoadingFooter();
        isLoading = true;
        compositeDisposable.add(RxBinder.bind(
                UizaClientFactory.getLiveService()
                        .getEntities(pageToken, 8)
                        .map(o -> {
                            pageToken = o.getNextPageToken();
                            return o.getData();
                        }),
                entities -> {
                    if (entities != null) {
                        adapter.addAll(entities);
                    }
                    isLoading = false;
                    adapter.removeLoadingFooter();
                    progressBar.setVisibility(View.GONE);
                }, throwable -> {
                    isLoading = false;
                    adapter.removeLoadingFooter();
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
        CreateLiveBody body = new CreateLiveBody(
                streamName,
                "Description of " + streamName,
                region
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

    private void showMessage(String message) {
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
                    }, throwable -> {
                        showMessage(throwable.getLocalizedMessage());
                        Timber.e(throwable);
                    }

            ));
        }
    }
}
