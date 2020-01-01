package vn.uiza.restapi;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import vn.uiza.restapi.model.ListWrap;
import vn.uiza.restapi.model.v5.vod.AWSUploadKey;
import vn.uiza.restapi.model.v5.vod.CreateVODBody;
import vn.uiza.restapi.model.v5.vod.DeleteVODResponse;
import vn.uiza.restapi.model.v5.vod.PublishVODResponse;
import vn.uiza.restapi.model.v5.vod.PublishVODStatusResponse;
import vn.uiza.restapi.model.v5.vod.UpdateVODBody;
import vn.uiza.restapi.model.v5.vod.UpdateVODResponse;
import vn.uiza.restapi.model.v5.vod.VODEntity;

/**
 * Connect Uiza VOD Service API
 */
public interface UizaVideoService {

    /**
     * Get an vod entity
     */
    @GET("/v1/video_entities/{id}")
    Observable<VODEntity> getEntity(@Path("id") String id);


    /**
     * Get list of vod entity
     */
    @GET("/v1/video_entities")
    Observable<ListWrap<VODEntity>> getEntities();


    /**
     * Create a vod entity
     */
    @POST("/v1/video_entities")
    Observable<VODEntity> createEntity(@Body CreateVODBody createVODBody);


    /**
     * update a vod entity
     */
    @PUT("/v1/video_entities/{id}")
    Observable<UpdateVODResponse> updateEntity(@Path("id") String id, @Body UpdateVODBody updateVODBody);


    /**
     * delete a vod entity
     */
    @DELETE("/v1/video_entities/{id}")
    Observable<DeleteVODResponse> deleteEntity(@Path("id") String id);

    /**
     * puslish a vod
     */
    @POST("/v1/video_entities/{id}/publish")
    Observable<PublishVODResponse> publishVOD(@Path("id") String id);

    /**
     * puslish a vod
     */
    @GET("/v1/video_entities/{id}/publish/status")
    Observable<PublishVODStatusResponse> getPublishVODStatus(@Path("id") String id);


    @GET("/v1/video_entities/{id}/config/aws")
    Observable<AWSUploadKey> getAWSUploadKey(@Path("id") String id);
}
