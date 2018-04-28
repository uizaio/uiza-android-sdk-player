package uiza.activity.home.view;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import uiza.R;
import vn.loitp.views.placeholderview.lib.placeholderview.annotations.Click;
import vn.loitp.views.placeholderview.lib.placeholderview.annotations.Layout;
import vn.loitp.views.placeholderview.lib.placeholderview.annotations.NonReusable;
import vn.loitp.views.placeholderview.lib.placeholderview.annotations.Resolve;
import vn.loitp.views.placeholderview.lib.placeholderview.annotations.View;

/**
 * Created by www.muathu@gmail.com on 9/16/2017.
 */

@NonReusable
@Layout(R.layout.uiza_drawer_header)
public class UizaDrawerHeader {
    @View(R.id.iv_avatar)
    private ImageView ivAvatar;

    @View(R.id.tv_name)
    private TextView tvName;

    @View(R.id.iv_log_out)
    private ImageView ivLogOut;

    @View(R.id.ll_login)
    private LinearLayout llLogin;

    //@View(R.id.tv_setting)
    //private TextView tvSetting;

    @Resolve
    private void onResolved() {
        tvName.setText(R.string.login);
        ivLogOut.setVisibility(android.view.View.INVISIBLE);
        /*if (Constants.IS_DEBUG) {
            tvSetting.setVisibility(android.view.View.VISIBLE);
        } else {
            tvSetting.setVisibility(android.view.View.GONE);
        }*/
    }

    @Click(R.id.iv_log_out)
    private void onClickLogOut() {
        if (callback != null) {
            callback.onClickLogOut();
        }
    }

    @Click(R.id.ll_login)
    private void onClickLogin() {
        if (callback != null) {
            callback.onClickLogin();
        }
    }

    /*@Click(R.id.tv_setting)
    private void onClickSetting() {
        if (callback != null) {
            callback.onClickSetting();
        }
    }*/

    private Callback callback;

    public interface Callback {
        public void onClickLogOut();

        public void onClickLogin();

        //public void onClickSetting();
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }
}
