package com.api.weatherapp.mvvm

import com.api.weatherapp.apicall.Service
import com.api.weatherapp.dataclass.Weather

class WeatherRepository(private val service: Service = Service()) {

    suspend fun getWeather(): Weather {
        return service.getWeather()
    }

}