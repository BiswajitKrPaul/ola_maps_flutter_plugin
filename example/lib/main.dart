import 'dart:async';

import 'package:flutter/material.dart';
import 'package:geolocator/geolocator.dart';
import 'package:ola_maps_flutter_plugin/ola_maps_flutter_plugin.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  final Completer<OlaMapController> _controller = Completer<OlaMapController>();

  LatLng initialPos =
      LatLng(latitude: 37.42796133580664, longitude: -122.085749655962);

  @override
  void initState() {
    super.initState();
    setInitialPos();
  }

  void setInitialPos() async {
    await Geolocator.requestPermission();
    final postion = await Geolocator.getCurrentPosition();
    setState(() {
      initialPos =
          LatLng(latitude: postion.latitude, longitude: postion.longitude);
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        body: OlaMap(
          onMapCreated: (controller) {
            _controller.complete(controller);
          },
          apiKey: "QXf3Ej8J6fpZAe58vxXl4uL5bNBL2CEVf5X34TPE",
          onTap: (data) async {
            final controller = await _controller.future;
            final data = await controller.getZoomLevel();
            print(data);
          },
          initialPosition: initialPos,
        ),
      ),
    );
  }
}
