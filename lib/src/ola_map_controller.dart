import 'package:flutter/services.dart';
import 'package:ola_maps_flutter_plugin/ola_maps_flutter_plugin.dart';

class OlaMapController {
  final MethodChannel _methodChannel;
  OlaMapController.init(int id, String apiKey, Function(LatLng)? onTap)
      : _methodChannel = MethodChannel("ola_maps_flutter_plugin_$id") {
    _initializeData(apiKey);
    _methodChannel.setMethodCallHandler((call) {
      switch (call.method) {
        case "onTap":
          onTap?.call(
            LatLng(
              latitude: call.arguments["lat"],
              longitude: call.arguments["lng"],
            ),
          );
          break;
        default:
          throw UnimplementedError("Unimplemented ${call.method}");
      }
      throw UnimplementedError("Unimplemented ${call.method}");
    });
  }

  Future<void> _initializeData(String apiKey) {
    return _methodChannel.invokeMethod("initializeData", {"apiKey": apiKey});
  }
}
