package com.example.myapplicationrestaurant.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import com.example.myapplicationrestaurant.data.SmartHealthRepository
import com.example.myapplicationrestaurant.data.models.SmartHealthData.MockData
import com.example.myapplicationrestaurant.data.db.LecturaFC

class DashboardViewModel : ViewModel() {
    // FC: viene del wearable real vía Repository.
    val fc: StateFlow<Int> = SmartHealthRepository.fcFlow
        .map { if (it == 0) MockData.fcActual else it }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = MockData.fcActual
        )

    val pasos: StateFlow<Int> = SmartHealthRepository.pasosFlow
        .map { if (it == 0) MockData.pasosActual else it }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = MockData.pasosActual
        )

    // Historial desde Room
    val historial: StateFlow<List<LecturaFC>> = SmartHealthRepository.getHistorial()
        ?.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        ) ?: MutableStateFlow(emptyList())
}
