package mobi.monaca.framework.plugin.innovationplus;

import mobi.monaca.framework.util.MyLog;

import org.json.JSONArray;

public class Util {
	public static String[] jsonArrayToStringArray(JSONArray array) {
		if (array == null || array.length() == 0) {
			return null;
		}
		String[] strs = new String[array.length()];
		for (int i = 0; i < strs.length; i++) {
			strs[i] = (String)array.optString(i);
			MyLog.d("jsonArrayToStringArray", "added ["+ i+ "]:" + strs[i]);
		}
		return strs;
	}
}
