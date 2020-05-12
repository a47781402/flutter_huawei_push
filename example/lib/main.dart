import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:flutter_huawei_push/flutter_huawei_push.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';
  String _token = 'Unknown';
  static const eventChannel = const EventChannel('flutter_huawei_push_event');
  StreamSubscription _streamSubscription;

  final FlutterHuaweiPush flutterHuaweiPush = new FlutterHuaweiPush();

  @override
  void initState() {
    super.initState();
    initPlatformState();
//    _getEventResult();
    eventChannel.receiveBroadcastStream().listen(_onEvent, onError: _onError);
  }
  void _onEvent(Object event) {
    setState(() {
      _token =event;
//      "Battery status: ${event == 'charging' ? '' : 'dis'}charging.";
    });
  }

  void _onError(Object error) {
    setState(() {
      PlatformException exception = error;
      _token = exception?.message ?? 'Battery status: unknown.$error';
    });
  }

//  _getEventResult() async {
//    try {
//      _streamSubscription =
//          eventChannel.receiveBroadcastStream().listen((data) {
//        setState(() {
//          print("3333333333333333:::::$_token");
//          _token = data;
//          print("444444444444444:::::$_token");
//        });
//      },onError: (e){
//            print('process error: $e');
//          });
//    } on PlatformException catch (e) {
//      setState(() {
//        _token = "event get data err: '${e.message}'.";
//      });
//    }
//  }
//
//  @override
//  void dispose() {
//    super.dispose();
//    if (_streamSubscription != null) {
//      _streamSubscription.cancel();
//    }
//  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    String platformVersion;
    // Platform messages may fail, so we use a try/catch PlatformException.
    try {
      platformVersion = await flutterHuaweiPush.platformVersion;
    } on PlatformException {
      platformVersion = 'Failed to get platform version.';
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _platformVersion = platformVersion;
    });
//    getToken();
  }

  Future<void> getToken() async {
    String token;
    // Platform messages may fail, so we use a try/catch PlatformException.
    try {
      print("3333333333333:::::$token");
      token = await flutterHuaweiPush.Token;
//      token = await flutterHuaweiPush.getToken;
      print("4444444444444:::::$token");
    } on PlatformException {
      token = 'Failed to get platform version.';
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _token = token;
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: GestureDetector(
              onTap: () {
                getToken();
              },
              child:
                  Text('Running on: $_token\nRunning on: $_platformVersion\n')),
        ),
      ),
    );
  }
}
