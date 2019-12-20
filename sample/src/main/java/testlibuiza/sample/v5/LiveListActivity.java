package testlibuiza.sample.v5;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import io.reactivex.disposables.CompositeDisposable;
import testlibuiza.R;
import testlibuiza.sample.utils.LiveEntityAdapter;
import testlibuiza.sample.utils.SampleUtils;
import timber.log.Timber;
import vn.uiza.restapi.RxBinder;
import vn.uiza.restapi.UizaLiveService;
import vn.uiza.restapi.model.v5.live.LiveEntity;
import vn.uiza.restapi.restclient.UizaClientFactory;
import vn.uiza.utils.ListUtil;


public class LiveListActivity extends AppCompatActivity {

    TextView textView;
    ProgressBar progressBar;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    LiveEntityAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_livelist);
        RecyclerView recyclerView = findViewById(R.id.rcv_content);
        progressBar = findViewById(R.id.progress_bar);
        textView = findViewById(R.id.tv_message);
        SampleUtils.setVertical(recyclerView);
        adapter = new LiveEntityAdapter();
        recyclerView.setAdapter(adapter);
        loadLiveEntities();
    }

    private void loadLiveEntities() {
        progressBar.setVisibility(View.VISIBLE);
        UizaLiveService service = UizaClientFactory.getLiveService();
        compositeDisposable.add(RxBinder.bind(service.getEntities()
                        .map(o -> ListUtil.filter(o.getData(), LiveEntity::isOnline)),
                entities -> {
                    if (!ListUtil.isEmpty(entities)) {
                        adapter.setEntities(entities);
                        textView.setVisibility(View.GONE);
                    } else {
                        textView.setText("No LiveEntity broadcasting...");
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
}
