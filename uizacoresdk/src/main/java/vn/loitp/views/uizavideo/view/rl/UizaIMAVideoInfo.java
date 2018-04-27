package vn.loitp.views.uizavideo.view.rl;

/**
 * Created by www.muathu@gmail.com on 12/24/2017.
 */

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.google.gson.Gson;

import loitp.core.R;
import vn.loitp.core.base.BaseActivity;
import vn.loitp.core.utilities.LLog;
import vn.loitp.restapi.restclient.RestClientV2;
import vn.loitp.restapi.uiza.UizaService;
import vn.loitp.restapi.uiza.model.v2.getdetailentity.GetDetailEntity;
import vn.loitp.restapi.uiza.model.v2.getdetailentity.JsonBodyGetDetailEntity;
import vn.loitp.rxandroid.ApiSubscriber;

/**
 * Created by www.muathu@gmail.com on 7/26/2017.
 */

public class UizaIMAVideoInfo extends RelativeLayout {
    private final String TAG = getClass().getSimpleName();
    private Activity activity;
    private Gson gson = new Gson();//TODO remove
    private String entityId;

    public void setEntityId(String entityId) {
        this.entityId = entityId;
        LLog.d(TAG, "setEntityId " + entityId);
        getDetailEntity();
    }

    public UizaIMAVideoInfo(Context context) {
        super(context);
        onCreate();
    }

    public UizaIMAVideoInfo(Context context, AttributeSet attrs) {
        super(context, attrs);
        onCreate();
    }

    public UizaIMAVideoInfo(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        onCreate();
    }

    public UizaIMAVideoInfo(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        onCreate();
    }

    private void onCreate() {
        inflate(getContext(), R.layout.uiza_ima_video_core_info_rl, this);
        activity = ((BaseActivity) getContext());
        findViews();
    }

    private void findViews() {
        //llMid = (RelativeLayout) findViewById(R.id.ll_mid);
    }

    private void getDetailEntity() {
        //API v2
        UizaService service = RestClientV2.createService(UizaService.class);
        JsonBodyGetDetailEntity jsonBodyGetDetailEntity = new JsonBodyGetDetailEntity();
        jsonBodyGetDetailEntity.setId(entityId);

        ((BaseActivity) activity).subscribe(service.getDetailEntityV2(jsonBodyGetDetailEntity), new ApiSubscriber<GetDetailEntity>() {
            @Override
            public void onSuccess(GetDetailEntity getDetailEntityV2) {
                LLog.d(TAG, "getDetailEntityV2 onSuccess " + gson.toJson(getDetailEntityV2));
            }

            @Override
            public void onFail(Throwable e) {
                LLog.e(TAG, "getDetailEntityV2 onFail " + e.toString());
                ((BaseActivity) activity).handleException(e);
            }
        });
        //EndAPI v2
    }
}