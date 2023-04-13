package com.busramestan.kotlincountries.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel(application : Application):AndroidViewModel(application),CoroutineScope {

    private val job= Job() // arka planda yapılcak iş

    override val coroutineContext: CoroutineContext
        get() = job +Dispatchers.Main  //önce işi yap sonra main threade dön

    override fun onCleared() {  //uygulama kapatılırsa
        super.onCleared()
        job.cancel()
    }

}