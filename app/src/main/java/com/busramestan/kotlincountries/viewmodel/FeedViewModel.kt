package com.busramestan.kotlincountries.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.busramestan.kotlincountries.model.Country
import com.busramestan.kotlincountries.service.CountryAPIService
import com.busramestan.kotlincountries.service.CountryDatabase
import com.busramestan.kotlincountries.util.CustomSharedPreferences
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import retrofit2.Response
import kotlin.coroutines.CoroutineContext

class FeedViewModel(application : Application) : BaseViewModel (application)  {  //burada canlı dataları tutucaz.3 tane life datamız olucak.

    private val countryAPIService = CountryAPIService()
    private val disposable= CompositeDisposable()
    private var customPreferences= CustomSharedPreferences(getApplication())
    private var refreshTime=10 * 60 *1000 * 1000 * 1000L

    val countries = MutableLiveData<List<Country>> () // mutable değiştirilebilir
    val countryError=MutableLiveData<Boolean>()
    val countryLoading =MutableLiveData<Boolean>()


    fun refreshData(){

        val updateTime=customPreferences.getTime()

        if(updateTime != null && updateTime != 0L && System.nanoTime()-updateTime< refreshTime ){
            //10 dakikadan küçükse sql den alıcak
            getDataFromSQLite()
        }else{

            getDataFromAPI2()
        }

    }
    fun refreshFromAPI(){


        getDataFromAPI2()

    }

    private fun getDataFromSQLite(){

        countryLoading.value=true

        launch {
            val countries=CountryDatabase(getApplication()).countryDao().getAllCountries()
            showCountries(countries)
            Toast.makeText(getApplication(),"Countries from SQLite",Toast.LENGTH_LONG).show()

        }
    }

    /*
    private fun getDataFromAPI(){
        countryLoading.value=true
        disposable.add(
            countryAPIService.getData()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object :DisposableSingleObserver<List<Country>>() {
                    override fun onSuccess(t: List<Country>) {
                        countries.value = t
                        countryError.value = false
                        countryLoading.value = false

                    }

                    override fun onError(e: Throwable) {
                        countryLoading.value = false
                        countryError.value = true
                        println("Exception-FeedViewModel-getDataFromAPI --> ${"e"}")
                        println(e)
                        e.printStackTrace()
                    }

                })

        )
    }

     */

    private fun getDataFromAPI2(){
        countryLoading.value=true
        disposable.add(
            countryAPIService.getData()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object :DisposableSingleObserver<Response<List<Country>>>() {


                    override fun onSuccess(t: Response<List<Country>>) {
                        storeInSQLite(t)
                        Toast.makeText(getApplication(),"Countries from API",Toast.LENGTH_LONG).show()

                      /*  t.code()
                        countries.value = t.body()
                        countryError.value = false
                        countryLoading.value = false

                       */
                    }

                    override fun onError(e: Throwable) {

                        countryLoading.value = false
                        countryError.value = true

                    }

                })

        )
    }

    private fun showCountries(countryList: List<Country>){

        countries.value = countryList
        countryError.value = false
        countryLoading.value = false


    }
    private fun storeInSQLite(list :Response<List<Country>>){
        launch {

            val dao =CountryDatabase(getApplication()).countryDao()
            dao.deleteAllCountries()
            var listLong = listOf<Long>()
            list.body()?.let {
                listLong =dao.insertAll(*it.toTypedArray())

                var i=0
                while (i< it.size){

                    it[i].uuid =listLong[i].toInt()

                    i= i + 1
                }
            }
            showCountries(list.body()!!)
        }
        customPreferences.saveTime(System.nanoTime())


    }




}












