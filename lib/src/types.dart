// ignore_for_file: public_member_api_docs, sort_constructors_first
import 'dart:convert';

import 'package:ola_maps_flutter_plugin/ola_maps_flutter_plugin.dart';

typedef OlaMapCreatedCallback = void Function(OlaMapController controller);

class LatLng {
  double latitude;
  double longitude;
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
