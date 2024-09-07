class Constants {
  static const String viewType = "com.goapptiv.ola_maps_flutter_plugin.OlaMap";
}

enum EventTypes {
  onMapReady(name: "OnMapReady"),
  onMapMove(name: "OnMapMove"),
  onMapClick(name: "OnMapClick");

  final String name;

  const EventTypes({required this.name});
}
