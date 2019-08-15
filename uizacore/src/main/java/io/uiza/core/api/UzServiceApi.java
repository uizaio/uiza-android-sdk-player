package io.uiza.core.api;

import io.uiza.core.api.request.streaming.StartALiveFeedRequest;
import io.uiza.core.api.request.streaming.StreamingTokenRequest;
import io.uiza.core.api.request.tracking.UizaTracking;
import io.uiza.core.api.request.tracking.UizaTrackingCCU;
import io.uiza.core.api.request.tracking.muiza.Muiza;
import io.uiza.core.api.response.BasePaginationResponse;
import io.uiza.core.api.response.BaseResponse;
import io.uiza.core.api.response.UtcTime;
import io.uiza.core.api.response.ad.Ad;
import io.uiza.core.api.response.linkplay.LinkPlay;
import io.uiza.core.api.response.playerinfo.PlayerInfo;
import io.uiza.core.api.response.skin.Skin;
import io.uiza.core.api.response.streaming.LiveFeedViews;
import io.uiza.core.api.response.streaming.StreamingToken;
import io.uiza.core.api.response.subtitle.Subtitle;
import io.uiza.core.api.response.video.VideoData;
import java.util.List;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface UzServiceApi {

    //http://dev-docs.uizadev.io/#get-list-metadata
    @GET("/api/public/{api_version}/media/metadata")
    Observable<BasePaginationResponse<List<VideoData>>> getListMetadata(
            @Path(value = "api_version", encoded = true) String apiVersion,
            @Query("limit") int limit, @Query("page") int page);

    //http://dev-docs.uizadev.io/#list-all-entity
    @GET("/api/public/{api_version}/media/entity")
    Observable<BasePaginationResponse<List<VideoData>>> getListAllEntity(
            @Path(value = "api_version", encoded = true) String apiVersion,
            @Query("metadataId") String metadataId,
            @Query("limit") int limit,
            @Query("page") int page,
            @Query("orderBy") String orderBy,
            @Query("orderType") String orderType,
            @Query("publishToCdn") String publishToCdn,
            @Query("appId") String appId);

    //http://dev-docs.uizadev.io/#retrieve-an-entity
    @GET("/api/public/{api_version}/media/entity")
    Observable<BaseResponse<VideoData>> retrieveAnEntity(
            @Path(value = "api_version", encoded = true) String apiVersion,
            @Query("id") String id, @Query("appId") String appId);

    @GET("/api/public/{api_version}/media/subtitle")
    Observable<BaseResponse<List<Subtitle>>> getSubtitles(
            @Path(value = "api_version", encoded = true) String apiVersion,
            @Query("entityId") String entityId, @Query("appId") String appId);

    //http://dev-docs.uizadev.io/#search-entity
    @GET("/api/public/{api_version}/media/entity/search")
    Observable<BasePaginationResponse<List<VideoData>>> searchEntity(
            @Path(value = "api_version", encoded = true) String apiVersion,
            @Query("keyword") String keyword);

    @POST("/api/public/{api_version}/media/entity/playback/token")
    Observable<BaseResponse<StreamingToken>> getTokenStreaming(
            @Path(value = "api_version", encoded = true) String apiVersion,
            @Body StreamingTokenRequest tokenRequest);

    @GET("/api/public/v1/cdn/linkplay")
    Observable<BaseResponse<LinkPlay>> getLinkPlay(@Query("app_id") String appId,
            @Query("entity_id") String entityId,
            @Query("type_content") String typeContent);

    @GET("/api/public/v1/cdn/live/linkplay")
    Observable<BaseResponse<LinkPlay>> getLinkPlayLive(@Query("app_id") String appId,
            @Query("stream_name") String streamName);

    @GET("/api/public/{api_version}/live/entity")
    Observable<BasePaginationResponse<List<VideoData>>> retrieveALiveEvent(
            @Path(value = "api_version", encoded = true) String apiVersion,
            @Query("limit") int limit,
            @Query("page") int page,
            @Query("orderBy") String orderBy,
            @Query("orderType") String orderType,
            @Query("appId") String appId);

    @GET("/api/public/{api_version}/live/entity")
    Observable<BaseResponse<VideoData>> retrieveALiveEvent(
            @Path(value = "api_version", encoded = true) String apiVersion,
            @Query("id") String entityId, @Query("appId") String appId);

    @POST("/api/public/{api_version}/live/entity/feed")
    Observable<Object> startALiveEvent(
            @Path(value = "api_version", encoded = true) String apiVersion,
            @Body StartALiveFeedRequest request);


    @GET("/api/public/{api_version}/live/entity/tracking/current-view")
    Observable<BaseResponse<LiveFeedViews>> getViewALiveFeed(
            @Path(value = "api_version", encoded = true) String apiVersion,
            @Query("id") String id, @Query("appId") String appId);

    @GET("/api/public/{api_version}/live/entity/tracking/")
    Observable<BaseResponse<VideoData>> getTimeStartLive(
            @Path(value = "api_version", encoded = true) String apiVersion,
            @Query("entityId") String entityId, @Query("feedId") String feedId,
            @Query("appId") String appId);

    @GET("/api/public/{api_version}/player/info/")
    Observable<BasePaginationResponse<List<Skin>>> getListSkin(
            @Path(value = "api_version", encoded = true) String apiVersion,
            @Query("platform") String platform);

    @GET("/api/public/{api_version}/player/info/config/")
    Observable<Object> getSkinConfig(@Path(value = "api_version", encoded = true) String apiVersion,
            @Query("id") String id);

    @GET("/api/public/{api_version}/media/entity/cue-point")
    Observable<BasePaginationResponse<List<Ad>>> getCuePoint(
            @Path(value = "api_version", encoded = true) String apiVersion,
            @Query("entityId") String entityId, @Query("appId") String appId);

    @GET("/api/public/{api_version}/player/info/config")
    Observable<BaseResponse<PlayerInfo>> getPlayerInfo(
            @Path(value = "api_version", encoded = true) String apiVersion,
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
    Observable<Object> pingHeartBeat(@Query("cdn_name") String cdnName,
            @Query("session") String session);
    //end =====================================================heartbeat

    // This free API does not need the base URL
    @GET("http://worldtimeapi.org/api/timezone/Etc/UTC")
    Observable<UtcTime> getCurrentUTCTime();
}
