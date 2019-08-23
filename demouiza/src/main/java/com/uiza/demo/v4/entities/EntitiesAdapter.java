package com.uiza.demo.v4.entities;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.uiza.demo.R;
import io.uiza.core.api.response.video.VideoData;
import io.uiza.core.util.UzDisplayUtil;
import io.uiza.core.util.UzImageUtil;
import io.uiza.core.util.constant.Constants;
import java.util.List;

public class EntitiesAdapter extends RecyclerView.Adapter<EntitiesAdapter.DataHolder> {
    private int lastPosition = -1;
    private Context context;
    private Callback callback;
    private List<VideoData> dataList;
    private int sizeH;

    public EntitiesAdapter(Context context, List<VideoData> dataList, Callback callback) {
        this.context = context;
        this.dataList = dataList;
        this.callback = callback;
        this.sizeH = UzDisplayUtil.getScreenWidth() * 9 / 16;
    }

    @Override
    public DataHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.v4_item_entities, parent, false);
        return new DataHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final DataHolder holder, final int position) {
        final VideoData data = dataList.get(position);

        holder.cardView.getLayoutParams().height = sizeH;
        holder.cardView.requestLayout();

        holder.tvTitle.setText(data.getName());
        UzDisplayUtil.setTextShadow(holder.tvTitle);
        String stt = data.getPublishToCdn() + "";
        holder.tvStatus.setText(stt);
        UzDisplayUtil.setTextShadow(holder.tvStatus);
        if (stt.equals(Constants.SUCCESS)) {
            holder.tvStatus.setTextColor(Color.GREEN);
        } else if (stt.equals(Constants.NOT_READY)) {
            holder.tvStatus.setTextColor(Color.YELLOW);
        } else {
            holder.tvStatus.setTextColor(Color.RED);
        }

        UzImageUtil.load(context, data.getThumbnail(), holder.ivThumbnail);

        holder.cardView.setOnClickListener(v -> {
            if (callback != null) {
                callback.onClick(data, position);
            }
        });
        holder.cardView.setOnLongClickListener(v -> {
            if (callback != null) {
                callback.onLongClick(data, position);
            }
            return true;
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
        void onClick(VideoData data, int position);

        void onLongClick(VideoData data, int position);

        void onLoadMore();
    }

    public class DataHolder extends RecyclerView.ViewHolder {
        public TextView tvTitle;
        public TextView tvStatus;
        public ImageView ivThumbnail;
        public CardView cardView;

        public DataHolder(View view) {
            super(view);
            tvTitle = view.findViewById(R.id.tv_title);
            tvStatus = view.findViewById(R.id.tv_status);
            ivThumbnail = view.findViewById(R.id.iv_thumnail);
            cardView = view.findViewById(R.id.card_view);
        }
    }
}