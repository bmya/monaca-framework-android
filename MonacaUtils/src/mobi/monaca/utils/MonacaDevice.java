package mobi.monaca.utils;

import java.util.UUID;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class MonacaDevice {
	public static String getDeviceId(Context context){
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
		String deviceId = sharedPref.getString("device_id", null);
		if(deviceId == null){
			deviceId = UUID.randomUUID().toString();
			sharedPref.edit().putString("device_id", deviceId).commit();
		}
		
		return deviceId;
	}
}
