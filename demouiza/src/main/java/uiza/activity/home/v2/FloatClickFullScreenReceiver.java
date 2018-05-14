package uiza.activity.home.v2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import uiza.activity.home.v2.cannotslide.UizaPlayerActivityV2;
import uiza.activity.home.v2.canslide.HomeV2CanSlideActivity;
import vn.loitp.core.common.Constants;
import vn.loitp.core.utilities.LLog;
import vn.loitp.core.utilities.LPref;
import vn.loitp.uizavideo.view.util.UizaUtil;
import vn.loitp.views.LToast;

/**
 * Created by LENOVO on 5/8/2018.
 */

public class FloatClickFullScreenReceiver extends BroadcastReceiver {
    private final String TAG = getClass().getSimpleName();

    @Override
    public void onReceive(Context context, Intent i) {
        long positionOfPlayer = i.getLongExtra(Constants.FLOAT_CURRENT_POSITION, 0l);
        String packageNameReceived = i.getStringExtra(Constants.FLOAT_CLICKED_PACKAGE_NAME);
        String entityId = i.getStringExtra(Constants.FLOAT_LINK_ENTITY_ID);
        String entityCover = i.getStringExtra(Constants.FLOAT_LINK_ENTITY_COVER);
        String entityTitle = i.getStringExtra(Constants.FLOAT_LINK_ENTITY_TITLE);
        LLog.d(TAG, "positionOfPlayer " + positionOfPlayer);
        LLog.d(TAG, "packageNameReceived " + packageNameReceived);
        LLog.d(TAG, "entityId " + entityId);
        LLog.d(TAG, "entityCover " + entityCover);
        LLog.d(TAG, "entityTitle " + entityTitle);

        boolean isAppInForeground = UizaUtil.isAppInForeground(context);
        LLog.d(TAG, "isAppInForeground " + isAppInForeground);

        /*if (isAppInForeground) {
            //TODO
            LToast.show(context, "TODO isAppInForeground");
        } else {
            boolean isSlideUizaVideoEnabled = LPref.getSlideUizaVideoEnabled(context);
            LLog.d(TAG, "isSlideUizaVideoEnabled " + isSlideUizaVideoEnabled);
            if (packageNameReceived != null && packageNameReceived.equals(context.getPackageName())) {
                Intent intent = new Intent(context, isSlideUizaVideoEnabled ? HomeV2CanSlideActivity.class : UizaPlayerActivityV2.class);
                intent.putExtra(Constants.FLOAT_CURRENT_POSITION, positionOfPlayer);
                intent.putExtra(Constants.FLOAT_LINK_ENTITY_ID, entityId);
                intent.putExtra(Constants.FLOAT_LINK_ENTITY_TITLE, entityCover);
                intent.putExtra(Constants.FLOAT_LINK_ENTITY_COVER, entityTitle);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        }*/
        boolean isSlideUizaVideoEnabled = LPref.getSlideUizaVideoEnabled(context);
        if (packageNameReceived != null && packageNameReceived.equals(context.getPackageName())) {
            Intent intent = new Intent(context, isSlideUizaVideoEnabled ? HomeV2CanSlideActivity.class : UizaPlayerActivityV2.class);
            intent.putExtra(Constants.FLOAT_CURRENT_POSITION, positionOfPlayer);
            intent.putExtra(Constants.FLOAT_LINK_ENTITY_ID, entityId);
            intent.putExtra(Constants.FLOAT_LINK_ENTITY_TITLE, entityTitle);
            intent.putExtra(Constants.FLOAT_LINK_ENTITY_COVER, entityCover);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }
}