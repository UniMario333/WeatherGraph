package com.example.weathergraph

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_initial.*

class Initial : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initial)

        searchCity.setOnClickListener {

            val i = Intent(applicationContext, SelectCityActivity::class.java)
            i.putExtra("cittaDaCercare", editText.text.toString())

            this.startActivity(i)
        }
    }
}
