package vn.loitp.uizavideo.view.dlg.info;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import loitp.core.R;
import vn.loitp.core.utilities.LUIUtil;

/**
 * Created by loitp on 5/2/2018.
 */

public class UizaDialogInfo extends Dialog {
    private final String TAG = getClass().getSimpleName();
    private Activity activity;
    private AlertDialog dialog;
    private ProgressBar progressBar;
    private TextView tvTitle;
    private String title;
    private String msg;

    public UizaDialogInfo(Activity activity, String title, String msg) {
        super(activity);
        this.activity = activity;
        this.title = title;
        this.msg = msg;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_info);

        progressBar = (ProgressBar) findViewById(R.id.pb);
        LUIUtil.setColorProgressBar(progressBar, ContextCompat.getColor(activity, R.color.colorPrimary));

        tvTitle = (TextView) findViewById(R.id.tv_title);
        final TextView tvContent = (TextView) findViewById(R.id.tv_content);
        findViewById(R.id.bt_exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        tvTitle.setText(title);
        LUIUtil.setDelay(1000, new LUIUtil.DelayCallback() {
            @Override
            public void doAfter(int mls) {
                tvContent.setText(msg);
                progressBar.setVisibility(View.GONE);
                tvContent.setVisibility(View.VISIBLE);
            }
        });
    }
}