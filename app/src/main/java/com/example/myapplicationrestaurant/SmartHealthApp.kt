package com.example.myapplicationrestaurant

import android.app.Application
import com.example.myapplicationrestaurant.shared.data.SmartHealthRepository
import com.example.myapplicationrestaurant.data.db.SmartHealthDatabase
import com.example.myapplicationrestaurant.data.db.LecturaFC
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SmartHealthApp : Application() {
    val database by lazy { SmartHealthDatabase.getDatabase(this) }

    override fun onCreate() {
        super.onCreate()
        
        // Configurar el repositorio para guardar en Room en el teléfono
        SmartHealthRepository.setOnFCChangedListener { bpm ->
            val horaActual = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
            CoroutineScope(Dispatchers.IO).launch {
                database.lecturaFCDao().insert(LecturaFC(valorBpm = bpm, hora = horaActual))
            }
        }
    }
}
