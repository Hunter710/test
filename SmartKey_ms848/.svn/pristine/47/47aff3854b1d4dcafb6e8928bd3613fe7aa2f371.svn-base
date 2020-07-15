package com.odm.smartkey;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.odm.smartkey.receiver.Util;

public class SingleListenerRemindActivity extends Activity {

    TextView mTextView;
    Context  context;
    private int     recLen = 6;
    private boolean isRun  = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_listener_reminder);

        context = SingleListenerRemindActivity.this;
        mTextView = (TextView) findViewById(R.id.count_down_value);
        ImageView time_count_down = (ImageView) findViewById(R.id.time_count_down);

        Animation operatingAnim = AnimationUtils.loadAnimation(this, R.anim.rotate);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);

        time_count_down.startAnimation(operatingAnim);

        new Thread(new MyThread()).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler = null;
        }
    }

    /**计时器*/
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    recLen--;
                    mTextView.setText(String.valueOf(recLen));
                    if (recLen == 0) {
                        Settings.Global.putInt(getContentResolver(),"singlelistenerstate",1);
                        Intent intent = new Intent();
                        intent.setAction(Util.ACTION_SINGLE_LISTEN_ON);
                        sendBroadcast(intent);
                        finish();
                    }
            }
            super.handleMessage(msg);
        }
    };

    /**计时器*/
    private class MyThread implements Runnable {
        @Override
        public void run() {
            while (recLen > 0 && !isRun) {
                try {
                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        isRun = true;
        Settings.Global.putInt(getContentResolver(),"singlelistenerstate",0);
        SingleListenerRemindActivity.this.finish();
        //返回键会finish界面并做一次回退 屏蔽回退
        return event.getKeyCode() == KeyEvent.KEYCODE_BACK || super.dispatchKeyEvent(event);
    }
}
