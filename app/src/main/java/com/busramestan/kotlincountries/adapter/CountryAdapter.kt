package com.busramestan.kotlincountries.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.busramestan.kotlincountries.R
import com.busramestan.kotlincountries.databinding.ItemCountryBinding
import com.busramestan.kotlincountries.model.Country
import com.busramestan.kotlincountries.util.downloadFromURL
import com.busramestan.kotlincountries.util.placeholderProgressBar
import com.busramestan.kotlincountries.view.FeedFragmentDirections
import kotlinx.android.synthetic.main.item_country.view.*

class CountryAdapter (val countryList : ArrayList<Country>) : RecyclerView.Adapter<CountryAdapter.CountryViewHolder>(),CountryClickListener{

    class CountryViewHolder(var view : ItemCountryBinding) : RecyclerView.ViewHolder (view.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        //item_country layout ile buradaki adapteri birbirine bağlıyoruz.
        //bu tarz işlemler için inflater kullanıyoruz


        var inflater =LayoutInflater.from(parent.context)
       //val view =inflater.inflate(R.layout.item_country,parent,false)
        val view =DataBindingUtil.inflate<ItemCountryBinding>(inflater,R.layout.item_country,parent,false)
        return CountryViewHolder(view)

    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        holder.view.country= countryList[position]
        holder.view.listener=this


    }

    override fun getItemCount(): Int {
    // kac tane row olusturulacak onu yazıyoruz.
        return countryList.size
    }

    fun updateCountryList(newCountryList : List<Country>){  //kullanıcı sayfayı refresh edince tekrardan güncellenmesi ile ilgili fonk. yazdık
        countryList.clear()
        countryList.addAll(newCountryList)
        notifyDataSetChanged() // adaptörü yenilemek için kulladığımız metot

    }

    override fun onCountryClicked(v: View) {
        val uuid=v.CountryUuidText.text.toString().toInt()
        val action = FeedFragmentDirections.actionFeedFragmentToCountryFragment(uuid)
        //action
        Navigation.findNavController(v).navigate(action)
    }
}

// modeli bitirdik view de tamamlandı arada kalan view modeli de yazıcaz.
//viewmodel da yeni bir kotlin class acıyoruz.
// her viewin kendi view momdeli olması gerekiyor. clean kod olması için her fragmente view model yazıcaz.mvvm amacı budur.