package uiza.v4.home;

/**
 * Created by www.muathu@gmail.com on 12/24/2017.
 */

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;

import uiza.R;
import vn.uiza.core.base.BaseFragment;
import vn.uiza.core.common.Constants;
import vn.uiza.core.utilities.LImageUtil;
import vn.uiza.core.utilities.LUIUtil;
import uizacoresdk.view.IOnBackPressed;

public class FrmHome extends BaseFragment implements IOnBackPressed {

    @Override
    protected String setTag() {
        return "Home";
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView iv = (ImageView) view.findViewById(R.id.iv);
        LImageUtil.load(getActivity(), Constants.URL_IMG_THUMBNAIL_2, iv);

        ScrollView scrollView = (ScrollView) view.findViewById(R.id.scroll_view);
        LUIUtil.setPullLikeIOSVertical(scrollView);
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.v4_frm_home;
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }
}