package com.goapptiv.ola_maps_flutter_plugin

import android.content.Context
import com.goapptiv.ola_maps_flutter_plugin.models.OlaMapConfigurations
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.StandardMessageCodec
import io.flutter.plugin.platform.PlatformView
import io.flutter.plugin.platform.PlatformViewFactory

class OlaMapViewFactory(private val messenger: BinaryMessenger) :
    PlatformViewFactory(StandardMessageCodec.INSTANCE) {
    override fun create(context: Context, viewId: Int, args: Any?): PlatformView {

        val creationParams = args as Map<*, *>
        val mapConfigurations = OlaMapConfigurations.fromMap(creationParams)
        return FlutterOlaMapView(
            context,
            messenger,
            viewId,
            olaMapConfigurations = mapConfigurations
        )
    }
}
