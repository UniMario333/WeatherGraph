package com.example.project.geoweather.json.mapping.openweather.pojo

import java.util.ArrayList

class OpenWeatherPOJO {
    var coord: Coord? = null
    var weather: List<SimpleWeather> = ArrayList()
    var base: String? = null
    var main: EnvironmentStatus? = null
    var visibility: Int? = null
    var wind: Wind? = null
    var clouds: Cloud? = null
    var rain: Rain? = null
    var snow: Snow? = null
    var dt: Long? = null
    var sys: System? = null
    var id: Long? = null
    var name: String? = null
    var cod: String? = null
    var message: String? = null
}
