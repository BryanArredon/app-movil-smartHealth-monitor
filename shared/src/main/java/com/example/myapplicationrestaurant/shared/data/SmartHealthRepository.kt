package com.example.myapplicationrestaurant.shared.data

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

    private var onFCChanged: ((Int) -> Unit)? = null

    fun setOnFCChangedListener(listener: (Int) -> Unit) {
        onFCChanged = listener
    }

    fun actualizarFC(bpm: Int) {
        _fcFlow.value = bpm
        onFCChanged?.invoke(bpm)
    }

    fun actualizarPasos(pasos: Int) {
        _pasosFlow.value = pasos
    }
}
