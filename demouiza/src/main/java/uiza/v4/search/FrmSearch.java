package uiza.v4.search;

/**
 * Created by www.muathu@gmail.com on 12/24/2017.
 */

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import io.uiza.core.api.UzApiMaster;
import io.uiza.core.api.UzServiceApi;
import io.uiza.core.api.client.UzRestClient;
import io.uiza.core.api.response.BasePaginationResponse;
import io.uiza.core.api.response.video.VideoData;
import io.uiza.core.api.util.ApiSubscriber;
import io.uiza.core.util.LLog;
import io.uiza.core.util.constant.Constants;
import io.uiza.core.view.LToast;
import java.util.ArrayList;
import java.util.List;
import uiza.R;
import uiza.v4.HomeV4CanSlideActivity;
import uiza.v4.entities.EntitiesAdapter;
import uiza.v4.helper.utils.KeyboardUtils;
import uizacoresdk.interfaces.IOnBackPressed;
import uizacoresdk.util.UZData;

public class FrmSearch extends Fragment implements View.OnClickListener, IOnBackPressed {
    private final String TAG = getClass().getSimpleName();
    private ImageView ivBack;
    private ImageView ivClearText;
    private EditText etSearch;
    private TextView tv;
    private final int limit = 20;
    private final String orderBy = "createdAt";
    private final String orderType = "DESC";
    private final String publishToCdn = "success";
    private RecyclerView recyclerView;
    private EntitiesAdapter mAdapter;
    private TextView tvMsg;
    private List<VideoData> dataList = new ArrayList<>();
    private int page = 0;
    private int totalPage = Integer.MAX_VALUE;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.v4_frm_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.rv);
        recyclerView = view.findViewById(R.id.rv);
        ivBack = view.findViewById(R.id.iv_back);
        ivClearText = view.findViewById(R.id.iv_clear_text);
        etSearch = view.findViewById(R.id.et_search);
        etSearch.requestFocus();
        tv = view.findViewById(R.id.tv);
        ivBack.setOnClickListener(this);
        ivClearText.setOnClickListener(this);
        mAdapter = new EntitiesAdapter(getActivity(), dataList, new EntitiesAdapter.Callback() {
            @Override
            public void onClick(VideoData data, int position) {
                ((HomeV4CanSlideActivity) getActivity()).playEntityId(data.getId());
            }

            @Override
            public void onLongClick(VideoData data, int position) {
            }

            @Override
            public void onLoadMore() {
                loadMore();
            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().isEmpty()) {
                    ivClearText.setVisibility(View.GONE);
                    tv.setVisibility(View.GONE);
                } else {
                    ivClearText.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        KeyboardUtils.showSoftInput(etSearch);
        if (etSearch == null) {
            return;
        }
        setImeiActionSearch(etSearch);
    }

    private void setImeiActionSearch(EditText editText) {
        if (etSearch == null) {
            return;
        }
        editText.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        editText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                search(etSearch.getText().toString());
                return true;
            }
            return false;
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                getActivity().onBackPressed();
                break;
            case R.id.iv_clear_text:
                etSearch.setText("");
                break;
        }
    }

    private void search(String keyword) {
        //TODO chia page https://jira.uiza.io/browse/UIZA-3157
        tv.setVisibility(View.GONE);
        page++;
        if (page >= totalPage) {
            if (Constants.IS_DEBUG) {
                LToast.show(getActivity(), getString(R.string.this_is_last_page));
            }
            return;
        }
        if (Constants.IS_DEBUG) {
            LToast.show(getActivity(), getString(R.string.load_page) + page);
        }
        LLog.d(TAG, "search " + page + "/" + totalPage);
        UzServiceApi service = UzRestClient.createService(UzServiceApi.class);
        UzApiMaster.getInstance()
                .subscribe(service.searchEntity(UZData.getInstance().getAPIVersion(), keyword),
                        new ApiSubscriber<BasePaginationResponse<List<VideoData>>>() {
                            @Override
                            public void onSuccess(BasePaginationResponse<List<VideoData>> reponse) {
                                if (reponse == null || reponse.getMetadata() == null || reponse
                                        .getData().isEmpty()) {
                                    tvMsg.setVisibility(View.VISIBLE);
                                    tvMsg.setText(getString(R.string.empty_list));
                                    return;
                                }
                                if (totalPage == Integer.MAX_VALUE) {
                                    int totalItem = (int) reponse.getMetadata().getTotal();
                                    float ratio = (float) (totalItem / limit);
                                    if (ratio == 0) {
                                        totalPage = (int) ratio;
                                    } else if (ratio > 0) {
                                        totalPage = (int) ratio + 1;
                                    } else {
                                        totalPage = (int) ratio;
                                    }
                                }
                                LLog.d(TAG, "-> totalPage: " + totalPage);
                                dataList.addAll(reponse.getData());
                                mAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onFail(Throwable e) {
                                LLog.e(TAG, "search onFail " + e.toString());
                                tv.setText("Error search " + e.getMessage());
                                tv.setVisibility(View.VISIBLE);
                            }
                        });
    }

    @Override
    public boolean onBackPressed() {
        LLog.d(TAG, "onBackPressed " + TAG);
        return false;
        //return ((HomeV4CanSlideActivity) getActivity()).handleOnbackpressFrm();
    }

    @Override
    public void onDestroyView() {
        ((HomeV4CanSlideActivity) getActivity()).llActionBar.setVisibility(View.VISIBLE);
        KeyboardUtils.hideSoftInput(getActivity());
        super.onDestroyView();
    }

    private void loadMore() {
        LLog.d(TAG, "loadMore");
        search(etSearch.getText().toString());
    }
}
