package io.uiza.player.util;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

//https://stackoverflow.com/questions/8248274/android-detect-orientation-changed
public class SensorChangeNotifier {

    private ArrayList<WeakReference<Listener>> compositeListener = new ArrayList<>(3);
    private int currentAngle = 0;
    private SensorEventListener sensorEventListener;
    private SensorManager sensorManager;
    private static SensorChangeNotifier instance;

    public static SensorChangeNotifier getInstance(Context context) {
        if (instance == null) {
            instance = new SensorChangeNotifier(context);
        }
        return instance;
    }

    private SensorChangeNotifier(Context context) {
        sensorEventListener = new NotifierSensorEventListener();
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
    }

    private void onResume() {
        sensorManager.registerListener(sensorEventListener,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void onPause() {
        sensorManager.unregisterListener(sensorEventListener);
    }

    private class NotifierSensorEventListener implements SensorEventListener {

        @Override
        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            float y = event.values[1];
            int newAngle = currentAngle;
            if (x < 5 && x > -5 && y > 5) {
                newAngle = 0;
            } else if (x < -5 && y < 5 && y > -5) {
                newAngle = 90;
            } else if (x < 5 && x > -5 && y < -5) {
                newAngle = 180;
            } else if (x > 5 && y < 5 && y > -5) {
                newAngle = 270;
            }
            if (currentAngle != newAngle) {
                currentAngle = newAngle;
                notifyListeners();
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

    }

    public int getOrientation() {
        return currentAngle;
    }

    public void addListener(SensorChangeNotifier.Listener listener) {
        if (get(listener) == null) {
            compositeListener.add(new WeakReference<>(listener));
        }

        if (compositeListener.size() == 1) {
            onResume(); // this is the first client
        }
    }

    public void remove(SensorChangeNotifier.Listener listener) {
        WeakReference<SensorChangeNotifier.Listener> listenerWR = get(listener);
        remove(listenerWR);
    }

    private void remove(WeakReference<SensorChangeNotifier.Listener> listenerWr) {
        if (listenerWr != null) {
            compositeListener.remove(listenerWr);
        }

        if (compositeListener.size() == 0) {
            onPause();
        }

    }

    private WeakReference<SensorChangeNotifier.Listener> get(
            SensorChangeNotifier.Listener listener) {
        for (WeakReference<SensorChangeNotifier.Listener> existingListener : compositeListener) {
            if (existingListener.get() == listener) {
                return existingListener;
            }
        }
        return null;
    }

    private void notifyListeners() {
        ArrayList<WeakReference<Listener>> deadLinksArr = new ArrayList<>();
        for (WeakReference<SensorChangeNotifier.Listener> wr : compositeListener) {
            if (wr.get() == null) {
                deadLinksArr.add(wr);
            } else {
                wr.get().onOrientationChange(currentAngle);
            }
        }

        // remove dead references
        for (WeakReference<SensorChangeNotifier.Listener> wr : deadLinksArr) {
            compositeListener.remove(wr);
        }
    }

    public boolean isPortrait() {
        return currentAngle == 0 || currentAngle == 180;
    }

    public boolean isLandscape() {
        return !isPortrait();
    }

    public interface Listener {

        void onOrientationChange(int orientation);
    }
}