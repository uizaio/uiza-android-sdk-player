package uizacoresdk.view.dlg.playlistfolder;

/**
 * Created by www.muathu@gmail.com on 11/7/2017.
 */

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.daimajia.androidanimations.library.Techniques;
import io.uiza.core.api.response.video.VideoData;
import io.uiza.core.util.UzImageUtil;
import io.uiza.core.util.LLog;
import io.uiza.core.util.UzAnimationUtil;
import java.util.List;
import uizacoresdk.R;
import uizacoresdk.util.UZUtil;

public class AdapterPlaylistFolder extends RecyclerView.Adapter<AdapterPlaylistFolder.PlayListHolder> {
    private final String TAG = getClass().getSimpleName();
    private List<VideoData> dataList;
    private int currentPositionOfDataList;
    private Context context;
    private CallbackPlaylistFolder callbackPlaylistFolder;
    //private int sizeW;
    //private int sizeH;

    public class PlayListHolder extends RecyclerView.ViewHolder {
        private TextView tvDuration;
        private TextView tvDuration2;
        private ImageView ivCover;
        private TextView tvName;
        private TextView tvYear;
        private TextView tvRate;
        private TextView tvDescription;
        private LinearLayout rootView;

        public PlayListHolder(View view) {
            super(view);
            rootView = view.findViewById(R.id.root_view);
            tvDuration = view.findViewById(R.id.tv_duration);
            tvDuration2 = view.findViewById(R.id.tv_duration_2);
            tvName = view.findViewById(R.id.tv_name);
            tvYear = view.findViewById(R.id.tv_year);
            tvRate = view.findViewById(R.id.tv_rate);
            tvDescription = view.findViewById(R.id.tv_description);
            ivCover = view.findViewById(R.id.iv_cover);
        }
    }

    public AdapterPlaylistFolder(Context context, List<VideoData> dataList, int currentPositionOfDataList, CallbackPlaylistFolder callbackPlaylistFolder) {
        this.context = context;
        this.dataList = dataList;
        this.currentPositionOfDataList = currentPositionOfDataList;
        this.callbackPlaylistFolder = callbackPlaylistFolder;
    }

    @Override
    @NonNull
    public PlayListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.v3_row_playlist_folder, parent, false);
        return new PlayListHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final PlayListHolder playListHolder, final int position) {
        final VideoData data = dataList.get(position);
        UZUtil.setTextDuration(playListHolder.tvDuration, data.getDuration());
        playListHolder.tvName.setText(data.getName());

        //TODO correct this
        playListHolder.tvYear.setText("2018");
        UZUtil.setTextDuration(playListHolder.tvDuration2, data.getDuration());

        //TODO correct this
        playListHolder.tvRate.setText("12+");
        if (data.getShortDescription() == null || data.getShortDescription().isEmpty()) {
            if (data.getDescription() == null || data.getDescription().isEmpty()) {
                playListHolder.tvDescription.setVisibility(View.GONE);
            } else {
                playListHolder.tvDescription.setText(data.getDescription());
                playListHolder.tvDescription.setVisibility(View.VISIBLE);
            }
        } else {
            playListHolder.tvDescription.setText(data.getShortDescription());
        }
        UzImageUtil.load(context, data.getThumbnail(), playListHolder.ivCover);

        playListHolder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UzAnimationUtil.play(v, Techniques.Pulse, new UzAnimationUtil.Callback() {
                    @Override
                    public void onCancel() {
                        //do nothing
                    }

                    @Override
                    public void onEnd() {
                        if (callbackPlaylistFolder != null) {
                            callbackPlaylistFolder.onClickItem(data, position);
                        }
                    }

                    @Override
                    public void onRepeat() {
                        //do nothing
                    }

                    @Override
                    public void onStart() {
                        //do nothing
                    }
                });
            }
        });

        playListHolder.rootView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean isFocus) {
                LLog.d(TAG, "onFocusChange isFocus: " + isFocus);
                if (isFocus) {
                    playListHolder.rootView.setBackgroundResource(R.drawable.bkg_item_playlist_folder);
                } else {
                    playListHolder.rootView.setBackgroundResource(0);
                }
                if (callbackPlaylistFolder != null) {
                    callbackPlaylistFolder.onFocusChange(data, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }
}