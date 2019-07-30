package vn.uiza.restapi.restclient;

import android.text.TextUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(TextUtils.class)
@PowerMockIgnore("javax.net.ssl.*")
@Ignore(value = "This is base class, and no testcase here")
public class BaseRestClientTest {
    String validURL = "https://uiza.io";
    String inValidURL = "uiza.io";
    String authorizationHeader = "Authorization";
    String accessToken = "AccessToken";
    String testHeader = "TEST";
    String testHeaderValue = "TEST_VALUE";
    @Before
    public void setup() {
        PowerMockito.mockStatic(TextUtils.class);
    }
}
