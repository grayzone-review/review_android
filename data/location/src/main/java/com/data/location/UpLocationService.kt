package com.data.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.cancellation.CancellationException

object UpLocationService {
    private lateinit var appContext: Context

    val DEFAULT_SEOUL_TOWNHALL: Pair<Double, Double> = 37.566535 to 126.9779692

    fun init(context: Context) {
        appContext = context.applicationContext
    }

    val locationPermissions = listOf(
        Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION
    )

    @SuppressLint("MissingPermission")
    suspend fun fetchCurrentLocation(): Pair<Double, Double>? {
        val fused = LocationServices.getFusedLocationProviderClient(appContext)
        val token  = CancellationTokenSource().token

        return try {
            val loc = fused.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, token).await()
            return loc?.let { it.latitude to it.longitude }
        } catch (e: CancellationException) {
            return null
        }
    }
}