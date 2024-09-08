package com.goapptiv.ola_maps_flutter_plugin.models


data class LatLng(val latitude: Double, val longitude: Double) {
    companion object {
        fun from(latlng: Map<*, *>): LatLng {
            return LatLng(
                latitude = latlng["latitude"] as Double, longitude = latlng["longitude"] as Double
            )
        }
    }

    fun toMap(): Map<String, Double> {
        return mapOf(
            "latitude" to latitude, "longitude" to longitude
        )
    }
}
