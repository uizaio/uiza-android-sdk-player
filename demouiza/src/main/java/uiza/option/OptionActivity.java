package uiza.option;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import io.uiza.core.util.LLog;
import io.uiza.core.util.UzDisplayUtil;
import io.uiza.core.util.constant.Constants;
import java.util.ArrayList;
import java.util.List;
import uiza.R;
import uiza.v4.SplashActivity;

public class OptionActivity extends AppCompatActivity {
    private final String TAG = getClass().getSimpleName();
    private Activity activity;
    public static final String KEY_SKIN = "KEY_SKIN";
    public static final String KEY_CAN_SLIDE = "KEY_CAN_SLIDE";
    public static final String KEY_API_END_POINT = "KEY_API_END_POINT";
    public static final String KEY_API_TRACKING_END_POINT = "KEY_API_TRACKING_END_POINT";

    private List<SkinObject> skinObjectList = new ArrayList<>();
    private ViewPager viewPager;

    private int currentEnvironment = Constants.NOT_FOUND;
    String currentApiTrackingEndPoint = null;
    String currentApiEndPoint = null;

    private RadioGroup radioDebugMode;
    private RadioButton radioDebugModeEnable;
    private RadioButton radioDebugModeDisable;

    private void genListSkin() {
        SkinObject s1 = new SkinObject();
        s1.setSkinName("Uiza Skin 1");
        s1.setResId(R.drawable.skin_1);
        s1.setSkinId(R.layout.uz_player_skin_1);
        skinObjectList.add(s1);

        SkinObject s2 = new SkinObject();
        s2.setSkinName("Uiza Skin 2");
        s2.setResId(R.drawable.skin_2);
        s2.setSkinId(R.layout.uz_player_skin_2);
        skinObjectList.add(s2);

        SkinObject s3 = new SkinObject();
        s3.setSkinName("Uiza Skin 3");
        s3.setResId(R.drawable.skin_3);
        s3.setSkinId(R.layout.uz_player_skin_3);
        skinObjectList.add(s3);

        SkinObject sDf = new SkinObject();
        sDf.setSkinName("Uiza Skin Default");
        sDf.setResId(R.drawable.skin_df);
        sDf.setSkinId(R.layout.uz_player_skin_0);
        skinObjectList.add(sDf);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activity = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.uiza_option_activity);
        genListSkin();
        findViewById(R.id.bt_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSplashScreen();
            }
        });
        findViews();
        setupSkin();
        setupDebugMode();
    }

    private void findViews() {
        //setting theme
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.getLayoutParams().height = UzDisplayUtil.getScreenWidth() * 9 / 16;
        viewPager.invalidate();
        //setting debug mode
        radioDebugMode = (RadioGroup) findViewById(R.id.radio_debug_mode);
        radioDebugModeDisable = (RadioButton) findViewById(R.id.radio_debug_mode_disable);
        radioDebugModeEnable = (RadioButton) findViewById(R.id.radio_debug_mode_enable);
    }

    private void goToSplashScreen() {
        switch (currentEnvironment) {
            case Constants.ENVIRONMENT_DEV:
                currentApiEndPoint = Constants.URL_DEV_UIZA_VERSION_2;
                currentApiTrackingEndPoint = Constants.URL_TRACKING_DEV;
                break;
            case Constants.ENVIRONMENT_STAG:
                currentApiEndPoint = Constants.URL_DEV_UIZA_VERSION_2_STAG;
                currentApiTrackingEndPoint = Constants.URL_TRACKING_STAG;
                break;
            case Constants.ENVIRONMENT_PROD:
                currentApiEndPoint = Constants.URL_DEV_UIZA_VERSION_2_DEMO;
                currentApiTrackingEndPoint = Constants.URL_TRACKING_PROD;
                break;
        }

        LLog.d(TAG, "currentPlayerId " + skinObjectList.get(viewPager.getCurrentItem()).getSkinId());
        LLog.d(TAG, "currentApiEndPoint " + currentApiEndPoint);
        LLog.d(TAG, "currentApiTrackingEndPoint " + currentApiTrackingEndPoint);

        Intent intent = new Intent(activity, SplashActivity.class);
        intent.putExtra(KEY_SKIN, skinObjectList.get(viewPager.getCurrentItem()).getSkinId());
        startActivity(intent);
    }

    private void setupSkin() {
        viewPager.setAdapter(new SlidePagerAdapter());
    }

    private void setupDebugMode() {
        radioDebugModeDisable.setChecked(true);
        Constants.setDebugMode(false);

        radioDebugMode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                int selectedId = radioDebugMode.getCheckedRadioButtonId();
                switch (selectedId) {
                    case R.id.radio_debug_mode_enable:
                        Constants.setDebugMode(true);
                        break;
                    case R.id.radio_debug_mode_disable:
                        Constants.setDebugMode(false);
                        break;
                }
            }
        });
    }

    private class SlidePagerAdapter extends PagerAdapter {

        @Override
        public Object instantiateItem(ViewGroup collection, int position) {
            SkinObject skinObject = skinObjectList.get(position);
            LayoutInflater inflater = LayoutInflater.from(activity);
            ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.item_photo_slide_iv, collection, false);

            ImageView imageView = (ImageView) layout.findViewById(R.id.imageView);
            imageView.setImageResource(skinObject.getResId());

            TextView tv = (TextView) layout.findViewById(R.id.tv);
            tv.setText(skinObject.getSkinName());
            UzDisplayUtil.setTextShadow(tv);

            collection.addView(layout);
            return layout;
        }

        @Override
        public void destroyItem(ViewGroup collection, int position, Object view) {
            collection.removeView((View) view);
        }

        @Override
        public int getCount() {
            return skinObjectList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
