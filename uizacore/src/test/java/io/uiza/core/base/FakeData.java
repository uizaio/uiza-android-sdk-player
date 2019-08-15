package io.uiza.core.base;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import io.uiza.core.api.request.tracking.UizaTracking;
import io.uiza.core.api.response.ad.Ad;
import io.uiza.core.api.response.skin.Skin;
import io.uiza.core.api.response.streaming.LiveFeedViews;
import io.uiza.core.api.response.streaming.StreamingToken;
import io.uiza.core.api.response.video.VideoData;
import java.util.ArrayList;
import java.util.List;

public class FakeData {
    public static final String BASE_URL = "/";
    public static final String TOKEN = "uap-7442d4b99eb349b1bb678614e64cf064-1405ee51";
    public static final String API_VERSION = "v4";
    public static final String USER_MANAGEMENT_PATH = "api-responses/user-management/";
    public static final String CATEGORIZATION_PATH = "api-responses/categorization/";
    public static final String VIDEO_PATH = "api-responses/video/";
    public static final String LIVE_STREAMING_PATH = "api-responses/livestreaming/";
    public static final String ANALYTIC_PATH = "api-responses/analytic/";
    public static final String SKIN_PATH = "api-responses/skin/";
    public static final String ERROR_PATH = "api-responses/error/";
    public static final String OTHER_PATH = "api-responses/other/";

    public static CreateUser updateUser() {
        CreateUser updateUser = new CreateUser();
        updateUser.setId("508d46b7-afa7-45e7-8141-fdeafe32a4cc");
        updateUser.setStatus(0);
        updateUser.setUsername("user_test");
        updateUser.setEmail("user_test@uiza.io");
        updateUser.setPassword("123456789");
        updateUser.setDob("2000-12-30");
        updateUser.setFullname("User Test");
        updateUser.setAvatar("https://exemple.com/avatar.jpeg");
        updateUser.setIsAdmin(1);
        updateUser.setGender(0);
        return updateUser;
    }

    public static UizaTracking uizaTracking() {
        UizaTracking tracking = new UizaTracking();
        tracking.setAppId("f785bc511967473fbe6048ee5fb7ea59");
        tracking.setViewerUserId("ffe636d04847bb2f");
        tracking.setUserAgent("UizaSDK-Android");
        tracking.setReferrer("");
        tracking.setDeviceId("ffe636d04847bb2f");
        tracking.setPlayerId("2131492956");
        tracking.setPlayerName("UZSDK");
        tracking.setPlayerVersion("3.1.9");
        tracking.setEntityId("33812ed9-4b02-408d-aab4-e77c12d16bb0");
        tracking.setEntityName("03/29/2019 10:09 - ConGaiBoGia_Trailer.mp4");
        tracking.setEntitySeries("");
        tracking.setEntityProducer("");
        tracking.setEntityContentType("video");
        tracking.setEntityLanguageCode("en-us");
        tracking.setEntityDuration("5");
        tracking.setEntityStreamType("");
        tracking.setEntityEncodingVariant("");
        tracking.setEntityCdn("asia-southeast1-vod.uizacdn.net");
        tracking.setPlayThrough("0");
        tracking.setEventType("view");
        tracking.setTimestamp("2019-07-05T10:42:37.413Z");
        return tracking;
    }

    public static BodyStartALiveFeed bodyStartALiveFeed() {
        BodyStartALiveFeed bodyStartALiveFeed = new BodyStartALiveFeed();
        bodyStartALiveFeed.setId("8b83886e-9cc3-4eab-9258-ebb16c0c73de");
        return bodyStartALiveFeed;
    }

    public static LiveStartTime resultTimeStartLive() {
        LiveStartTime timeStartLive = new LiveStartTime();
        VideoData data = new VideoData();
        data.setEncode(0);
        data.setStatus(0);
        data.setType("SUCCESS");
        data.setView(0);
        timeStartLive.setData(data);
        timeStartLive.setDatetime("2019-07-05T07:46:50.675Z");
        timeStartLive.setPolicy("public");
        timeStartLive.setRequestId("0fd9bb8c-1deb-471e-8d24-f67f44deb0c3");
        timeStartLive.setServiceName("api-v4");
        timeStartLive.setVersion(4L);
        return timeStartLive;
    }

    public static VideoData dataMetadata1() {
        VideoData data = new VideoData();
        data.setId("f932aa79-852a-41f7-9adc-19935034f944");
        data.setName("Playlist sample");
        data.setDescription("Playlist description");
        data.setSlug("playlist-sample");
        data.setType("playlist");
        data.setOrderNumber(1);
        data.setIcon("/example.com/image002.png");
        data.setStatus(1);
        data.setCreatedAt("2018-06-18T04:29:05.000Z");
        data.setUpdatedAt("2018-06-18T04:29:05.000Z");
        return data;
    }

