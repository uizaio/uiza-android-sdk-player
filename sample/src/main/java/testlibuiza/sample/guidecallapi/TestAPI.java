package testlibuiza.sample.guidecallapi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import testlibuiza.R;
import testlibuiza.app.LSApplication;
import vn.uiza.core.exception.UZException;
import vn.uiza.core.utilities.LConnectivityUtil;
import vn.uiza.core.utilities.LDialogUtil;
import vn.uiza.core.utilities.LLog;
import vn.uiza.core.utilities.LUIUtil;
import vn.uiza.rxandroid.ApiSubscriber;

public class TestAPI extends AppCompatActivity {
    private final String TAG = getClass().getSimpleName();
    protected CompositeSubscription compositeSubscription = new CompositeSubscription();
    private String domainAPI = "https://loctbprod01.uiza.co";
    private String token = "uap-9816792bb84642f09d843af4f93fb748-b94fcbd1";
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_api);
        RestClientTestAPI.init(domainAPI, token);

        tv = (TextView) findViewById(R.id.tv);

        findViewById(R.id.bt_get_list_user).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getListAllUser();
            }
        });
    }

    private void showTv(Object o) {
        LUIUtil.printBeautyJson(o, tv);
    }

    private void getListAllUser() {
        Service service = RestClientTestAPI.createService(Service.class);
        subscribe(service.listAllUser(), new ApiSubscriber<Object>() {
            @Override
            public void onSuccess(Object o) {
                LLog.d(TAG, "createAnUser onSuccess: " + LSApplication.getInstance().getGson().toJson(o));
                showTv(o);
            }

            @Override
            public void onFail(Throwable e) {
                LLog.e(TAG, "createAnUser onFail " + e.toString());
                showTv(e.getMessage());
            }
        });
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
        if (!LConnectivityUtil.isConnected(this)) {
            subscriber.onError(new Exception(UZException.ERR_0));
            return;
        }
        Subscription subscription = observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
        compositeSubscription.add(subscription);
    }
}
