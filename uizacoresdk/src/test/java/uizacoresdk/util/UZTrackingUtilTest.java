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
public class UZTrackingUtilTest {
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
        boolean expected = UZTrackingUtil.isTrackedEventTypeDisplay(context);
        Mockito.verify(sharedPreferences).getBoolean(anyString(), anyBoolean());
        assertTrue(expected);
    }

    @Test
    public void setTrackingDoneWithEventTypeDisplay() {
        UZTrackingUtil.setTrackingDoneWithEventTypeDisplay(context, true);
        Mockito.verify(sharedPreferences).edit();
        Mockito.verify(editor).putBoolean(anyString(), anyBoolean());
        Mockito.verify(editor).apply();
    }

    @Test
    public void isTrackedEventTypePlaysRequested() {
        boolean expected = UZTrackingUtil.isTrackedEventTypePlaysRequested(context);
        Mockito.verify(sharedPreferences).getBoolean(anyString(), anyBoolean());
        assertTrue(expected);
    }

    @Test
    public void setTrackingDoneWithEventTypePlaysRequested() {
        UZTrackingUtil.setTrackingDoneWithEventTypePlaysRequested(context, true);
        Mockito.verify(sharedPreferences).edit();
        Mockito.verify(editor).putBoolean(anyString(), anyBoolean());
        Mockito.verify(editor).apply();
    }

    @Test
    public void isTrackedEventTypeVideoStarts() {
        boolean expected = UZTrackingUtil.isTrackedEventTypeVideoStarts(context);
        Mockito.verify(sharedPreferences).getBoolean(anyString(), anyBoolean());
        assertTrue(expected);
    }

    @Test
    public void setTrackingDoneWithEventTypeVideoStarts() {
        UZTrackingUtil.setTrackingDoneWithEventTypeVideoStarts(context, true);
        Mockito.verify(sharedPreferences).edit();
        Mockito.verify(editor).putBoolean(anyString(), anyBoolean());
        Mockito.verify(editor).apply();
    }

    @Test
    public void isTrackedEventTypeView() {
        boolean expected = UZTrackingUtil.isTrackedEventTypeView(context);
        Mockito.verify(sharedPreferences).getBoolean(anyString(), anyBoolean());
        assertTrue(expected);
    }

    @Test
    public void setTrackingDoneWithEventTypeView() {
        UZTrackingUtil.setTrackingDoneWithEventTypeView(context, true);
        Mockito.verify(sharedPreferences).edit();
        Mockito.verify(editor).putBoolean(anyString(), anyBoolean());
        Mockito.verify(editor).apply();
    }

    @Test
    public void isTrackedEventTypeReplay() {
        boolean expected = UZTrackingUtil.isTrackedEventTypeReplay(context);
        Mockito.verify(sharedPreferences).getBoolean(anyString(), anyBoolean());
        assertTrue(expected);
    }

    @Test
    public void setTrackingDoneWithEventTypeReplay() {
        UZTrackingUtil.setTrackingDoneWithEventTypeReplay(context, true);
        Mockito.verify(sharedPreferences).edit();
        Mockito.verify(editor).putBoolean(anyString(), anyBoolean());
        Mockito.verify(editor).apply();
    }

    @Test
    public void isTrackedEventTypePlayThrought25() {
        boolean expected = UZTrackingUtil.isTrackedEventTypePlayThrought25(context);
        Mockito.verify(sharedPreferences).getBoolean(anyString(), anyBoolean());
        assertTrue(expected);
    }

    @Test
    public void setTrackingDoneWithEventTypePlayThrought25() {
        UZTrackingUtil.setTrackingDoneWithEventTypePlayThrought25(context, true);
        Mockito.verify(sharedPreferences).edit();
        Mockito.verify(editor).putBoolean(anyString(), anyBoolean());
        Mockito.verify(editor).apply();
    }

    @Test
    public void isTrackedEventTypePlayThrought50() {
        boolean expected = UZTrackingUtil.isTrackedEventTypePlayThrought50(context);
        Mockito.verify(sharedPreferences).getBoolean(anyString(), anyBoolean());
        assertTrue(expected);
    }

    @Test
    public void setTrackingDoneWithEventTypePlayThrought50() {
        UZTrackingUtil.setTrackingDoneWithEventTypePlayThrought50(context, true);
        Mockito.verify(sharedPreferences).edit();
        Mockito.verify(editor).putBoolean(anyString(), anyBoolean());
        Mockito.verify(editor).apply();
    }

    @Test
    public void isTrackedEventTypePlayThrought75() {
        boolean expected = UZTrackingUtil.isTrackedEventTypePlayThrought75(context);
        Mockito.verify(sharedPreferences).getBoolean(anyString(), anyBoolean());
        assertTrue(expected);
    }

    @Test
    public void setTrackingDoneWithEventTypePlayThrought75() {
        UZTrackingUtil.setTrackingDoneWithEventTypePlayThrought75(context, true);
        Mockito.verify(sharedPreferences).edit();
        Mockito.verify(editor).putBoolean(anyString(), anyBoolean());
        Mockito.verify(editor).apply();
    }

    @Test
    public void isTrackedEventTypePlayThrought100() {
        boolean expected = UZTrackingUtil.isTrackedEventTypePlayThrought100(context);
        Mockito.verify(sharedPreferences).getBoolean(anyString(), anyBoolean());
        assertTrue(expected);
    }

    @Test
    public void setTrackingDoneWithEventTypePlayThrought100() {
        UZTrackingUtil.setTrackingDoneWithEventTypePlayThrought100(context, true);
        Mockito.verify(sharedPreferences).edit();
        Mockito.verify(editor).putBoolean(anyString(), anyBoolean());
        Mockito.verify(editor).apply();
    }

    @Test
    public void clearAllValues() {
        UZTrackingUtil.clearAllValues(context);
        Mockito.verify(sharedPreferences, times(9)).edit();
        Mockito.verify(editor, times(9)).putBoolean(anyString(), anyBoolean());
        Mockito.verify(editor, times(9)).apply();
    }
}
