class Constants {
  static const String viewType = "in.haxon420.ola_maps_flutter_plugin.OlaMap";
}

enum EventTypes {
  onMapReady(name: "OnMapReady"),
  onMapMove(name: "OnMapMove"),
  onMapClick(name: "OnMapClick");

  final String name;

  const EventTypes({required this.name});
}
