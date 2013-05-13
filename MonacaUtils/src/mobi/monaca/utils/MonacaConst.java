package mobi.monaca.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.util.Log;

public class MonacaConst {
	private static Map<String, String> constMap = null;
	private static final String SCHEME = "https://";

	private static final String KEY_DOMAIN = "domain";
	private static final String KEY_ENV = "env";
	private static final String KEY_ISCUSTOM ="isCustom";

	private MonacaConst() {};

	public static String getPushRegistrationAPIUrl(Context context, String pushProjectId) {
		try {
			return SCHEME + getConst(context, KEY_DOMAIN) + "/v1/push/register/" + URLEncoder.encode(pushProjectId, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	public static String getPushUnegistrationAPIUrl(Context context, String pushProjectId) {
		try {
			return SCHEME + getConst(context, KEY_DOMAIN) + "/v1/push/unregister/" + URLEncoder.encode(pushProjectId, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	public static String getIsCustom(Context context) {
		return getConst(context, KEY_ISCUSTOM);
	}

	private static String getConst(Context context, String key) {
		if (context == null) {
			throw new NullPointerException();
		}
		if (constMap == null) {
			loadMonacaConst(context);
		}
		return constMap.get(key);
	}

	private static void loadMonacaConst(Context context) {
		constMap = new HashMap<String, String>();
		constMap.put(KEY_DOMAIN, "api.monaca.mobi");
		constMap.put(KEY_ENV, "prod");
		constMap.put(KEY_ISCUSTOM, "false");

		Resources res = context.getResources();
		int id = res.getIdentifier("monaca_const", "xml", context.getPackageName());
		if (id != 0) {
			Log.d("MonacaConst", "found monaca_const.xml");
			XmlResourceParser xml = res.getXml(id);
			int eventType = -1;
			while (eventType != XmlResourceParser.END_DOCUMENT) {
				String node = xml.getName();
				if (eventType == XmlResourceParser.START_TAG && node.equalsIgnoreCase("monaca")) {
					int count = xml.getAttributeCount();
					for (int i = 0; i < count; i++) {
						constMap.put(xml.getAttributeName(i), xml.getAttributeValue(i));
					}
					// only uses first Monaca tag
					eventType = XmlResourceParser.END_DOCUMENT;
				} else {
					try {
						eventType = xml.next();
					} catch (XmlPullParserException e) {
						eventType = XmlResourceParser.END_DOCUMENT;
						e.printStackTrace();
					} catch (IOException e) {
						eventType = XmlResourceParser.END_DOCUMENT;
						e.printStackTrace();
					}
				}
			}
		}
	}
}
