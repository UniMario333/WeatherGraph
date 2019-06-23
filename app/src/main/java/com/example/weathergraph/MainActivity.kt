package com.example.weathergraph

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.weathergraph.R

class MainActivity : AppCompatActivity() {

    companion object {
        const val APPLICATION_TAG = "DebugCheck"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
