import 'dart:async';

import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

class FlutterHuaweiPush {
  final String flutter_log = "| FlutterHuaweiPush | Flutter | ";

  factory FlutterHuaweiPush() => _instance;

//  static const MethodChannel _channel =
//      const MethodChannel('flutter_huawei_push');
  final MethodChannel _channel;

  @visibleForTesting
  FlutterHuaweiPush.private(MethodChannel channel) : _channel = channel;

  static final FlutterHuaweiPush _instance =
      new FlutterHuaweiPush.private(const MethodChannel('flutter_huawei_push'));

  Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

   Future<String> get getToken async {
    final String token = await _channel.invokeMethod('getToken');
    return token;
  }

//  Future<Map<dynamic, dynamic>> getToken() async {
////    print(flutter_log + "getToken:");
//    final Map<dynamic, dynamic> result =
//        await _channel.invokeMethod('getToken');
//    return result;
//  }

//  Future HMSAgentInIt() async {
//    print(flutter_log + "HMSAgentInIt:");
//
//    await _channel.invokeMethod('HMSAgentInIt');
//  }

}
