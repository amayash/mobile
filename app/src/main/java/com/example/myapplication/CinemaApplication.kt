package com.example.myapplication

import android.app.Application
import com.example.myapplication.database.AppContainer
import com.example.myapplication.database.AppDataContainer

class CinemaApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}