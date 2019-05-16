package vn.uiza.utils;

import vn.uiza.restapi.uiza.model.v3.metadata.getdetailofmetadata.Data;

public interface CallbackGetDetailEntity {
    void onSuccess(Data data);

    void onError(Throwable e);
}