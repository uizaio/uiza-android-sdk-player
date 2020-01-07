package io.uiza.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import io.uiza.samplelive.CheckLiveActivity;
import io.uiza.samplelive.R;
import io.uiza.samplelive.SampleLiveApplication;
import io.uiza.samplelive.UizaLiveActivity;
import vn.uiza.models.live.LiveEntity;
import vn.uiza.models.live.LiveStatus;
import vn.uiza.utils.ImageUtil;

public class LiveEntityAdapter extends RecyclerView.Adapter<LiveEntityAdapter.ItemViewHolder> {

    public static final String[] thumbnails = new String[]{"https://cdn.pose.com.vn/assets/2019/04/22/do-my-linh-2.jpg",
            "https://vcdn-giaitri.vnecdn.net/2019/10/01/SHION-264-1569900835_680x0.jpg",
            "https://media.ex-cdn.com/EXP/media.phunutoday.vn/files/hai.pham/2017/04/21/luu-diec-phi-tam-sinh-tam-the-phunutoday-5-1139-phunutoday.jpg",
            "https://2sao.vietnamnetjsc.vn/images/2018/09/29/20/51/duong-tu-04.jpg", "https://nguoi-noi-tieng.com/images/post/maria-ozawa-nong-bong-tren-khan-dai-tran-dau-thai-lan-indonesia-871230.jpg",
            "https://dep365.com/wp-content/uploads/2019/11/img_5dce6569246fa.jpg", "http://giadinh.mediacdn.vn/thumb_w/640/2019/10/30/ngoc-trinh-5-1572423107231301246873-crop-15724237122011649225014.jpg", "https://haiquanonline.com.vn/stores/news_dataimages/hoannm/122019/19/17/in_article/2415_9-3835_phim_Mat_Biec.jpg",
            "https://media.tinmoi.vn/upload/honghanh/2019/05/07/086010-phi-huyen-trang-giau-co-va-noi-tieng-muc-nao-sau-tuyen-bo-tu-kiem-tie.jpg", "https://viknews.com/vi/wp-content/uploads/2019/04/Hot-girl-Trâm-Anh.jpg"};


    private List<LiveEntity> entities;
    private OnActionListener listener;
    private boolean isLoadingAdded = false;

    public LiveEntityAdapter() {
        entities = new ArrayList<>();
    }

    public void setEntities(List<LiveEntity> entities) {
        this.entities = entities;
        notifyDataSetChanged();
    }

    public List<LiveEntity> getEntities() {
        return entities;
    }

    @Override
    public int getItemCount() {
        return entities == null ? 0 : isLoadingAdded ? entities.size() + 1 : entities.size();
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View root = inflater.inflate(R.layout.row_entity, parent, false);
        return new ItemViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        LiveEntity entity = entities.get(position);
        holder.titleView.setText(entity.getName());
        holder.setStatusView(entity.getStatus());
        holder.setThumbnailView(position);
        holder.itemView.setOnClickListener(v -> holder.onClick(entity));
        if (listener != null) {
            holder.actionBtn.setOnClickListener(v -> listener.onMoreClick(v, entity.getId()));
        }

    }

    protected static class ItemViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        AppCompatTextView titleView;
        AppCompatTextView statusView;
        AppCompatImageButton actionBtn;
        AppCompatImageView thumbnailView;

        ItemViewHolder(View root) {
            super(root);
            thumbnailView = root.findViewById(R.id.iv_thumbnail);
            titleView = root.findViewById(R.id.tv_title);
            statusView = root.findViewById(R.id.tv_status);
            actionBtn = root.findViewById(R.id.action_button);
        }

        public void setThumbnailView(int position) {
            String thumbnail = thumbnails[position % thumbnails.length];
            ImageUtil.load(thumbnailView, thumbnail, R.drawable.ic_person_white_48);
        }

        private void setStatusView(LiveStatus status) {
            if (status == null) {
                statusView.setBackground(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.background_err, null));
                statusView.setText("Error");
                return;
            }
            switch (status) {
                case INIT:
                    statusView.setBackground(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.background_init, null));
                    break;
                case READY:
                    statusView.setBackground(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.background_ready, null));
                    break;
                case BROADCASTING:
                    statusView.setBackground(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.background_live, null));
                    break;
            }
            statusView.setText(status.getValue());
        }

        private void onClick(LiveEntity entity) {
            Context context = itemView.getContext();
            if (entity.canLive()) {
                Intent liveIntent = new Intent(context, UizaLiveActivity.class);
                liveIntent.putExtra(SampleLiveApplication.EXTRA_STREAM_ID, entity.getId());
                liveIntent.putExtra(SampleLiveApplication.EXTRA_STREAM_ENDPOINT, entity.getIngest().getStreamLink());
                ((Activity) context).startActivityForResult(liveIntent, 1001);
            } else if (entity.isOnline()) {
                Toast.makeText(context, "Thread is streaming..", Toast.LENGTH_LONG).show();
            } else {
                Intent intent = new Intent(context, CheckLiveActivity.class);
                intent.putExtra(CheckLiveActivity.EXTRA_ENTITY, entity);
                ((Activity) context).startActivityForResult(intent, 1001);
            }
        }
    }

    /**
     * ------ helper -------
     */

    public LiveEntity getItemId(String id) {
        for (LiveEntity entity : entities) {
            if (entity.getId().equalsIgnoreCase(id)) {
                return entity;
            }
        }
        return null;
    }

    public LiveEntity getItem(int position) {
        return entities.get(position);
    }

    public void removeItem(String id) {
        LiveEntity entity = getItemId(id);
        if (entity != null) {
            remove(entity);
        }
    }

    public void remove(LiveEntity entity) {
        int position = entities.indexOf(entity);
        if (position > -1) {
            entities.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void replace(LiveEntity entity) {
        int position = entities.indexOf(entity);
        if (position < entities.size() && position >= 0) {
            entities.remove(position);
            entities.add(position, entity);
            notifyItemChanged(position);
        }

    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public void addAll(List<LiveEntity> data) {
        for (LiveEntity e : data) {
            add(e);
        }
    }


    public void add(LiveEntity entity) {
        entities.add(entity);
        notifyItemInserted(entities.size() - 1);
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    public void setListener(OnActionListener listener) {
        this.listener = listener;
    }


    public interface OnActionListener {
        void onMoreClick(View v, String entityId);
    }
}
