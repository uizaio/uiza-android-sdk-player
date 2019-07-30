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

public class UZRestClientGetLinkPlayTest extends BaseRestClientTest {

    @Test
    public void initSuccess() {
        PowerMockito.when(TextUtils.isEmpty(anyString())).thenReturn(false);

        UZRestClientGetLinkPlay.init(validURL);

        TextUtils.isEmpty(validURL);
        UZRestClientGetLinkPlay.addAuthorization("");

        assertNotNull(UZRestClientGetLinkPlay.getRetrofit());
        assertTrue(UZRestClientGetLinkPlay.hasHeader(authorizationHeader));
    }

    @Test
    public void init_emptyBaseApiUrl_error() {
        PowerMockito.when(TextUtils.isEmpty(anyString())).thenReturn(true);

        try {
            UZRestClientGetLinkPlay.init(validURL);
        } catch (Exception e) {
            assertTrue(e instanceof InvalidParameterException);
            assertEquals(e.getMessage(), "baseApiUrl cannot null or empty");
        }
    }

    @Test
    public void init_invalidUrlFormat_error() {
        PowerMockito.when(TextUtils.isEmpty(anyString())).thenReturn(false);
        try {
            UZRestClientGetLinkPlay.init(inValidURL);
        } catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
        }
    }

    @Test
    public void create_service_success() {
        PowerMockito.when(TextUtils.isEmpty(anyString())).thenReturn(false);
        UZRestClientGetLinkPlay.init(validURL);
        assertNotNull(UZRestClientGetLinkPlay.createService(UZService.class));
    }

    @Test
    public void create_service_without_init_failed() {
        PowerMockito.when(TextUtils.isEmpty(anyString())).thenReturn(false);
        try {
            UZRestClientGetLinkPlay.createService(UZService.class);
        } catch (Exception e) {
            assertTrue(e instanceof IllegalStateException);
            assertEquals(e.getMessage(), "Must call init() before use");
        }
    }

    @Test
    public void add_header_success() {
        PowerMockito.when(TextUtils.isEmpty(anyString())).thenReturn(false);
        UZRestClientGetLinkPlay.init(validURL);
        UZRestClientGetLinkPlay.addHeader(testHeader, testHeaderValue);
        assertTrue(UZRestClientGetLinkPlay.hasHeader(testHeader));
    }

    @Test
    public void remove_header_success() {
        PowerMockito.when(TextUtils.isEmpty(anyString())).thenReturn(false);
        UZRestClientGetLinkPlay.init(validURL);
        UZRestClientGetLinkPlay.addHeader(testHeader, testHeaderValue);
        UZRestClientGetLinkPlay.removeHeader(testHeader);
        assertFalse(UZRestClientGetLinkPlay.hasHeader(testHeader));
    }

    @Test
    public void remove_removeAuthorization_success(){
        PowerMockito.when(TextUtils.isEmpty(anyString())).thenReturn(false);
        UZRestClientGetLinkPlay.init(validURL);
        assertTrue(UZRestClientGetLinkPlay.hasHeader(authorizationHeader));
        UZRestClientGetLinkPlay.removeAuthorization();
        assertFalse(UZRestClientGetLinkPlay.hasHeader(authorizationHeader));
    }
}
