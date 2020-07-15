package com.odm.smartkey.bridge;

import com.mstar.android.tv.TvAudioManager;
import com.mstar.android.tv.TvCecManager;
import com.mstar.android.tv.TvTimerManager;
import com.mstar.android.tvapi.common.AudioManager;
import com.mstar.android.tvapi.common.TvManager;
import com.mstar.android.tvapi.common.exception.TvCommonException;

/*
 *  @创建者:   yangchuanzhi
 *  @创建时间:  2018/1/16 8:42
 *  @描述：    接口桥接类
 */

public class APIBridge {
    private static final String TAG = "APIBridge";

    public static final int SLEEP_TIME_OFF    = 0;
    public static final int SLEEP_TIME_10MIN  = 1;
    public static final int SLEEP_TIME_20MIN  = 2;
    public static final int SLEEP_TIME_30MIN  = 3;
    public static final int SLEEP_TIME_60MIN  = 4;
    public static final int SLEEP_TIME_90MIN  = 5;
    public static final int SLEEP_TIME_120MIN = 6;
    public static final int SLEEP_TIME_180MIN = 7;
    public static final int SLEEP_TIME_240MIN = 8;

    public static final int SOUNDBAR_STANDARD = 1;
    public static final int SOUNDBAR_MUSIC    = 2;
    public static final int SOUNDBAR_MOVIE    = 3;

    private APIBridge() {
        //工具类，私有
    }

    //-----------Night Mode-----------

    /**夜间模式*/
    public static AudioManager getBacklightApi() {
        return TvManager.getInstance().getAudioManager();
    }

    //-----------Sleep Time-----------

    /**睡眠关机*/
    public static TvTimerManager getSleepTimeApi() {
        return TvTimerManager.getInstance();
    }

    /**睡眠关机是否开启*/
    public static boolean isSleepTime() {
        //Log.d(TAG, "isSleepTime: getSleepTimeMode() = " + getSleepTimeApi().getSleepTimeMode());
        return getSleepTimeApi().getSleepTimeMode() != SLEEP_TIME_OFF;
    }

    /**设置睡眠关机*/
    public static void setSleepTime(int time) {
        getSleepTimeApi().setSleepTimeMode(time);
    }

    /**获取睡眠关机时间*/
    public static int getSleepTime() {
        return getSleepTimeApi().getSleepTimeMode();
    }

    //-----------Soundbar-----------
    //Soundbar(CEC)
    public static TvCecManager getSoundbarApi() {
        return TvCecManager.getInstance();
    }

    /**Soundbar(CEC)是否开启*/
    public static boolean isSoundbarOn() {
        return getSoundbarApi().isTLinkEnable() == 1;
    }

    /**Soundbar(CEC)开关*/
    public static void setSoundbarToggle(boolean toggle) {
        getSoundbarApi().setTLinkEnable(toggle);
    }

    /**连接的Soundbar是否支持音效调节*/
    public static boolean isSupportedSoundbar() {
        return getSoundbarApi().isSupportAudioSystemChange(0);
    }

    /**设置Soundbar音效*/
    public static void setSoundbarMode(int mode) {
        TvAudioManager.getInstance().setAudioSoundMode(mode);
    }

    /**获取Soundbar音效*/
    public static int setSoundbarMode() {
        return TvAudioManager.getInstance().getAudioSoundMode();
    }
}
