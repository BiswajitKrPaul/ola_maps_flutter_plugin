import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'ola_maps_flutter_plugin_method_channel.dart';

abstract class OlaMapsFlutterPluginPlatform extends PlatformInterface {
  /// Constructs a OlaMapsFlutterPluginPlatform.
  OlaMapsFlutterPluginPlatform() : super(token: _token);

  static final Object _token = Object();

  static OlaMapsFlutterPluginPlatform _instance = MethodChannelOlaMapsFlutterPlugin();

  /// The default instance of [OlaMapsFlutterPluginPlatform] to use.
  ///
  /// Defaults to [MethodChannelOlaMapsFlutterPlugin].
  static OlaMapsFlutterPluginPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [OlaMapsFlutterPluginPlatform] when
  /// they register themselves.
  static set instance(OlaMapsFlutterPluginPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }
}
