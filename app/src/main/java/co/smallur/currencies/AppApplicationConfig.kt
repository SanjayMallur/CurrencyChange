package co.smallur.currencies

import android.app.Application

import co.smallur.currencies.di.AppComponent
import co.smallur.currencies.di.DaggerAppComponent
import co.smallur.currencies.di.RepositoryModule
import co.smallur.currencies.model.network.NetworkManager
import co.smallur.currencies.model.remote.CurrencyRemoteDataSource
import co.smallur.currencies.view.base.SchedulersFacadeImpl

class AppApplicationConfig : Application() {

    override fun onCreate() {
        super.onCreate()
        component = DaggerAppComponent.builder()
                .repositoryModule(RepositoryModule(CurrencyRemoteDataSource(NetworkManager(), SchedulersFacadeImpl())))
                .build()
    }

    companion object {
        var component: AppComponent? = null
            private set
    }
}