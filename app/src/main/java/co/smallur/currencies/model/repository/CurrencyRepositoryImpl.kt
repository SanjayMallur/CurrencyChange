package co.smallur.currencies.model.repository

import android.util.Log
import co.smallur.currencies.model.entity.RateResponse
import co.smallur.currencies.model.remote.CurrencyRemoteDataSource

/**
* Created by Sanjay
 * Class for [CurrencyRepository] implementation
* */
class CurrencyRepositoryImpl(private val currencyRemoteDataSource: CurrencyRemoteDataSource) : CurrencyRepository {

    private var currencies: RateResponse? = null

    private var listener: RepositoryListener? = null

    private fun addBaseCurrencyToBeginOfList() {
        currencies!!.rates.put(currencies!!.base, 1.0f)
    }

    override fun getDataAndListen() {

        currencyRemoteDataSource.requestDataAndListen(object : CurrencyRemoteDataSource.NetworkListener {
            override fun onSuccess(newCurrencies: RateResponse) {
                currencies = newCurrencies
                if (listener != null)
                    listener!!.currenciesFromNetwork(proceedToListener(currencies!!))
            }

            override fun onFailure(t: Throwable?) {
                val errorString = t!!.localizedMessage
                if (listener != null)
                    listener!!.errorFromRepository(errorString)
            }
        })
    }

    override fun cancelData() {
        currencyRemoteDataSource.shutdownRequestsToServer()
    }

    override fun setListener(listener: RepositoryListener) {
        this.listener = listener
    }

    override fun removeListener() {
        this.listener = null
    }

    private fun proceedToListener(currencies: RateResponse): Map<String, Float> {
        this.currencies = currencies
        addBaseCurrencyToBeginOfList()
        return currencies.rates
    }
}
