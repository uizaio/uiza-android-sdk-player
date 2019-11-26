package vn.uiza.utils;

import vn.uiza.core.exception.UZException;
import vn.uiza.restapi.UZAPIMaster;
import vn.uiza.restapi.restclient.UZRestClient;
import vn.uiza.restapi.uiza.UZService;
import vn.uiza.restapi.uiza.model.v3.metadata.getdetailofmetadata.Data;

public class UZUtilBase {
    public static void getDetailEntity(final String entityId, final CallbackGetDetailEntity callbackGetDetailEntity) {
        UZService service = UZRestClient.createService(UZService.class);
        UZAPIMaster.getInstance().subscribe(service.retrieveAnEntity(entityId), resultRetrieveAnEntity -> {
            if (resultRetrieveAnEntity == null
                    || resultRetrieveAnEntity.getData() == null
                    || resultRetrieveAnEntity.getData().getId() == null
                    || resultRetrieveAnEntity.getData().getId().isEmpty()) {

                getDataFromEntityIdLIVE(entityId, callbackGetDetailEntity);
            } else {
                if (callbackGetDetailEntity != null) {
                    Data d = resultRetrieveAnEntity.getData();
                    callbackGetDetailEntity.onSuccess(d);
                }
            }
        }, throwable -> {
            if (callbackGetDetailEntity != null) {
                callbackGetDetailEntity.onError(throwable);
            }
        });
    }

    public static void getDataFromEntityIdLIVE(String entityId, final CallbackGetDetailEntity callbackGetDetailEntity) {
        UZService service = UZRestClient.createService(UZService.class);
        UZAPIMaster.getInstance().subscribe(service.retrieveALiveEvent(entityId), resultRetrieveALive -> {
            if (resultRetrieveALive == null
                    || resultRetrieveALive.getData() == null
                    || resultRetrieveALive.getData().getId() == null
                    || resultRetrieveALive.getData().getId().isEmpty()) {
                if (callbackGetDetailEntity != null) {
                    callbackGetDetailEntity.onError(new Exception(UZException.ERR_21));
                }
            } else {
                if (callbackGetDetailEntity != null) {
                    Data d = resultRetrieveALive.getData();
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
