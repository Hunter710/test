package com.odm.smartkey;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iflytek.xiri.Feedback;
import com.iflytek.xiri.scene.ISceneListener;
import com.iflytek.xiri.scene.Scene;
import com.mstar.android.tv.TvTimerManager;
import com.mstar.android.tvapi.common.exception.TvCommonException;
import com.odm.smartkey.bridge.APIBridge;
import com.odm.smartkey.receiver.ActivityManager;
import com.odm.smartkey.receiver.Util;

import java.io.File;

/**
 * @author ycz
 */
public class SmartKeyActivity extends Activity implements OnFocusChangeListener {
    private static String TAG = "SmartActivity";

    private long mTimeBefore = 0;

    private PopupWindow mPopWindow;

    private RelativeLayout enjoy_music_layout, sleep_timer_layout, world_clock_layout, soundbar_ready_layout, fast_cast_layout;
    private TextView enjoy_music_text, sleep_timer_text, world_clock_text, soundbar_ready_text, fast_cast_text;
    private TextView text_off_content, text_min10_content, text_min20_content, text_min30_content, text_min60_content, text_min90_content, text_min120_content, text_min180_content, text_standard_content, text_music_content, text_movie_content;
    private ImageView sleep_timer_icon, soundbar_ready_icon;
//    private ImageView  arrow_up;
    private RelativeLayout text_off, text_min10, text_min20, text_min30, text_min60, text_min90, text_min120, text_min180;

    private SharedPreferences mSharedPreferences;

    private PackageInfo isPackageExit;

    private int mBlue;
    private int mWhite;

    private Handler  mHandler;
    private Runnable mRunnable;
    private int mTimer = 0;

    //    private AudioManager   mBacklightApi;
    //    private TvTimerManager mSleepTimeApi;
    //    private TvCecManager   mSoundbarApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart);
        ActivityManager.getInstance().addActivity(this);

