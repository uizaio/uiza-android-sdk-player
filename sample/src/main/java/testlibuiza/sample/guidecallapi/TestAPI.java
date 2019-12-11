package testlibuiza.sample.guidecallapi;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import testlibuiza.R;
import testlibuiza.app.LSApplication;
import timber.log.Timber;
import vn.uiza.core.exception.UZException;
import vn.uiza.core.utilities.LConnectivityUtil;
import vn.uiza.core.utilities.LDialogUtil;
import vn.uiza.core.utilities.LUIUtil;

public class TestAPI extends AppCompatActivity {
    private final String TAG = getClass().getSimpleName();
    protected CompositeDisposable compositeDisposable = new CompositeDisposable();
    private String domainAPI = "https://loctbprod01.uiza.co";
    private String token = "uap-9816792bb84642f09d843af4f93fb748-b94fcbd1";
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_api);
        RestClientTestAPI.init(domainAPI, token);

        tv = findViewById(R.id.tv);

        findViewById(R.id.bt_get_list_user).setOnClickListener(view -> getListAllUser());
    }

    private void showTv(Object o) {
        LUIUtil.printBeautyJson(o, tv);
    }

    private void getListAllUser() {
        Service service = RestClientTestAPI.createService(Service.class);
        subscribe(service.listAllUser(), o -> {
            Timber.d("createAnUser onSuccess: %s", LSApplication.getInstance().getGson().toJson(o));
            showTv(o);
        }, throwable -> {
            Timber.e(throwable, "createAnUser onFail");
            showTv(throwable.getLocalizedMessage());
        });
    }

    @Override
    protected void onDestroy() {
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
        LDialogUtil.clearAll();
        super.onDestroy();
    }

    @SuppressWarnings("unchecked")
    public <T> void subscribe(Observable<T> observable, Consumer<T> onNext, Consumer<Throwable> onError) {
        if (!LConnectivityUtil.isConnected(this)) {
            try {
                onError.accept(new Throwable((UZException.ERR_0)));
            } catch (Exception e) {
                Timber.e(e);
            }

            return;
        }
        Disposable disposable = observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onNext, onError);
        compositeDisposable.add(disposable);
    }
}
