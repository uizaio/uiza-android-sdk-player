package io.uiza.samplelive;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import vn.uiza.restapi.model.v5.live.LiveEntity;

public class LiveEntityAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;

    List<LiveEntity> entities;
    OnActionListener listener;
    private boolean isLoadingAdded = false;

    public LiveEntityAdapter() {
        entities = new ArrayList<>();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == entities.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
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
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == LOADING) {
            View rootLoad = inflater.inflate(R.layout.row_loading, parent, false);
            viewHolder = new LoadingViewHolder(rootLoad);
        } else {
            View root = inflater.inflate(R.layout.row_entity, parent, false);
            viewHolder = new ItemViewHolder(root);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        LiveEntity entity = entities.get(position);

        switch (getItemViewType(position)) {
            case LOADING:
                // Nothing
                break;
            default:
                ItemViewHolder itemHolder = (ItemViewHolder) holder;
                itemHolder.titleView.setText(entity.getName());
                if (entity.getStatus() != null)
                    itemHolder.descView.setText(entity.getStatus().getValue());
                else
                    itemHolder.descView.setText("");
                itemHolder.itemView.setOnClickListener(v -> {
                    Intent intent = new Intent(holder.itemView.getContext(), CheckLiveActivity.class);
                    intent.putExtra(CheckLiveActivity.EXTRA_ENTITY, entity);
                    holder.itemView.getContext().startActivity(intent);
                });
                if (listener != null) {
                    itemHolder.actionBtn.setOnClickListener(v -> listener.onMoreClick(v, entity.getId()));
                }
                break;
        }

    }

    protected static class ItemViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        AppCompatTextView titleView;
        AppCompatTextView descView;
        AppCompatImageButton actionBtn;

        ItemViewHolder(View root) {
            super(root);
            titleView = root.findViewById(R.id.tv_title);
            descView = root.findViewById(R.id.tv_description);
            actionBtn = root.findViewById(R.id.action_button);
        }
    }

    protected class LoadingViewHolder extends RecyclerView.ViewHolder {

        public LoadingViewHolder(View root) {
            super(root);
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

    public void addLoadingFooter() {
        isLoadingAdded = true;
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;
    }

    public void setListener(OnActionListener listener) {
        this.listener = listener;
    }


    interface OnActionListener {
        void onMoreClick(View v, String entityId);
    }
}
