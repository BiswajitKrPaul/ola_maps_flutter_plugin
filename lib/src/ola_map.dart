import 'dart:async';

import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';
import 'package:flutter/widgets.dart';
import 'package:ola_maps_flutter_plugin/src/constants.dart';
import 'package:ola_maps_flutter_plugin/src/ola_map_controller.dart';
import 'package:ola_maps_flutter_plugin/src/types.dart';

class OlaMap extends StatefulWidget {
  const OlaMap({
    super.key,
    required this.onMapCreated,
    required this.apiKey,
    required this.initialPosition,
    this.onTap,
    this.onMapMove,
    this.showCurrentLocation = false,
    this.setCompassEnabled = false,
    this.setRotateGesturesEnabled = false,
    this.setTiltGesturesEnabled = false,
    this.setZoomGesturesEnabled = true,
    this.setDoubleTapGesturesEnabled = true,
  });

  final OlaMapCreatedCallback onMapCreated;
  final String apiKey;
  final Function(LatLng latLng)? onTap;
  final VoidCallback? onMapMove;
  final LatLng initialPosition;
  final bool showCurrentLocation;
  final bool setCompassEnabled;
  final bool setRotateGesturesEnabled;
  final bool setTiltGesturesEnabled;
  final bool setZoomGesturesEnabled;
  final bool setDoubleTapGesturesEnabled;

  @override
  State<OlaMap> createState() => _OlaMapState();
}

class _OlaMapState extends State<OlaMap> {
  final Completer<OlaMapControllerInternal> _controller = Completer();

  @override
  Widget build(BuildContext context) {
    switch (defaultTargetPlatform) {
      case TargetPlatform.android:
        return AndroidView(
          viewType: Constants.viewType,
          onPlatformViewCreated: (id) {
            final controller = OlaMapControllerInternal.init(
                id, widget.initialPosition, widget);
            _controller.complete(controller);
          },
          creationParams: OlaMapConfigurations(
            apiKey: widget.apiKey,
            initialPosition: widget.initialPosition,
            setCompassEnabled: widget.setCompassEnabled,
            setDoubleTapGesturesEnabled: widget.setDoubleTapGesturesEnabled,
            setTiltGesturesEnabled: widget.setTiltGesturesEnabled,
            setRotateGesturesEnabled: widget.setRotateGesturesEnabled,
            setZoomGesturesEnabled: widget.setZoomGesturesEnabled,
          ).toMap(),
          creationParamsCodec: const StandardMessageCodec(),
        );
      case TargetPlatform.iOS:
        return UiKitView(
          viewType: Constants.viewType,
          onPlatformViewCreated: (id) {
            final controller = OlaMapControllerInternal.init(
                id, widget.initialPosition, widget);
            _controller.complete(controller);
          },
          creationParams: OlaMapConfigurations(
            apiKey: widget.apiKey,
            initialPosition: widget.initialPosition,
            setCompassEnabled: widget.setCompassEnabled,
            setDoubleTapGesturesEnabled: widget.setDoubleTapGesturesEnabled,
            setTiltGesturesEnabled: widget.setTiltGesturesEnabled,
            setRotateGesturesEnabled: widget.setRotateGesturesEnabled,
            setZoomGesturesEnabled: widget.setZoomGesturesEnabled,
          ).toMap(),
          creationParamsCodec: const StandardMessageCodec(),
        );
      case TargetPlatform.fuchsia:
        throw UnimplementedError();
      case TargetPlatform.linux:
        throw UnimplementedError();
      case TargetPlatform.macOS:
        throw UnimplementedError();
      case TargetPlatform.windows:
        throw UnimplementedError();
    }
  }
}
