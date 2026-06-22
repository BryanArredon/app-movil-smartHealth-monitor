package com.example.myapplicationrestaurant.shared.data

import com.example.myapplicationrestaurant.shared.data.models.LecturaFC
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Repositorio singleton que centraliza los datos de salud.
 */
object SmartHealthRepository {
    // FC actual del wearable (bpm)
    private val _fcFlow = MutableStateFlow(0)
    val fcFlow: StateFlow<Int> = _fcFlow.asStateFlow()

    // Pasos del día actual
    private val _pasosFlow = MutableStateFlow(0)
    val pasosFlow: StateFlow<Int> = _pasosFlow.asStateFlow()

    // Historial (placeholder para sync Wear-App)
    private val _historialFlow = MutableStateFlow<List<LecturaFC>>(emptyList())
    val historialFlow = _historialFlow.asStateFlow()

    private var onFCChanged: ((Int) -> Unit)? = null

    fun setOnFCChangedListener(listener: (Int) -> Unit) {
        onFCChanged = listener
    }

    fun actualizarFC(bpm: Int) {
        _fcFlow.value = bpm
        onFCChanged?.invoke(bpm)
        
        // Simulación local en el repo compartido para que el reloj vea algo
        val nuevaLectura = LecturaFC(
            id = (System.currentTimeMillis() % 10000).toInt(),
            valorBpm = bpm,
            hora = java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault()).format(java.util.Date())
        )
        _historialFlow.value = (listOf(nuevaLectura) + _historialFlow.value).take(10)
    }

    fun actualizarPasos(pasos: Int) {
        _pasosFlow.value = pasos
    }
    
    fun obtenerHistorial() = historialFlow
}
