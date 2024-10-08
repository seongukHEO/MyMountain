package com.example.my_mountain.ui.weather

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.helper.widget.Carousel.Adapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.my_mountain.MainActivity
import com.example.my_mountain.R
import com.example.my_mountain.databinding.FragmentWeatherBinding
import com.example.my_mountain.ui.weather.adapter.WeatherAdapter
import com.google.android.material.divider.MaterialDividerItemDecoration

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentWeatherBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity
        weatherAdapterAll()
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
}