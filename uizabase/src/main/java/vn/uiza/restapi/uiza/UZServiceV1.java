package vn.uiza.restapi.uiza;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;
import vn.uiza.restapi.uiza.model.tracking.UizaTracking;
import vn.uiza.restapi.uiza.model.v2.auth.Auth;
import vn.uiza.restapi.uiza.model.v2.auth.JsonBodyAuth;
import vn.uiza.restapi.uiza.model.v2.getdetailentity.GetDetailEntity;
import vn.uiza.restapi.uiza.model.v2.getdetailentity.JsonBodyGetDetailEntity;
import vn.uiza.restapi.uiza.model.v2.getlinkdownload.GetLinkDownload;
import vn.uiza.restapi.uiza.model.v2.getlinkdownload.JsonBodyGetLinkDownload;
import vn.uiza.restapi.uiza.model.v2.getplayerinfo.PlayerConfig;
import vn.uiza.restapi.uiza.model.v2.listallentity.JsonBodyListAllEntity;
import vn.uiza.restapi.uiza.model.v2.listallentity.ListAllEntity;
import vn.uiza.restapi.uiza.model.v2.listallentityrelation.JsonBodyListAllEntityRelation;
import vn.uiza.restapi.uiza.model.v2.listallentityrelation.ListAllEntityRelation;
import vn.uiza.restapi.uiza.model.v2.listallmetadata.JsonBodyMetadataList;
import vn.uiza.restapi.uiza.model.v2.search.JsonBodySearch;
import vn.uiza.restapi.uiza.model.v2.search.Search;

/**
 * @author loitp
 */

public interface UZServiceV1 {
    //=====================================================Sample
    /*@GET("v1/app/poster")
    Observable<GetPoster[]> getPoster(@Query("number") int number);

    @GET("api/public/v1/media/entity/get-link-play")
    Observable<GetLinkDownload> getLinkPlayV1(@Query("entityId") String entityId);

    @FormUrlEncoded
    @PUT("v1/room/follow")
    Observable<Object> followIdol(@Field("roomId") String roomId);

    @FormUrlEncoded
    @POST("/public/v1/auth/login")
    Observable<Object> login(@Field("username") String username, @Field("password") String password);

    @Headers("Content-Type: application/vnd.api+json")
    @GET("/api/data/v1/metadata/list")
    Observable<Object> getListAllMetadata();

    @GET("/api/data/v1/entity/list")
    Observable<GetAll> getAll(@Query("limit") int limit, @Query("page") int page);

    @GET("/api/data/v1/entity/list")
    Observable<Object> testGetAll();*/
    //=====================================================End Sample


    //=====================================================v2 dev-api.uiza.io/resource/index.html

    //@Headers("Content-Type: application/json")
    @POST("/api/resource/v1/auth/credentical")
    //Observable<Auth> auth(@Field("accessKeyId") String accessKeyId, @Field("secretKeyId") String secretKeyId);
    Observable<Auth> auth(@Body JsonBodyAuth jsonBodyAuth);

    //@FormUrlEncoded
    @POST("/api/resource/v1/auth/check-token")
    Observable<Auth> checkToken();

    //@FormUrlEncoded
    @POST("/api/resource/v1/media/metadata/list")
    //Observable<vn.loitp.restapi.uiza.model.v2.listallmetadata.ListAllMetadata> listAllMetadataV2(@Field("limit") int limit, @Field("orderBy") String orderBy, @Field("orderType") String orderType);
    Observable<vn.uiza.restapi.uiza.model.v2.listallmetadata.ListAllMetadata> listAllMetadataV2(@Body JsonBodyMetadataList jsonBodyMetadataList);

    //@Headers("Content-Type: application/json")
    @POST("/api/resource/v1/media/entity/list")
    Observable<ListAllEntity> listAllEntityV2(@Body JsonBodyListAllEntity jsonBodyListAllEntity);

    //@FormUrlEncoded
    @POST("/api/resource/v1/media/entity/detail")
    //Observable<GetDetailEntity> getDetailEntityV2(@Field("id") String id);
    Observable<GetDetailEntity> getDetailEntityV2(@Body JsonBodyGetDetailEntity jsonBodyGetDetailEntity);

    @GET("/api/resource/v1/media/entity/get-link-play")
    Observable<vn.uiza.restapi.uiza.model.v2.getlinkplay.GetLinkPlay> getLinkPlayV2(@Query("entityId") String entityId, @Query("appId") String appId);

    //@Headers("Content-Type: application/json")
    @POST("/api/resource/v1/media/entity/link-download")
    Observable<GetLinkDownload> getLinkDownloadV2(@Body JsonBodyGetLinkDownload jsonBodyGetLinkDownload);

    //@FormUrlEncoded
    @POST("/api/resource/v1/media/entity/related")
    //Observable<ListAllEntityRelation> getListAllEntityRalationV2(@Field("id") String id);
    Observable<ListAllEntityRelation> getListAllEntityRalationV2(@Body JsonBodyListAllEntityRelation jsonBodyListAllEntityRelation);

    //@FormUrlEncoded
    @POST("/api/resource/v1/media/search")
    //Observable<Search> searchEntityV2(@Field("keyword") String keyword, @Field("limit") int limit, @Field("page") int page);
    Observable<Search> searchEntityV2(@Body JsonBodySearch jsonBodySearch);

    //=====================================================end v2 dev-api.uiza.io/resource/index.html


    //getPlayerConfig
    @GET("/api/public/v1/player/info/{id}")
    Observable<PlayerConfig> getPlayerInfo(@Path("id") String playerId);

    //=====================================================end v1 http://dev-api.uiza.io/data/index.html

    /*//=====================================================tracking
    //@Headers("Content-Type: application/json")
    @POST("/analytic-tracking/v1/tracking/mobile")
    Observable<Object> track(@Body UizaTracking uizaTracking);
    //end =====================================================tracking*/
}
