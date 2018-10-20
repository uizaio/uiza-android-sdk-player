package vn.uiza.uzv3.view.dlg.playlistfolder;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageButton;

import com.daimajia.androidanimations.library.Techniques;

import java.util.List;

import vn.uiza.R;
import vn.uiza.core.utilities.LAnimationUtil;
import vn.uiza.core.utilities.LLog;
import vn.uiza.core.utilities.LUIUtil;
import vn.uiza.restapi.uiza.model.v3.metadata.getdetailofmetadata.Data;
import vn.uiza.uzv3.util.UZData;
import vn.uiza.views.recyclerview.snappysmoothscroller.SnapType;
import vn.uiza.views.recyclerview.snappysmoothscroller.SnappyLinearLayoutManager;

/**
 * Created by loitp on 16/10/2018.
 */

public class UZDlgPlaylistFolder extends Dialog {
    private final String TAG = getClass().getSimpleName();
    private Activity activity;
    private AlertDialog dialog;
    private boolean isLandscape;
    //private Gson gson = new Gson();
    //private ProgressBar progressBar;
    //private TextView tvMsg;
    private RecyclerView recyclerView;
    private AdapterPlaylistFolder adapterPlaylistFolder;

    private List<Data> dataList;
    private int currentPositionOfDataList;

    private CallbackPlaylistFolder callbackPlaylistFolder;

    public UZDlgPlaylistFolder(Activity activity, boolean isLandscape, List<Data> dataList, int currentPositionOfDataList, CallbackPlaylistFolder callbackPlaylistFolder) {
        super(activity);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.activity = activity;
        this.isLandscape = isLandscape;
        this.dataList = dataList;
        this.currentPositionOfDataList = currentPositionOfDataList;
        this.callbackPlaylistFolder = callbackPlaylistFolder;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.v3_dialog_list_playlist_folder);

        //progressBar = (ProgressBar) findViewById(R.id.pb);
        //LUIUtil.setColorProgressBar(progressBar, ContextCompat.getColor(activity, R.color.colorPrimary));

        //tvMsg = (TextView) findViewById(R.id.tv_msg);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        final ImageButton btExit = (ImageButton) findViewById(R.id.bt_exit);
        btExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btExit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean isFocus) {
                if (isFocus) {
                    LAnimationUtil.play(view, Techniques.Pulse);
                    btExit.setColorFilter(Color.WHITE);
                    btExit.setBackgroundColor(Color.BLACK);
                } else {
                    btExit.setBackgroundColor(Color.TRANSPARENT);
                    btExit.setColorFilter(Color.BLACK);
                }
            }
        });
        setupUI();
    }

    private void setupUI() {
        //recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));

        SnappyLinearLayoutManager layoutManager = new SnappyLinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
        layoutManager.setSnapType(SnapType.CENTER);
        layoutManager.setSnapInterpolator(new DecelerateInterpolator());
        recyclerView.setLayoutManager(layoutManager);

        //recyclerView.setItemAnimator(new DefaultItemAnimator());
        //LLog.d(TAG, "--------> " + widthRecyclerView + " x " + heightRecyclerView);
        adapterPlaylistFolder = new AdapterPlaylistFolder(activity, dataList, currentPositionOfDataList, new CallbackPlaylistFolder() {
            @Override
            public void onClickItem(Data data, int position) {
                //LLog.d(TAG, "onClickItem position: " + position);
                if (UZData.getInstance().isSettingPlayer()) {
                    return;
                }
                dismiss();
                if (callbackPlaylistFolder != null) {
                    callbackPlaylistFolder.onClickItem(data, position);
                }
            }

            @Override
            public void onFocusChange(Data data, int position) {
                if (recyclerView != null) {
                    recyclerView.smoothScrollToPosition(position);
                }
            }

            @Override
            public void onDismiss() {
                if (callbackPlaylistFolder != null) {
                    callbackPlaylistFolder.onDismiss();
                }
            }
        });
        recyclerView.setAdapter(adapterPlaylistFolder);
        LUIUtil.setPullLikeIOSHorizontal(recyclerView);
        LLog.d(TAG, "currentPositionOfDataList " + currentPositionOfDataList + "/" + dataList.size());
        //recyclerView.smoothScrollToPosition(currentPositionOfDataList);
        recyclerView.scrollToPosition(currentPositionOfDataList);
        recyclerView.requestFocus();

        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (recyclerView == null || recyclerView.findViewHolderForAdapterPosition(currentPositionOfDataList) == null || recyclerView.findViewHolderForAdapterPosition(currentPositionOfDataList).itemView == null) {
                    return;
                }
                recyclerView.findViewHolderForAdapterPosition(currentPositionOfDataList).itemView.requestFocus();
            }
        }, 50);
    }
}