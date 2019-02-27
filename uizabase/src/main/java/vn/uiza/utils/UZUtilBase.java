package vn.uiza.utils;

import android.content.Context;

import vn.uiza.core.exception.UZException;
import vn.uiza.restapi.UZAPIMaster;
import vn.uiza.restapi.restclient.UZRestClient;
import vn.uiza.restapi.uiza.UZService;
import vn.uiza.restapi.uiza.model.v3.livestreaming.retrievealive.ResultRetrieveALive;
import vn.uiza.restapi.uiza.model.v3.metadata.getdetailofmetadata.Data;
import vn.uiza.restapi.uiza.model.v3.videoondeman.retrieveanentity.ResultRetrieveAnEntity;
import vn.uiza.rxandroid.ApiSubscriber;

public class UZUtilBase {
    public static void getDetailEntity(final Context context, final String entityId, final CallbackGetDetailEntity callbackGetDetailEntity) {
        UZService service = UZRestClient.createService(UZService.class);
        UZAPIMaster.getInstance().subscribe(service.retrieveAnEntity(entityId), new ApiSubscriber<ResultRetrieveAnEntity>() {
            @Override
            public void onSuccess(ResultRetrieveAnEntity result) {
                if (result == null || result.getData() == null || result.getData().getId() == null || result.getData().getId().isEmpty()) {
                    getDataFromEntityIdLIVE(context, entityId, callbackGetDetailEntity);
                } else {
                    if (callbackGetDetailEntity != null) {
                        Data d = result.getData();
                        callbackGetDetailEntity.onSuccess(d);
                    }
                }
            }

            @Override
            public void onFail(Throwable e) {
                if (callbackGetDetailEntity != null) {
                    callbackGetDetailEntity.onError(e);
                }
            }
        });
    }

    public static void getDataFromEntityIdLIVE(final Context context, String entityId, final CallbackGetDetailEntity callbackGetDetailEntity) {
        UZService service = UZRestClient.createService(UZService.class);
        UZAPIMaster.getInstance().subscribe(service.retrieveALiveEvent(entityId), new ApiSubscriber<ResultRetrieveALive>() {
            @Override
            public void onSuccess(ResultRetrieveALive result) {
                if (result == null || result.getData() == null || result.getData().getId() == null || result.getData().getId().isEmpty()) {
                    if (callbackGetDetailEntity != null) {
                        callbackGetDetailEntity.onError(new Exception(UZException.ERR_21));
                    }
                } else {
                    if (callbackGetDetailEntity != null) {
                        Data d = result.getData();
                        callbackGetDetailEntity.onSuccess(d);
                    }
                }
            }

            @Override
            public void onFail(Throwable e) {
                if (callbackGetDetailEntity != null) {
                    callbackGetDetailEntity.onError(e);
                }
            }
        });
    }
}
