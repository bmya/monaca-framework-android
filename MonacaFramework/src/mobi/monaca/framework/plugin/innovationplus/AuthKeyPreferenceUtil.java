package mobi.monaca.framework.plugin.innovationplus;

import android.content.Context;
import android.content.SharedPreferences;

public class AuthKeyPreferenceUtil {
	private static final String PREF = "ipp_auth_pref";
	private static final String KEY_AUTH ="ipp_auth_key";
	private AuthKeyPreferenceUtil() {};

	/**
	 * get last saved auth key.
	 * @param context
	 * @return if no auth key saved, return empty String
	 */
	public static String getAuthKey(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
		return pref.getString(KEY_AUTH, "");
	}

	/**
	 * save auth key to preference. old key is overridden
	 * @param context
	 * @param authKey
	 */
	public static void saveAuthKey(Context context, String authKey) {
		SharedPreferences pref = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
		pref.edit().putString(KEY_AUTH, authKey).commit();
	}

	/**
	 * clear saved auth key.
	 * @param context
	 */
	public static void removeAuthKey(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
		pref.edit().clear().commit();
	}
}
