package com.example.my_mountain.ui.weather.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.my_mountain.dataSource.LocationInfoDataSource
import com.example.my_mountain.model.LocationInfoModel

class WeatherLocationViewModel : ViewModel() {

    private val locationInfoDataSource = LocationInfoDataSource()

    private val _locationInfoList = MutableLiveData<List<LocationInfoModel>>()
    var locationInfoList : LiveData<List<LocationInfoModel>> = _locationInfoList


    suspend fun getLocationListData(){
        val locationInfo = locationInfoDataSource.getParkingResInfo()
        val locationInfoList = mutableListOf<LocationInfoModel>()

        locationInfo.forEach {
            locationInfoList.add(it)

            _locationInfoList.value = locationInfoList
        }
    }

}