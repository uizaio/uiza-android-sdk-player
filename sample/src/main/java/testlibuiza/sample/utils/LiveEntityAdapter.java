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
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import testlibuiza.BuildConfig;
import testlibuiza.R;
import testlibuiza.sample.v5.LivePlaybackActivity;
import vn.uiza.restapi.model.v5.live.LiveEntity;
import vn.uiza.restapi.model.v5.live.LiveStatus;

public class LiveEntityAdapter extends RecyclerView.Adapter<LiveEntityAdapter.ViewHolder> {

    List<LiveEntity> entities;

    OnMoreActionListener listener;

    public LiveEntityAdapter() {
    }

    public void setEntities(List<LiveEntity> entities) {
        this.entities = entities;
        notifyDataSetChanged();
    }

    public void setOnMoreListener(OnMoreActionListener listener) {
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
        LiveEntity entity = entities.get(position);
        holder.titleView.setText(entity.getName());
        holder.setStatusView(entity.getStatus());
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

    public LiveEntity getItemId(String id) {
        for (LiveEntity entity : entities) {
            if (entity.getId().equalsIgnoreCase(id)) {
                return entity;
            }
        }
        return null;
    }

    public void removeItem(String id) {
        LiveEntity entity = getItemId(id);
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
        }

        private void setStatusView(LiveStatus status) {
            if (status == null) {
                statusView.setBackground(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.background_err, null));
                statusView.setText("Error");
                return;
            }
            switch (status) {
                case INIT:
                case READY:
                    statusView.setBackground(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.background_init, null));
                    statusView.setText("Offline");
                    break;
                case BROADCASTING:
                    statusView.setBackground(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.background_live, null));
                    statusView.setText(status.getValue());
                    break;
            }

        }

        private void onClick(LiveEntity entity) {
            Context context = itemView.getContext();
            if (entity.isOnline() || BuildConfig.DEBUG) {
                Intent liveIntent = new Intent(context, LivePlaybackActivity.class);
                liveIntent.putExtra("extra_live_entity", entity);
                ((Activity) context).startActivityForResult(liveIntent, 1001);
            } else {
                Toast.makeText(context, "Thread is offline", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
