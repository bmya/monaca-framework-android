package mobi.monaca.framework.util;

import java.io.File;

import android.content.Context;


public class UrlUtil {
	public static final String DELIMITTER = "/assets/www/";
	public static final String EMBEDDING_HASH ="#embedding";

	static public String getUIFileUrl(String url) {
		if (url.endsWith(".html")) {
			return url.substring(0, url.length() - 4) + "ui";
		}

		if (url.endsWith(".htm")) {
			return url.substring(0, url.length() - 3) + "ui";
		}

		return null;
	}

	static public boolean isMonacaUri(Context context, String uri) {
		return uri.startsWith("file:///android_asset/") || uri.startsWith("file://" + context.getApplicationInfo().dataDir);
	}

	static public boolean isEmbedding(String uri) {
		return uri.endsWith(EMBEDDING_HASH);
	}

	static public String getResolvedUrl(String url) {
		if (url.startsWith("file://")) {
			try {
				return "file://" + new File(url.substring("file://".length())).getCanonicalPath();
			} catch (Exception e) {
			}
		}
		return url;
	}

    public static String cutHostInUri(String uri){
		if(uri.contains(DELIMITTER)){
			int delimitterIndex = uri.indexOf(DELIMITTER) + "/assets/".length();
			uri = uri.substring(delimitterIndex);
		}
		return uri;
	}
}
