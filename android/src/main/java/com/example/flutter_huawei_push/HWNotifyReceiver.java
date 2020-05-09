package com.example.flutter_huawei_push;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.huawei.hms.support.api.push.PushReceiver;

import java.io.UnsupportedEncodingException;

public class HWNotifyReceiver extends PushReceiver {
    String TAG = "HWNotifyReceiver";
    @Override
    public void onToken(Context context, String token, Bundle extras) {
        super.onToken(context, token, extras);
        System.out.println("项目token"+token);
    }


    @Override
    public boolean onPushMsg(Context context, byte[] msg, Bundle bundle) {
        try {
            String content = new String(msg, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void onEvent(Context context, Event event, Bundle extras) {
        Log.i(TAG, extras.toString());
        if (Event.NOTIFICATION_OPENED.equals(event) || Event.NOTIFICATION_CLICK_BTN.equals(event)) {
            int notifyId = extras.getInt(BOUND_KEY.pushNotifyId, 0);
            if (0 != notifyId) {
                NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                manager.cancel(notifyId);
            }
            String content = "收到通知附加消息： " + extras.getString(BOUND_KEY.pushMsgKey);
            Log.i(TAG, content);
        }
        super.onEvent(context, event, extras);
    }


    @Override
    public void onPushState(Context context, boolean pushState) {
        super.onPushState(context, pushState);
//        System.out.println("项目token"+token);
    }


}
