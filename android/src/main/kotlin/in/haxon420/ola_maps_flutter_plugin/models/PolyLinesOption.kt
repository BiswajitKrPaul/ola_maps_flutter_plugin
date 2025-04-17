package `in`.haxon420.ola_maps_flutter_plugin.models


data class PolyLinesOption(
    val polylineId: String,
    val points: List<LatLng>,
    val color: String,
    val width: Double,
    val lineType: String?
) {
    companion object {
        fun fromMap(map: Map<*, *>): PolyLinesOption {
            val polylineId = map["id"] as String
            val points = map["points"] as List<*>
            val color = map["color"] as String
            val width = map["width"] as Double
            val lineType = map["lineType"] as String?
            return PolyLinesOption(
                polylineId, points.map { LatLng.from(it as Map<*, *>) }, color, width, lineType
            )
        }
    }

    fun toMap(): Map<String, Any?> {
        return mapOf(
            "polylineId" to polylineId,
            "points" to points,
            "color" to color,
            "width" to width,
            "lineType" to lineType
        )
    }
}
