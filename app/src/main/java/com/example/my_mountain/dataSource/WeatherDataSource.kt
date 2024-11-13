package com.example.my_mountain.dataSource

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.my_mountain.model.CategoryModel
import com.example.my_mountain.model.ForecastModel
import com.example.my_mountain.model.Item
import com.example.my_mountain.model.WeatherEntity
import com.example.my_mountain.service.WeatherService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherDataSource {

    private val retrofit = Retrofit.Builder()
        .baseUrl("http://apis.data.go.kr/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service = retrofit.create(WeatherService::class.java)

    @RequiresApi(Build.VERSION_CODES.O)
    fun getVillageForecast(
        nx:Int,
        ny:Int,
        successCallback: (List<ForecastModel>) -> Unit,
        failureCallback: (Throwable) -> Unit
    ){

        val baseDate = BaseDateTime.getBaseDateTime()

        Log.e("test1234", "${baseDate.baseDate}, ${baseDate.baseTime}")

        service.getVillageForecast(
            "Qahe3YsG5DEh1NrEibW9IUu4P/yYTgk4lBC6o0giu4nI1UjwSA3iTZXm4OcQ4Z/Q1ALRTLaKfZ6DsMEg+XsoqA==",
            baseDate.baseDate,
            baseDate.baseTime,
            nx,
            ny
        ).enqueue(object : Callback<WeatherEntity> {
            override fun onResponse(p0: Call<WeatherEntity>, p1: Response<WeatherEntity>) {

                val forecastDateTimeMap = mutableMapOf<String, ForecastModel>()

                val forecastList = p1.body()?.response?.body?.items?.item.orEmpty()

                for (forecast in forecastList){

                    forecastDateTimeMap["${forecast?.fcstDate}/${forecast?.fcstTime}"]

                    if ( forecastDateTimeMap["${forecast?.fcstDate}/${forecast?.fcstTime}"] == null){
                        forecastDateTimeMap["${forecast?.fcstDate}/${forecast?.fcstTime}"] = ForecastModel(forecast?.fcstDate, forecast?.fcstTime)
                    }
                    forecastDateTimeMap["${forecast?.fcstDate}/${forecast?.fcstTime}"]?.apply {
                        when(forecast?.category){
                            CategoryModel.POP -> {
                                precipitation = forecast.fcstValue.toString().toInt()
                            }

                            CategoryModel.PTY -> {
                                precipitationType = transformRainType(forecast)
                            }

                            CategoryModel.SKY -> {
                                sky = transformSky(forecast)
                            }

                            CategoryModel.TMP -> {
                                temperature = forecast.fcstValue.toString().toDouble()
                            }

                            else -> {}
                        }
                    }
                }
                //데이터 집어넣기
                //들어오는 값 정리하기
                //최신순으로 줬겠지만 혹시 모르니 한 번 더 정렬한다 (시간순)
                val list = forecastDateTimeMap.values.toMutableList()
                list.sortWith{ f1, f2 ->
                    val f1DateTime = "${f1.fcstDate}${f1.fcstTime}"
                    val f2DateTime = "${f2.fcstDate}${f2.fcstTime}"

                    return@sortWith f1DateTime.compareTo(f2DateTime)
                }

                //유효성 검사
                if (list.isEmpty()){
                    failureCallback(NullPointerException())
                }else {
                    successCallback(list)
                }
            }

            override fun onFailure(p0: Call<WeatherEntity>, p1: Throwable) {
                failureCallback(p1)

            }

        })
    }

    private fun transformRainType(forecast: Item): String{
        return when(forecast.fcstValue.toString().toInt()){
            0 -> "없음"
            1 -> "비"
            2 -> "비/눈"
            3 -> "눈"
            4 -> "소나기"
            else -> ""
        }
    }

    private fun transformSky(forecast: Item): String{
        return when(forecast.fcstValue.toString().toInt()){
            1 -> "맑음"
            3 -> "구름 많음"
            4 -> "흐림"
            else -> ""
        }
    }

}