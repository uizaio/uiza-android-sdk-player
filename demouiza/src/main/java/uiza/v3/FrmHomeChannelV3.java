package uiza.v3;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import uiza.R;
import uiza.app.LSApplication;
import uiza.v2.home.view.BlankView;
import uiza.v2.home.view.EntityItemV2;
import uiza.v2.home.view.LoadingView;
import uiza.v3.data.HomeDataV3;
import uiza.v3.view.EntityItemV3;
import vn.loitp.core.base.BaseFragment;
import vn.loitp.core.common.Constants;
import vn.loitp.core.utilities.LDisplayUtils;
import vn.loitp.core.utilities.LLog;
import vn.loitp.core.utilities.LUIUtil;
import vn.loitp.restapi.restclient.RestClientV2;
import vn.loitp.restapi.uiza.UizaServiceV2;
import vn.loitp.restapi.uiza.model.v2.listallentity.Item;
import vn.loitp.restapi.uiza.model.v2.listallentity.JsonBodyListAllEntity;
import vn.loitp.restapi.uiza.model.v2.listallentity.ListAllEntity;
import vn.loitp.rxandroid.ApiSubscriber;
import vn.loitp.views.LToast;
import vn.loitp.views.placeholderview.lib.placeholderview.PlaceHolderView;

/**
 * Created by www.muathu@gmail.com on 7/26/2017.
 */

public class FrmHomeChannelV3 extends BaseFragment {
    private final String TAG = getClass().getSimpleName();
    private TextView tv;
    private TextView tvMsg;
    private PlaceHolderView placeHolderView;
    private ProgressBar progressBar;

    private final int NUMBER_OF_COLUMN_1 = 1;
    private final int NUMBER_OF_COLUMN_2 = 2;
    private final int POSITION_OF_LOADING_REFRESH = 2;

