package co.smallur.currencies.viewmodel


import org.junit.Before
import org.junit.Test

import co.smallur.currencies.model.repository.CurrencyRepository
import co.smallur.currencies.view.CurrenciesView
import com.nhaarman.mockito_kotlin.mock
import org.mockito.Mockito.verify

class ViewModelTest {

    private lateinit var viewModel: CurrenciesViewModel
    private val currenciesView: CurrenciesView = mock()
    private var repository: CurrencyRepository = mock()

    @Before
    fun setUp() {
        viewModel = CurrenciesViewModel(repository)
    }


    @Test
    fun attachView() {
        viewModel.attachView(currenciesView)
    }

    @Test
    fun testError(){
        viewModel.attachView(currenciesView)
        viewModel.errorFromRepository("ServerError")
        verify(this.currenciesView).showError("ServerError")
    }


}