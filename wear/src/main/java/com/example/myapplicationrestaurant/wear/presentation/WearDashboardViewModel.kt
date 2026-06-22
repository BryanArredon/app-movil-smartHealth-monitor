package com.example.myapplicationrestaurant.wear.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplicationrestaurant.shared.data.SmartHealthRepository
import com.example.myapplicationrestaurant.shared.data.models.LecturaFC
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class WearDashboardViewModel : ViewModel() {

    // FC: viene del wearable real vía Repository.
    val fc: StateFlow<Int> = SmartHealthRepository.fcFlow
        .map { if (it == 0) 72 else it }  // valor por defecto
        .stateIn(viewModelScope,
                 SharingStarted.WhileSubscribed(5_000), 72)
                 
    // Historial para el reloj
    val historial: StateFlow<List<LecturaFC>> = 
        SmartHealthRepository.obtenerHistorial()
            .stateIn(viewModelScope, 
                     SharingStarted.WhileSubscribed(5_000), 
                     emptyList())
}
