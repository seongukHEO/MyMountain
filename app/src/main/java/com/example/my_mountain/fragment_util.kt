package com.example.my_mountain

enum class MainFragmentName(val id: Int, val str:String){
    HOME_FRAGMENT(R.id.home_menu, "HomeFragment"),
    LOCATION_FRAGMENT(R.id.compass_menu, "LocationFragment"),
    WEATHER_FRAGMENT(R.id.weather_menu, "WeatherFragment"),
    MY_PAGE_FRAGMENT(R.id.my_page_menu, "MyPageFragment")
}