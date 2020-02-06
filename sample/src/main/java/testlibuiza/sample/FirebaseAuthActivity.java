package testlibuiza.sample;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import testlibuiza.R;
import testlibuiza.sample.utils.SampleUtils;
import timber.log.Timber;
import vn.uiza.utils.StringUtils;

public class FirebaseAuthActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    Handler handler = new Handler();

    private AppCompatEditText etEmail, etPassword;
    ProgressBar progressBar;
    ConstraintLayout loginForm;
    AppCompatButton btLogout;

    @Override
    protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.activity_firebase_auth);
        mAuth = FirebaseAuth.getInstance();
        loginForm = findViewById(R.id.login_form);
        btLogout = findViewById(R.id.bt_logout);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        progressBar = findViewById(R.id.progress_bar);
        findViewById(R.id.bt_login).setOnClickListener(this);
        findViewById(R.id.bt_register).setOnClickListener(this);
        btLogout.setOnClickListener(this);
        if (!TextUtils.isEmpty(SampleUtils.getLocalUserId(this))) {
            loginForm.setVisibility(View.GONE);
            btLogout.setVisibility(View.VISIBLE);
        } else {
            loginForm.setVisibility(View.VISIBLE);
            btLogout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_login:
                progressBar.setVisibility(View.VISIBLE);
                Editable user = etEmail.getText();
                Editable pwd = etPassword.getText();
                if (StringUtils.isEmailValid(user) || TextUtils.isEmpty(pwd)) {
                    onFailure(new Throwable("User or password is wrong!"));
                    return;
                }
                attemptLogin(user.toString(), pwd.toString());
                break;
            case R.id.bt_register:
                progressBar.setVisibility(View.VISIBLE);
                Editable rguser = etEmail.getText();
                Editable rgpwd = etPassword.getText();
                if (StringUtils.isEmailValid(rguser) || TextUtils.isEmpty(rgpwd)) {
                    onFailure(new Throwable("User or password is wrong!"));
                    return;
                }
                createAccount(rguser.toString(), rgpwd.toString());
                break;
            case R.id.bt_logout:
                progressBar.setVisibility(View.VISIBLE);
                handler.postDelayed(() -> {
                    mAuth.signOut();
                    progressBar.setVisibility(View.GONE);
                    btLogout.setVisibility(View.GONE);
                    loginForm.setVisibility(View.VISIBLE);
                }, 1000);
                break;
        }

    }

    private void createAccount(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
            if (!task.isSuccessful()) {
                onFailure(new Throwable(task.getException()));
            } else {
                registerSuccess(task.getResult().getUser());
            }
        });
    }

    private void attemptLogin(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> loginSuccess(authResult.getUser()))
                .addOnFailureListener(this::onFailure);
    }

    public void loginSuccess(FirebaseUser user) {
        SampleUtils.saveLocalUser(this, etEmail.getText().toString(), user.getUid());
        loginForm.setVisibility(View.GONE);
        btLogout.setVisibility(View.VISIBLE);
        handler.post(() -> Toast.makeText(this, user.getUid() + " login success", Toast.LENGTH_LONG).show());
        progressBar.setVisibility(View.GONE);
    }

    public void registerSuccess(FirebaseUser user) {
        SampleUtils.saveLocalUser(this, etEmail.getText().toString(), user.getUid());
        loginForm.setVisibility(View.GONE);
        btLogout.setVisibility(View.VISIBLE);
        handler.post(() -> Toast.makeText(this, user.getDisplayName() + " register success", Toast.LENGTH_LONG).show());
        progressBar.setVisibility(View.GONE);
        Timber.e(user.getDisplayName());
    }

    public void onFailure(Throwable throwable) {
        progressBar.setVisibility(View.GONE);
        handler.post(() -> Toast.makeText(FirebaseAuthActivity.this, throwable.getLocalizedMessage(), Toast.LENGTH_LONG).show());
    }

    public void logout() {
        mAuth.signOut();
    }

}
