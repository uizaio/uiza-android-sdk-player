package testlibuiza.sample.v3.demoui;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import testlibuiza.R;
import testlibuiza.sample.v3.demoui.interfaces.FragmentHost;
import testlibuiza.sample.v3.demoui.utils.WWLVideoDataset;
import vn.loitp.core.base.BaseFragment;
import vn.loitp.core.utilities.LImageUtil;

/**
 * Created by loitp on 2/26/17.
 */

public class HomeFragment extends BaseFragment {
    private final String TAG = getClass().getSimpleName();
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private CustomAdapter mAdapter;
    private FragmentHost mFragmentHost;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.mRecyclerView = (RecyclerView) frmRootView.findViewById(R.id.recyclerView);
        this.mLayoutManager = new LinearLayoutManager(getActivity());
        this.mRecyclerView.setLayoutManager(mLayoutManager);
        this.mAdapter = new CustomAdapter(WWLVideoDataset.datasetItemList);
        mRecyclerView.setAdapter(mAdapter);

        updateLayoutIfNeed();
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.wwl_video_home_fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mFragmentHost = (FragmentHost) context;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        updateLayoutIfNeed();
    }

    private void updateLayoutIfNeed() {
        if (this.mAdapter != null) {
            this.mAdapter.notifyDataSetChanged();
        }
    }

    private void onItemClicked(WWLVideoDataset.DatasetItem item) {
        if (this.mFragmentHost != null) {
            this.mFragmentHost.goToDetail(item);
        }
    }

    private class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
        private List<WWLVideoDataset.DatasetItem> datasetItemList;

        public CustomAdapter(List<WWLVideoDataset.DatasetItem> datasetItemList) {
            this.datasetItemList = datasetItemList;
            //LLog.d(TAG, "CustomAdapter datasetItemList " + datasetItemList.size());
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.wwl_video_card_row_item, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.getTitleView().setText(this.datasetItemList.get(position).title);
            holder.getSubTitleView().setText(this.datasetItemList.get(position).subtitle);
            LImageUtil.load(getActivity(), datasetItemList.get(position).getCover(), holder.getIvCover());
        }

        @Override
        public int getItemCount() {
            return this.datasetItemList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            private final TextView titleView;
            private final TextView subtitleView;
            private final ImageView ivCover;

            public ViewHolder(View v) {
                super(v);
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HomeFragment.this.onItemClicked(CustomAdapter.this.datasetItemList.get(getAdapterPosition()));
                    }
                });
                titleView = (TextView) v.findViewById(R.id.li_title);
                subtitleView = (TextView) v.findViewById(R.id.li_subtitle);
                ivCover = (ImageView) v.findViewById(R.id.iv_cover);
            }

            public TextView getTitleView() {
                return titleView;
            }

            public TextView getSubTitleView() {
                return subtitleView;
            }

            public ImageView getIvCover() {
                return ivCover;
            }
        }
    }
}
