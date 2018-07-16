package testlibuiza.sample.v3.uizavideov3;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.google.gson.Gson;

import vn.loitp.core.common.Constants;
import vn.loitp.core.utilities.LLog;
import vn.loitp.core.utilities.LPref;
import vn.loitp.restapi.uiza.model.v3.metadata.getdetailofmetadata.Data;

/**
 * Created by loitp on 5/8/2018.
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

        /*String entityId = i.getStringExtra(Constants.FLOAT_LINK_ENTITY_ID);
        String entityCover = i.getStringExtra(Constants.FLOAT_LINK_ENTITY_COVER);
        String entityTitle = i.getStringExtra(Constants.FLOAT_LINK_ENTITY_TITLE);*/
        LLog.d(TAG, "packageNameReceived " + packageNameReceived);
        /*LLog.d(TAG, "entityId " + entityId);
        LLog.d(TAG, "entityCover " + entityCover);
        LLog.d(TAG, "entityTitle " + entityTitle);*/

        boolean isSlideUizaVideoEnabled = LPref.getSlideUizaVideoEnabled(context);
        LLog.d(TAG, "isSlideUizaVideoEnabled " + isSlideUizaVideoEnabled);

        if (packageNameReceived != null && packageNameReceived.equals(context.getPackageName())) {
            if (isSlideUizaVideoEnabled) {
                boolean isActivityRunning = LPref.getAcitivityCanSlideIsRunning(context);
                LLog.d(TAG, "isActivityRunning " + isActivityRunning);
                Intent intent = new Intent(context, V3CannotSlidePlayer.class);
                /*intent.putExtra(Constants.FLOAT_LINK_ENTITY_ID, entityId);
                intent.putExtra(Constants.FLOAT_LINK_ENTITY_TITLE, entityTitle);
                intent.putExtra(Constants.FLOAT_LINK_ENTITY_COVER, entityCover);*/
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
            /*else {
                Intent intent = new Intent(context, UizaPlayerActivityV2.class);
                intent.putExtra(Constants.FLOAT_LINK_ENTITY_ID, entityId);
                intent.putExtra(Constants.FLOAT_LINK_ENTITY_TITLE, entityTitle);
                intent.putExtra(Constants.FLOAT_LINK_ENTITY_COVER, entityCover);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }*/
        }
    }
}