package com.example.my_mountain.ui.weather

import android.graphics.Color
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
import java.time.LocalTime

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
        updateBackgroundColorBasedOnTime()

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


                        itemView.timeTextView.text = forecastModel.fcstTime?.let {
                            convertTo12HourFormat(
                                it
                            )
                        }
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

    fun convertTo12HourFormat(fcstTime: String): String {
        // 24시간 형식의 시간 문자열을 12시간 형식으로 변환
        val hour = fcstTime.substring(0, 2).toInt()
        val isPM = hour >= 12
        val convertedHour = if (hour % 12 == 0) 12 else hour % 12

        // "오전" 또는 "오후" 표시 추가
        val period = if (isPM) "오후" else "오전"

        return "$period ${convertedHour}시"
    }


    //현재 시간 가져와서 배경 변경하기
    @RequiresApi(Build.VERSION_CODES.O)
    fun updateBackgroundColorBasedOnTime() {
        // 현재 시간을 구하기
        val currentTime = LocalTime.now()
        val morningTime = LocalTime.of(7, 0)   // 오전 7시
        val eveningTime = LocalTime.of(18, 0)  // 오후 6시

        // 시간에 따른 배경색 설정
        val backgroundColor = if (currentTime.isAfter(morningTime) && currentTime.isBefore(eveningTime)) {
            // 오전 7시 ~ 오후 6시: 빨간색 배경
            R.drawable.weather_morning_background
        } else {
            // 오후 6시 ~ 다음날 오전 7시: 검정색 배경
            R.drawable.weather_widget_background
        }

        // 배경색 적용
        binding.root.setBackgroundResource(backgroundColor)
    }


}