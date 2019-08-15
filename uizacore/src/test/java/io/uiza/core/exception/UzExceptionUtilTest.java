package io.uiza.core.exception;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

@RunWith(BlockJUnit4ClassRunner.class)
public class UzExceptionUtilTest {

    @Test
    public void getExceptionNoConnection() {
        UzException testException = UzExceptionUtil.getExceptionNoConnection();
        assertNotNull(testException);
        assertNotNull(testException.getMessage());
        assertNotNull(testException.getException());
        assertEquals(testException.getErrorCode(), UzException.ERR_CODE_0);
        assertTrue(testException.getMessage().contains(UzException.ERR_0));
        assertTrue(testException.getMessage().contains(String.valueOf(UzException.ERR_CODE_0)));
    }

    @Test
    public void getExceptionCannotGetDetailEntity() {
        UzException testException = UzExceptionUtil.getExceptionCannotGetDetailEntity();
        assertNotNull(testException);
        assertNotNull(testException.getMessage());
        assertNotNull(testException.getException());
        assertEquals(testException.getErrorCode(), UzException.ERR_CODE_1);
        assertTrue(testException.getMessage().contains(UzException.ERR_1));
        assertTrue(testException.getMessage().contains(String.valueOf(UzException.ERR_CODE_1)));
    }

    @Test
    public void getExceptionNoTokenStreaming() {
        UzException testException = UzExceptionUtil.getExceptionNoTokenStreaming();
        assertNotNull(testException);
        assertNotNull(testException.getMessage());
        assertNotNull(testException.getException());
        assertEquals(testException.getErrorCode(), UzException.ERR_CODE_2);
        assertTrue(testException.getMessage().contains(UzException.ERR_2));
        assertTrue(testException.getMessage().contains(String.valueOf(UzException.ERR_CODE_2)));
    }

    @Test
    public void getExceptionCannotGetLinkPlayLive() {
        UzException testException = UzExceptionUtil.getExceptionCannotGetLinkPlayLive();
        assertNotNull(testException);
        assertNotNull(testException.getMessage());
        assertNotNull(testException.getException());
        assertEquals(testException.getErrorCode(), UzException.ERR_CODE_3);
        assertTrue(testException.getMessage().contains(UzException.ERR_3));
        assertTrue(testException.getMessage().contains(String.valueOf(UzException.ERR_CODE_3)));
    }

    @Test
    public void getExceptionCannotGetLinkPlayVOD() {
        UzException testException = UzExceptionUtil.getExceptionCannotGetLinkPlayVOD();
        assertNotNull(testException);
        assertNotNull(testException.getMessage());
        assertNotNull(testException.getException());
        assertEquals(testException.getErrorCode(), UzException.ERR_CODE_4);
        assertTrue(testException.getMessage().contains(UzException.ERR_4));
        assertTrue(testException.getMessage().contains(String.valueOf(UzException.ERR_CODE_4)));
    }

    @Test
    public void getExceptionEntityId() {
        UzException testException = UzExceptionUtil.getExceptionEntityId();
        assertNotNull(testException);
        assertNotNull(testException.getMessage());
        assertNotNull(testException.getException());
        assertEquals(testException.getErrorCode(), UzException.ERR_CODE_5);
        assertTrue(testException.getMessage().contains(UzException.ERR_5));
        assertTrue(testException.getMessage().contains(String.valueOf(UzException.ERR_CODE_5)));
    }

    @Test
    public void getExceptionTryAllLinkPlay() {
        UzException testException = UzExceptionUtil.getExceptionTryAllLinkPlay();
        assertNotNull(testException);
        assertNotNull(testException.getMessage());
        assertNotNull(testException.getException());
        assertEquals(testException.getErrorCode(), UzException.ERR_CODE_6);
        assertTrue(testException.getMessage().contains(UzException.ERR_6));
        assertTrue(testException.getMessage().contains(String.valueOf(UzException.ERR_CODE_6)));
    }

    @Test
    public void getExceptionSetup() {
        UzException testException = UzExceptionUtil.getExceptionSetup();
        assertNotNull(testException);
        assertNotNull(testException.getMessage());
        assertNotNull(testException.getException());
        assertEquals(testException.getErrorCode(), UzException.ERR_CODE_7);
        assertTrue(testException.getMessage().contains(UzException.ERR_7));
        assertTrue(testException.getMessage().contains(String.valueOf(UzException.ERR_CODE_7)));
    }

    @Test
    public void getExceptionListAllEntity() {
        UzException testException = UzExceptionUtil.getExceptionListAllEntity();
        assertNotNull(testException);
        assertNotNull(testException.getMessage());
        assertNotNull(testException.getException());
        assertEquals(testException.getErrorCode(), UzException.ERR_CODE_8);
        assertTrue(testException.getMessage().contains(UzException.ERR_8));
        assertTrue(testException.getMessage().contains(String.valueOf(UzException.ERR_CODE_8)));
    }

