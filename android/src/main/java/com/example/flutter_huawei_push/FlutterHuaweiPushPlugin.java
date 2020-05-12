package com.example.flutter_huawei_push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
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
    private String EVENT_TOKEN = "action.event.token";
    private static String EVENT_ACTION = "flutter_huawei_push_event";
    private EventChannel.EventSink plugineventSink;
    private BroadcastReceiver mStateChangeReceiver;
    private FlutterPluginBinding flutterPluginBinding;
    private MethodChannel channel;
    private EventChannel eventChannel;
    private String token ;
    private Context context;


    public static void registerWith(Registrar registrar) {

        final MethodChannel channel = new MethodChannel(registrar.messenger(), "flutter_huawei_push");
        channel.setMethodCallHandler(new FlutterHuaweiPushPlugin());
    }


    @Override
    public void onAttachedToEngine(@NonNull final FlutterPluginBinding flutterPluginBinding) {
        final MethodChannel channel = new MethodChannel(flutterPluginBinding.getFlutterEngine().getDartExecutor(), "flutter_huawei_push");
        final EventChannel eventChannel = new EventChannel(flutterPluginBinding.getBinaryMessenger(), EVENT_ACTION);
        this.flutterPluginBinding = flutterPluginBinding;
        this.context = flutterPluginBinding.getApplicationContext();
        this.channel = channel;
        this.eventChannel = eventChannel;
        channel.setMethodCallHandler(new FlutterHuaweiPushPlugin());
        eventChannel.setStreamHandler(new EventChannel.StreamHandler() {
            private BroadcastReceiver chargingStateChangeReceiver;

            @Override
            public void onListen(Object arguments, EventChannel.EventSink events) {
                chargingStateChangeReceiver = createChargingStateChangeReceiver(events);
                flutterPluginBinding.getApplicationContext().registerReceiver(
                        chargingStateChangeReceiver, new IntentFilter(EVENT_TOKEN));
            }

            @Override
            public void onCancel(Object arguments) {
                flutterPluginBinding.getApplicationContext().unregisterReceiver(chargingStateChangeReceiver);
                chargingStateChangeReceiver = null;
            }
        });

    }

    private BroadcastReceiver createChargingStateChangeReceiver(final EventChannel.EventSink events) {


        return new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("aaaaaaaaaaaaaa", "1111111111111111111");
                String status = intent.getStringExtra("token");
                if (token != null) {
                    events.success(token);
                }

//                if (status == BatteryManager.BATTERY_STATUS_UNKNOWN) {
//                    events.error("UNAVAILABLE", "Charging status unavailable", null);
//                } else {
//                    boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
//                            status == BatteryManager.BATTERY_STATUS_FULL;
//                    events.success(isCharging ? "charging" : "discharging");
//                }
            }
        };
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    token = (String) msg.obj;
            }
        }
    };

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
        if (call.method.equals("getPlatformVersion")) {
            result.success("Android " + android.os.Build.VERSION.RELEASE);
        } else if (call.method.equals("getToken")) {
            Token(call, result);
            result.success(token);
        } else if (call.method.equals("Token")) {
            Token(call, result);
        } else {
            result.notImplemented();
        }
    }

    //获取token
    public void Token(MethodCall call, final Result result) {
        final HashMap<String, Object> map = call.arguments();
        Log.d(TAG, "getToken： ");

        HMSAgent.Push.getToken(new GetTokenHandler() {
            @Override
            public void onResult(final int rtnCode) {
                Log.d("getToken", "get token: end code=" + rtnCode);
                String code = "get token: end code=" + rtnCode;

                MyPushService.registerPushCallback(new MyPushService.IPushCallback() {
                    @Override
                    public void onReceive(Intent intent) {
                         String token_value= intent.getStringExtra(KEY_TOKEN);

                        if (token_value != "" && token_value != null) {
                            Intent it = new Intent(EVENT_TOKEN);
                            it.putExtra("token",token_value);
                            flutterPluginBinding.getApplicationContext().sendBroadcast(it);
//                            token = token_value;
////                            it.putExtra("token", token);
//                            Message message = new Message();
//                            message.what = 1;
//                            message.obj = token;
//                            handler.sendMessage(message);
                        }

//                        map.put("token",token);
//                        result.success(map);
                    }
                });

            }
        });

//        MyPushService.registerPushCallback(new MyPushService.IPushCallback() {
//            @Override
//            public void onReceive(Intent intent) {
//                // 判断token是否有值
//                if (intent.hasExtra(KEY_TOKEN)) {
//                    // 获取token
//                    token = intent.getStringExtra(KEY_TOKEN);
////                    channel.invokeMethod("token",token);
//
//                    Log.d("aaaaaaaaaaaaaa", "1111111111111111111");
////                    // 成功返回token
//                    if (plugineventSink!=null){
//
//                        plugineventSink.success(token);Log.d("aaaaaaaaaaaaaa", "222222222222222");
//                    }
//                    Log.d("aaaaaaaaaaaaaa", "3333333333333333333");
////                    result.success(token);
//                } else {
////                    // 失败返回错误信息
////                    result.error("-1", "未拿到token", "未拿到token");
//                }
//            }
//
//        });

    }


//    private BroadcastReceiver createEventListener(final EventChannel.EventSink sink){
//        return new BroadcastReceiver(){
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                if(TextUtils.equals(intent.getAction(),EVENT_ACTION)){
//                    sink.success(intent.getIntExtra(EVENT_TOKEN,-1));
//                }
//            }
//        };
//    }


    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    }

//    @Override
//    public void onListen(Object arguments, EventChannel.EventSink events) {
//        mStateChangeReceiver = createEventListener(events);
//        flutterPluginBinding.getApplicationContext().registerReceiver(mStateChangeReceiver, new IntentFilter(EVENT_ACTION));
//
//        plugineventSink = events;
//
//    }
//
//    @Override
//    public void onCancel(Object arguments) {
//        plugineventSink = null;
//
//    }
}
