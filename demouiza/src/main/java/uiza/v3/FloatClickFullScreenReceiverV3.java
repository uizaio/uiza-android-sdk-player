package uiza.v3;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.google.gson.Gson;

import uiza.v3.canslide.HomeV3CanSlideActivity;
import vn.loitp.core.common.Constants;
import vn.loitp.core.utilities.LPref;
import vn.loitp.restapi.uiza.model.v3.metadata.getdetailofmetadata.Data;

/**
 * Created by LENOVO on 5/8/2018.
 */

public class FloatClickFullScreenReceiverV3 extends BroadcastReceiver {
    private final String TAG = getClass().getSimpleName();
    private Data data;
    private Gson gson = new Gson();

    @Override
    public void onReceive(Context context, Intent i) {
        String packageNameReceived = i.getStringExtra(Constants.FLOAT_CLICKED_PACKAGE_NAME);

        data = LPref.getData(context, gson);
        if (data == null) {
            return;
        }

        boolean isSlideUizaVideoEnabled = LPref.getSlideUizaVideoEnabled(context);

        if (packageNameReceived != null && packageNameReceived.equals(context.getPackageName())) {
            if (isSlideUizaVideoEnabled) {
                //boolean isActivityRunning = LPref.getAcitivityCanSlideIsRunning(context);
                //LLog.d(TAG, "isActivityRunning " + isActivityRunning);
                Intent intent = new Intent(context, HomeV3CanSlideActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        }
    }
}