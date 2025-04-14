package com.goapptiv.ola_maps_flutter_plugin.models

import kotlin.time.Duration

data class CameraUpdate(val target: LatLng, val zoomLevel: Double, val durationInMs: Int){
    companion object{
        fun fromMap(map: Map<*, *>): CameraUpdate {
            val target = LatLng.from(map["target"] as Map<*, *>)
            val zoomLevel = map["zoomLevel"] as Double
            val durationInMs = map["animationDurationInMs"] as Int
            return CameraUpdate(target, zoomLevel, durationInMs)
        }
    }
}
