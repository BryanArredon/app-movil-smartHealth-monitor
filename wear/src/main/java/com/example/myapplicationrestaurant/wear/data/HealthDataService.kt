package com.example.myapplicationrestaurant.wear.data

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.health.services.client.HealthServices
import androidx.health.services.client.MeasureCallback
import androidx.health.services.client.data.Availability
import androidx.health.services.client.data.DataPointContainer
import androidx.health.services.client.data.DataType
import androidx.health.services.client.data.DeltaDataType
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class HealthDataService : Service() {
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)
    
    private val measureClient by lazy { HealthServices.getClient(this).measureClient }
    private val messageClient by lazy { Wearable.getMessageClient(this) }

    private val callback = object : MeasureCallback {
        override fun onAvailabilityChanged(dataType: DeltaDataType<*, *>, availability: Availability) {
            Log.d("HealthService", "Availability changed: $availability")
        }

        override fun onDataReceived(data: DataPointContainer) {
            val samples = data.getData(DataType.HEART_RATE_BPM)
            samples.forEach { sample ->
                val bpm = sample.value
                Log.d("HealthService", "Heart rate received: $bpm")
                sendToPhone("/smarthealthmonitor/fc", bpm.toString())
            }
        }
    }

    private fun sendToPhone(path: String, message: String) {
        scope.launch {
            try {
                val nodes = Wearable.getNodeClient(this@HealthDataService).connectedNodes.await()
                nodes.forEach { node ->
                    messageClient.sendMessage(node.id, path, message.toByteArray()).await()
                }
            } catch (e: Exception) {
                Log.e("HealthService", "Error sending message", e)
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        scope.launch {
            measureClient.registerMeasureCallback(DataType.HEART_RATE_BPM, callback)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.launch {
            measureClient.unregisterMeasureCallbackAsync(DataType.HEART_RATE_BPM, callback)
            job.cancel()
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
