package vn.uiza.base;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import vn.uiza.restapi.uiza.model.v3.linkplay.gettokenstreaming.ResultGetTokenStreaming;
import vn.uiza.restapi.uiza.model.v3.linkplay.gettokenstreaming.SendGetTokenStreaming;
import vn.uiza.restapi.uiza.model.v3.livestreaming.getviewalivefeed.ResultGetViewALiveFeed;
import vn.uiza.restapi.uiza.model.v3.livestreaming.retrievealive.ResultRetrieveALive;
import vn.uiza.restapi.uiza.model.v3.livestreaming.retrievealiveevent.Metadata;
import vn.uiza.restapi.uiza.model.v3.livestreaming.startALiveFeed.BodyStartALiveFeed;
import vn.uiza.restapi.uiza.model.v3.metadata.createmetadata.CreateMetadata;
import vn.uiza.restapi.uiza.model.v3.metadata.createmetadata.ResultCreateMetadata;
import vn.uiza.restapi.uiza.model.v3.metadata.deleteanmetadata.ResultDeleteAnMetadata;
import vn.uiza.restapi.uiza.model.v3.metadata.getdetailofmetadata.Data;
import vn.uiza.restapi.uiza.model.v3.metadata.getdetailofmetadata.ResultGetDetailOfMetadata;
import vn.uiza.restapi.uiza.model.v3.metadata.getlistmetadata.ResultGetListMetadata;
import vn.uiza.restapi.uiza.model.v3.metadata.updatemetadata.ResultUpdateMetadata;
import vn.uiza.restapi.uiza.model.v3.usermanagement.createanuser.CreateUser;
import vn.uiza.restapi.uiza.model.v3.usermanagement.updatepassword.UpdatePassword;
import vn.uiza.restapi.uiza.model.v3.videoondeman.listallentity.ResultListEntity;
import vn.uiza.restapi.uiza.model.v3.videoondeman.retrieveanentity.ResultRetrieveAnEntity;

import java.util.ArrayList;
import java.util.List;

public class FakeData {
    public static final String BASE_URL = "/";
    public static final String TOKEN = "uap-7442d4b99eb349b1bb678614e64cf064-1405ee51";
    public static final String API_VERSION = "v4";

    public static CreateUser createUser() {
        CreateUser createUser = new CreateUser();
        createUser.setStatus(1);
        createUser.setUsername("user_test");
        createUser.setEmail("user_test@uiza.io");
        createUser.setPassword("FMpsr<4[dGPu?B#u");
        createUser.setDob("05/15/2018");
        createUser.setFullname("User Test");
        createUser.setAvatar("https://exemple.com/avatar.jpeg");
        createUser.setIsAdmin(1);
        createUser.setGender(0);
        return createUser;
    }

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

    public static CreateUser deleteUser() {
        CreateUser deleteUser = new CreateUser();
        deleteUser.setId("2c98b4d5-7d7f-4a0f-9258-5689f90fd28c");
        return deleteUser;
    }

    public static UpdatePassword updatePassword() {
        UpdatePassword updatePassword = new UpdatePassword();
        updatePassword.setOldPassword("FMpsr<4[dGPu?B#u");
        updatePassword.setNewPassword("S57Eb{:aMZhW=)G$");
        updatePassword.setId("2c98b4d5-7d7f-4a0f-9258-5689f90fd28c");
        return updatePassword;
    }

    public static BodyStartALiveFeed bodyStartALiveFeed() {
        BodyStartALiveFeed bodyStartALiveFeed = new BodyStartALiveFeed();
        bodyStartALiveFeed.setId("8b83886e-9cc3-4eab-9258-ebb16c0c73de");
        return bodyStartALiveFeed;
    }

    public static SendGetTokenStreaming sendGetTokenStreaming() {
        SendGetTokenStreaming sendGetTokenStreaming = new SendGetTokenStreaming();
        sendGetTokenStreaming.setAppId("01e137ad1b534004ad822035bf89b29f");
        sendGetTokenStreaming.setContentType("stream");
        sendGetTokenStreaming.setEntityId("b7297b29-c6c4-4bd6-a74f-b60d0118d275");
        return sendGetTokenStreaming;
    }

