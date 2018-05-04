package uiza.activity.home.v2.canslide;

/**
 * Created by www.muathu@gmail.com on 12/24/2017.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import uiza.R;
import vn.loitp.core.base.BaseFragment;
import vn.loitp.restapi.uiza.model.v2.listallmetadata.Datum;
import vn.loitp.views.placeholderview.lib.placeholderview.PlaceHolderView;

public class FrmHome extends BaseFragment {
    private PlaceHolderView mDrawerView;
    private DrawerLayout mDrawerLayout;
    private List<Datum> datumList = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frm_home, container, false);
        return view;
    }
}
