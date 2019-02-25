package testlibuiza.sample.v3.slide;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import testlibuiza.R;
import testlibuiza.app.LSApplication;

public class Slide0Activity extends AppCompatActivity {
    private final String TAG = getClass().getSimpleName();
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activity = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_0);
        if (LSApplication.DF_DOMAIN_API.equals("input")) {
            showDialogInitWorkspace();
        }
    }

    private void showDialogInitWorkspace() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("Please correct your workspace's information first..");
        builder.setCancelable(false);
        builder.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        onBackPressed();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
