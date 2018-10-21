package vn.uiza.utils;

import vn.uiza.restapi.uiza.model.v3.metadata.getdetailofmetadata.Data;

public interface CallbackGetDetailEntity {
    public void onSuccess(Data data);

    public void onError(Throwable e);
}