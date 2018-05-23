package uiza.activity.home.v2.login;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import uiza.R;
import vn.loitp.core.base.BaseActivity;
import vn.loitp.core.utilities.LUIUtil;
import vn.loitp.views.LToast;

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
        /*LSService service = RestClientV2.createService(LSService.class);
        String id = "a";
        String pw = "a";
        subscribe(service.login(id, pw), new ApiSubscriber<Object>() {
            @Override
            public void onSuccess(Object result) {
                LLog.d(TAG, "onSuccess " + LSApplication.getInstance().getGson().toJson(result));
            }

            @Override
            public void onFail(Throwable e) {
                handleException(e);
            }
        });*/
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
