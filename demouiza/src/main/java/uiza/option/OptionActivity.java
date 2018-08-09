package uiza.option;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import uiza.R;
import uiza.app.LSApplication;
import uiza.v2.splash.SplashActivity;
import uiza.v3.canslide.SplashActivityV3;
import vn.loitp.core.base.BaseActivity;
import vn.loitp.core.common.Constants;
import vn.loitp.core.utilities.LActivityUtil;
import vn.loitp.core.utilities.LLog;
import vn.loitp.core.utilities.LScreenUtil;
import vn.loitp.core.utilities.LUIUtil;
import vn.loitp.uizavideov3.UizaUtil;
import vn.loitp.views.viewpager.parrallaxviewpager.lib.parrallaxviewpager.Mode;
import vn.loitp.views.viewpager.parrallaxviewpager.lib.parrallaxviewpager.ParallaxViewPager;

public class OptionActivity extends BaseActivity {
    public static final String KEY_SKIN = "KEY_SKIN";
    public static final String KEY_CAN_SLIDE = "KEY_CAN_SLIDE";
    public static final String KEY_API_END_POINT = "KEY_API_END_POINT";
    public static final String KEY_API_TRACKING_END_POINT = "KEY_API_TRACKING_END_POINT";

    private List<SkinObject> skinObjectList = new ArrayList<>();
    private ParallaxViewPager viewPager;

    private RadioGroup radioGroupSlide;
    private RadioButton radioCanSlide;
    private RadioButton radioCannotSlide;
    private boolean canSlide;

    private RadioGroup radioEnvironment;
    private RadioButton radioEnvironmentAPIV3;
    private RadioButton radioEnvironmentDev;
    private RadioButton radioEnvironmentStag;
    private RadioButton radioEnvironmentProd;
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
        s1.setSkinId(Constants.PLAYER_ID_SKIN_1);
        skinObjectList.add(s1);

        SkinObject s2 = new SkinObject();
        s2.setSkinName("Uiza Skin 2");
        s2.setResId(R.drawable.skin_2);
        s2.setSkinId(Constants.PLAYER_ID_SKIN_2);
        skinObjectList.add(s2);

        SkinObject s3 = new SkinObject();
        s3.setSkinName("Uiza Skin 3");
        s3.setResId(R.drawable.skin_3);
        s3.setSkinId(Constants.PLAYER_ID_SKIN_3);
        skinObjectList.add(s3);

        SkinObject sDf = new SkinObject();
        sDf.setSkinName("Uiza Skin Default");
        sDf.setResId(R.drawable.skin_df);
        sDf.setSkinId(Constants.PLAYER_ID_SKIN_0);
        skinObjectList.add(sDf);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        genListSkin();

        findViewById(R.id.bt_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSplashScreen();
            }
        });

        //set auth null every run this app
        UizaUtil.setAuth(activity, null, LSApplication.getInstance().getGson());

        findViews();
        setupSkin();
        setupSlide();
        setupEnvironment();
        setupDebugMode();
    }

    private void findViews() {
        //setting theme
        viewPager = (ParallaxViewPager) findViewById(R.id.view_pager);
        viewPager.getLayoutParams().height = LScreenUtil.getScreenWidth() * 9 / 16;
        viewPager.invalidate();
        viewPager.setMode(Mode.LEFT_OVERLAY);
        LUIUtil.setPullLikeIOSHorizontal(viewPager);
        //setting slide
        radioGroupSlide = (RadioGroup) findViewById(R.id.radio_group_slide);
        radioCanSlide = (RadioButton) findViewById(R.id.radio_can_slide);
        radioCannotSlide = (RadioButton) findViewById(R.id.radio_cannot_slide);
        //setting environment
        radioEnvironment = (RadioGroup) findViewById(R.id.radio_environment);
        radioEnvironmentAPIV3 = (RadioButton) findViewById(R.id.radio_environment_api_v3);
        radioEnvironmentDev = (RadioButton) findViewById(R.id.radio_environment_dev);
        radioEnvironmentStag = (RadioButton) findViewById(R.id.radio_environment_stag);
        radioEnvironmentProd = (RadioButton) findViewById(R.id.radio_environment_prod);
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
        LLog.d(TAG, "canSlide " + canSlide);
        LLog.d(TAG, "currentApiEndPoint " + currentApiEndPoint);
        LLog.d(TAG, "currentApiTrackingEndPoint " + currentApiTrackingEndPoint);

        if (radioEnvironmentAPIV3.isChecked()) {
            Intent intent = new Intent(activity, SplashActivityV3.class);
            intent.putExtra(KEY_SKIN, skinObjectList.get(viewPager.getCurrentItem()).getSkinId());
            startActivity(intent);
            LActivityUtil.tranIn(activity);
        } else {
            Intent intent = new Intent(activity, SplashActivity.class);
            intent.putExtra(KEY_SKIN, skinObjectList.get(viewPager.getCurrentItem()).getSkinId());
            intent.putExtra(KEY_CAN_SLIDE, canSlide);
            intent.putExtra(KEY_API_END_POINT, currentApiEndPoint);
            intent.putExtra(KEY_API_TRACKING_END_POINT, currentApiTrackingEndPoint);
            startActivity(intent);
            LActivityUtil.tranIn(activity);
        }
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
        return R.layout.uiza_option_activity;
    }

    private void setupSkin() {
        viewPager.setAdapter(new SlidePagerAdapter());
    }

    private void setupSlide() {
        //default can slide
        radioCanSlide.setChecked(true);
        canSlide = true;

        radioGroupSlide.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                int selectedId = radioGroupSlide.getCheckedRadioButtonId();
                switch (selectedId) {
                    case R.id.radio_can_slide:
                        canSlide = true;
                        break;
                    case R.id.radio_cannot_slide:
                        canSlide = false;
                        break;
                }
            }
        });
    }

    private void setupEnvironment() {
        //default
        radioEnvironmentDev.setChecked(true);
        currentApiEndPoint = Constants.URL_DEV_UIZA_VERSION_2;
        currentApiTrackingEndPoint = Constants.URL_TRACKING_DEV;

        radioEnvironment.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                int selectedId = radioEnvironment.getCheckedRadioButtonId();
                switch (selectedId) {
                    case R.id.radio_environment_dev:
                        currentEnvironment = Constants.ENVIRONMENT_DEV;
                        radioCannotSlide.setVisibility(View.VISIBLE);
                        break;
                    case R.id.radio_environment_stag:
                        currentEnvironment = Constants.ENVIRONMENT_STAG;
                        radioCannotSlide.setVisibility(View.VISIBLE);
                        break;
                    case R.id.radio_environment_prod:
                        currentEnvironment = Constants.ENVIRONMENT_PROD;
                        radioCannotSlide.setVisibility(View.VISIBLE);
                        break;
                    case R.id.radio_environment_api_v3:
                        radioCannotSlide.setVisibility(View.GONE);
                        radioCanSlide.setChecked(true);
                        break;
                }
                UizaUtil.setAuth(activity, null, LSApplication.getInstance().getGson());
            }
        });
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
            LUIUtil.setTextShadow(tv);

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
