package vn.uiza.restapi.restclient;

import android.text.TextUtils;
import java.security.InvalidParameterException;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;
import vn.uiza.restapi.uiza.UZService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;

public class UZRestClientTrackingTest extends BaseRestClientTest {
    @Test
    public void initSuccess() {
        PowerMockito.when(TextUtils.isEmpty(anyString())).thenReturn(false);

        UZRestClientTracking.init(validURL);

        TextUtils.isEmpty(validURL);
        UZRestClientTracking.addAuthorization("");

        assertNotNull(UZRestClientTracking.getRetrofit());
        assertTrue(UZRestClientTracking.hasHeader(authorizationHeader));
    }

    @Test
    public void init_emptyBaseApiUrl_error() {
        PowerMockito.when(TextUtils.isEmpty(anyString())).thenReturn(true);

        try {
            UZRestClientTracking.init(validURL);
        } catch (Exception e) {
            assertTrue(e instanceof InvalidParameterException);
            assertEquals(e.getMessage(), "baseApiUrl cannot null or empty");
        }
    }

    @Test
    public void init_invalidUrlFormat_error() {
        PowerMockito.when(TextUtils.isEmpty(anyString())).thenReturn(false);
        try {
            UZRestClientTracking.init(inValidURL);
        } catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
        }
    }

    @Test
    public void create_service_success() {
        PowerMockito.when(TextUtils.isEmpty(anyString())).thenReturn(false);
        UZRestClientTracking.init(validURL);
        assertNotNull(UZRestClientTracking.createService(UZService.class));
    }

    @Test
    public void create_service_without_init_failed() {
        PowerMockito.when(TextUtils.isEmpty(anyString())).thenReturn(false);
        try {
            UZRestClientTracking.createService(UZService.class);
        } catch (Exception e) {
            assertTrue(e instanceof IllegalStateException);
            assertEquals(e.getMessage(), "Must call init() before use");
        }
    }

    @Test
    public void add_header_success() {
        PowerMockito.when(TextUtils.isEmpty(anyString())).thenReturn(false);
        UZRestClientTracking.init(validURL);
        UZRestClientTracking.addHeader(testHeader, testHeaderValue);
        assertTrue(UZRestClientTracking.hasHeader(testHeader));
    }

    @Test
    public void remove_header_success() {
        PowerMockito.when(TextUtils.isEmpty(anyString())).thenReturn(false);
        UZRestClientTracking.init(validURL);
        UZRestClientTracking.addHeader(testHeader, testHeaderValue);
        UZRestClientTracking.removeHeader(testHeader);
        assertFalse(UZRestClientTracking.hasHeader(testHeader));
    }

    @Test
    public void remove_removeAuthorization_success() {
        PowerMockito.when(TextUtils.isEmpty(anyString())).thenReturn(false);
        UZRestClientTracking.init(validURL);
        assertTrue(UZRestClientTracking.hasHeader(authorizationHeader));
        UZRestClientTracking.removeAuthorization();
        assertFalse(UZRestClientTracking.hasHeader(authorizationHeader));
    }

    @Test
    public void add_accessToken_success() {
        PowerMockito.when(TextUtils.isEmpty(anyString())).thenReturn(false);
        UZRestClientTracking.init(validURL);
        UZRestClientTracking.addAccessToken(accessToken);
        assertTrue(UZRestClientTracking.hasHeader(accessToken));
    }

    @Test
    public void remove_accessToken_success() {
        PowerMockito.when(TextUtils.isEmpty(anyString())).thenReturn(false);
        UZRestClientTracking.init(validURL);
        UZRestClientTracking.addAccessToken(accessToken);
        assertTrue(UZRestClientTracking.hasHeader(accessToken));
        UZRestClientTracking.removeAccessToken();
        assertFalse(UZRestClientTracking.hasHeader(accessToken));
    }
}
