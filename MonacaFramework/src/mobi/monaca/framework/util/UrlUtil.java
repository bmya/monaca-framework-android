package mobi.monaca.framework.util;

import java.io.File;
import java.net.URI;

import android.content.Context;


public class UrlUtil {
	public static final String DELIMITTER = "/assets/www/";

    static public boolean isUrl(String url) {
        return url.startsWith("http://") || url.startsWith("https://");
    }


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

    static public String resolve(String url, String relativePath) {
        try {
            String result = new URI(url).resolve(relativePath).toString();
            return result.contains("..") ? result.replace("../", "") : result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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

    static public String normalize(String url) {
        try {
            return new URI(url).normalize().toString().replace("../", "");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String cutHostInUri(String uri){
		if(uri.contains(DELIMITTER)){
			int delimitterIndex = uri.indexOf(DELIMITTER) + "/assets/".length();
			uri = uri.substring(delimitterIndex);
		}
		return uri;
	}
}
