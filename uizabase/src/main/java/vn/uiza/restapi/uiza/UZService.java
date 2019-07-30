package vn.uiza.restapi.uiza;

import java.util.List;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;
import vn.uiza.restapi.uiza.model.UTCTime;
import vn.uiza.restapi.uiza.model.tracking.UizaTracking;
import vn.uiza.restapi.uiza.model.tracking.UizaTrackingCCU;
import vn.uiza.restapi.uiza.model.tracking.muiza.Muiza;
import vn.uiza.restapi.uiza.model.v3.UizaWorkspaceInfo;
import vn.uiza.restapi.uiza.model.v3.ad.AdWrapper;
import vn.uiza.restapi.uiza.model.v3.authentication.gettoken.ResultGetToken;
import vn.uiza.restapi.uiza.model.v3.linkplay.getlinkplay.ResultGetLinkPlay;
import vn.uiza.restapi.uiza.model.v3.linkplay.gettokenstreaming.ResultGetTokenStreaming;
import vn.uiza.restapi.uiza.model.v3.linkplay.gettokenstreaming.SendGetTokenStreaming;
import vn.uiza.restapi.uiza.model.v3.livestreaming.gettimestartlive.ResultTimeStartLive;
import vn.uiza.restapi.uiza.model.v3.livestreaming.getviewalivefeed.ResultGetViewALiveFeed;
import vn.uiza.restapi.uiza.model.v3.livestreaming.retrievealive.ResultRetrieveALive;
import vn.uiza.restapi.uiza.model.v3.livestreaming.retrievealiveevent.ResultRetrieveALiveEvent;
import vn.uiza.restapi.uiza.model.v3.livestreaming.startALiveFeed.BodyStartALiveFeed;
import vn.uiza.restapi.uiza.model.v3.metadata.createmetadata.CreateMetadata;
import vn.uiza.restapi.uiza.model.v3.metadata.createmetadata.ResultCreateMetadata;
import vn.uiza.restapi.uiza.model.v3.metadata.deleteanmetadata.ResultDeleteAnMetadata;
import vn.uiza.restapi.uiza.model.v3.metadata.getdetailofmetadata.ResultGetDetailOfMetadata;
import vn.uiza.restapi.uiza.model.v3.metadata.getlistmetadata.ResultGetListMetadata;
import vn.uiza.restapi.uiza.model.v3.metadata.updatemetadata.ResultUpdateMetadata;
import vn.uiza.restapi.uiza.model.v3.skin.listskin.ResultGetListSkin;
import vn.uiza.restapi.uiza.model.v3.usermanagement.createanuser.CreateUser;
import vn.uiza.restapi.uiza.model.v3.usermanagement.updatepassword.UpdatePassword;
import vn.uiza.restapi.uiza.model.v3.videoondeman.listallentity.ResultListEntity;
import vn.uiza.restapi.uiza.model.v3.videoondeman.retrieveanentity.ResultRetrieveAnEntity;
import vn.uiza.restapi.uiza.model.v4.playerinfo.PlayerInfor;
import vn.uiza.restapi.uiza.model.v4.subtitle.ResultGetSubtitles;

/**
 * @author loitp
 */

public interface UZService {
    //http://dev-docs.uizadev.io/#get-token
    @POST("/api/public/{api_version}/admin/user/auth")
    Observable<ResultGetToken> getToken(@Path(value = "api_version", encoded = true) String apiVersion, @Body UizaWorkspaceInfo uizaWorkspaceInfo);

    //http://dev-docs.uizadev.io/#check-token
    //@Headers("Content-Type: application/json")
    @POST("/api/public/{api_version}/admin/user/auth/check-token")
    Observable<Object> checkToken(@Path(value = "api_version", encoded = true) String apiVersion);

    //https://docs.uiza.io/#create-an-user
    @POST("/api/public/{api_version}/admin/user")
    Observable<Object> createAnUser(@Path(value = "api_version", encoded = true) String apiVersion,@Body CreateUser createUser);

    //https://docs.uiza.io/#retrieve-an-user
    @GET("/api/public/{api_version}/admin/user")
    Observable<Object> retrieveAnUser(@Path(value = "api_version", encoded = true) String apiVersion,@Query("id") String id);

    //https://docs.uiza.io/#list-all-users
    @GET("/api/public/{api_version}/admin/user")
    Observable<Object> listAllUser(@Path(value = "api_version", encoded = true) String apiVersion);

