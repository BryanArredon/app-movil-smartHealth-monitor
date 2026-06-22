package com.example.myapplicationrestaurant

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplicationrestaurant.ui.theme.SmartHealthMonitorTheme
import com.example.myapplicationrestaurant.ui.screens.DashboardScreen
import com.example.myapplicationrestaurant.ui.screens.HistorialScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartHealthMonitorTheme {
                val navController = rememberNavController()

                Surface(modifier = Modifier.fillMaxSize()) {
                    NavHost(navController = navController, startDestination = "login") {
                        composable("login") {
                            LoginScreen(
                                onLoginSuccess = {
                                    navController.navigate("dashboard") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                    Log.d("SmartHealth", "Login exitoso")
                                }
                            )
                        }
                        composable("dashboard") {
                            val configuration = LocalConfiguration.current
                            val isLandscape = configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE
                            
                            DashboardScreen(
                                onHistorialClick = { navController.navigate("historial") },
                                onAlertClick = { /* Enviar alerta */ }
                            )
                        }
                        composable("historial") {
                            HistorialScreen(
                                onBackClick = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}
