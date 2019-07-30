package uizacoresdk.view.dlg.info;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;
import uizacoresdk.R;
import vn.uiza.core.utilities.LUIUtil;

/**
 * Created by loitp on 5/2/2018.
 */

public class UZDlgInfoV1 extends Dialog {
    private final String TAG = getClass().getSimpleName();
    private Context context;
    private ProgressBar progressBar;
    private TextView tvTitle;
    private String title;
    private String msg;

    public UZDlgInfoV1(Context context, String title, String msg) {
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

        progressBar = findViewById(R.id.pb);
        LUIUtil.setColorProgressBar(progressBar, ContextCompat.getColor(context, R.color.colorPrimary));

        tvTitle = findViewById(R.id.tv_title);
        final TextView tvContent = findViewById(R.id.tv_content);

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