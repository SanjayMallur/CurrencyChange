package co.smallur.currencies.view

import android.support.v7.widget.RecyclerView

/**
 * Created by Sanjay
 * View for activity
 */

interface CurrenciesView {

    fun initializeList(adapter: RecyclerView.Adapter<*>)
    fun showError(errorString: String)
}