    private boolean isRefreshing;
    private boolean isLoadMoreCalling;
    private final int limit = 50;
    private int page = 0;
    private int totalPage = Integer.MAX_VALUE;
    private final String orderBy = "createdAt";
    private final String orderType = "DESC";

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tv = (TextView) view.findViewById(R.id.tv);
        tvMsg = (TextView) view.findViewById(R.id.tv_msg);
        if (Constants.IS_DEBUG) {
            tv.setVisibility(View.VISIBLE);
            tv.setText("Debug: " + HomeDataV3.getInstance().getData().getName());
        } else {
            tv.setVisibility(View.GONE);
        }
        placeHolderView = (PlaceHolderView) view.findViewById(R.id.place_holder_view);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), NUMBER_OF_COLUMN_2);
        placeHolderView.getBuilder()
                .setHasFixedSize(false)
                .setItemViewCacheSize(10)
                .setLayoutManager(gridLayoutManager);

        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == POSITION_OF_LOADING_REFRESH) {
                    return isRefreshing ? NUMBER_OF_COLUMN_2 : NUMBER_OF_COLUMN_1;
                } else if (position == getListSize() - 1) {
                    //LLog.d(TAG, "isLastPage " + isLastPage);
                    if (isLastPage) {
                        return NUMBER_OF_COLUMN_1;
                    } else {
                        return NUMBER_OF_COLUMN_2;
                    }
                } else {
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
                //loadMore();
            }
        });

        progressBar = (ProgressBar) view.findViewById(R.id.pb);
        LUIUtil.setColorProgressBar(progressBar, ContextCompat.getColor(getActivity(), R.color.White));

        getData(false);
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.v3_uiza_frm_channel;
    }

    private void setupData(List<Item> itemList, boolean isCallFromLoadMore) {
        int sizeW = LDisplayUtils.getScreenW(getActivity()) / 2;
        int sizeH = sizeW * 9 / 16;

        if (isCallFromLoadMore) {
            placeHolderView.removeView(getListSize() - 1);//remove loading view
        } else {
            addBlankView();
        }
        for (Item item : itemList) {
            placeHolderView.addView(new EntityItemV2(getActivity(), item, sizeW, sizeH, new EntityItemV2.Callback() {
                @Override
                public void onClick(Item item, int position) {
                    //onClickVideo(item, position);
                    if (callback != null) {
                        callback.onClick(item, position);
                    }
                }

                @Override
                public void onPosition(int position) {
                    LLog.d(TAG, "_____onPosition " + position + " ~ " + getListSize());
                    if (position == getListSize() - 1) {
                        LLog.d(TAG, "_____onLast");
                        loadMore();
                    }
                    if (callback != null) {
                        callback.onPosition(position);
                    }
                }
            }));
        }
        if (!isCallFromLoadMore) {
            LUIUtil.hideProgressBar(progressBar);
        } else {
            isLoadMoreCalling = false;
        }
    }

    private int getListSize() {
        return placeHolderView.getAllViewResolvers().size();
    }

    private void addBlankView() {
        for (int i = 0; i < NUMBER_OF_COLUMN_2; i++) {
            placeHolderView.addView(new BlankView());
        }
    }

    private EntityItemV3.Callback callback;

    public void setCallback(EntityItemV3.Callback callback) {
        this.callback = callback;
    }

    private boolean isLastPage;

    private void getData(final boolean isCallFromLoadMore) {
        LLog.d(TAG, ">>>getData " + page + "/" + totalPage);
        if (page >= totalPage) {
            LLog.d(TAG, "page >= totalPage -> return");
            if (Constants.IS_DEBUG) {
                LToast.show(getActivity(), getString(R.string.this_is_last_page));
            }
            placeHolderView.removeView(getListSize() - 1);//remove loading view
            if (isCallFromLoadMore) {
                isLoadMoreCalling = false;
            }
            isLastPage = true;
            return;
        }

        if (Constants.IS_DEBUG) {
            LToast.show(getActivity(), getString(R.string.load_page) + page);
        }
        if (tvMsg.getVisibility() != View.GONE) {
            tvMsg.setVisibility(View.GONE);
        }
        UizaServiceV2 service = RestClientV2.createService(UizaServiceV2.class);
        JsonBodyListAllEntity jsonBodyListAllEntity = new JsonBodyListAllEntity();
        if (HomeDataV3.getInstance().getData().getId().equals(String.valueOf(Constants.NOT_FOUND))) {
            LLog.d(TAG, "HOME category");
        } else {
            LLog.d(TAG, "!HOME category");
            List<String> metadataId = new ArrayList<>();
            metadataId.add(HomeDataV3.getInstance().getData().getId());
            jsonBodyListAllEntity.setMetadataId(metadataId);
        }
        jsonBodyListAllEntity.setLimit(limit);
        jsonBodyListAllEntity.setPage(page);
        jsonBodyListAllEntity.setOrderBy(orderBy);
        jsonBodyListAllEntity.setOrderType(orderType);
        LLog.d(TAG, "jsonBodyListAllEntity " + LSApplication.getInstance().getGson().toJson(jsonBodyListAllEntity));
        LLog.d(TAG, "<<<<<<<<<<<<<<<<<<<<<<<<");

        subscribe(service.listAllEntityV2(jsonBodyListAllEntity), new ApiSubscriber<ListAllEntity>() {
            @Override
            public void onSuccess(ListAllEntity listAllEntity) {
                LLog.d(TAG, "listAllEntityV2 onSuccess " + LSApplication.getInstance().getGson().toJson(listAllEntity));
                LLog.d(TAG, "getLimit " + listAllEntity.getMetadata().getLimit());
                LLog.d(TAG, "getPage " + listAllEntity.getMetadata().getPage());
                LLog.d(TAG, "getTotal " + listAllEntity.getMetadata().getTotal());
                LLog.d(TAG, "getItems().size " + listAllEntity.getData().size());

                if (totalPage == Integer.MAX_VALUE) {
                    int totalItem = (int) listAllEntity.getMetadata().getTotal();
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

                List<Item> itemList = listAllEntity.getData();
                if (itemList == null || itemList.isEmpty()) {
                    if (tvMsg.getVisibility() != View.VISIBLE) {
                        tvMsg.setVisibility(View.VISIBLE);
                        tvMsg.setText(getString(R.string.empty_list));
                        placeHolderView.setVisibility(View.GONE);
                    }
                    if (!isCallFromLoadMore) {
                        LUIUtil.hideProgressBar(progressBar);
                    } else {
                        isLoadMoreCalling = false;
                    }
                }
                setupData(itemList, isCallFromLoadMore);
            }

            @Override
            public void onFail(Throwable e) {
                LLog.e(TAG, "listAllEntityV2 onFail " + e.toString());
                //handleException(e);
                if (tvMsg.getVisibility() != View.VISIBLE) {
                    tvMsg.setVisibility(View.VISIBLE);
                    if (e != null && e.getMessage() != null) {
                        tvMsg.setText("onFail " + e.getMessage());
                    }
                    placeHolderView.setVisibility(View.GONE);
                }
                if (!isCallFromLoadMore) {
                    LUIUtil.hideProgressBar(progressBar);
                } else {
                    isLoadMoreCalling = false;
                }
            }
        });
    }

    private void swipeToRefresh() {
        if (isRefreshing) {
            return;
        }
        isRefreshing = true;
        placeHolderView.addView(POSITION_OF_LOADING_REFRESH, new LoadingView());

        //TODO refresh
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
            LLog.d(TAG, "loadMore return");
            return;
        }
        isLoadMoreCalling = true;
        placeHolderView.post(new Runnable() {
            @Override
            public void run() {
                placeHolderView.addView(new LoadingView());
                //placeHolderView.smoothScrollToPosition(getListSize() - 1);
                page++;
                LUIUtil.setDelay(1000, new LUIUtil.DelayCallback() {
                    @Override
                    public void doAfter(int mls) {
                        getData(true);
                    }
                });
            }
        });
    }
}