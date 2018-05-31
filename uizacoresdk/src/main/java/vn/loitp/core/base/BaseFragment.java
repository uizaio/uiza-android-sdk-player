package vn.loitp.core.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import loitp.core.R;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import vn.loitp.core.utilities.LConnectivityUtil;
import vn.loitp.core.utilities.LDialogUtil;
import vn.loitp.utils.util.NetworkUtils;

/**
 * Created by khanh on 7/31/16.
 */
public abstract class BaseFragment extends Fragment {
    protected Context context;
    protected CompositeSubscription compositeSubscription = new CompositeSubscription();
    protected final String BASE_TAG = BaseFragment.class.getSimpleName();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (fragmentCallback != null) {
            fragmentCallback.onViewCreated();
        }
    }

    @Override
    public void onDestroyView() {
        if (!compositeSubscription.isUnsubscribed()) {
            compositeSubscription.unsubscribe();
        }
        LDialogUtil.clearAll();
        super.onDestroyView();
    }

    @SuppressWarnings("unchecked")
    protected void subscribe(Observable observable, Subscriber subscriber) {
        if (!LConnectivityUtil.isConnected(getActivity())) {
            //showDialogError(getString(R.string.err_no_internet));
            subscriber.onError(new NoConnectionException(getString(R.string.err_no_internet)));
            return;
        }

        Subscription subscription = observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
        compositeSubscription.add(subscription);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public interface FragmentCallback {
        public void onViewCreated();
    }

    protected FragmentCallback fragmentCallback;

    public void setFragmentCallback(FragmentCallback fragmentCallback) {
        this.fragmentCallback = fragmentCallback;
    }
}
