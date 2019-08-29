package io.uiza.player.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;
import io.uiza.core.util.UzCommonUtil;
import io.uiza.core.util.UzCommonUtil.DelayCallback;
import io.uiza.core.util.UzDisplayUtil;
import io.uiza.player.R;

public final class UzDialogInfo extends Dialog {

    private Context context;
    private String title;
    private String msg;

    public UzDialogInfo(Context context, String title, String msg) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.context = context;
        this.title = title;
        this.msg = msg;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dlg_info);

        final ProgressBar progressBar = findViewById(R.id.pb);
        UzDisplayUtil.setColorProgressBar(progressBar,
                ContextCompat.getColor(context, R.color.colorPrimary));

        final TextView tvTitle = findViewById(R.id.tv_title);
        final TextView tvContent = findViewById(R.id.tv_content);

        tvTitle.setText(title);
        UzCommonUtil.actionWithDelayed(1000, new DelayCallback() {
            @Override
            public void doAfter(int mls) {
                tvContent.setText(msg);
                progressBar.setVisibility(View.GONE);
                tvContent.setVisibility(View.VISIBLE);
            }
        });
    }
}