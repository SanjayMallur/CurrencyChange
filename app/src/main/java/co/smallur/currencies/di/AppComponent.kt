package co.smallur.currencies.di

import co.smallur.currencies.view.ActivityCurrencies
import javax.inject.Singleton

import dagger.Component

/**
 * Created by Sanjay
 * Class to init application component with dependency modules
 * */
@Singleton
@Component(modules = [(RepositoryModule::class), (CurrenciesViewModelModule::class)])
interface AppComponent {

    fun inject(activity: ActivityCurrencies) // Activity injection
}
