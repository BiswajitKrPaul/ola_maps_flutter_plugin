import 'package:ola_maps_flutter_plugin/ola_maps_flutter_plugin.dart';

typedef OlaMapCreatedCallback = void Function(OlaMapController controller);

class LatLng {
  double latitude;
  double longitude;
  LatLng({required this.latitude, required this.longitude});
}
