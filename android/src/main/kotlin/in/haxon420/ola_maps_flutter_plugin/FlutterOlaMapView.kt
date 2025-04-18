package `in`.haxon420.ola_maps_flutter_plugin

import android.content.Context
import android.util.Log
import android.view.View
import com.ola.mapsdk.camera.MapControlSettings
import com.ola.mapsdk.interfaces.MarkerEventListener
import com.ola.mapsdk.interfaces.OlaMapCallback
import com.ola.mapsdk.listeners.OlaMapsListenerManager
import com.ola.mapsdk.model.BorderOptions
import com.ola.mapsdk.model.OlaCircleOptions
import com.ola.mapsdk.model.OlaLatLng
import com.ola.mapsdk.model.OlaMarkerOptions
import com.ola.mapsdk.model.OlaPolylineOptions
import com.ola.mapsdk.view.Circle
import com.ola.mapsdk.view.Marker
import com.ola.mapsdk.view.OlaMap
import com.ola.mapsdk.view.OlaMapView
import com.ola.mapsdk.view.Polyline
import `in`.haxon420.ola_maps_flutter_plugin.models.CameraUpdate
import `in`.haxon420.ola_maps_flutter_plugin.models.EventTypes
import `in`.haxon420.ola_maps_flutter_plugin.models.LatLng
import `in`.haxon420.ola_maps_flutter_plugin.models.MapCircleOptions
import `in`.haxon420.ola_maps_flutter_plugin.models.MarkerOptions
import `in`.haxon420.ola_maps_flutter_plugin.models.MethodCallFunctionName
import `in`.haxon420.ola_maps_flutter_plugin.models.OlaMapConfigurations
import `in`.haxon420.ola_maps_flutter_plugin.models.PolyLinesOption
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.EventChannel.EventSink
import io.flutter.plugin.common.EventChannel.StreamHandler
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.platform.PlatformView

