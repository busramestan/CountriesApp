package com.busramestan.kotlincountries.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.busramestan.kotlincountries.R
import com.busramestan.kotlincountries.databinding.FragmentCountryBinding
import com.busramestan.kotlincountries.util.downloadFromURL
import com.busramestan.kotlincountries.util.placeholderProgressBar
import com.busramestan.kotlincountries.viewmodel.CountryViewModel
import kotlinx.android.synthetic.main.fragment_country.*


class CountryFragment : Fragment() {
    private lateinit var viewModel : CountryViewModel
    private var countryUuid=0
    private lateinit var dataBinding : FragmentCountryBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_country, container, false)

        dataBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_country,container,false)

        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            countryUuid=CountryFragmentArgs.fromBundle(it).countryUuid
        }

        viewModel = ViewModelProviders.of(this).get(CountryViewModel ::class.java)
        viewModel.getDataFromRoom(countryUuid)


        observeLiveData()
    }

    fun observeLiveData(){
        viewModel.countryLiveData.observe(viewLifecycleOwner, Observer { country ->
            country?.let{
                dataBinding.selectedCountry= country

            }

        })

    }

}