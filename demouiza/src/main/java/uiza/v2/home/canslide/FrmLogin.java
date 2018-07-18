package uiza.v2.home.canslide;

/**
 * Created by www.muathu@gmail.com on 12/24/2017.
 */

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import uiza.R;
import vn.loitp.core.base.BaseFragment;
import vn.loitp.core.utilities.LUIUtil;
import vn.loitp.uizavideo.view.IOnBackPressed;
import vn.loitp.views.LToast;

public class FrmLogin extends BaseFragment implements View.OnClickListener, IOnBackPressed {
    private final String TAG = getClass().getSimpleName();
    private EditText etId;
    private EditText etPw;
    private TextView tvForgotPw;
    private TextView tvDontHaveAcc;
    private TextView tvLogin;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etId = (EditText) view.findViewById(R.id.et_id);
        etPw = (EditText) view.findViewById(R.id.et_pw);
        tvForgotPw = (TextView) view.findViewById(R.id.tv_forgot_pw);
        tvDontHaveAcc = (TextView) view.findViewById(R.id.tv_dont_have_acc);
        tvLogin = (TextView) view.findViewById(R.id.tv_login);

        tvForgotPw.setOnClickListener(this);
        tvDontHaveAcc.setOnClickListener(this);
        tvLogin.setOnClickListener(this);

        LUIUtil.setTextViewUnderLine(tvForgotPw);
        LUIUtil.setTextViewUnderLine(tvDontHaveAcc);

        view.findViewById(R.id.iv_close_activity).setOnClickListener(this);
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.frm_login;
    }

    private void login() {
        LToast.show(getActivity(), "Click");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_forgot_pw:
                LToast.show(getActivity(), "Click tv_forgot_pw");
                break;
            case R.id.tv_dont_have_acc:
                LToast.show(getActivity(), "Click tv_dont_have_acc");
                break;
            case R.id.tv_login:
                login();
                break;
            case R.id.iv_close_activity:
                getActivity().onBackPressed();
                break;
        }
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }
}
