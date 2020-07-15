package com.odm.smartkey.receiver;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class Logcat {

	private static final boolean Log_Flag = true;

	public static void i(String TAG, String Text) {
		if (Log_Flag)
			Log.i(TAG, Text);
	}

	public static void e(String TAG, String Text) {
		if (Log_Flag)
			Log.e(TAG, Text);
	}

	public static void w(String TAG, String Text) {
		if (Log_Flag)
			Log.w(TAG, Text);
	}

	public static void d(String TAG, String Text) {
		if (Log_Flag)
			Log.d(TAG, Text);
	}

	public static void v(String TAG, String Text) {
		if (Log_Flag)
			Log.v(TAG, Text);
	}

	public static void makeToast(Context context, String Text) {
		if (Log_Flag)
			Toast.makeText(context, Text, Toast.LENGTH_SHORT).show();
	}
}
