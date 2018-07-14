package co.smallur.currencies.model.repository

/**
 * Created by Sanjay
 * Class for Repository listener
 * */
interface RepositoryListener {

    fun currenciesFromNetwork(data: Map<String, Float>)
    fun errorFromRepository(errorString: String)
}
