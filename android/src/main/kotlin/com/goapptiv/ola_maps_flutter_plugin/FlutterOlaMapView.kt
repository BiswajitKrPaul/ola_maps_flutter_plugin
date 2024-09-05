package com.goapptiv.ola_maps_flutter_plugin

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.View
import com.ola.mapsdk.camera.MapControlSettings
import com.ola.mapsdk.interfaces.OlaMapCallback
import com.ola.mapsdk.listeners.OlaMapsListenerManager.OnOlaMapClickedListener
import com.ola.mapsdk.model.OlaLatLng
import com.ola.mapsdk.view.OlaMap
import com.ola.mapsdk.view.OlaMapView
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.platform.PlatformView
import java.util.Objects

class FlutterOlaMapView internal constructor(
    context: Context, messenger: BinaryMessenger, viewId: Int
) : PlatformView, MethodCallHandler {

    @SuppressLint("InflateParams")
    private val methodChannel: MethodChannel =
        MethodChannel(messenger, "ola_maps_flutter_plugin_$viewId")
    private val olaMapView: OlaMapView = OlaMapView(context)
    private var olaMap: OlaMap? = null

    private val TAG: String = "FlutterMapView"

    override fun getView(): View {
        return olaMapView
    }

    init {
        methodChannel.setMethodCallHandler(this)
    }

    override fun dispose() {
        Log.d(TAG, "dispose: Disposed")
    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        when (call.method) {
            "initializeData" -> {
                val args = call.arguments<Map<String, Objects>>()
                val apiKey: String = args?.get("apiKey") as String
                val mapControlSettings = MapControlSettings.Builder().setCompassEnabled(false)
                    .setRotateGesturesEnabled(false).setTiltGesturesEnabled(false).build()
                if (apiKey != "") {
                    olaMapView.getMap(
                        apiKey, olaMapCallback = object : OlaMapCallback {
                            override fun onMapError(error: String) {

                            }

                            override fun onMapReady(olaMap: OlaMap) {
                                Log.d(TAG, "On Map Ready Called")
                                this@FlutterOlaMapView.olaMap = olaMap
                                olaMap.setOnMapClickedListener(onOlaMapClickedListener = object :
                                    OnOlaMapClickedListener {
                                    override fun onOlaMapClicked(olaLatLng: OlaLatLng) {
                                        methodChannel.invokeMethod(
                                            "onTap", mapOf(
                                                "lat" to olaLatLng.latitude,
                                                "lng" to olaLatLng.longitude,
                                            )
                                        )
                                    }
                                })
                            }
                        }, mapControlSettings
                    )
                }
            }

            "addMarker" -> {
                val args = call.arguments<Map<String, Objects>>()

            }
        }
    }


}
