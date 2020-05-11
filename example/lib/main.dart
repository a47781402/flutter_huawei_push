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
  final FlutterHuaweiPush flutterHuaweiPush = new FlutterHuaweiPush();

  @override
  void initState() {
    super.initState();
    initPlatformState();
//    FlutterHuaweiPush().HMSAgentInIt();
  }

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
    getToken();
  }
  Future<void> getToken() async {
    String token;
    // Platform messages may fail, so we use a try/catch PlatformException.
    try {
      token = await flutterHuaweiPush.getToken;
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
          child:
              GestureDetector(onTap:()  {
                getToken();
              },child: Text('Running on: $_token\nRunning on: $_platformVersion\n')),
        ),
      ),
    );
  }
}
