package uiza.v4.categories;

/**
 * Created by www.muathu@gmail.com on 12/24/2017.
 */

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import uiza.R;
import vn.loitp.core.base.BaseFragment;
import vn.loitp.core.utilities.LUIUtil;
import vn.loitp.uizavideo.view.IOnBackPressed;

public class FrmCategories extends BaseFragment implements IOnBackPressed {
    private final String TAG = getClass().getSimpleName();
    private RecyclerView recyclerView;
    private TextView tvMsg;
    private ProgressBar pb;

    @Override
    protected int setLayoutResourceId() {
        return R.layout.v4_frm_categories;
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv);
        tvMsg = (TextView) view.findViewById(R.id.tv_msg);
        pb = (ProgressBar) view.findViewById(R.id.pb);
        LUIUtil.setColorProgressBar(pb, Color.WHITE);
    }
}
