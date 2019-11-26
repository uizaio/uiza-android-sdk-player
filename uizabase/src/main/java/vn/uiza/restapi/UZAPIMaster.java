package vn.uiza.restapi;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by loitp on 14/1/2019.
 */

public class UZAPIMaster {
    private static UZAPIMaster ourInstance;

    public static UZAPIMaster getInstance() {
        if (ourInstance == null) {
            ourInstance = new UZAPIMaster();
        }
        return ourInstance;
    }

    private UZAPIMaster() {
    }

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public void destroy() {
        if (!compositeDisposable.isDisposed() ) {
            compositeDisposable.dispose();
        }
    }

    @SuppressWarnings("unchecked")
    public <T> void subscribe(Observable<T> observable, Consumer<? super T> onNext, Consumer<? super Throwable> onError) {
        //TODO maybe in some cases we don't need to check internet connection
        Disposable disposable = observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onNext, onError);
        compositeDisposable.add(disposable);
    }
}
