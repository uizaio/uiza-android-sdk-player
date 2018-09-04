package vn.loitp.uizavideov3.view.rl.livestream;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;

import loitp.core.R;
import retrofit2.HttpException;
import vn.loitp.core.base.BaseActivity;
import vn.loitp.core.common.Constants;
import vn.loitp.core.utilities.LLog;
import vn.loitp.core.utilities.LUIUtil;
import vn.loitp.restapi.restclient.RestClientV3;
import vn.loitp.restapi.uiza.UizaServiceV3;
import vn.loitp.restapi.uiza.model.ErrorBody;
import vn.loitp.restapi.uiza.model.v3.livestreaming.startALiveFeed.BodyStartALiveFeed;
import vn.loitp.restapi.uiza.model.v3.metadata.getdetailofmetadata.Data;
import vn.loitp.rxandroid.ApiSubscriber;
import vn.loitp.uizavideov3.util.UizaUtil;

/**
 * Created by loitp on 7/26/2017.
 */
public class UizaLivestream extends RelativeLayout {
    private final String TAG = "TAG" + getClass().getSimpleName();
    //TODO remove gson later
    private Gson gson = new Gson();
    private ProgressBar progressBar;
    private TextView tvLiveStatus;
    private String mainStreamUrl;

    public UizaLivestream(Context context) {
        super(context);
        onCreate();
    }

    public UizaLivestream(Context context, AttributeSet attrs) {
        super(context, attrs);
        onCreate();
    }

    public UizaLivestream(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        onCreate();
    }

    public UizaLivestream(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        onCreate();
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public TextView getTvLiveStatus() {
        return tvLiveStatus;
    }

    private void onCreate() {
        inflate(getContext(), R.layout.v3_uiza_livestream_filter, this);
        tvLiveStatus = (TextView) findViewById(R.id.tv_live_status);
        progressBar = (ProgressBar) findViewById(R.id.pb);
        LUIUtil.setColorProgressBar(progressBar, Color.WHITE);
    }

    public void setId(final String entityLiveId) {
        if (entityLiveId == null || entityLiveId.isEmpty()) {
            throw new NullPointerException(getContext().getString(R.string.entity_cannot_be_null_or_empty));
        }
        //Chỉ cần gọi start live thôi, ko cần quan tâm đến kết quả của api này start success hay ko
        //Vẫn tiếp tục gọi detail entity để lấy streamUrl
        startLivestream(entityLiveId);
    }

    private void startLivestream(final String entityLiveId) {
        UizaServiceV3 service = RestClientV3.createService(UizaServiceV3.class);
        BodyStartALiveFeed bodyStartALiveFeed = new BodyStartALiveFeed();
        bodyStartALiveFeed.setId(entityLiveId);
        ((BaseActivity) getContext()).subscribe(service.startALiveEvent(bodyStartALiveFeed), new ApiSubscriber<Object>() {
            @Override
            public void onSuccess(Object result) {
                LLog.d(TAG, "startLivestream onSuccess " + new Gson().toJson(result));
                getDetailEntity(entityLiveId, false, null);
            }

            @Override
            public void onFail(Throwable e) {
                Log.e(TAG, "startLivestream onFail " + e.toString() + ", " + e.getMessage());
                HttpException error = (HttpException) e;
                String responseBody = null;
                try {
                    responseBody = error.response().errorBody().string();
                    ErrorBody errorBody = gson.fromJson(responseBody, ErrorBody.class);
                    Log.e(TAG, "startLivestream onFail try " + errorBody);
                    getDetailEntity(entityLiveId, true, errorBody.getMessage());
                } catch (IOException e1) {
                    Log.e(TAG, "startLivestream IOException catch " + e1.toString());
                    getDetailEntity(entityLiveId, true, e1.getMessage());
                }
            }
        });
    }

    private void getDetailEntity(String entityLiveId, final boolean isErrorStartLive, final String errorMsg) {
        UizaUtil.getDataFromEntityIdLIVE((BaseActivity) getContext(), entityLiveId, new UizaUtil.Callback() {
            @Override
            public void onSuccess(Data d) {
                LLog.d(TAG, "init getDetailEntity onSuccess: " + gson.toJson(d));
                if (d == null || d.getLastPushInfo() == null || d.getLastPushInfo().isEmpty() || d.getLastPushInfo().get(0) == null) {
                    throw new NullPointerException("Data is null");
                }
                String streamKey = d.getLastPushInfo().get(0).getStreamKey();
                String streamUrl = d.getLastPushInfo().get(0).getStreamUrl();
                String mainUrl = streamUrl + "/" + streamKey;
                LLog.d(TAG, ">>>>mainUrl: " + mainUrl);

                mainStreamUrl = mainUrl;
                //TODO remove harcode
                //mainStreamUrl = "rtmp://14.161.0.68/live-origin/testapp";

                boolean isTranscode = d.getEncode() == 1;//1 is Push with Transcode, !1 Push-only, no transcode
                LLog.d(TAG, "isTranscode " + isTranscode);

                /*presetLiveStreamingFeed = new PresetLiveStreamingFeed();
                presetLiveStreamingFeed.setTranscode(isTranscode);

                boolean isConnectedFast = LConnectivityUtil.isConnectedFast(getContext());
                if (isTranscode) {
                    //Push with Transcode
                    presetLiveStreamingFeed.setS1080p(isConnectedFast ? 5000000 : 2500000);
                    presetLiveStreamingFeed.setS720p(isConnectedFast ? 3000000 : 1500000);
                    presetLiveStreamingFeed.setS480p(isConnectedFast ? 1500000 : 800000);
                } else {
                    //Push-only, no transcode
                    presetLiveStreamingFeed.setS1080p(isConnectedFast ? 2500000 : 1500000);
                    presetLiveStreamingFeed.setS720p(isConnectedFast ? 1500000 : 800000);
                    presetLiveStreamingFeed.setS480p(isConnectedFast ? 800000 : 400000);
                }*/
                LLog.d(TAG, "isErrorStartLive " + isErrorStartLive);
                if (isErrorStartLive) {
                    if (d.getLastProcess() == null) {
                        LLog.d(TAG, "isErrorStartLive -> onError Last process null");
                    } else {
                        LLog.d(TAG, "getLastProcess " + d.getLastProcess());
                        if ((d.getLastProcess().toLowerCase().equals(Constants.LAST_PROCESS_STOP))) {
                            LLog.d(TAG, "Start live 400 but last process STOP -> cannot livestream");
                        } else {
                            LLog.d(TAG, "Start live 400 but last process START || INIT -> can livestream");
                        }
                    }
                } else {
                    LLog.d(TAG, "onGetDataSuccess");
                }
                LUIUtil.hideProgressBar(progressBar);
                LLog.d(TAG, "===================finish");
            }

            @Override
            public void onError(Throwable e) {
                LLog.e(TAG, "setId onError " + e.toString());
                LUIUtil.hideProgressBar(progressBar);
            }
        });
    }
}