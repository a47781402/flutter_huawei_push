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
    public interface MyCallBack{
        public abstract void getIntent(Intent intent);
    }

    // 设置回调接口对象成员变量
    public void setMyCallBack(MyCallBack mCallBack) {
        Log.d(TAG, "getIntent: 1"+mCallBack);
        this.myCallBack = mCallBack;
        Log.d(TAG, "getIntent: 0" + mCallBack);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //通过土司验证接收到广播
        Toast t = Toast.makeText(context,"onReceive: name::"+intent.getStringExtra("name")+" age::"+intent.getStringExtra("age") , Toast.LENGTH_SHORT);
        Log.d("DynamicReceiver", "onReceive: name::"+intent.getStringExtra("name")+" age::"+intent.getStringExtra("age") );
        t.setGravity(Gravity.TOP,0,0);//方便录屏，将土司设置在屏幕顶端
        t.show();
        if(myCallBack!=null){
            myCallBack.getIntent(intent);
        }
//        myCallBack.getIntent(intent);
//        doWork(intent);
        abortBroadcast();
    }
}
