package com.example.project.geoweather.json.mapping.openweather.pojo

import com.google.gson.annotations.SerializedName

class Snow {
    @SerializedName("1h")
    var h1: Float? = null
    @SerializedName("3h")
    var h3: Float? = null
}
