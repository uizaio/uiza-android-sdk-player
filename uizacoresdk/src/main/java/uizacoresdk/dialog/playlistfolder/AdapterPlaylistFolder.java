package uizacoresdk.dialog.playlistfolder;

/**
 * Created by www.muathu@gmail.com on 11/7/2017.
 */

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.androidanimations.library.Techniques;

import java.util.List;

import timber.log.Timber;
import uizacoresdk.R;
import uizacoresdk.util.UZUtil;
import vn.uiza.models.PlaybackInfo;
import vn.uiza.utils.ImageUtil;
import vn.uiza.utils.LAnimationUtil;

public class AdapterPlaylistFolder extends RecyclerView.Adapter<AdapterPlaylistFolder.PlayListHolder> {
    private final String TAG = getClass().getSimpleName();
    private List<PlaybackInfo> playList;
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
        private CardView rootView;

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

    public AdapterPlaylistFolder(Context context, List<PlaybackInfo> playList, int currentPositionOfDataList, CallbackPlaylistFolder callbackPlaylistFolder) {
        this.context = context;
        this.playList = playList;
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
        final PlaybackInfo data = playList.get(position);
        UZUtil.setTextDuration(playListHolder.tvDuration, String.valueOf(data.getDuration()));
        playListHolder.tvName.setText(data.getName());

        //TODO correct this
        playListHolder.tvYear.setText("2018");
        UZUtil.setTextDuration(playListHolder.tvDuration2, String.valueOf(data.getDuration()));

        //TODO correct this
        playListHolder.tvRate.setText("12+");
        if (TextUtils.isEmpty(data.getDescription())) {
            playListHolder.tvDescription.setVisibility(View.GONE);
        } else {
            playListHolder.tvDescription.setText(data.getDescription());
            playListHolder.tvDescription.setVisibility(View.VISIBLE);
        }

        ImageUtil.load(playListHolder.ivCover, data.getThumbnail());

        playListHolder.rootView.setOnClickListener(v -> LAnimationUtil.play(v, Techniques.Pulse, new LAnimationUtil.Callback() {
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
        }));

        playListHolder.rootView.setOnFocusChangeListener((view, isFocus) -> {
            Timber.d("onFocusChange isFocus: %b", isFocus);
            if (isFocus) {
                playListHolder.rootView.setBackgroundResource(R.drawable.bkg_item_playlist_folder);
            } else {
                playListHolder.rootView.setBackgroundResource(0);
            }
            if (callbackPlaylistFolder != null) {
                callbackPlaylistFolder.onFocusChange(data, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return playList == null ? 0 : playList.size();
    }
}