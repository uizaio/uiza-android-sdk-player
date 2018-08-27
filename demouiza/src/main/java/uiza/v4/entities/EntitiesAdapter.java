package uiza.v4.entities;

/**
 * Created by www.muathu@gmail.com on 12/8/2017.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import uiza.R;
import vn.loitp.core.utilities.LImageUtil;
import vn.loitp.restapi.uiza.model.v3.metadata.getdetailofmetadata.Data;

public class EntitiesAdapter extends RecyclerView.Adapter<EntitiesAdapter.DataHolder> {
    private int lastPosition = -1;
    private Context context;
    private Callback callback;
    private List<Data> dataList;

    public EntitiesAdapter(Context context, List<Data> dataList, Callback callback) {
        this.context = context;
        this.dataList = dataList;
        this.callback = callback;
    }

    @Override
    public DataHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.v4_item_data, parent, false);
        return new DataHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final DataHolder holder, final int position) {
        final Data data = dataList.get(position);
        holder.tvTitle.setText(data.getName());
        LImageUtil.load(context, data.getThumbnail(), holder.ivThumnail);

        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null) {
                    callback.onClick(data, position);
                }
            }
        });
        holder.rootView.setOnLongClickListener(new View.OnLongClickListener() {
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
        public void onClick(Data data, int position);

        public void onLongClick(Data data, int position);

        public void onLoadMore();
    }

    public class DataHolder extends RecyclerView.ViewHolder {
        public TextView tvTitle;
        public ImageView ivThumnail;
        public RelativeLayout rootView;

        public DataHolder(View view) {
            super(view);
            tvTitle = (TextView) view.findViewById(R.id.tv_title);
            ivThumnail = (ImageView) view.findViewById(R.id.iv_thumnail);
            rootView = (RelativeLayout) view.findViewById(R.id.root_view);
        }
    }
}