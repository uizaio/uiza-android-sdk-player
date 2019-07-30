package uiza.v4.videoinfo;

/**
 * Created by www.muathu@gmail.com on 12/24/2017.
 */

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
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
import java.util.ArrayList;
import java.util.List;
import uiza.v4.videoinfo.util.UizaDataV1;
import uizacoresdk.R;
import vn.uiza.core.utilities.LScreenUtil;
import vn.uiza.core.utilities.LUIUtil;
import vn.uiza.restapi.uiza.model.v2.getdetailentity.GetDetailEntity;
import vn.uiza.restapi.uiza.model.v2.listallentity.Item;
import vn.uiza.utils.util.SentryUtils;

public class UZVideoInfoV1 extends RelativeLayout {
    private final String TAG = getClass().getSimpleName();
    private Activity activity;
    //private Gson gson = new Gson();
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
    private Item mItem;

    private List<Item> itemList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ItemAdapterV1 mAdapter;
    private ItemAdapterV1.Callback callback;

    public void init(ItemAdapterV1.Callback callback) {
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

    public UZVideoInfoV1(Context context) {
        super(context);
        onCreate();
    }

    public UZVideoInfoV1(Context context, AttributeSet attrs) {
        super(context, attrs);
        onCreate();
    }

    public UZVideoInfoV1(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        onCreate();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public UZVideoInfoV1(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        onCreate();
    }

    private void onCreate() {
        inflate(getContext(), R.layout.uiza_ima_video_core_info_rl, this);
        activity = (Activity) getContext();
        findViews();
    }

    private void findViews() {
        nestedScrollView = findViewById(R.id.scroll_view);
        //nestedScrollView.setNestedScrollingEnabled(false);
        progressBar = findViewById(R.id.pb);
        LUIUtil.setColorProgressBar(progressBar, ContextCompat.getColor(activity, R.color.White));
        recyclerView = findViewById(R.id.rv);
        tvVideoName = findViewById(R.id.tv_video_name);
        tvVideoTime = findViewById(R.id.tv_video_time);
        tvVideoRate = findViewById(R.id.tv_video_rate);
        tvVideoDescription = findViewById(R.id.tv_video_description);
        tvVideoStarring = findViewById(R.id.tv_video_starring);
        tvVideoDirector = findViewById(R.id.tv_video_director);
        tvVideoGenres = findViewById(R.id.tv_video_genres);
        tvDebug = findViewById(R.id.tv_debug);
        tvMoreLikeThisMsg = findViewById(R.id.tv_more_like_this_msg);

        int sizeW = LScreenUtil.getScreenWidth() / 2;
        int sizeH = sizeW * 9 / 16;
        mAdapter = new ItemAdapterV1(activity, itemList, sizeW, sizeH, new ItemAdapterV1.Callback() {
            @Override
            public void onClickItemBottom(Item item, int position) {
                if (UizaDataV1.getInstance().isSettingPlayer()) {
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

    public void setup(GetDetailEntity getDetailEntity) {
        if (getDetailEntity == null) {
            return;
        }
        mItem = getDetailEntity.getData().get(0);
        updateUI();
    }

    public void updateUI() {
        final String emptyS = "Empty string";
        final String nullS = "Data is null";
        try {
            tvVideoName.setText(mItem.getName());
        } catch (NullPointerException e) {
            tvVideoName.setText(nullS);
            SentryUtils.captureException(e);
        }
        tvVideoTime.setText("Dummy Time");
        tvVideoRate.setText("Dummy 18+");

        try {
            tvVideoDescription.setText(mItem.getDescription().isEmpty() ? mItem.getShortDescription().isEmpty() ? emptyS : mItem.getShortDescription() : mItem.getDescription());
        } catch (NullPointerException e) {
            tvVideoDescription.setText(nullS);
            SentryUtils.captureException(e);
        }

        tvVideoStarring.setText("Dummy starring");

        if (mItem.getExtendData() == null || mItem.getExtendData().getDirector() == null) {
            tvVideoDirector.setText(nullS);
        } else {
            tvVideoDirector.setText(mItem.getExtendData().getDirector());
        }

        tvVideoGenres.setText(emptyS);

        //get more like this video
        getListAllEntityRelation();
    }

    private void getListAllEntityRelation() {
        //TODO getListAllEntityRelation
        tvMoreLikeThisMsg.setText(R.string.no_data);
        tvMoreLikeThisMsg.setVisibility(View.VISIBLE);
        LUIUtil.hideProgressBar(progressBar);
    }

    private void setupUIMoreLikeThis(List<Item> itemList) {
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