        init();
        initApi();
        initView();
        initTimer();
        setClickListener();
        setFocusChangeListener();
        xiriSceneAttach();
    }

    @Override
    protected void onResume() {
        xiriSceneAttach();
        Intent intent = getIntent();
        if (null != intent) {
            if (intent.hasExtra("_target")) {
                popupSubItem(intent.getStringExtra("_target"));
            }
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        xiriSceneDetach();
        super.onPause();
        finish();
    }

    @Override
    protected void onDestroy() {
        xiriSceneDetach();
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacks(mRunnable);
            mHandler = null;
        }

        if (mPopWindow != null) {
            mPopWindow.dismiss();
            mPopWindow = null;
        }
    }

    /**findViewById*/
    private void init() {
        enjoy_music_layout = (RelativeLayout) findViewById(R.id.enjoy_music_text);
        sleep_timer_layout = (RelativeLayout) findViewById(R.id.sleep_timer_text);
        world_clock_layout = (RelativeLayout) findViewById(R.id.world_clock_text);
        soundbar_ready_layout = (RelativeLayout) findViewById(R.id.soundbar_ready_text);
        fast_cast_layout = (RelativeLayout) findViewById(R.id.fast_cast_text);

        enjoy_music_text = (TextView) findViewById(R.id.enjoy_music_text_context);
        sleep_timer_text = (TextView) findViewById(R.id.sleep_timer_text_context);
        world_clock_text = (TextView) findViewById(R.id.world_clock_text_context);
        soundbar_ready_text = (TextView) findViewById(R.id.soundbar_ready_text_context);
        fast_cast_text = (TextView) findViewById(R.id.fast_cast_text_context);

        sleep_timer_icon = (ImageView) findViewById(R.id.sleep_timer_icon);
        soundbar_ready_icon = (ImageView) findViewById(R.id.soundbar_ready_icon);
//        arrow_up = (ImageView) findViewById(R.id.arrow_up);

        mBlue = getResources().getColor(R.color.blue);
        mWhite = getResources().getColor(R.color.white);

        mSharedPreferences = getSharedPreferences(Util.ON_OFF_MODE_NAME, Context.MODE_PRIVATE);
    }

    /**初始化接口*/
    private void initApi() {
        //        mBacklightApi = TvManager.getInstance().getAudioManager();
        //        mSleepTimeApi = TvTimerManager.getInstance();
        //        mSoundbarApi = TvCecManager.getInstance();
    }

    private void initView() {
        // 定时关机
        boolean isSleepTime = APIBridge.isSleepTime();
        if (isSleepTime) {
            sleep_timer_icon.setBackgroundResource(R.drawable.smart_sleep_timer_focus);
        } else {
            sleep_timer_icon.setBackgroundResource(R.drawable.smart_sleep_timer);
        }

        // 夜间模式
//        boolean isNightMode = APIBridge.isNightMode();
//        if (isNightMode) {
//            night_mode_icon.setBackgroundResource(R.drawable.smart_night_mode_focus);
//        } else {
//            night_mode_icon.setBackgroundResource(R.drawable.smart_night_mode);
//        }

        //soundBar
        boolean isSoundbarOn = APIBridge.isSoundbarOn();
        if (isSoundbarOn) {
            soundbar_ready_icon.setBackgroundResource(R.drawable.smart_soundbar_focus);
//            if (APIBridge.isSupportedSoundbar()) {
//                // 特定的b3340 出箭头
//                arrow_up.setVisibility(View.VISIBLE);
//            } else {
//                arrow_up.setVisibility(View.INVISIBLE);
//            }
        } else {
            soundbar_ready_icon.setBackgroundResource(R.drawable.smart_soundbar);
//            arrow_up.setVisibility(View.INVISIBLE);
        }

        int position = mSharedPreferences.getInt("position", 0);
        setFocus(position);
    }

    /**15秒自动关闭*/
    private void initTimer() {
        mHandler = new Handler();
        mRunnable = new Runnable() {
            @Override
            public void run() {
                mTimer++;
                //Log.d(TAG, mTimer + "");
                if (mTimer > 15) {
                    SmartKeyActivity.this.finish();
                }
                mHandler.postDelayed(this, 1000);
            }
        };
        mHandler.postDelayed(mRunnable, 1000);
    }

    private void setClickListener() {
        enjoy_music_text.setOnClickListener(mOnClickListener);
        sleep_timer_text.setOnClickListener(mOnClickListener);
        world_clock_text.setOnClickListener(mOnClickListener);
        soundbar_ready_text.setOnClickListener(mOnClickListener);
        fast_cast_text.setOnClickListener(mOnClickListener);
    }

    private void setOnClickListener(RelativeLayout relativeLayout) {
        relativeLayout.setOnClickListener(mOnClickListener);
    }

    /**点击事件*/
    private OnClickListener mOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {

            // 防连击：间隔300ms
            long time = System.currentTimeMillis();
            if (time - mTimeBefore < 300) {
                Log.d(TAG, "===== Double click =====");
                return;
            }
            mTimeBefore = time;

            Intent intent;
            switch (view.getId()) {
                case R.id.enjoy_music_text_context:
                    mSharedPreferences.edit().putInt("position", 1).apply();
                    intent = new Intent();
                    intent.setClass(SmartKeyActivity.this, SingleListenerRemindActivity.class);
                    startActivity(intent);
                    SmartKeyActivity.this.finish();
                    break;
                case R.id.sleep_timer_text_context:
                    mSharedPreferences.edit().putInt("position", 2).apply();
                    showSleepPopupWindow();
                    break;
                case R.id.world_clock_text_context:
                    mSharedPreferences.edit().putInt("position", 3).apply();
//                    LauncherApp(Util.TIME_CLOCK);
                    Intent mIntent = new Intent();
                    mIntent.setClassName("com.apps.timeclock","com.apps.timeclock.TimeClockActivity");
                    startActivity(mIntent);
                    overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                    break;
                case R.id.soundbar_ready_text_context:
                    mSharedPreferences.edit().putInt("position", 4).apply();
                    if (APIBridge.isSoundbarOn()) {
                        soundbar_ready_icon.setBackgroundResource(R.drawable.smart_soundbar);
                        APIBridge.setSoundbarToggle(false);
//                        arrow_up.setVisibility(View.INVISIBLE);

                    } else {
                        APIBridge.setSoundbarToggle(true);
                        soundbar_ready_icon.setBackgroundResource(R.drawable.smart_soundbar_focus);
//                        if (APIBridge.isSupportedSoundbar()) {
//                            arrow_up.setVisibility(View.VISIBLE);
//                        } else {
//                            arrow_up.setVisibility(View.INVISIBLE);
//                        }
                    }
                    break;
                case R.id.fast_cast_text_context:
                    mSharedPreferences.edit().putInt("position", 5).apply();
                    LauncherApp(Util.FAST_CAST);
                    break;
//                case R.id.night_mode_text_context:
//                    mSharedPreferences.edit().putInt("position", 7).apply();
//                    try {
//                        boolean isNightMode = APIBridge.isNightMode();
//                        if (isNightMode) {
//                            APIBridge.setNightModeToggle(false);
//                            night_mode_icon.setBackgroundResource(R.drawable.smart_night_mode);
//
//                        } else {
//                            boolean isSuccess = APIBridge.setNightModeToggle(true);
//                            if (isSuccess) {
//                                night_mode_icon.setBackgroundResource(R.drawable.smart_night_mode_focus);
//                            } else {
//                                Toast.makeText(SmartKeyActivity.this, R.string.night_mode_failed, Toast.LENGTH_SHORT)
//                                     .show();
//                            }
//                        }
//                    } catch (TvCommonException e) {
//                        e.printStackTrace();
//                    }
//                    break;

                case R.id.text_off:
                    setSleepMode(0);
                    break;
                case R.id.text_min10:
                    setSleepMode(1);
                    break;
                case R.id.text_min20:
                    setSleepMode(2);
                    break;
                case R.id.text_min30:
                    setSleepMode(3);
                    break;
                case R.id.text_min60:
                    setSleepMode(4);
                    break;
                case R.id.text_min90:
                    setSleepMode(5);
                    break;
                case R.id.text_min120:
                    setSleepMode(6);
                    break;
                case R.id.text_min180:
                    setSleepMode(7);
                    break;

                case R.id.text_standard:
                    setSoundbarMode(APIBridge.SOUNDBAR_STANDARD);
                    break;
                case R.id.text_music:
                    setSoundbarMode(APIBridge.SOUNDBAR_MUSIC);
                    break;
                case R.id.text_movie:
                    setSoundbarMode(APIBridge.SOUNDBAR_MOVIE);
                    break;
            }
        }
    };

    private void setFocusChangeListener() {
        enjoy_music_text.setOnFocusChangeListener(this);
        sleep_timer_text.setOnFocusChangeListener(this);
        world_clock_text.setOnFocusChangeListener(this);
        soundbar_ready_text.setOnFocusChangeListener(this);
        fast_cast_text.setOnFocusChangeListener(this);
    }

    private void setOnKeyListener(RelativeLayout relativeLayout) {
        relativeLayout.setOnKeyListener(mOnKeyListener);
    }

    /**mOnKeyListener*/
    private OnKeyListener mOnKeyListener = new OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            mTimer = 0;
            if (event.getAction() == KeyEvent.ACTION_DOWN && (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_DPAD_DOWN)) {
                mPopWindow.dismiss();
            }
            return false;
        }
    };

    /**设置睡眠关机*/
    public void setSleepMode(int mSleepingTimeIndex) {

        text_off_content.setTextColor(mWhite);
        text_min10_content.setTextColor(mWhite);
        text_min20_content.setTextColor(mWhite);
        text_min30_content.setTextColor(mWhite);
        text_min60_content.setTextColor(mWhite);
        text_min90_content.setTextColor(mWhite);
        text_min120_content.setTextColor(mWhite);
        text_min180_content.setTextColor(mWhite);

        try {
            switch (mSleepingTimeIndex) {
                case 0:
                    Log.d(TAG, "setSleepMode: mSleepTimeApi === off");
                    APIBridge.setSleepTime(APIBridge.SLEEP_TIME_OFF);
                    sleep_timer_icon.setBackgroundResource(R.drawable.smart_sleep_timer);
                    text_off_content.setTextColor(mBlue);
                    break;
                case 1:
                    APIBridge.setSleepTime(APIBridge.SLEEP_TIME_10MIN);
                    sleep_timer_icon.setBackgroundResource(R.drawable.smart_sleep_timer_focus);
                    text_min10_content.setTextColor(mBlue);
                    break;
                case 2:
                    APIBridge.setSleepTime(APIBridge.SLEEP_TIME_20MIN);
                    sleep_timer_icon.setBackgroundResource(R.drawable.smart_sleep_timer_focus);
                    text_min20_content.setTextColor(mBlue);
                    break;
                case 3:
                    APIBridge.setSleepTime(APIBridge.SLEEP_TIME_30MIN);
                    sleep_timer_icon.setBackgroundResource(R.drawable.smart_sleep_timer_focus);
                    text_min30_content.setTextColor(mBlue);
                    break;
                case 4:
                    APIBridge.setSleepTime(APIBridge.SLEEP_TIME_60MIN);
                    sleep_timer_icon.setBackgroundResource(R.drawable.smart_sleep_timer_focus);
                    text_min60_content.setTextColor(mBlue);
                    break;
                case 5:
                    APIBridge.setSleepTime(APIBridge.SLEEP_TIME_90MIN);
                    sleep_timer_icon.setBackgroundResource(R.drawable.smart_sleep_timer_focus);
                    text_min90_content.setTextColor(mBlue);
                    break;
                case 6:
                    APIBridge.setSleepTime(APIBridge.SLEEP_TIME_120MIN);
                    sleep_timer_icon.setBackgroundResource(R.drawable.smart_sleep_timer_focus);
                    text_min120_content.setTextColor(mBlue);
                    break;
                case 7:
                    APIBridge.setSleepTime(APIBridge.SLEEP_TIME_180MIN);
                    sleep_timer_icon.setBackgroundResource(R.drawable.smart_sleep_timer_focus);
                    text_min180_content.setTextColor(mBlue);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**设置soundbar音效*/
    private void setSoundbarMode(int mode) {
        APIBridge.setSoundbarMode(mode);
        Log.d(TAG, "setAudioSystemOption ===" + mode);

        text_standard_content.setTextColor(mWhite);
        text_music_content.setTextColor(mWhite);
        text_movie_content.setTextColor(mWhite);

        switch (mode) {
            case 1:
                text_standard_content.setTextColor(mBlue);
                break;
            case 2:
                text_music_content.setTextColor(mBlue);
                break;
            case 3:
                text_movie_content.setTextColor(mBlue);
                break;
        }
    }

    /**初始化：停留在上次退出的position*/
    private void setFocus(int position) {
        switch (position) {
            case 1:
                enjoy_music_text.requestFocus();
                enjoy_music_text.setTextColor(mBlue);
                enjoy_music_layout.setBackgroundResource(R.drawable.smart_focus);
                break;
            case 2:
                sleep_timer_text.requestFocus();
                sleep_timer_text.setTextColor(mBlue);
                sleep_timer_layout.setBackgroundResource(R.drawable.smart_focus);
                break;
            case 3:
                world_clock_text.requestFocus();
                world_clock_text.setTextColor(mBlue);
                world_clock_layout.setBackgroundResource(R.drawable.smart_focus);
                break;
            case 4:
                soundbar_ready_text.requestFocus();
                soundbar_ready_text.setTextColor(mBlue);
                soundbar_ready_layout.setBackgroundResource(R.drawable.smart_focus);
                break;
            case 5:
                fast_cast_text.requestFocus();
                fast_cast_text.setTextColor(mBlue);
                fast_cast_layout.setBackgroundResource(R.drawable.smart_focus);
                break;
        }
    }

    /**启动apk*/
    private void LauncherApp(String packname) {
        try {
            isPackageExit = this.getPackageManager().getPackageInfo(packname, 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        if (isPackageExit != null) {
            Intent intent = getPackageManager().getLaunchIntentForPackage(packname);
            startActivity(intent);
            finish();
        } else {
            Log.i(TAG, packname + " is no exist");
        }
    }

    /**焦点事件*/
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            switch (v.getId()) {
                case R.id.enjoy_music_text_context:
                    enjoy_music_layout.setBackgroundResource(R.drawable.smart_focus);
                    enjoy_music_text.setTextColor(mBlue);
                    break;
                case R.id.sleep_timer_text_context:
                    sleep_timer_layout.setBackgroundResource(R.drawable.smart_focus);
                    sleep_timer_text.setTextColor(mBlue);
                    break;
                case R.id.world_clock_text_context:
                    world_clock_layout.setBackgroundResource(R.drawable.smart_focus);
                    world_clock_text.setTextColor(mBlue);
                    break;
                case R.id.soundbar_ready_text_context:
                    soundbar_ready_layout.setBackgroundResource(R.drawable.smart_focus);
                    soundbar_ready_text.setTextColor(mBlue);
                    break;
                case R.id.fast_cast_text_context:
                    fast_cast_layout.setBackgroundResource(R.drawable.smart_focus);
                    fast_cast_text.setTextColor(mBlue);
                    break;
            }
        } else {
            switch (v.getId()) {
                case R.id.enjoy_music_text_context:
                    enjoy_music_layout.setBackgroundResource(R.drawable.text_bg_normal);
                    enjoy_music_text.setTextColor(mWhite);
                    break;
                case R.id.sleep_timer_text_context:
                    sleep_timer_layout.setBackgroundResource(R.drawable.text_bg_normal);
                    sleep_timer_text.setTextColor(mWhite);
                    break;
                case R.id.world_clock_text_context:
                    world_clock_layout.setBackgroundResource(R.drawable.text_bg_normal);
                    world_clock_text.setTextColor(mWhite);
                    break;
                case R.id.soundbar_ready_text_context:
                    soundbar_ready_layout.setBackgroundResource(R.drawable.text_bg_normal);
                    soundbar_ready_text.setTextColor(mWhite);
                    break;
                case R.id.fast_cast_text_context:
                    fast_cast_layout.setBackgroundResource(R.drawable.text_bg_normal);
                    fast_cast_text.setTextColor(mWhite);
                    break;
            }
        }
    }

    private void showSleepPopupWindow() {
        @SuppressLint("InflateParams") View contentView = LayoutInflater.from(SmartKeyActivity.this)
                                                                        .inflate(R.layout.pop_window_sleep, null);
        mPopWindow = new PopupWindow(contentView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
        mPopWindow.setContentView(contentView);
        mPopWindow.setFocusable(true);

        text_off = (RelativeLayout) contentView.findViewById(R.id.text_off);
        text_min10 = (RelativeLayout) contentView.findViewById(R.id.text_min10);
        text_min20 = (RelativeLayout) contentView.findViewById(R.id.text_min20);
        text_min30 = (RelativeLayout) contentView.findViewById(R.id.text_min30);
        text_min60 = (RelativeLayout) contentView.findViewById(R.id.text_min60);
        text_min90 = (RelativeLayout) contentView.findViewById(R.id.text_min90);
        text_min120 = (RelativeLayout) contentView.findViewById(R.id.text_min120);
        text_min180 = (RelativeLayout) contentView.findViewById(R.id.text_min180);

        text_off_content = (TextView) contentView.findViewById(R.id.text_off_content);
        text_min10_content = (TextView) contentView.findViewById(R.id.text_min10_content);
        text_min20_content = (TextView) contentView.findViewById(R.id.text_min20_content);
        text_min30_content = (TextView) contentView.findViewById(R.id.text_min30_content);
        text_min60_content = (TextView) contentView.findViewById(R.id.text_min60_content);
        text_min90_content = (TextView) contentView.findViewById(R.id.text_min90_content);
        text_min120_content = (TextView) contentView.findViewById(R.id.text_min120_content);
        text_min180_content = (TextView) contentView.findViewById(R.id.text_min180_content);


        int sleep_mode = APIBridge.getSleepTime();
        Log.i(TAG, "sleep_mode:" + sleep_mode);

        switch (sleep_mode) {
            case TvTimerManager.SLEEP_TIME_OFF:
                text_off.requestFocus();
                text_off_content.setTextColor(mBlue);
                break;
            case TvTimerManager.SLEEP_TIME_10MIN:
                text_min10.requestFocus();
                text_min10_content.setTextColor(mBlue);
                break;
            case TvTimerManager.SLEEP_TIME_20MIN:
                text_min20.requestFocus();
                text_min20_content.setTextColor(mBlue);
                break;
            case TvTimerManager.SLEEP_TIME_30MIN:
                text_min30.requestFocus();
                text_min30_content.setTextColor(mBlue);
                break;
            case TvTimerManager.SLEEP_TIME_60MIN:
                text_min60.requestFocus();
                text_min60_content.setTextColor(mBlue);
                break;
            case TvTimerManager.SLEEP_TIME_90MIN:
                text_min90.requestFocus();
                text_min90_content.setTextColor(mBlue);
                break;
            case TvTimerManager.SLEEP_TIME_120MIN:
                text_min120.requestFocus();
                text_min120_content.setTextColor(mBlue);
                break;
            case TvTimerManager.SLEEP_TIME_180MIN:
                text_min180.requestFocus();
                text_min180_content.setTextColor(mBlue);
                break;
        }

        setOnClickListener(text_off);
        setOnClickListener(text_min10);
        setOnClickListener(text_min20);
        setOnClickListener(text_min30);
        setOnClickListener(text_min60);
        setOnClickListener(text_min90);
        setOnClickListener(text_min120);
        setOnClickListener(text_min180);

        setOnKeyListener(text_off);
        setOnKeyListener(text_min10);
        setOnKeyListener(text_min20);
        setOnKeyListener(text_min30);
        setOnKeyListener(text_min60);
        setOnKeyListener(text_min90);
        setOnKeyListener(text_min120);
        setOnKeyListener(text_min180);

        // PopupWindow定位
        @SuppressLint("InflateParams") View rootview = LayoutInflater.from(SmartKeyActivity.this)
                                                                     .inflate(R.layout.activity_smart, null);
        mPopWindow.showAtLocation(rootview, Gravity.BOTTOM, 0, 0);
    }

//    private void showSoundBarPopupWindow() {
//        @SuppressLint("InflateParams") View contentView = LayoutInflater.from(SmartKeyActivity.this)
//                                                                        .inflate(R.layout.pop_window_soundbar, null);
//        mPopWindow = new PopupWindow(contentView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
//        mPopWindow.setContentView(contentView);
//        mPopWindow.setFocusable(true);
//
//        RelativeLayout text_standard = (RelativeLayout) contentView.findViewById(R.id.text_standard);
//        RelativeLayout text_music = (RelativeLayout) contentView.findViewById(R.id.text_music);
//        RelativeLayout text_movie = (RelativeLayout) contentView.findViewById(R.id.text_movie);
//
//        text_standard_content = (TextView) contentView.findViewById(R.id.text_standard_content);
//        text_music_content = (TextView) contentView.findViewById(R.id.text_music_content);
//        text_movie_content = (TextView) contentView.findViewById(R.id.text_movie_content);
//
//        int value = APIBridge.setSoundbarMode();
//        switch (value) {
//            case APIBridge.SOUNDBAR_STANDARD:
//                text_standard.requestFocus();
//                text_standard_content.setTextColor(mBlue);
//                break;
//            case APIBridge.SOUNDBAR_MUSIC:
//                text_music.requestFocus();
//                text_music_content.setTextColor(mBlue);
//                break;
//            case APIBridge.SOUNDBAR_MOVIE:
//                text_movie.requestFocus();
//                text_movie_content.setTextColor(mBlue);
//                break;
//        }
//
//        setOnClickListener(text_standard);
//        setOnClickListener(text_music);
//        setOnClickListener(text_movie);
//
//        setOnKeyListener(text_standard);
//        setOnKeyListener(text_music);
//        setOnKeyListener(text_movie);
//
//        @SuppressLint("InflateParams") View rootview = LayoutInflater.from(SmartKeyActivity.this)
//                                                                     .inflate(R.layout.activity_smart, null);
//        mPopWindow.showAtLocation(rootview, Gravity.BOTTOM, 0, 0);
//    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        mTimer = 0;
        if (event.getAction() == KeyEvent.ACTION_UP) {
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_DPAD_UP:
                    if (soundbar_ready_text.hasFocus()) {
                        if (APIBridge.isSupportedSoundbar()) {
                            mSharedPreferences.edit().putInt("position", 5).apply();
//                            showSoundBarPopupWindow();
                        }
                    } else if (sleep_timer_text.hasFocus()) {
                        mSharedPreferences.edit().putInt("position", 3).apply();
                        showSleepPopupWindow();
                    }
                    break;
                case KeyEvent.KEYCODE_DPAD_DOWN:
                    if (mPopWindow != null && mPopWindow.isShowing()) {
                        mPopWindow.dismiss();
                    }
                    break;
            }
        }
        return super.dispatchKeyEvent(event);
    }


    ///voice control code start
    private SceneListener objSceneListener = null;
    private boolean xiriSceneAttach() {
        if (null == objSceneListener) {
            objSceneListener = new SceneListener();
            objSceneListener.onCreate(this);
        }
        return true;
    }

    private  boolean xiriSceneDetach() {
        if (null != objSceneListener) {
            objSceneListener.onDestroy();
            objSceneListener = null;
        }
        return true;
    }

    private class SceneListener implements ISceneListener {
        private Scene objScene;
        private String strTAG ;
        private String strSceneName;

        private XiriVoiceControlHelper objVoiceCtrlHelper = null;
        private Feedback objFeedback = null;
        private String strCmdWirelessDisplay = "cmd_wireless_display";
        private String strCmdEnjoyMusic = "cmd_enjoy_music";
        private String strCmdSleepTimer = "cmd_sleep_timer";
        private String strCmdWorldClock = "cmd_world_clock";
        private String strCmdSoundBarReady = "cmd_soundbar_ready";
        private String strCmdFastcast = "cmd_fastcast";
        private String strCmdNightMode = "cmd_night_mode";

        private boolean bWirelessDisplay = false;
        private boolean bEnjoyMusic = false;
        private boolean bSleepTimer = false;
        private boolean bWorldClock = false;
        private boolean bSoundBarReady = false;
        private boolean bFastcast = false;
        private boolean bNightMode = false;

        void onCreate(Context context) {
            Log.v(strTAG, "create scene " + strTAG);
            objScene = new Scene(context);
            objScene.init(this);
            strSceneName = context.getClass().getName();
            objFeedback = new Feedback(context);
            strTAG = strSceneName;
            objVoiceCtrlHelper = new XiriVoiceControlHelper(context);

            ///todo:get all butten status
        }

        void onDestroy() {
            Log.v(strTAG, "destroy scene " + strTAG);
            objScene.release();
            objFeedback = null;
        }

        @Override
        public void onExecute(Intent intent) {
            objFeedback.begin(intent);
            Log.v(strTAG, "onExecute, Uri:" + Uri.decode(intent.toURI())
                    + " scene:" + intent.getStringExtra("_scene"));
            if (intent.hasExtra("_scene") && intent.getStringExtra("_scene").equals(strSceneName)) {
                String strCmd = objVoiceCtrlHelper.GetCmd(intent);

                ///reset close activity timer before exec cmd
                mTimer = 0;
                ///need feedback before exec action, any operate will finish current activity before feedback code exec
                objFeedback.feedback(objVoiceCtrlHelper.GetFuzzyWord(intent), Feedback.SILENCE);

                Log.v(strTAG, "onExecute:" + strCmd);
                if (strCmd.equals(strCmdWirelessDisplay)) {
                    Log.v(strTAG, "wireless display");
                    execWirelessDisplay(intent);
                } else if (strCmd.equals(strCmdEnjoyMusic)) {
                    Log.v(strTAG, "enjoy music");
                    execEnjoyMusic(intent);
                } else if (strCmd.equals(strCmdSleepTimer)) {
                    Log.v(strTAG, "sleep timer");
                    String strSleepTime = objVoiceCtrlHelper.GetStringParam(intent);
                    execSleepTimer(strSleepTime);
                } else if (strCmd.equals(strCmdWorldClock)) {
                    Log.v(strTAG, "world clock");
                    execWorldClock(intent);
                } else if (strCmd.equals(strCmdSoundBarReady)) {
                    Log.v(strTAG, "sound bar ready");
                    String strAction = objVoiceCtrlHelper.GetStringParam(intent);
                    execSoundBarReady(strAction);
                } else if (strCmd.equals(strCmdFastcast)) {
                    Log.v(strTAG, "fastcast");
                    execFastcast(intent);
                } else if (strCmd.equals(strCmdNightMode)) {
                    Log.v(strTAG, "night mode");
                    execNightMode(intent);
                } else {
                    Log.v(strTAG, "unsupport now");
                    return;
                }
            }
        }

        @Override
        public String onQuery() {
            Log.v(strTAG, "query:" + strSceneName + " jason info");
            /// clear last cmd before in need
            objVoiceCtrlHelper.ResetAllFuzzyWord();

            ///Wireless Display
            objVoiceCtrlHelper.SetFuzzyWord(strCmdWirelessDisplay, "无线互联", "switch");
            objVoiceCtrlHelper.SetFuzzyWord(strCmdWirelessDisplay, "打开无线互联", "open");
            objVoiceCtrlHelper.SetFuzzyWord(strCmdWirelessDisplay, "关闭无线互联", "close");

            ///Enjoy Music
            objVoiceCtrlHelper.SetFuzzyWord(strCmdEnjoyMusic, "单独听", "switch");
            objVoiceCtrlHelper.SetFuzzyWord(strCmdEnjoyMusic, "关闭单独听", "open");
            objVoiceCtrlHelper.SetFuzzyWord(strCmdEnjoyMusic, "打开单独听", "close");

            ///Sleep Timer
            objVoiceCtrlHelper.SetFuzzyWord(strCmdSleepTimer, "睡眠关机", "show");
            objVoiceCtrlHelper.SetFuzzyWord(strCmdSleepTimer, "睡眠关机时间设置", "show");
            objVoiceCtrlHelper.SetFuzzyWord(strCmdSleepTimer, "我要设置睡眠关机", "show");
            objVoiceCtrlHelper.SetFuzzyWord(strCmdSleepTimer, "我要设置睡眠关机时间", "show");
            objVoiceCtrlHelper.SetFuzzyWord(strCmdSleepTimer, "我要设置定时关机", "show");

            objVoiceCtrlHelper.SetFuzzyWord(strCmdSleepTimer, "关闭", "Close");
            objVoiceCtrlHelper.SetFuzzyWord(strCmdSleepTimer, "close", "Close");
            objVoiceCtrlHelper.SetFuzzyWord(strCmdSleepTimer, "关闭睡眠关机", "Close");
            objVoiceCtrlHelper.SetFuzzyWord(strCmdSleepTimer, "请除睡眠关机时间", "Close");
            objVoiceCtrlHelper.SetFuzzyWord(strCmdSleepTimer, "关闭睡眠关机设置", "Close");
            objVoiceCtrlHelper.SetFuzzyWord(strCmdSleepTimer, "请除睡眠关机设置", "Close");

            objVoiceCtrlHelper.SetFuzzyWord(strCmdSleepTimer, "10分钟", "10");
            objVoiceCtrlHelper.SetFuzzyWord(strCmdSleepTimer, "10 mini", "10");
            objVoiceCtrlHelper.SetFuzzyWord(strCmdSleepTimer, "设置10分钟后睡眠关机", "10");
            objVoiceCtrlHelper.SetFuzzyWord(strCmdSleepTimer, "设置十分钟后睡眠关机", "10");

            objVoiceCtrlHelper.SetFuzzyWord(strCmdSleepTimer, "20分钟", "20");
            objVoiceCtrlHelper.SetFuzzyWord(strCmdSleepTimer, "20 mini", "20");
            objVoiceCtrlHelper.SetFuzzyWord(strCmdSleepTimer, "设置20分钟后睡眠关机", "20");
            objVoiceCtrlHelper.SetFuzzyWord(strCmdSleepTimer, "设置二十分钟后睡眠关机", "20");

            objVoiceCtrlHelper.SetFuzzyWord(strCmdSleepTimer, "30分钟", "30");
            objVoiceCtrlHelper.SetFuzzyWord(strCmdSleepTimer, "30 mini", "30");
            objVoiceCtrlHelper.SetFuzzyWord(strCmdSleepTimer, "半个钟", "30");
            objVoiceCtrlHelper.SetFuzzyWord(strCmdSleepTimer, "设置30分钟后睡眠关机", "30");
            objVoiceCtrlHelper.SetFuzzyWord(strCmdSleepTimer, "设置三十分钟后睡眠关机", "30");
            objVoiceCtrlHelper.SetFuzzyWord(strCmdSleepTimer, "设置半个小时后睡眠关机", "30");

            objVoiceCtrlHelper.SetFuzzyWord(strCmdSleepTimer, "60分钟", "60");
            objVoiceCtrlHelper.SetFuzzyWord(strCmdSleepTimer, "60 mini", "60");
            objVoiceCtrlHelper.SetFuzzyWord(strCmdSleepTimer, "1个钟", "60");
            objVoiceCtrlHelper.SetFuzzyWord(strCmdSleepTimer, "设置60分钟后睡眠关机", "60");
            objVoiceCtrlHelper.SetFuzzyWord(strCmdSleepTimer, "设置六十分钟后睡眠关机", "60");
            objVoiceCtrlHelper.SetFuzzyWord(strCmdSleepTimer, "设置1个小时后睡眠关机", "60");

            objVoiceCtrlHelper.SetFuzzyWord(strCmdSleepTimer, "90分钟", "90");
            objVoiceCtrlHelper.SetFuzzyWord(strCmdSleepTimer, "90 mini", "90");
            objVoiceCtrlHelper.SetFuzzyWord(strCmdSleepTimer, "1个半小时", "90");
            objVoiceCtrlHelper.SetFuzzyWord(strCmdSleepTimer, "1个半钟", "90");
            objVoiceCtrlHelper.SetFuzzyWord(strCmdSleepTimer, "设置90分钟后睡眠关机", "90");
            objVoiceCtrlHelper.SetFuzzyWord(strCmdSleepTimer, "设置九十分钟后睡眠关机", "90");

            objVoiceCtrlHelper.SetFuzzyWord(strCmdSleepTimer, "120分钟", "120");
            objVoiceCtrlHelper.SetFuzzyWord(strCmdSleepTimer, "120 mini", "120");
            objVoiceCtrlHelper.SetFuzzyWord(strCmdSleepTimer, "2个小时", "120");
            objVoiceCtrlHelper.SetFuzzyWord(strCmdSleepTimer, "2个钟", "120");
            objVoiceCtrlHelper.SetFuzzyWord(strCmdSleepTimer, "设置120分钟后睡眠关机", "120");
            objVoiceCtrlHelper.SetFuzzyWord(strCmdSleepTimer, "设置一百二十分钟后睡眠关机", "120");
            objVoiceCtrlHelper.SetFuzzyWord(strCmdSleepTimer, "设置1个半小时后睡眠关机", "120");

            objVoiceCtrlHelper.SetFuzzyWord(strCmdSleepTimer, "180分钟", "180");
            objVoiceCtrlHelper.SetFuzzyWord(strCmdSleepTimer, "180 mini", "180");
            objVoiceCtrlHelper.SetFuzzyWord(strCmdSleepTimer, "3个小时", "180");
            objVoiceCtrlHelper.SetFuzzyWord(strCmdSleepTimer, "3个钟", "180");
            objVoiceCtrlHelper.SetFuzzyWord(strCmdSleepTimer, "设置180分钟后睡眠关机", "180");
            objVoiceCtrlHelper.SetFuzzyWord(strCmdSleepTimer, "设置一百八十分钟后睡眠关机", "180");
            objVoiceCtrlHelper.SetFuzzyWord(strCmdSleepTimer, "设置3个小时后睡眠关机", "180");

            ///World Clock
            objVoiceCtrlHelper.SetFuzzyWord(strCmdWorldClock, "World Clock", "show");
            objVoiceCtrlHelper.SetFuzzyWord(strCmdWorldClock, "世界时钟", "show");
            objVoiceCtrlHelper.SetFuzzyWord(strCmdWorldClock, "打开世界时钟", "show");
            objVoiceCtrlHelper.SetFuzzyWord(strCmdWorldClock, "查看世界时钟", "show");

            ///SoundBar Ready
            objVoiceCtrlHelper.SetFuzzyWord(strCmdSoundBarReady, "声霸通", "switch");
            objVoiceCtrlHelper.SetFuzzyWord(strCmdSoundBarReady, "打开声霸通", "Open");
            objVoiceCtrlHelper.SetFuzzyWord(strCmdSoundBarReady, "设置声霸通", "switch");
            objVoiceCtrlHelper.SetFuzzyWord(strCmdSoundBarReady, "关闭声霸通", "Close");
            objVoiceCtrlHelper.SetFuzzyWord(strCmdSoundBarReady, "打开声霸通", "Open");

            ///Fastcast
            objVoiceCtrlHelper.SetFuzzyWord(strCmdFastcast, "Fastcast", "Fastcast");
            objVoiceCtrlHelper.SetFuzzyWord(strCmdFastcast, "极速投屏", "switch");
            objVoiceCtrlHelper.SetFuzzyWord(strCmdFastcast, "打开极速投屏", "Open");
            objVoiceCtrlHelper.SetFuzzyWord(strCmdFastcast, "关闭极速投屏", "Close");

            ///Night Mode
            objVoiceCtrlHelper.SetFuzzyWord(strCmdNightMode, "Night Mode", "Night Mode");
            objVoiceCtrlHelper.SetFuzzyWord(strCmdNightMode, "夜间模式", "switch");
            objVoiceCtrlHelper.SetFuzzyWord(strCmdNightMode, "打开夜间模式", "Open");
            objVoiceCtrlHelper.SetFuzzyWord(strCmdNightMode, "关闭夜间模式", "Close");

            ///create cmd string
            return objVoiceCtrlHelper.GetQuerySceneJson(strSceneName);
        }

        @Override
        public void onTextFilter(Intent arg0) {
            // TODO Auto-generated method stub

        }

        public File getSharedPrefsFile(String name) {
            // TODO Auto-generated method stub
            return null;
        }
    }

    private void execWirelessDisplay(Intent intent) {
    }

    private void execEnjoyMusic(Intent intent) {
        mSharedPreferences.edit().putInt("position", 1).apply();
        setFocus(1);
        closePopWindow();
        intent = new Intent();
        intent.setClass(SmartKeyActivity.this, SingleListenerRemindActivity.class);
        startActivity(intent);
        SmartKeyActivity.this.finish();
    }

    private void execSleepTimer(String strSleepTime) {
        mSharedPreferences.edit().putInt("position", 2).apply();
        setFocus(2);
        int iSleepTime = 0;

        if (strSleepTime.equals("show")) {
            showSleepPopupWindow();
        } else if (strSleepTime.equals("Close")) {
            iSleepTime = 0;
            text_off.requestFocus();
        } else if (strSleepTime.equals("10")) {
            iSleepTime = 1;
            text_min10.requestFocus();
        } else if (strSleepTime.equals("20")) {
            iSleepTime = 2;
            text_min20.requestFocus();
        } else if (strSleepTime.equals("30")) {
            iSleepTime = 3;
            text_min30.requestFocus();
        } else if (strSleepTime.equals("60")) {
            iSleepTime = 4;
            text_min60.requestFocus();
        } else if (strSleepTime.equals("90")) {
            iSleepTime = 5;
            text_min90.requestFocus();
        } else if (strSleepTime.equals("120")) {
            iSleepTime = 6;
            text_min120.requestFocus();
        } else if (strSleepTime.equals("180")) {
            iSleepTime = 7;
            text_min180.requestFocus();
        }
        else {
            Log.v(TAG, "unkonw param:" + strSleepTime);
            return;
        }
        Log.v(TAG, "sleep time param:" + strSleepTime + " sleep mode:" + iSleepTime);
        setSleepMode(iSleepTime);
    }

    private void execWorldClock(Intent intent) {
        mSharedPreferences.edit().putInt("position", 3).apply();
        setFocus(3);
        closePopWindow();
        LauncherApp(Util.TIME_CLOCK);
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }

    private void execSoundBarReady(String strAction) {
        mSharedPreferences.edit().putInt("position", 4).apply();
        setFocus(4);
        closePopWindow();
        if (strAction.equals("switch")) {
            if (APIBridge.isSoundbarOn()) {
                soundbar_ready_icon.setBackgroundResource(R.drawable.smart_soundbar);
                APIBridge.setSoundbarToggle(false);
//                arrow_up.setVisibility(View.INVISIBLE);

            } else {
                APIBridge.setSoundbarToggle(true);
                soundbar_ready_icon.setBackgroundResource(R.drawable.smart_soundbar_focus);
//                if (APIBridge.isSupportedSoundbar()) {
//                    arrow_up.setVisibility(View.VISIBLE);
//                } else {
//                    arrow_up.setVisibility(View.INVISIBLE);
//                }
            }
        } else if (strAction.equals("Open")) {
            if (!APIBridge.isSoundbarOn()) {
                APIBridge.setSoundbarToggle(true);
                soundbar_ready_icon.setBackgroundResource(R.drawable.smart_soundbar_focus);
//                if (APIBridge.isSupportedSoundbar()) {
//                    arrow_up.setVisibility(View.VISIBLE);
//                } else {
//                    arrow_up.setVisibility(View.INVISIBLE);
//                }
            }
        } else if (strAction.equals("Close")) {
            if (APIBridge.isSoundbarOn()) {
                soundbar_ready_icon.setBackgroundResource(R.drawable.smart_soundbar);
                APIBridge.setSoundbarToggle(false);
//                arrow_up.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void execFastcast(Intent intent) {
        mSharedPreferences.edit().putInt("position", 5).apply();
        setFocus(5);
        closePopWindow();
        LauncherApp(Util.FAST_CAST);
    }

    private void execNightMode(Intent intent) {
    }

    private void closePopWindow() {
        if (mPopWindow != null && mPopWindow.isShowing()) {
            mPopWindow.dismiss();
        }
    }

    private void popupSubItem(String item) {
        if (item.equals("sleep")) {
            //closePopWindow();
            //execSleepTimer("show");
            //如果要实现此功能,只能通过Handle的方式,需要先等ROOT ACTIVITY READY后才能显示睡眠关机的
            //SUB item
        } else if (item.equals("listen")) {
            execEnjoyMusic(null);
        } else if (item.equals("soundbar")) {
            execSoundBarReady("Open");
        } else if (item.equals("clock")) {
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setClassName("com.apps.timeclock", "com.apps.timeclock.TimeClockActivity");
            startActivity(intent);
            finish();
        }
    }
    ///voice control code end
}
