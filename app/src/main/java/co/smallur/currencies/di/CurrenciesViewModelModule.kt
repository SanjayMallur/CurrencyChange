package co.smallur.currencies.di

import javax.inject.Singleton

import dagger.Module
import dagger.Provides
import co.smallur.currencies.model.repository.CurrencyRepository
import co.smallur.currencies.viewmodel.CurrenciesViewModel

/**
 * Created by Sanjay
 * Class for providing [CurrenciesViewModel] instance
 * */
@Module
class CurrenciesViewModelModule {

    @Provides
    @Singleton
    fun provideViewModel(repository: CurrencyRepository): CurrenciesViewModel = CurrenciesViewModel(repository)

}
