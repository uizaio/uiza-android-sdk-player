package io.uiza.core.api.restclient;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;

import android.text.TextUtils;
import io.uiza.core.api.UzServiceApi;
import io.uiza.core.api.client.UzRestClientHeartBeat;
import java.security.InvalidParameterException;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;

public class UzRestClientHeartBeatTest extends BaseRestClientTest {
    @Test
    public void initSuccess() {
        PowerMockito.when(TextUtils.isEmpty(anyString())).thenReturn(false);

        UzRestClientHeartBeat.init(validURL);

        TextUtils.isEmpty(validURL);
        UzRestClientHeartBeat.addAuthorization("");

        assertNotNull(UzRestClientHeartBeat.getRetrofit());
        assertTrue(UzRestClientHeartBeat.hasHeader(authorizationHeader));
    }

    @Test
    public void init_emptyBaseApiUrl_error() {
        PowerMockito.when(TextUtils.isEmpty(anyString())).thenReturn(true);

        try {
            UzRestClientHeartBeat.init(validURL);
        } catch (Exception e) {
            assertTrue(e instanceof InvalidParameterException);
            assertEquals(e.getMessage(), "baseApiUrl cannot null or empty");
        }
    }

    @Test
    public void init_invalidUrlFormat_error() {
        PowerMockito.when(TextUtils.isEmpty(anyString())).thenReturn(false);
        try {
            UzRestClientHeartBeat.init(inValidURL);
        } catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
        }
    }

    @Test
    public void create_service_success() {
        PowerMockito.when(TextUtils.isEmpty(anyString())).thenReturn(false);
        UzRestClientHeartBeat.init(validURL);
        assertNotNull(UzRestClientHeartBeat.createService(UzServiceApi.class));
    }

    @Test
    public void create_service_without_init_failed() {
        PowerMockito.when(TextUtils.isEmpty(anyString())).thenReturn(false);
        try {
            UzRestClientHeartBeat.createService(UzServiceApi.class);
        } catch (Exception e) {
            assertTrue(e instanceof IllegalStateException);
            assertEquals(e.getMessage(), "Must call init() before use");
        }
    }

    @Test
    public void add_header_success() {
        PowerMockito.when(TextUtils.isEmpty(anyString())).thenReturn(false);
        UzRestClientHeartBeat.init(validURL);
        UzRestClientHeartBeat.addHeader(testHeader, testHeaderValue);
        assertTrue(UzRestClientHeartBeat.hasHeader(testHeader));
    }

    @Test
    public void remove_header_success() {
        PowerMockito.when(TextUtils.isEmpty(anyString())).thenReturn(false);
        UzRestClientHeartBeat.init(validURL);
        UzRestClientHeartBeat.addHeader(testHeader, testHeaderValue);
        UzRestClientHeartBeat.removeHeader(testHeader);
        assertFalse(UzRestClientHeartBeat.hasHeader(testHeader));
    }

    @Test
    public void remove_removeAuthorization_success() {
        PowerMockito.when(TextUtils.isEmpty(anyString())).thenReturn(false);
        UzRestClientHeartBeat.init(validURL);
        assertTrue(UzRestClientHeartBeat.hasHeader(authorizationHeader));
        UzRestClientHeartBeat.removeAuthorization();
        assertFalse(UzRestClientHeartBeat.hasHeader(authorizationHeader));
    }
}
