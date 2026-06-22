package com.example.myapplicationrestaurant.data

import com.example.myapplicationrestaurant.data.db.LecturaFC
import com.example.myapplicationrestaurant.data.db.LecturaFCDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Repositorio singleton que centraliza los datos de salud.
 */
object SmartHealthRepository {
    private var dao: LecturaFCDao? = null

    fun init(dao: LecturaFCDao) {
        this.dao = dao
    }

    // FC actual del wearable (bpm)
    private val _fcFlow = MutableStateFlow(0)
    val fcFlow: StateFlow<Int> = _fcFlow.asStateFlow()

    // Pasos del día actual
    private val _pasosFlow = MutableStateFlow(0)
    val pasosFlow: StateFlow<Int> = _pasosFlow.asStateFlow()

    fun actualizarFC(bpm: Int) {
        _fcFlow.value = bpm
        val horaActual = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
        CoroutineScope(Dispatchers.IO).launch {
            dao?.insert(LecturaFC(valorBpm = bpm, hora = horaActual))
        }
    }

    fun actualizarPasos(pasos: Int) {
        _pasosFlow.value = pasos
    }

    fun getHistorial() = dao?.getAll()
}
