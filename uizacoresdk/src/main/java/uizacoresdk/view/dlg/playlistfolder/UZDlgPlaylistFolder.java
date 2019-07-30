package uizacoresdk.view.dlg.playlistfolder;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
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
import uizacoresdk.R;
import uizacoresdk.util.UZData;
import vn.uiza.core.utilities.LAnimationUtil;
import vn.uiza.restapi.uiza.model.v3.metadata.getdetailofmetadata.Data;
import vn.uiza.views.recyclerview.snappysmoothscroller.SnapType;
import vn.uiza.views.recyclerview.snappysmoothscroller.SnappyLinearLayoutManager;

/**
 * Created by loitp on 16/10/2018.
 */

public class UZDlgPlaylistFolder extends Dialog {
    private final String TAG = getClass().getSimpleName();
    private Context context;
    private AlertDialog dialog;
    private boolean isLandscape;
    private RecyclerView recyclerView;
    private AdapterPlaylistFolder adapterPlaylistFolder;
    private List<Data> dataList;
    private int currentPositionOfDataList;
    private CallbackPlaylistFolder callbackPlaylistFolder;

    public UZDlgPlaylistFolder(Context context, boolean isLandscape, List<Data> dataList, int currentPositionOfDataList, CallbackPlaylistFolder callbackPlaylistFolder) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.context = context;
        this.isLandscape = isLandscape;
        this.dataList = dataList;
        this.currentPositionOfDataList = currentPositionOfDataList;
        this.callbackPlaylistFolder = callbackPlaylistFolder;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.v3_dialog_list_playlist_folder);
        recyclerView = findViewById(R.id.recycler_view);
        final ImageButton btExit = findViewById(R.id.bt_exit);
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
        SnappyLinearLayoutManager layoutManager = new SnappyLinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        layoutManager.setSnapType(SnapType.CENTER);
        layoutManager.setSnapInterpolator(new DecelerateInterpolator());
        recyclerView.setLayoutManager(layoutManager);
        adapterPlaylistFolder = new AdapterPlaylistFolder(context, dataList, currentPositionOfDataList, new CallbackPlaylistFolder() {
            @Override
            public void onClickItem(Data data, int position) {
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
        recyclerView.scrollToPosition(currentPositionOfDataList);
        recyclerView.requestFocus();

        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (recyclerView == null || recyclerView.findViewHolderForAdapterPosition(currentPositionOfDataList) == null || recyclerView.findViewHolderForAdapterPosition(currentPositionOfDataList).itemView == null) {
                    return;
                }
                RecyclerView.ViewHolder holder = recyclerView.findViewHolderForAdapterPosition(currentPositionOfDataList);
                if (holder != null) {
                    holder.itemView.requestFocus();
                }
            }
        }, 50);
    }
}