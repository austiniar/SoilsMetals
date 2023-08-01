package com.example.soilsmetals

import android.app.Application
import com.example.soilsmetals.data.AppContainer
import com.example.soilsmetals.data.DefaultAppContainer

class SoilsMetalsApplication: Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}