    //https://docs.uiza.io/#update-an-user
    @PUT("/api/public/{api_version}/admin/user")
    Observable<Object> updateAnUser(@Path(value = "api_version", encoded = true) String apiVersion,@Body CreateUser updateUser);

    //https://docs.uiza.io/#update-an-user
    @HTTP(method = "DELETE", path = "/api/public/v4/admin/user", hasBody = true)
    //@DELETE("/api/public/v4/admin/user")
    Observable<Object> deleteAnUser(@Body CreateUser deleteUser);

    //https://docs.uiza.io/#update-password
    @PUT("/api/public/{api_version}/admin/user/changepassword")
    Observable<Object> updatePassword(@Path(value = "api_version", encoded = true) String apiVersion,@Body UpdatePassword updatePassword);

    //http://dev-docs.uizadev.io/#get-list-metadata
    @GET("/api/public/{api_version}/media/metadata")
    Observable<ResultGetListMetadata> getListMetadata(@Path(value = "api_version", encoded = true) String apiVersion);

    //http://dev-docs.uizadev.io/#get-list-metadata
    @GET("/api/public/{api_version}/media/metadata")
    Observable<ResultGetListMetadata> getListMetadata(@Path(value = "api_version", encoded = true) String apiVersion,@Query("limit") int limit, @Query("page") int page);

    //http://dev-docs.uizadev.io/#create-metadata
    @POST("/api/public/{api_version}/media/metadata")
    Observable<ResultCreateMetadata> createMetadata(@Path(value = "api_version", encoded = true) String apiVersion,@Body CreateMetadata createMetadata);

    //http://dev-docs.uizadev.io/#get-detail-of-metadata
    @GET("/api/public/{api_version}/media/metadata")
    Observable<ResultGetDetailOfMetadata> getDetailOfMetadata(@Path(value = "api_version", encoded = true) String apiVersion,@Query("id") String id);

    //http://dev-docs.uizadev.io/#update-metadata
    @PUT("/api/public/{api_version}/media/metadata")
    Observable<ResultUpdateMetadata> updateMetadata(@Path(value = "api_version", encoded = true) String apiVersion,@Body CreateMetadata createMetadata);

    //http://dev-docs.uizadev.io/#delete-an-metadata
    @DELETE("/api/public/{api_version}/media/metadata")
    Observable<ResultDeleteAnMetadata> deleteAnMetadata(@Path(value = "api_version", encoded = true) String apiVersion,@Query("id") String id);

    //http://dev-docs.uizadev.io/#list-all-entity
    @GET("/api/public/{api_version}/media/entity")
    Observable<ResultListEntity> getListAllEntity(@Path(value = "api_version", encoded = true) String apiVersion);

    //http://dev-docs.uizadev.io/#list-all-entity
    @GET("/api/public/{api_version}/media/entity")
    Observable<ResultListEntity> getListAllEntity(@Path(value = "api_version", encoded = true) String apiVersion,
                                                  @Query("metadataId") String metadataid,
                                                  @Query("limit") int limit,
                                                  @Query("page") int page,
                                                  @Query("orderBy") String orderBy,
                                                  @Query("orderType") String orderType,
                                                  @Query("publishToCdn") String publishToCdn,
                                                  @Query("appId") String appId);

    //http://dev-docs.uizadev.io/#list-all-entity
    @GET("/api/public/{api_version}/media/entity")
    Observable<ResultListEntity> getListAllEntity(@Path(value = "api_version", encoded = true) String apiVersion,
                                                  @Query("metadataId") String metadataid,
                                                  @Query("limit") int limit,
                                                  @Query("page") int page);

    //http://dev-docs.uizadev.io/#list-all-entity
    @GET("/api/public/{api_version}/media/entity")
    Observable<ResultListEntity> getListAllEntity(@Path(value = "api_version", encoded = true) String apiVersion,
                                                  @Query("metadataId") String metadataid);

    //http://dev-docs.uizadev.io/#retrieve-an-entity
    @GET("/api/public/{api_version}/media/entity")
    Observable<ResultRetrieveAnEntity> retrieveAnEntity(@Path(value = "api_version", encoded = true) String apiVersion,
                                                        @Query("id") String id, @Query("appId") String appId);
    @GET("/api/public/{api_version}/media/subtitle")
    Observable<ResultGetSubtitles> getSubtitles(@Path(value = "api_version", encoded = true) String apiVersion,
            @Query("entityId") String entityId, @Query("appId") String appId);

