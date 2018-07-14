package co.smallur.currencies.viewmodel

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView

import com.jakewharton.rxbinding2.widget.RxTextView

import java.util.HashMap
import java.util.Locale

import co.smallur.currencies.R
import co.smallur.currencies.model.entity.Currency
import io.reactivex.disposables.Disposable
import java.lang.Float.parseFloat

/**
 * Created by Sanjay
 * Adapter for recycler view for custom actions
 * @param viewModel injecting [CurrenciesViewModel]
 * */
class CurrenciesAdapter private constructor(private val viewModel: CurrenciesViewModel) : RecyclerView.Adapter<CurrenciesAdapter.ItemViewHolder>() {

    private val editTextKeyValue = HashMap<String, EditText>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val rowView = LayoutInflater.from(parent.context).inflate(R.layout.currency_item, parent, false)
        return ItemViewHolder(rowView)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = viewModel.modelList[position]
        holder.textLabel.text = item.name
        setValueToEditText(holder.editTextRate, item.value!!)
        clickAndFocus(holder, item)
        editTextKeyValue[item.name] = holder.editTextRate
    }


    /**
     * Set action for click and focus
     * @param holder Recycler view holder
     * @param item currency item
     * */
    private fun clickAndFocus(holder: ItemViewHolder, item: Currency) {
        //Set action for click on whole view
        holder.itemView.setOnClickListener { moveToZeroPosition(item, holder) }
        //Set action for in focus for EditText
        holder.editTextRate.onFocusChangeListener = View.OnFocusChangeListener { _, isInFocus -> handleFocusForItem(isInFocus, item, holder) }
    }


    /**
     * Handling focus for item
     * @param isInFocus boolean value for focus
     * @param item item currency
     * @param holder view holder
     * */
    private fun handleFocusForItem(isInFocus: Boolean, item: Currency, holder: ItemViewHolder) {
        if (isInFocus) {
            moveToZeroPosition(item, holder)
            holder.setReactiveEditText(subscribingToTextEvents(item, holder))
            cursorPosition(holder)
        } else {
            //Dispose
            holder.disposeReactiveEditTextIfPossible()
            //Remove soft keyboard
            val imm = holder.itemView.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(holder.editTextRate.windowToken, 0)
        }
    }


    /**
     * Method for cursor position
     * @param item item view holder
     * */
    private fun cursorPosition(holder: ItemViewHolder) {
        val posOfBeforePoint = holder.editTextRate.text.toString().lastIndexOf(".") - 1
        holder.editTextRate.setSelection(posOfBeforePoint)
    }

    /**
     * Method for subscribing to events
     * @param item item for subscribing
     * @param holder item view holder
     * */
    private fun subscribingToTextEvents(item: Currency, holder: ItemViewHolder): Disposable {
        return RxTextView.textChanges(holder.editTextRate)
                .skip(1)
                .map { text -> stringAsZero(text.toString()) }
                .map { parseFloat(it) }
                .doOnNext { value ->
                    viewModel.changeCurrentAmountWithItem(value!!, item.value!!)
                    updateValues()
                }
                .subscribe()
    }

    /**
     * Method to set string as zero
     * @param str value
     * */
    private fun stringAsZero(str: String?): String {
        return if (str == null || str.isEmpty() || str == ".")
            "0"
        else
            str
    }

    /**
     * Method to set value to edit text
     * @param editText edit text to set value
     * @param value to set
     * */
    private fun setValueToEditText(editText: EditText?, value: Float) {
        val result = value * viewModel.currentAmount!!
        editText!!.setText(String.format(Locale.US, "%.2f", result))
    }

    override fun getItemCount(): Int {
        return viewModel.modelList.size
    }

    /**
     * Method to move to position
     * @param item currency item
     * @param holder Recycler view holder
     * */
    private fun moveToZeroPosition(item: Currency, holder: ItemViewHolder) {

        val pos = viewModel.modelList.indexOf(item) //previous position

        if (viewModel.modelList.remove(item)) {
            viewModel.modelList.add(0, item)

            holder.editTextRate.requestFocus()  //focus to top element

            val imm = holder.itemView.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(holder.editTextRate, InputMethodManager.SHOW_IMPLICIT)

            notifyItemMoved(pos, 0) //recyclerView refresh
        }
    }

    fun updateValues() {
        for (currencyName in editTextKeyValue.keys) {
            updateCurrencyValue(currencyName, getValueByName(currencyName))
        }
    }

    /**
     * Updating currency value
     * @param currencyName name of the currency
     * @param value value to set
     * */
    private fun updateCurrencyValue(currencyName: String, value: Float?) {
        val editText = editTextKeyValue[currencyName]

        var isTopItem = false
        if (viewModel.modelList.size > 0) {
            val (name) = viewModel.modelList[0]
            if (name == currencyName)
                isTopItem = true
        }

        if (!isTopItem)
            setValueToEditText(editText, value!!)
    }

    /**
     * To get the value by name of the currency
     * @param name of the currency
     * */
    private fun getValueByName(name: String): Float? {
        for ((name1, value) in viewModel.modelList) {
            if (name1 == name)
                return value
        }
        return Float.NaN
    }


    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var reactiveEditText: Disposable? = null

        var textLabel: TextView = itemView.findViewById(R.id.currency_label)
        var editTextRate: EditText = itemView.findViewById(R.id.currency_rateValue)

        fun disposeReactiveEditTextIfPossible() {
            if (reactiveEditText != null)
                reactiveEditText!!.dispose()
        }

        fun setReactiveEditText(reactiveEditText: Disposable) {
            this.reactiveEditText = reactiveEditText
        }
    }

    companion object {
        fun create(viewModel: CurrenciesViewModel): CurrenciesAdapter {
            return CurrenciesAdapter(viewModel)
        }
    }
}
