import 'package:flutter/foundation.dart';
import 'package:flutter/widgets.dart';
import 'package:ola_maps_flutter_plugin/src/constants.dart';
import 'package:ola_maps_flutter_plugin/src/ola_map_controller.dart';
import 'package:ola_maps_flutter_plugin/src/types.dart';

class OlaMap extends StatelessWidget {
  const OlaMap({
    super.key,
    required this.onMapCreated,
    required this.apiKey,
    this.onTap,
  });

  final OlaMapCreatedCallback onMapCreated;
  final String apiKey;
  final Function(LatLng latLng)? onTap;

  @override
  Widget build(BuildContext context) {
    switch (defaultTargetPlatform) {
      case TargetPlatform.android:
        return AndroidView(
          viewType: Constants.viewType,
          onPlatformViewCreated: _onPlatformViewCreated,
        );
      case TargetPlatform.iOS:
        return UiKitView(
          viewType: "",
          onPlatformViewCreated: _onPlatformViewCreated,
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

  void _onPlatformViewCreated(int id) =>
      onMapCreated(OlaMapController.init(id, apiKey, onTap));
}
