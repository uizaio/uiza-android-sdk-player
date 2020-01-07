package vn.uiza.restapi;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import vn.uiza.models.ListWrap;
import vn.uiza.models.live.CreateLiveBody;
import vn.uiza.models.live.DeleteLiveEntityResponse;
import vn.uiza.models.live.LiveEntity;
import vn.uiza.models.live.LiveSession;
import vn.uiza.models.live.UpdateLiveBody;

/**
 * Connect UizaLive Service API
 */
public interface UizaLiveService {

    /**
     * Get an entity
     */
    @GET("/v1/live_entities/{id}")
    Observable<LiveEntity> getEntity(@Path("id") String id);

    /**
     * Get list of entities
     * Default pageSize = 10
     */
    @GET("/v1/live_entities")
    Observable<ListWrap<LiveEntity>> getEntities();

    /**
     * Get list of entities
     */
    @GET("/v1/live_entities")
    Observable<ListWrap<LiveEntity>> getEntities(@Query("page_token") String pageToken,
                                                 @Query("page_size") int pageSize);

    /**
     * Get list of entities
     */
    @GET("/v1/live_entities")
    Observable<ListWrap<LiveEntity>> getEntities(@Query("page_token") String pageToken,
                                                 @Query("page_size") int pageSize,
                                                 @Query("created_at_lt") long ltTime,
                                                 @Query("created_at_lte") long lteTime,
                                                 @Query("created_at_gt") long gtTime,
                                                 @Query("created_at_gte") long gteTime);

    /**
     * Create an entity
     */
    @POST("/v1/live_entities")
    Observable<LiveEntity> createEntity(@Body CreateLiveBody createBody);

    /**
     * Update an entity
     */
    @PUT("/v1/live_entities/{id}")
    Observable<LiveEntity> updateEntity(@Path("id") String id, @Body UpdateLiveBody updateBody);

    /**
     * Delete an entity
     */
    @DELETE("/v1/live_entities/{id}")
    Observable<DeleteLiveEntityResponse> deleteEntity(@Path("id") String id);

    /**
     * Reset stream key of an entity
     */
    @PUT("/v1/live_entities/{id}/stream_key")
    Observable<LiveEntity> resetStreamKey(@Path("id") String id);

    /**
     * Get an session => move to private api
     */
    @Deprecated
    @GET("/v1/live_entities/{entity_id}/live_sessions/{id}")
    Observable<LiveSession> getSession(@Path("entity_id") String entityId, @Path("id") String id);

    /**
     * Get list of sessions
     * pageSize default 10
     */
    @GET("/v1/live_entities/{entity_id}/live_sessions")
    Observable<ListWrap<LiveSession>> getSessions(@Path("entity_id") String entityId);

    /**
     * Get list of sessions
     */
    @GET("/v1/live_entities/{entity_id}/live_sessions")
    Observable<ListWrap<LiveSession>> getSessions(@Path("entity_id") String entityId,
                                                  @Query("page_size") int pageSize,
                                                  @Query("page_token") String pageToken,
                                                  @Query("order_by") String orderBy);
}
