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
  final bool setScrollGesturesEnabled;

  OlaMapConfigurations({
    required this.apiKey,
    this.showCurrentLocation = true,
    this.setCompassEnabled = true,
    this.setRotateGesturesEnabled = true,
    this.setTiltGesturesEnabled = true,
    required this.initialPosition,
    this.setZoomGesturesEnabled = true,
    this.setDoubleTapGesturesEnabled = true,
    this.setScrollGesturesEnabled = true,
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
      'setScrollGesturesEnabled': setScrollGesturesEnabled
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
      setScrollGesturesEnabled: map['setScrollGesturesEnabled'] as bool,
    );
  }

  String toJson() => json.encode(toMap());

  factory OlaMapConfigurations.fromJson(String source) =>
      OlaMapConfigurations.fromMap(json.decode(source) as Map<String, dynamic>);
}

class MarkerOptions {
  final String id;
  final LatLng position;
  final bool isIconClickable;
  final double iconRotation;
  final bool isAnimationEnabled;
  final bool isInfoWindowDismissOnClick;
  final String infoWindowSnippet;

  MarkerOptions({
    required this.id,
    required this.position,
    required this.isIconClickable,
    required this.iconRotation,
    required this.isAnimationEnabled,
    required this.isInfoWindowDismissOnClick,
    this.infoWindowSnippet = "",
  });

  Map<String, dynamic> toMap() {
    return <String, dynamic>{
      'id': id,
      'position': position.toMap(),
      'isIconClickable': isIconClickable,
      'iconRotation': iconRotation,
      'isAnimationEnabled': isAnimationEnabled,
      'isInfoWindowDismissOnClick': isInfoWindowDismissOnClick,
      'infoWindowSnippet': infoWindowSnippet,
    };
  }

  factory MarkerOptions.fromMap(Map<String, dynamic> map) {
    return MarkerOptions(
      id: map['id'] as String,
      position: LatLng.fromMap(map['position'] as Map<String, dynamic>),
      isIconClickable: map['isIconClickable'] as bool,
      iconRotation: map['iconRotation'] as double,
      isAnimationEnabled: map['isAnimationEnabled'] as bool,
      isInfoWindowDismissOnClick: map['isInfoWindowDismissOnClick'] as bool,
      infoWindowSnippet: map['infoWindowSnippet'] as String,
    );
  }

  String toJson() => json.encode(toMap());

  factory MarkerOptions.fromJson(String source) =>
      MarkerOptions.fromMap(json.decode(source) as Map<String, dynamic>);
}
