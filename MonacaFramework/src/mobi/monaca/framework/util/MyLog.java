package mobi.monaca.framework.util;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyLog {
	public static boolean shouldLog = true;

	public static void i(String TAG, String msg) {
		if (shouldLog) {
			Log.i(TAG, msg);
		}
	}

	public static void v(String TAG, String msg) {
		if (shouldLog) {
			Log.v(TAG, msg);
		}
	}

	public static void w(String TAG, String msg) {
		if (shouldLog) {
			Log.w(TAG, msg);
		}
	}

	public static void d(String TAG, String msg) {
		if (shouldLog) {
			Log.d(TAG, msg);
		}
	}

	public static void e(String TAG, String msg) {
		if (shouldLog) {
			Log.e(TAG, msg);
		}
	}

	// send debuglog to debbuger and server
	public static void sendBloadcastDebugLog(Context context, String broadcastMessage, String logType, String logLevel) {
		Intent broadcastIntent = new Intent("log_message_action");
		broadcastIntent.putExtra("message", broadcastMessage);
		broadcastIntent.putExtra("logType", logType);
		broadcastIntent.putExtra("logLevel", logLevel);
		context.sendBroadcast(broadcastIntent);
	}
}
