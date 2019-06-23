package com.example.project.geoweather.json.mapping.openweather

import com.google.gson.Gson
import com.example.project.geoweather.json.mapping.GeoAPIUrlUtil
import com.example.project.geoweather.json.mapping.openweather.pojo.OpenWeatherPOJO

import java.io.IOException
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.util.NoSuchElementException

object OpenWeatherAPIUtil {

    @Throws(UnsupportedEncodingException::class)
    private fun getAPIurl(citta: String, nazione: String): String {

        val urlBuff = StringBuilder()

        return urlBuff.append("http://api.openweathermap.org/data/2.5/weather?q=").
            append(URLEncoder.encode(citta, "UTF-8")).
            append(',').
            append(URLEncoder.encode(nazione, "UTF-8")).
            append("&appid=33b23774299815d0281a984945946660&units=metric").toString()
    }

    @Throws(IOException::class)
    fun getWeatherData(citta: String, nazione: String): OpenWeatherPOJO {

        val jsonResponse = GeoAPIUrlUtil.getServerResponse(getAPIurl(citta, nazione))

        val pojo = Gson().fromJson(jsonResponse, OpenWeatherPOJO::class.java)

        if (pojo.cod.equals("404")) {
            throw NoSuchElementException(pojo.message)
        }

        return pojo
    }
}
