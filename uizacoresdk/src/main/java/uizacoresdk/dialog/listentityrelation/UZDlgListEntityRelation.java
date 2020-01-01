package uizacoresdk.dialog.listentityrelation;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import uizacoresdk.R;
import vn.uiza.utils.LUIUtil;
import vn.uiza.restapi.model.v2.listallentity.Item;

/**
 * Created by LENOVO on 5/2/2018.
 */

public class UZDlgListEntityRelation extends Dialog {
    private final String TAG = getClass().getSimpleName();
    private Activity activity;
    private AlertDialog dialog;
    private boolean isLandscape;

    private ProgressBar progressBar;
    private TextView tvMsg;
    private List<Item> itemList;
    private RecyclerView recyclerView;
    private PlayListAdapter playListAdapter;

    private PlayListCallback playListCallback;

    public UZDlgListEntityRelation(Activity activity, boolean isLandscape, PlayListCallback playListCallback) {
        super(activity);
        this.activity = activity;
        this.isLandscape = isLandscape;
        this.playListCallback = playListCallback;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dlg_list_entity_relation);

        progressBar = findViewById(R.id.pb);
        LUIUtil.setColorProgressBar(progressBar, ContextCompat.getColor(activity, R.color.colorPrimary));

        tvMsg = findViewById(R.id.tv_msg);
        recyclerView = findViewById(R.id.recycler_view);

        findViewById(R.id.bt_exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        getListAllEntityRelation();
    }

    private void getListAllEntityRelation() {
        LUIUtil.showProgressBar(progressBar);
        //TODO getListAllEntityRelation
        setupUI();
    }

    private void setupUI() {
        tvMsg.setVisibility(View.VISIBLE);
        LUIUtil.hideProgressBar(progressBar);
    }
}