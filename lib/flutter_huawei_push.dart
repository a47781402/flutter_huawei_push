import 'dart:async';

import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

class FlutterHuaweiPush {
  final String flutter_log = "| FlutterHuaweiPush | Flutter | ";

  factory FlutterHuaweiPush() => _instance;

//  static const MethodChannel _channel =
//      const MethodChannel('flutter_huawei_push');
  final MethodChannel _channel;
  final EventChannel _eventChannel;

  @visibleForTesting
  FlutterHuaweiPush.private(MethodChannel channel,EventChannel eventChannel) : _channel = channel, _eventChannel = eventChannel;

  static final FlutterHuaweiPush _instance =
      new FlutterHuaweiPush.private(const MethodChannel('flutter_huawei_push'),const EventChannel('flutter_huawei_push_event'));

  Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  /// 请求token
   Future<void>  reqToken() async {
    print("使用了Token");
     _channel.invokeMethod('Token');

//     final Map<dynamic, dynamic> result = await _channel.invokeMethod('Token');
//     return token;
  }
  ///获取token
  Future<String> get getToken async {
    final String token = await _channel.invokeMethod('getToken');
    print(token);
    return token;
  }


}
