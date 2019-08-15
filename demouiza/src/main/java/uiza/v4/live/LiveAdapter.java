package uiza.v4.live;

/**
 * Created by www.muathu@gmail.com on 12/8/2017.
 */

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import io.uiza.core.api.response.video.VideoData;
import io.uiza.core.util.UzDisplayUtil;
import io.uiza.core.util.UzImageUtil;
import io.uiza.core.util.constant.Constants;
import java.util.List;
import uiza.R;

public class LiveAdapter extends RecyclerView.Adapter<LiveAdapter.DataHolder> {
    private int lastPosition = -1;
    private Context context;
    private Callback callback;
    private List<VideoData> dataList;
    private int sizeH;

    public LiveAdapter(Context context, List<VideoData> dataList, Callback callback) {
        this.context = context;
        this.dataList = dataList;
        this.callback = callback;
        this.sizeH = UzDisplayUtil.getScreenWidth() * 9 / 16;
    }

    @Override
    public DataHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.v4_item_live, parent, false);
        return new DataHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final DataHolder holder, final int position) {
        final VideoData data = dataList.get(position);

        holder.cardView.getLayoutParams().height = sizeH;
        holder.cardView.requestLayout();

        if (data.getLastProcess() != null) {
            if (data.getLastProcess().toLowerCase().equals(Constants.LAST_PROCESS_START)) {
                holder.tvLastProcess.setVisibility(View.VISIBLE);
                //holder.ivLivestream.setVisibility(View.GONE);
            } else {
                holder.tvLastProcess.setVisibility(View.GONE);
                //holder.ivLivestream.setVisibility(View.VISIBLE);
            }
        } else {
            holder.tvLastProcess.setVisibility(View.GONE);
        }

        holder.tvTitle.setText(data.getName());
        UzDisplayUtil.setTextShadow(holder.tvTitle);
        UzImageUtil.load(context, data.getThumbnail(), holder.ivThumnail);
        if (data.getMode() == null) {
            holder.ivLivestream.setVisibility(View.GONE);
        } else {
            if (data.getMode().equals(Constants.MODE_PULL)) {
                holder.ivLivestream.setVisibility(View.GONE);
            } else if (data.getMode().equals(Constants.MODE_PUSH)) {
                holder.ivLivestream.setVisibility(View.VISIBLE);
            }
        }
        holder.ivLivestream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null) {
                    callback.onClickLivestream(data, position);
                }
            }
        });
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null) {
                    callback.onClick(data, position);
                }
            }
        });
        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (callback != null) {
                    callback.onLongClick(data, position);
                }
                return true;
            }
        });
        if (position == dataList.size() - 1) {
            if (callback != null) {
                callback.onLoadMore();
            }
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public interface Callback {
        public void onClick(VideoData data, int position);

        public void onClickLivestream(VideoData data, int position);

        public void onLongClick(VideoData data, int position);

        public void onLoadMore();
    }

    public class DataHolder extends RecyclerView.ViewHolder {
        public TextView tvTitle;
        public TextView tvLastProcess;
        public ImageView ivThumnail;
        public ImageView ivLivestream;
        public CardView cardView;

        public DataHolder(View view) {
            super(view);
            tvTitle = (TextView) view.findViewById(R.id.tv_title);
            tvLastProcess = (TextView) view.findViewById(R.id.tv_last_process);
            ivThumnail = (ImageView) view.findViewById(R.id.iv_thumnail);
            ivLivestream = (ImageView) view.findViewById(R.id.iv_livestream);
            cardView = (CardView) view.findViewById(R.id.card_view);
        }
    }
}