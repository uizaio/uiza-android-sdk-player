package uizacoresdk.util;

import android.content.Context;
import android.content.SharedPreferences;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
public class UizaTrackingUtilTest {
    @Mock
    Context context;
    @Mock
    SharedPreferences sharedPreferences;
    @Mock
    SharedPreferences.Editor editor;

    private boolean defaultReturnBooleanValue = true;

    @Before
    public void setup() {
        Mockito.when(context.getSharedPreferences(anyString(), anyInt()))
                .thenReturn(sharedPreferences);
        Mockito.when(sharedPreferences.edit()).thenReturn(editor);
        Mockito.when(sharedPreferences.getBoolean(anyString(), anyBoolean()))
                .thenReturn(defaultReturnBooleanValue);
        Mockito.when(sharedPreferences.getBoolean(anyString(), anyBoolean()))
                .thenReturn(defaultReturnBooleanValue);
    }

    @Test
    public void isTrackedEventTypeDisplay() {
        boolean expected = UizaTrackingUtil.isTrackedEventTypeDisplay(context);
        Mockito.verify(sharedPreferences).getBoolean(anyString(), anyBoolean());
        assertTrue(expected);
    }

    @Test
    public void setTrackingDoneWithEventTypeDisplay() {
        UizaTrackingUtil.setTrackingDoneWithEventTypeDisplay(context, true);
        Mockito.verify(sharedPreferences).edit();
        Mockito.verify(editor).putBoolean(anyString(), anyBoolean());
        Mockito.verify(editor).apply();
    }

    @Test
    public void isTrackedEventTypePlaysRequested() {
        boolean expected = UizaTrackingUtil.isTrackedEventTypePlaysRequested(context);
        Mockito.verify(sharedPreferences).getBoolean(anyString(), anyBoolean());
        assertTrue(expected);
    }

    @Test
    public void setTrackingDoneWithEventTypePlaysRequested() {
        UizaTrackingUtil.setTrackingDoneWithEventTypePlaysRequested(context, true);
        Mockito.verify(sharedPreferences).edit();
        Mockito.verify(editor).putBoolean(anyString(), anyBoolean());
        Mockito.verify(editor).apply();
    }

    @Test
    public void isTrackedEventTypeVideoStarts() {
        boolean expected = UizaTrackingUtil.isTrackedEventTypeVideoStarts(context);
        Mockito.verify(sharedPreferences).getBoolean(anyString(), anyBoolean());
        assertTrue(expected);
    }

    @Test
    public void setTrackingDoneWithEventTypeVideoStarts() {
        UizaTrackingUtil.setTrackingDoneWithEventTypeVideoStarts(context, true);
        Mockito.verify(sharedPreferences).edit();
        Mockito.verify(editor).putBoolean(anyString(), anyBoolean());
        Mockito.verify(editor).apply();
    }

    @Test
    public void isTrackedEventTypeView() {
        boolean expected = UizaTrackingUtil.isTrackedEventTypeView(context);
        Mockito.verify(sharedPreferences).getBoolean(anyString(), anyBoolean());
        assertTrue(expected);
    }

    @Test
    public void setTrackingDoneWithEventTypeView() {
        UizaTrackingUtil.setTrackingDoneWithEventTypeView(context, true);
        Mockito.verify(sharedPreferences).edit();
        Mockito.verify(editor).putBoolean(anyString(), anyBoolean());
        Mockito.verify(editor).apply();
    }

    @Test
    public void isTrackedEventTypeReplay() {
        boolean expected = UizaTrackingUtil.isTrackedEventTypeReplay(context);
        Mockito.verify(sharedPreferences).getBoolean(anyString(), anyBoolean());
        assertTrue(expected);
    }

    @Test
    public void setTrackingDoneWithEventTypeReplay() {
        UizaTrackingUtil.setTrackingDoneWithEventTypeReplay(context, true);
        Mockito.verify(sharedPreferences).edit();
        Mockito.verify(editor).putBoolean(anyString(), anyBoolean());
        Mockito.verify(editor).apply();
    }

    @Test
    public void isTrackedEventTypePlayThrought25() {
        boolean expected = UizaTrackingUtil.isTrackedEventTypePlayThrought25(context);
        Mockito.verify(sharedPreferences).getBoolean(anyString(), anyBoolean());
        assertTrue(expected);
    }

    @Test
    public void setTrackingDoneWithEventTypePlayThrought25() {
        UizaTrackingUtil.setTrackingDoneWithEventTypePlayThrought25(context, true);
        Mockito.verify(sharedPreferences).edit();
        Mockito.verify(editor).putBoolean(anyString(), anyBoolean());
        Mockito.verify(editor).apply();
    }

    @Test
    public void isTrackedEventTypePlayThrought50() {
        boolean expected = UizaTrackingUtil.isTrackedEventTypePlayThrought50(context);
        Mockito.verify(sharedPreferences).getBoolean(anyString(), anyBoolean());
        assertTrue(expected);
    }

    @Test
    public void setTrackingDoneWithEventTypePlayThrought50() {
        UizaTrackingUtil.setTrackingDoneWithEventTypePlayThrought50(context, true);
        Mockito.verify(sharedPreferences).edit();
        Mockito.verify(editor).putBoolean(anyString(), anyBoolean());
        Mockito.verify(editor).apply();
    }

    @Test
    public void isTrackedEventTypePlayThrought75() {
        boolean expected = UizaTrackingUtil.isTrackedEventTypePlayThrought75(context);
        Mockito.verify(sharedPreferences).getBoolean(anyString(), anyBoolean());
        assertTrue(expected);
    }

    @Test
    public void setTrackingDoneWithEventTypePlayThrought75() {
        UizaTrackingUtil.setTrackingDoneWithEventTypePlayThrought75(context, true);
        Mockito.verify(sharedPreferences).edit();
        Mockito.verify(editor).putBoolean(anyString(), anyBoolean());
        Mockito.verify(editor).apply();
    }

    @Test
    public void isTrackedEventTypePlayThrought100() {
        boolean expected = UizaTrackingUtil.isTrackedEventTypePlayThrought100(context);
        Mockito.verify(sharedPreferences).getBoolean(anyString(), anyBoolean());
        assertTrue(expected);
    }

    @Test
    public void setTrackingDoneWithEventTypePlayThrought100() {
        UizaTrackingUtil.setTrackingDoneWithEventTypePlayThrought100(context, true);
        Mockito.verify(sharedPreferences).edit();
        Mockito.verify(editor).putBoolean(anyString(), anyBoolean());
        Mockito.verify(editor).apply();
    }

    @Test
    public void clearAllValues() {
        UizaTrackingUtil.clearAllValues(context);
        Mockito.verify(sharedPreferences, times(9)).edit();
        Mockito.verify(editor, times(9)).putBoolean(anyString(), anyBoolean());
        Mockito.verify(editor, times(9)).apply();
    }
}
