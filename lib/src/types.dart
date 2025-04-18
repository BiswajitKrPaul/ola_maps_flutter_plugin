// ignore_for_file: public_member_api_docs, sort_constructors_first
import 'dart:convert';
import 'dart:ui';

import 'package:ola_maps_flutter_plugin/ola_maps_flutter_plugin.dart';
import 'package:ola_maps_flutter_plugin/src/common/utils.dart';

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

class PolylineOptions {
  final String id;
  final List<LatLng> points;
  final Color color;
  final double width;
  final String? lineType;

  PolylineOptions({
    required this.id,
    required this.points,
    required this.color,
    required this.width,
    this.lineType,
  });

  Map<String, dynamic> toMap() {
    return <String, dynamic>{
      'id': id,
      'points': points.map((x) => x.toMap()).toList(),
      'color': Utils.getHexColorCode(color),
      'width': width,
      'lineType': lineType,
    };
  }

  factory PolylineOptions.fromMap(Map<String, dynamic> map) {
    return PolylineOptions(
      id: map['id'] as String,
      points: List<LatLng>.from(
        (map['points'] as List<int>).map<LatLng>(
          (x) => LatLng.fromMap(x as Map<dynamic, dynamic>),
        ),
      ),
      color: map['color'],
      width: map['width'],
      lineType: map['lineType'] != null ? map['lineType'] as String : null,
    );
  }

  String toJson() => json.encode(toMap());

  factory PolylineOptions.fromJson(String source) =>
      PolylineOptions.fromMap(json.decode(source) as Map<String, dynamic>);
}

class MapBorderOptions {
  final String borderColor;
  final double borderWidth;
  final String? borderLineType;
  final List<double>? borderLineDashArray;

  MapBorderOptions({
    required this.borderColor,
    required this.borderWidth,
    this.borderLineType,
    this.borderLineDashArray,
  });
  Map<String, dynamic> toMap() {
    return <String, dynamic>{
      'borderColor': borderColor,
      'borderWidth': borderWidth,
      'borderLineType': borderLineType,
      'borderLineDashArray': borderLineDashArray,
    };
  }

  factory MapBorderOptions.fromMap(Map<String, dynamic> map) {
    return MapBorderOptions(
      borderColor: map['borderColor'] as String,
      borderWidth: map['borderWidth'] as double,
      borderLineType: map['borderLineType'] != null
          ? map['borderLineType'] as String
          : null,
      borderLineDashArray: map['borderLineDashArray'] != null
          ? List<double>.from((map['borderLineDashArray'] as List<double>))
          : null,
    );
  }

  String toJson() => json.encode(toMap());

  factory MapBorderOptions.fromJson(String source) =>
      MapBorderOptions.fromMap(json.decode(source) as Map<String, dynamic>);
}

class MapCircleOptions {
  final Color colorHexCode;
  final LatLng latLng;
  final double radius;
  final double circleOpacity;
  final double circleBlur;
  final int circleId;
  final MapBorderOptions? borderOptions;

  MapCircleOptions({
    required this.colorHexCode,
    required this.latLng,
    required this.radius,
    required this.circleOpacity,
    required this.circleBlur,
    required this.circleId,
    this.borderOptions,
  });

  Map<String, dynamic> toMap() {
    return {
      'colorHexCode': Utils.getHexColorCode(colorHexCode),
      'latLng': latLng.toMap(),
      'radius': radius,
      'circleOpacity': circleOpacity,
      'circleBlur': circleBlur,
      'circleId': circleId,
      'borderOptions': borderOptions?.toMap(),
    };
  }

  factory MapCircleOptions.fromMap(Map<dynamic, dynamic> map) {
    return MapCircleOptions(
      colorHexCode: map['colorHexCode'],
      latLng: LatLng.fromMap(map['latLng'] as Map<dynamic, dynamic>),
      radius: map['radius'] as double,
      circleOpacity: map['circleOpacity'] as double,
      circleBlur: map['circleBlur'] as double,
      circleId: map['circleId'] as int,
      borderOptions: map['borderOptions'] != null
          ? MapBorderOptions.fromMap(
              map['borderOptions'] as Map<String, dynamic>)
          : null,
    );
  }
}
