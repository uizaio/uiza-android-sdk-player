package uizacoresdk.util;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import vn.uiza.core.common.Constants;
import vn.uiza.models.tracking.UizaTracking;
import vn.uiza.restapi.RxBinder;
import vn.uiza.restapi.UizaClientFactory;
import vn.uiza.restapi.UizaHeartBeatService;
import vn.uiza.restapi.UizaVideoService;
import vn.uiza.restapi.restclient.UizaHeartBeatClient;
import vn.uiza.restapi.restclient.UizaRestClient;
import vn.uiza.restapi.restclient.UizaTrackingClient;
import vn.uiza.utils.LDateUtils;
import vn.uiza.utils.LDeviceUtil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({
        UizaRestClient.class, UizaHeartBeatClient.class,
        UizaTrackingClient.class, UZUtil.class, UZUtil.class, LDeviceUtil.class, TmpParamData.class,
        LDateUtils.class, RxBinder.class, UizaVideoService.class
})
public class UZDataTest {
    private String domain = "domainAPi";
    private String token = "token";

    @Before
    public void setup() {
        PowerMockito.mockStatic(UizaRestClient.class,
                UizaHeartBeatClient.class, UizaTrackingClient.class, UZUtil.class,
                LDeviceUtil.class, TmpParamData.class, LDateUtils.class, UizaVideoService.class);
    }

    @Test
    public void initSDK_DEV_success() {

        ArgumentCaptor<String> arg1 = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> arg2 = ArgumentCaptor.forClass(String.class);

        Context mockContext = mock(Context.class);
        PowerMockito.when(UZUtil.getContext()).thenReturn(mockContext);
        UizaVideoService uzService = mock(UizaVideoService.class);
        PowerMockito.when(UizaClientFactory.getVideoService()).thenReturn(uzService);
        UZData.getInstance().initSDK(domain, token, Constants.ENVIRONMENT.DEV);

        PowerMockito.verifyStatic(UizaRestClient.class, times(1));
        UizaClientFactory.setup(arg1.capture(), arg2.capture());
        assertEquals(arg2.getValue(), token);

        UZUtil.setToken(anyString());

        PowerMockito.verifyStatic(UZUtil.class, times(2));
        UZUtil.getContext();

        PowerMockito.verifyStatic(UizaHeartBeatService.class, times(1));
        UizaHeartBeatClient.getInstance().init(arg1.capture(), "");
        assertEquals(arg1.getValue(), Constants.URL_HEART_BEAT_DEV);

        UizaTrackingClient.getInstance().init(anyString(), "");
        UizaTrackingClient.getInstance().addAccessToken(anyString());
        UZUtil.setApiTrackEndPoint(anyString());
    }

    @Test
    public void initSDK_STG_success() {

        ArgumentCaptor<String> arg1 = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> arg2 = ArgumentCaptor.forClass(String.class);

        Context mockContext = mock(Context.class);
        PowerMockito.when(UZUtil.getContext()).thenReturn(mockContext);
        UizaVideoService uzService = mock(UizaVideoService.class);
        PowerMockito.when(UizaClientFactory.getVideoService()).thenReturn(uzService);
        UZData.getInstance().initSDK(domain, token, Constants.ENVIRONMENT.STAG);

        PowerMockito.verifyStatic(UizaRestClient.class, times(1));
        UizaRestClient.getInstance().init(arg1.capture(), arg2.capture());
        assertEquals(arg2.getValue(), token);

        UZUtil.setToken(anyString());

        PowerMockito.verifyStatic(UZUtil.class, times(2));
        UZUtil.getContext();

        PowerMockito.verifyStatic(UizaHeartBeatService.class, times(1));
        UizaHeartBeatClient.getInstance().init(arg1.capture(), "");
        assertEquals(arg1.getValue(), Constants.URL_HEART_BEAT_STAG);

        UizaTrackingClient.getInstance().init(anyString(), "");
        UizaTrackingClient.getInstance().addAccessToken(anyString());
        UZUtil.setApiTrackEndPoint(anyString());
    }

    @Test
    public void initSDK_PRO_success() {

        ArgumentCaptor<String> arg1 = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> arg2 = ArgumentCaptor.forClass(String.class);

        Context mockContext = mock(Context.class);
        PowerMockito.when(UZUtil.getContext()).thenReturn(mockContext);
        UizaVideoService uzService = mock(UizaVideoService.class);
        PowerMockito.when(UizaClientFactory.getVideoService()).thenReturn(uzService);

        UZData.getInstance().initSDK(domain, token, Constants.ENVIRONMENT.PROD);

        PowerMockito.verifyStatic(UizaRestClient.class, times(1));
        UizaRestClient.getInstance().init(arg1.capture(), arg2.capture());
        assertEquals(arg2.getValue(), token);

        UZUtil.setToken(anyString());

        PowerMockito.verifyStatic(UZUtil.class, times(2));
        UZUtil.getContext();

        PowerMockito.verifyStatic(UizaHeartBeatClient.class, times(1));
        UizaHeartBeatClient.getInstance().init(arg1.capture(), "");
        assertEquals(arg1.getValue(), Constants.URL_HEART_BEAT_PROD);

        UizaTrackingClient.getInstance().init(anyString(), "");
        UizaTrackingClient.getInstance().addAccessToken(anyString());
        UZUtil.setApiTrackEndPoint(anyString());
    }

    @Test
    public void initSDK_wrongDomain_failed() {
        String[] invalidDomains = new String[]{
                "domain API", null, ""
        };
        String token = "token";
        for (String domain : invalidDomains) {
            assertFalse(UZData.getInstance()
                    .initSDK(domain, token, Constants.ENVIRONMENT.PROD));
        }
    }

    @Test
    public void initSDK_wrongToken_failed() {
        String domain = "domainAPi";
        assertFalse(UZData.getInstance()
                .initSDK(domain, token, Constants.ENVIRONMENT.PROD));
    }

    @Test
    public void initSDK_wrongAppId_failed() {
        String domain = "domainAPi";
        String token = "token";
        assertFalse(UZData.getInstance()
                .initSDK(domain, token, Constants.ENVIRONMENT.PROD));
    }

    @Test
    public void createTrackingInput_Success() {
        String playThrough = "";
        String eventType = "";
        String fakeDeviceId = "";
        Context mockContext = mock(Context.class);

        when(LDeviceUtil.getDeviceId(mockContext)).thenReturn(fakeDeviceId);

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
