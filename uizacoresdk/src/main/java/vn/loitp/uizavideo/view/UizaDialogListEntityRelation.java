package vn.loitp.uizavideo.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import java.util.List;

import loitp.core.R;
import vn.loitp.core.utilities.LDeviceUtil;
import vn.loitp.core.utilities.LLog;
import vn.loitp.core.utilities.LScreenUtil;
import vn.loitp.core.utilities.LSocialUtil;
import vn.loitp.uizavideo.view.util.UizaData;
import vn.loitp.views.layout.flowlayout.FlowLayout;

/**
 * Created by LENOVO on 5/2/2018.
 */

public class UizaDialogListEntityRelation extends Dialog {
    private final String TAG = getClass().getSimpleName();
    private Activity activity;
    private AlertDialog dialog;
    private boolean isLandscape;

    public UizaDialogListEntityRelation(Activity activity, boolean isLandscape) {
        super(activity);
        this.activity = activity;
        this.isLandscape = isLandscape;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_list_entity_relation);

        //ll = (FlowLayout) findViewById(R.id.ll);
    }
}