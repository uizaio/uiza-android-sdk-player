package uizacoresdk.dialog.playlistfolder;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.androidanimations.library.Techniques;

import java.util.List;

import uizacoresdk.R;
import uizacoresdk.animations.AnimationUtils;
import uizacoresdk.util.UizaData;
import vn.uiza.models.PlaybackInfo;
import uizacoresdk.widget.recyclerview.SnapType;
import uizacoresdk.widget.recyclerview.SnappyLinearLayoutManager;

/**
 * Created by loitp on 16/10/2018.
 */

public class UizaPlaylistFolderDialog extends Dialog {
    private Context context;
    private AlertDialog dialog;
    private boolean isLandscape;
    private RecyclerView recyclerView;
    private AdapterPlaylistFolder adapterPlaylistFolder;
    private List<PlaybackInfo> playList;
    private int currentPositionOfDataList;
    private CallbackPlaylistFolder callbackPlaylistFolder;

    public UizaPlaylistFolderDialog(@NonNull Context context, boolean isLandscape, List<PlaybackInfo> playList, int currentPositionOfDataList, CallbackPlaylistFolder callbackPlaylistFolder) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.context = context;
        this.isLandscape = isLandscape;
        this.playList = playList;
        this.currentPositionOfDataList = currentPositionOfDataList;
        this.callbackPlaylistFolder = callbackPlaylistFolder;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.v3_dialog_list_playlist_folder);
        recyclerView = findViewById(R.id.recycler_view);
        final ImageButton btExit = findViewById(R.id.bt_exit);
        btExit.setOnClickListener(v -> dismiss());
        btExit.setOnFocusChangeListener((view, isFocus) -> {
            if (isFocus) {
                AnimationUtils.play(view, Techniques.Pulse);
                btExit.setColorFilter(Color.WHITE);
                btExit.setBackgroundColor(Color.BLACK);
            } else {
                btExit.setBackgroundColor(Color.TRANSPARENT);
                btExit.setColorFilter(Color.BLACK);
            }
        });
        setupUI();
    }

    private void setupUI() {
        SnappyLinearLayoutManager layoutManager = new SnappyLinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        layoutManager.setSnapType(SnapType.CENTER);
        layoutManager.setSnapInterpolator(new DecelerateInterpolator());
        recyclerView.setLayoutManager(layoutManager);
        adapterPlaylistFolder = new AdapterPlaylistFolder(context, playList, currentPositionOfDataList, new CallbackPlaylistFolder() {
            @Override
            public void onClickItem(PlaybackInfo data, int position) {
                if (UizaData.getInstance().isSettingPlayer()) {
                    return;
                }
                dismiss();
                if (callbackPlaylistFolder != null) {
                    callbackPlaylistFolder.onClickItem(data, position);
                }
            }

            @Override
            public void onFocusChange(PlaybackInfo data, int position) {
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

        recyclerView.postDelayed(() -> {
            RecyclerView.ViewHolder holder = recyclerView.findViewHolderForAdapterPosition(currentPositionOfDataList);
            if (holder == null) {
                return;
            }
            holder.itemView.requestFocus();
        }, 50);
    }
}