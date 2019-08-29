package com.uiza.demo.v4.login;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import com.uiza.demo.R;
import com.uiza.demo.v4.HomeV4CanSlideActivity;
import com.uiza.demo.v4.helper.utils.KeyboardUtils;
import io.uiza.core.util.UzDisplayUtil;
import io.uiza.core.util.constant.Constants;
import io.uiza.core.view.LToast;
import io.uiza.player.interfaces.IOnBackPressed;

public class FrmLogin extends Fragment implements View.OnClickListener, IOnBackPressed {
    private final String TAG = getClass().getSimpleName();
    private EditText etId;
    private EditText etPw;
    private TextView tvForgotPw;
    private TextView tvDontHaveAcc;
    private TextView tvLogin;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.v4_frm_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etId = view.findViewById(R.id.et_id);
        etPw = view.findViewById(R.id.et_pw);
        tvForgotPw = view.findViewById(R.id.tv_forgot_pw);
        tvDontHaveAcc = view.findViewById(R.id.tv_dont_have_acc);
        tvLogin = view.findViewById(R.id.tv_login);

        tvForgotPw.setOnClickListener(this);
        tvDontHaveAcc.setOnClickListener(this);
        tvLogin.setOnClickListener(this);

        UzDisplayUtil.setTextViewUnderLine(tvForgotPw);
        UzDisplayUtil.setTextViewUnderLine(tvDontHaveAcc);

        //view.findViewById(R.id.iv_close_activity).setOnClickListener(this);

        etId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkToEnableButtonLogin();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        etPw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkToEnableButtonLogin();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void checkToEnableButtonLogin() {
        if (!etId.getText().toString().isEmpty() && !etPw.getText().toString().isEmpty()) {
            tvLogin.setBackgroundResource(R.drawable.bt_login_enable);
            tvLogin.setClickable(true);
        } else {
            tvLogin.setBackgroundResource(R.drawable.bt_login_disabled);
            tvLogin.setClickable(false);
        }
    }

    private void login() {
        //TODO iplm login logic
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_forgot_pw:
                if (Constants.IS_DEBUG) {
                    LToast.show(getActivity(), "Click " + tvForgotPw.getText().toString());
                }
                break;
            case R.id.tv_dont_have_acc:
                if (Constants.IS_DEBUG) {
                    LToast.show(getActivity(), "Click " + tvDontHaveAcc.getText().toString());
                }
                break;
            case R.id.tv_login:
                login();
                break;
        }
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public void onDestroyView() {
        ((HomeV4CanSlideActivity) getActivity()).setVisibilityBtSearch(View.VISIBLE);
        KeyboardUtils.hideSoftInput(getActivity());
        super.onDestroyView();
    }
}