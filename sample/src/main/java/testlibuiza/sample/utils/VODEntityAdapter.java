package testlibuiza.sample.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Random;

import testlibuiza.R;
import testlibuiza.sample.PlayerActivity;
import vn.uiza.models.PlaybackInfo;
import vn.uiza.models.UizaPlayback;
import vn.uiza.models.vod.VODEntity;
import vn.uiza.utils.ImageUtils;

public class VODEntityAdapter extends RecyclerView.Adapter<VODEntityAdapter.ViewHolder> {

    public static final String[] thumbnails = new String[]{"https://cdn.pose.com.vn/assets/2019/04/22/do-my-linh-2.jpg",
            "https://vcdn-giaitri.vnecdn.net/2019/10/01/SHION-264-1569900835_680x0.jpg",
            "https://media.ex-cdn.com/EXP/media.phunutoday.vn/files/hai.pham/2017/04/21/luu-diec-phi-tam-sinh-tam-the-phunutoday-5-1139-phunutoday.jpg",
            "https://2sao.vietnamnetjsc.vn/images/2018/09/29/20/51/duong-tu-04.jpg", "https://nguoi-noi-tieng.com/images/post/maria-ozawa-nong-bong-tren-khan-dai-tran-dau-thai-lan-indonesia-871230.jpg",
            "https://dep365.com/wp-content/uploads/2019/11/img_5dce6569246fa.jpg", "http://giadinh.mediacdn.vn/thumb_w/640/2019/10/30/ngoc-trinh-5-1572423107231301246873-crop-15724237122011649225014.jpg", "https://haiquanonline.com.vn/stores/news_dataimages/hoannm/122019/19/17/in_article/2415_9-3835_phim_Mat_Biec.jpg",
            "https://media.tinmoi.vn/upload/honghanh/2019/05/07/086010-phi-huyen-trang-giau-co-va-noi-tieng-muc-nao-sau-tuyen-bo-tu-kiem-tie.jpg", "https://viknews.com/vi/wp-content/uploads/2019/04/Hot-girl-Trâm-Anh.jpg"};

    List<VODEntity> entities;
    OnMoreActionListener listener;

    public VODEntityAdapter() {
    }

    public void setEntities(List<VODEntity> entities) {
        this.entities = entities;
        notifyDataSetChanged();
    }

    public void setMoreActionListener(OnMoreActionListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_entity, parent, false);
        return new ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        VODEntity entity = entities.get(position);
        holder.titleView.setText(entity.getName());
        holder.setThumbnail(entity, position);
        holder.itemView.setOnClickListener(v -> holder.onClick(entity));
        if (listener != null) {
            holder.actionButton.setOnClickListener(v -> listener.onMoreClick(v, entity.getId()));
        }
    }

    @Override
    public int getItemCount() {
        if (entities == null)
            return 0;
        return entities.size();
    }

    public VODEntity getItemId(String id) {
        for (VODEntity entity : entities) {
            if (entity.getId().equalsIgnoreCase(id)) {
                return entity;
            }
        }
        return null;
    }

    public void removeItem(String id) {
        VODEntity entity = getItemId(id);
        if (entity != null) {
            entities.remove(entity);
            notifyDataSetChanged();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        AppCompatTextView titleView;
        AppCompatTextView statusView;
        AppCompatTextView tvViewers;
        AppCompatImageView thumbnailView;
        AppCompatImageButton actionButton;

        ViewHolder(View root) {
            super(root);
            titleView = root.findViewById(R.id.tv_title);
            statusView = root.findViewById(R.id.tv_status);
            tvViewers = root.findViewById(R.id.tv_viewers);
            thumbnailView = root.findViewById(R.id.iv_thumbnail);
            actionButton = root.findViewById(R.id.action_button);
            statusView.setVisibility(View.GONE);
            tvViewers.setText(String.valueOf((new Random().nextInt(1000))));

        }

        private void setThumbnail(VODEntity entity, int position) {
            if (TextUtils.isEmpty(entity.getThumbnail())) {
                String thumbnail = thumbnails[position % thumbnails.length];
                ImageUtils.load(thumbnailView, thumbnail, R.drawable.ic_person_white_48);
            } else {
                ImageUtils.load(thumbnailView, entity.getThumbnail(), R.drawable.ic_person_white_48);
            }
        }


        private void onClick(VODEntity entity) {
            Context context = itemView.getContext();
            PlaybackInfo info = entity.getPlaybackInfo();
            if (!info.canPlay()) {
                info.setUizaPlayback(new UizaPlayback("https://bitdash-a.akamaihd.net/content/sintel/hls/playlist.m3u8",
                        "https://mnmedias.api.telequebec.tv/m3u8/29880.m3u8"
                        , "https://s3-ap-southeast-1.amazonaws.com/cdnetwork-test/drm_sample_byterange/manifest.mpd"));
            }
            if (info.canPlay()) {
                Intent intent = new Intent(context, PlayerActivity.class);
                intent.putExtra("extra_playback_info", info);
                ((Activity) context).startActivityForResult(intent, 1001);
            } else {
                Toast.makeText(context, "No playback...", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
