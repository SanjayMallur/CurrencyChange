package co.smallur.currencies.model.repository

/**
 * Created by Sanjay
 * Repository interface
 * */
interface CurrencyRepository {

    fun getDataAndListen()
    fun cancelData()

    fun setListener(listener: RepositoryListener)
    fun removeListener()
}
