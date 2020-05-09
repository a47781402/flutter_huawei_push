import 'dart:async';

import 'package:flutter/services.dart';

class FlutterHuaweiPush {
  final String flutter_log = "| JPUSH | Flutter | ";
  static const MethodChannel _channel =
      const MethodChannel('flutter_huawei_push');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static Future<Map<dynamic, dynamic>> getToken() async {
//    print(flutter_log + "getToken:");
    final Map<dynamic, dynamic> result =
    await _channel.invokeMethod('getToken');
    return result;
  }

}
