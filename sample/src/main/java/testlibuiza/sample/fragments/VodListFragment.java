package testlibuiza.sample.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
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
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import io.reactivex.disposables.CompositeDisposable;
import testlibuiza.R;
import testlibuiza.sample.utils.OnMoreActionListener;
import testlibuiza.sample.utils.SampleUtils;
import testlibuiza.sample.utils.VODEntityAdapter;
import timber.log.Timber;
import vn.uiza.restapi.RxBinder;
import vn.uiza.restapi.UizaVideoService;
import vn.uiza.models.ListWrap;
import vn.uiza.models.vod.VODEntity;
import vn.uiza.restapi.UizaClientFactory;
import vn.uiza.utils.ListUtils;
import vn.uiza.utils.StringUtils;

public class VodListFragment extends Fragment implements OnMoreActionListener,
        PopupMenu.OnMenuItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    TextView textView;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    VODEntityAdapter adapter;
    String currentEntityId;
    SwipeRefreshLayout mSwipeRefreshLayout;
    private boolean isLoading = false;

    public VodListFragment() {
        // Required empty public constructor
    }

    /**
     * @return A new instance of fragment VodListFragment.
     */
    public static VodListFragment newInstance() {
        return new VodListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_livelist, container, false);
        mSwipeRefreshLayout = root.findViewById(R.id.swipe_container);
        RecyclerView recyclerView = root.findViewById(R.id.rcv_content);
        textView = root.findViewById(R.id.tv_message);
        SampleUtils.setVertical(recyclerView, 2);
        adapter = new VODEntityAdapter();
        adapter.setMoreActionListener(this);
        recyclerView.setAdapter(adapter);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
        recyclerView.setAdapter(adapter);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String apiToken = preferences.getString("api_token_key", "");
        if (!TextUtils.isEmpty(apiToken))
            mSwipeRefreshLayout.post(this::loadVODEntities);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(adapter != null && adapter.getItemCount() == 0)
        mSwipeRefreshLayout.post(this::loadVODEntities);
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

    private void loadVODEntities() {
        mSwipeRefreshLayout.setRefreshing(true);
        isLoading = true;
        UizaVideoService service = UizaClientFactory.getVideoService();
        compositeDisposable.add(RxBinder.bind(service.getEntities()
                        .map(ListWrap::getData),
                entities -> {
                    if (!ListUtils.isEmpty(entities)) {
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

    private void showDetailDialog(final VODEntity entity) {
        if (getContext() != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(entity.getName());
            builder.setMessage(StringUtils.toBeautyJson(entity));
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
        VODEntity entity = adapter.getItemId(currentEntityId);
        if (itemId == R.id.detail_entity) {
            showDetailDialog(entity);
        }
        return false;
    }

    @Override
    public void onRefresh() {
        if (!isLoading) {
            loadVODEntities();
        } else {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }
}
