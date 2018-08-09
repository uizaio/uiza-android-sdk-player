package testlibuiza.sample.v2.uizavideo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import testlibuiza.sample.v2.uizavideo.rl.V2UizaVideoIMActivity;
import testlibuiza.sample.v2.uizavideo.slide.V2UizaVideoIMActivitySlide;
import vn.loitp.core.common.Constants;
import vn.loitp.core.utilities.LLog;
import vn.loitp.core.utilities.UizaPref;

/**
 * Created by LENOVO on 5/8/2018.
 */

public class FloatClickFullScreenReceiver extends BroadcastReceiver {
    private final String TAG = getClass().getSimpleName();

    @Override
    public void onReceive(Context context, Intent i) {
        String packageNameReceived = i.getStringExtra(Constants.FLOAT_CLICKED_PACKAGE_NAME);
        String entityId = i.getStringExtra(Constants.FLOAT_LINK_ENTITY_ID);
        String entityCover = i.getStringExtra(Constants.FLOAT_LINK_ENTITY_COVER);
        String entityTitle = i.getStringExtra(Constants.FLOAT_LINK_ENTITY_TITLE);
        LLog.d(TAG, "packageNameReceived " + packageNameReceived);
        LLog.d(TAG, "entityId " + entityId);
        LLog.d(TAG, "entityCover " + entityCover);
        LLog.d(TAG, "entityTitle " + entityTitle);

        boolean isSlideUizaVideoEnabled = UizaPref.getSlideUizaVideoEnabled(context);
        //LLog.d(TAG, "isSlideUizaVideoEnabled " + isSlideUizaVideoEnabled);
        if (packageNameReceived != null && packageNameReceived.equals(context.getPackageName())) {
            if (isSlideUizaVideoEnabled) {
                boolean isActivityRunning = UizaPref.getAcitivityCanSlideIsRunning(context);
                Intent intent = new Intent(context, V2UizaVideoIMActivitySlide.class);
                intent.putExtra(Constants.FLOAT_LINK_ENTITY_ID, entityId);
                intent.putExtra(Constants.FLOAT_LINK_ENTITY_TITLE, entityTitle);
                intent.putExtra(Constants.FLOAT_LINK_ENTITY_COVER, entityCover);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } else {
                Intent intent = new Intent(context, V2UizaVideoIMActivity.class);
                intent.putExtra(Constants.FLOAT_LINK_ENTITY_ID, entityId);
                intent.putExtra(Constants.FLOAT_LINK_ENTITY_TITLE, entityTitle);
                intent.putExtra(Constants.FLOAT_LINK_ENTITY_COVER, entityCover);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        }
    }
}