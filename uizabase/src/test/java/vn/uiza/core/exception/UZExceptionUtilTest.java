package vn.uiza.core.exception;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(BlockJUnit4ClassRunner.class)
public class UZExceptionUtilTest {

    @Test
    public void getExceptionNoConnection() {
        UZException testException = UZExceptionUtil.getExceptionNoConnection();
        assertNotNull(testException);
        assertNotNull(testException.getMessage());
        assertNotNull(testException.getException());
        assertEquals(testException.getErrorCode(), UZException.ERR_CODE_0);
        assertTrue(testException.getMessage().contains(UZException.ERR_0));
        assertTrue(testException.getMessage().contains(String.valueOf(UZException.ERR_CODE_0)));
    }

    @Test
    public void getExceptionCannotGetDetailEntity() {
        UZException testException = UZExceptionUtil.getExceptionCannotGetDetailEntity();
        assertNotNull(testException);
        assertNotNull(testException.getMessage());
        assertNotNull(testException.getException());
        assertEquals(testException.getErrorCode(), UZException.ERR_CODE_1);
        assertTrue(testException.getMessage().contains(UZException.ERR_1));
        assertTrue(testException.getMessage().contains(String.valueOf(UZException.ERR_CODE_1)));
    }

    @Test
    public void getExceptionNoTokenStreaming() {
        UZException testException = UZExceptionUtil.getExceptionNoTokenStreaming();
        assertNotNull(testException);
        assertNotNull(testException.getMessage());
        assertNotNull(testException.getException());
        assertEquals(testException.getErrorCode(), UZException.ERR_CODE_2);
        assertTrue(testException.getMessage().contains(UZException.ERR_2));
        assertTrue(testException.getMessage().contains(String.valueOf(UZException.ERR_CODE_2)));
    }

    @Test
    public void getExceptionCannotGetLinkPlayLive() {
        UZException testException = UZExceptionUtil.getExceptionCannotGetLinkPlayLive();
        assertNotNull(testException);
        assertNotNull(testException.getMessage());
        assertNotNull(testException.getException());
        assertEquals(testException.getErrorCode(), UZException.ERR_CODE_3);
        assertTrue(testException.getMessage().contains(UZException.ERR_3));
        assertTrue(testException.getMessage().contains(String.valueOf(UZException.ERR_CODE_3)));
    }

    @Test
    public void getExceptionCannotGetLinkPlayVOD() {
        UZException testException = UZExceptionUtil.getExceptionCannotGetLinkPlayVOD();
        assertNotNull(testException);
        assertNotNull(testException.getMessage());
        assertNotNull(testException.getException());
        assertEquals(testException.getErrorCode(), UZException.ERR_CODE_4);
        assertTrue(testException.getMessage().contains(UZException.ERR_4));
        assertTrue(testException.getMessage().contains(String.valueOf(UZException.ERR_CODE_4)));
    }

    @Test
    public void getExceptionEntityId() {
        UZException testException = UZExceptionUtil.getExceptionEntityId();
        assertNotNull(testException);
        assertNotNull(testException.getMessage());
        assertNotNull(testException.getException());
        assertEquals(testException.getErrorCode(), UZException.ERR_CODE_5);
        assertTrue(testException.getMessage().contains(UZException.ERR_5));
        assertTrue(testException.getMessage().contains(String.valueOf(UZException.ERR_CODE_5)));
    }

    @Test
    public void getExceptionTryAllLinkPlay() {
        UZException testException = UZExceptionUtil.getExceptionTryAllLinkPlay();
        assertNotNull(testException);
        assertNotNull(testException.getMessage());
        assertNotNull(testException.getException());
        assertEquals(testException.getErrorCode(), UZException.ERR_CODE_6);
        assertTrue(testException.getMessage().contains(UZException.ERR_6));
        assertTrue(testException.getMessage().contains(String.valueOf(UZException.ERR_CODE_6)));
    }

    @Test
    public void getExceptionSetup() {
        UZException testException = UZExceptionUtil.getExceptionSetup();
        assertNotNull(testException);
        assertNotNull(testException.getMessage());
        assertNotNull(testException.getException());
        assertEquals(testException.getErrorCode(), UZException.ERR_CODE_7);
        assertTrue(testException.getMessage().contains(UZException.ERR_7));
        assertTrue(testException.getMessage().contains(String.valueOf(UZException.ERR_CODE_7)));
    }

    @Test
    public void getExceptionListAllEntity() {
        UZException testException = UZExceptionUtil.getExceptionListAllEntity();
        assertNotNull(testException);
        assertNotNull(testException.getMessage());
        assertNotNull(testException.getException());
        assertEquals(testException.getErrorCode(), UZException.ERR_CODE_8);
        assertTrue(testException.getMessage().contains(UZException.ERR_8));
        assertTrue(testException.getMessage().contains(String.valueOf(UZException.ERR_CODE_8)));
    }

