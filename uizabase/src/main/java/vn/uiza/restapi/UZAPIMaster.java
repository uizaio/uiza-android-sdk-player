package vn.uiza.restapi;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

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

    private CompositeSubscription compositeSubscription = new CompositeSubscription();

    public void destroy() {
        if (!compositeSubscription.isUnsubscribed()) {
            compositeSubscription.unsubscribe();
        }
    }

    @SuppressWarnings("unchecked")
    public void subscribe(Observable observable, Subscriber subscriber) {
        //TODO maybe in some cases we don't need to check internet connection
        /*if (!NetworkUtils.hasConnection(this)) {
            subscriber.onError(new NoConnectionException());
            return;
        }*/
        Subscription subscription = observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
        compositeSubscription.add(subscription);
    }
}
