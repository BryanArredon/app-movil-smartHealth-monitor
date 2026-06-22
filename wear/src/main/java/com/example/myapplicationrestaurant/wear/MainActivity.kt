package com.example.myapplicationrestaurant.wear

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.myapplicationrestaurant.wear.presentation.theme.SmartHealthWearTheme
import com.example.myapplicationrestaurant.wear.presentation.SmartHealthWearNavGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartHealthWearTheme {
                SmartHealthWearNavGraph()
            }
        }
    }
}
