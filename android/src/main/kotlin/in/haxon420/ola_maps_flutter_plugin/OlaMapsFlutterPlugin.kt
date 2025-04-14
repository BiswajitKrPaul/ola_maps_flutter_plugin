package `in`.haxon420.ola_maps_flutter_plugin

import io.flutter.embedding.engine.plugins.FlutterPlugin

/** OlaMapsFlutterPlugin */
class OlaMapsFlutterPlugin : FlutterPlugin {

    override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        flutterPluginBinding.platformViewRegistry.registerViewFactory(
            "in.haxon420.ola_maps_flutter_plugin.OlaMap",
            OlaMapViewFactory(flutterPluginBinding.binaryMessenger)
        )
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        TODO("NOT yet implemented")
    }
}
