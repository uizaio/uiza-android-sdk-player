package io.uiza.core.util.connection;

import android.annotation.TargetApi;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import io.uiza.core.util.LLog;
import org.greenrobot.eventbus.EventBus;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class UzConectifyService extends JobService
        implements UzConnectivityReceiver.ConnectivityReceiverListener {

    private static final String TAG = UzConectifyService.class.getSimpleName();

    private UzConnectivityReceiver uzConnectivityReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
        uzConnectivityReceiver = new UzConnectivityReceiver(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        registerReceiver(uzConnectivityReceiver,
                new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        unregisterReceiver(uzConnectivityReceiver);
        return true;
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        LLog.d(TAG, "onNetworkConnectionChanged isConnected: " + isConnected);
        boolean isConnectedMobile = false;
        boolean isConnectedWifi = false;
        boolean isConnectedFast = false;
        if (isConnected) {
            if (UzConnectivityUtil.isConnectedMobile(this)) {
                isConnectedMobile = true;
            }
            if (UzConnectivityUtil.isConnectedWifi(this)) {
                isConnectedWifi = true;
            }
            if (UzConnectivityUtil.isConnectedFast(this)) {
                isConnectedFast = true;
            }
        }

        UzConnectivityEvent event = new UzConnectivityEvent();
        event.setConnected(isConnected);
        event.setConnectedMobile(isConnectedMobile);
        event.setConnectedWifi(isConnectedWifi);
        event.setConnectedFast(isConnectedFast);

        EventBus.getDefault().postSticky(event);
    }
}