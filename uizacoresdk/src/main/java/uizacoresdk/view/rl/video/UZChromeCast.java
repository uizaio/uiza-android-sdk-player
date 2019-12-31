package uizacoresdk.view.rl.video;

import android.content.Context;
import androidx.annotation.UiThread;
import android.util.Log;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastState;
import com.google.android.gms.cast.framework.CastStateListener;
import uizacoresdk.chromecast.Casty;
import uizacoresdk.util.UZData;
import uizacoresdk.util.UZUtil;
import vn.uiza.core.exception.UizaException;
import vn.uiza.utils.AppUtils;
import vn.uiza.utils.SentryUtils;
import vn.uiza.utils.ViewUtil;

/**
 * Created by loitp on 2/27/2019.
 */

public class UZChromeCast {

    //chromecast https://github.com/DroidsOnRoids/Casty
    private UZMediaRouteButton uzMediaRouteButton;
    protected final String TAG = "TAG" + getClass().getSimpleName();
    private UZChromeCastListener listener;

    {
        if (!AppUtils.checkChromeCastAvailable()) {
            throw new NoClassDefFoundError(UizaException.ERR_505);
        }
    }

    public void setUZChromeCastListener(UZChromeCastListener listener) {
        this.listener = listener;
    }

    public void setupChromeCast(Context context, boolean isTV) {
        if (isTV) return;
        uzMediaRouteButton = new UZMediaRouteButton(context);
        setUpMediaRouteButton(isTV);
        addUIChromecastLayer(context);
    }

    @UiThread
    private void setUpMediaRouteButton(boolean isTV) {
        if (isTV) {
            return;
        }
        UZData.getInstance().getCasty().setUpMediaRouteButton(uzMediaRouteButton);
        UZData.getInstance().getCasty().setOnConnectChangeListener(new Casty.OnConnectChangeListener() {
            @Override
            public void onConnected() {
                if (listener != null) listener.onConnected();
            }

            @Override
            public void onDisconnected() {
                if (listener != null) listener.onDisconnected();
            }
        });
    }

    private void updateMediaRouteButtonVisibility(int state) {
        if (state == CastState.NO_DEVICES_AVAILABLE) {
            ViewUtil.goneViews(uzMediaRouteButton);
        } else {
            ViewUtil.visibleViews(uzMediaRouteButton);
        }
    }

    //tự tạo layout chromecast và background đen
    //Gen layout chromecast with black backgroudn programmatically
    private void addUIChromecastLayer(Context context) {
        //listener check state of chromecast
        CastContext castContext = null;
        try {
            castContext = CastContext.getSharedInstance(context);
        } catch (Exception e) {
            Log.e(TAG, "Error addUIChromecastLayer: " + e.toString());
            SentryUtils.captureException(e);
        }
        if (castContext == null) {
            ViewUtil.goneViews(uzMediaRouteButton);
            return;
        }
        updateMediaRouteButtonVisibility(castContext.getCastState());
        castContext.addCastStateListener(new CastStateListener() {
            @Override
            public void onCastStateChanged(int state) {
                updateMediaRouteButtonVisibility(state);
            }
        });
        if (listener != null) listener.addUIChromecast();
    }

    public void setTintMediaRouteButton(final int color) {
        if (uzMediaRouteButton != null) {
            uzMediaRouteButton.post(new Runnable() {
                @Override
                public void run() {
                    uzMediaRouteButton.applyTint(color);
                }
            });
        }
    }

    public UZMediaRouteButton getUzMediaRouteButton() {
        return uzMediaRouteButton;
    }

    public interface UZChromeCastListener {
        void onConnected();
        void onDisconnected();
        void addUIChromecast();
    }
}
