package vn.uiza.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.TelephonyManager;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;

/**
 * before API 29
 * <p>
 * Callers should instead use the {@link ConnectivityManager.NetworkCallback} API to
 * *             learn about connectivity changes, or switch to use
 * *             {@link ConnectivityManager#getNetworkCapabilities} or
 * *             {@link ConnectivityManager#getLinkProperties} to get information synchronously
 */
public final class ConnectivityUtils {

    private ConnectivityUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * Get the network info
     */

    private static ConnectivityManager getConnectivityManager(@NonNull Context context) {
        return (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    /**
     * Check if there is any connectivity
     */
//    @RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
    public static boolean isConnected(@NonNull Context context) {
        ConnectivityManager cm = getConnectivityManager(context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Network network = cm.getActiveNetwork();
            if (network != null) {
                NetworkCapabilities ncs = cm.getNetworkCapabilities(network);
                return ncs.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || ncs.hasTransport(NetworkCapabilities.TRANSPORT_WIFI);
            }
            return false;
        } else {
            NetworkInfo info = cm.getActiveNetworkInfo();
            return (info != null && info.isConnected());
        }
    }

    /**
     * Check if there is any connectivity to a Wifi network
     */
    public static boolean isConnectedWifi(@NonNull Context context) {
        NetworkInfo info = getConnectivityManager(context).getActiveNetworkInfo();
        return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_WIFI);
    }

    /**
     * Check if there is any connectivity to a mobile network
     */
    public static boolean isConnectedMobile(@NonNull Context context) {
        NetworkInfo info = getConnectivityManager(context).getActiveNetworkInfo();
        return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_MOBILE);
    }

    /**
     * Check if there is fast connectivity
     */
    public static boolean isConnectedFast(@NonNull Context context) {
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q){
        NetworkInfo info = getConnectivityManager(context).getActiveNetworkInfo();
        return (info != null && info.isConnected() && isConnectionFast(info.getType(), info.getSubtype()));
    }

    /**
     * Check if the connection is fast
     */
    private static boolean isConnectionFast(int type, int subType) {
        if (type == ConnectivityManager.TYPE_WIFI) {
            return true;
        } else if (type == ConnectivityManager.TYPE_MOBILE) {
            switch (subType) {
                case TelephonyManager.NETWORK_TYPE_1xRTT: // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_CDMA: // ~ 14-64 kbps
                case TelephonyManager.NETWORK_TYPE_EDGE: // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_GPRS: // ~ 100 kbps
                case TelephonyManager.NETWORK_TYPE_IDEN: // API level 8, // ~25 kbps
                    return false;
                case TelephonyManager.NETWORK_TYPE_EVDO_0: // ~ 400-1000 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_A: // ~ 600-1400 kbps
                case TelephonyManager.NETWORK_TYPE_HSDPA: // ~ 2-14 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPA: // ~ 700-1700 kbps
                case TelephonyManager.NETWORK_TYPE_HSUPA: // ~ 1-23 Mbps
                case TelephonyManager.NETWORK_TYPE_UMTS: // ~ 400-7000 kbps
                case TelephonyManager.NETWORK_TYPE_EHRPD: // API level 11, ~ 1-2 Mbps
                case TelephonyManager.NETWORK_TYPE_EVDO_B: // API level 9, ~ 5 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPAP: // API level 13, ~ 10-20 Mbps
                case TelephonyManager.NETWORK_TYPE_LTE: // API level 11, // ~ 10+ Mbps
                case TelephonyManager.NETWORK_TYPE_IWLAN:
                case 19:
                    return true;
                // Unknown
                case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                default:
                    return false;
            }
        } else {
            return false;
        }
    }

}