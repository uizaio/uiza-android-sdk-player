package testlibuiza.sample.v3.demouibeta.detail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import testlibuiza.R;
import testlibuiza.sample.v3.demouibeta.utils.WWLVideoDataset;
import vn.loitp.core.base.BaseFragment;

/**
 * Created by loitp on 2/26/17.
 */
public class WWLVideoMetaInfoFragment extends BaseFragment {
    private TextView mTitleView;
    private TextView mSubTitleView;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.mTitleView = (TextView) view.findViewById(R.id.li_title);
        this.mSubTitleView = (TextView) view.findViewById(R.id.li_subtitle);
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.wwl_video_meta_info_fragment;
    }

    public void updateItem(WWLVideoDataset.DatasetItem item) {
        if (this.mTitleView != null) {
            this.mTitleView.setText(item.title);
            this.mSubTitleView.setText(item.subtitle);
        }
    }
}
