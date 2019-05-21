package vn.uiza.restapi.restclient;

import android.text.TextUtils;

import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;

import java.security.InvalidParameterException;

import vn.uiza.restapi.uiza.UZService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;

public class RestClientTest extends BaseRestClientTest{
    @Test
    public void initSuccess() {
        PowerMockito.when(TextUtils.isEmpty(anyString())).thenReturn(false);
        RestClient.init(validURL);

        PowerMockito.verifyStatic(TextUtils.class);
        TextUtils.isEmpty(validURL);
        RestClient.addAuthorization("");

        assertNotNull(RestClient.getRetrofit());
        assertTrue(RestClient.hasHeader(authorizationHeader));
    }

    @Test
    public void init_emptyBaseApiUrl_error() {
        PowerMockito.when(TextUtils.isEmpty(anyString())).thenReturn(true);

        try {
            RestClient.init(validURL);
        } catch (Exception e) {
            assertTrue(e instanceof InvalidParameterException);
            assertEquals(e.getMessage(), "baseApiUrl cannot null or empty");
        }
    }

    @Test
    public void init_invalidUrlFormat_error() {
        PowerMockito.when(TextUtils.isEmpty(anyString())).thenReturn(false);
        try {
            RestClient.init(inValidURL);
        } catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
        }
    }

    @Test
    public void create_service_success() {
        PowerMockito.when(TextUtils.isEmpty(anyString())).thenReturn(false);
        RestClient.init(validURL);
        assertNotNull(RestClient.createService(UZService.class));
    }

    @Test
    public void create_service_without_init_failed() {
        PowerMockito.when(TextUtils.isEmpty(anyString())).thenReturn(false);
        try {
            RestClient.createService(UZService.class);
        } catch (Exception e) {
            assertTrue(e instanceof IllegalStateException);
            assertEquals(e.getMessage(), "Must call init() before use");
        }
    }

    @Test
    public void add_header_success() {
        PowerMockito.when(TextUtils.isEmpty(anyString())).thenReturn(false);
        RestClient.init(validURL);
        RestClient.addHeader(testHeader, testHeaderValue);
        assertTrue(RestClient.hasHeader(testHeader));
    }

    @Test
    public void remove_header_success() {
        PowerMockito.when(TextUtils.isEmpty(anyString())).thenReturn(false);
        RestClient.init(validURL);
        RestClient.addHeader(testHeader, testHeaderValue);
        RestClient.removeHeader(testHeader);
        assertFalse(RestClient.hasHeader(testHeader));
    }

    @Test
    public void remove_removeAuthorization_success(){
        PowerMockito.when(TextUtils.isEmpty(anyString())).thenReturn(false);
        RestClient.init(validURL);
        assertTrue(RestClient.hasHeader(authorizationHeader));
        RestClient.removeAuthorization();
        assertFalse(RestClient.hasHeader(authorizationHeader));
    }
}
