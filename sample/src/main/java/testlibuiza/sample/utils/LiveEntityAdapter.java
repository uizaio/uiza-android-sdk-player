package testlibuiza.sample.utils;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import testlibuiza.R;
import testlibuiza.sample.v5.LivePlaybackActivity;
import vn.uiza.restapi.model.v5.live.LiveEntity;

public class LiveEntityAdapter extends RecyclerView.Adapter<LiveEntityAdapter.ViewHolder> {

    List<LiveEntity> entities;

    public LiveEntityAdapter() {
    }

    public void setEntities(List<LiveEntity> entities) {
        this.entities = entities;
        notifyDataSetChanged();
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
        holder.descView.setText(entity.getStatus().getValue());
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), LivePlaybackActivity.class);
            intent.putExtra("extra_live_entity", entity);
            holder.itemView.getContext().startActivity(intent);
        });
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
        AppCompatTextView descView;
        AppCompatImageView thumbnailView;

        ViewHolder(View root) {
            super(root);
            titleView = root.findViewById(R.id.tv_title);
            descView = root.findViewById(R.id.tv_description);
            thumbnailView = root.findViewById(R.id.iv_thumbnail);
        }
    }
}
