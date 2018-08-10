package testlibuiza.sample.v3.demouibeta.detail;

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
import testlibuiza.sample.v3.demouibeta.interfaces.FragmentHost;
import testlibuiza.sample.v3.demouibeta.utils.WWLVideoDataset;
import vn.loitp.core.base.BaseFragment;
import vn.loitp.core.utilities.LImageUtil;

/**
 * Created by loitp on 2/26/17.
 */
public class WWLVideoUpNextFragment extends BaseFragment {
    private FragmentHost mFragmentHost;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private CustomAdapter mAdapter;
    private boolean mIsLandscape;

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

        /*this.mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return WWLVideoUpNextFragment.this.getSpanSize(position);
            }
        });*/

        updateLayoutIfNeed();
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.wwl_video_up_next_fragment;
    }

    /*private int getSpanSize(int position) {
        int spanSize = this.mLayoutManager.getSpanCount();
        if (this.mIsLandscape) {
            return spanSize;
        }
        if (position == 0) {
            return spanSize;
        }
        position--;
        if (position == 0) {
            return spanSize;
        }
        return 1;
    }*/

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

    public void updateItem(WWLVideoDataset.DatasetItem item) {
        if (this.mAdapter != null) {
            this.mAdapter.updateHeader(item);
            this.mAdapter.notifyDataSetChanged();
        }
    }

    private void updateLayoutIfNeed() {
        boolean enable = true;
        int orientation = getResources().getConfiguration().orientation;
        this.mIsLandscape = orientation == Configuration.ORIENTATION_LANDSCAPE;
        if (this.mIsLandscape) {
            enable = false;
        }
        /*if (this.mLayoutManager != null) {
            if (this.mIsLandscape) {
                this.mLayoutManager.setSpanCount(1);
            } else {
                this.mLayoutManager.setSpanCount(WWLUiUtil.getGridColumnCount(getResources()));
            }
        }*/
        if (this.mAdapter != null) {
            this.mAdapter.setHeader(enable);
            this.mAdapter.notifyDataSetChanged();
        }
    }

    private void onItemClicked(WWLVideoDataset.DatasetItem item) {
        if (this.mFragmentHost != null) {
            this.mFragmentHost.goToDetail(item);
        }
    }

    public class CustomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private static final int HEADER = 0;
        private static final int TITLE = 1;
        private static final int OTHER = 2;
        public boolean mHasHeader;
        private List<WWLVideoDataset.DatasetItem> datasetItemList;
        private WWLVideoDataset.DatasetItem datasetItem;

        public CustomAdapter(List<WWLVideoDataset.DatasetItem> dataset) {
            this.datasetItemList = dataset;
            this.mHasHeader = true;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == HEADER) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.wwl_video_card_header_item, parent, false);
                return new HeaderViewHolder(v);
            }
            if (viewType == TITLE) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.wwl_video_card_title_item, parent, false);
                return new TitleViewHolder(v);
            }
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.wwl_video_card_row_item, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof HeaderViewHolder) {
                if (this.datasetItem != null) {
                    ((HeaderViewHolder) holder).getTitleView().setText(this.datasetItem.title);
                    ((HeaderViewHolder) holder).getSubTitleView().setText(this.datasetItem.subtitle);
                }
            } else if (holder instanceof TitleViewHolder) {
                ((TitleViewHolder) holder).getTitleView().setText(getString(R.string.large_text));
            } else if (holder instanceof ViewHolder) {
                ((ViewHolder) holder).getTitleView().setText(getItem(position).title);
                ((ViewHolder) holder).getSubTitleView().setText(getItem(position).subtitle);
                LImageUtil.load(getActivity(), datasetItem.getCover(), ((ViewHolder) holder).ivCover);
            }
        }

        @Override
        public int getItemCount() {
            return (mHasHeader ? 1 : 0) + 1 + this.datasetItemList.size();
        }

        @Override
        public int getItemViewType(int position) {
            if (mHasHeader) {
                if (position == 0) {
                    return HEADER;
                }
                position--;
            }
            if (position == 0) {
                return TITLE;
            }
            return OTHER;
        }

        public void setHeader(boolean enable) {
            this.mHasHeader = enable;
        }

        public void updateHeader(WWLVideoDataset.DatasetItem item) {
            this.datasetItem = item;
        }

        private WWLVideoDataset.DatasetItem getItem(int position) {
            return this.datasetItemList.get(position - 1 - (this.mHasHeader ? 1 : 0));
        }

        class HeaderViewHolder extends RecyclerView.ViewHolder {
            private final TextView titleView;
            private final TextView subtitleView;

            public HeaderViewHolder(View v) {
                super(v);
                titleView = (TextView) v.findViewById(R.id.li_title);
                subtitleView = (TextView) v.findViewById(R.id.li_subtitle);
            }

            public TextView getTitleView() {
                return titleView;
            }

            public TextView getSubTitleView() {
                return subtitleView;
            }
        }

        class TitleViewHolder extends RecyclerView.ViewHolder {
            private final TextView titleView;

            public TitleViewHolder(View v) {
                super(v);
                titleView = (TextView) v.findViewById(R.id.li_title);
            }

            public TextView getTitleView() {
                return titleView;
            }
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
                        WWLVideoUpNextFragment.this.onItemClicked(CustomAdapter.this.getItem(getAdapterPosition()));
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
