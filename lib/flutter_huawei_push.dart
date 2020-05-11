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



  /// change
   Future<String> get getToken async {
      String token ;
     _channel.invokeMethod('getToken');
     _eventChannel.receiveBroadcastStream().listen((data){
        token = data;
    });
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
