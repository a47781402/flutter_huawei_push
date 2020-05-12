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

  Future<String> get getToken async {
    final String token = await _channel.invokeMethod('getToken');
    return token;
  }

  /// change
   Future<String> get Token async {
     print("00000000000000000:::::");
     final Map<dynamic, dynamic> result = await _channel.invokeMethod('Token');
     print("11111111111111111:::::$result");
     String token = result["token"];
     print("22222222222222:::::$token");
     return token;
//     _eventChannel.receiveBroadcastStream().listen((data){
//        token = data;
//        print("00000000000000000:::::$token");
//        return token;
//    });


  }

//  Future<String> get getToken async {
//    String token ;
//    _channel.invokeMethod('getToken');
//    _channel.setMethodCallHandler((handler){
//      switch(handler.method){
//        case "token":
//          token = handler.arguments.toString();
//          print("111111111111111:::::$token");
//      }
//    });
//    print("2222222222222222222222:::::$token");
////     _eventChannel.receiveBroadcastStream().listen((data){
////        token = data;
////    });
//    return token;
//  }

}
