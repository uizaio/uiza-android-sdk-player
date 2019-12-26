package testlibuiza.sample.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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


public class LiveListFragment extends Fragment implements OnMoreActionListener, PopupMenu.OnMenuItemClickListener, SwipeRefreshLayout.OnRefreshListener {


    TextView textView;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    LiveEntityAdapter adapter;
    String currentEntityId;
    SwipeRefreshLayout mSwipeRefreshLayout;
    boolean isLoading = false;

    public LiveListFragment() {
        // Required empty public constructor
    }

    /**
     * @return A new instance of fragment LiveListFragment.
     */
    public static LiveListFragment newInstance() {
        return new LiveListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.activity_livelist, container, false);
        mSwipeRefreshLayout = root.findViewById(R.id.swipe_container);
        RecyclerView recyclerView = root.findViewById(R.id.rcv_content);
        textView = root.findViewById(R.id.tv_message);
        SampleUtils.setVertical(recyclerView, 2);
        adapter = new LiveEntityAdapter();
        adapter.setOnMoreListener(this);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
        recyclerView.setAdapter(adapter);
        mSwipeRefreshLayout.post(this::loadLiveEntities);
        return root;
    }

    @Override
    public void onPause() {
        super.onPause();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mSwipeRefreshLayout.setRefreshing(false);
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }

    private void loadLiveEntities() {
        mSwipeRefreshLayout.setRefreshing(true);
        isLoading = true;
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
                    mSwipeRefreshLayout.setRefreshing(false);
                    isLoading = false;
                }, throwable -> {
                    textView.setText(throwable.getLocalizedMessage());
                    textView.setVisibility(View.VISIBLE);
                    mSwipeRefreshLayout.setRefreshing(false);
                    Timber.e(throwable);
                    isLoading = false;
                }
        ));

    }

    private void showDetailDialog(final LiveEntity entity) {
        if (getContext() != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(entity.getName());
            builder.setMessage(StringUtil.toBeautyJson(entity));
            builder.setPositiveButton(R.string.ok, (dialog, which) -> dialog.dismiss());
            builder.show();
        }
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

    @Override
    public void onRefresh() {
        if (!isLoading) {
            loadLiveEntities();
        } else {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }
}
