import 'dart:developer';

import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import '../model/baidu_config.dart';
import '../model/navi_lat_lng.dart';

class MethodChannelFlutterBaiduMapNavi {
  final methodChannel = const MethodChannel('flutter_baidu_map_navi');

  Future<void> init(BaiDuConfig config) async {
    await methodChannel
        .invokeMethod<String>('init', {'config': config.toJson()});
  }

  Future<dynamic> calculate(
      NaviLatLng startLatLng, NaviLatLng endLatLng) async {
    debugPrint('方法:navigation dart端参数:-> $startLatLng $endLatLng');
    var invoke = await methodChannel.invokeMethod("calculate",
        {'location': startLatLng.toJson(), 'endLatlng': endLatLng.toJson()});
    log("invoke:::${invoke.runtimeType}");
    return invoke;
  }

  Future<void> finishNavi() async {
    await methodChannel.invokeMethod("finishNavi");
  }
}
