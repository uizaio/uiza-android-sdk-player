package vn.uiza.core.base;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import vn.uiza.R;
import vn.uiza.core.utilities.LActivityUtil;
import vn.uiza.core.utilities.LConnectivityUtil;
import vn.uiza.core.utilities.LDialogUtil;

public abstract class BaseActivity extends AppCompatActivity {
    protected CompositeSubscription compositeSubscription = new CompositeSubscription();
    protected Activity activity;
    protected String TAG;
    //private RelativeLayout rootView;

    /*protected RelativeLayout getRootView() {
        return rootView;
    }*/

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        activity = this;
        TAG = setTag();
        if (setFullScreen()) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            //LActivityUtil.hideSystemUI(getWindow().getDecorView());
        }
        setCustomStatusBar(ContextCompat.getColor(activity, R.color.colorPrimary), ContextCompat.getColor(activity, R.color.colorPrimary));
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(setLayoutResourceId());

        /*View view = activity.findViewById(R.id.scroll_view);
        if (view != null) {
            if (view instanceof ScrollView) {
                LUIUtil.setPullLikeIOSVertical((ScrollView) view);
            } else if (view instanceof NestedScrollView) {
                LUIUtil.setPullLikeIOSVertical((NestedScrollView) view);
            }
        }
        rootView = (RelativeLayout) activity.findViewById(R.id.root_view);*/
    }

    protected void setCustomStatusBar(int colorStatusBar, int colorNavigationBar) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //getWindow().setNavigationBarColor(ContextCompat.getColor(activity, R.color.colorPrimary));
            //getWindow().setStatusBarColor(ContextCompat.getColor(activity, R.color.colorPrimary));

            getWindow().setStatusBarColor(colorStatusBar);
            getWindow().setNavigationBarColor(colorNavigationBar);
        }

        //set color for status bar
        //StatusBarCompat.setStatusBarColor(activity, ContextCompat.getColor(activity, R.color.Red));

        //add alpha to color
        //StatusBarCompat.setStatusBarColor(activity, ContextCompat.getColor(activity, R.color.Red), 50);

        //translucent status bar
        //StatusBarCompat.translucentStatusBar(activity);

        //should hide status bar background (default black background) when SDK >= 21
        //StatusBarCompat.translucentStatusBar(activity, true);

        //set color for CollapsingToolbarLayout
        //setStatusBarColorForCollapsingToolbar(Activity activity, AppBarLayout appBarLayout, CollapsingToolbarLayout collapsingToolbarLayout, Toolbar toolbar, int statusColor)
    }

    @Override
    protected void onDestroy() {
        if (!compositeSubscription.isUnsubscribed()) {
            compositeSubscription.unsubscribe();
        }
        LDialogUtil.clearAll();
        super.onDestroy();
    }

    @SuppressWarnings("unchecked")
    public void subscribe(Observable observable, Subscriber subscriber) {
        if (!LConnectivityUtil.isConnected(activity)) {
            //showDialogError(getString(R.string.err_no_internet));
            subscriber.onError(new NoConnectionException(getString(R.string.err_no_internet)));
            return;
        }

        Subscription subscription = observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
        compositeSubscription.add(subscription);
    }

    /*public void startActivity(Class<? extends Activity> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }*/

    protected abstract boolean setFullScreen();

    protected abstract String setTag();

    protected abstract int setLayoutResourceId();

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (activity != null) {
            LActivityUtil.tranOut(activity);
        }
    }

    //private TextView tvConnectStt;

    /*private void showTvNoConnect() {
        if (rootView != null) {
            if (tvConnectStt == null) {
                //LLog.d(TAG, "tvConnectStt == null -> new tvConnectStt");
                tvConnectStt = new TextView(activity);
                tvConnectStt.setTextColor(Color.WHITE);
                //tvConnectStt.setBackgroundColor(ContextCompat.getColor(activity, R.color.LightPink));
                tvConnectStt.setBackgroundColor(Color.RED);
                tvConnectStt.setPadding(20, 20, 20, 20);
                tvConnectStt.setGravity(Gravity.CENTER);
                //tvConnectStt.setText(R.string.check_ur_connection);
                tvConnectStt.setText(R.string.check_ur_connection_vn);

                RelativeLayout.LayoutParams rLParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                rLParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 1);
                rootView.addView(tvConnectStt, rLParams);
                //rootView.requestLayout();
            } else {
                //LLog.d(TAG, "tvConnectStt != null");
                tvConnectStt.setText(R.string.check_ur_connection);
            }
            LAnimationUtil.play(tvConnectStt, Techniques.FadeIn);
        } else {
            //LLog.d(TAG, "rootView == null");
        }
    }*/

    /*@Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBusData.ConnectEvent event) {
        //TAG = "onMessageEvent";
        //LLog.d(TAG, "onMessageEvent " + event.isConnected());
        //onNetworkChange(event);
        *//*if (!event.isConnected()) {//no network
            showTvNoConnect();
        } else {
            if (tvConnectStt != null) {
                LAnimationUtil.play(tvConnectStt, Techniques.FadeOut, new LAnimationUtil.UZCallback() {
                    @Override
                    public void onCancel() {
                        //do nothing
                    }

                    @Override
                    public void onEnd() {
                        if (tvConnectStt != null) {
                            tvConnectStt.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onRepeat() {
                        //do nothing
                    }

                    @Override
                    public void onStart() {
                        //do nothing
                    }
                });
                tvConnectStt = null;
            }
        }*//*
    }*/

    /*protected void onNetworkChange(EventBusData.ConnectEvent event){

    }*/

    /*@Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }*/

    /*@Override
    protected void onResume() {
        super.onResume();
        if (!LConnectivityUtil.isConnected(activity)) {
            showTvNoConnect();
        }
    }*/
}
