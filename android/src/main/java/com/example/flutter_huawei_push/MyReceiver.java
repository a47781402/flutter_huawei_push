package com.example.flutter_huawei_push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {
    private String TAG = "MyReceiver";
    // 定义回调接口的成员变量
    private MyCallBack myCallBack;

    // 申明回调接口
    public interface MyCallBack {
        public abstract void getIntent(Intent intent);
    }

    // 设置回调接口对象成员变量
    public void setMyCallBack(MyCallBack mCallBack) {
        this.myCallBack = mCallBack;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: "+intent);
        if (myCallBack != null) {
            myCallBack.getIntent(intent);
        }

        abortBroadcast();
    }
}
