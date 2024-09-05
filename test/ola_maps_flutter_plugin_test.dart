import 'package:flutter_test/flutter_test.dart';
import 'package:ola_maps_flutter_plugin/ola_maps_flutter_plugin_platform_interface.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockOlaMapsFlutterPluginPlatform
    with MockPlatformInterfaceMixin
    implements OlaMapsFlutterPluginPlatform {
  @override
  Future<String?> getPlatformVersion() => Future.value('42');
}

void main() {}
