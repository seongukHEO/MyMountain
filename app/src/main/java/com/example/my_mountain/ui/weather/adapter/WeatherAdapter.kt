package com.example.my_mountain.ui.weather.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.my_mountain.databinding.ItemWeatherReBinding

class WeatherAdapter():RecyclerView.Adapter<WeatherViewHolder>() {

    private lateinit var recyclerviewClick: WeatherClickInterface

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        val itemWeatherReBinding = ItemWeatherReBinding.inflate(layoutInflater)
        val viewHolder = WeatherViewHolder(itemWeatherReBinding)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return 10
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        holder.itemWeatherReBinding.root.setOnClickListener {
            recyclerviewClick.recyclerviewClickLister()
        }
    }

    interface WeatherClickInterface{
        fun recyclerviewClickLister()
    }

    fun recyclerviewClick(recyclerviewClick: WeatherClickInterface){
        this.recyclerviewClick = recyclerviewClick
    }
}

class WeatherViewHolder(itemWeatherReBinding: ItemWeatherReBinding):RecyclerView.ViewHolder(itemWeatherReBinding.root){
    val itemWeatherReBinding: ItemWeatherReBinding

    init {
        this.itemWeatherReBinding = itemWeatherReBinding
        this.itemWeatherReBinding.root.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
}