    @Test
    public void getExceptionChangeSkin() {
        UzException testException = UzExceptionUtil.getExceptionChangeSkin();
        assertNotNull(testException);
        assertNotNull(testException.getMessage());
        assertNotNull(testException.getException());
        assertEquals(testException.getErrorCode(), UzException.ERR_CODE_9);
        assertTrue(testException.getMessage().contains(UzException.ERR_9));
        assertTrue(testException.getMessage().contains(String.valueOf(UzException.ERR_CODE_9)));
    }

    @Test
    public void getExceptionListHQ() {
        UzException testException = UzExceptionUtil.getExceptionListHQ();
        assertNotNull(testException);
        assertNotNull(testException.getMessage());
        assertNotNull(testException.getException());
        assertEquals(testException.getErrorCode(), UzException.ERR_CODE_10);
        assertTrue(testException.getMessage().contains(UzException.ERR_10));
        assertTrue(testException.getMessage().contains(String.valueOf(UzException.ERR_CODE_10)));
    }

    @Test
    public void getExceptionListAudio() {
        UzException testException = UzExceptionUtil.getExceptionListAudio();
        assertNotNull(testException);
        assertNotNull(testException.getMessage());
        assertNotNull(testException.getException());
        assertEquals(testException.getErrorCode(), UzException.ERR_CODE_11);
        assertTrue(testException.getMessage().contains(UzException.ERR_11));
        assertTrue(testException.getMessage().contains(String.valueOf(UzException.ERR_CODE_11)));
    }

    @Test
    public void getExceptionShowPip() {
        UzException testException = UzExceptionUtil.getExceptionShowPip();
        assertNotNull(testException);
        assertNotNull(testException.getMessage());
        assertNotNull(testException.getException());
        assertEquals(testException.getErrorCode(), UzException.ERR_CODE_19);
        assertTrue(testException.getMessage().contains(UzException.ERR_19));
        assertTrue(testException.getMessage().contains(String.valueOf(UzException.ERR_CODE_19)));
    }

    @Test
    public void getExceptionRetrieveALiveEvent() {
        UzException testException = UzExceptionUtil.getExceptionRetrieveALiveEvent();
        assertNotNull(testException);
        assertNotNull(testException.getMessage());
        assertNotNull(testException.getException());
        assertEquals(testException.getErrorCode(), UzException.ERR_CODE_21);
        assertTrue(testException.getMessage().contains(UzException.ERR_21));
        assertTrue(testException.getMessage().contains(String.valueOf(UzException.ERR_CODE_21)));
    }

    @Test
    public void getExceptionPlayback() {
        UzException testException = UzExceptionUtil.getExceptionPlayback();
        assertNotNull(testException);
        assertNotNull(testException.getMessage());
        assertNotNull(testException.getException());
        assertEquals(testException.getErrorCode(), UzException.ERR_CODE_24);
        assertTrue(testException.getMessage().contains(UzException.ERR_24));
        assertTrue(testException.getMessage().contains(String.valueOf(UzException.ERR_CODE_24)));
    }

    @Test
    public void getExceptionPlaylistFolderItemFirst() {
        UzException testException = UzExceptionUtil.getExceptionPlaylistFolderItemFirst();
        assertNotNull(testException);
        assertNotNull(testException.getMessage());
        assertNotNull(testException.getException());
        assertEquals(testException.getErrorCode(), UzException.ERR_CODE_25);
        assertTrue(testException.getMessage().contains(UzException.ERR_25));
        assertTrue(testException.getMessage().contains(String.valueOf(UzException.ERR_CODE_25)));
    }

    @Test
    public void getExceptionPlaylistFolderItemLast() {
        UzException testException = UzExceptionUtil.getExceptionPlaylistFolderItemLast();
        assertNotNull(testException);
        assertNotNull(testException.getMessage());
        assertNotNull(testException.getException());
        assertEquals(testException.getErrorCode(), UzException.ERR_CODE_26);
        assertTrue(testException.getMessage().contains(UzException.ERR_26));
        assertTrue(testException.getMessage().contains(String.valueOf(UzException.ERR_CODE_26)));
    }

    @Test
    public void getExceptionPlayerInfor() {
        UzException testException = UzExceptionUtil.getExceptionPlayerInfo();
        assertNotNull(testException);
        assertNotNull(testException.getMessage());
        assertNotNull(testException.getException());
        assertEquals(testException.getErrorCode(), UzException.ERR_CODE_27);
        assertTrue(testException.getMessage().contains(UzException.ERR_27));
        assertTrue(testException.getMessage().contains(String.valueOf(UzException.ERR_CODE_27)));
    }
}
