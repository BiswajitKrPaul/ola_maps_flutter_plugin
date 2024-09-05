import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'ola_maps_flutter_plugin_platform_interface.dart';

/// An implementation of [OlaMapsFlutterPluginPlatform] that uses method channels.
class MethodChannelOlaMapsFlutterPlugin extends OlaMapsFlutterPluginPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('ola_maps_flutter_plugin');

  @override
  Future<String?> getPlatformVersion() async {
    final version =
        await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }
}
