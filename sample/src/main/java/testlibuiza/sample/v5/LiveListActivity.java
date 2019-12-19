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
import vn.uiza.restapi.model.ListWrap;
import vn.uiza.restapi.restclient.UizaClientFactory;


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
        compositeDisposable.add(RxBinder.bind(service.getEntities().map(ListWrap::getData), entities -> {
            if (entities != null && !entities.isEmpty()) {
                adapter.setEntities(entities);
                textView.setVisibility(View.GONE);
            } else {
                textView.setText("Empty");
                textView.setVisibility(View.VISIBLE);
            }
            progressBar.setVisibility(View.GONE);
        }, throwable -> {
            textView.setText(throwable.getLocalizedMessage());
            progressBar.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
            Timber.e(throwable);
        }));

    }
}
