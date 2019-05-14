package vn.uiza.core.utilities.connection;

import android.annotation.TargetApi;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;

import vn.uiza.core.utilities.LConnectivityUtil;
import vn.uiza.core.utilities.LLog;
import vn.uiza.data.EventBusData;

/**
 * Created by Loitp on 5/6/2017.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class LConectifyService extends JobService implements ConnectivityReceiver.ConnectivityReceiverListener {

    private final String TAG = LConectifyService.class.getSimpleName();

    private ConnectivityReceiver mConnectivityReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
        mConnectivityReceiver = new ConnectivityReceiver(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        registerReceiver(mConnectivityReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        unregisterReceiver(mConnectivityReceiver);
        return true;
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        LLog.d(TAG, "onNetworkConnectionChanged isConnected: " + isConnected);
        if (isConnected) {
            boolean isConnectedMobile = false;
            boolean isConnectedWifi = false;
            boolean isConnectedFast = false;
            if (LConnectivityUtil.isConnectedMobile(this)) {
                isConnectedMobile = true;
            }
            if (LConnectivityUtil.isConnectedWifi(this)) {
                isConnectedWifi = true;
            }
            if (LConnectivityUtil.isConnectedFast(this)) {
                isConnectedFast = true;
            }
            EventBusData.getInstance().sendConnectChange(true, isConnectedFast, isConnectedWifi, isConnectedMobile);
        } else {
            EventBusData.getInstance().sendConnectChange(false, false, false, false);
        }
    }
}