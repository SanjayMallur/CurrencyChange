package co.smallur.currencies.model.entity


import java.util.TreeMap

/**
 * Created by Sanjay
 * Data class for response from server
 * @param base base value
 * @param date date of the value
 * @param rates key value pair of diff rates
 * */
data class RateResponse(var base: String, var date: String, var rates: TreeMap<String, Float>)

