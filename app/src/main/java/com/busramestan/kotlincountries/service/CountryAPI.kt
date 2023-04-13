package com.busramestan.kotlincountries.service

import com.busramestan.kotlincountries.model.Country
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET

interface CountryAPI {

    @GET("atilsamancioglu/IA19-DataSetCountries/master/countrydataset.json")
    fun getCountries() :Single<Response<List<Country>>>

}