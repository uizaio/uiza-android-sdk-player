package testlibuiza.sample.v3.error;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import io.uiza.core.exception.UzException;
import testlibuiza.R;

public class ErrorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);
        TextView tvErr = (TextView) findViewById(R.id.tv_err);
        String s = UzException.ERR_CODE_0 + " - " + UzException.ERR_0 + "\n"
                + UzException.ERR_CODE_1 + " - " + UzException.ERR_1 + "\n"
                + UzException.ERR_CODE_2 + " - " + UzException.ERR_2 + "\n"
                + UzException.ERR_CODE_3 + " - " + UzException.ERR_3 + "\n"
                + UzException.ERR_CODE_4 + " - " + UzException.ERR_4 + "\n"
                + UzException.ERR_CODE_5 + " - " + UzException.ERR_5 + "\n"
                + UzException.ERR_CODE_6 + " - " + UzException.ERR_6 + "\n"
                + UzException.ERR_CODE_7 + " - " + UzException.ERR_7 + "\n"
                + UzException.ERR_CODE_8 + " - " + UzException.ERR_8 + "\n"
                + UzException.ERR_CODE_9 + " - " + UzException.ERR_9 + "\n"
                + UzException.ERR_CODE_10 + " - " + UzException.ERR_10 + "\n"
                + UzException.ERR_CODE_11 + " - " + UzException.ERR_11 + "\n"
                + UzException.ERR_CODE_12 + " - " + UzException.ERR_12 + "\n"
                + UzException.ERR_CODE_13 + " - " + UzException.ERR_13 + "\n"
                + UzException.ERR_CODE_14 + " - " + UzException.ERR_14 + "\n"
                + UzException.ERR_CODE_15 + " - " + UzException.ERR_15 + "\n"
                + UzException.ERR_CODE_16 + " - " + UzException.ERR_16 + "\n"
                + UzException.ERR_CODE_17 + " - " + UzException.ERR_17 + "\n"
                + UzException.ERR_CODE_18 + " - " + UzException.ERR_18 + "\n"
                + UzException.ERR_CODE_19 + " - " + UzException.ERR_19 + "\n"
                + UzException.ERR_CODE_20 + " - " + UzException.ERR_20 + "\n"
                + UzException.ERR_CODE_21 + " - " + UzException.ERR_21 + "\n"
                + UzException.ERR_CODE_22 + " - " + UzException.ERR_22 + "\n"
                + UzException.ERR_CODE_23 + " - " + UzException.ERR_23 + "\n"
                + UzException.ERR_CODE_24 + " - " + UzException.ERR_24 + "\n"
                + UzException.ERR_CODE_400 + " - " + UzException.ERR_400 + "\n"
                + UzException.ERR_CODE_401 + " - " + UzException.ERR_401 + "\n"
                + UzException.ERR_CODE_404 + " - " + UzException.ERR_404 + "\n"
                + UzException.ERR_CODE_422 + " - " + UzException.ERR_422 + "\n"
                + UzException.ERR_CODE_500 + " - " + UzException.ERR_500 + "\n"
                + UzException.ERR_CODE_503 + " - " + UzException.ERR_503 + "\n";

        tvErr.setText(s);
    }
}
