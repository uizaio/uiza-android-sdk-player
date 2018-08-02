package vn.loitp.uizavideov3.view.rl.videoinfo;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import loitp.core.R;
import vn.loitp.core.base.BaseActivity;
import vn.loitp.core.utilities.LDateUtils;
import vn.loitp.core.utilities.LDisplayUtils;
import vn.loitp.core.utilities.LLog;
import vn.loitp.core.utilities.LPref;
import vn.loitp.core.utilities.LUIUtil;
import vn.loitp.restapi.uiza.model.v2.listallentity.Item;
import vn.loitp.restapi.uiza.model.v3.metadata.getdetailofmetadata.Data;
import vn.loitp.uizavideo.view.rl.videoinfo.ItemAdapterV2;
import vn.loitp.uizavideov3.view.util.UizaDataV3;

/**
 * Created by www.muathu@gmail.com on 7/26/2017.
 */

public class UizaIMAVideoInfoV3 extends RelativeLayout {
    private final String TAG = getClass().getSimpleName();
    private Activity activity;
    //TODO remove
    private Gson gson = new Gson();
    private ProgressBar progressBar;
    private TextView tvVideoName;
    private TextView tvVideoTime;
    private TextView tvVideoRate;
    private TextView tvVideoDescription;
    private TextView tvVideoStarring;
    private TextView tvVideoDirector;
    private TextView tvVideoGenres;
    private TextView tvDebug;
    private TextView tvMoreLikeThisMsg;
    private NestedScrollView nestedScrollView;
    private Data data;

    private List<Item> itemList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ItemAdapterV2 mAdapter;
    private ItemAdapterV2.Callback callback;

    public void init(ItemAdapterV2.Callback callback) {
        this.callback = callback;
        clearAllViews();
    }

    public void clearAllViews() {
        itemList.clear();
        notifyViews();
        LUIUtil.showProgressBar(progressBar);

        String s = "...";
        tvVideoName.setText(s);
        tvVideoTime.setText(s);
        tvVideoRate.setText(s);
        tvVideoDescription.setText(s);
        tvVideoStarring.setText(s);
        tvVideoDirector.setText(s);
        tvVideoGenres.setText(s);
    }

    public UizaIMAVideoInfoV3(Context context) {
        super(context);
        onCreate();
    }

    public UizaIMAVideoInfoV3(Context context, AttributeSet attrs) {
        super(context, attrs);
        onCreate();
    }

    public UizaIMAVideoInfoV3(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        onCreate();
    }

    public UizaIMAVideoInfoV3(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        onCreate();
    }

    private void onCreate() {
        inflate(getContext(), R.layout.v3_uiza_ima_video_core_info_rl, this);
        activity = ((BaseActivity) getContext());
        findViews();
    }

