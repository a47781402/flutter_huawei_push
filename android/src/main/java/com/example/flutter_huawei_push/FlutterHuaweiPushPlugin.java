package com.example.flutter_huawei_push;

import android.app.Activity;
import android.app.Application;
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
    private String EVENT_TOKEN = "action.event.token";
    private static String Method_Channel_ACTION = "flutter_huawei_push";
    private static String EVENT_Channel_ACTION = "flutter_huawei_push_event";
//    private EventChannel.EventSink plugineventSink;
//    private BroadcastReceiver mStateChangeReceiver;
    // 获取环境
    private FlutterPluginBinding flutterPluginBinding;
    private MethodChannel mMethodChannel;
    private EventChannel mEventChannel;
    private WeakReference<Activity> mActivity;
    private Application mApplication;
    // 需要的变量
    // 华为推送token
    private String token ;

    public String getToken() {
        return token;
    }

    // 旧版本的注册
    public static void registerWith(Registrar registrar) {

        final MethodChannel channel = new MethodChannel(registrar.messenger(), Method_Channel_ACTION);
        channel.setMethodCallHandler(new FlutterHuaweiPushPlugin().initPlugin(channel,registrar));
    }

    // 注册
    @Override
    public void onAttachedToEngine(@NonNull final FlutterPluginBinding flutterPluginBinding) {
        mMethodChannel = new MethodChannel(flutterPluginBinding.getFlutterEngine().getDartExecutor(), Method_Channel_ACTION);
        mApplication = (Application) flutterPluginBinding.getApplicationContext();
        mMethodChannel.setMethodCallHandler(new FlutterHuaweiPushPlugin());
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        mMethodChannel.setMethodCallHandler(null);
        mMethodChannel = null;
    }

    public FlutterHuaweiPushPlugin initPlugin(MethodChannel methodChannel, Registrar registrar) {
        mMethodChannel = methodChannel;
        mApplication = (Application) registrar.context().getApplicationContext();
        mActivity = new WeakReference<>(registrar.activity());
        return this;
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
            Token();
        }  else {
            result.notImplemented();
        }
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
