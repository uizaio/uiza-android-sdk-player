package uizacoresdk.util;

import android.content.Context;
import io.uiza.core.api.client.UzRestClient;
import io.uiza.core.api.client.UzRestClientTracking;
import io.uiza.core.util.UzCoreUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import io.uiza.core.util.constant.Constants;
import io.uiza.core.util.UzDateTimeUtil;
import io.uiza.core.api.UzApiMaster;
import io.uiza.core.api.client.UzRestClientGetLinkPlay;
import io.uiza.core.api.client.UzRestClientHeartBeat;
import io.uiza.core.api.UzServiceApi;
import io.uiza.core.api.request.tracking.UizaTracking;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({
        UzRestClient.class, UzRestClientGetLinkPlay.class, UzRestClientHeartBeat.class,
        UzRestClientTracking.class, UZUtil.class, UzCoreUtil.class, UZOsUtil.class, TmpParamData.class,
        UzDateTimeUtil.class, UzApiMaster.class, UzServiceApi.class
})
public class UZVideoDataTest {
    private String domain = "domainAPi";
    private String token = "token";
    private String appId = "appId";
    private final int API_VERSION_3 = 3;

    @Before
    public void setup() {
        PowerMockito.mockStatic(UzRestClient.class, UzRestClientGetLinkPlay.class,
                UzRestClientHeartBeat.class, UzRestClientTracking.class, UZUtil.class, UzCoreUtil.class,
                UZOsUtil.class, TmpParamData.class, UzDateTimeUtil.class, UzApiMaster.class, UzServiceApi.class);
    }

    @Test
    public void initSDK_DEV_success() {

        ArgumentCaptor<String> arg1 = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> arg2 = ArgumentCaptor.forClass(String.class);

        Context mockContext = mock(Context.class);
        PowerMockito.when(UzCoreUtil.getContext()).thenReturn(mockContext);
        UzServiceApi uzService = mock(UzServiceApi.class);
        PowerMockito.when(UzRestClient.createService(UzServiceApi.class)).thenReturn(uzService);
        UzApiMaster uzapiMaster = mock(UzApiMaster.class);
        PowerMockito.when(UzApiMaster.getInstance()).thenReturn(uzapiMaster);

        UZData.getInstance().initSDK(API_VERSION_3, domain, token, appId, Constants.ENVIRONMENT_DEV);

        PowerMockito.verifyStatic(UzRestClient.class, times(1));
        UzRestClient.init(arg1.capture(), arg2.capture());
        assertEquals(arg2.getValue(), token);

        UZUtil.setToken(any(Context.class), anyString());

        PowerMockito.verifyStatic(UzCoreUtil.class, times(2));
        UzCoreUtil.getContext();

        PowerMockito.verifyStatic(UzRestClientGetLinkPlay.class, times(1));
        UzRestClientGetLinkPlay.init(arg1.capture());
        assertEquals(arg1.getValue(), Constants.URL_GET_LINK_PLAY_DEV);

        PowerMockito.verifyStatic(UzRestClientHeartBeat.class, times(1));
        UzRestClientHeartBeat.init(arg1.capture());
        assertEquals(arg1.getValue(), Constants.URL_HEART_BEAT_DEV);

        UzRestClientTracking.init(anyString());
        UzRestClientTracking.addAccessToken(anyString());
        UZUtil.setApiTrackEndPoint(any(Context.class), anyString());
    }

    @Test
    public void initSDK_STG_success() {

        ArgumentCaptor<String> arg1 = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> arg2 = ArgumentCaptor.forClass(String.class);

        Context mockContext = mock(Context.class);
        PowerMockito.when(UzCoreUtil.getContext()).thenReturn(mockContext);
        UzServiceApi uzService = mock(UzServiceApi.class);
        PowerMockito.when(UzRestClient.createService(UzServiceApi.class)).thenReturn(uzService);
        UzApiMaster uzapiMaster = mock(UzApiMaster.class);
        PowerMockito.when(UzApiMaster.getInstance()).thenReturn(uzapiMaster);

        UZData.getInstance().initSDK(API_VERSION_3, domain, token, appId, Constants.ENVIRONMENT_STAG);

        PowerMockito.verifyStatic(UzRestClient.class, times(1));
        UzRestClient.init(arg1.capture(), arg2.capture());
        assertEquals(arg2.getValue(), token);

        UZUtil.setToken(any(Context.class), anyString());

        PowerMockito.verifyStatic(UzCoreUtil.class, times(2));
        UzCoreUtil.getContext();

        PowerMockito.verifyStatic(UzRestClientGetLinkPlay.class, times(1));
        UzRestClientGetLinkPlay.init(arg1.capture());
        assertEquals(arg1.getValue(), Constants.URL_GET_LINK_PLAY_STAG);

        PowerMockito.verifyStatic(UzRestClientHeartBeat.class, times(1));
        UzRestClientHeartBeat.init(arg1.capture());
        assertEquals(arg1.getValue(), Constants.URL_HEART_BEAT_STAG);

        UzRestClientTracking.init(anyString());
        UzRestClientTracking.addAccessToken(anyString());
        UZUtil.setApiTrackEndPoint(any(Context.class), anyString());
    }

