package com.example.my_mountain.ui.weather

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.constraintlayout.helper.widget.Carousel.Adapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.my_mountain.MainActivity
import com.example.my_mountain.R
import com.example.my_mountain.databinding.FragmentWeatherBinding
import com.example.my_mountain.model.WeatherApiModel
import com.example.my_mountain.retrofit.RetrofitWeatherInstance
import com.example.my_mountain.ui.weather.adapter.WeatherAdapter
import com.google.android.material.divider.MaterialDividerItemDecoration
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class WeatherFragment : Fragment() {
    lateinit var binding: FragmentWeatherBinding
    lateinit var mainActivity: MainActivity

    val weatherAdapter : WeatherAdapter by lazy {
        val adapter = WeatherAdapter()
        adapter.recyclerviewClick(object : WeatherAdapter.WeatherClickInterface{
            override fun recyclerviewClickLister() {

            }

        })

        adapter
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentWeatherBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity
        weatherAdapterAll()
        retrofitWork()
        return binding.root
    }

    private fun weatherAdapterAll(){
        binding.apply {
            recyclerViewWeather.apply {
                adapter = weatherAdapter
                layoutManager = LinearLayoutManager(mainActivity)
                val deco = MaterialDividerItemDecoration(mainActivity, MaterialDividerItemDecoration.VERTICAL)
                addItemDecoration(deco)
            }
        }
    }

    //Retrofit
    @RequiresApi(Build.VERSION_CODES.O)
    private fun retrofitWork(){
        val service = RetrofitWeatherInstance.retrofitService
        val currentDate = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE)

        service.getWeather(RetrofitWeatherInstance.LOCAL_KEY, 1, 10, "xml", 10, 1910, currentDate)
            .enqueue(object : retrofit2.Callback<WeatherApiModel>{
                override fun onResponse(p0: Call<WeatherApiModel>, p1: Response<WeatherApiModel>) {
                    val result = p1.body()?.body?.items?.item
                    Log.e("seonguk", "${result?.map { it.cprn }}")
                }

                override fun onFailure(p0: Call<WeatherApiModel>, p1: Throwable) {
                    TODO("Not yet implemented")
                }

            })
    }
}