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
import uiza.app.LSApplication;
import uiza.v2.home.view.LoadingView;
import uiza.v3.view.EntityItemV3;
import vn.loitp.core.base.BaseActivity;
import vn.loitp.core.base.BaseFragment;
import vn.loitp.core.utilities.LActivityUtil;
import vn.loitp.core.utilities.LDisplayUtils;
import vn.loitp.core.utilities.LLog;
import vn.loitp.core.utilities.LScreenUtil;
import vn.loitp.core.utilities.LUIUtil;
import vn.loitp.restapi.restclient.RestClientV3;
import vn.loitp.restapi.uiza.UizaServiceV3;
import vn.loitp.restapi.uiza.model.v3.metadata.getdetailofmetadata.Data;
import vn.loitp.restapi.uiza.model.v3.videoondeman.listallentity.ResultListEntity;
import vn.loitp.rxandroid.ApiSubscriber;
import vn.loitp.uizavideo.view.IOnBackPressed;
import vn.loitp.utils.util.KeyboardUtils;
import vn.loitp.views.LToast;
import vn.loitp.views.placeholderview.lib.placeholderview.PlaceHolderView;

public class FrmSearchV3 extends BaseFragment implements View.OnClickListener, IOnBackPressed {
    private final String TAG = getClass().getSimpleName();
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
                //do nothing
            }

            @Override
            public void onUpOrLeftRefresh(float offset) {
                LLog.d(TAG, "onUpOrLeftRefresh");
                swipeToRefresh();
            }

            @Override
            public void onDownOrRight(float offset) {
                //do nothing
            }

            @Override
            public void onDownOrRightRefresh(float offset) {
                LLog.d(TAG, "onDownOrRightRefresh");
            }
        });

        ivBack.setOnClickListener(this);
        ivClearText.setOnClickListener(this);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().isEmpty()) {
                    ivClearText.setVisibility(View.GONE);
                    placeHolderView.removeAllViews();
                    tv.setVisibility(View.GONE);
                    //resetAllView();
                } else {
                    ivClearText.setVisibility(View.VISIBLE);
                    //search(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                //do nothing
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
        //avi.smoothToShow();
        if (isCallFromLoadMore) {
            //do nothing
        } else {
            placeHolderView.removeAllViews();
            page = 0;
            totalPage = Integer.MAX_VALUE;
        }

        LLog.d(TAG, ">>>getPage/totalPage: " + page + "/" + totalPage);

        if (page >= totalPage) {
            LLog.d(TAG, "page >= totalPage -> return");
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
                LLog.d(TAG, "searchAnEntity onSuccess: " + LSApplication.getInstance().getGson().toJson(result));
                if (result == null || result.getData().isEmpty()) {
                    tv.setText(getString(R.string.empty_list));
                    tv.setVisibility(View.VISIBLE);
                    return;
                }
                if (totalPage == Integer.MAX_VALUE) {
                    int totalItem = (int) result.getMetadata().getTotal();
                    float ratio = (float) (totalItem / limit);
                    LLog.d(TAG, "ratio: " + ratio);
                    if (ratio == 0) {
                        totalPage = (int) ratio;
                    } else if (ratio > 0) {
                        totalPage = (int) ratio + 1;
                    } else {
                        totalPage = (int) ratio;
                    }
                    LLog.d(TAG, ">>>totalPage: " + totalPage);
                }
                LLog.d(TAG, "size: " + result.getData().size());
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

        /*UizaServiceV2 service = RestClientV2.createService(UizaServiceV2.class);

        JsonBodySearch jsonBodySearch = new JsonBodySearch();
        jsonBodySearch.setKeyword(keyword);
        jsonBodySearch.setLimit(limit);
        jsonBodySearch.setPage(page);

        subscribe(service.searchEntityV2(jsonBodySearch), new ApiSubscriber<Search>() {
            @Override
            public void onSuccess(Search search) {
                LLog.d(TAG, "search onSuccess " + LSApplication.getInstance().getGson().toJson(search));

                if (totalPage == Integer.MAX_VALUE) {
                    int totalItem = (int) search.getMetadata().getTotal();
                    float ratio = (float) (totalItem / limit);
                    LLog.d(TAG, "ratio: " + ratio);
                    if (ratio == 0) {
                        totalPage = (int) ratio;
                    } else if (ratio > 0) {
                        totalPage = (int) ratio + 1;
                    } else {
                        totalPage = (int) ratio;
                    }
                    LLog.d(TAG, ">>>totalPage: " + totalPage);
                }

                if (search == null || search.getItemList().isEmpty()) {
                    tv.setText(getString(R.string.empty_list));
                    tv.setVisibility(View.VISIBLE);
                } else {
                    LLog.d(TAG, "size: " + search.getItemList().size());
                    setupUIList(search.getItemList());
                }
            }

            @Override
            public void onFail(Throwable e) {
                if (e == null || e.toString() == null) {
                    return;
                }
                LLog.e(TAG, "search onFail " + e.toString());
                tv.setText("Error search " + e.toString());
                tv.setVisibility(View.VISIBLE);
                //avi.smoothToHide();
            }
        });*/
    }

    private void resetAllView() {
        tv.setText("");
        tv.setVisibility(View.GONE);
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
                    LLog.d(TAG, "_____onPosition " + position + " ~ " + getListSize());
                    if (position == getListSize() - 1) {
                        LLog.d(TAG, "_____onLast");
                        loadMore();
                    }
                }
            }));
        }
    }

    private void onClickVideo(Data data, int position) {
        //LLog.d(TAG, "onClickItem at " + position + ": " + LSApplication.getInstance().getGson().toJson(item));
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
        //LLog.d(TAG, "getListSize " + placeHolderView.getAllViewResolvers().size());
        return placeHolderView.getAllViewResolvers().size();
    }

    @Override
    public boolean onBackPressed() {
        //LLog.d(TAG, "onBackPressed");
        boolean isLandscapeScreen = LScreenUtil.isFullScreen(getActivity());
        if (isLandscapeScreen) {
            LActivityUtil.toggleScreenOritation((BaseActivity) getContext());
        } else {
            //LLog.d(TAG, "onBackPressed !isLandscapeScreen");
            if (((HomeV3CanSlideActivity) getActivity()).getDraggablePanel().getVisibility() == View.VISIBLE) {
                if (((HomeV3CanSlideActivity) getActivity()).getDraggablePanel().isMaximized()) {
                    //LLog.d(TAG, "onBackPressed !isLandscapeScreen VISIBLE if");
                    ((HomeV3CanSlideActivity) getActivity()).getDraggablePanel().minimize();
                    return true;
                } else {
                    //LLog.d(TAG, "onBackPressed !isLandscapeScreen VISIBLE if");
                }
            } else {
                //LLog.d(TAG, "onBackPressed !isLandscapeScreen !VISIBLE");
            }
        }
        return false;
    }

    @Override
    public void onDestroyView() {
        LLog.d(TAG, "onDestroyView");
        KeyboardUtils.hideSoftInput(getActivity());
        super.onDestroyView();
    }
}
