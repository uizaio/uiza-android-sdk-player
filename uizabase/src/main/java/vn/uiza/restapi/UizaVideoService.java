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
import vn.uiza.models.Subtitle;
import vn.uiza.models.ads.Ad;
import vn.uiza.models.playerinfo.PlayerInfor;
import vn.uiza.models.vod.AWSUploadKey;
import vn.uiza.models.vod.CreateVODBody;
import vn.uiza.models.vod.DeleteVODResponse;
import vn.uiza.models.vod.PublishVODResponse;
import vn.uiza.models.vod.PublishVODStatusResponse;
import vn.uiza.models.vod.UpdateVODBody;
import vn.uiza.models.vod.UpdateVODResponse;
import vn.uiza.models.vod.VODEntity;

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

    // BACKUP from V4
    @GET("/v1/player/info/config")
    Observable<PlayerInfor> getPlayerInfo(@Query("id") String id);

    @GET("/v1/media/entity/cue-point")
    Observable<ListWrap<Ad>> getCuePoint(@Query("entityId") String entityId);

    @GET("/v1/media/subtitle")
    Observable<ListWrap<Subtitle>> getSubtitles(@Query("entityId") String entityId);
}
