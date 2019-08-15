package io.uiza.core.api;

import io.uiza.core.api.response.video.VideoData;

public interface CallbackGetDetailEntity {
    void onSuccess(VideoData data);

    void onError(Throwable e);
}