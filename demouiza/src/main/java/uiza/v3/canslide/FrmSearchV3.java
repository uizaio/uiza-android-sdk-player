package uiza.v3.canslide;

/**
 * Created by www.muathu@gmail.com on 12/24/2017.
 */

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import uiza.R;
import uiza.v2.home.view.LoadingView;
import uiza.v3.view.EntityItemV3;
import vn.uiza.core.base.BaseActivity;
import vn.uiza.core.base.BaseFragment;
import vn.uiza.core.utilities.LActivityUtil;
import vn.uiza.core.utilities.LDisplayUtils;
import vn.uiza.core.utilities.LLog;
import vn.uiza.core.utilities.LScreenUtil;
import vn.uiza.core.utilities.LUIUtil;
import vn.uiza.restapi.restclient.RestClientV3;
import vn.uiza.restapi.uiza.UizaServiceV3;
import vn.uiza.restapi.uiza.model.v3.metadata.getdetailofmetadata.Data;
import vn.uiza.restapi.uiza.model.v3.videoondeman.listallentity.ResultListEntity;
import vn.uiza.rxandroid.ApiSubscriber;
import vn.uiza.utils.util.KeyboardUtils;
import vn.uiza.uzv1.view.IOnBackPressed;
import vn.uiza.views.LToast;
import vn.uiza.views.placeholderview.lib.placeholderview.PlaceHolderView;

public class FrmSearchV3 extends BaseFragment implements View.OnClickListener, IOnBackPressed {
    private ImageView ivBack;
    private ImageView ivClearText;
    private EditText etSearch;
    private TextView tv;
    private PlaceHolderView placeHolderView;
    private final int NUMBER_OF_COLUMN_1 = 1;
    private final int NUMBER_OF_COLUMN_2 = 2;
    private final int POSITION_OF_LOADING_REFRESH = 0;
    private boolean isRefreshing;
    private boolean isLoadMoreCalling;
    private final int limit = 50;
    private int page = 0;
    private int totalPage = Integer.MAX_VALUE;

