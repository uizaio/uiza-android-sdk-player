package io.uiza.core.api.restclient;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;

import android.text.TextUtils;
import io.uiza.core.api.UzServiceApi;
import io.uiza.core.api.client.UzRestClientGetLinkPlay;
import java.security.InvalidParameterException;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;

public class UzRestClientGetLinkPlayTest extends BaseRestClientTest {

    @Test
    public void initSuccess() {
        PowerMockito.when(TextUtils.isEmpty(anyString())).thenReturn(false);

        UzRestClientGetLinkPlay.init(validURL);

        TextUtils.isEmpty(validURL);
        UzRestClientGetLinkPlay.addAuthorization("");

        assertNotNull(UzRestClientGetLinkPlay.getRetrofit());
        assertTrue(UzRestClientGetLinkPlay.hasHeader(authorizationHeader));
    }

    @Test
    public void init_emptyBaseApiUrl_error() {
        PowerMockito.when(TextUtils.isEmpty(anyString())).thenReturn(true);

        try {
            UzRestClientGetLinkPlay.init(validURL);
        } catch (Exception e) {
            assertTrue(e instanceof InvalidParameterException);
            assertEquals(e.getMessage(), "baseApiUrl cannot null or empty");
        }
    }

    @Test
    public void init_invalidUrlFormat_error() {
        PowerMockito.when(TextUtils.isEmpty(anyString())).thenReturn(false);
        try {
            UzRestClientGetLinkPlay.init(inValidURL);
        } catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
        }
    }

    @Test
    public void create_service_success() {
        PowerMockito.when(TextUtils.isEmpty(anyString())).thenReturn(false);
        UzRestClientGetLinkPlay.init(validURL);
        assertNotNull(UzRestClientGetLinkPlay.createService(UzServiceApi.class));
    }

    @Test
    public void create_service_without_init_failed() {
        PowerMockito.when(TextUtils.isEmpty(anyString())).thenReturn(false);
        try {
            UzRestClientGetLinkPlay.createService(UzServiceApi.class);
        } catch (Exception e) {
            assertTrue(e instanceof IllegalStateException);
            assertEquals(e.getMessage(), "Must call init() before use");
        }
    }

    @Test
    public void add_header_success() {
        PowerMockito.when(TextUtils.isEmpty(anyString())).thenReturn(false);
        UzRestClientGetLinkPlay.init(validURL);
        UzRestClientGetLinkPlay.addHeader(testHeader, testHeaderValue);
        assertTrue(UzRestClientGetLinkPlay.hasHeader(testHeader));
    }

    @Test
    public void remove_header_success() {
        PowerMockito.when(TextUtils.isEmpty(anyString())).thenReturn(false);
        UzRestClientGetLinkPlay.init(validURL);
        UzRestClientGetLinkPlay.addHeader(testHeader, testHeaderValue);
        UzRestClientGetLinkPlay.removeHeader(testHeader);
        assertFalse(UzRestClientGetLinkPlay.hasHeader(testHeader));
    }

    @Test
    public void remove_removeAuthorization_success(){
        PowerMockito.when(TextUtils.isEmpty(anyString())).thenReturn(false);
        UzRestClientGetLinkPlay.init(validURL);
        assertTrue(UzRestClientGetLinkPlay.hasHeader(authorizationHeader));
        UzRestClientGetLinkPlay.removeAuthorization();
        assertFalse(UzRestClientGetLinkPlay.hasHeader(authorizationHeader));
    }
}
