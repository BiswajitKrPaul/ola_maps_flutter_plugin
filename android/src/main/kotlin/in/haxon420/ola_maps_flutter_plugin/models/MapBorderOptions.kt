package `in`.haxon420.ola_maps_flutter_plugin.models

data class MapBorderOptions(
    val borderColor: String,
    val borderWidth: Double,
    val borderLineType: String? = null,
    val borderLineDashArray: List<Double>? = null
) {
    fun toMap(): Map<String, Any?> = mapOf(
        "borderColor" to borderColor,
        "borderWidth" to borderWidth,
        "borderLineType" to borderLineType,
        "borderLineDashArray" to borderLineDashArray?.toList()
    )

    companion object {
        fun fromMap(map: Map<*, *>): MapBorderOptions {
            return MapBorderOptions(
                borderColor = map["borderColor"] as String,
                borderWidth = (map["borderWidth"] as Double),
                borderLineType = map["borderLineType"] as? String,
                borderLineDashArray = (map["borderLineDashArray"] as? List<*>)?.map { it as Double })
        }
    }
}