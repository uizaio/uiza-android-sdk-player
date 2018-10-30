package vn.uiza.core.base;

import android.app.Activity;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
import vn.uiza.core.utilities.LLog;
import vn.uiza.core.utilities.connection.LConectifyService;
import vn.uiza.data.EventBusData;

public abstract class BaseActivity extends AppCompatActivity {
    protected CompositeSubscription compositeSubscription = new CompositeSubscription();
    protected Activity activity;
    protected String TAG;

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

        scheduleJob();
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
            subscriber.onError(new NoConnectionException(getString(R.string.err_no_internet)));
            return;
        }
        Subscription subscription = observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
        compositeSubscription.add(subscription);
    }

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

    @Override
    public void onStart() {
        EventBus.getDefault().register(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Intent startServiceIntent = new Intent(this, LConectifyService.class);
            startService(startServiceIntent);
        }
        super.onStart();
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            stopService(new Intent(this, LConectifyService.class));
        }
        super.onStop();
    }

    private void scheduleJob() {
        JobInfo myJob = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            myJob = new JobInfo.Builder(0, new ComponentName(this, LConectifyService.class))
                    .setRequiresCharging(true)
                    .setMinimumLatency(1000)
                    .setOverrideDeadline(2000)
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                    .setPersisted(true)
                    .build();
            JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
            jobScheduler.schedule(myJob);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBusData.ConnectEvent event) {
        //TAG = "onMessageEvent";
        //LLog.d(TAG, "onMessageEvent " + event.isConnected());
        onNetworkChange(event);
    }

    protected void onNetworkChange(EventBusData.ConnectEvent event) {
        if (event != null) {
            LLog.d(TAG, "onNetworkChange " + event.isConnected());
        }
    }
}
