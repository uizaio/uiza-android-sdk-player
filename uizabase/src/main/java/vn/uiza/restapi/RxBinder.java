package vn.uiza.restapi;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by loitp on 14/1/2019.
 */

public class RxBinder {
    private static class RxBinderHelper {
        private static final RxBinder INSTANCE = new RxBinder();
    }

    public static RxBinder getInstance() {
        return RxBinderHelper.INSTANCE;
    }

    private RxBinder() {
    }

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public void dispose() {
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }

    public <T> void bind(Observable<T> observable, Consumer<T> onNext, Consumer<Throwable> onError) {
        //TODO maybe in some cases we don't need to check internet connection
        /*if (!NetworkUtils.hasConnection(this)) {
            subscriber.onError(new NoConnectionException());
            return;
        }*/
        Disposable disposable = observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onNext, onError);
        compositeDisposable.add(disposable);
    }

    @SuppressWarnings("unchecked")
    public <T> void bind(Observable<T> observable, Consumer<T> onNext, Consumer<Throwable> onError, Action onComplete) {
        //TODO maybe in some cases we don't need to check internet connection
        /*if (!NetworkUtils.hasConnection(this)) {
            subscriber.onError(new NoConnectionException());
            return;
        }*/
        Disposable disposable = observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onNext, onError, onComplete);
        compositeDisposable.add(disposable);
    }
}
