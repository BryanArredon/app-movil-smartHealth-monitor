package com.example.myapplicationrestaurant.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplicationrestaurant.FilaHistorial
import com.example.myapplicationrestaurant.ui.components.TarjetaDato
import com.example.myapplicationrestaurant.ui.viewmodel.DashboardViewModel
import com.example.myapplicationrestaurant.shared.data.SmartHealthRepository
import com.example.myapplicationrestaurant.BuildConfig
import com.example.myapplicationrestaurant.ui.theme.SmartHealthMonitorTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onHistorialClick: () -> Unit = {},
    onAlertClick: () -> Unit = {},
    viewModel: DashboardViewModel = viewModel()
) {
    // collectAsState() convierte StateFlow en State de Compose
    val fc by viewModel.fc.collectAsState()
    val pasos by viewModel.pasos.collectAsState()
    val historial by viewModel.historial.collectAsState()

    // ── Estado del diálogo y Snackbar ──────────────────────
    var mostrarAlerta by remember { mutableStateOf(false) }
    val snackbarHost = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // ── Diálogo condicional ────────────────────────────────
    if (mostrarAlerta) {
        AlertaScreen(
            fc = fc,
            onDismiss = { mostrarAlerta = false },
            onConfirmar = {
                mostrarAlerta = false
                scope.launch {
                    snackbarHost.showSnackbar(
                        message = "⚠️ Alerta enviada a tus contactos de emergencia",
                        duration = SnackbarDuration.Long
                    )
                }
            }
        )
    }

    SmartHealthMonitorTheme {
        Scaffold(
            // ── Snackbar host en el Scaffold ───────────────
            snackbarHost = { SnackbarHost(hostState = snackbarHost) },
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "SmartHealth",
                            style = MaterialTheme.typography.titleLarge
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { mostrarAlerta = true },
                    containerColor = MaterialTheme.colorScheme.error
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Enviar alerta de emergencia",
                        tint = MaterialTheme.colorScheme.onError
                    )
                }
            }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // ── Tarjeta FC ────────────────────────────
                item {
                    TarjetaDato(
                        valor = "$fc",
                        unidad = "bpm",
                        label = "Frecuencia cardíaca",
                        colorValor = MaterialTheme.colorScheme.error
                    )
                }
                // ── Tarjeta Pasos ─────────────────────────
                item {
                    TarjetaDato(
                        valor = "%,d".format(pasos),
                        unidad = "pasos",
                        label = "Pasos del día",
                        colorValor = MaterialTheme.colorScheme.primary
                    )
                }
                // ── Encabezado historial ──────────────────
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Historial reciente",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                    TextButton(onClick = onHistorialClick) {
                        Text("Ver todo")
                    }
                }
                // ── Lista del historial ───────────────────
                items(historial, key = { it.id }) { lectura ->
                    FilaHistorial(lectura = lectura)
                }

                // Botón de simulación — SOLO PARA DEBUG
                item {
                    if (BuildConfig.DEBUG) {
                        OutlinedButton(
                            onClick = {
                                // Simular lectura del wearable
                                val fcSimulado = (60..110).random()
                                SmartHealthRepository.actualizarFC(fcSimulado)
                                SmartHealthRepository.actualizarPasos((3000..8000).random())
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Simular dato del wearable (DEBUG)")
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Dashboard - Light",
    showSystemUi = true, device = "spec:width=411dp,height=891dp")
@Preview(showBackground = true, name = "Dashboard - Dark",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun DashboardScreenPreview() {
    SmartHealthMonitorTheme {
        DashboardScreen()
    }
}
