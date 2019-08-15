package uiza.v4.videoinfo;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
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
import io.uiza.core.api.response.video.VideoData;
import io.uiza.core.util.LLog;
import io.uiza.core.util.SentryUtil;
import io.uiza.core.util.UzDateTimeUtil;
import io.uiza.core.util.UzDisplayUtil;
import uizacoresdk.R;
import uizacoresdk.util.UZData;

/**
 * Created by www.muathu@gmail.com on 18/1/2019.
 */

public class UZVideoInfo extends RelativeLayout {
    private final String TAG = "TAG" + getClass().getSimpleName();
    private Activity activity;
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
    private RecyclerView recyclerView;

    public void clearAllViews() {
        UzDisplayUtil.showProgressBar(progressBar);

        String s = "...";
        tvVideoName.setText(s);
        tvVideoTime.setText(s);
        tvVideoRate.setText(s);
        tvVideoDescription.setText(s);
        tvVideoStarring.setText(s);
        tvVideoDirector.setText(s);
        tvVideoGenres.setText(s);
    }

    public UZVideoInfo(Context context) {
        super(context);
        onCreate();
    }

    public UZVideoInfo(Context context, AttributeSet attrs) {
        super(context, attrs);
        onCreate();
    }

    public UZVideoInfo(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        onCreate();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public UZVideoInfo(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        onCreate();
    }

    private void onCreate() {
        inflate(getContext(), R.layout.v3_uiza_ima_video_core_info_rl, this);
        activity = (Activity) getContext();
        findViews();
    }

    private void findViews() {
        nestedScrollView = findViewById(R.id.scroll_view);
        //nestedScrollView.setNestedScrollingEnabled(false);
        progressBar = findViewById(R.id.pb);
        UzDisplayUtil.setColorProgressBar(progressBar, ContextCompat.getColor(activity, R.color.White));
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

        int sizeW = UzDisplayUtil.getScreenWidth() / 2;
        int sizeH = sizeW * 9 / 16;

        recyclerView.setNestedScrollingEnabled(false);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(activity, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    public void setup(VideoData data) {
        if (data == null) {
            LLog.e(TAG, "setup resultRetrieveAnEntity == null");
            return;
        }
        if (UZData.getInstance().getData() == null || UZData.getInstance().getData().getId() == null || UZData.getInstance().getData().getId() == null) {
            LLog.e(TAG, "setup data is null");
        }
        updateUI();
    }

    public void updateUI() {
        final String emptyS = "Empty string";
        final String nullS = "LinkPlay is null";
        try {
            tvVideoName.setText(UZData.getInstance().getData().getName());
        } catch (NullPointerException e) {
            tvVideoName.setText(nullS);
            SentryUtil.captureException(e);
        }
        if (UZData.getInstance().getData().getCreatedAt() != null && !UZData.getInstance().getData().getCreatedAt().isEmpty()) {
            tvVideoTime.setText(UzDateTimeUtil.getDateWithoutTime(UZData.getInstance().getData().getCreatedAt()));
        } else {
            tvVideoTime.setText(nullS);
        }
        //TODO
        tvVideoRate.setText("12+");
        try {
            tvVideoDescription.setText(UZData.getInstance().getData().getDescription().isEmpty() ? UZData.getInstance().getData().getShortDescription().isEmpty() ? emptyS : UZData.getInstance().getData().getShortDescription() : UZData.getInstance().getData().getDescription());
        } catch (NullPointerException e) {
            tvVideoDescription.setText(nullS);
            SentryUtil.captureException(e);
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
        tvMoreLikeThisMsg.setText(R.string.no_data);
        tvMoreLikeThisMsg.setVisibility(View.VISIBLE);
        UzDisplayUtil.hideProgressBar(progressBar);
    }

}