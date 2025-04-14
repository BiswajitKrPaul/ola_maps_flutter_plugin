// ignore_for_file: public_member_api_docs, sort_constructors_first
import 'dart:convert';

import 'package:ola_maps_flutter_plugin/src/types.dart';

class CameraUpdate {
  const CameraUpdate({
    required this.target,
    this.zoomLevel = 15.0,
    this.animationDurationInMs = 200,
  });

  final LatLng target;
  final double zoomLevel;
  final int animationDurationInMs;

  Map<String, dynamic> toMap() {
    return <String, dynamic>{
      'target': target.toMap(),
      'zoomLevel': zoomLevel,
      'animationDurationInMs': animationDurationInMs,
    };
  }

  factory CameraUpdate.fromMap(Map map) {
    return CameraUpdate(
      target: LatLng.fromMap(map['target'] as Map<String, dynamic>),
      zoomLevel: map['zoomLevel'] as double,
      animationDurationInMs: map['animationDurationInMs'] as int,
    );
  }

  String toJson() => json.encode(toMap());

  factory CameraUpdate.fromJson(String source) =>
      CameraUpdate.fromMap(json.decode(source) as Map<String, dynamic>);

  CameraUpdate copyWith({
    LatLng? target,
    double? zoomLevel,
    int? animationDurationInMs,
  }) {
    return CameraUpdate(
      target: target ?? this.target,
      zoomLevel: zoomLevel ?? this.zoomLevel,
      animationDurationInMs:
          animationDurationInMs ?? this.animationDurationInMs,
    );
  }
}
