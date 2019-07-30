package testlibuiza.sample.v3.error;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import testlibuiza.R;
import vn.uiza.core.exception.UZException;

public class ErrorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);
        TextView tvErr = (TextView) findViewById(R.id.tv_err);
        String s = UZException.ERR_CODE_0 + " - " + UZException.ERR_0 + "\n"
                + UZException.ERR_CODE_1 + " - " + UZException.ERR_1 + "\n"
                + UZException.ERR_CODE_2 + " - " + UZException.ERR_2 + "\n"
                + UZException.ERR_CODE_3 + " - " + UZException.ERR_3 + "\n"
                + UZException.ERR_CODE_4 + " - " + UZException.ERR_4 + "\n"
                + UZException.ERR_CODE_5 + " - " + UZException.ERR_5 + "\n"
                + UZException.ERR_CODE_6 + " - " + UZException.ERR_6 + "\n"
                + UZException.ERR_CODE_7 + " - " + UZException.ERR_7 + "\n"
                + UZException.ERR_CODE_8 + " - " + UZException.ERR_8 + "\n"
                + UZException.ERR_CODE_9 + " - " + UZException.ERR_9 + "\n"
                + UZException.ERR_CODE_10 + " - " + UZException.ERR_10 + "\n"
                + UZException.ERR_CODE_11 + " - " + UZException.ERR_11 + "\n"
                + UZException.ERR_CODE_12 + " - " + UZException.ERR_12 + "\n"
                + UZException.ERR_CODE_13 + " - " + UZException.ERR_13 + "\n"
                + UZException.ERR_CODE_14 + " - " + UZException.ERR_14 + "\n"
                + UZException.ERR_CODE_15 + " - " + UZException.ERR_15 + "\n"
                + UZException.ERR_CODE_16 + " - " + UZException.ERR_16 + "\n"
                + UZException.ERR_CODE_17 + " - " + UZException.ERR_17 + "\n"
                + UZException.ERR_CODE_18 + " - " + UZException.ERR_18 + "\n"
                + UZException.ERR_CODE_19 + " - " + UZException.ERR_19 + "\n"
                + UZException.ERR_CODE_20 + " - " + UZException.ERR_20 + "\n"
                + UZException.ERR_CODE_21 + " - " + UZException.ERR_21 + "\n"
                + UZException.ERR_CODE_22 + " - " + UZException.ERR_22 + "\n"
                + UZException.ERR_CODE_23 + " - " + UZException.ERR_23 + "\n"
                + UZException.ERR_CODE_24 + " - " + UZException.ERR_24 + "\n"
                + UZException.ERR_CODE_400 + " - " + UZException.ERR_400 + "\n"
                + UZException.ERR_CODE_401 + " - " + UZException.ERR_401 + "\n"
                + UZException.ERR_CODE_404 + " - " + UZException.ERR_404 + "\n"
                + UZException.ERR_CODE_422 + " - " + UZException.ERR_422 + "\n"
                + UZException.ERR_CODE_500 + " - " + UZException.ERR_500 + "\n"
                + UZException.ERR_CODE_503 + " - " + UZException.ERR_503 + "\n";

        tvErr.setText(s);
    }
}
