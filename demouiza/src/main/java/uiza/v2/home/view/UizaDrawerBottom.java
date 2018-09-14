package uiza.v2.home.view;

import android.widget.TextView;

import uiza.R;
import vn.uiza.utils.util.AppUtils;
import vn.uiza.views.placeholderview.lib.placeholderview.annotations.Layout;
import vn.uiza.views.placeholderview.lib.placeholderview.annotations.NonReusable;
import vn.uiza.views.placeholderview.lib.placeholderview.annotations.Resolve;
import vn.uiza.views.placeholderview.lib.placeholderview.annotations.View;

/**
 * Created by www.muathu@gmail.com on 9/16/2017.
 */

@NonReusable
@Layout(R.layout.uiza_drawer_bottom)
public class UizaDrawerBottom {
    @View(R.id.tv_vs)
    private TextView tvVs;

    @Resolve
    private void onResolved() {
        tvVs.setText("Version " + AppUtils.getAppVersionCode());
    }
}