    public static VideoData dataMetadata2() {
        VideoData data = new VideoData();
        data.setId("ab54db88-0c8c-4928-b1be-1e7120ad2c39");
        data.setName("Folder sample");
        data.setDescription("Folder's description");
        data.setSlug("folder-sample");
        data.setType("folder");
        data.setOrderNumber(2);
        data.setIcon("/example.com/icon.png");
        data.setStatus(1);
        data.setCreatedAt("2018-06-18T03:17:07.000Z");
        data.setUpdatedAt("2018-06-18T03:17:07.000Z");
        return data;
    }

    public static Metadata metadata() {
        Metadata metadata = new Metadata();
        metadata.setTotal(2);
        metadata.setResult(2);
        metadata.setPage(1);
        metadata.setLimit(20);
        return metadata;
    }

    public static ResultGetListMetadata resultGetListMetadata() {
        ResultGetListMetadata result = new ResultGetListMetadata();
        List<VideoData> dataList = new ArrayList<>();
        dataList.add(dataMetadata1());
        dataList.add(dataMetadata2());
        result.setData(dataList);
        result.setMetadata(metadata());
        result.setVersion(4);
        result.setDatetime("2019-07-03T02:20:59.423Z");
        result.setPolicy("public");
        result.setRequestId("948004bb-f798-4183-b4dd-12559c2975bd");
        result.setServiceName("api-v4");
        result.setMessage("OK");
        result.setCode(200);
        result.setType("SUCCESS");
        return result;
    }

    public static CreateMetadata createMetadata() {
        CreateMetadata createMetadata = new CreateMetadata();
        createMetadata.setName("Folder sample");
        createMetadata.setType(CreateMetadata.TYPE_FOLDER);
        createMetadata.setDescription("This is a description sentences");
        createMetadata.setOrderNumber(1);
        createMetadata.setIcon("/example.com/icon.png");
        return createMetadata;
    }

    public static ResultCreateMetadata resultCreateMetadata() {
        ResultCreateMetadata resultCreateMetadata = new ResultCreateMetadata();
        VideoData data = new VideoData();
        data.setId("095778fa-7e42-45cc-8a0e-6118e540b61d");
        data.setEncode(0);
        data.setStatus(0);
        data.setView(0);
        resultCreateMetadata.setData(data);
        resultCreateMetadata.setVersion(4);
        resultCreateMetadata.setDatetime("2018-06-18T03:17:07.022Z");
        resultCreateMetadata.setPolicy("public");
        resultCreateMetadata.setRequestId("244f6f8f-4fc5-4f20-a535-e8ea4e0cab0e");
        resultCreateMetadata.setServiceName("api-v4");
        resultCreateMetadata.setMessage("OK");
        resultCreateMetadata.setCode(200);
        resultCreateMetadata.setType("SUCCESS");
        return resultCreateMetadata;
    }

    public static ResultGetDetailOfMetadata resultGetDetailOfMetadata() {
        ResultGetDetailOfMetadata result = new ResultGetDetailOfMetadata();
        result.setData(dataMetadata1());
        result.setVersion(4);
        result.setDatetime("2018-06-18T04:30:26.394Z");
        result.setPolicy("public");
        result.setRequestId("992ecf2d-3ece-44a2-880f-c60d4e6597fa");
        result.setServiceName("api");
        result.setMessage("OK");
        result.setCode(200);
        result.setType("SUCCESS");
        return result;
    }

    public static ResultUpdateMetadata resultUpdateMetadata() {
        ResultUpdateMetadata resultUpdateMetadata = new ResultUpdateMetadata();
        VideoData data = new VideoData();
        data.setId("095778fa-7e42-45cc-8a0e-6118e540b61d");
        resultUpdateMetadata.setData(data);
        resultUpdateMetadata.setVersion(4);
        resultUpdateMetadata.setDatetime("2018-06-18T03:17:07.022Z");
        resultUpdateMetadata.setPolicy("public");
        resultUpdateMetadata.setRequestId("244f6f8f-4fc5-4f20-a535-e8ea4e0cab0e");
        resultUpdateMetadata.setServiceName("api");
        resultUpdateMetadata.setMessage("OK");
        resultUpdateMetadata.setCode(200);
        resultUpdateMetadata.setType("SUCCESS");
        return resultUpdateMetadata;
    }

