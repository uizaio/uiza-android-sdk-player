package io.uiza.core.api;

import android.content.Context;
import io.uiza.core.api.client.UzRestClient;
import io.uiza.core.api.response.BaseResponse;
import io.uiza.core.api.response.video.VideoData;
import io.uiza.core.api.util.ApiSubscriber;
import io.uiza.core.exception.UzException;

public class UzUtilBase {

    public static void getDetailEntity(final Context context, final String apiVersion,
            final String entityId, final String appId,
            final CallbackGetDetailEntity callbackGetDetailEntity) {
        UzServiceApi service = UzRestClient.createService(UzServiceApi.class);
        UzApiMaster.getInstance().subscribe(service.retrieveAnEntity(apiVersion, entityId, appId),
                new ApiSubscriber<BaseResponse<VideoData>>() {
                    @Override
                    public void onSuccess(BaseResponse<VideoData> response) {
                        if (response == null || response.getData() == null
                                || response.getData().getId() == null || response.getData().getId()
                                .isEmpty()) {
                            getDataFromEntityIdLive(context, apiVersion, appId, entityId,
                                    callbackGetDetailEntity);
                        } else {
                            if (callbackGetDetailEntity != null) {
                                VideoData d = response.getData();
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

    public static void getDataFromEntityIdLive(final Context context, final String apiVersion,
            String appId, String entityId, final CallbackGetDetailEntity callbackGetDetailEntity) {
        UzServiceApi service = UzRestClient.createService(UzServiceApi.class);
        UzApiMaster.getInstance().subscribe(service.retrieveALiveEvent(apiVersion, entityId, appId),
                new ApiSubscriber<BaseResponse<VideoData>>() {
                    @Override
                    public void onSuccess(BaseResponse<VideoData> response) {
                        if (response == null || response.getData() == null
                                || response.getData().getId() == null || response.getData().getId()
                                .isEmpty()) {
                            if (callbackGetDetailEntity != null) {
                                callbackGetDetailEntity.onError(new Exception(UzException.ERR_21));
                            }
                        } else {
                            if (callbackGetDetailEntity != null) {
                                VideoData d = response.getData();
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
