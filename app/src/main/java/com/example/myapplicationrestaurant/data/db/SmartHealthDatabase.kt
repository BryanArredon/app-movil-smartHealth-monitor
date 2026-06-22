package com.example.myapplicationrestaurant.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [LecturaFC::class], version = 1, exportSchema = false)
abstract class SmartHealthDatabase : RoomDatabase() {
    abstract fun lecturaFCDao(): LecturaFCDao

    companion object {
        @Volatile
        private var INSTANCE: SmartHealthDatabase? = null

        fun getDatabase(context: Context): SmartHealthDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SmartHealthDatabase::class.java,
                    "smart_health_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
