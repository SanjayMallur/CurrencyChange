package co.smallur.currencies.di


import javax.inject.Singleton

import dagger.Module
import dagger.Provides
import co.smallur.currencies.model.remote.CurrencyRemoteDataSource
import co.smallur.currencies.model.repository.CurrencyRepositoryImpl
import co.smallur.currencies.model.repository.CurrencyRepository

/**
* Created by Sanjay
 * Class for providing [CurrencyRepository] instance
* */

@Module
class RepositoryModule(currencyRemoteDataSource: CurrencyRemoteDataSource) {
    private val repository: CurrencyRepository

    init {
        repository = CurrencyRepositoryImpl(currencyRemoteDataSource)
    }

    @Provides
    @Singleton
    internal fun provideRepository(): CurrencyRepository = repository
}