    public static Data dataMetadata1() {
        Data data = new Data();
        data.setId("f932aa79-852a-41f7-9adc-19935034f944");
        data.setName("Playlist sample");
        data.setDescription("Playlist desciption");
        data.setSlug("playlist-sample");
        data.setType("playlist");
        data.setOrderNumber(3);
        data.setIcon("/example.com/image002.png");
        data.setStatus(1);
        data.setCreatedAt("2018-06-18T04:29:05.000Z");
        data.setUpdatedAt("2018-06-18T04:29:05.000Z");
        return data;
    }

    public static Data dataMetadata2() {
        Data data = new Data();
        data.setId("4dc1e88f-29d8-401f-a4dd-dd7011a2dc92");
        data.setName("Loitp 1562119338575");
        data.setDescription("This is a description sentences");
        data.setSlug("loitp-1562119338575");
        data.setType("folder");
        data.setOrderNumber(1);
        data.setIcon("/exemple.com/icon.png");
        data.setStatus(1);
        data.setEncode(0);
        data.setView(0);
        data.setCreatedAt("2019-07-03T02:02:19.000Z");
        data.setUpdatedAt("0000-00-00 00:00:00");
        return data;
    }

    public static Data dataMetadata3() {
        Data data = new Data();
        data.setId("b5c700f5-e861-4b56-b147-9ed41a259ef3");
        data.setName("Loitp 1561390093942");
        data.setDescription("This is a description sentences");
        data.setSlug("loitp-1561390093942");
        data.setType("folder");
        data.setOrderNumber(1);
        data.setIcon("/exemple.com/icon.png");
        data.setStatus(1);
        data.setEncode(0);
        data.setView(0);
        data.setCreatedAt("2019-06-24T15:28:14.000Z");
        data.setUpdatedAt("0000-00-00 00:00:00");
        return data;
    }

    public static Data dataMetadata4() {
        Data data = new Data();
        data.setId("01b53df6-020a-4d07-ad38-e775b3659868");
        data.setName("Loitp 1559277566328");
        data.setDescription("This is a description sentences");
        data.setSlug("loitp-1559277566328");
        data.setType("folder");
        data.setOrderNumber(1);
        data.setIcon("/exemple.com/icon.png");
        data.setStatus(1);
        data.setEncode(0);
        data.setView(0);
        data.setCreatedAt("2019-05-31T04:39:26.000Z");
        data.setUpdatedAt("0000-00-00 00:00:00");
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
        List<Data> dataList = new ArrayList<>();
        dataList.add(dataMetadata1());
        dataList.add(dataMetadata2());
        dataList.add(dataMetadata3());
        dataList.add(dataMetadata4());
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
        createMetadata.setName("Loitp " + System.currentTimeMillis());
        createMetadata.setType(CreateMetadata.TYPE_FOLDER);
        createMetadata.setDescription("This is a description sentences");
        createMetadata.setOrderNumber(1);
        createMetadata.setIcon("/exemple.com/icon.png");
        return createMetadata;
    }

