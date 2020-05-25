import 'dart:async';

import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

typedef void EventHandler(Object event);

class FlutterHuaweiPush {
  final String flutter_log = "| FlutterHuaweiPush | Flutter | ";

  factory FlutterHuaweiPush() => _instance;

  final MethodChannel _channel;
  final EventChannel _eventChannel;

  static EventChannel _channelReciever = const EventChannel('flutter_huawei_receiver');

  @visibleForTesting
  FlutterHuaweiPush.private(MethodChannel channel,EventChannel eventChannel) : _channel = channel, _eventChannel = eventChannel;

  static final FlutterHuaweiPush _instance =
      new FlutterHuaweiPush.private(const MethodChannel('flutter_huawei_push'),const EventChannel('flutter_huawei_push_event'));

  Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  /*
   * 添加推送回调监听（接收自定义透传消息回调、接收通知消息回调、接收点击通知消息回调、接收别名或标签操作回调）
   */
  static addPushReceiver(EventHandler onEvent, EventHandler onError) {
    _channelReciever.receiveBroadcastStream().listen(onEvent, onError: onError);
  }

  /// 请求token
   Future<void>  reqToken() async {
    print("使用了Token");
     _channel.invokeMethod('Token');
  }
  ///获取token
  Future<String> get getToken async {
    final String token = await _channel.invokeMethod('getToken');
    print(token);
    return token;
  }

  /// 注册广播
  Future<void>  reqReceiver() async {
    print("使用了Token");
    _channel.invokeMethod('Receiver');
  }
  ///获取广播信息
  Future<String> get getHuaWeiMsg async {
    final String intentMsg = await _channel.invokeMethod('getIntentMsg');
    print(intentMsg);
    return intentMsg;
  }
}
