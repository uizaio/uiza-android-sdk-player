package vn.uiza.utils;

import vn.uiza.core.exception.UZException;
import vn.uiza.restapi.RxBinder;
import vn.uiza.restapi.restclient.UZRestClient;
import vn.uiza.restapi.uiza.UZService;
import vn.uiza.restapi.uiza.model.v3.metadata.getdetailofmetadata.Data;

public class UZUtilBase {
    public static void getDetailEntity(final String apiVersion, final String entityId, final String appId, final CallbackGetDetailEntity callbackGetDetailEntity) {
        UZService service = UZRestClient.createService(UZService.class);
        RxBinder.getInstance().bind(service.retrieveAnEntity(apiVersion, entityId, appId), result -> {
            if (result == null
                    || result.getData() == null
                    || result.getData().getId() == null
                    || result.getData().getId().isEmpty()) {
                getDataFromEntityIdLive(apiVersion, appId, entityId, callbackGetDetailEntity);
            } else {
                if (callbackGetDetailEntity != null) {
                    Data d = result.getData();
                    callbackGetDetailEntity.onSuccess(d);
                }
            }
        }, throwable -> {
            if (callbackGetDetailEntity != null) {
                callbackGetDetailEntity.onError(throwable);
            }
        });
    }

    public static void getDataFromEntityIdLive(final String apiVersion, String appId, String entityId, final CallbackGetDetailEntity callbackGetDetailEntity) {
        UZService service = UZRestClient.createService(UZService.class);
        RxBinder.getInstance().bind(service.retrieveALiveEvent(apiVersion, entityId, appId), result -> {
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
        }, throwable -> {
            if (callbackGetDetailEntity != null) {
                callbackGetDetailEntity.onError(throwable);
            }
        });
    }
}
