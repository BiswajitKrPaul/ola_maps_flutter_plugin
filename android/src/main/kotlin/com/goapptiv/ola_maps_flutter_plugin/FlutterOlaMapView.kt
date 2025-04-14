package com.goapptiv.ola_maps_flutter_plugin

import android.content.Context
import android.util.Log
import android.view.View
import com.goapptiv.ola_maps_flutter_plugin.models.CameraUpdate
import com.goapptiv.ola_maps_flutter_plugin.models.EventTypes
import com.goapptiv.ola_maps_flutter_plugin.models.LatLng
import com.goapptiv.ola_maps_flutter_plugin.models.MarkerOptions
import com.goapptiv.ola_maps_flutter_plugin.models.OlaMapConfigurations
import com.ola.mapsdk.camera.MapControlSettings
import com.ola.mapsdk.interfaces.MarkerEventListener
import com.ola.mapsdk.interfaces.OlaMapCallback
import com.ola.mapsdk.listeners.OlaMapsListenerManager
import com.ola.mapsdk.model.OlaLatLng
import com.ola.mapsdk.model.OlaMarkerOptions
import com.ola.mapsdk.view.OlaMap
import com.ola.mapsdk.view.OlaMapView
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

    private var addedMarkers = hashMapOf<String, OlaMarkerOptions>()

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
        eventChannel.setStreamHandler(this)
    }


    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        when (call.method) {
            "showCurrentLocation" -> {
                map?.showCurrentLocation()
            }

            "hideCurrentLocation" -> {
                map?.hideCurrentLocation()
            }

            "moveCameraToLatLong" -> {
                val args = call.arguments as Map<*, *>
                val cameraUpdate: CameraUpdate = CameraUpdate.fromMap(args)
                map?.moveCameraToLatLong(
                    olaLatLng = OlaLatLng(
                        latitude = cameraUpdate.target.latitude,
                        longitude = cameraUpdate.target.longitude
                    ), zoomLevel = cameraUpdate.zoomLevel, durationMs = cameraUpdate.durationInMs
                )
            }

            "addMarker" -> {
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
                addedMarkers.put(olaMarkerOptions.markerId,olaMarkerOptions)
                map?.addMarker(
                    olaMarkerOptions = olaMarkerOptions
                )
            }

            "getZoomLevel" -> {
                val currentCameraPosition = map?.getCurrentOlaCameraPosition()
                result.success(currentCameraPosition?.zoomLevel)
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
