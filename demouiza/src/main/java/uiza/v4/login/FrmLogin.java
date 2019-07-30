package uiza.v4.login;

/**
 * Created by www.muathu@gmail.com on 12/24/2017.
 */

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
import uiza.R;
import uiza.v4.HomeV4CanSlideActivity;
import uiza.v4.helper.utils.KeyboardUtils;
import uizacoresdk.interfaces.IOnBackPressed;
import vn.uiza.core.common.Constants;
import vn.uiza.core.utilities.LUIUtil;
import vn.uiza.views.LToast;

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
            /*case R.id.iv_close_activity:
                getActivity().onBackPressed();
                break;*/
        }
    }

    @Override
    public boolean onBackPressed() {
        //return ((HomeV4CanSlideActivity) getActivity()).handleOnbackpressFrm();
        return false;
    }

    @Override
    public void onDestroyView() {
        ((HomeV4CanSlideActivity) getActivity()).setVisibilityBtSearch(View.VISIBLE);
        KeyboardUtils.hideSoftInput(getActivity());
        super.onDestroyView();
    }
}