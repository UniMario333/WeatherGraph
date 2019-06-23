package com.example.project.geoweather.json.mapping

import com.example.project.geoweather.GeoLocation
import com.example.project.geoweather.json.mapping.geonames.GeoNamesAPIUtil
import com.example.project.geoweather.json.mapping.geonames.pojo.GeoNames
import com.example.project.geoweather.json.mapping.geonames.pojo.Place
import com.example.project.geoweather.json.mapping.openweather.OpenWeatherAPIUtil
import com.example.project.geoweather.json.mapping.openweather.pojo.Coord
import com.example.project.geoweather.json.mapping.openweather.pojo.OpenWeatherPOJO

import java.io.IOException
import java.util.NoSuchElementException

object GeoWeatherUtil {
    @Throws(NoSuchElementException::class, IOException::class)
    fun getPlacesNear(location: GeoLocation, radius: Int): GeoNames {

        return GeoNamesAPIUtil.getPlacesNear(location, radius)
    }

    @Throws(IOException::class, IllegalArgumentException::class, NoSuchElementException::class)
    fun autocompleteCityName(partialName: String): GeoNames {

        return GeoNamesAPIUtil.autocompleteCityName(partialName)
    }

    @Throws(IOException::class, NoSuchElementException::class)
    fun getWeatherData(citta: String, nazione: String): OpenWeatherPOJO {

        return OpenWeatherAPIUtil.getWeatherData(citta, nazione)
    }

    fun getGeoLocation(place: Place): GeoLocation {
        return GeoLocation(place.lng, place.lat)
    }

    fun getGeoLocation(coords: Coord): GeoLocation {
        return GeoLocation(coords.lon, coords.lat)
    }
}
