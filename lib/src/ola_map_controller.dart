import 'package:flutter/services.dart';
import 'package:ola_maps_flutter_plugin/ola_maps_flutter_plugin.dart';

abstract class OlaMapController {
  Future<double> getZoomLevel();
}

class OlaMapControllerInternal implements OlaMapController {
  final MethodChannel _methodChannel;

  OlaMapControllerInternal.init(
      int id, Function(LatLng)? onTap, LatLng initialPosition)
      : _methodChannel = MethodChannel("ola_maps_flutter_plugin_$id") {
    _methodChannel.setMethodCallHandler((call) {
      switch (call.method) {
        case "onMapReady":
          showCurrentPosition();
          break;
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

  Future<void> showCurrentPosition() {
    return _methodChannel.invokeMethod("showCurrentLocation", null);
  }

  @override
  Future<double> getZoomLevel() async {
    final data = await _methodChannel.invokeMethod("getZoomLevel");
    return Future.value(data);
  }
}
