package com.goapptiv.ola_maps_flutter_plugin.models

import kotlinx.serialization.Serializable

@Serializable
data class LatLng(val latitude: Double, val longitude: Double) {
    companion object {
        fun from(plating: Map<*, *>): LatLng {
            return LatLng(
                latitude = plating["latitude"] as Double, longitude = plating["longitude"] as Double
            )
        }
    }

    fun toMap(): Map<String, Double> {
        return mapOf(
            "latitude" to latitude, "longitude" to longitude
        )
    }
}
