package io.uiza.core.util.connection;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class UzConnectivityReceiver extends BroadcastReceiver {

    private ConnectivityReceiverListener receiverListener;

    UzConnectivityReceiver(ConnectivityReceiverListener listener) {
        receiverListener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        receiverListener.onNetworkConnectionChanged(isConnected(context));
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public interface ConnectivityReceiverListener {

        void onNetworkConnectionChanged(boolean isConnected);
    }
}