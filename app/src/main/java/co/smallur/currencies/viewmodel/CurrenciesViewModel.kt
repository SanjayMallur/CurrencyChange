package co.smallur.currencies.viewmodel

import android.support.v7.widget.RecyclerView

import java.util.ArrayList

import co.smallur.currencies.utils.Constants
import co.smallur.currencies.model.repository.CurrencyRepository
import co.smallur.currencies.model.repository.RepositoryListener
import co.smallur.currencies.view.CurrenciesView
import co.smallur.currencies.model.entity.Currency

/**
 * Created by Sanjay
 * */
class CurrenciesViewModel(private val repository: CurrencyRepository) : RepositoryListener {

    private var view: CurrenciesView? = null

    private var currenciesAdapter: CurrenciesAdapter? = null
    var modelList = mutableListOf<Currency>()
        private set
    var currentAmount: Float? = Constants.BASE_AMOUNT
        private set

    fun attachView(view: CurrenciesView) {
        this.view = view
        repository.setListener(this)
        repository.getDataAndListen()
    }

    private fun createListModelIfNeeded(ratesFromCache: Map<String, Float>) {
        if (modelList.isEmpty()) {
            modelList = mapRepoDataToModel(ratesFromCache)
            view!!.initializeList(buildAdapter())
        }
    }

    fun detachView() {
        currenciesAdapter = null
        repository.cancelData()
        repository.removeListener()
        this.view = null
    }

    private fun buildAdapter(): RecyclerView.Adapter<*> {
        if (currenciesAdapter == null)
            currenciesAdapter = CurrenciesAdapter.create(this)
        return currenciesAdapter as RecyclerView.Adapter<*>
    }

    private fun mapRepoDataToModel(repoData: Map<String, Float>?): MutableList<Currency> {
        val returnValue = ArrayList<Currency>()
        if (repoData != null) {
            for (currencyName in repoData.keys) {
                val value = repoData[currencyName]
                if (currencyName == Constants.BASE_CURRENCY)
                    returnValue.add(0, Currency(currencyName, value))
                else
                    returnValue.add(Currency(currencyName, value))
            }
        }
        return returnValue
    }

    override fun currenciesFromNetwork(data: Map<String, Float>) {
        if (modelList.isEmpty())
            createListModelIfNeeded(data)
        else {
            updateListModelWithNewData(data)
            if (currenciesAdapter != null)
                currenciesAdapter!!.updateValues()
            else
                view!!.initializeList(buildAdapter())
        }
    }

    private fun updateListModelWithNewData(data: Map<String, Float>) {
        for (currencyName in data.keys) {
            val value = data[currencyName]

            for (item in modelList) {
                if (item.name == currencyName) {
                    item.value = value
                    break
                }
            }
        }
    }

    override fun errorFromRepository(errorString: String) {
        view!!.showError(errorString)
    }

    fun changeCurrentAmountWithItem(newValue: Float, oldValue: Float) {
        if (oldValue > 0.0f)
            this.currentAmount = newValue / oldValue
    }

}

