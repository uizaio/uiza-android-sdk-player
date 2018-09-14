package vn.uiza.restapi.uiza;

import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;
import rx.Observable;
import vn.uiza.restapi.uiza.model.v3.UizaWorkspaceInfo;
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
import vn.uiza.restapi.uiza.model.v3.usermanagement.createanuser.CreateUser;
import vn.uiza.restapi.uiza.model.v3.usermanagement.updatepassword.UpdatePassword;
import vn.uiza.restapi.uiza.model.v3.videoondeman.listallentity.ResultListEntity;
import vn.uiza.restapi.uiza.model.v3.videoondeman.retrieveanentity.ResultRetrieveAnEntity;

/**
 * @author loitp
 */

public interface UZService {
    //http://dev-docs.uizadev.io/#get-token
    @POST("/api/public/v3/admin/user/auth")
    Observable<ResultGetToken> getToken(@Body UizaWorkspaceInfo uizaWorkspaceInfo);

    //http://dev-docs.uizadev.io/#check-token
    //@Headers("Content-Type: application/json")
    @POST("/api/public/v3/admin/user/auth/check-token")
    Observable<Object> checkToken();

    //https://docs.uiza.io/#create-an-user
    @POST("/api/public/v3/admin/user")
    Observable<Object> createAnUser(@Body CreateUser createUser);

    //https://docs.uiza.io/#retrieve-an-user
    @GET("/api/public/v3/admin/user")
    Observable<Object> retrieveAnUser(@Query("id") String id);

    //https://docs.uiza.io/#list-all-users
    @GET("/api/public/v3/admin/user")
    Observable<Object> listAllUser();

    //https://docs.uiza.io/#update-an-user
    @PUT("/api/public/v3/admin/user")
    Observable<Object> updateAnUser(@Body CreateUser updateUser);

    //https://docs.uiza.io/#update-an-user
    @HTTP(method = "DELETE", path = "/api/public/v3/admin/user", hasBody = true)
    //@DELETE("/api/public/v3/admin/user")
    Observable<Object> deleteAnUser(@Body CreateUser deleteUser);

    //https://docs.uiza.io/#update-password
    @PUT("/api/public/v3/admin/user/changepassword")
    Observable<Object> updatePassword(@Body UpdatePassword updatePassword);

    //http://dev-docs.uizadev.io/#get-list-metadata
    @GET("/api/public/v3/media/metadata")
    Observable<ResultGetListMetadata> getListMetadata();

    //http://dev-docs.uizadev.io/#get-list-metadata
    @GET("/api/public/v3/media/metadata")
    Observable<ResultGetListMetadata> getListMetadata(@Query("limit") int limit, @Query("page") int page);

    //http://dev-docs.uizadev.io/#create-metadata
    @POST("/api/public/v3/media/metadata")
    Observable<ResultCreateMetadata> createMetadata(@Body CreateMetadata createMetadata);

    //http://dev-docs.uizadev.io/#get-detail-of-metadata
    @GET("/api/public/v3/media/metadata")
    Observable<ResultGetDetailOfMetadata> getDetailOfMetadata(@Query("id") String id);

    //http://dev-docs.uizadev.io/#update-metadata
    @PUT("/api/public/v3/media/metadata")
    Observable<ResultUpdateMetadata> updateMetadata(@Body CreateMetadata createMetadata);

    //http://dev-docs.uizadev.io/#delete-an-metadata
    @DELETE("/api/public/v3/media/metadata")
    Observable<ResultDeleteAnMetadata> deleteAnMetadata(@Query("id") String id);

    //http://dev-docs.uizadev.io/#list-all-entity
    @GET("/api/public/v3/media/entity")
    Observable<ResultListEntity> getListAllEntity();

    //http://dev-docs.uizadev.io/#list-all-entity
    @GET("/api/public/v3/media/entity")
    Observable<ResultListEntity> getListAllEntity(@Query("metadataId") String metadataid,
                                                  @Query("limit") int limit,
                                                  @Query("page") int page,
                                                  @Query("orderBy") String orderBy,
                                                  @Query("orderType") String orderType,
                                                  @Query("publishToCdn") String publishToCdn);

    //http://dev-docs.uizadev.io/#list-all-entity
    @GET("/api/public/v3/media/entity")
    Observable<ResultListEntity> getListAllEntity(@Query("metadataId") String metadataid,
                                                  @Query("limit") int limit,
                                                  @Query("page") int page);

    //http://dev-docs.uizadev.io/#list-all-entity
    @GET("/api/public/v3/media/entity")
    Observable<ResultListEntity> getListAllEntity(@Query("metadataId") String metadataid);

    //http://dev-docs.uizadev.io/#retrieve-an-entity
    @GET("/api/public/v3/media/entity")
    Observable<ResultRetrieveAnEntity> retrieveAnEntity(@Query("id") String id);

    //http://dev-docs.uizadev.io/#search-entity
    @GET("/api/public/v3/media/entity/search")
    Observable<ResultListEntity> searchEntity(@Query("keyword") String keyword);

    @POST("/api/public/v3/media/entity/playback/token")
    Observable<ResultGetTokenStreaming> getTokenStreaming(@Body SendGetTokenStreaming sendGetTokenStreaming);

    @GET("/api/private/v1/cdn/linkplay")
    Observable<ResultGetLinkPlay> getLinkPlay(@Query("app_id") String appId,
                                              @Query("entity_id") String entityId,
                                              @Query("type_content") String typeContent);

    @GET("/api/private/v1/cdn/live/linkplay")
    Observable<ResultGetLinkPlay> getLinkPlayLive(@Query("app_id") String appId,
                                                  @Query("stream_name") String streamName);

    @GET("/api/public/v3/live/entity")
    Observable<ResultRetrieveALiveEvent> retrieveALiveEvent(@Query("limit") int limit,
                                                            @Query("page") int page,
                                                            @Query("orderBy") String orderBy,
                                                            @Query("orderType") String orderType);

    @GET("/api/public/v3/live/entity")
    Observable<ResultRetrieveALive> retrieveALiveEvent(@Query("id") String entityId);

    @POST("/api/public/v3/live/entity/feed")
    Observable<Object> startALiveEvent(@Body BodyStartALiveFeed bodyStartALiveFeed);

    @GET("/api/public/v3/live/entity/tracking/current-view")
    Observable<ResultGetViewALiveFeed> getViewALiveFeed(@Query("id") String id);

    @GET("/api/private/v3/live/entity/tracking/")
    Observable<ResultTimeStartLive> getTimeStartLive(@Query("entityId") String entityId, @Query("feedId") String feedId);
}
