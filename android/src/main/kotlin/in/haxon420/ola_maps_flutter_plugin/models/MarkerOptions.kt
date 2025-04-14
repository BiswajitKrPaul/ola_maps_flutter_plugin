package `in`.haxon420.ola_maps_flutter_plugin.models

import kotlin.collections.get

data class MarkerOptions(
    val markerId: String,
    val position: LatLng,
    val isIconClickable: Boolean,
    val iconRotation: Double,
    val isAnimationEnabled: Boolean,
    val isInfoWindowDismissOnClick: Boolean,
    val infoWindowSnippet: String,
) {
    companion object {
        fun fromMap(map: Map<*, *>): MarkerOptions {
            val markerId = map["id"] as String
            val position = LatLng.from(map["position"] as Map<*, *>)
            val isIconClickable = map["isIconClickable"] as Boolean
            val iconRotation = map["iconRotation"] as Double
            val isAnimationEnabled = map["isAnimationEnabled"] as Boolean
            val isInfoWindowDismissOnClick = map["isInfoWindowDismissOnClick"] as Boolean
            val infoWindowSnippet = map["infoWindowSnippet"] as String
            return MarkerOptions(
                markerId,
                position,
                isIconClickable,
                iconRotation,
                isAnimationEnabled,
                isInfoWindowDismissOnClick,
                infoWindowSnippet
            )
        }
    }

    fun toMap(): Map<String, Any> {
        return mapOf(
            "markerId" to markerId,
            "position" to position.toMap(),
            "isIconClickable" to isIconClickable,
            "iconRotation" to iconRotation,
            "isAnimationEnabled" to isAnimationEnabled,
            "isInfoWindowDismissOnClick" to isInfoWindowDismissOnClick,
            "infoWindowSnippet" to infoWindowSnippet
        )
    }
}
