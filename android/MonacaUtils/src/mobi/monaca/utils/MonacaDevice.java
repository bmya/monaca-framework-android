package mobi.monaca.utils;

import java.util.UUID;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.Settings.Secure;


public class MonacaDevice {
	public static String getDeviceId(Context context) {
		String id = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
		if(id == null){
			id = getIDUsingUUID(context);
		}
		String hashedString = SHA1Util.toHashedString(id);
		return hashedString;
	}

	private static String getIDUsingUUID(Context context) {
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
		String deviceId = sharedPref.getString("device_id", null);
		if(deviceId == null){
			deviceId = UUID.randomUUID().toString();
			sharedPref.edit().putString("device_id", deviceId).commit();
		}
		return deviceId;
	}
}
