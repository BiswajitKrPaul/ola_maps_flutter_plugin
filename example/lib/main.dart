import 'dart:async';

import 'package:flutter/material.dart';
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

  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: OlaMap(
          onMapCreated: (controller) {
            _controller.complete(controller);
          },
          apiKey: "QXf3Ej8J6fpZAe58vxXl4uL5bNBL2CEVf5X34TPE",
          onTap: (data) async {
            print(data);
          },
        ),
      ),
    );
  }
}
