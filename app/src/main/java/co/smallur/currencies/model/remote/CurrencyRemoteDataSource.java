package co.smallur.currencies.model.remote;

import android.support.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

import co.smallur.currencies.model.entity.RateResponse;
import co.smallur.currencies.model.network.NetworkManager;
import co.smallur.currencies.model.repository.CurrencyRepository;
import co.smallur.currencies.model.repository.RepositoryListener;
import co.smallur.currencies.view.base.SchedulerFacade;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

import static co.smallur.currencies.utils.Constants.INSTANCE;

/**
 * Created by Sanjay
 * Class to call service to get data every second
 */
public class CurrencyRemoteDataSource {

    private NetworkManager networkManager;
    private Disposable networkObservable;
    private SchedulerFacade schedulerFacade;


    public CurrencyRemoteDataSource(NetworkManager networkManager, SchedulerFacade schedulerFacade) {
        this.networkManager = networkManager;
        this.schedulerFacade = schedulerFacade;

    }

    public void requestDataAndListen(@NonNull NetworkListener callback) {

        networkObservable = Observable.interval(1, TimeUnit.SECONDS)
                .flatMap(n -> networkManager.getApiService().getCurrencies(INSTANCE.getBASE_CURRENCY())
                        .flatMap(Observable::just)
                        .subscribeOn(schedulerFacade.io())
                        .observeOn(schedulerFacade.ui())
                        .onErrorResumeNext(observer -> { })
                        .doOnNext(callback::onSuccess)
                        .doOnError(callback::onFailure)
                ).subscribe();
    }

    public void shutdownRequestsToServer() {
        if (null != networkObservable) {
            networkObservable.dispose();
            networkObservable = null;
        }
    }

    public interface NetworkListener {
        void onSuccess(RateResponse currencies);
        void onFailure(Throwable t);
    }
}
