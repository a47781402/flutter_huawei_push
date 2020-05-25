package com.example.flutter_huawei_push;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

public class TransitionActivity extends Activity {

    private static final String TAG = "TransitionActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
