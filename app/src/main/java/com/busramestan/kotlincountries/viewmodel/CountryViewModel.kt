package com.busramestan.kotlincountries.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.busramestan.kotlincountries.adapter.CountryAdapter
import com.busramestan.kotlincountries.model.Country
import com.busramestan.kotlincountries.service.CountryDatabase
import kotlinx.coroutines.launch

class CountryViewModel(application: Application) : BaseViewModel (application){ //burada mutable yapıcaz bir tane live data tutucaz.

    val countryLiveData =MutableLiveData<Country>()

    fun getDataFromRoom (uuid : Int){

        launch {
            val dao= CountryDatabase(getApplication()).countryDao()
            val country= dao.getCountry(uuid)
            countryLiveData.value=country
        }


    }




}