package com.example.flutter_huawei_push;

import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.PluginRegistry;

class MyServicePlugin implements EventChannel.StreamHandler {

    private static MyReceiver myPushReceiver;
//    public static MyReceiver getMyPushReceiver(){
//        return myPushReceiver;
//    }
    public static void registerWith(PluginRegistry.Registrar registrar) {
        final EventChannel channel = new EventChannel(registrar.messenger(), "flutter_huawei_receiver");
        channel.setStreamHandler(new MyServicePlugin());
    }

    private MyReceiver createMyPushReceiver(final EventChannel.EventSink event){
        myPushReceiver = new MyReceiver();

        return myPushReceiver;
    }

    @Override
    public void onListen(Object o, EventChannel.EventSink eventSink) {

    }

    @Override
    public void onCancel(Object o) {

    }
}
