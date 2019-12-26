package testlibuiza.sample.utils;

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
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import testlibuiza.R;
import testlibuiza.sample.PlayerActivity;
import vn.uiza.restapi.model.v5.PlaybackInfo;
import vn.uiza.restapi.model.v5.UizaPlayback;
import vn.uiza.restapi.model.v5.vod.VODEntity;
import vn.uiza.utils.util.ViewUtils;

public class VODEntityAdapter extends RecyclerView.Adapter<VODEntityAdapter.ViewHolder> {

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
        holder.setThumbnail(entity);
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
        AppCompatImageView thumbnailView;
        AppCompatImageButton actionButton;

        ViewHolder(View root) {
            super(root);
            titleView = root.findViewById(R.id.tv_title);
            statusView = root.findViewById(R.id.tv_status);
            thumbnailView = root.findViewById(R.id.iv_thumbnail);
            actionButton = root.findViewById(R.id.action_button);
            statusView.setVisibility(View.GONE);
        }

        private void setThumbnail(VODEntity entity) {
            ViewUtils.loadImage(thumbnailView, entity.getThumbnail(), R.drawable.ic_person_white_48);
        }

        private void onClick(VODEntity entity) {
            Context context = itemView.getContext();
            PlaybackInfo info = entity.getPlaybackInfo();
            info.setUizaPlayback(new UizaPlayback("https://bitdash-a.akamaihd.net/content/sintel/hls/playlist.m3u8",
                    "https://mnmedias.api.telequebec.tv/m3u8/29880.m3u8"
                    , "https://s3-ap-southeast-1.amazonaws.com/cdnetwork-test/drm_sample_byterange/manifest.mpd"));
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
