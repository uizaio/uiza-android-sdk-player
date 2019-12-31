package testlibuiza.sample;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

import testlibuiza.R;
import vn.uiza.core.exception.UizaException;

public class ErrorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);
        TextView tvErr = findViewById(R.id.tv_err);
        String s = UizaException.ERR_CODE_0 + " - " + UizaException.ERR_0 + "\n"
                + UizaException.ERR_CODE_1 + " - " + UizaException.ERR_1 + "\n"
                + UizaException.ERR_CODE_2 + " - " + UizaException.ERR_2 + "\n"
                + UizaException.ERR_CODE_3 + " - " + UizaException.ERR_3 + "\n"
                + UizaException.ERR_CODE_4 + " - " + UizaException.ERR_4 + "\n"
                + UizaException.ERR_CODE_5 + " - " + UizaException.ERR_5 + "\n"
                + UizaException.ERR_CODE_6 + " - " + UizaException.ERR_6 + "\n"
                + UizaException.ERR_CODE_7 + " - " + UizaException.ERR_7 + "\n"
                + UizaException.ERR_CODE_8 + " - " + UizaException.ERR_8 + "\n"
                + UizaException.ERR_CODE_9 + " - " + UizaException.ERR_9 + "\n"
                + UizaException.ERR_CODE_10 + " - " + UizaException.ERR_10 + "\n"
                + UizaException.ERR_CODE_11 + " - " + UizaException.ERR_11 + "\n"
                + UizaException.ERR_CODE_12 + " - " + UizaException.ERR_12 + "\n"
                + UizaException.ERR_CODE_13 + " - " + UizaException.ERR_13 + "\n"
                + UizaException.ERR_CODE_14 + " - " + UizaException.ERR_14 + "\n"
                + UizaException.ERR_CODE_15 + " - " + UizaException.ERR_15 + "\n"
                + UizaException.ERR_CODE_16 + " - " + UizaException.ERR_16 + "\n"
                + UizaException.ERR_CODE_17 + " - " + UizaException.ERR_17 + "\n"
                + UizaException.ERR_CODE_18 + " - " + UizaException.ERR_18 + "\n"
                + UizaException.ERR_CODE_19 + " - " + UizaException.ERR_19 + "\n"
                + UizaException.ERR_CODE_20 + " - " + UizaException.ERR_20 + "\n"
                + UizaException.ERR_CODE_21 + " - " + UizaException.ERR_21 + "\n"
                + UizaException.ERR_CODE_22 + " - " + UizaException.ERR_22 + "\n"
                + UizaException.ERR_CODE_23 + " - " + UizaException.ERR_23 + "\n"
                + UizaException.ERR_CODE_24 + " - " + UizaException.ERR_24 + "\n"
                + UizaException.ERR_CODE_400 + " - " + UizaException.ERR_400 + "\n"
                + UizaException.ERR_CODE_401 + " - " + UizaException.ERR_401 + "\n"
                + UizaException.ERR_CODE_404 + " - " + UizaException.ERR_404 + "\n"
                + UizaException.ERR_CODE_422 + " - " + UizaException.ERR_422 + "\n"
                + UizaException.ERR_CODE_500 + " - " + UizaException.ERR_500 + "\n"
                + UizaException.ERR_CODE_503 + " - " + UizaException.ERR_503 + "\n";

        tvErr.setText(s);
    }
}
