package com.odm.smartkey.receiver;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.odm.smartkey.SmartKeyActivity;


public class MyReceiver extends BroadcastReceiver {
	private String TAG = "MyReceiver";
	private String action;
	private SharedPreferences sharedPreferences = null;
	private SharedPreferences.Editor editor = null;
	private ActivityManager mActivityManager = null;

	@Override
	public void onReceive(Context context, Intent intent) {
		action = intent.getAction();
		Logcat.i(TAG, "action:" + action);
		sharedPreferences = context.getSharedPreferences(Util.ON_OFF_MODE_NAME,
				Context.MODE_PRIVATE);
		editor = sharedPreferences.edit();

		if (action.equals(Util.SLEEP_ACTION)) { // ˯�߹ػ�
			int time_id = intent.getIntExtra(Util.SLEEP_ID, 0);
			Log.i("zhuke", "time_id:"+time_id);
			editor.putInt(Util.SLEEP_ID, time_id);
			editor.commit();
		} else if (Util.KEY_PRESS.equals(action)) { // ң����
			String type = intent.getStringExtra("Type");
			Logcat.i(TAG, "pressed key :" + type);
			if ("SmartKey".equals(type)) {
				if (isAppOnForeground(context)) {

					System.exit(0);
				} else {
					Intent intent_open = new Intent(context,
							SmartKeyActivity.class);
					intent_open.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(intent_open);
				}
			}

		} else if (Util.ACTION_SINGLE_LISTEN_OFF.equals(action)) {
			// sk = new SimKey();
			// sk.setkey(-1357930454);
		}
	}

	/**
	 * 
	 * @Description: �жϵ�ǰӦ���Ƿ��ڶ�ջ�Ķ���
	 * @return
	 */
	public boolean isAppOnForeground(Context context) {
		mActivityManager = ((ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE));

		String mPackageName = context.getPackageName();

		List<RunningTaskInfo> tasksInfo = mActivityManager.getRunningTasks(1);
		if (tasksInfo.size() > 0) {
			if (mPackageName.equals(tasksInfo.get(0).topActivity
					.getPackageName())) {
				return true;
			}
		}
		return false;
	}

}
