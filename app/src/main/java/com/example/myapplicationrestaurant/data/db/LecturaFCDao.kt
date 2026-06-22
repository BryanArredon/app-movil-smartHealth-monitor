package com.example.myapplicationrestaurant.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface LecturaFCDao {
    @Query("SELECT * FROM lecturas_fc ORDER BY hora DESC")
    fun getAll(): Flow<List<LecturaFC>>

    @Insert
    suspend fun insert(lectura: LecturaFC)

    @Query("DELETE FROM lecturas_fc")
    suspend fun deleteAll()
}