class FlutterOlaMapView internal constructor(
    context: Context,
    messenger: BinaryMessenger,
    viewId: Int,
    olaMapConfigurations: OlaMapConfigurations
) : PlatformView, MethodCallHandler, StreamHandler {

    private val methodChannel: MethodChannel =
        MethodChannel(messenger, "ola_maps_flutter_plugin_$viewId")

    private val eventChannel: EventChannel =
        EventChannel(messenger, "ola_maps_flutter_plugin_event_channel_$viewId")

    private var eventSink: EventSink? = null

    private val olaMapView: OlaMapView = OlaMapView(context)
    private var map: OlaMap? = null

    private val TAG: String = "FlutterMapView"

    private val initialPos: LatLng = olaMapConfigurations.initialPosition

    private var addedMarkers = hashMapOf<String, Marker?>()
    private var addedPolyLines = hashMapOf<String, Polyline?>()
    private var addedCircles = hashMapOf<Long, Circle?>()

    override fun getView(): View {
        return olaMapView
    }

    init {
        val mapControlSettings =
            MapControlSettings.Builder().setCompassEnabled(olaMapConfigurations.setCompassEnabled)
                .setRotateGesturesEnabled(olaMapConfigurations.setRotateGesturesEnabled)
                .setTiltGesturesEnabled(olaMapConfigurations.setTiltGesturesEnabled)
                .setZoomGesturesEnabled(olaMapConfigurations.setZoomGesturesEnabled)
                .setDoubleTapGesturesEnabled(olaMapConfigurations.setDoubleTapGesturesEnabled)
                .build()
        olaMapView.getMap(
            olaMapConfigurations.apiKey, olaMapCallback = object : OlaMapCallback {
                override fun onMapError(error: String) {
                    eventSink?.error("map_error", error, error)
                }

                override fun onMapReady(olaMap: OlaMap) {
                    Log.d(TAG, "On Map Ready Called")
                    map = olaMap
                    eventSink?.success(
                        getEventMap(
                            EventTypes.OnMapReady, mapOf(
                                "map_created" to true
                            )
                        )
                    )
                    moveToInitialPosition()
                    // Set Up of all Event Listeners
                    olaMap.setOnMapClickedListener(getMapClickListener())
                    olaMap.setOnMapMoveListener(getMapMovedListener())
                    olaMap.setMarkerListener(getMapMarkerListener())
                }
            }, mapControlSettings
        )
        methodChannel.setMethodCallHandler(this)
        eventChannel.setStreamHandler(this)
    }

    private fun getMapMarkerListener(): MarkerEventListener {
        return object : MarkerEventListener {
            override fun onMarkerClicked(markerId: String) {
                println(markerId)
            }
        }
    }

    fun getMapMovedListener(): OlaMapsListenerManager.OnOlaMapMovedListener {
        return object : OlaMapsListenerManager.OnOlaMapMovedListener {
            override fun onOlaMapMoved() {
                eventSink?.success(
                    getEventMap(EventTypes.OnMapMove, emptyMap())
                )
            }
        }
    }

    fun getMapClickListener(): OlaMapsListenerManager.OnOlaMapClickedListener {
        return object : OlaMapsListenerManager.OnOlaMapClickedListener {
            override fun onOlaMapClicked(olaLatLng: OlaLatLng) {
                eventSink?.success(
                    getEventMap(
                        EventTypes.OnMapClick,
                        LatLng(
                            latitude = olaLatLng.latitude, longitude = olaLatLng.longitude
                        ).toMap(),
                    )
                )
            }
        }
    }


    fun getEventMap(event: EventTypes, data: Map<String, Any>): Map<Any, Any> {
        return mapOf(
            "event_type" to event.name, "data" to data
        )
    }

    override fun dispose() {
        methodChannel.setMethodCallHandler(null)
        eventChannel.setStreamHandler(null)
    }


    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        when (call.method) {
            MethodCallFunctionName.Show_Current_Location -> {
                map?.showCurrentLocation()
            }

            MethodCallFunctionName.Hide_Current_Location -> {
                map?.hideCurrentLocation()
            }

            MethodCallFunctionName.Move_Camera_To_LatLong -> {
                val args = call.arguments as Map<*, *>
                val cameraUpdate: CameraUpdate = CameraUpdate.fromMap(args)
                map?.moveCameraToLatLong(
                    olaLatLng = OlaLatLng(
                        latitude = cameraUpdate.target.latitude,
                        longitude = cameraUpdate.target.longitude
                    ), zoomLevel = cameraUpdate.zoomLevel, durationMs = cameraUpdate.durationInMs
                )
            }

            MethodCallFunctionName.Add_Marker -> {
                val args = call.arguments as Map<*, *>
                val markerOptions: MarkerOptions = MarkerOptions.fromMap(args)
                val olaMarkerOptions: OlaMarkerOptions =
                    OlaMarkerOptions.Builder().setMarkerId(markerOptions.markerId).setPosition(
                        OlaLatLng(
                            latitude = markerOptions.position.latitude,
                            longitude = markerOptions.position.longitude
                        )
                    ).setIsIconClickable(markerOptions.isIconClickable)
                        .setIconRotation(markerOptions.iconRotation.toFloat())
                        .setIsAnimationEnable(markerOptions.isAnimationEnabled)
                        .setIsInfoWindowDismissOnClick(markerOptions.isInfoWindowDismissOnClick)
                        .setSnippet(markerOptions.infoWindowSnippet.ifEmpty { null }).build()
                val createdMarker = map?.addMarker(
                    olaMarkerOptions = olaMarkerOptions
                )
                addedMarkers.put(olaMarkerOptions.markerId, createdMarker)
            }

            MethodCallFunctionName.Get_Zoom_Level -> {
                val currentCameraPosition = map?.getCurrentOlaCameraPosition()
                result.success(currentCameraPosition?.zoomLevel)
            }

            MethodCallFunctionName.Remove_Marker -> {
                val markerId = call.arguments as String
                if (addedMarkers.containsKey(markerId)) {
                    addedMarkers[markerId]?.removeMarker()
                    addedMarkers.remove(markerId)
                    result.success(true)
                } else {
                    result.error("marker_not_found", "Marker not found", null)
                }
            }

            MethodCallFunctionName.AddPolyline -> {
                val args = call.arguments as Map<*, *>
                val polylineOptions: PolyLinesOption = PolyLinesOption.fromMap(args)
                val olaPolyLinesOption =
                    OlaPolylineOptions.Builder().setPolylineId(polylineOptions.polylineId)
                        .setPoints(polylineOptions.points.map {
                            OlaLatLng(
                                it.latitude, it.longitude
                            )
                        } as ArrayList<OlaLatLng>).setLineType(polylineOptions.lineType)
                        .setColor(polylineOptions.color).setWidth(polylineOptions.width.toFloat())
                        .build()

                val createdPolyline = map?.addPolyline(olaPolyLinesOption)
                addedPolyLines.put(polylineOptions.polylineId, createdPolyline)
            }

            MethodCallFunctionName.RemovePolyline -> {
                val polylineId = call.arguments as String
                if (addedPolyLines.containsKey(polylineId)) {
                    addedPolyLines[polylineId]?.removePolyline()
                    addedPolyLines.remove(polylineId)
                    result.success(true)
                } else {
                    result.error("polyline_not_found", "Polyline not found", null)
                }
            }

            MethodCallFunctionName.UpdatePolylineColor -> {
                val args = call.arguments as Map<*, *>
                val polylineId = args["polylineId"] as String
                val color = args["color"] as String
                if (addedPolyLines.containsKey(polylineId)) {
                    addedPolyLines[polylineId]?.setColor(color)
                    result.success(true)
                } else {
                    result.error("polyline_not_found", "Polyline not found", null)
                }
            }

            MethodCallFunctionName.UpdatePolylineWidth -> {
                val args = call.arguments as Map<*, *>
                val polylineId = args["polylineId"] as String
                val width = args["width"] as Double
                if (addedPolyLines.containsKey(polylineId)) {
                    addedPolyLines[polylineId]?.setWidth(width.toFloat())
                    result.success(true)
                } else {
                    result.error("polyline_not_found", "Polyline not found", null)
                }
            }

            MethodCallFunctionName.UpdatePolylinePoints -> {
                val args = call.arguments as Map<*, *>
                val polylineId = args["polylineId"] as String
                val points = args["points"] as List<*>
                val latlng = points.map { LatLng.from(it as Map<*, *>) }
                if (addedPolyLines.containsKey(polylineId)) {
                    addedPolyLines[polylineId]?.setPoints(latlng.map {
                        OlaLatLng(
                            it.latitude, it.longitude
                        )
                    } as ArrayList<OlaLatLng>)
                    result.success(true)
                } else {
                    result.error("polyline_not_found", "Polyline not found", null)
                }
            }

            MethodCallFunctionName.UpdatePolylineLineType -> {
                val args = call.arguments as Map<*, *>
                val polylineId = args["polylineId"] as String
                val lineType = args["lineType"] as String
                if (addedPolyLines.containsKey(polylineId)) {
                    addedPolyLines[polylineId]?.setLineType(lineType)
                    result.success(true)
                } else {
                    result.error("polyline_not_found", "Polyline not found", null)
                }
            }

            MethodCallFunctionName.AddCircle -> {
                val args = call.arguments as Map<*, *>
                val circleOptions: MapCircleOptions = MapCircleOptions.fromMap(args)
                var borderOptions: BorderOptions? = null
                if (circleOptions.borderOptions != null) {
                    borderOptions = BorderOptions.Builder()
                        .setBorderColor(circleOptions.borderOptions.borderColor)
                        .setBorderWidth(circleOptions.borderOptions.borderWidth.toFloat())
                        .setBorderLineType(circleOptions.borderOptions.borderLineType)
                        .setBorderLineDashArray(circleOptions.borderOptions.borderLineDashArray?.map { it.toFloat() }
                            ?.toTypedArray()).build()
                }
                val olaCircleOptionsBuilder =
                    OlaCircleOptions.Builder().setCircleId(circleOptions.circleId)
                        .setCircleBlur(circleOptions.circleBlur.toFloat())
                        .setCircleOpacity(circleOptions.circleOpacity.toFloat())
                        .setColorHexCode(circleOptions.colorHexCode)
                        .setRadius(circleOptions.radius.toFloat()).setOlaLatLng(
                            OlaLatLng(
                                latitude = circleOptions.latLng.latitude,
                                longitude = circleOptions.latLng.longitude
                            )
                        )
                if (borderOptions != null) olaCircleOptionsBuilder.setBorderOptions(borderOptions)
                val olaCircleOptions = olaCircleOptionsBuilder.build()
                val createdCircle = map?.addCircle(olaCircleOptions)
                addedCircles.put(circleOptions.circleId, createdCircle)
            }

            else -> {
                throw NotImplementedError("Method ${call.method} is not implemented")
            }
        }
    }

    private fun moveToInitialPosition() {
        map?.moveCameraToLatLong(
            olaLatLng = OlaLatLng(
                latitude = initialPos.latitude,
                longitude = initialPos.longitude,
            ), zoomLevel = map?.getCurrentOlaCameraPosition()?.zoomLevel ?: 15.0
        )
    }

    override fun onListen(arguments: Any?, events: EventSink?) {
        eventSink = events
    }

    override fun onCancel(arguments: Any?) {
        eventSink = null
    }
}
