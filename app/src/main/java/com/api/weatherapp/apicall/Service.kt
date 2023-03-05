package com.api.weatherapp.apicall

import com.api.weatherapp.apicall.Constant.BASE_URL
import com.api.weatherapp.dataclass.Weather
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class Service {
    private val weatherApi: WeatherApi

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        weatherApi = retrofit.create(WeatherApi::class.java)
    }

    suspend fun getWeather(): Weather {
        return weatherApi.getWeather()
    }
}