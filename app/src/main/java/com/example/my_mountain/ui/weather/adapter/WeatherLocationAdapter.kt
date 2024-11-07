package com.example.my_mountain.ui.weather.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.my_mountain.databinding.ItemWeatherReBinding
import com.example.my_mountain.model.LocationInfoModel

class WeatherLocationAdapter(): ListAdapter<LocationInfoModel, WeatherLocationAdapter.WeatherViewHolder>(differ) {

    private lateinit var recyclerviewClick: WeatherClickInterface

    inner class WeatherViewHolder(val binding: ItemWeatherReBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(weatherData: LocationInfoModel){
            binding.textWeatherMountain.text = "지역 : ${weatherData.locationName}"
            binding.root.setOnClickListener {
                recyclerviewClick.recyclerviewClickLister()
            }
        }
    }


    interface WeatherClickInterface{
        fun recyclerviewClickLister()
    }

    fun recyclerviewClick(recyclerviewClick: WeatherClickInterface){
        this.recyclerviewClick = recyclerviewClick
    }

    companion object {
        val differ = object : DiffUtil.ItemCallback<LocationInfoModel>(){
            override fun areItemsTheSame(oldItem: LocationInfoModel, newItem: LocationInfoModel): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: LocationInfoModel, newItem: LocationInfoModel): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        return WeatherViewHolder(ItemWeatherReBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
}

