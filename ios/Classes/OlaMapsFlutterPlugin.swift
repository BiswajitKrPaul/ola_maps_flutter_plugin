import Flutter
import UIKit
import MapLibre

public class OlaMapsFlutterPlugin: NSObject, FlutterPlugin {
  public static func register(with registrar: FlutterPluginRegistrar) {
      let instance = OlaMapPlatformViewFactory(messenger: registrar.messenger())
    registrar.register(instance, withId: "com.goapptiv.ola_maps_flutter_plugin.OlaMap")
  }
}
