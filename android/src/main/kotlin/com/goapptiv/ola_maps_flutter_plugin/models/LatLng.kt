package com.goapptiv.ola_maps_flutter_plugin.models

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class LatLng( val latitude: Double , val longitude: Double )
