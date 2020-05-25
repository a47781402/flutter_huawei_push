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
public class FlutterHuaweiPushPlugin implements FlutterPlugin, MethodCallHandler, ActivityAware,EventChannel.StreamHandler {


    private static String TAG = "| FlutterHuaweiPushPlugin | Flutter | Android | ";

    // token标记
    private static String KEY_TOKEN = "action.updateToken";
    private static String Method_Channel_ACTION = "flutter_huawei_push";
    private static String Event_Channel_ACTION = "flutter_huawei_receiver";
    // 获取环境
    private FlutterPluginBinding flutterPluginBinding;
    private static  MethodChannel mMethodChannel;
    private static  WeakReference<Activity> mActivity;
    private static  Application mApplication;
    private static  EventChannel mEventChannel;
    private EventChannel.EventSink mEventSink;
    // 单例
    public static FlutterHuaweiPushPlugin instance;
    // 华为推送token
    private String token ;
    // 获取点击事件广播
    private MyReceiver myReceiver;
    private String receiverIntent = null;
    private String offReceiverIntent = null;
    // dart是否准备好
    private boolean isDartReady = false;


    // 构造方法
    public FlutterHuaweiPushPlugin() { }

    // 单例模式
    public static FlutterHuaweiPushPlugin getInstance(){
        if(instance == null){
            instance = new FlutterHuaweiPushPlugin();
            instance.isDartReady = true;

        }
        return instance;
    }

    // 旧版本的注册
    public static void registerWith(Registrar registrar) {
        mMethodChannel = new MethodChannel(registrar.messenger(), Method_Channel_ACTION);
        mApplication = (Application) registrar.context().getApplicationContext();
        mActivity = new WeakReference<>(registrar.activity());
        mEventChannel = new EventChannel(registrar.messenger(),Event_Channel_ACTION);
        mMethodChannel.setMethodCallHandler(getInstance());
        mEventChannel.setStreamHandler(getInstance());
        registrar.addViewDestroyListener(new PluginRegistry.ViewDestroyListener() {
            @Override
            public boolean onViewDestroy(FlutterNativeView view) {
                return false;
            }
        });
    }


    // 注册
    @Override
    public void onAttachedToEngine(@NonNull final FlutterPluginBinding flutterPluginBinding) {
        mMethodChannel = new MethodChannel(flutterPluginBinding.getFlutterEngine().getDartExecutor(), Method_Channel_ACTION);
        mEventChannel = new EventChannel(flutterPluginBinding.getFlutterEngine().getDartExecutor(),Event_Channel_ACTION);
        mApplication = (Application) flutterPluginBinding.getApplicationContext();
        mMethodChannel.setMethodCallHandler(getInstance());
        mEventChannel.setStreamHandler(getInstance());
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        mMethodChannel.setMethodCallHandler(null);
        mMethodChannel = null;
    }


    @Override
    public void onListen(Object o, EventChannel.EventSink eventSink) {
        this.mEventSink = eventSink;
        // 处理程序被杀死时跳转获得的intent数据
        if(instance.isDartReady && offReceiverIntent!=null){
            eventSink.success(offReceiverIntent);
            Log.d(TAG, "mEventChannel监听 onListen: "+offReceiverIntent);
        }
    }

    @Override
    public void onCancel(Object o) {
        if(receiverIntent == null){
            Log.d(TAG, "onCancel: null");
        }

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
                    if(mEventSink!=null){
                        if(isDartReady){
                            mEventSink.success(receiverIntent);
                        }
                    }else {
                        offReceiverIntent = receiverIntent;
                    }
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
        // 设置action
        filter.addAction("con.flutter.HuaWeiPush");
        // 实例化自定义广播
        myReceiver = new MyReceiver();
        //注册广播接收
        mApplication.registerReceiver(myReceiver,filter);
        // 自定义广播监听
        myReceiver.setMyCallBack(new MyReceiver.MyCallBack() {
            @Override
            public void getIntent(Intent intent) {
                if(intent!=null){
                    Message message = new Message();
                    message.what = 2;
                    String msg = intent.getStringExtra("HuaWeiPushUri");
                    message.obj = msg;
                    handler.sendMessage(message);
                }
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
