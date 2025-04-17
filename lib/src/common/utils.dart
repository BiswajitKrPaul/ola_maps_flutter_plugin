import 'dart:ui';

class Utils {
  static String getHexColorCode(Color c) {
    // final a = (c.a * 255).round().toRadixString(16).padLeft(2, '0');
    final r = (c.r * 255).round().toRadixString(16).padLeft(2, '0');
    final g = (c.g * 255).round().toRadixString(16).padLeft(2, '0');
    final b = (c.b * 255).round().toRadixString(16).padLeft(2, '0');
    return "#$r$g$b";
  }
}