    public static ResultDeleteAnMetadata resultDeleteMetadata() {
        ResultDeleteAnMetadata resultDeleteAnMetadata = new ResultDeleteAnMetadata();
        VideoData data = new VideoData();
        data.setId("095778fa-7e42-45cc-8a0e-6118e540b61d");
        resultDeleteAnMetadata.setData(data);
        resultDeleteAnMetadata.setVersion(4);
        resultDeleteAnMetadata.setDatetime("2018-06-18T03:17:07.022Z");
        resultDeleteAnMetadata.setPolicy("public");
        resultDeleteAnMetadata.setRequestId("244f6f8f-4fc5-4f20-a535-e8ea4e0cab0e");
        resultDeleteAnMetadata.setServiceName("api");
        resultDeleteAnMetadata.setMessage("OK");
        resultDeleteAnMetadata.setCode(200);
        resultDeleteAnMetadata.setType("SUCCESS");
        return resultDeleteAnMetadata;
    }

    public static VideoData dataEntity1() {
        VideoData data = new VideoData();
        data.setId("42ceb1ab-18ef-4f2e-b076-14299756d182");
        data.setName("Sample Video 1");
        data.setDescription(
                "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.");
        data.setShortDescription("Lorem Ipsum is simply dummy text of the printing and typesetting industry");
        data.setView(0);
        data.setPoster("https://example.com/picture001");
        data.setThumbnail("https://example.com/picture002");
        data.setType("vod");
        data.setDuration("237.865215");
        data.setPublishToCdn("success");
        data.setCreatedAt("2018-06-22T19:20:17.000Z");
        data.setUpdatedAt("2018-06-22T19:20:17.000Z");
        Object embedMetadata = new Gson().fromJson(
                "{\"artist\":\"John Doe\"," + "\"album\":\"Album sample\"," + "\"genre\":\"Pop\"}",
                LinkedTreeMap.class);
        data.setEmbedMetadata(embedMetadata);
        return data;
    }

    public static VideoData dataEntity2() {
        VideoData data = new VideoData();
        data.setId("64b15996-2261-4f41-a3c4-72b652323f67");
        data.setName("Sample Video 2");
        data.setDescription(
                "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.");
        data.setShortDescription("Lorem Ipsum is simply dummy text of the printing and typesetting industry");
        data.setView(0);
        data.setPoster("https://example.com/picture001");
        data.setThumbnail("https://example.com/picture002");
        data.setType("vod");
        data.setDuration("178.178105");
        data.setPublishToCdn("success");
        data.setCreatedAt("2018-06-22T19:16:22.000Z");
        data.setUpdatedAt("2018-06-22T19:16:22.000Z");
        Object embedMetadata = new Gson().fromJson(
                "{\"artist\":\"John Doe\"," + "\"album\":\"Album sample\"," + "\"genre\":\"Pop\"}",
                LinkedTreeMap.class);
        data.setEmbedMetadata(embedMetadata);
        return data;
    }

    public static ResultListEntity resultResultListEntity() {
        ResultListEntity resultListEntity = new ResultListEntity();
        List<VideoData> list = new ArrayList<>();
        list.add(dataEntity1());
        list.add(dataEntity2());
        resultListEntity.setData(list);
        resultListEntity.setMetadata(metadata());
        resultListEntity.setVersion(4);
        resultListEntity.setDatetime("2018-06-22T19:20:19.536Z");
        resultListEntity.setPolicy("public");
        resultListEntity.setRequestId("beac9674-9d87-49eb-9ded-3b8ddc258044");
        resultListEntity.setServiceName("api");
        resultListEntity.setMessage("OK");
        resultListEntity.setCode(200);
        resultListEntity.setType("SUCCESS");
        return resultListEntity;
    }

    public static ResultRetrieveAnEntity resultRetrieveAnEntity() {
        ResultRetrieveAnEntity resultRetrieveAnEntity = new ResultRetrieveAnEntity();
        VideoData data = new VideoData();
        data.setId("8b83886e-9cc3-4eab-9258-ebb16c0c73de");
        resultRetrieveAnEntity.setData(data);
        resultRetrieveAnEntity.setVersion(4);
        resultRetrieveAnEntity.setDatetime("2018-06-15T18:52:45.755Z");
        resultRetrieveAnEntity.setPolicy("public");
        resultRetrieveAnEntity.setRequestId("a27c393d-c90d-44a0-9d44-4d493647889a");
        resultRetrieveAnEntity.setServiceName("api");
        resultRetrieveAnEntity.setMessage("OK");
        resultRetrieveAnEntity.setCode(200);
        resultRetrieveAnEntity.setType("SUCCESS");
        return resultRetrieveAnEntity;
    }

