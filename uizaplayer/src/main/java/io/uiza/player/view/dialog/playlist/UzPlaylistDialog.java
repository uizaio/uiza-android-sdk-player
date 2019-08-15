package io.uiza.player.view.dialog.playlist;

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
import io.uiza.core.api.response.video.VideoData;
import io.uiza.core.util.UzAnimationUtil;
import io.uiza.core.view.snappysmoothscroller.SnapType;
import io.uiza.core.view.snappysmoothscroller.SnappyLinearLayoutManager;
import io.uiza.player.R;
import io.uiza.player.util.UzPlayerData;
import java.util.List;

public class UzPlaylistDialog extends Dialog {

    private Context context;
    private boolean isLandscape;
    private RecyclerView recyclerView;
    private UzPlaylistAdapter uzPlaylistAdapter;
    private List<VideoData> dataList;
    private int currentPositionOfDataList;
    private UzPlaylistCallback uzPlaylistCallback;

    public UzPlaylistDialog(Context context, boolean isLandscape, List<VideoData> dataList,
            int currentPositionOfDataList, UzPlaylistCallback uzPlaylistCallback) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.context = context;
        this.isLandscape = isLandscape;
        this.dataList = dataList;
        this.currentPositionOfDataList = currentPositionOfDataList;
        this.uzPlaylistCallback = uzPlaylistCallback;
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
                    UzAnimationUtil.play(view, Techniques.Pulse);
                    btExit.setColorFilter(Color.WHITE);
                    btExit.setBackgroundColor(Color.BLACK);
                } else {
                    btExit.setBackgroundColor(Color.TRANSPARENT);
                    btExit.setColorFilter(Color.BLACK);
                }
            }
        });
        setupUi();
    }

    private void setupUi() {
        SnappyLinearLayoutManager layoutManager = new SnappyLinearLayoutManager(context,
                LinearLayoutManager.HORIZONTAL, false);
        layoutManager.setSnapType(SnapType.CENTER);
        layoutManager.setSnapInterpolator(new DecelerateInterpolator());
        recyclerView.setLayoutManager(layoutManager);
        uzPlaylistAdapter = new UzPlaylistAdapter(context, dataList, currentPositionOfDataList,
                new UzPlaylistCallback() {
                    @Override
                    public void onClickItem(VideoData data, int position) {
                        if (UzPlayerData.getInstance().isSettingPlayer()) {
                            return;
                        }
                        dismiss();
                        if (uzPlaylistCallback != null) {
                            uzPlaylistCallback.onClickItem(data, position);
                        }
                    }

                    @Override
                    public void onFocusChange(VideoData data, int position) {
                        if (recyclerView != null) {
                            recyclerView.smoothScrollToPosition(position);
                        }
                    }

                    @Override
                    public void onDismiss() {
                        if (uzPlaylistCallback != null) {
                            uzPlaylistCallback.onDismiss();
                        }
                    }
                });
        recyclerView.setAdapter(uzPlaylistAdapter);
        recyclerView.scrollToPosition(currentPositionOfDataList);
        recyclerView.requestFocus();

        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (recyclerView == null
                        || recyclerView.findViewHolderForAdapterPosition(currentPositionOfDataList)
                        == null || recyclerView
                        .findViewHolderForAdapterPosition(currentPositionOfDataList).itemView
                        == null) {
                    return;
                }
                RecyclerView.ViewHolder holder = recyclerView
                        .findViewHolderForAdapterPosition(currentPositionOfDataList);
                if (holder != null) {
                    holder.itemView.requestFocus();
                }
            }
        }, 50);
    }
}