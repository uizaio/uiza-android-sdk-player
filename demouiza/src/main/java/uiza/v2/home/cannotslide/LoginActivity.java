package uiza.v2.home.cannotslide;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import uiza.R;
import vn.uiza.core.base.BaseActivity;
import vn.uiza.core.utilities.LUIUtil;
import vn.uiza.views.LToast;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private EditText etId;
    private EditText etPw;
    private TextView tvForgotPw;
    private TextView tvDontHaveAcc;
    private TextView tvLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        etId = (EditText) findViewById(R.id.et_id);
        etPw = (EditText) findViewById(R.id.et_pw);
        tvForgotPw = (TextView) findViewById(R.id.tv_forgot_pw);
        tvDontHaveAcc = (TextView) findViewById(R.id.tv_dont_have_acc);
        tvLogin = (TextView) findViewById(R.id.tv_login);

        tvForgotPw.setOnClickListener(this);
        tvDontHaveAcc.setOnClickListener(this);
        tvLogin.setOnClickListener(this);

        LUIUtil.setTextViewUnderLine(tvForgotPw);
        LUIUtil.setTextViewUnderLine(tvDontHaveAcc);

        findViewById(R.id.iv_close_activity).setOnClickListener(this);
    }

    @Override
    protected boolean setFullScreen() {
        return false;
    }

    @Override
    protected String setTag() {
        return getClass().getSimpleName();
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.uiza_login_activity;
    }

    private void login() {
        LToast.show(activity, "Click");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_forgot_pw:
                LToast.show(activity, "Click tv_forgot_pw");
                break;
            case R.id.tv_dont_have_acc:
                LToast.show(activity, "Click tv_dont_have_acc");
                break;
            case R.id.tv_login:
                login();
                break;
            case R.id.iv_close_activity:
                onBackPressed();
                break;
        }
    }
}
