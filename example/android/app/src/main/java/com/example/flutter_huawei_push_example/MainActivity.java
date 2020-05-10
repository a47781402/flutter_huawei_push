package com.example.flutter_huawei_push_example;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.huawei.android.hms.agent.HMSAgent;
import com.huawei.android.hms.agent.common.handler.ConnectHandler;
import com.huawei.android.hms.agent.push.handler.GetTokenHandler;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.plugins.GeneratedPluginRegistrant;

public class MainActivity extends FlutterActivity {

    @Override
    public void configureFlutterEngine(@NonNull FlutterEngine flutterEngine) {
        GeneratedPluginRegistrant.registerWith(flutterEngine);
        HMSAgent.init(this);
        HMSAgent.connect(this, new ConnectHandler() {
            @Override
            public void onConnect(int rst) {
                Log.e("MainActivity","connect result" + rst);
            }
        });

        getToken();


//        Log.d("MainActivity", "getToken： ");
//        HMSAgent.Push.getToken(new GetTokenHandler() {
//            @Override
//            public void onResult(int rtnCode) {
//                Log.d("getToken", "get token: end code=" + rtnCode);
//                String code = rtnCode + "";
//            }
//        });
    }

    private void getToken() {
        new Thread() {
            @Override
            public void run() {
                try {
                    // read from agconnect-services.json
                    HMSAgent.Push.getToken(new GetTokenHandler() {
                        @Override
                        public void onResult(int rtnCode) {
                            Log.d("getToken", "get token: end code=" + rtnCode);
                            String code = rtnCode + "";

                        }
                    });
                } catch (Exception e) {
                    Log.e("getToken", "get token failed, " + e);
                }
            }
        }.start();

    }


    public static void HUAWEIConnect(Activity activity) {
        HMSAgent.connect(activity, new ConnectHandler() {
            @Override
            public void onConnect(int rst) {
                Log.d("MainActivity", "connect result" + rst);
//                LogUtils.e("connect result" + rst);
            }
        });
    }
}
