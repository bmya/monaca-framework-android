package mobi.monaca.framework;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mobi.monaca.framework.util.MyLog;

import org.json.JSONObject;

/**
 * URI that contains queryData as ArrayList and has script inserted html getter.
 * also able to convert query containing uri into noQueryURI
 */

public class MonacaURI {
	public static final String URL_ENCODE = "UTF-8";
	private static final String TAG = MonacaURI.class.getSimpleName();
	private URI uri;
	private ArrayList<QueryParam> queryParamsArrayList;

	public MonacaURI(String url) {
		try {
			this.uri = new URI(url);
			this.parseQuery();
		} catch (URISyntaxException e) {
			MyLog.e(TAG, "URISyntacException! : " + url);
		}
	}
	
	public static String buildUrlWithQuery(String baseUrl, JSONObject queryJson) {

		MyLog.d(TAG, "buildUrl :" + baseUrl);
		if (queryJson == null || (queryJson != null && queryJson.length() == 0)) {
			MyLog.d(TAG, "no query");
			return baseUrl;
		}

		Iterator iterator = queryJson.keys();
		String newUrl = new String(baseUrl);
		String key;

		try {
			if (new URI(baseUrl).getQuery() != null) {
				newUrl += "&";
			} else {
				newUrl += "?";
			}
		} catch (URISyntaxException e) {
			return baseUrl;
		}

		while (iterator.hasNext()) {

			key = (String)iterator.next();
			try {
				if (key != null && queryJson.isNull(key)) {
					newUrl += URLEncoder.encode(key, URL_ENCODE) + "&";
				} else if (key != null ) {
					newUrl += URLEncoder.encode(key, URL_ENCODE) + "=" + URLEncoder.encode(queryJson.optString(key), URL_ENCODE)  + "&";
				}
			} catch (UnsupportedEncodingException e) {
				MyLog.e(TAG, e.getMessage());
			}
		}
		newUrl = trimLastChar(newUrl);

		return newUrl;
	}

	public String getUrlWithQuery() {
		return uri.toString();
	}

	public String getUrlWithoutQuery() {
		if (uri.getQuery() == null) {
			return getUrlWithQuery();
		}else {
			return uri.toString().replace("?" + uri.getRawQuery(), "");
		}
	}

	public boolean hasQueryParams() {
		return queryParamsArrayList != null && !queryParamsArrayList.isEmpty();
	}

	public static String trimLastChar(String target) {
		StringBuffer sb = new StringBuffer(target);
		sb.deleteCharAt(target.length() - 1);
		target = sb.toString();
		return target;
	}

	public static String removeSpecialChar(String target) {
		String result;
		//MyLog.d(TAG,"removeSpecialChar");
		result = target.replace("\\","\\\\").replace("\"", "\\\\\"").replace("\'", "\\\\\'").replace("/", "\\/").replace("}", "\\}");
		MyLog.d(TAG, target);
		MyLog.d(TAG, result);
		return result;
	}

	public String getQueryParamsContainingHtml(String baseHtml) {
		String paramsString;

		paramsString = "<script type=\"text/javascript\">"
				+ "window.monaca = window.monaca || {};"
				+ "monaca.queryParams = monaca.queryParams || {";

		for (QueryParam q : this.queryParamsArrayList) {
			String script;
			if (q.hasValue()) {
				script = "\"" + removeSpecialChar(q.getDecodedKey()) + "\":"  + "\"" + removeSpecialChar(q.getDecodedValue()) + "\",";
			} else {
				script = "\"" + removeSpecialChar(q.getDecodedKey()) + "\":" + "null,";
			}
			paramsString += script;
		}

		paramsString = trimLastChar(paramsString);
		paramsString += "};</script>";

		String targetHtml = new String(baseHtml);
		Pattern pattern = Pattern.compile("<head.*?>", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(targetHtml);

		if (matcher.find()) {
		//	MyLog.d(TAG, "matches");
			return matcher.replaceFirst(matcher.group() + paramsString);
		}else {
		//	MyLog.d(TAG, "no matches");
			return paramsString + targetHtml;
		}
	}

	public void parseQuery() {
		if (uri.getQuery() != null) {
			//MyLog.d(TAG, "hasQuery");
			MyLog.d(TAG, "rawQuery:"+ uri.getRawQuery());
			String[] params = uri.getRawQuery().split("&");
			String[] keyAndValue;

			queryParamsArrayList = new ArrayList<QueryParam>();

			for (int i = 0; i < params.length; i++) {
				keyAndValue = params[i].split("=");
				queryParamsArrayList.add(new QueryParam(keyAndValue, params[i]));
			}
		} else {
			//MyLog.d(TAG, "noQuery");
			queryParamsArrayList = null;
		}
	}

	public class QueryParam {
		private String key;
		private String value;

		public boolean hasValue() {
			return (value != null);
		}

		public String getDecodedKey() {
			try {
				return URLDecoder.decode(key, MonacaURI.URL_ENCODE);
			} catch (UnsupportedEncodingException e) {
				return key;
			}
		}

		public String getDecodedValue() {
			try {
				return URLDecoder.decode(value, MonacaURI.URL_ENCODE);
			} catch (UnsupportedEncodingException e) {
				return value;
			}
		}

		public QueryParam(String[] keyAndValue, String params) {
			try{
				if (keyAndValue == null || keyAndValue.length < 2) {
					//MyLog.d(TAG, "not splitted KeyAndValue");
					//MyLog.d(TAG, params);
					this.key = params;
					this.value = null;
				} else {
					this.key = keyAndValue[0];
					this.value = keyAndValue[1];
				}
			} catch (Exception e) {
				this.key = null;
				this.value = null;
			}
		//	MyLog.d(TAG, keyAndValue[0] + " : " + keyAndValue[1]);
		}
	}

}
