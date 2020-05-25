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

    private static final String TAG = "MainActivity";
    private FlutterHuaweiPushPlugin flutterHuaweiPushPlugin;


    @Override
    public void configureFlutterEngine(@NonNull FlutterEngine flutterEngine) {
        GeneratedPluginRegistrant.registerWith(flutterEngine);
        HMSAgent.init(this);
        flutterHuaweiPushPlugin = new FlutterHuaweiPushPlugin().getInstance();
        flutterHuaweiPushPlugin.Token();
        flutterHuaweiPushPlugin.getReceiver();

    }
    @Override
    protected void onResume() {
        super.onResume();
        getIntentData(getIntent());
    }

    private void getIntentData(Intent intent) {
        if (intent != null) {
            try {
                Uri uri = intent.getData();
                if (uri == null) {
                    Log.e(TAG, "getData null");
                    return;
                }
                Intent it = new Intent();
                it.setAction("con.flutter.HuaWeiPush");
                it.putExtra("HuaWeiPushUri",uri.toString());
                sendOrderedBroadcast(it,null);
            } catch (NullPointerException e) {
                Log.e(TAG, "NullPointer," + e);
            } catch (NumberFormatException e) {
                Log.e(TAG, "NumberFormatException," + e);
            } catch (UnsupportedOperationException e) {
                Log.e(TAG, "UnsupportedOperationException," + e);
            }

        }
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getIntentData(intent);
    }
}
