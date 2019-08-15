package io.uiza.core.api.restclient;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;

import android.text.TextUtils;
import io.uiza.core.api.UzServiceApi;
import io.uiza.core.api.client.UzRestClientTracking;
import java.security.InvalidParameterException;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;

public class UzRestClientTrackingTest extends BaseRestClientTest {
    @Test
    public void initSuccess() {
        PowerMockito.when(TextUtils.isEmpty(anyString())).thenReturn(false);

        UzRestClientTracking.init(validURL);

        TextUtils.isEmpty(validURL);
        UzRestClientTracking.addAuthorization("");

        assertNotNull(UzRestClientTracking.getRetrofit());
        assertTrue(UzRestClientTracking.hasHeader(authorizationHeader));
    }

    @Test
    public void init_emptyBaseApiUrl_error() {
        PowerMockito.when(TextUtils.isEmpty(anyString())).thenReturn(true);

        try {
            UzRestClientTracking.init(validURL);
        } catch (Exception e) {
            assertTrue(e instanceof InvalidParameterException);
            assertEquals(e.getMessage(), "baseApiUrl cannot null or empty");
        }
    }

    @Test
    public void init_invalidUrlFormat_error() {
        PowerMockito.when(TextUtils.isEmpty(anyString())).thenReturn(false);
        try {
            UzRestClientTracking.init(inValidURL);
        } catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
        }
    }

    @Test
    public void create_service_success() {
        PowerMockito.when(TextUtils.isEmpty(anyString())).thenReturn(false);
        UzRestClientTracking.init(validURL);
        assertNotNull(UzRestClientTracking.createService(UzServiceApi.class));
    }

    @Test
    public void create_service_without_init_failed() {
        PowerMockito.when(TextUtils.isEmpty(anyString())).thenReturn(false);
        try {
            UzRestClientTracking.createService(UzServiceApi.class);
        } catch (Exception e) {
            assertTrue(e instanceof IllegalStateException);
            assertEquals(e.getMessage(), "Must call init() before use");
        }
    }

    @Test
    public void add_header_success() {
        PowerMockito.when(TextUtils.isEmpty(anyString())).thenReturn(false);
        UzRestClientTracking.init(validURL);
        UzRestClientTracking.addHeader(testHeader, testHeaderValue);
        assertTrue(UzRestClientTracking.hasHeader(testHeader));
    }

    @Test
    public void remove_header_success() {
        PowerMockito.when(TextUtils.isEmpty(anyString())).thenReturn(false);
        UzRestClientTracking.init(validURL);
        UzRestClientTracking.addHeader(testHeader, testHeaderValue);
        UzRestClientTracking.removeHeader(testHeader);
        assertFalse(UzRestClientTracking.hasHeader(testHeader));
    }

    @Test
    public void remove_removeAuthorization_success() {
        PowerMockito.when(TextUtils.isEmpty(anyString())).thenReturn(false);
        UzRestClientTracking.init(validURL);
        assertTrue(UzRestClientTracking.hasHeader(authorizationHeader));
        UzRestClientTracking.removeAuthorization();
        assertFalse(UzRestClientTracking.hasHeader(authorizationHeader));
    }

    @Test
    public void add_accessToken_success() {
        PowerMockito.when(TextUtils.isEmpty(anyString())).thenReturn(false);
        UzRestClientTracking.init(validURL);
        UzRestClientTracking.addAccessToken(accessToken);
        assertTrue(UzRestClientTracking.hasHeader(accessToken));
    }

    @Test
    public void remove_accessToken_success() {
        PowerMockito.when(TextUtils.isEmpty(anyString())).thenReturn(false);
        UzRestClientTracking.init(validURL);
        UzRestClientTracking.addAccessToken(accessToken);
        assertTrue(UzRestClientTracking.hasHeader(accessToken));
        UzRestClientTracking.removeAccessToken();
        assertFalse(UzRestClientTracking.hasHeader(accessToken));
    }
}
