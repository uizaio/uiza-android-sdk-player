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
import testlibuiza.sample.utils.LiveEntityAdapter;
import testlibuiza.sample.utils.OnMoreActionListener;
import testlibuiza.sample.utils.SampleUtils;
import timber.log.Timber;
import vn.uiza.restapi.RxBinder;
import vn.uiza.restapi.UizaLiveService;
import vn.uiza.restapi.model.v5.live.LiveEntity;
import vn.uiza.restapi.restclient.UizaClientFactory;
import vn.uiza.utils.ListUtil;
import vn.uiza.utils.StringUtil;


public class LiveListActivity extends AppCompatActivity implements OnMoreActionListener, PopupMenu.OnMenuItemClickListener {

    TextView textView;
    ProgressBar progressBar;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    LiveEntityAdapter adapter;
    String currentEntityId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_livelist);
        RecyclerView recyclerView = findViewById(R.id.rcv_content);
        progressBar = findViewById(R.id.progress_bar);
        textView = findViewById(R.id.tv_message);
        SampleUtils.setVertical(recyclerView, 2);
        adapter = new LiveEntityAdapter();
        adapter.setOnMoreListener(this);
        recyclerView.setAdapter(adapter);
        loadLiveEntities();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }

    private void loadLiveEntities() {
        progressBar.setVisibility(View.VISIBLE);
        UizaLiveService service = UizaClientFactory.getLiveService();
        compositeDisposable.add(RxBinder.bind(service.getEntities()
                        .map(o -> ListUtil.filter(o.getData(), e -> !e.isInit())),
                entities -> {
                    if (!ListUtil.isEmpty(entities)) {
                        adapter.setEntities(entities);
                        textView.setVisibility(View.GONE);
                    } else {
                        textView.setText("No Thread...");
                        textView.setVisibility(View.VISIBLE);
                    }
                    progressBar.setVisibility(View.GONE);
                }, throwable -> {
                    textView.setText(throwable.getLocalizedMessage());
                    textView.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    Timber.e(throwable);
                }
        ));

    }

    private void showDetailDialog(final LiveEntity entity) {
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
        LiveEntity entity = adapter.getItemId(currentEntityId);
        if (itemId == R.id.detail_entity) {
            showDetailDialog(entity);
        }
        return false;
    }
}
