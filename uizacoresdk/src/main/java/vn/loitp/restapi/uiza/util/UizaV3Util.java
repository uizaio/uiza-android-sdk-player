package vn.loitp.restapi.uiza.util;

import android.app.Activity;
import android.content.Context;

import com.google.gson.Gson;

import loitp.core.R;
import vn.loitp.core.base.BaseActivity;
import vn.loitp.core.common.Constants;
import vn.loitp.core.utilities.UizaPref;
import vn.loitp.restapi.restclient.RestClientV3;
import vn.loitp.restapi.uiza.UizaServiceV3;
import vn.loitp.restapi.uiza.model.v3.UizaWorkspaceInfo;
import vn.loitp.restapi.uiza.model.v3.authentication.gettoken.ResultGetToken;
import vn.loitp.restapi.uiza.model.v3.livestreaming.retrievealive.ResultRetrieveALive;
import vn.loitp.restapi.uiza.model.v3.metadata.getdetailofmetadata.Data;
import vn.loitp.restapi.uiza.model.v3.videoondeman.retrieveanentity.ResultRetrieveAnEntity;
import vn.loitp.rxandroid.ApiSubscriber;
import vn.loitp.uizavideov3.view.util.UizaDataV3;

/**
 * Created by loitp on 6/21/2018.
 */

public class UizaV3Util {
    private final static String TAG = UizaDataV3.class.getSimpleName();

    public static void initUizaWorkspace(Context context, UizaWorkspaceInfo uizaWorkspaceInfo) {
        if (uizaWorkspaceInfo == null || uizaWorkspaceInfo.getUsername() == null || uizaWorkspaceInfo.getPassword() == null || uizaWorkspaceInfo.getUrlApi() == null) {
            throw new NullPointerException("UizaWorkspaceInfo cannot be null or empty!");
        }
        UizaPref.setUizaWorkspaceInfo(context, uizaWorkspaceInfo);
        RestClientV3.init(Constants.PREFIXS + uizaWorkspaceInfo.getUrlApi());
    }

    public static UizaWorkspaceInfo getUizaWorkspace(Context context) {
        if (context == null) {
            return null;
        }
        return UizaPref.getUizaWorkspaceInfo(context);
    }

    public static void setResultGetToken(Context context, ResultGetToken resultGetToken) {
        UizaPref.setResultGetToken(context, resultGetToken);
    }

    public static ResultGetToken getResultGetToken(Context context) {
        return UizaPref.getResultGetToken(context);
    }

    public static String getToken(Context context) {
        if (context == null) {
            return null;
        }
        ResultGetToken resultGetToken = getResultGetToken(context);
        if (resultGetToken == null) {
            return null;
        }
        return resultGetToken.getData().getToken();
    }

    public static String getAppId(Context context) {
        if (context == null) {
            return null;
        }
        ResultGetToken resultGetToken = getResultGetToken(context);
        if (resultGetToken == null) {
            return null;
        }
        return resultGetToken.getData().getAppId();
    }

    public interface Callback {
        public void onSuccess(Data data);

        public void onError(Throwable e);
    }

    public static void getDetailEntity(final BaseActivity activity, final String entityId, final Callback callback) {
        UizaServiceV3 service = RestClientV3.createService(UizaServiceV3.class);
        activity.subscribe(service.retrieveAnEntity(entityId), new ApiSubscriber<ResultRetrieveAnEntity>() {
            @Override
            public void onSuccess(ResultRetrieveAnEntity result) {
                if (result == null || result.getData() == null || result.getData().getId() == null || result.getData().getId().isEmpty()) {
                    getDataFromEntityIdLIVE(activity, entityId, callback);
                } else {
                    if (callback != null) {
                        Data d = result.getData();
                        callback.onSuccess(d);
                    }
                }
            }

            @Override
            public void onFail(Throwable e) {
                if (callback != null) {
                    callback.onError(e);
                }
            }
        });
    }

    private static void getDataFromEntityIdLIVE(final BaseActivity activity, String entityId, final Callback callback) {
        UizaServiceV3 service = RestClientV3.createService(UizaServiceV3.class);
        activity.subscribe(service.retrieveALiveEvent(entityId), new ApiSubscriber<ResultRetrieveALive>() {
            @Override
            public void onSuccess(ResultRetrieveALive result) {
                if (result == null || result.getData() == null || result.getData().getId() == null || result.getData().getId().isEmpty()) {
                    if (callback != null) {
                        callback.onError(new Exception(activity.getString(R.string.err_unknow)));
                    }
                } else {
                    if (callback != null) {
                        Data d = result.getData();
                        callback.onSuccess(d);
                    }
                }
            }

            @Override
            public void onFail(Throwable e) {
                if (callback != null) {
                    callback.onError(e);
                }
            }
        });
    }

    /*public static void setData(Activity activity, Data data) {
        UizaPref.setData(activity, data, new Gson());
    }*/

    public static Data getData(Activity activity) {
        return UizaPref.getData(activity, new Gson());
    }
}
