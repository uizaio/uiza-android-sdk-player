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

public class UZRestClientTest extends BaseRestClientTest {

    @Test
    public void initSuccess() {
        PowerMockito.when(TextUtils.isEmpty(anyString())).thenReturn(false);

        UZRestClient.init(validURL);

        TextUtils.isEmpty(validURL);
        UZRestClient.addAuthorization("");

        assertNotNull(UZRestClient.getRetrofit());
        assertTrue(UZRestClient.hasHeader(authorizationHeader));
    }

    @Test
    public void init_emptyBaseApiUrl_error() {
        PowerMockito.when(TextUtils.isEmpty(anyString())).thenReturn(true);

        try {
            UZRestClient.init(validURL);
        } catch (Exception e) {
            assertTrue(e instanceof InvalidParameterException);
            assertEquals(e.getMessage(), "baseApiUrl cannot null or empty");
        }
    }

    @Test
    public void init_invalidUrlFormat_error() {
        PowerMockito.when(TextUtils.isEmpty(anyString())).thenReturn(false);
        try {
            UZRestClient.init(inValidURL);
        } catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
        }
    }

    @Test
    public void create_service_success() {
        PowerMockito.when(TextUtils.isEmpty(anyString())).thenReturn(false);
        UZRestClient.init(validURL);
        assertNotNull(UZRestClient.createService(UZService.class));
    }

    @Test
    public void create_service_without_init_failed() {
        PowerMockito.when(TextUtils.isEmpty(anyString())).thenReturn(false);
        try {
            UZRestClient.createService(UZService.class);
        } catch (Exception e) {
            assertTrue(e instanceof IllegalStateException);
            assertEquals(e.getMessage(), "Must call init() before using");
        }
    }

    @Test
    public void add_header_success() {
        PowerMockito.when(TextUtils.isEmpty(anyString())).thenReturn(false);
        UZRestClient.init(validURL);
        UZRestClient.addHeader(testHeader, testHeaderValue);
        assertTrue(UZRestClient.hasHeader(testHeader));
    }

    @Test
    public void remove_header_success() {
        PowerMockito.when(TextUtils.isEmpty(anyString())).thenReturn(false);
        UZRestClient.init(validURL);
        UZRestClient.addHeader(testHeader, testHeaderValue);
        UZRestClient.removeHeader(testHeader);
        assertFalse(UZRestClient.hasHeader(testHeader));
    }

    @Test
    public void remove_removeAuthorization_success() {
        PowerMockito.when(TextUtils.isEmpty(anyString())).thenReturn(false);
        UZRestClient.init(validURL);
        assertTrue(UZRestClient.hasHeader(authorizationHeader));
        UZRestClient.removeAuthorization();
        assertFalse(UZRestClient.hasHeader(authorizationHeader));
    }
}