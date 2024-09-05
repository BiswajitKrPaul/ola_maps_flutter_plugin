import 'package:flutter_test/flutter_test.dart';
import 'package:ola_maps_flutter_plugin/ola_maps_flutter_plugin.dart';
import 'package:ola_maps_flutter_plugin/ola_maps_flutter_plugin_platform_interface.dart';
import 'package:ola_maps_flutter_plugin/ola_maps_flutter_plugin_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockOlaMapsFlutterPluginPlatform
    with MockPlatformInterfaceMixin
    implements OlaMapsFlutterPluginPlatform {

  @override
  Future<String?> getPlatformVersion() => Future.value('42');
}

void main() {
  final OlaMapsFlutterPluginPlatform initialPlatform = OlaMapsFlutterPluginPlatform.instance;

  test('$MethodChannelOlaMapsFlutterPlugin is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelOlaMapsFlutterPlugin>());
  });

  test('getPlatformVersion', () async {
    OlaMapsFlutterPlugin olaMapsFlutterPlugin = OlaMapsFlutterPlugin();
    MockOlaMapsFlutterPluginPlatform fakePlatform = MockOlaMapsFlutterPluginPlatform();
    OlaMapsFlutterPluginPlatform.instance = fakePlatform;

    expect(await olaMapsFlutterPlugin.getPlatformVersion(), '42');
  });
}
