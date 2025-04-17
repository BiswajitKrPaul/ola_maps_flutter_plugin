package `in`.haxon420.ola_maps_flutter_plugin

import io.flutter.embedding.engine.plugins.FlutterPlugin

/** OlaMapsFlutterPlugin */
class OlaMapsFlutterPlugin : FlutterPlugin {

    private var olaMapViewFactory: OlaMapViewFactory? = null

    override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {

        olaMapViewFactory = OlaMapViewFactory(flutterPluginBinding.binaryMessenger)
        flutterPluginBinding.platformViewRegistry.registerViewFactory(
            "in.haxon420.ola_maps_flutter_plugin.OlaMap", olaMapViewFactory!!
        )
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        olaMapViewFactory = null
    }
}
