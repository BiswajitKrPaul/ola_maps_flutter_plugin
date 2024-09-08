package com.goapptiv.ola_maps_flutter_plugin.models

data class OlaMapConfigurations(
    val apiKey: String,
    val showCurrentLocation: Boolean,
    val setCompassEnabled: Boolean,
    val setRotateGesturesEnabled: Boolean,
    val setTiltGesturesEnabled: Boolean,
    val initialPosition: LatLng,
    val setZoomGesturesEnabled: Boolean,
    val setDoubleTapGesturesEnabled: Boolean,
) {
    companion object {
        fun fromMap(configurations: Map<*, *>): OlaMapConfigurations {
            return OlaMapConfigurations(
                apiKey = configurations["apiKey"] as String,
                showCurrentLocation = configurations["showCurrentLocation"] as Boolean,
                setCompassEnabled = configurations["setCompassEnabled"] as Boolean,
                setRotateGesturesEnabled = configurations["setRotateGesturesEnabled"] as Boolean,
                setTiltGesturesEnabled = configurations["setTiltGesturesEnabled"] as Boolean,
                initialPosition = LatLng.from(configurations["initialPosition"] as Map<*, *>),
                setZoomGesturesEnabled = configurations["setZoomGesturesEnabled"] as Boolean,
                setDoubleTapGesturesEnabled = configurations["setDoubleTapGesturesEnabled"] as Boolean,
            )
        }
    }
}