    private void findViews() {
        nestedScrollView = (NestedScrollView) findViewById(R.id.scroll_view);
        //nestedScrollView.setNestedScrollingEnabled(false);
        progressBar = (ProgressBar) findViewById(R.id.pb);
        LUIUtil.setColorProgressBar(progressBar, ContextCompat.getColor(activity, R.color.White));
        recyclerView = (RecyclerView) findViewById(R.id.rv);
        tvVideoName = (TextView) findViewById(R.id.tv_video_name);
        tvVideoTime = (TextView) findViewById(R.id.tv_video_time);
        tvVideoRate = (TextView) findViewById(R.id.tv_video_rate);
        tvVideoDescription = (TextView) findViewById(R.id.tv_video_description);
        tvVideoStarring = (TextView) findViewById(R.id.tv_video_starring);
        tvVideoDirector = (TextView) findViewById(R.id.tv_video_director);
        tvVideoGenres = (TextView) findViewById(R.id.tv_video_genres);
        tvDebug = (TextView) findViewById(R.id.tv_debug);
        tvMoreLikeThisMsg = (TextView) findViewById(R.id.tv_more_like_this_msg);

        int sizeW = LDisplayUtils.getScreenW(activity) / 2;
        int sizeH = sizeW * 9 / 16;
        mAdapter = new ItemAdapterV2(activity, itemList, sizeW, sizeH, new ItemAdapterV2.Callback() {
            @Override
            public void onClickItemBottom(Item item, int position) {
                if (UizaDataV3.getInstance().isSettingPlayer()) {
                    return;
                }
                itemList.clear();
                notifyViews();
                if (callback != null) {
                    callback.onClickItemBottom(item, position);
                }
            }

            @Override
            public void onLoadMore() {
                loadMore();
                if (callback != null) {
                    callback.onLoadMore();
                }
            }
        });

        recyclerView.setNestedScrollingEnabled(false);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(activity, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }

    public void setup(Data data) {
        LLog.d(TAG, "setup");
        if (data == null) {
            LLog.d(TAG, "setup resultRetrieveAnEntity == null");
            return;
        }
        this.data = data;
        if (this.data == null || this.data.getId() == null || this.data.getId() == null) {
            LLog.d(TAG, "setup data is null");
            this.data = LPref.getData(activity, gson);
        }
        LLog.d(TAG, "setup " + gson.toJson(this.data));
        updateUI();
    }

    public void updateUI() {
        final String emptyS = "Empty string";
        final String nullS = "Data is null";
        try {
            tvVideoName.setText(data.getName());
        } catch (NullPointerException e) {
            tvVideoName.setText(nullS);
        }
        if (data.getCreatedAt() != null && !data.getCreatedAt().isEmpty()) {
            tvVideoTime.setText(LDateUtils.getDateWithoutTime(data.getCreatedAt()));
        } else {
            tvVideoTime.setText(nullS);
        }
        //TODO
        tvVideoRate.setText("12+");

        try {
            tvVideoDescription.setText(data.getDescription().isEmpty() ? data.getShortDescription().isEmpty() ? emptyS : data.getShortDescription() : data.getDescription());
        } catch (NullPointerException e) {
            tvVideoDescription.setText(nullS);
        }

        //TODO
        tvVideoStarring.setText("Dummy starring");

        //TODO
        tvVideoDirector.setText("Dummy director");

        //TODO
        tvVideoGenres.setText("Dummy genres");

        //get more like this video
        getListAllEntityRelation();
    }

    private void getListAllEntityRelation() {
        //TODO
        /*UizaServiceV2 service = RestClientV2.createService(UizaServiceV2.class);
        //LLog.d(TAG, "entityId: " + UizaData.getInstance().getEntityId());

        JsonBodyListAllEntityRelation jsonBodyListAllEntityRelation = new JsonBodyListAllEntityRelation();
        jsonBodyListAllEntityRelation.setId(UizaData.getInstance().getUizaInputV3().getEntityId());

        ((BaseActivity) activity).subscribe(service.getListAllEntityRalationV2(jsonBodyListAllEntityRelation), new ApiSubscriber<ListAllEntityRelation>() {
            @Override
            public void onSuccess(ListAllEntityRelation listAllEntityRelation) {
                //LLog.d(TAG, "getListAllEntityRalationV1 onSuccess " + gson.toJson(listAllEntityRelation));
                if (listAllEntityRelation == null || listAllEntityRelation.getItemList().isEmpty()) {
                    tvMoreLikeThisMsg.setText(R.string.no_data);
                    tvMoreLikeThisMsg.setVisibility(View.VISIBLE);
                } else {
                    tvMoreLikeThisMsg.setVisibility(View.GONE);
                    setupUIMoreLikeThis(listAllEntityRelation.getItemList());
                }
                LUIUtil.hideProgressBar(progressBar);
            }

            @Override
            public void onFail(Throwable e) {
                LLog.e(TAG, "getListAllEntityRelation onFail " + e.toString());
                LDialogUtil.showDialog1(activity, activity.getString(R.string.cannot_get_list_relation), new LDialogUtil.Callback1() {
                    @Override
                    public void onClick1() {
                        if (activity != null) {
                            activity.onBackPressed();
                        }
                    }

                    @Override
                    public void onCancel() {
                        if (activity != null) {
                            activity.onBackPressed();
                        }
                    }
                });
                LUIUtil.hideProgressBar(progressBar);
            }
        });*/

        //TODO remove hardcode
        tvMoreLikeThisMsg.setText(R.string.no_data);
        tvMoreLikeThisMsg.setVisibility(View.VISIBLE);
        LUIUtil.hideProgressBar(progressBar);
    }

    private void setupUIMoreLikeThis(List<Item> itemList) {
        //LLog.d(TAG, "setupUIMoreLikeThis itemList size: " + itemList.size());
        this.itemList.addAll(itemList);
        notifyViews();
    }

    private void notifyViews() {
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    private void loadMore() {
        //do nothing
    }
}