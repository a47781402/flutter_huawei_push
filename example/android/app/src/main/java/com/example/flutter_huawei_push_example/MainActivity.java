package com.example.flutter_huawei_push_example;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.flutter_huawei_push.FlutterHuaweiPushPlugin;
import com.huawei.android.hms.agent.HMSAgent;
import com.huawei.android.hms.agent.common.handler.ConnectHandler;
import com.huawei.android.hms.agent.push.handler.GetTokenHandler;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugins.GeneratedPluginRegistrant;

public class MainActivity extends FlutterActivity {

    private static final String CHANNEL = "huawei.push.channel1";
    private static final String TAG = "MainActivity";
    private FlutterHuaweiPushPlugin flutterHuaweiPushPlugin;


//    @Override
//    protected void onResume() {
//        super.onResume();
//        flutterHuaweiPushPlugin.getReceiver();
//    }

    @Override
    public void configureFlutterEngine(@NonNull FlutterEngine flutterEngine) {
        GeneratedPluginRegistrant.registerWith(flutterEngine);
        HMSAgent.init(this);
//        getToken();
        flutterHuaweiPushPlugin = new FlutterHuaweiPushPlugin().getInstance();
        flutterHuaweiPushPlugin.Token();
        flutterHuaweiPushPlugin.getReceiver();



//        MyPushService.registerPushCallback(new MyPushService.IPushCallback() {
//            @Override
//            public void onReceive(Intent intent) {
//                token = intent.getStringExtra("action.updateToken");
//            }
//        });


//        new MethodChannel(flutterEngine.getDartExecutor(), CHANNEL).setMethodCallHandler(
//                new MethodChannel.MethodCallHandler() {
//                    @Override
//                    public void onMethodCall(MethodCall call, MethodChannel.Result result) {
//                        if (call.method.equals("getToken")) {
//
//                            String token = flutterHuaweiPushPlugin.getToken();
//                            result.success(token);
//                        } else {
//                            result.notImplemented();
//                        }
//                    }
//                });
//        HMSAgent.connect(this, new ConnectHandler() {
//            @Override
//            public void onConnect(int rst) {
//                Log.e("MainActivity","connect result" + rst);
//            }
//        });

//        getToken();


//        Log.d("MainActivity", "getToken： ");
//        HMSAgent.Push.getToken(new GetTokenHandler() {
//            @Override
//            public void onResult(int rtnCode) {
//                Log.d("getToken", "get token: end code=" + rtnCode);
//                String code = rtnCode + "";
//            }
//        });
    }


}
