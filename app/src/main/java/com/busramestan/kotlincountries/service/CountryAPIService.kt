package com.busramestan.kotlincountries.service

import com.busramestan.kotlincountries.model.Country
import io.reactivex.Single
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class CountryAPIService {

    private val BASE_URL ="https://raw.githubusercontent.com/"
    private val api =Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(CountryAPI::class.java)

    fun getData() : Single<Response<List<Country>>> {

        return api.getCountries()

    }


}