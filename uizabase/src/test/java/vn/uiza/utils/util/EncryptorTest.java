package vn.uiza.utils.util;

import android.util.Base64;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertNotEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.times;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Base64.class)
@PowerMockIgnore({"javax.crypto.*"})
public class EncryptorTest {

    // TODO: Add test decrypt method

    @Before
    public void setup() {
        PowerMockito.mockStatic(Base64.class);
    }

    @Test
    public void test_encrypt_without_exception() {
        String normalString = "This is my test string";
        String key = "1234567890123456";
        String vector = "1234567890123456";

        PowerMockito.when(Base64.encodeToString(any(byte[].class), anyInt())).thenReturn("");

        String realEncryptedString = Encryptor.encrypt(key, vector, normalString);
        assertNotEquals(realEncryptedString, normalString);

        PowerMockito.verifyStatic(Base64.class, times(1));
        Base64.encodeToString(any(byte[].class), anyInt());
    }
}
