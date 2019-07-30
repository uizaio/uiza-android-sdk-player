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

public class UZRestClientHeartBeatTest extends BaseRestClientTest {
    @Test
    public void initSuccess() {
        PowerMockito.when(TextUtils.isEmpty(anyString())).thenReturn(false);

        UZRestClientHeartBeat.init(validURL);

        TextUtils.isEmpty(validURL);
        UZRestClientHeartBeat.addAuthorization("");

        assertNotNull(UZRestClientHeartBeat.getRetrofit());
        assertTrue(UZRestClientHeartBeat.hasHeader(authorizationHeader));
    }

    @Test
    public void init_emptyBaseApiUrl_error() {
        PowerMockito.when(TextUtils.isEmpty(anyString())).thenReturn(true);

        try {
            UZRestClientHeartBeat.init(validURL);
        } catch (Exception e) {
            assertTrue(e instanceof InvalidParameterException);
            assertEquals(e.getMessage(), "baseApiUrl cannot null or empty");
        }
    }

    @Test
    public void init_invalidUrlFormat_error() {
        PowerMockito.when(TextUtils.isEmpty(anyString())).thenReturn(false);
        try {
            UZRestClientHeartBeat.init(inValidURL);
        } catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
        }
    }

    @Test
    public void create_service_success() {
        PowerMockito.when(TextUtils.isEmpty(anyString())).thenReturn(false);
        UZRestClientHeartBeat.init(validURL);
        assertNotNull(UZRestClientHeartBeat.createService(UZService.class));
    }

    @Test
    public void create_service_without_init_failed() {
        PowerMockito.when(TextUtils.isEmpty(anyString())).thenReturn(false);
        try {
            UZRestClientHeartBeat.createService(UZService.class);
        } catch (Exception e) {
            assertTrue(e instanceof IllegalStateException);
            assertEquals(e.getMessage(), "Must call init() before use");
        }
    }

    @Test
    public void add_header_success() {
        PowerMockito.when(TextUtils.isEmpty(anyString())).thenReturn(false);
        UZRestClientHeartBeat.init(validURL);
        UZRestClientHeartBeat.addHeader(testHeader, testHeaderValue);
        assertTrue(UZRestClientHeartBeat.hasHeader(testHeader));
    }

    @Test
    public void remove_header_success() {
        PowerMockito.when(TextUtils.isEmpty(anyString())).thenReturn(false);
        UZRestClientHeartBeat.init(validURL);
        UZRestClientHeartBeat.addHeader(testHeader, testHeaderValue);
        UZRestClientHeartBeat.removeHeader(testHeader);
        assertFalse(UZRestClientHeartBeat.hasHeader(testHeader));
    }

    @Test
    public void remove_removeAuthorization_success() {
        PowerMockito.when(TextUtils.isEmpty(anyString())).thenReturn(false);
        UZRestClientHeartBeat.init(validURL);
        assertTrue(UZRestClientHeartBeat.hasHeader(authorizationHeader));
        UZRestClientHeartBeat.removeAuthorization();
        assertFalse(UZRestClientHeartBeat.hasHeader(authorizationHeader));
    }
}
