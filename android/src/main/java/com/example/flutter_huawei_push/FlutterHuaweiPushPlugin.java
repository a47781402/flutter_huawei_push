package com.example.flutter_huawei_push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.huawei.android.hms.agent.HMSAgent;
import com.huawei.android.hms.agent.push.handler.GetTokenHandler;

import java.util.HashMap;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry;
import io.flutter.plugin.common.PluginRegistry.Registrar;
import io.flutter.view.FlutterNativeView;

/**
 * FlutterHuaweiPushPlugin
 */
public class FlutterHuaweiPushPlugin implements FlutterPlugin, MethodCallHandler {


    private static String TAG = "| FlutterHuaweiPushPlugin | Flutter | Android | ";
    // token标记
    private static String KEY_TOKEN = "action.updateToken";
    private EventChannel.EventSink  plugineventSink;
    public  void registerWith(Registrar registrar) {

        final MethodChannel channel = new MethodChannel(registrar.messenger(), "flutter_huawei_push");
        channel.setMethodCallHandler(new FlutterHuaweiPushPlugin());
        final EventChannel eventChannel = new EventChannel(registrar.messenger(), "flutter_huawei_push_event");
        eventChannel.setStreamHandler(new EventChannel.StreamHandler() {

            @Override
            public void onListen(Object o, EventChannel.EventSink eventSink) {
                plugineventSink = eventSink;

            }

            @Override
            public void onCancel(Object o) {
                plugineventSink = null;
            }
        });
    }


    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
        final MethodChannel channel = new MethodChannel(flutterPluginBinding.getFlutterEngine().getDartExecutor(), "flutter_huawei_push");
        channel.setMethodCallHandler(new FlutterHuaweiPushPlugin());

    }

    private String token;
    Handler handler = new Handler();

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
        if (call.method.equals("getPlatformVersion")) {
            result.success("Android " + android.os.Build.VERSION.RELEASE);
        } else if (call.method.equals("getToken")) {
//      result.success("getToken " + android.os.Build.VERSION.RELEASE);
            getToken(call, result);
        }
//    else if(call.method.equals("HMSAgentInIt")){
//        HMSAgentInIt(call, result);
//
//    }
        else {
            result.notImplemented();
        }
    }

    public void getToken(MethodCall call, final Result result) {
        Log.d(TAG, "getToken： ");
        HMSAgent.Push.getToken(new GetTokenHandler() {
            @Override
            public void onResult(final int rtnCode) {
                Log.d("getToken", "get token: end code=" + rtnCode);
                String code = "get token: end code=" + rtnCode;
//        handler.post(new Runnable() {
//          @Override
//          public void run() {
//           return token;
//          }
//        });
//        code = "get token: end code=" + rtnCode;

            }
        });
        MyPushService.registerPushCallback(new MyPushService.IPushCallback() {
            @Override
            public void onReceive(Intent intent) {
                // 判断token是否有值
                if (intent.hasExtra(KEY_TOKEN)) {
                    // 获取token
                    token = intent.getStringExtra(KEY_TOKEN);
                    Log.d("getToken", token);
                    // 成功返回token
                    if (plugineventSink!=null){
                        plugineventSink.success(token);
                    }
                    result.success(token);
                } else {
                    // 失败返回错误信息
                    result.error("-1", "未拿到token", "未拿到token");
                }
            }

        });
//    return token;
//    result.success(code);
    }

//  public void HMSAgentInIt(MethodCall call, Result result) {
//    Log.d(TAG,"HMSAgentInIt： ");
//    HMSAgent.init(registrar.activity());
//
//  }



    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    }
}
