package testlibuiza.sample;

import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import io.reactivex.disposables.CompositeDisposable;
import testlibuiza.R;
import testlibuiza.sample.utils.OnMoreActionListener;
import testlibuiza.sample.utils.SampleUtils;
import testlibuiza.sample.utils.VODEntityAdapter;
import timber.log.Timber;
import vn.uiza.restapi.RxBinder;
import vn.uiza.restapi.UizaVodService;
import vn.uiza.restapi.model.ListWrap;
import vn.uiza.restapi.model.v5.vod.VODEntity;
import vn.uiza.restapi.restclient.UizaClientFactory;
import vn.uiza.utils.ListUtil;
import vn.uiza.utils.StringUtil;

public class VODListActivity extends AppCompatActivity implements OnMoreActionListener,
        PopupMenu.OnMenuItemClickListener {

    TextView textView;
    ProgressBar progressBar;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    VODEntityAdapter adapter;
    String currentEntityId;

    @Override
    protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.activity_livelist);
        RecyclerView recyclerView = findViewById(R.id.rcv_content);
        progressBar = findViewById(R.id.progress_bar);
        textView = findViewById(R.id.tv_message);
        SampleUtils.setVertical(recyclerView, 2);
        adapter = new VODEntityAdapter();
        adapter.setMoreActionListener(this);
        recyclerView.setAdapter(adapter);
        loadVODEntities();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }

    private void loadVODEntities() {
        progressBar.setVisibility(View.VISIBLE);
        UizaVodService service = UizaClientFactory.getVideoService();
        compositeDisposable.add(RxBinder.bind(service.getEntities()
                        .map(ListWrap::getData),
                entities -> {
                    if (!ListUtil.isEmpty(entities)) {
                        adapter.setEntities(entities);
                        textView.setVisibility(View.GONE);
                    } else {
                        textView.setText("No Thread...");
                        textView.setVisibility(View.VISIBLE);
                    }
                }, throwable -> {
                    textView.setText(throwable.getLocalizedMessage());
                    textView.setVisibility(View.VISIBLE);
                    Timber.e(throwable);
                }, () ->
                        progressBar.setVisibility(View.GONE)
        ));
    }


    private void showDetailDialog(final VODEntity entity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(entity.getName());
        builder.setMessage(StringUtil.toBeautyJson(entity));
        builder.setPositiveButton(R.string.ok, (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void showPopup(View v) {
        PopupMenu popup = new PopupMenu(v.getContext(), v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.vod_menu, popup.getMenu());
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
        VODEntity entity = adapter.getItemId(currentEntityId);
        if (itemId == R.id.detail_entity) {
            showDetailDialog(entity);
        }
        return false;
    }
}
