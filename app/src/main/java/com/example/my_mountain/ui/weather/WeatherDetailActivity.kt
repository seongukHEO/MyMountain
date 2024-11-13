package com.example.my_mountain.ui.weather

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.my_mountain.R
import com.example.my_mountain.dataSource.WeatherDataSource
import com.example.my_mountain.databinding.ActivityWeatherDetailActicityBinding
import com.example.my_mountain.databinding.ActivityWeatherInfoBinding
import com.example.my_mountain.databinding.ItemForecastBinding

class WeatherDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWeatherDetailActicityBinding

    private var locationName: String = ""
    private var nx: Int = 0
    private var ny:Int = 0

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeatherDetailActicityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getDataLocation()
        applyApi()

    }

    //넘겨준 데이터 받기
    private fun getDataLocation(){
        locationName = intent.getStringExtra("name").toString()
        nx = intent.getIntExtra("nx", 0)
        ny = intent.getIntExtra("ny", 0)
        binding.apply {
            locationTextView.text = locationName

        }
        Log.e("test1234", "${nx}, ${ny}")
    }

    //공공데이터 적용하기
    @RequiresApi(Build.VERSION_CODES.O)
    private fun applyApi(){
        val weatherData = WeatherDataSource()

        weatherData.getVillageForecast(
            nx,
            ny,
            successCallback = {list ->
                val currentForecast = list.first()

                binding.temperatureTextview.text = getString(R.string.temperature_text, currentForecast.temperature)
                binding.skyTextview.text = "강수 유형 : ${currentForecast.precipitationType}"
                binding.precipitationTextview.text = getString(R.string.precipitation_text, currentForecast.precipitation)

                binding.childForecastLayout.apply {
                    list.forEachIndexed { index, forecastModel ->
                        if (index == 0){return@forEachIndexed}

                        val itemView = ItemForecastBinding.inflate(layoutInflater)

                        itemView.timeTextView.text = forecastModel.fcstTime
                        itemView.weatherTextView.text = forecastModel.sky
                        itemView.temperatureTextview.text = getString(R.string.temperature_text, currentForecast.temperature)

                        addView(itemView.root)
                    }
                }
            },
            failureCallback = {
                it.printStackTrace()
            }
        )
    }

}