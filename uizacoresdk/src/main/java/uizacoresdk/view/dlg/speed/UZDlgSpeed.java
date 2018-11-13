package uizacoresdk.view.dlg.speed;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.CheckedTextView;
import android.widget.ScrollView;

import uizacoresdk.R;
import vn.uiza.core.utilities.LUIUtil;

/**
 * Created by loitp on 13/11/2018.
 */

public class UZDlgSpeed extends Dialog implements View.OnClickListener {
    private final String TAG = getClass().getSimpleName();
    private Activity activity;
    private ScrollView sv;
    private CheckedTextView ct0;
    private CheckedTextView ct1;
    private CheckedTextView ct2;
    private CheckedTextView ct3;
    private CheckedTextView ct4;
    private CheckedTextView ct5;
    private CheckedTextView ct6;

    private float currentSpeed;

    public UZDlgSpeed(Activity activity, float currentSpeed, Callback callback) {
        super(activity);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.activity = activity;
        this.currentSpeed = currentSpeed;
        //LLog.d(TAG, "currentSpeed " + currentSpeed);
        this.callback = callback;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dlg_speed);
        sv = (ScrollView) findViewById(R.id.sv);
        ct0 = (CheckedTextView) findViewById(R.id.ct_0);
        ct1 = (CheckedTextView) findViewById(R.id.ct_1);
        ct2 = (CheckedTextView) findViewById(R.id.ct_2);
        ct3 = (CheckedTextView) findViewById(R.id.ct_3);
        ct4 = (CheckedTextView) findViewById(R.id.ct_4);
        ct5 = (CheckedTextView) findViewById(R.id.ct_5);
        ct6 = (CheckedTextView) findViewById(R.id.ct_6);

        Speed speed0 = new Speed("0.25", 0.25f);
        Speed speed1 = new Speed("0.5", 0.5f);
        Speed speed2 = new Speed("0.75", 0.75f);
        Speed speed3 = new Speed("Normal", 1f);
        Speed speed4 = new Speed("1.25", 1.25f);
        Speed speed5 = new Speed("1.5", 1.5f);
        Speed speed6 = new Speed("2.0", 2f);

        ct0.setText(speed0.getName());
        ct1.setText(speed1.getName());
        ct2.setText(speed2.getName());
        ct3.setText(speed3.getName());
        ct4.setText(speed4.getName());
        ct5.setText(speed5.getName());
        ct6.setText(speed6.getName());

        ct0.setTag(speed0);
        ct1.setTag(speed1);
        ct2.setTag(speed2);
        ct3.setTag(speed3);
        ct4.setTag(speed4);
        ct5.setTag(speed5);
        ct6.setTag(speed6);

        setEvent(ct0);
        setEvent(ct1);
        setEvent(ct2);
        setEvent(ct3);
        setEvent(ct4);
        setEvent(ct5);
        setEvent(ct6);

        if (currentSpeed == speed0.value) {
            scrollTo(ct0);
        } else if (currentSpeed == speed1.value) {
            scrollTo(ct1);
        } else if (currentSpeed == speed2.value) {
            scrollTo(ct2);
        } else if (currentSpeed == speed3.value) {
            scrollTo(ct3);
        } else if (currentSpeed == speed4.value) {
            scrollTo(ct4);
        } else if (currentSpeed == speed5.value) {
            scrollTo(ct5);
        } else if (currentSpeed == speed6.value) {
            scrollTo(ct6);
        }
    }

    private void scrollTo(final CheckedTextView checkedTextView) {
        checkedTextView.setChecked(true);
        sv.postDelayed(new Runnable() {
            @Override
            public void run() {
                sv.scrollTo(0, checkedTextView.getTop());
            }
        }, 100);
    }

    private void setEvent(CheckedTextView checkedTextView) {
        checkedTextView.setFocusable(true);
        checkedTextView.setSoundEffectsEnabled(false);
        checkedTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        ct0.setChecked(false);
        ct1.setChecked(false);
        ct2.setChecked(false);
        ct3.setChecked(false);
        ct4.setChecked(false);
        ct5.setChecked(false);
        ct6.setChecked(false);
        if (view instanceof CheckedTextView) {
            ((CheckedTextView) view).setChecked(!((CheckedTextView) view).isChecked());
            if (callback != null) {
                callback.onSelectItem((Speed) view.getTag());
            }
        }
        LUIUtil.setDelay(200, new LUIUtil.DelayCallback() {
            @Override
            public void doAfter(int mls) {
                cancel();
            }
        });
    }

    public interface Callback {
        public void onSelectItem(Speed speed);
    }

    private Callback callback;

    public class Speed {
        private String name;
        private float value;

        public Speed(String name, float value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public float getValue() {
            return value;
        }

        public void setValue(float value) {
            this.value = value;
        }
    }
}