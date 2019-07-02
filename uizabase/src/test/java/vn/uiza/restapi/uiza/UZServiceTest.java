package vn.uiza.restapi.uiza;

import android.text.TextUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import rx.observers.TestSubscriber;
import vn.uiza.base.FakeData;
import vn.uiza.restapi.restclient.RestClient;
import vn.uiza.restapi.uiza.model.v3.metadata.createmetadata.ResultCreateMetadata;
import vn.uiza.restapi.uiza.model.v3.metadata.getlistmetadata.ResultGetListMetadata;
import vn.uiza.utils.util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static vn.uiza.base.FakeData.API_VERSION;
import static vn.uiza.base.FakeData.createUser;

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
    public void createAnUser_Success() {
        // JSON V3, Docs not found response for this request
        String responseString = getJsonFromResource("create_an_user_success.json");
        MockResponse response = new MockResponse().setResponseCode(200).setBody(responseString);
        mockServer.enqueue(response);
        TestSubscriber testSubscriber = new TestSubscriber<Object>();
        uzService.createAnUser(API_VERSION, createUser()).subscribe(testSubscriber);
        testSubscriber.awaitTerminalEvent(2, TimeUnit.SECONDS);

        testSubscriber.assertCompleted();
        testSubscriber.assertNoErrors();
        assertEquals(gson.toJsonTree(testSubscriber.getOnNextEvents().get(0)),
                gson.fromJson(responseString, JsonObject.class));
    }

    @Test
    public void retrieveAnUser_Success() {
        String responseString = getJsonFromResource("retrieve_an_user_success.json");
        MockResponse response = new MockResponse().setResponseCode(200).setBody(responseString);
        mockServer.enqueue(response);
        TestSubscriber testSubscriber = new TestSubscriber<Object>();
        uzService.retrieveAnUser(API_VERSION, "508d46b7-afa7-45e7-8141-fdeafe32a4cc").subscribe(testSubscriber);
        testSubscriber.awaitTerminalEvent(2, TimeUnit.SECONDS);

        testSubscriber.assertCompleted();
        testSubscriber.assertNoErrors();
        assertEquals(gson.toJsonTree(testSubscriber.getOnNextEvents().get(0)),
                gson.fromJson(responseString, JsonObject.class));
    }

    @Test
    public void listAllUser_Success() {
        // JSON V3, Docs not found response for this request
        String responseString = getJsonFromResource("list_all_users_success.json");
        MockResponse response = new MockResponse().setResponseCode(200).setBody(responseString);
        mockServer.enqueue(response);
        TestSubscriber testSubscriber = new TestSubscriber<Object>();
        uzService.listAllUser(API_VERSION).subscribe(testSubscriber);
        testSubscriber.awaitTerminalEvent(2, TimeUnit.SECONDS);

        testSubscriber.assertCompleted();
        testSubscriber.assertNoErrors();
        assertEquals(gson.toJsonTree(testSubscriber.getOnNextEvents().get(0)),
                gson.fromJson(responseString, JsonObject.class));
    }

    @Test
    public void updateAnUser_Success() {
        String responseString = getJsonFromResource("update_an_user_success.json");
        MockResponse response = new MockResponse().setResponseCode(200).setBody(responseString);
        mockServer.enqueue(response);
        TestSubscriber testSubscriber = new TestSubscriber<Object>();
        uzService.updateAnUser(API_VERSION, FakeData.updateUser()).subscribe(testSubscriber);
        testSubscriber.awaitTerminalEvent(2, TimeUnit.SECONDS);

        testSubscriber.assertCompleted();
        testSubscriber.assertNoErrors();
        assertEquals(gson.toJsonTree(testSubscriber.getOnNextEvents().get(0)),
                gson.fromJson(responseString, JsonObject.class));
    }

    @Test
    public void deleteAnUser_Success() {
        // JSON V3, Docs not found response for this request
        String responseString = getJsonFromResource("delete_an_user_success.json");
        MockResponse response = new MockResponse().setResponseCode(200).setBody(responseString);
        mockServer.enqueue(response);
        TestSubscriber testSubscriber = new TestSubscriber<Object>();
        uzService.deleteAnUser(FakeData.deleteUser()).subscribe(testSubscriber);
        testSubscriber.awaitTerminalEvent(2, TimeUnit.SECONDS);

        testSubscriber.assertCompleted();
        testSubscriber.assertNoErrors();
        assertEquals(gson.toJsonTree(testSubscriber.getOnNextEvents().get(0)),
                gson.fromJson(responseString, JsonObject.class));
    }

    @Test
    public void updatePassword_Success() {
        // JSON V3, Docs not found response for this request
        String responseString = getJsonFromResource("update_password_success.json");
        MockResponse response = new MockResponse().setResponseCode(200).setBody(responseString);
        mockServer.enqueue(response);
        TestSubscriber testSubscriber = new TestSubscriber<Object>();
        uzService.updatePassword(API_VERSION, FakeData.updatePassword()).subscribe(testSubscriber);
        testSubscriber.awaitTerminalEvent(2, TimeUnit.SECONDS);

        testSubscriber.assertCompleted();
        testSubscriber.assertNoErrors();
        assertEquals(gson.toJsonTree(testSubscriber.getOnNextEvents().get(0)),
                gson.fromJson(responseString, JsonObject.class));
    }

    @Test
    public void getListMetadata_Success() {
        // JSON V3, Docs not found response for this request
        String responseString = getJsonFromResource("get_list_metadata_success.json");
        MockResponse response = new MockResponse().setResponseCode(200).setBody(responseString);
        mockServer.enqueue(response);
        TestSubscriber testSubscriber = new TestSubscriber<ResultGetListMetadata>();
        uzService.getListMetadata(API_VERSION).subscribe(testSubscriber);
        testSubscriber.awaitTerminalEvent(2, TimeUnit.SECONDS);

        testSubscriber.assertCompleted();
        testSubscriber.assertNoErrors();
        ResultGetListMetadata expect = FakeData.resultGetListMetadata();
        ResultGetListMetadata actual = (ResultGetListMetadata) testSubscriber.getOnNextEvents().get(0);
        assertEquals(gson.toJson(expect), gson.toJson(actual));
    }


    @Test
    public void createMetadata_Success() {
        String responseString = getJsonFromResource("create_metadata_success.json");
        MockResponse response = new MockResponse().setResponseCode(200).setBody(responseString);
        mockServer.enqueue(response);
        TestSubscriber testSubscriber = new TestSubscriber<ResultCreateMetadata>();
        uzService.createMetadata(API_VERSION, FakeData.createMetadata()).subscribe(testSubscriber);
        testSubscriber.awaitTerminalEvent(2, TimeUnit.SECONDS);

        testSubscriber.assertCompleted();
        testSubscriber.assertNoErrors();
        ResultCreateMetadata expect = FakeData.resultCreateMetadata();
        ResultCreateMetadata actual = (ResultCreateMetadata) testSubscriber.getOnNextEvents().get(0);
        assertEquals(gson.toJson(expect), gson.toJson(actual));
    }

    private String getJsonFromResource(String fileName) {
        URL url = Objects.requireNonNull(getClass().getClassLoader()).getResource("api-response/" + fileName);
        File file = new File(url.getPath());
        return FileUtils.readFile2String(file, "UTF-8");
    }
}
