package uiza.activity.option;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import uiza.R;
import vn.loitp.views.recyclerview.banner.BannerLayout;

/**
 * Created by test on 2017/11/22.
 */
public class SkinAdapter extends RecyclerView.Adapter<SkinAdapter.MzViewHolder> {
    private Context context;
    private List<SkinObject> skinObjectList;
    private BannerLayout.OnBannerItemClickListener onBannerItemClickListener;

    public SkinAdapter(Context context, List<SkinObject> skinObjectList) {
        this.context = context;
        this.skinObjectList = skinObjectList;
    }

    public void setOnBannerItemClickListener(BannerLayout.OnBannerItemClickListener onBannerItemClickListener) {
        this.onBannerItemClickListener = onBannerItemClickListener;
    }

    @Override
    public SkinAdapter.MzViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MzViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false));
    }

    @Override
    public void onBindViewHolder(SkinAdapter.MzViewHolder holder, final int position) {
        if (skinObjectList == null || skinObjectList.isEmpty()) {
            return;
        }
        SkinObject skinObject = skinObjectList.get(position);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onBannerItemClickListener != null) {
                    onBannerItemClickListener.onItemClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (skinObjectList != null) {
            return skinObjectList.size();
        }
        return 0;
    }


    public class MzViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        MzViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image);
        }
    }

}
