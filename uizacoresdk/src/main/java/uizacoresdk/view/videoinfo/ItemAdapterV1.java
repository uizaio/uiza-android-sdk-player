package uizacoresdk.view.videoinfo;

/**
 * Created by www.muathu@gmail.com on 12/8/2017.
 */

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.androidanimations.library.Techniques;

import java.util.List;

import uizacoresdk.R;
import vn.uiza.core.common.Constants;
import vn.uiza.restapi.model.v2.listallentity.Item;
import vn.uiza.utils.ImageUtil;
import vn.uiza.utils.LAnimationUtil;
import vn.uiza.utils.LUIUtil;

public class ItemAdapterV1 extends RecyclerView.Adapter<ItemAdapterV1.ItemViewHolder> {

    public interface Callback {
        void onClickItemBottom(Item item, int position);

        void onLoadMore();
    }

    private Callback callback;
    private List<Item> itemList;
    private int mSizeW;
    private int mSizeH;

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public ProgressBar progressBar;
        private TextView tvName;

        public ItemViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.imageView);
            progressBar = view.findViewById(R.id.pb);
            tvName = view.findViewById(R.id.tv_name);
        }
    }

    public ItemAdapterV1(List<Item> itemList, int sizeW, int sizeH, Callback callback) {
        this.itemList = itemList;
        this.mSizeW = sizeW;
        this.mSizeH = sizeH;
        this.callback = callback;
    }

    @Override
    @NonNull
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_more_like_this, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, final int position) {
        final Item item = itemList.get(position);
        holder.imageView.getLayoutParams().width = mSizeW;
        holder.imageView.getLayoutParams().height = mSizeH;
        holder.imageView.requestLayout();

        if (item.getThumbnail() == null || item.getThumbnail().isEmpty()) {
            ImageUtil.load(holder.imageView, Constants.URL_IMG_16x9, holder.progressBar);
        } else {
            ImageUtil.load(holder.imageView, Constants.PREFIXS + item.getThumbnail(), holder.progressBar);
        }

        holder.tvName.setText(item.getName());
        LUIUtil.setTextShadow(holder.tvName);
        holder.imageView.setOnClickListener(v -> {
            if (callback != null) {
                LAnimationUtil.play(v, Techniques.Pulse, new LAnimationUtil.Callback() {
                    @Override
                    public void onCancel() {
                        //do nothing
                    }

                    @Override
                    public void onEnd() {
                        callback.onClickItemBottom(item, position);
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
        if (position == itemList.size() - 1) {
            if (callback != null) {
                callback.onLoadMore();
            }
        }
    }

    @Override
    public int getItemCount() {
        return itemList == null ? 0 : itemList.size();
    }
}