    @Override
    protected String setTag() {
        return getClass().getSimpleName();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ivBack = (ImageView) view.findViewById(R.id.iv_back);
        ivClearText = (ImageView) view.findViewById(R.id.iv_clear_text);
        etSearch = (EditText) view.findViewById(R.id.et_search);
        etSearch.requestFocus();
        tv = (TextView) view.findViewById(R.id.tv);
        placeHolderView = (PlaceHolderView) view.findViewById(R.id.place_holder_view);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), NUMBER_OF_COLUMN_2);
        placeHolderView.getBuilder()
                .setHasFixedSize(false)
                .setItemViewCacheSize(10)
                .setLayoutManager(gridLayoutManager);

        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (position) {
                    case POSITION_OF_LOADING_REFRESH:
                        return isRefreshing ? NUMBER_OF_COLUMN_2 : NUMBER_OF_COLUMN_1;
                    default:
                        return NUMBER_OF_COLUMN_1;
                }
            }
        });

        LUIUtil.setPullLikeIOSVertical(placeHolderView, new LUIUtil.Callback() {
            @Override
            public void onUpOrLeft(float offset) {
            }

            @Override
            public void onUpOrLeftRefresh(float offset) {
                swipeToRefresh();
            }

            @Override
            public void onDownOrRight(float offset) {
            }

            @Override
            public void onDownOrRightRefresh(float offset) {
            }
        });

        ivBack.setOnClickListener(this);
        ivClearText.setOnClickListener(this);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().isEmpty()) {
                    ivClearText.setVisibility(View.GONE);
                    placeHolderView.removeAllViews();
                    tv.setVisibility(View.GONE);
                } else {
                    ivClearText.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        LUIUtil.setImeiActionSearch(etSearch, new LUIUtil.CallbackSearch() {
            @Override
            public void onSearch() {
                search(etSearch.getText().toString(), false);
            }
        });
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.v3_frm_search;
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

    private void search(String keyword, boolean isCallFromLoadMore) {
        tv.setVisibility(View.GONE);
        if (isCallFromLoadMore) {
        } else {
            placeHolderView.removeAllViews();
            page = 0;
            totalPage = Integer.MAX_VALUE;
        }

        if (page >= totalPage) {
            LToast.show(getActivity(), "This is last page");
            if (isCallFromLoadMore) {
                placeHolderView.removeView(getListSize() - 1);//remove loading view
                isLoadMoreCalling = false;
            }
            return;
        }
        LToast.show(getActivity(), getString(R.string.load_page) + page);


        UizaServiceV3 service = RestClientV3.createService(UizaServiceV3.class);
        subscribe(service.searchEntity(keyword), new ApiSubscriber<ResultListEntity>() {
            @Override
            public void onSuccess(ResultListEntity result) {
                if (result == null || result.getData().isEmpty()) {
                    tv.setText(getString(R.string.empty_list));
                    tv.setVisibility(View.VISIBLE);
                    return;
                }
                if (totalPage == Integer.MAX_VALUE) {
                    int totalItem = (int) result.getMetadata().getTotal();
                    float ratio = (float) (totalItem / limit);
                    if (ratio == 0) {
                        totalPage = (int) ratio;
                    } else if (ratio > 0) {
                        totalPage = (int) ratio + 1;
                    } else {
                        totalPage = (int) ratio;
                    }
                }
                setupUIList(result.getData());
            }

            @Override
            public void onFail(Throwable e) {
                LLog.e(TAG, "searchAnEntity onFail " + e.getMessage());
                if (e == null || e.toString() == null) {
                    return;
                }
                LLog.e(TAG, "search onFail " + e.toString());
                tv.setText("Error search " + e.toString());
                tv.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setupUIList(List<Data> dataList) {
        int sizeW = LDisplayUtils.getScreenW(getActivity()) / 2;
        int sizeH = sizeW * 9 / 16;
        for (Data data : dataList) {
            placeHolderView.addView(new EntityItemV3(getActivity(), data, sizeW, sizeH, new EntityItemV3.Callback() {
                @Override
                public void onClick(Data data, int position) {
                    onClickVideo(data, position);
                }

                @Override
                public void onPosition(int position) {
                    if (position == getListSize() - 1) {
                        loadMore();
                    }
                }
            }));
        }
    }

    private void onClickVideo(Data data, int position) {
        ((HomeV3CanSlideActivity) getActivity()).play(data);
    }

    private void swipeToRefresh() {
        if (isRefreshing) {
            return;
        }
        isRefreshing = true;
        placeHolderView.addView(POSITION_OF_LOADING_REFRESH, new LoadingView());

        LUIUtil.setDelay(2000, new LUIUtil.DelayCallback() {
            @Override
            public void doAfter(int mls) {
                placeHolderView.removeView(POSITION_OF_LOADING_REFRESH);
                isRefreshing = false;
            }
        });
    }

    private void loadMore() {
        if (isLoadMoreCalling) {
            return;
        }
        isLoadMoreCalling = true;
        placeHolderView.post(new Runnable() {
            @Override
            public void run() {
                placeHolderView.addView(new LoadingView());
                placeHolderView.smoothScrollToPosition(getListSize() - 1);
                page++;
                search(etSearch.getText().toString().trim(), true);
            }
        });
    }

    private int getListSize() {
        return placeHolderView.getAllViewResolvers().size();
    }

    @Override
    public boolean onBackPressed() {
        boolean isLandscapeScreen = LScreenUtil.isFullScreen(getActivity());
        if (isLandscapeScreen) {
            LActivityUtil.toggleScreenOritation((BaseActivity) getContext());
        } else {
            if (((HomeV3CanSlideActivity) getActivity()).getDraggablePanel().getVisibility() == View.VISIBLE) {
                if (((HomeV3CanSlideActivity) getActivity()).getDraggablePanel().isMaximized()) {
                    ((HomeV3CanSlideActivity) getActivity()).getDraggablePanel().minimize();
                    return true;
                } else {
                }
            } else {
            }
        }
        return false;
    }

    @Override
    public void onDestroyView() {
        KeyboardUtils.hideSoftInput(getActivity());
        super.onDestroyView();
    }
}
