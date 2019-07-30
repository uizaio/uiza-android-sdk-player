package vn.uiza.core.utilities.connection;

public final class ConnectivityEvent {
    private boolean isConnected;
    private boolean isConnectedFast;
    private boolean isConnectedWifi;
    private boolean isConnectedMobile;

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    public boolean isConnectedFast() {
        return isConnectedFast;
    }

    public void setConnectedFast(boolean connectedFast) {
        isConnectedFast = connectedFast;
    }

    public boolean isConnectedWifi() {
        return isConnectedWifi;
    }

    public void setConnectedWifi(boolean connectedWifi) {
        isConnectedWifi = connectedWifi;
    }

    public boolean isConnectedMobile() {
        return isConnectedMobile;
    }

    public void setConnectedMobile(boolean connectedMobile) {
        isConnectedMobile = connectedMobile;
    }
}
