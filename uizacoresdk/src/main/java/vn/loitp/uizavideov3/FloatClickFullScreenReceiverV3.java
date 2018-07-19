package vn.loitp.uizavideov3;

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
        //LLog.d(TAG, "packageNameReceived " + packageNameReceived);
        String classNameOfPlayer = LPref.getClassNameOfPlayer(context);
        data = LPref.getData(context, gson);
        if (data == null || classNameOfPlayer == null) {
            return;
        }
        LLog.d(TAG, "onReceive " + LPref.getClassNameOfPlayer(context));
        if (packageNameReceived != null && packageNameReceived.equals(context.getPackageName())) {
            try {
                Class classNamePfPlayer = Class.forName(classNameOfPlayer);
                Intent intent = new Intent(context, classNamePfPlayer);
                LPref.setClassNameOfPlayer(context, null);//clear class name of player
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } catch (ClassNotFoundException e) {
                LLog.e(TAG, "ClassNotFoundException " + e.toString());
            }
        }
    }
}