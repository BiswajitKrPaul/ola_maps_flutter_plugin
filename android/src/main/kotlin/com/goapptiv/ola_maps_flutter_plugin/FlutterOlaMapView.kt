package com.goapptiv.ola_maps_flutter_plugin

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.View
import com.goapptiv.ola_maps_flutter_plugin.models.LatLng
import com.ola.mapsdk.camera.MapControlSettings
import com.ola.mapsdk.interfaces.OlaMapCallback
import com.ola.mapsdk.listeners.OlaMapsListenerManager
import com.ola.mapsdk.model.OlaLatLng
import com.ola.mapsdk.view.OlaMap
import com.ola.mapsdk.view.OlaMapView
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.platform.PlatformView

class FlutterOlaMapView internal constructor(
    context: Context, messenger: BinaryMessenger, viewId: Int, apiKey: String, latLng: LatLng
) : PlatformView, MethodCallHandler {

    @SuppressLint("InflateParams")
    private val methodChannel: MethodChannel =
        MethodChannel(messenger, "ola_maps_flutter_plugin_$viewId")
    private val olaMapView: OlaMapView = OlaMapView(context)
    private var map: OlaMap? = null

    private val TAG: String = "FlutterMapView"

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
                    methodChannel.invokeMethod("onMapReady", null)
                    olaMap.setOnMapClickedListener(onOlaMapClickedListener = object :
                        OlaMapsListenerManager.OnOlaMapClickedListener {
                        override fun onOlaMapClicked(olaLatLng: OlaLatLng) {
                            onMapClicker(olaLatLng)
                        }
                    })
                }
            }, mapControlSettings
        )

        methodChannel.setMethodCallHandler(this)
    }

    fun onMapClicker(olaLatLng: OlaLatLng) {
        methodChannel.invokeMethod(
            "onTap", mapOf(
                "lat" to olaLatLng.latitude,
                "lng" to olaLatLng.longitude,
            )
        )
    }

    override fun dispose() {
        methodChannel.setMethodCallHandler(null)
    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        when (call.method) {
            "initializeData" -> {

            }

            "showCurrentLocation" -> {
                map?.showCurrentLocation()
                map?.moveCameraToLatLong(olaLatLng =  map!!.getCurrentLocation(), zoomLevel = 15.0)
            }

            "addMarker" -> {
                val args = call.arguments<Map<String, Any>>()
            }

            "getZoomLevel" -> {
                val currentCameraPosition = map?.getCurrentOlaCameraPosition()
                result.success(currentCameraPosition?.zoomLevel)
            }
        }
    }


}