    public static ResultRetrieveALive resultRetrieveALive() {
        ResultRetrieveALive resultRetrieveALive = new ResultRetrieveALive();
        VideoData data = new VideoData();
        data.setId("8b83886e-9cc3-4eab-9258-ebb16c0c73de");
        data.setName("checking 01");
        data.setDescription("checking");
        data.setMode("pull");
        data.setResourceMode("single");
        data.setEncode(0);
        data.setChannelName("checking-01");
        data.setPoster("https://example.com/poster.jpeg");
        data.setThumbnail("https://example.com/thumbnail.jpeg");
        data.setLinkStream("[\"https://www.youtube.com/watch?v=pQzaHPoNX1I\"]");
        data.setCreatedAt("2018-06-21T14:33:36.000Z");
        data.setUpdatedAt("2018-06-21T14:33:36.000Z");
        resultRetrieveALive.setData(data);
        resultRetrieveALive.setVersion(4);
        resultRetrieveALive.setDatetime("2018-06-21T14:34:22.335Z");
        resultRetrieveALive.setPolicy("public");
        resultRetrieveALive.setRequestId("088668ec-6046-4310-879a-2e0e72ac1f52");
        resultRetrieveALive.setServiceName("api");
        resultRetrieveALive.setMessage("OK");
        resultRetrieveALive.setCode(200);
        resultRetrieveALive.setType("SUCCESS");
        return resultRetrieveALive;
    }

    public static LiveFeedViews resultGetViewALiveFeed() {
        LiveFeedViews liveFeedViews = new LiveFeedViews();
        vn.uiza.restapi.uiza.model.v3.livestreaming.getviewalivefeed.Data data =
                new vn.uiza.restapi.uiza.model.v3.livestreaming.getviewalivefeed.Data();
        data.setWatchnow(1);
        liveFeedViews.setData(data);
        liveFeedViews.setVersion(4);
        liveFeedViews.setDatetime("2018-06-15T18:52:45.755Z");
        liveFeedViews.setPolicy("public");
        liveFeedViews.setRequestId("a27c393d-c90d-44a0-9d44-4d493647889a");
        liveFeedViews.setServiceName("api");
        liveFeedViews.setMessage("OK");
        liveFeedViews.setCode(200);
        liveFeedViews.setType("SUCCESS");
        return liveFeedViews;
    }

    public static ResultGetTokenStreaming resultGetTokenStreaming() {
        ResultGetTokenStreaming tokenStreaming = new ResultGetTokenStreaming();
        StreamingToken streamingToken =
                new StreamingToken();
        streamingToken.setToken(
                "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbnRpdHlfaWQiOiI0NWE5MDhmNy1hNjJlLTRlYWYtOGNlMi1kYzU2OTlmMzM0MDYiLCJhcHBfaWQiOiI5OThhMWExNzEzODY0NDQyOGNlMDI4ZDJkZTIwYzVhMCIsImNvbnRlbnRfdHlwZSI6ImxpdmUiLCJpYXQiOjE1NjIyOTY4NjksImV4cCI6MTU2MjMwMDQ2OX0.fcdN0vibBqlUxfknTwJy5OlticpyjqsqZz0yiTmek_I");
        tokenStreaming.setStreamingToken(streamingToken);
        tokenStreaming.setDatetime("2019-07-05T03:21:09.536Z");
        tokenStreaming.setPolicy("public");
        tokenStreaming.setRequestId("0a1d31a1-a45c-4fc0-bb5d-9ac1587f50f9");
        tokenStreaming.setServiceName("api-v4");
        tokenStreaming.setVersion(4);
        tokenStreaming.setMessage("OK");
        tokenStreaming.setType("SUCCESS");
        tokenStreaming.setCode(200);
        return tokenStreaming;
    }

    public static ListSkin resultGetListSkin() {
        ListSkin listSkin = new ListSkin();
        listSkin.setData(new ArrayList<Skin>());
        listSkin.setDatetime("2019-07-05T03:25:22.917Z");
        listSkin.setPolicy("public");
        listSkin.setRequestId("a28b58d5-514f-48d7-b11c-d2a56bc5b7a9");
        listSkin.setServiceName("api-v4");
        listSkin.setVersion(4);
        listSkin.setMessage("OK");
        listSkin.setType("SUCCESS");
        listSkin.setCode(200);
        return listSkin;
    }

    public static AdWrapper adWrapper() {
        AdWrapper adWrapper = new AdWrapper();
        adWrapper.setData(new ArrayList<Ad>());
        adWrapper.setDatetime("2019-07-05T03:25:22.917Z");
        adWrapper.setPolicy("public");
        adWrapper.setRequestId("a28b58d5-514f-48d7-b11c-d2a56bc5b7a9");
        adWrapper.setServiceName("api-v4");
        adWrapper.setVersion(4);
        adWrapper.setMessage("OK");
        adWrapper.setType("SUCCESS");
        adWrapper.setCode(200);
        return adWrapper;
    }
}
