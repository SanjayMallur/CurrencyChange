package co.smallur.currencies.model.repository


import org.junit.Before
import org.junit.Test

import co.smallur.currencies.model.network.NetworkManager
import co.smallur.currencies.model.remote.CurrencyRemoteDataSource
import co.smallur.currencies.view.base.SchedulersFacadeImpl
import com.nhaarman.mockito_kotlin.mock

class RepositoryTest {

    private val netWorkManager = NetworkManager()
    private val repositoryListener: RepositoryListener = mock()
    private val currencyRemoteDataSource = CurrencyRemoteDataSource(this.netWorkManager, SchedulersFacadeImpl())
    private lateinit var repository: CurrencyRepositoryImpl

    @Before
    fun setUp() {
        repository = CurrencyRepositoryImpl(this.currencyRemoteDataSource)
    }

    @Test
    fun testGetData(){
        this.repository.getDataAndListen()
    }

    @Test
    fun testCancelData(){
        this.repository.cancelData()
    }

    @Test
    fun testSetListener(){
        this.repository.setListener(repositoryListener)
    }

}