    public static ResultCreateMetadata resultCreateMetadata() {
        ResultCreateMetadata resultCreateMetadata = new ResultCreateMetadata();
        Data data = new Data();
        data.setId("4dc1e88f-29d8-401f-a4dd-dd7011a2dc92");
        data.setEncode(0);
        data.setStatus(0);
        data.setView(0);
        resultCreateMetadata.setData(data);
        resultCreateMetadata.setVersion(4);
        resultCreateMetadata.setDatetime("2019-07-03T02:02:19.595Z");
        resultCreateMetadata.setPolicy("public");
        resultCreateMetadata.setRequestId("f0a916c2-2b84-4c83-905f-d2d12ec0486b");
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
        Data data = new Data();
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
        Data data = new Data();
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

    public static Data dataEntity1() {
        Data data = new Data();
        data.setId("42ceb1ab-18ef-4f2e-b076-14299756d182");
        data.setName("Sample Video 1");
        data.setDescription("Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.");
        data.setShortDescription("Lorem Ipsum is simply dummy text of the printing and typesetting industry");
        data.setView(0);
        data.setPoster("https://example.com/picture001");
        data.setThumbnail("https://example.com/picture002");
        data.setType("vod");
        data.setDuration("237.865215");
        data.setPublishToCdn("success");
        data.setCreatedAt("2018-06-22T19:20:17.000Z");
        data.setUpdatedAt("2018-06-22T19:20:17.000Z");
        Object embedMetadata = new Gson().fromJson("{\"artist\":\"John Doe\"," +
                "\"album\":\"Album sample\"," +
                "\"genre\":\"Pop\"}", LinkedTreeMap.class);
        data.setEmbedMetadata(embedMetadata);
        return data;
    }

    public static Data dataEntity2() {
        Data data = new Data();
        data.setId("64b15996-2261-4f41-a3c4-72b652323f67");
        data.setName("Sample Video 2");
        data.setDescription("Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.");
        data.setShortDescription("Lorem Ipsum is simply dummy text of the printing and typesetting industry");
        data.setView(0);
        data.setPoster("https://example.com/picture001");
        data.setThumbnail("https://example.com/picture002");
        data.setType("vod");
        data.setDuration("178.178105");
        data.setPublishToCdn("success");
        data.setCreatedAt("2018-06-22T19:16:22.000Z");
        data.setUpdatedAt("2018-06-22T19:16:22.000Z");
        Object embedMetadata = new Gson().fromJson("{\"artist\":\"John Doe\"," +
                "\"album\":\"Album sample\"," +
                "\"genre\":\"Pop\"}", LinkedTreeMap.class);
        data.setEmbedMetadata(embedMetadata);
        return data;
    }

    public static ResultListEntity resultResultListEntity() {
        ResultListEntity resultListEntity = new ResultListEntity();
        List<Data> list = new ArrayList<>();
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
        Data data = new Data();
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
        Data data = new Data();
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

    public static ResultGetViewALiveFeed resultGetViewALiveFeed() {
        ResultGetViewALiveFeed resultGetViewALiveFeed = new ResultGetViewALiveFeed();
        vn.uiza.restapi.uiza.model.v3.livestreaming.getviewalivefeed.Data data =
                new vn.uiza.restapi.uiza.model.v3.livestreaming.getviewalivefeed.Data();
        data.setWatchnow(1);
        resultGetViewALiveFeed.setData(data);
        resultGetViewALiveFeed.setVersion(4);
        resultGetViewALiveFeed.setDatetime("2018-06-15T18:52:45.755Z");
        resultGetViewALiveFeed.setPolicy("public");
        resultGetViewALiveFeed.setRequestId("a27c393d-c90d-44a0-9d44-4d493647889a");
        resultGetViewALiveFeed.setServiceName("api");
        resultGetViewALiveFeed.setMessage("OK");
        resultGetViewALiveFeed.setCode(200);
        resultGetViewALiveFeed.setType("SUCCESS");
        return resultGetViewALiveFeed;
    }

    public static ResultGetTokenStreaming resultGetTokenStreaming() {
        ResultGetTokenStreaming tokenStreaming = new ResultGetTokenStreaming();
        vn.uiza.restapi.uiza.model.v3.linkplay.gettokenstreaming.Data data =
                new vn.uiza.restapi.uiza.model.v3.linkplay.gettokenstreaming.Data();
        data.setToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbnRpdHlfaWQiOiJiNzI5N2IyOS1jNmM0LTRiZDYtYTc0Zi1iNjBkMDExOGQyNzUiLCJhcHBfaWQiOiIwMWUxMzdhZDFiNTM0MDA0YWQ4MjIwMzViZjg5YjI5ZiIsImNvbnRlbnRfdHlwZSI6InN0cmVhbSIsImlhdCI6MTU1OTAzMzI4MSwiZXhwIjoxNTU5MDM2ODgxfQ.23zZtk4AzU99GsBJ99eoMs2-bInlq7I7S3TIkuHNQP0");
        tokenStreaming.setData(data);
        tokenStreaming.setVersion(4);
        tokenStreaming.setDatetime("2019-05-28T08:48:01.663Z");
        tokenStreaming.setPolicy("public");
        tokenStreaming.setRequestId("326f8ede-d4c1-480e-ac37-05b9a84fdeca");
        tokenStreaming.setServiceName("api");
        tokenStreaming.setMessage("OK");
        tokenStreaming.setCode(200);
        tokenStreaming.setType("SUCCESS");
        return tokenStreaming;
    }
}
