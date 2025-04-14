import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:ola_maps_flutter_plugin/ola_maps_flutter_plugin.dart';
import 'package:ola_maps_flutter_plugin/src/constants.dart';
import 'package:ola_maps_flutter_plugin/src/models/method_call_functions.dart';

abstract class OlaMapController {
  void moveCameraToLatLong(CameraUpdate latlng);
  void showCurrentLocation();
  void hideCurrentLocation();
  Future<double> getZoomLevel();
  void addMarker(MarkerOptions latlng);
  Future<bool> removeMarker(String markerId);
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
              final receivedData = data as Map<dynamic, dynamic>;
              if (receivedData["event_type"] == EventTypes.onMapReady.name) {
                mapRef.onMapCreated(this);
                if (mapRef.showCurrentLocation) {
                  showCurrentLocation();
                } else {
                  hideCurrentLocation();
                }
              }
              if (receivedData["event_type"] == EventTypes.onMapClick.name) {
                var latlng = LatLng.fromMap(receivedData["data"] as Map);
                mapRef.onTap?.call(latlng);
              }

              if (receivedData["event_type"] == EventTypes.onMapMove.name) {
                mapRef.onMapMove?.call();
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
    return methodChannel.invokeMethod(
        MethodCallFunctions.showCurrentLocation, null);
  }

  @override
  Future<void> hideCurrentLocation() {
    return methodChannel.invokeMethod(
        MethodCallFunctions.hideCurrentLocation, null);
  }

  @override
  Future<double> getZoomLevel() async {
    final data =
        await methodChannel.invokeMethod(MethodCallFunctions.getZoomLevel);
    return Future.value(data);
  }

  @override
  void addMarker(MarkerOptions markerOptions) {
    methodChannel.invokeMethod(
        MethodCallFunctions.addMarker, markerOptions.toMap());
  }

  @override
  Future<void> moveCameraToLatLong(CameraUpdate cameraUpdate) {
    return methodChannel.invokeMethod(
      MethodCallFunctions.moveCameraToLatLong,
      cameraUpdate.toMap(),
    );
  }

  @override
  Future<bool> removeMarker(String markerId) async {
    final data = await methodChannel.invokeMethod(
      MethodCallFunctions.removeMarker,
      markerId,
    );
    return Future.value(data);
  }
}
