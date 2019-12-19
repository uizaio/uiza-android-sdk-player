package vn.uiza.restapi.linkplay;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import vn.uiza.restapi.model.v3.linkplay.getlinkplay.ResultGetLinkPlay;

public interface UizaLinkPlayService {


    @GET("/api/public/v1/cdn/linkplay")
    Observable<ResultGetLinkPlay> getLinkPlay(@Query("app_id") String appId,
                                              @Query("entity_id") String entityId,
                                              @Query("type_content") String typeContent);

    @GET("/api/public/v1/cdn/live/linkplay")
    Observable<ResultGetLinkPlay> getLinkPlayLive(@Query("app_id") String appId,
                                                  @Query("stream_name") String streamName);

}
