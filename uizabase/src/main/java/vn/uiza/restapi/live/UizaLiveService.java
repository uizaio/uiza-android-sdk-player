package vn.uiza.restapi.live;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import vn.uiza.restapi.uiza.model.ListWrap;
import vn.uiza.restapi.uiza.model.v5.CreateLiveEntityBody;
import vn.uiza.restapi.uiza.model.v5.DeleteLiveEntityResponse;
import vn.uiza.restapi.uiza.model.v5.LiveEntity;
import vn.uiza.restapi.uiza.model.v5.LiveSession;

public interface UizaLiveService {

    /**
     * Get an entity
     */
    @GET("/api/v5/live/entities/{id}")
    Observable<LiveEntity> getEntity(@Path("id") String id);

    /**
     * Get list of entities
     */
    @GET("/api/v5/live/entities")
    Observable<ListWrap<LiveEntity>> getEntities();

    /**
     * Create an entity
     */
    @POST("/api/v5/live/entities")
    Observable<LiveEntity> createEntity(@Body CreateLiveEntityBody createEntity);

    /**
     * Update an entity
     */
    @PUT("/api/v5/live/entities/{id}")
    Observable<LiveEntity> updateEntity(@Path("id") String id, @Query("name") String name);

    /**
     * Delete an entity
     */
    @DELETE("/api/v5/live/entities/{id}")
    Observable<DeleteLiveEntityResponse> deleteEntity(@Path("id") String id);

    /**
     * Reset stream key of an entity
     */
    @PUT("/api/v5/live/entities/{id}/stream-key")
    Observable<LiveEntity> resetStreamKey(@Path("id") String id);

    /**
     * Get an session
     */
    @GET("/api/v5/live/entities/{entity_id}/sessions/{id}")
    Observable<LiveSession> getSession(@Path("entity_id") String entityId, @Path("id") String id);

    /**
     * Get list of sessions
     */
    @GET("/api/v5/live/entities/{entity_id}/sessions")
    Observable<ListWrap<LiveSession>> getSessions(@Path("entity_id") String entityId);
}