    @Test
    public void initSDK_PRO_success() {

        ArgumentCaptor<String> arg1 = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> arg2 = ArgumentCaptor.forClass(String.class);

        Context mockContext = mock(Context.class);
        PowerMockito.when(UzCoreUtil.getContext()).thenReturn(mockContext);
        UzServiceApi uzService = mock(UzServiceApi.class);
        PowerMockito.when(UzRestClient.createService(UzServiceApi.class)).thenReturn(uzService);
        UzApiMaster uzapiMaster = mock(UzApiMaster.class);
        PowerMockito.when(UzApiMaster.getInstance()).thenReturn(uzapiMaster);

        UZData.getInstance().initSDK(API_VERSION_3, domain, token, appId, Constants.ENVIRONMENT_PROD);

        PowerMockito.verifyStatic(UzRestClient.class, times(1));
        UzRestClient.init(arg1.capture(), arg2.capture());
        assertEquals(arg2.getValue(), token);

        UZUtil.setToken(any(Context.class), anyString());

        PowerMockito.verifyStatic(UzCoreUtil.class, times(2));
        UzCoreUtil.getContext();

        PowerMockito.verifyStatic(UzRestClientGetLinkPlay.class, times(1));
        UzRestClientGetLinkPlay.init(arg1.capture());
        assertEquals(arg1.getValue(), Constants.URL_GET_LINK_PLAY_PROD);

        PowerMockito.verifyStatic(UzRestClientHeartBeat.class, times(1));
        UzRestClientHeartBeat.init(arg1.capture());
        assertEquals(arg1.getValue(), Constants.URL_HEART_BEAT_PROD);

        UzRestClientTracking.init(anyString());
        UzRestClientTracking.addAccessToken(anyString());
        UZUtil.setApiTrackEndPoint(any(Context.class), anyString());
    }

    @Test
    public void initSDK_wrongDomain_failed() {
        String[] invalidDomains = new String[] {
                "domain API", null, ""
        };
        String token = "token";
        String appId = "appId";
        for (String domain : invalidDomains) {
            assertFalse(UZData.getInstance()
                    .initSDK(API_VERSION_3, domain, token, appId, Constants.ENVIRONMENT_PROD));
        }
    }

    @Test
    public void initSDK_wrongToken_failed() {
        String domain = "domainAPi";
        String[] invalidTokens = new String[] {
                "tok en", null, ""
        };
        String appId = "appId";
        for (String token : invalidTokens) {
            assertFalse(UZData.getInstance()
                    .initSDK(API_VERSION_3, domain, token, appId, Constants.ENVIRONMENT_PROD));
        }
    }

    @Test
    public void initSDK_wrongAppId_failed() {
        String domain = "domainAPi";
        String[] invalidAppIds = new String[] {
                "tok en", null, ""
        };
        String token = "token";
        for (String appId : invalidAppIds) {
            assertFalse(UZData.getInstance()
                    .initSDK(API_VERSION_3, domain, token, appId, Constants.ENVIRONMENT_PROD));
        }
    }

    @Test
    public void createTrackingInput_Success() {
        String playThrough = "";
        String eventType = "";
        String fakeDeviceId = "";
        Context mockContext = mock(Context.class);

        when(UZOsUtil.getDeviceId(mockContext)).thenReturn(fakeDeviceId);

        // For verifying that the TmpParamData is set.
        when(TmpParamData.getInstance()).thenReturn(mock(TmpParamData.class));
        when(TmpParamData.getInstance().getReferrer()).thenReturn("");
        when(TmpParamData.getInstance().getEntitySeries()).thenReturn("");
        when(TmpParamData.getInstance().getEntityProducer()).thenReturn("");
        when(TmpParamData.getInstance().getEntityContentType()).thenReturn("");
        when(TmpParamData.getInstance().getEntityLanguageCode()).thenReturn("");
        when(TmpParamData.getInstance().getEntityVariantName()).thenReturn("");
        when(TmpParamData.getInstance().getEntityVariantId()).thenReturn("");
        when(TmpParamData.getInstance().getEntityDuration()).thenReturn("");
        when(TmpParamData.getInstance().getEntityStreamType()).thenReturn("");
        when(TmpParamData.getInstance().getEntityEncodingVariant()).thenReturn("");

        // Because the dependency uizaTracking object can not be mocked, so try call real method for testing
        when(UZData.getInstance()
                .createTrackingInput(mockContext, playThrough, eventType)).thenCallRealMethod();
        UizaTracking uizaTracking =
                UZData.getInstance().createTrackingInput(mockContext, playThrough, eventType);

        // We can assert more detail
        assertNotNull(uizaTracking.getReferrer());
        assertNotNull(uizaTracking.getEntitySeries());
        assertNotNull(uizaTracking.getEntityProducer());
        assertNotNull(uizaTracking.getEntityContentType());
        assertNotNull(uizaTracking.getEntityLanguageCode());
        assertNotNull(uizaTracking.getEntityVariantName());
        assertNotNull(uizaTracking.getEntityVariantId());
        assertNotNull(uizaTracking.getEntityDuration());
        assertNotNull(uizaTracking.getEntityStreamType());
        assertNotNull(uizaTracking.getEntityEncodingVariant());
    }
}
