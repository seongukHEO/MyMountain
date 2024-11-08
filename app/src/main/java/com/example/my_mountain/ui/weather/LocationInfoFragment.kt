package com.example.my_mountain.ui.weather

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.my_mountain.MainActivity
import com.example.my_mountain.databinding.FragmentWeatherBinding
import com.example.my_mountain.ui.weather.adapter.WeatherLocationAdapter
import com.example.my_mountain.ui.weather.viewModel.WeatherLocationViewModel
import com.google.android.material.divider.MaterialDividerItemDecoration
import kotlinx.coroutines.launch

class LocationInfoFragment : Fragment() {
    lateinit var binding: FragmentWeatherBinding
    lateinit var mainActivity: MainActivity

    val weatherLocationAdapter : WeatherLocationAdapter by lazy {
        val adapter = WeatherLocationAdapter()
        adapter.recyclerviewClick(object : WeatherLocationAdapter.WeatherClickInterface{
            override fun recyclerviewClickLister(name:String, nx: Int, ny: Int) {
                val intent = Intent(mainActivity, WeatherDetailActivity::class.java)
                intent.putExtra("name", name)
                intent.putExtra("ny", ny)
                intent.putExtra("nx", nx)
                startActivity(intent)
            }
        })

        adapter
    }

    val viewModel : WeatherLocationViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentWeatherBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity
        weatherAdapterAll()
        gettingData()
        return binding.root
    }

    private fun weatherAdapterAll(){
        binding.apply {
            recyclerViewWeather.apply {
                adapter = weatherLocationAdapter
                layoutManager = LinearLayoutManager(mainActivity)
                val deco = MaterialDividerItemDecoration(mainActivity, MaterialDividerItemDecoration.VERTICAL)
                addItemDecoration(deco)
            }
        }
    }

    //데이터 받아오기
    private fun gettingData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getLocationListData()
            viewModel.locationInfoList.observe(requireActivity()) { value ->
                Log.d("test1234", "${value.size}")
                weatherLocationAdapter.submitList(value)
            }
        }
    }

}