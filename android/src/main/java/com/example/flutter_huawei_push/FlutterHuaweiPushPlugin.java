package com.example.flutter_huawei_push;

import android.util.Log;

import androidx.annotation.NonNull;

import com.huawei.android.hms.agent.HMSAgent;
import com.huawei.android.hms.agent.push.handler.GetTokenHandler;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/** FlutterHuaweiPushPlugin */
public class FlutterHuaweiPushPlugin implements FlutterPlugin, MethodCallHandler {
  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    final MethodChannel channel = new MethodChannel(flutterPluginBinding.getFlutterEngine().getDartExecutor(), "flutter_huawei_push");
    channel.setMethodCallHandler(new FlutterHuaweiPushPlugin());
  }

  private static String TAG = "| JPUSH | Flutter | Android | ";

  public static void registerWith(Registrar registrar) {
    final MethodChannel channel = new MethodChannel(registrar.messenger(), "flutter_huawei_push");
    channel.setMethodCallHandler(new FlutterHuaweiPushPlugin());
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    if (call.method.equals("getPlatformVersion")) {
      result.success("Android " + android.os.Build.VERSION.RELEASE);
    }else if(call.method.equals("getToken")){
//      result.success("getToken " + android.os.Build.VERSION.RELEASE);
      getToken(call, result);

    } else {
      result.notImplemented();
    }
  }

  public void getToken(MethodCall call, Result result) {
    Log.d(TAG,"getAllTags： ");
    HMSAgent.Push.getToken(new GetTokenHandler() {
      @Override
      public void onResult(int rtnCode) {
        Log.d("getToken","get token: end code=" + rtnCode);
        String code = rtnCode+"";
      }
    });

  }


  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
  }
}
