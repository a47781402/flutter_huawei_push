package com.example.flutter_huawei_push;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

public class TransitionActivity extends Activity {

    private static final String TAG = "TransitionActivity";

    FlutterHuaweiPushPlugin flutterHuaweiPushPlugin = new com.example.flutter_huawei_push.FlutterHuaweiPushPlugin().getInstance();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(Intent.ACTION_VIEW);
// Scheme协议（pushscheme://com.huawei.codelabpush/deeplink?）需要开发者自定义
        intent.setData(Uri.parse("huawei_push://com.example.flutter_huawei_push_example/push?"));

// 必须带上该Flag
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        String intentUri = intent.toUri(Intent.URI_INTENT_SCHEME);
// 打印出的intentUri值就是设置到推送消息中intent字段的值
        Log.d("intentUri", intentUri);
        flutterHuaweiPushPlugin.getReceiver();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        flutterHuaweiPushPlugin.getReceiver();
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
                finish();
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
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getIntentData(intent);
    }
}
