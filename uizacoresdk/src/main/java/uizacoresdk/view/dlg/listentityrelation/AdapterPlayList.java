package uizacoresdk.view.dlg.listentityrelation;

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

import java.util.List;

import uizacoresdk.R;
import uizacoresdk.util.UZUtil;
import vn.uiza.core.common.Constants;
import vn.uiza.core.utilities.LAnimationUtil;
import vn.uiza.core.utilities.LImageUtil;
import vn.uiza.restapi.uiza.model.v2.listallentity.Item;

public class AdapterPlayList extends RecyclerView.Adapter<AdapterPlayList.PlayListHolder> {
    private final String TAG = getClass().getSimpleName();
    private List<Item> itemList;
    private Context context;
    private CallbackPlayList callbackPlayList;

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

    public AdapterPlayList(Context context, List<Item> itemList, CallbackPlayList callbackPlayList) {
        this.itemList = itemList;
        this.callbackPlayList = callbackPlayList;
        this.context = context;
    }

    @Override
    @NonNull
    public PlayListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_playlist, parent, false);
        return new PlayListHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayListHolder playListHolder, final int position) {
        final Item item = itemList.get(position);
        UZUtil.setTextDuration(playListHolder.tvDuration, item.getDuration());
        playListHolder.tvName.setText(item.getName());

        //TODO
        playListHolder.tvYear.setText("2018");
        UZUtil.setTextDuration(playListHolder.tvDuration2, item.getDuration());

        //TODO
        playListHolder.tvRate.setText("18+");
        if (item.getShortDescription() == null || item.getShortDescription().isEmpty()) {
            if (item.getDescription() == null || item.getDescription().isEmpty()) {
                playListHolder.tvDescription.setVisibility(View.GONE);
            } else {
                playListHolder.tvDescription.setText(item.getDescription());
                playListHolder.tvDescription.setVisibility(View.VISIBLE);
            }
        } else {
            playListHolder.tvDescription.setText(item.getShortDescription());
        }

        String thumbnail;
        if (item.getThumbnail() == null || item.getThumbnail().isEmpty()) {
            thumbnail = Constants.URL_IMG_THUMBNAIL;
        } else {
            thumbnail = Constants.PREFIXS_SHORT + item.getThumbnail();
        }
        LImageUtil.load(context, thumbnail, playListHolder.ivCover);

        playListHolder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LAnimationUtil.play(v, Techniques.Pulse, new LAnimationUtil.Callback() {
                    @Override
                    public void onCancel() {
                        //do nothing
                    }

                    @Override
                    public void onEnd() {
                        if (callbackPlayList != null) {
                            callbackPlayList.onClickItem(item, position);
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
    }

    @Override
    public int getItemCount() {
        return itemList == null ? 0 : itemList.size();
    }
}