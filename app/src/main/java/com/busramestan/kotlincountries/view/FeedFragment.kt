package com.busramestan.kotlincountries.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.busramestan.kotlincountries.R
import com.busramestan.kotlincountries.adapter.CountryAdapter
import com.busramestan.kotlincountries.viewmodel.FeedViewModel
import kotlinx.android.synthetic.main.fragment_feed.*


class FeedFragment : Fragment() {

    private lateinit var viewModel : FeedViewModel
    //view modeli tanımlıyoruz
    private var countryAdapter = CountryAdapter(arrayListOf())



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel=ViewModelProviders.of(this).get(FeedViewModel :: class.java)
        //hangi viewmodel ın sınıfında çalışmak istiyoruz bunu belirtiyoruz.
        viewModel.refreshData()

        countryList.layoutManager = LinearLayoutManager(context)
        //countryLİst in layoutunu degistiryoruz.
        countryList.adapter= countryAdapter


        swipeRefreshLayout.setOnRefreshListener {  //Sayfa refresh edilince recyclerviewi yok ettik
            countryList.visibility= View.GONE
            countryError.visibility= View.GONE
            countryLoading.visibility = View.VISIBLE
            viewModel.refreshFromAPI()
            swipeRefreshLayout.isRefreshing=false
        }

        observeLiveData()
    }

     private fun observeLiveData(){  //private yapmıştım.şimdi değştirdim
        viewModel.countries.observe(viewLifecycleOwner,Observer{ countries ->

            countries?. let {
                countryList.visibility = View.VISIBLE
                countryAdapter.updateCountryList(countries)
            }
        })

        viewModel.countryError.observe(viewLifecycleOwner,Observer{ error ->
            error?.let{
                if(it){
                    countryError.visibility= View.VISIBLE
                } else{
                    countryError.visibility = View.GONE
                }
            }

        })

        viewModel.countryLoading.observe(viewLifecycleOwner,Observer{loading ->
            loading?.let {
                if(it){
                    countryLoading.visibility  = View.VISIBLE
                    countryList.visibility = View.GONE // Gösterme
                    countryError.visibility  =View.GONE
                }
                else {
                    countryLoading.visibility = View.GONE

                }

            }
        })

    }



}