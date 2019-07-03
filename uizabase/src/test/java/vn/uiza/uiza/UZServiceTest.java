package vn.uiza.uiza;


import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import rx.observers.TestSubscriber;
import vn.uiza.base.FakeData;
import vn.uiza.restapi.restclient.RestClient;
import vn.uiza.restapi.uiza.UZService;
import vn.uiza.restapi.uiza.model.v3.linkplay.getlinkplay.ResultGetLinkPlay;
import vn.uiza.restapi.uiza.model.v3.linkplay.gettokenstreaming.ResultGetTokenStreaming;
import vn.uiza.restapi.uiza.model.v3.livestreaming.getviewalivefeed.ResultGetViewALiveFeed;
import vn.uiza.restapi.uiza.model.v3.livestreaming.retrievealive.ResultRetrieveALive;
import vn.uiza.restapi.uiza.model.v3.metadata.deleteanmetadata.ResultDeleteAnMetadata;
import vn.uiza.restapi.uiza.model.v3.metadata.getdetailofmetadata.ResultGetDetailOfMetadata;
import vn.uiza.restapi.uiza.model.v3.metadata.updatemetadata.ResultUpdateMetadata;
import vn.uiza.restapi.uiza.model.v3.videoondeman.listallentity.ResultListEntity;
import vn.uiza.restapi.uiza.model.v3.videoondeman.retrieveanentity.ResultRetrieveAnEntity;
import vn.uiza.utils.util.FileUtils;

import static org.junit.Assert.assertEquals;
import static vn.uiza.base.FakeData.API_VERSION;
import static vn.uiza.base.FakeData.createMetadata;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.net.ssl.*")
@PrepareForTest({TextUtils.class})
public class UZServiceTest {
    private Gson gson;
    private MockWebServer mockServer;
    private UZService uzService;

    @Before
    public void setup() {
        PowerMockito.mockStatic(TextUtils.class);
        gson = new Gson();
        mockServer = new MockWebServer();
        String mockUrl = mockServer.url(FakeData.BASE_URL).toString();
        RestClient.init(mockUrl, FakeData.TOKEN);
        uzService = RestClient.createService(UZService.class);
    }

    @After
    public void tearDown() throws IOException {
        mockServer.shutdown();
    }

    @Test
    public void getDetailOfMetadata_Success() {
        String responseString = getJsonFromResource("get_detail_metadata_success.json");
        MockResponse response = new MockResponse().setResponseCode(200).setBody(responseString);
        mockServer.enqueue(response);
        TestSubscriber testSubscriber = new TestSubscriber<ResultGetDetailOfMetadata>();
        uzService.getDetailOfMetadata(API_VERSION,"f932aa79-852a-41f7-9adc-19935034f944").subscribe(testSubscriber);
        testSubscriber.awaitTerminalEvent(2, TimeUnit.SECONDS);
        testSubscriber.assertCompleted();
        testSubscriber.assertNoErrors();
        ResultGetDetailOfMetadata expect = FakeData.resultGetDetailOfMetadata();
        ResultGetDetailOfMetadata actual = (ResultGetDetailOfMetadata) testSubscriber.getOnNextEvents().get(0);
        assertEquals(gson.toJson(expect), gson.toJson(actual));
    }


    @Test
    public void updateMetadata_Success() {
        String responseString = getJsonFromResource("update_metadata_success.json");
        MockResponse response = new MockResponse().setResponseCode(200).setBody(responseString);
        mockServer.enqueue(response);
        TestSubscriber testSubscriber = new TestSubscriber<ResultUpdateMetadata>();
        uzService.updateMetadata(API_VERSION,createMetadata()).subscribe(testSubscriber);
        testSubscriber.awaitTerminalEvent(2, TimeUnit.SECONDS);
        testSubscriber.assertCompleted();
        testSubscriber.assertNoErrors();
        ResultUpdateMetadata expect = FakeData.resultUpdateMetadata();
        ResultUpdateMetadata actual = (ResultUpdateMetadata) testSubscriber.getOnNextEvents().get(0);
        assertEquals(gson.toJson(expect), gson.toJson(actual));
    }

