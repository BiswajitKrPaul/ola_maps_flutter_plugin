// ignore_for_file: public_member_api_docs, sort_constructors_first
import 'dart:convert';

import 'package:ola_maps_flutter_plugin/ola_maps_flutter_plugin.dart';

typedef OlaMapCreatedCallback = void Function(OlaMapController controller);

class LatLng {
  final double latitude;
  final double longitude;
  LatLng({required this.latitude, required this.longitude});

  Map<String, dynamic> toMap() {
    return <String, dynamic>{
      'latitude': latitude,
      'longitude': longitude,
    };
  }

  factory LatLng.fromMap(Map map) {
    return LatLng(
      latitude: map['latitude'] as double,
      longitude: map['longitude'] as double,
    );
  }

  String toJson() => json.encode(toMap());

  factory LatLng.fromJson(String source) =>
      LatLng.fromMap(json.decode(source) as Map<String, dynamic>);
}

class OlaMapConfigurations {
  final String apiKey;
  final bool showCurrentLocation;
  final bool setCompassEnabled;
  final bool setRotateGesturesEnabled;
  final bool setTiltGesturesEnabled;
  final LatLng initialPosition;
  final bool setZoomGesturesEnabled;
  final bool setDoubleTapGesturesEnabled;

  OlaMapConfigurations({
    required this.apiKey,
    this.showCurrentLocation = false,
    this.setCompassEnabled = false,
    this.setRotateGesturesEnabled = false,
    this.setTiltGesturesEnabled = false,
    required this.initialPosition,
    this.setZoomGesturesEnabled = true,
    this.setDoubleTapGesturesEnabled = false,
  });

  Map<String, dynamic> toMap() {
    return <String, dynamic>{
      'apiKey': apiKey,
      'showCurrentLocation': showCurrentLocation,
      'setCompassEnabled': setCompassEnabled,
      'setRotateGesturesEnabled': setRotateGesturesEnabled,
      'setTiltGesturesEnabled': setTiltGesturesEnabled,
      'initialPosition': initialPosition.toMap(),
      'setZoomGesturesEnabled': setZoomGesturesEnabled,
      'setDoubleTapGesturesEnabled': setDoubleTapGesturesEnabled,
    };
  }

  factory OlaMapConfigurations.fromMap(Map<String, dynamic> map) {
    return OlaMapConfigurations(
      apiKey: map['apiKey'] as String,
      showCurrentLocation: map['showCurrentLocation'] as bool,
      setCompassEnabled: map['setCompassEnabled'] as bool,
      setRotateGesturesEnabled: map['setRotateGesturesEnabled'] as bool,
      setTiltGesturesEnabled: map['setTiltGesturesEnabled'] as bool,
      initialPosition:
          LatLng.fromMap(map['initialPosition'] as Map<String, dynamic>),
      setZoomGesturesEnabled: map['setZoomGesturesEnabled'] as bool,
      setDoubleTapGesturesEnabled: map['setDoubleTapGesturesEnabled'] as bool,
    );
  }

  String toJson() => json.encode(toMap());

  factory OlaMapConfigurations.fromJson(String source) =>
      OlaMapConfigurations.fromMap(json.decode(source) as Map<String, dynamic>);
}
