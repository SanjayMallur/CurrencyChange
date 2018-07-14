package co.smallur.currencies.view

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Toast

import javax.inject.Inject


import co.smallur.currencies.AppApplicationConfig
import co.smallur.currencies.R
import co.smallur.currencies.viewmodel.CurrenciesViewModel

/**
 * Created by Sanjay
 * Implementing [CurrenciesView] class
 * */
class ActivityCurrencies : AppCompatActivity(), CurrenciesView {

    @Inject
    lateinit var viewModel: CurrenciesViewModel
    private lateinit var ratesList: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_currencies)
        ratesList = findViewById(R.id.rates_list)
        AppApplicationConfig.component!!.inject(this)
    }

    override fun onStart() {
        super.onStart()
        viewModel.attachView(this)
    }

    override fun initializeList(adapter: RecyclerView.Adapter<*>) {
        ratesList.layoutManager = LinearLayoutManager(this)
        ratesList.setHasFixedSize(true)
        ratesList.setItemViewCacheSize(adapter.itemCount)
        ratesList.adapter = adapter
    }

    override fun showError(errorString: String) {
        Toast.makeText(this, errorString, Toast.LENGTH_SHORT).show()
    }

    override fun onStop() {
        ratesList.adapter = null // clearing adapter values
        viewModel.detachView() //detaching view model
        super.onStop()
    }
}

