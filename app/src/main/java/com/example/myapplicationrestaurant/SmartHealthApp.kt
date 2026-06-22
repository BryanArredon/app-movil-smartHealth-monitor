package com.example.myapplicationrestaurant

import android.app.Application
import com.example.myapplicationrestaurant.data.SmartHealthRepository
import com.example.myapplicationrestaurant.data.db.SmartHealthDatabase

class SmartHealthApp : Application() {
    val database by lazy { SmartHealthDatabase.getDatabase(this) }

    override fun onCreate() {
        super.onCreate()
        SmartHealthRepository.init(database.lecturaFCDao())
    }
}
