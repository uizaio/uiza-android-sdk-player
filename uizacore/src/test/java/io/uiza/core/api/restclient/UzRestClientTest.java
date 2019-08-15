package io.uiza.core.api.restclient;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;

import android.text.TextUtils;
import io.uiza.core.api.UzServiceApi;
import io.uiza.core.api.client.UzRestClient;
import java.security.InvalidParameterException;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;

public class UzRestClientTest extends BaseRestClientTest {

    @Test
    public void initSuccess() {
        PowerMockito.when(TextUtils.isEmpty(anyString())).thenReturn(false);

        UzRestClient.init(validURL);

        TextUtils.isEmpty(validURL);
        UzRestClient.addAuthorization("");

        assertNotNull(UzRestClient.getRetrofit());
        assertTrue(UzRestClient.hasHeader(authorizationHeader));
    }

    @Test
    public void init_emptyBaseApiUrl_error() {
        PowerMockito.when(TextUtils.isEmpty(anyString())).thenReturn(true);

        try {
            UzRestClient.init(validURL);
        } catch (Exception e) {
            assertTrue(e instanceof InvalidParameterException);
            assertEquals(e.getMessage(), "baseApiUrl cannot null or empty");
        }
    }

    @Test
    public void init_invalidUrlFormat_error() {
        PowerMockito.when(TextUtils.isEmpty(anyString())).thenReturn(false);
        try {
            UzRestClient.init(inValidURL);
        } catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
        }
    }

    @Test
    public void create_service_success() {
        PowerMockito.when(TextUtils.isEmpty(anyString())).thenReturn(false);
        UzRestClient.init(validURL);
        assertNotNull(UzRestClient.createService(UzServiceApi.class));
    }

    @Test
    public void create_service_without_init_failed() {
        PowerMockito.when(TextUtils.isEmpty(anyString())).thenReturn(false);
        try {
            UzRestClient.createService(UzServiceApi.class);
        } catch (Exception e) {
            assertTrue(e instanceof IllegalStateException);
            assertEquals(e.getMessage(), "Must call init() before using");
        }
    }

    @Test
    public void add_header_success() {
        PowerMockito.when(TextUtils.isEmpty(anyString())).thenReturn(false);
        UzRestClient.init(validURL);
        UzRestClient.addHeader(testHeader, testHeaderValue);
        assertTrue(UzRestClient.hasHeader(testHeader));
    }

    @Test
    public void remove_header_success() {
        PowerMockito.when(TextUtils.isEmpty(anyString())).thenReturn(false);
        UzRestClient.init(validURL);
        UzRestClient.addHeader(testHeader, testHeaderValue);
        UzRestClient.removeHeader(testHeader);
        assertFalse(UzRestClient.hasHeader(testHeader));
    }

    @Test
    public void remove_removeAuthorization_success() {
        PowerMockito.when(TextUtils.isEmpty(anyString())).thenReturn(false);
        UzRestClient.init(validURL);
        assertTrue(UzRestClient.hasHeader(authorizationHeader));
        UzRestClient.removeAuthorization();
        assertFalse(UzRestClient.hasHeader(authorizationHeader));
    }
}