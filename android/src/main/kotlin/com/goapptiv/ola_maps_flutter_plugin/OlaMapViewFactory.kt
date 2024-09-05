package com.goapptiv.ola_maps_flutter_plugin

import android.content.Context
import com.goapptiv.ola_maps_flutter_plugin.models.LatLng
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.StandardMessageCodec
import io.flutter.plugin.platform.PlatformView
import io.flutter.plugin.platform.PlatformViewFactory

class OlaMapViewFactory(private val messenger: BinaryMessenger) :
    PlatformViewFactory(StandardMessageCodec.INSTANCE) {
    override fun create(context: Context, viewId: Int, args: Any?): PlatformView {

        val creationParams = args as Map<*, *>
        val apiKey:String = creationParams["apiKey"] as String
        val position  = creationParams["initialPosition"] as Map<*, *>
        val initialPosition : LatLng = LatLng(latitude = position["latitude"] as Double, longitude = position["longitude"] as Double)
        return FlutterOlaMapView(context, messenger, viewId,apiKey,initialPosition)
    }
}
