package io.uiza.core.api;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class UzApiMaster {
    private static UzApiMaster ourInstance = new UzApiMaster();
    private CompositeSubscription compositeSubscription = new CompositeSubscription();

    public static UzApiMaster getInstance() {
        return ourInstance;
    }

    private UzApiMaster() {
    }

    public void unsubscribe() {
        if (!compositeSubscription.isUnsubscribed()) {
            compositeSubscription.unsubscribe();
        }
    }

    @SuppressWarnings("unchecked")
    public void subscribe(Observable observable, Subscriber subscriber) {
        Subscription subscription = observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
        compositeSubscription.add(subscription);
    }
}
