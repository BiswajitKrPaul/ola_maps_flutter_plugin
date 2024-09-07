package com.goapptiv.ola_maps_flutter_plugin

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import com.goapptiv.ola_maps_flutter_plugin.models.EventTypes
import com.goapptiv.ola_maps_flutter_plugin.models.LatLng
import com.ola.mapsdk.camera.MapControlSettings
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
    context: Context, messenger: BinaryMessenger, viewId: Int, apiKey: String, latLng: LatLng
) : PlatformView, MethodCallHandler, StreamHandler {

    @SuppressLint("InflateParams")
    private val methodChannel: MethodChannel =
        MethodChannel(messenger, "ola_maps_flutter_plugin_$viewId")

    private val eventChannel: EventChannel =
        EventChannel(messenger, "ola_maps_flutter_plugin_event_channel_$viewId")

    private var eventSink: EventSink? = null

    private val olaMapView: OlaMapView = OlaMapView(context)
    private var map: OlaMap? = null

    private val TAG: String = "FlutterMapView"

    private val initialPos: LatLng = latLng

    private val ctx: Context = context

    override fun getView(): View {
        return olaMapView
    }

    init {
        val mapControlSettings =
            MapControlSettings.Builder().setCompassEnabled(false).setRotateGesturesEnabled(false)
                .setTiltGesturesEnabled(false).build()
        olaMapView.getMap(
            apiKey, olaMapCallback = object : OlaMapCallback {
                override fun onMapError(error: String) {
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


                }
            }, mapControlSettings
        )
        methodChannel.setMethodCallHandler(this)
        eventChannel.setStreamHandler(this)
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
    }


    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        when (call.method) {
            "showCurrentLocation" -> {
                map?.showCurrentLocation()
//                map?.moveCameraToLatLong(olaLatLng = map!!.getCurrentLocation(), zoomLevel = 15.0)
            }

            "hideCurrentLocation" -> {
                map?.hideCurrentLocation()
            }

            "moveCameraToLatLong" -> {
                var args = call.arguments as Map<*, *>
                val latLng: LatLng = LatLng.from(args)
                map?.moveCameraToLatLong(
                    olaLatLng = OlaLatLng(latitude = latLng.latitude, longitude = latLng.longitude),
                    zoomLevel = map?.getCurrentOlaCameraPosition()?.zoomLevel ?: 15.0
                )
            }

            "addMarker" -> {
                val args = call.arguments as Map<*, *>
                val latLng: LatLng = LatLng.from(args)
                val markerOptions: OlaMarkerOptions =
                    OlaMarkerOptions.Builder().setMarkerId("Marker " + Math.random().toString())
                        .setPosition(
                            OlaLatLng(
                                latitude = latLng.latitude, longitude = latLng.longitude
                            )
                        ).setIsIconClickable(true).setIconRotation(0f).setIsAnimationEnable(true)
                        .setIsInfoWindowDismissOnClick(true).build()
                val marker = map?.addMarker(
                    olaMarkerOptions = markerOptions
                )
            }

            "getZoomLevel" -> {
                val currentCameraPosition = map?.getCurrentOlaCameraPosition()
                result.success(currentCameraPosition?.zoomLevel)
            }
        }
    }

    private fun drawableToBitmap(drawable: Drawable): Bitmap {


        // Create a Bitmap with the same size as the Drawable
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)


        // Set the bounds and draw the Drawable onto the Canvas
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)

        return bitmap
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
