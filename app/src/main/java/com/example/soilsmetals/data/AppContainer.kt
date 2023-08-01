package com.example.soilsmetals.data

import android.content.Context

interface AppContainer {
    val mapsRepository: MapsRepository
}

class DefaultAppContainer(context: Context): AppContainer {
    override val mapsRepository: MapsRepository by lazy {
        NetworkMapsRepository(context)
    }
}