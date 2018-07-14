package co.smallur.currencies.model.network

import co.smallur.currencies.model.entity.RateResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Sanjay
 * Interface to get values
 * */
interface ApiService {

    @GET("/latest")
    fun getCurrencies(@Query("base") base: String): Observable<RateResponse>
}
