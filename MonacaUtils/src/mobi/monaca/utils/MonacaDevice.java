package mobi.monaca.utils;


public class MonacaDevice {
	public static String getDeviceId() {
		String id = android.provider.Settings.Secure.ANDROID_ID;
		return SHA1Util.toHashedString(id);

		/* old implementation
		//SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
		//String deviceId = sharedPref.getString("device_id", null);
		//if(deviceId == null){
		//	deviceId = UUID.randomUUID().toString();
		//	sharedPref.edit().putString("device_id", deviceId).commit();
		//}
		//
		//return deviceId;
		*/
	}
}
