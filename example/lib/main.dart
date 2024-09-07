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

  LatLng initialPos = LatLng(latitude: 17.4774271, longitude: 78.4169806);

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
        body: Column(
          children: [
            Expanded(
              child: OlaMap(
                onMapCreated: (controller) {
                  _controller.complete(controller);
                },
                apiKey: "YOUR API KEY",
                onTap: (data) async {
                  final controller = await _controller.future;
                  controller.addMarker(data);
                },
                initialPosition: initialPos,
                showCurrentLocation: false,
              ),
            ),
            ElevatedButton(
              onPressed: () async {
                var currentPos = await Geolocator.getLastKnownPosition();
                currentPos ??= await Geolocator.getCurrentPosition();

                final controller = await _controller.future;
                controller.moveCameraToLatLong(
                  LatLng(
                      latitude: currentPos.latitude,
                      longitude: currentPos.longitude),
                );
              },
              child: const Text("Locate Me"),
            )
          ],
        ),
      ),
    );
  }
}
