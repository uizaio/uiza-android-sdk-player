package uiza.activity.home.v2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import uiza.activity.home.v2.cannotslide.UizaPlayerActivityV2;
import uiza.activity.home.v2.canslide.HomeV2CanSlideActivity;
import vn.loitp.core.common.Constants;
import vn.loitp.core.utilities.LLog;
import vn.loitp.core.utilities.LPref;

import static vn.loitp.core.common.Constants.KEY_UIZA_ENTITY_COVER;
import static vn.loitp.core.common.Constants.KEY_UIZA_ENTITY_ID;
import static vn.loitp.core.common.Constants.KEY_UIZA_ENTITY_TITLE;

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

        boolean isSlideUizaVideoEnabled = LPref.getSlideUizaVideoEnabled(context);
        LLog.d(TAG, "isSlideUizaVideoEnabled " + isSlideUizaVideoEnabled);
        if (packageNameReceived != null && packageNameReceived.equals(context.getPackageName())) {
            Intent intent = new Intent(context, isSlideUizaVideoEnabled ? HomeV2CanSlideActivity.class : UizaPlayerActivityV2.class);
            intent.putExtra(Constants.FLOAT_CURRENT_POSITION, positionOfPlayer);
            intent.putExtra(KEY_UIZA_ENTITY_ID, entityId);
            intent.putExtra(KEY_UIZA_ENTITY_COVER, entityCover);
            intent.putExtra(KEY_UIZA_ENTITY_TITLE, entityTitle);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }
}