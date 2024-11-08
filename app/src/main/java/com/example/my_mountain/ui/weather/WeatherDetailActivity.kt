package com.example.my_mountain.ui.weather

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.my_mountain.R
import com.example.my_mountain.databinding.ActivityWeatherDetailActicityBinding
import com.example.my_mountain.databinding.ActivityWeatherInfoBinding

class WeatherDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWeatherDetailActicityBinding

    private var locationName: String = ""
    private var nx: Int = 0
    private var ny:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeatherDetailActicityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getDataLocation()

    }

    //넘겨준 데이터 받기
    private fun getDataLocation(){
        locationName = intent.getStringExtra("name").toString()
        nx = intent.getIntExtra("nx", 0)
        ny = intent.getIntExtra("ny", 0)
        binding.apply {
            locationTextView.text = locationName

        }
    }

}