import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:ola_maps_flutter_plugin/ola_maps_flutter_plugin.dart';
import 'package:ola_maps_flutter_plugin/src/constants.dart';

abstract class OlaMapController {
  void moveCameraToLatLong(LatLng latlng);
  void showCurrentLocation();
  void hideCurrentLocation();
  Future<double> getZoomLevel();
  void addMarker(LatLng latlng);
}

class OlaMapControllerInternal implements OlaMapController {
  final MethodChannel methodChannel;
  final EventChannel eventChannel;

  OlaMapControllerInternal.init(int id, LatLng initialPosition, OlaMap mapRef)
      : eventChannel =
            EventChannel("ola_maps_flutter_plugin_event_channel_$id"),
        methodChannel = MethodChannel("ola_maps_flutter_plugin_$id") {
    eventChannel.receiveBroadcastStream().listen(
          (dynamic data) {
            if (data != null) {
              final recievedData = data as Map<dynamic, dynamic>;
              if (recievedData["event_type"] == EventTypes.onMapReady.name) {
                mapRef.onMapCreated(this);
                if (mapRef.showCurrentLocation) {
                  showCurrentLocation();
                } else {
                  hideCurrentLocation();
                }
              }
              if (recievedData["event_type"] == EventTypes.onMapClick.name) {
                var latlng = LatLng.fromMap(recievedData["data"] as Map);
                mapRef.onTap?.call(latlng);
              }
            }
          },
          onDone: () {},
          onError: (error) {
            debugPrint(error);
          },
        );
  }

  @override
  Future<void> showCurrentLocation() {
    return methodChannel.invokeMethod("showCurrentLocation", null);
  }

  @override
  Future<void> hideCurrentLocation() {
    return methodChannel.invokeMethod("hideCurrentLocation", null);
  }

  @override
  Future<double> getZoomLevel() async {
    final data = await methodChannel.invokeMethod("getZoomLevel");
    return Future.value(data);
  }

  @override
  void addMarker(LatLng latLng) {
    methodChannel.invokeMethod("addMarker", latLng.toMap());
  }

  @override
  Future<void> moveCameraToLatLong(LatLng latlng) {
    return methodChannel.invokeMethod("moveCameraToLatLong", latlng.toMap());
  }
}
