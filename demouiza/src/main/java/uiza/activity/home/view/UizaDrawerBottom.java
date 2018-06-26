package uiza.activity.home.view;

import android.widget.TextView;

import uiza.R;
import vn.loitp.utils.util.AppUtils;
import vn.loitp.views.placeholderview.lib.placeholderview.annotations.Layout;
import vn.loitp.views.placeholderview.lib.placeholderview.annotations.NonReusable;
import vn.loitp.views.placeholderview.lib.placeholderview.annotations.Resolve;
import vn.loitp.views.placeholderview.lib.placeholderview.annotations.View;

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