    //http://dev-docs.uizadev.io/#search-entity
    @GET("/api/public/{api_version}/media/entity/search")
    Observable<ResultListEntity> searchEntity(@Path(value = "api_version", encoded = true) String apiVersion,
                                              @Query("keyword") String keyword);

    @POST("/api/public/{api_version}/media/entity/playback/token")
    Observable<ResultGetTokenStreaming> getTokenStreaming(@Path(value = "api_version", encoded = true) String apiVersion,
                                                          @Body SendGetTokenStreaming sendGetTokenStreaming);

    @GET("/api/public/v1/cdn/linkplay")
    Observable<ResultGetLinkPlay> getLinkPlay(@Query("app_id") String appId,
                                              @Query("entity_id") String entityId,
                                              @Query("type_content") String typeContent);

    @GET("/api/public/v1/cdn/live/linkplay")
    Observable<ResultGetLinkPlay> getLinkPlayLive(@Query("app_id") String appId,
                                                  @Query("stream_name") String streamName);

    @GET("/api/public/{api_version}/live/entity")
    Observable<ResultRetrieveALiveEvent> retrieveALiveEvent(@Path(value = "api_version", encoded = true) String apiVersion,
                                                            @Query("limit") int limit,
                                                            @Query("page") int page,
                                                            @Query("orderBy") String orderBy,
                                                            @Query("orderType") String orderType,
                                                            @Query("appId") String appId);

    @GET("/api/public/{api_version}/live/entity")
    Observable<ResultRetrieveALive> retrieveALiveEvent(@Path(value = "api_version", encoded = true) String apiVersion,
                                                       @Query("id") String entityId, @Query("appId") String appId);

    @POST("/api/public/{api_version}/live/entity/feed")
    Observable<Object> startALiveEvent(@Path(value = "api_version", encoded = true) String apiVersion,
                                       @Body BodyStartALiveFeed bodyStartALiveFeed);

    @GET("/api/public/{api_version}/live/entity/tracking/current-view")
    Observable<ResultGetViewALiveFeed> getViewALiveFeed(@Path(value = "api_version", encoded = true) String apiVersion,
                                                        @Query("id") String id, @Query("appId") String appId);

    @GET("/api/public/{api_version}/live/entity/tracking/")
    Observable<ResultTimeStartLive> getTimeStartLive(@Path(value = "api_version", encoded = true) String apiVersion,
                                                     @Query("entityId") String entityId, @Query("feedId") String feedId, @Query("appId") String appId);

    @GET("/api/public/{api_version}/player/info/")
    Observable<ResultGetListSkin> getListSkin(@Path(value = "api_version", encoded = true) String apiVersion,
                                              @Query("platform") String platform);

    @GET("/api/public/{api_version}/player/info/config/")
    Observable<Object> getSkinConfig(@Path(value = "api_version", encoded = true) String apiVersion,
                                     @Query("id") String id);

    @GET("/api/public/{api_version}/media/entity/cue-point")
    Observable<AdWrapper> getCuePoint(@Path(value = "api_version", encoded = true) String apiVersion,
                                      @Query("entityId") String entityId, @Query("appId") String appId);

    @GET("/api/public/{api_version}/player/info/config")
    Observable<PlayerInfor> getPlayerInfo(@Path(value = "api_version", encoded = true) String apiVersion,
                                          @Query("id") String id, @Query("appId") String appId);//id: player id

    //=====================================================tracking
    //@Headers("Content-Type: application/json")
    @POST("/analytic-tracking/v1/tracking/mobile")
    Observable<Object> track(@Body UizaTracking uizaTracking);

    //@Headers("Content-Type: application/json")
    @POST("/analytic-tracking/v1/ccu/mobile")
    Observable<Object> trackCCU(@Body UizaTrackingCCU uizaTrackingCCU);

    //@Headers("Content-Type: application/json")
    @POST("/analytic-tracking/v2/muiza/eventbulk/mobile")
    Observable<Object> trackMuiza(@Body List<Muiza> muizaList);
    //end =====================================================tracking

    //=====================================================heartbeat
    @GET("/v1/cdn/ccu/ping")
    Observable<Object> pingHeartBeat(@Query("cdn_name") String cdnName, @Query("session") String session);
    //end =====================================================heartbeat

    // This free API does not need the base URL
    @GET("http://worldtimeapi.org/api/timezone/Etc/UTC")
    Observable<UTCTime> getCurrentUTCTime();
}
