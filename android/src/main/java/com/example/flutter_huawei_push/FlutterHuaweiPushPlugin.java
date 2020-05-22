package com.example.flutter_huawei_push;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.huawei.android.hms.agent.HMSAgent;
import com.huawei.android.hms.agent.push.handler.GetTokenHandler;

import java.lang.ref.WeakReference;
import java.util.HashMap;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
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
public class FlutterHuaweiPushPlugin implements FlutterPlugin, MethodCallHandler, ActivityAware {


    private static String TAG = "| FlutterHuaweiPushPlugin | Flutter | Android | ";
    // token标记
    private static String KEY_TOKEN = "action.updateToken";
    private static String Method_Channel_ACTION = "flutter_huawei_push";

    // 获取环境
    private FlutterPluginBinding flutterPluginBinding;
    private MethodChannel mMethodChannel;
    private WeakReference<Activity> mActivity;
    private Application mApplication;

    // 单例
    public static FlutterHuaweiPushPlugin instance;

    // 华为推送token
    private String token ;
    // 获取点击事件广播
    private MyReceiver myReceiver;
    private String receiverIntent;

    // 构造方法
    public FlutterHuaweiPushPlugin() { }

    // 单例模式
    public static FlutterHuaweiPushPlugin getInstance(){
        if(instance == null){
            instance = new FlutterHuaweiPushPlugin();
        }
        return instance;
    }

    // 旧版本的注册
    public static void registerWith(Registrar registrar) {

        final MethodChannel channel = new MethodChannel(registrar.messenger(), Method_Channel_ACTION);
        channel.setMethodCallHandler(new FlutterHuaweiPushPlugin().initPlugin(channel,registrar));
    }

    public FlutterHuaweiPushPlugin initPlugin(MethodChannel methodChannel, Registrar registrar) {
        mMethodChannel = methodChannel;
        mApplication = (Application) registrar.context().getApplicationContext();
        mActivity = new WeakReference<>(registrar.activity());
        return this;
    }

    // 注册
    @Override
    public void onAttachedToEngine(@NonNull final FlutterPluginBinding flutterPluginBinding) {
        mMethodChannel = new MethodChannel(flutterPluginBinding.getFlutterEngine().getDartExecutor(), Method_Channel_ACTION);
        mApplication = (Application) flutterPluginBinding.getApplicationContext();
        mMethodChannel.setMethodCallHandler(getInstance());
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        mMethodChannel.setMethodCallHandler(null);
        mMethodChannel = null;
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    token = (String) msg.obj;
                    Log.d(TAG, "handleMessage: "+token);
                    break;
                case 2:
                    receiverIntent = (String) msg.obj;
                    Log.d(TAG, "handleMessage: "+receiverIntent);
                    break;
            }
        }
    };

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
        if (call.method.equals("getPlatformVersion")) {
            result.success("Android " + android.os.Build.VERSION.RELEASE);
        } else if (call.method.equals("Token")) {// 请求token
            Token();
            Log.d(TAG, "onMethodCall: "+token);
        } else if (call.method.equals("getToken")) {// 获取token
            Log.d(TAG, "onMethodCall11111: "+token);
            result.success(token);
        } else if (call.method.equals("Receiver")) { //调用getReceiver注册广播
            getReceiver();
            Log.d(TAG, "onMethodCall: "+receiverIntent);
        } else if (call.method.equals("getIntentMsg")) { //获取返回的intent
            result.success(receiverIntent);
            Log.d(TAG, "onMethodCall: "+receiverIntent);
        }  else {
            result.notImplemented();
        }
    }

    // 接收广播
    public void getReceiver(){
        //实例化IntentFilter对象
        IntentFilter filter = new IntentFilter();
        filter.addAction("con.flutter.HuaWeiPush");
        myReceiver = new MyReceiver();
        //注册广播接收
        mApplication.registerReceiver(myReceiver,filter);
//        myReceiver = new MyReceiver();
        myReceiver.setMyCallBack(new MyReceiver.MyCallBack() {
            @Override
            public void getIntent(Intent intent) {
                if(intent!=null){
                    Message message = new Message();
                    message.what = 2;
//                    Uri uri = intent.getParcelableExtra("HuaWeiPushUri");
                    String msg = intent.getStringExtra("HuaWeiPushUri");
//                    String msg = intent.getExtras().getString("HuaWeiPushUri");
                    message.obj = msg;
                    handler.sendMessage(message);
                }

                Log.d("MyReceiver", "getIntent: 0");
            }
        });
    }

    //获取token
    public void Token() {
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
                            Message message = new Message();
                            message.what = 1;
                            message.obj = token_value;
                            handler.sendMessage(message);
                            Log.d(TAG, "onReceive: "+ token);
                        }
                    }
                });

            }
        });

    }

    @Override
    public void onAttachedToActivity(ActivityPluginBinding binding) {
        mActivity = new WeakReference<>(binding.getActivity());
    }

    @Override
    public void onDetachedFromActivityForConfigChanges() {

    }

    @Override
    public void onReattachedToActivityForConfigChanges(ActivityPluginBinding binding) {

    }

    @Override
    public void onDetachedFromActivity() {
        mActivity = null;
    }


}
