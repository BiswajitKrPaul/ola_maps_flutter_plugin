package `in`.haxon420.ola_maps_flutter_plugin.models

data class MapCircleOptions(
    val colorHexCode: String,
    val latLng: LatLng,
    val radius: Double,
    val circleOpacity: Double,
    val circleBlur: Double,
    val circleId: Long,
    val borderOptions: MapBorderOptions? = null
) {
    fun toMap(): Map<String, Any?> = mapOf(
        "colorHexCode" to colorHexCode,
        "latLng" to latLng.toMap(),
        "radius" to radius,
        "circleOpacity" to circleOpacity,
        "circleBlur" to circleBlur,
        "circleId" to circleId,
        "borderOptions" to borderOptions?.toMap()
    )

    companion object {
        fun fromMap(map: Map<*, *>): MapCircleOptions {
            return MapCircleOptions(
                colorHexCode = map["colorHexCode"] as String,
                latLng = LatLng.from(map["latLng"] as Map<*, *>),
                radius = map["radius"] as Double,
                circleOpacity = map["circleOpacity"] as Double,
                circleBlur = map["circleBlur"] as Double,
                circleId = (map["circleId"] as Int).toLong(),
                borderOptions = (map["borderOptions"] as? Map<*, *>)?.let {
                    MapBorderOptions.fromMap(it)
                })
        }
    }
}