    @Test
    public void deleteMetadata_Success() {
        String responseString = getJsonFromResource("delete_metadata_success.json");
        MockResponse response = new MockResponse().setResponseCode(200).setBody(responseString);
        mockServer.enqueue(response);
        TestSubscriber testSubscriber = new TestSubscriber<ResultDeleteAnMetadata>();
        uzService.deleteAnMetadata(API_VERSION,"f932aa79-852a-41f7-9adc-19935034f944").subscribe(testSubscriber);
        testSubscriber.awaitTerminalEvent(2, TimeUnit.SECONDS);
        testSubscriber.assertCompleted();
        testSubscriber.assertNoErrors();
        ResultDeleteAnMetadata expect = FakeData.resultDeleteMetadata();
        ResultDeleteAnMetadata actual = (ResultDeleteAnMetadata) testSubscriber.getOnNextEvents().get(0);
        assertEquals(gson.toJson(expect), gson.toJson(actual));
    }

    @Test
    public void getListAllEntity_Success() {
        String responseString = getJsonFromResource("get_list_all_entity_success.json");
        MockResponse response = new MockResponse().setResponseCode(200).setBody(responseString);
        mockServer.enqueue(response);
        TestSubscriber testSubscriber = new TestSubscriber<ResultListEntity>();
        uzService.getListAllEntity(API_VERSION).subscribe(testSubscriber);
        testSubscriber.awaitTerminalEvent(2, TimeUnit.SECONDS);
        testSubscriber.assertCompleted();
        testSubscriber.assertNoErrors();
        ResultListEntity expect = FakeData.resultResultListEntity();
        ResultListEntity actual = (ResultListEntity) testSubscriber.getOnNextEvents().get(0);
        assertEquals(gson.toJson(expect), gson.toJson(actual));
    }
    @Test
    public void retrieveAnEntity_Success() {
        String responseString = getJsonFromResource("retrieve_an_entity_success.json");
        MockResponse response = new MockResponse().setResponseCode(200).setBody(responseString);
        mockServer.enqueue(response);
        TestSubscriber testSubscriber = new TestSubscriber<ResultRetrieveAnEntity>();
        uzService.retrieveAnEntity(API_VERSION,"8b83886e-9cc3-4eab-9258-ebb16c0c73de","appId").subscribe(testSubscriber);
        testSubscriber.awaitTerminalEvent(2, TimeUnit.SECONDS);
        testSubscriber.assertCompleted();
        testSubscriber.assertNoErrors();
        ResultRetrieveAnEntity expect = FakeData.resultRetrieveAnEntity();
        ResultRetrieveAnEntity actual = (ResultRetrieveAnEntity) testSubscriber.getOnNextEvents().get(0);
        assertEquals(gson.toJson(expect), gson.toJson(actual));
    }

