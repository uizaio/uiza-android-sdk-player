package uiza.v3.canslide;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import uiza.R;
import uiza.app.LSApplication;
import uiza.v2.home.view.BlankView;
import uiza.v2.home.view.LoadingView;
import uiza.v3.data.HomeDataV3;
import uiza.v3.view.EntityItemV3;
import vn.loitp.core.base.BaseFragment;
import vn.loitp.core.common.Constants;
import vn.loitp.core.utilities.LDisplayUtils;
import vn.loitp.core.utilities.LLog;
import vn.loitp.core.utilities.LUIUtil;
import vn.loitp.restapi.restclient.RestClientV3;
import vn.loitp.restapi.uiza.UizaServiceV3;
import vn.loitp.restapi.uiza.model.v3.livestreaming.retrievealiveevent.ResultRetrieveALiveEvent;
import vn.loitp.restapi.uiza.model.v3.metadata.getdetailofmetadata.Data;
import vn.loitp.restapi.uiza.model.v3.videoondeman.listallentity.ResultListEntity;
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

    private boolean isLivestream;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isLivestream = HomeDataV3.getInstance().getData().getName().equals(Constants.MENU_LIVESTREAM);
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
                //LLog.d(TAG, "onUpOrLeftRefresh");
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

        getData(false, false);
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.v3_uiza_frm_channel;
    }

    private void setupData(List<Data> dataList, boolean isCallFromLoadMore) {
        int sizeW = LDisplayUtils.getScreenW(getActivity()) / 2;
        int sizeH = sizeW * 9 / 16;

        if (isCallFromLoadMore) {
            placeHolderView.removeView(getListSize() - 1);//remove loading view
        } else {
            addBlankView();
        }
        for (Data data : dataList) {
            placeHolderView.addView(new EntityItemV3(getActivity(), data, sizeW, sizeH, new EntityItemV3.Callback() {
                @Override
                public void onClick(Data data, int position) {
                    //onClickVideo(item, position);
                    if (callback != null) {
                        callback.onClick(data, position);
                    }
                }

                @Override
                public void onPosition(int position) {
                    //LLog.d(TAG, "_____onPosition " + position + " / " + getListSize());
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

    private void getData(final boolean isCallFromLoadMore, final boolean isCallFromSwipeToRefresh) {
        LLog.d(TAG, ">>>getData " + page + "/" + totalPage);
        if (page >= totalPage) {
            LLog.d(TAG, "page >= totalPage -> return");
            if (Constants.IS_DEBUG) {
                LToast.show(getActivity(), getString(R.string.this_is_last_page));
            }
            if (getListSize() >= 0) {
                placeHolderView.removeView(getListSize() - 1);//remove loading view
            }
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

        //check if is livestream true -> get data by using api retrieveALiveEvent
        //else call getListAllEntity
        if (isLivestream) {
            UizaServiceV3 service = RestClientV3.createService(UizaServiceV3.class);
            subscribe(service.retrieveALiveEvent(limit, page, orderBy, orderType), new ApiSubscriber<ResultRetrieveALiveEvent>() {
                @Override
                public void onSuccess(ResultRetrieveALiveEvent result) {
                    LLog.d(TAG, "retrieveALiveEvent onSuccess: " + LSApplication.getInstance().getGson().toJson(result));
                    if (isCallFromSwipeToRefresh) {
                        placeHolderView.removeAllViews();
                    }
                    if (result == null || result.getMetadata() == null || result.getData().isEmpty()) {
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
                        return;
                    }
                    if (totalPage == Integer.MAX_VALUE) {
                        int totalItem = (int) result.getMetadata().getTotal();
                        //LLog.d(TAG, "totalItem " + totalItem);
                        float ratio = (float) (totalItem / limit);
                        //LLog.d(TAG, "ratio: " + ratio);
                        if (ratio == 0) {
                            totalPage = (int) ratio;
                        } else if (ratio > 0) {
                            totalPage = (int) ratio + 1;
                        } else {
                            totalPage = (int) ratio;
                        }
                        //LLog.d(TAG, ">>>totalPage: " + totalPage);
                    }

                    List<Data> dataList = result.getData();
                    if (dataList == null || dataList.isEmpty()) {
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
                    setupData(dataList, isCallFromLoadMore);
                    if (isCallFromSwipeToRefresh) {
                        isRefreshing = false;
                    }
                }

                @Override
                public void onFail(Throwable e) {
                    LLog.e(TAG, "retrieveALiveEvent onFail " + e.getMessage());
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
                    if (isCallFromSwipeToRefresh) {
                        placeHolderView.removeAllViews();
                        isRefreshing = false;
                    }
                }
            });
        } else {
            String metadataId = "";
            if (HomeDataV3.getInstance().getData().getName().equals(Constants.MENU_HOME_V3)) {
                //do nothing
            } else {
                //LLog.d(TAG, "!HOME category");
                metadataId = HomeDataV3.getInstance().getData().getId();
            }
            //LLog.d(TAG, "getData metadataId: " + metadataId);

            UizaServiceV3 service = RestClientV3.createService(UizaServiceV3.class);
            subscribe(service.getListAllEntity(metadataId, limit, page, orderBy, orderType), new ApiSubscriber<ResultListEntity>() {
                @Override
                public void onSuccess(ResultListEntity result) {
                    if (isCallFromSwipeToRefresh) {
                        placeHolderView.removeAllViews();
                    }
                    //LLog.d(TAG, "getListAllEntity onSuccess: " + LSApplication.getInstance().getGson().toJson(result));
                    if (result == null || result.getMetadata() == null || result.getData().isEmpty()) {
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
                        return;
                    }
                    if (totalPage == Integer.MAX_VALUE) {
                        int totalItem = (int) result.getMetadata().getTotal();
                        float ratio = (float) (totalItem / limit);
                        //LLog.d(TAG, "ratio: " + ratio);
                        if (ratio == 0) {
                            totalPage = (int) ratio;
                        } else if (ratio > 0) {
                            totalPage = (int) ratio + 1;
                        } else {
                            totalPage = (int) ratio;
                        }
                        //LLog.d(TAG, ">>>totalPage: " + totalPage);
                    }

                    List<Data> dataList = result.getData();
                    if (dataList == null || dataList.isEmpty()) {
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
                    setupData(dataList, isCallFromLoadMore);
                    if (isCallFromSwipeToRefresh) {
                        isRefreshing = false;
                    }
                }

                @Override
                public void onFail(Throwable e) {
                    LLog.e(TAG, "getListAllEntity onFail " + e.getMessage());
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
                    if (isCallFromSwipeToRefresh) {
                        placeHolderView.removeAllViews();
                        isRefreshing = false;
                    }
                }
            });
        }
    }

    private void swipeToRefresh() {
        LLog.d(TAG, "swipeToRefresh");
        if (isRefreshing) {
            return;
        }
        isRefreshing = true;

        //reset value
        page = 0;
        totalPage = Integer.MAX_VALUE;
        placeHolderView.addView(POSITION_OF_LOADING_REFRESH, new LoadingView());

        LUIUtil.setDelay(1000, new LUIUtil.DelayCallback() {
            @Override
            public void doAfter(int mls) {
                getData(false, true);
                //placeHolderView.removeView(POSITION_OF_LOADING_REFRESH);
                //isRefreshing = false;
            }
        });
    }

    private void loadMore() {
        if (isLoadMoreCalling) {
            LLog.d(TAG, "loadMore return");
            return;
        }
        LLog.d(TAG, "call loadMore");
        isLoadMoreCalling = true;
        placeHolderView.post(new Runnable() {
            @Override
            public void run() {
                placeHolderView.addView(new LoadingView());
                page++;
                LUIUtil.setDelay(1000, new LUIUtil.DelayCallback() {
                    @Override
                    public void doAfter(int mls) {
                        getData(true, false);
                    }
                });
            }
        });
    }
}