    @Test
    public void getExceptionChangeSkin() {
        UZException testException = UZExceptionUtil.getExceptionChangeSkin();
        assertNotNull(testException);
        assertNotNull(testException.getMessage());
        assertNotNull(testException.getException());
        assertEquals(testException.getErrorCode(), UZException.ERR_CODE_9);
        assertTrue(testException.getMessage().contains(UZException.ERR_9));
        assertTrue(testException.getMessage().contains(String.valueOf(UZException.ERR_CODE_9)));
    }

    @Test
    public void getExceptionListHQ() {
        UZException testException = UZExceptionUtil.getExceptionListHQ();
        assertNotNull(testException);
        assertNotNull(testException.getMessage());
        assertNotNull(testException.getException());
        assertEquals(testException.getErrorCode(), UZException.ERR_CODE_10);
        assertTrue(testException.getMessage().contains(UZException.ERR_10));
        assertTrue(testException.getMessage().contains(String.valueOf(UZException.ERR_CODE_10)));
    }

    @Test
    public void getExceptionListAudio() {
        UZException testException = UZExceptionUtil.getExceptionListAudio();
        assertNotNull(testException);
        assertNotNull(testException.getMessage());
        assertNotNull(testException.getException());
        assertEquals(testException.getErrorCode(), UZException.ERR_CODE_11);
        assertTrue(testException.getMessage().contains(UZException.ERR_11));
        assertTrue(testException.getMessage().contains(String.valueOf(UZException.ERR_CODE_11)));
    }

    @Test
    public void getExceptionShowPip() {
        UZException testException = UZExceptionUtil.getExceptionShowPip();
        assertNotNull(testException);
        assertNotNull(testException.getMessage());
        assertNotNull(testException.getException());
        assertEquals(testException.getErrorCode(), UZException.ERR_CODE_19);
        assertTrue(testException.getMessage().contains(UZException.ERR_19));
        assertTrue(testException.getMessage().contains(String.valueOf(UZException.ERR_CODE_19)));
    }

    @Test
    public void getExceptionRetrieveALiveEvent() {
        UZException testException = UZExceptionUtil.getExceptionRetrieveALiveEvent();
        assertNotNull(testException);
        assertNotNull(testException.getMessage());
        assertNotNull(testException.getException());
        assertEquals(testException.getErrorCode(), UZException.ERR_CODE_21);
        assertTrue(testException.getMessage().contains(UZException.ERR_21));
        assertTrue(testException.getMessage().contains(String.valueOf(UZException.ERR_CODE_21)));
    }

    @Test
    public void getExceptionPlayback() {
        UZException testException = UZExceptionUtil.getExceptionPlayback();
        assertNotNull(testException);
        assertNotNull(testException.getMessage());
        assertNotNull(testException.getException());
        assertEquals(testException.getErrorCode(), UZException.ERR_CODE_24);
        assertTrue(testException.getMessage().contains(UZException.ERR_24));
        assertTrue(testException.getMessage().contains(String.valueOf(UZException.ERR_CODE_24)));
    }

    @Test
    public void getExceptionPlaylistFolderItemFirst() {
        UZException testException = UZExceptionUtil.getExceptionPlaylistFolderItemFirst();
        assertNotNull(testException);
        assertNotNull(testException.getMessage());
        assertNotNull(testException.getException());
        assertEquals(testException.getErrorCode(), UZException.ERR_CODE_25);
        assertTrue(testException.getMessage().contains(UZException.ERR_25));
        assertTrue(testException.getMessage().contains(String.valueOf(UZException.ERR_CODE_25)));
    }

    @Test
    public void getExceptionPlaylistFolderItemLast() {
        UZException testException = UZExceptionUtil.getExceptionPlaylistFolderItemLast();
        assertNotNull(testException);
        assertNotNull(testException.getMessage());
        assertNotNull(testException.getException());
        assertEquals(testException.getErrorCode(), UZException.ERR_CODE_26);
        assertTrue(testException.getMessage().contains(UZException.ERR_26));
        assertTrue(testException.getMessage().contains(String.valueOf(UZException.ERR_CODE_26)));
    }

    @Test
    public void getExceptionPlayerInfor() {
        UZException testException = UZExceptionUtil.getExceptionPlayerInfor();
        assertNotNull(testException);
        assertNotNull(testException.getMessage());
        assertNotNull(testException.getException());
        assertEquals(testException.getErrorCode(), UZException.ERR_CODE_27);
        assertTrue(testException.getMessage().contains(UZException.ERR_27));
        assertTrue(testException.getMessage().contains(String.valueOf(UZException.ERR_CODE_27)));
    }
}
