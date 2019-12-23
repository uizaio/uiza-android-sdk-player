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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.PopupMenu;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import io.reactivex.disposables.CompositeDisposable;
import io.uiza.adapters.LiveEntityAdapter;
import io.uiza.extensions.SampleUtils;
import timber.log.Timber;
import vn.uiza.core.utilities.LUIUtil;
import vn.uiza.restapi.RxBinder;
import vn.uiza.restapi.model.ListWrap;
import vn.uiza.restapi.model.v5.live.CreateLiveBody;
import vn.uiza.restapi.model.v5.live.LiveEntity;
import vn.uiza.restapi.model.v5.live.UpdateLiveBody;
import vn.uiza.restapi.restclient.UizaClientFactory;
import vn.uiza.utils.StringUtil;

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
        FloatingActionButton actionButton = findViewById(R.id.fb_btn);
        actionButton.setOnClickListener(v -> showCreateLiveDialog());
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        region = preferences.getString("region_key", "asia-south1");
        SampleUtils.setVertical(recyclerView, 2);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.setListener(this);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && actionButton.getVisibility() == View.VISIBLE) {
                    actionButton.hide();
                } else if (dy < 0 && actionButton.getVisibility() != View.VISIBLE) {
                    actionButton.show();
                }
            }
        });
        new Handler().postDelayed(this::loadEntities, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1001) {
            if (adapter != null) {
                new Handler().postDelayed(this::loadEntities, 100);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
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

    private void showUpdateLiveDialog(final LiveEntity entity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(entity.getName());
        View root = getLayoutInflater().inflate(R.layout.dlg_update_live, null);
        builder.setView(root);
        final AppCompatEditText streamIp = root.findViewById(R.id.stream_name);
        final AppCompatEditText descIp = root.findViewById(R.id.stream_desc);
        streamIp.setText(entity.getName());
        LUIUtil.setLastCursorEditText(streamIp);
        descIp.setText(entity.getDescription());
        LUIUtil.setLastCursorEditText(descIp);
        builder.setPositiveButton(R.string.ok, (dialog, which) -> {
            String name = streamIp.getText().toString();
            String desc = descIp.getText().toString();

            boolean isValid = true;
            if (TextUtils.isEmpty(name)) {
                streamIp.setError("Error");
                isValid = false;
            }
            if (TextUtils.isEmpty(desc)) {
                descIp.setError("Error");
                isValid = false;
            }
            if (name.equalsIgnoreCase(entity.getName()) && desc.equalsIgnoreCase(entity.getDescription())) {
                isValid = false;
            }
            if (isValid) {
                dialog.dismiss();
                updateEntity(new UpdateLiveBody(name, desc));
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void showDetailDialog(final LiveEntity entity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(entity.getName());
        builder.setMessage(StringUtil.toBeautyJson(entity));
        builder.setPositiveButton(R.string.ok, (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void showConfirmRemoveDialog(LiveEntity entity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(entity.getName());
        builder.setMessage("Do you want Remove?");
        builder.setPositiveButton(R.string.ok, (dialog, which) -> {
            removeEntity();
            dialog.dismiss();
        });
        builder.setNegativeButton(R.string.cancel, (dialog, which) -> dialog.cancel());
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
        if (adapter.isEmpty())
            progressBar.setVisibility(View.VISIBLE);
        adapter.addLoadingFooter();
        compositeDisposable.add(RxBinder.bind(
                UizaClientFactory.getLiveService()
                        .getEntities()
                        .map(ListWrap::getData),
                entities -> {
                    if (entities != null) {
                        adapter.setEntities(entities);
                    }
                    adapter.removeLoadingFooter();
                    progressBar.setVisibility(View.GONE);
                }, throwable -> {
                    adapter.removeLoadingFooter();
                    progressBar.setVisibility(View.GONE);
                    if (!(throwable instanceof java.lang.NullPointerException)) {
                        showMessage(throwable.getLocalizedMessage());
                    }
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
        int itemId = item.getItemId();
        LiveEntity entity = adapter.getItemId(currentEntityId);
        if (itemId == R.id.del_entity) {
            showConfirmRemoveDialog(entity);
        } else if (itemId == R.id.rename_entity) {
            showUpdateLiveDialog(entity);
        } else if (itemId == R.id.detail_entity) {
            showDetailDialog(entity);
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
                    startActivityForResult(intent, 1001);
                }, throwable -> {
                    showMessage(throwable.getLocalizedMessage());
                    Timber.e(throwable);
                }
        ));
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void updateEntity(@NonNull UpdateLiveBody updateLiveBody) {
        progressBar.setVisibility(View.VISIBLE);
        if (currentEntityId != null) {
            compositeDisposable.add(RxBinder.bind(
                    UizaClientFactory.getLiveService()
                            .updateEntity(currentEntityId, updateLiveBody),
                    res -> adapter.replace(res), throwable -> {
                        showMessage(throwable.getLocalizedMessage());
                        Timber.e(throwable);
                    }, () -> progressBar.setVisibility(View.GONE)

            ));
        }
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