    @Test
    public void searchEntity_Success() {
        String responseString = getJsonFromResource("search_entity_success.json");
        MockResponse response = new MockResponse().setResponseCode(200).setBody(responseString);
        mockServer.enqueue(response);
        TestSubscriber testSubscriber = new TestSubscriber<ResultListEntity>();
        uzService.searchEntity(API_VERSION,"keyword").subscribe(testSubscriber);
        testSubscriber.awaitTerminalEvent(2, TimeUnit.SECONDS);
        testSubscriber.assertCompleted();
        testSubscriber.assertNoErrors();
        ResultListEntity expect = FakeData.resultResultListEntity();
        ResultListEntity actual = (ResultListEntity) testSubscriber.getOnNextEvents().get(0);
        assertEquals(gson.toJson(expect), gson.toJson(actual));
    }
    @Test
    public void retrieveALiveEvent_Success() {
        String responseString = getJsonFromResource("retrieve_a_live_event_success.json");
        MockResponse response = new MockResponse().setResponseCode(200).setBody(responseString);
        mockServer.enqueue(response);
        TestSubscriber testSubscriber = new TestSubscriber<ResultRetrieveALive>();
        uzService.retrieveALiveEvent(API_VERSION,"8b83886e-9cc3-4eab-9258-ebb16c0c73de","AppId").subscribe(testSubscriber);
        testSubscriber.awaitTerminalEvent(2, TimeUnit.SECONDS);
        testSubscriber.assertCompleted();
        testSubscriber.assertNoErrors();
        ResultRetrieveALive expect = FakeData.resultRetrieveALive();
        ResultRetrieveALive actual = (ResultRetrieveALive) testSubscriber.getOnNextEvents().get(0);
        assertEquals(gson.toJson(expect), gson.toJson(actual));
    }
    @Test
    public void startALiveStream_Success() {
        String responseString = getJsonFromResource("start_a_live_stream_success.json");
        MockResponse response = new MockResponse().setResponseCode(200).setBody(responseString);
        mockServer.enqueue(response);
        TestSubscriber testSubscriber = new TestSubscriber<Object>();
        uzService.startALiveEvent(API_VERSION,FakeData.bodyStartALiveFeed()).subscribe(testSubscriber);
        testSubscriber.awaitTerminalEvent(2, TimeUnit.SECONDS);
        testSubscriber.assertCompleted();
        testSubscriber.assertNoErrors();
        assertEquals(gson.toJsonTree(testSubscriber.getOnNextEvents().get(0)),
                gson.fromJson(responseString, JsonObject.class));
    }
    @Test
    public void getViewALiveFeed_Success() {
        String responseString = getJsonFromResource("get_view_a_live_feed_success.json");
        MockResponse response = new MockResponse().setResponseCode(200).setBody(responseString);
        mockServer.enqueue(response);
        TestSubscriber testSubscriber = new TestSubscriber<ResultGetViewALiveFeed>();
        uzService.getViewALiveFeed(API_VERSION,"8e133d0d-5f67-45e8-8812-44b2ddfd9fe2","AppId").subscribe(testSubscriber);
        testSubscriber.awaitTerminalEvent(2, TimeUnit.SECONDS);
        testSubscriber.assertCompleted();
        testSubscriber.assertNoErrors();
        ResultGetViewALiveFeed expect = FakeData.resultGetViewALiveFeed();
        ResultGetViewALiveFeed actual = (ResultGetViewALiveFeed) testSubscriber.getOnNextEvents().get(0);
        assertEquals(gson.toJson(expect), gson.toJson(actual));
    }
    @Test
    public void getTokenStreaming_Success() {
        String responseString = getJsonFromResource("get_token_streaming_success.json");
        MockResponse response = new MockResponse().setResponseCode(200).setBody(responseString);
        mockServer.enqueue(response);
        TestSubscriber testSubscriber = new TestSubscriber<ResultGetLinkPlay>();
        uzService.getTokenStreaming(API_VERSION,FakeData.sendGetTokenStreaming()).subscribe(testSubscriber);
        testSubscriber.awaitTerminalEvent(2, TimeUnit.SECONDS);
        testSubscriber.assertCompleted();
        testSubscriber.assertNoErrors();
        ResultGetTokenStreaming expect = FakeData.resultGetTokenStreaming();
        ResultGetTokenStreaming actual = (ResultGetTokenStreaming) testSubscriber.getOnNextEvents().get(0);
        assertEquals(gson.toJson(expect), gson.toJson(actual));
    }

    private String getJsonFromResource(String fileName) {
        URL url = Objects.requireNonNull(getClass().getClassLoader()).getResource("api-response/" + fileName);
        File file = new File(url.getPath());
        return FileUtils.readFile2String(file, "UTF-8");
    }
}
