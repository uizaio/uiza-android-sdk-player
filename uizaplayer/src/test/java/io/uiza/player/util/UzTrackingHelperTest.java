package io.uiza.player.util;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;

import android.content.Context;
import android.content.SharedPreferences;
import io.uiza.player.analytic.event.EventTrackingManager;
import io.uiza.player.analytic.event.TrackingEvent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UzTrackingHelperTest {
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
        boolean expected = EventTrackingManager.isEventTracked(context, TrackingEvent.E_DISPLAY);
        Mockito.verify(sharedPreferences).getBoolean(anyString(), anyBoolean());
        assertTrue(expected);
    }

    @Test
    public void setTrackingDoneWithEventTypeDisplay() {
        EventTrackingManager.markEventTracked(context, TrackingEvent.E_DISPLAY, true);
        Mockito.verify(sharedPreferences).edit();
        Mockito.verify(editor).putBoolean(anyString(), anyBoolean());
        Mockito.verify(editor).apply();
    }

    @Test
    public void isTrackedEventTypePlaysRequested() {
        boolean expected = EventTrackingManager.isTrackedEventTypePlaysRequested(context);
        Mockito.verify(sharedPreferences).getBoolean(anyString(), anyBoolean());
        assertTrue(expected);
    }

    @Test
    public void setTrackingDoneWithEventTypePlaysRequested() {
        EventTrackingManager.setTrackingDoneWithEventTypePlaysRequested(context, true);
        Mockito.verify(sharedPreferences).edit();
        Mockito.verify(editor).putBoolean(anyString(), anyBoolean());
        Mockito.verify(editor).apply();
    }

    @Test
    public void isTrackedEventTypeVideoStarts() {
        boolean expected = EventTrackingManager.isTrackedEventTypeVideoStarts(context);
        Mockito.verify(sharedPreferences).getBoolean(anyString(), anyBoolean());
        assertTrue(expected);
    }

    @Test
    public void setTrackingDoneWithEventTypeVideoStarts() {
        EventTrackingManager.setTrackingDoneWithEventTypeVideoStarts(context, true);
        Mockito.verify(sharedPreferences).edit();
        Mockito.verify(editor).putBoolean(anyString(), anyBoolean());
        Mockito.verify(editor).apply();
    }

    @Test
    public void isTrackedEventTypeView() {
        boolean expected = EventTrackingManager.isTrackedEventTypeView(context);
        Mockito.verify(sharedPreferences).getBoolean(anyString(), anyBoolean());
        assertTrue(expected);
    }

    @Test
    public void setTrackingDoneWithEventTypeView() {
        EventTrackingManager.setTrackingDoneWithEventTypeView(context, true);
        Mockito.verify(sharedPreferences).edit();
        Mockito.verify(editor).putBoolean(anyString(), anyBoolean());
        Mockito.verify(editor).apply();
    }

    @Test
    public void isTrackedEventTypeReplay() {
        boolean expected = EventTrackingManager.isTrackedEventTypeReplay(context);
        Mockito.verify(sharedPreferences).getBoolean(anyString(), anyBoolean());
        assertTrue(expected);
    }

    @Test
    public void setTrackingDoneWithEventTypeReplay() {
        EventTrackingManager.setTrackingDoneWithEventTypeReplay(context, true);
        Mockito.verify(sharedPreferences).edit();
        Mockito.verify(editor).putBoolean(anyString(), anyBoolean());
        Mockito.verify(editor).apply();
    }

    @Test
    public void isTrackedEventTypePlayThrought25() {
        boolean expected = EventTrackingManager.isTrackedEventTypePlayThrough25(context);
        Mockito.verify(sharedPreferences).getBoolean(anyString(), anyBoolean());
        assertTrue(expected);
    }

    @Test
    public void setTrackingDoneWithEventTypePlayThrought25() {
        EventTrackingManager.setTrackingDoneWithEventTypePlayThrough25(context, true);
        Mockito.verify(sharedPreferences).edit();
        Mockito.verify(editor).putBoolean(anyString(), anyBoolean());
        Mockito.verify(editor).apply();
    }

    @Test
    public void isTrackedEventTypePlayThrought50() {
        boolean expected = EventTrackingManager.isTrackedEventTypePlayThrough50(context);
        Mockito.verify(sharedPreferences).getBoolean(anyString(), anyBoolean());
        assertTrue(expected);
    }

    @Test
    public void setTrackingDoneWithEventTypePlayThrought50() {
        EventTrackingManager.setTrackingDoneWithEventTypePlayThrough50(context, true);
        Mockito.verify(sharedPreferences).edit();
        Mockito.verify(editor).putBoolean(anyString(), anyBoolean());
        Mockito.verify(editor).apply();
    }

    @Test
    public void isTrackedEventTypePlayThrought75() {
        boolean expected = EventTrackingManager.isTrackedEventTypePlayThrough75(context);
        Mockito.verify(sharedPreferences).getBoolean(anyString(), anyBoolean());
        assertTrue(expected);
    }

    @Test
    public void setTrackingDoneWithEventTypePlayThrought75() {
        EventTrackingManager.setTrackingDoneWithEventTypePlayThrough75(context, true);
        Mockito.verify(sharedPreferences).edit();
        Mockito.verify(editor).putBoolean(anyString(), anyBoolean());
        Mockito.verify(editor).apply();
    }

    @Test
    public void isTrackedEventTypePlayThrought100() {
        boolean expected = EventTrackingManager.isTrackedEventTypePlayThrough100(context);
        Mockito.verify(sharedPreferences).getBoolean(anyString(), anyBoolean());
        assertTrue(expected);
    }

    @Test
    public void setTrackingDoneWithEventTypePlayThrought100() {
        EventTrackingManager.setTrackingDoneWithEventTypePlayThrough100(context, true);
        Mockito.verify(sharedPreferences).edit();
        Mockito.verify(editor).putBoolean(anyString(), anyBoolean());
        Mockito.verify(editor).apply();
    }

    @Test
    public void clearAllValues() {
        EventTrackingManager.resetTracking(context);
        Mockito.verify(sharedPreferences, times(9)).edit();
        Mockito.verify(editor, times(9)).putBoolean(anyString(), anyBoolean());
        Mockito.verify(editor, times(9)).apply();
    }
}
