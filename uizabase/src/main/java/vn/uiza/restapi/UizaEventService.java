package vn.uiza.restapi;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import vn.uiza.models.ListWrap;
import vn.uiza.models.UTCTime;
import vn.uiza.models.event.EventEntity;

/**
 * Connect UizaEvent Service API
 */
public interface UizaEventService {

    /**
     * Get an entity
     */
    @GET("/v1/events/{id}")
    Observable<EventEntity> getEntity(@Path("id") String id);

    /**
     * Get list of entities
     */
    @GET("/v1/events")
    Observable<ListWrap<EventEntity>> getEntities();


    @GET("http://worldtimeapi.org/api/timezone/Etc/UTC")
    Observable<UTCTime> getCurrentUTCTime();
}
