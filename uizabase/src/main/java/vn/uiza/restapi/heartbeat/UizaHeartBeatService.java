package vn.uiza.restapi.heartbeat;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UizaHeartBeatService {

    @GET("/v1/cdn/ccu/ping")
    Observable<Object> pingHeartBeat(@Query("cdn_name") String cdnName, @Query("session") String session);
}
