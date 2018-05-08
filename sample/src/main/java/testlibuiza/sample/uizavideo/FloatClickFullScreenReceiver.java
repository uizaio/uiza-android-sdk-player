package testlibuiza.sample.uizavideo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import testlibuiza.sample.uizavideo.rl.TestUizaVideoIMActivityRl;
import vn.loitp.core.common.Constants;
import vn.loitp.core.utilities.LLog;

/**
 * Created by LENOVO on 5/8/2018.
 */

public class FloatClickFullScreenReceiver extends BroadcastReceiver {
    private final String TAG = getClass().getSimpleName();

    @Override
    public void onReceive(Context context, Intent i) {
        long positionOfPlayer = i.getLongExtra(Constants.FLOAT_CURRENT_POSITION, 0l);
        LLog.d(TAG, "positionOfPlayer " + positionOfPlayer);
        Intent intent = new Intent(context, TestUizaVideoIMActivityRl.class);
        intent.putExtra(Constants.FLOAT_CURRENT_POSITION, positionOfPlayer);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}