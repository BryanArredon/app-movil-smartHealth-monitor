package com.example.myapplicationrestaurant.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lecturas_fc")
data class LecturaFC(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val valorBpm: Int,
    val hora: String,
    val esNormal: Boolean = valorBpm in 60..100
)
