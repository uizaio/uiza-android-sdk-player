package uizacoresdk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.google.gson.Gson;

import vn.uiza.core.common.Constants;
import vn.uiza.core.utilities.LLog;
import uizacoresdk.util.UZData;
import uizacoresdk.util.UZUtil;

/**
 * Created by loitp on 5/8/2018.
 */

public class FloatClickFullScreenReceiverV3 extends BroadcastReceiver {
    private final String TAG = getClass().getSimpleName();
    //private Data data;
    private Gson gson = new Gson();

    @Override
    public void onReceive(Context context, Intent i) {
        if (context == null || i == null) {
            return;
        }
        String packageNameReceived = i.getStringExtra(Constants.FLOAT_CLICKED_PACKAGE_NAME);
        //LLog.d(TAG, "packageNameReceived " + packageNameReceived);
        String classNameOfPlayer = UZUtil.getClassNameOfPlayer(context);
        //data = UZUtil.getData(context, gson);
        if (UZData.getInstance().getData() == null || classNameOfPlayer == null) {
            return;
        }
        //LLog.d(TAG, "onReceive " + UizaPref.getClassNameOfPlayer(context));
        if (packageNameReceived != null && packageNameReceived.equals(context.getPackageName())) {
            try {
                Class classNamePfPlayer = Class.forName(classNameOfPlayer);
                Intent intent = new Intent(context, classNamePfPlayer);
                UZUtil.setClassNameOfPlayer(context, null);//clear class name of player
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } catch (ClassNotFoundException e) {
                LLog.e(TAG, "ClassNotFoundException " + e.toString());
            }
        }
    }
}