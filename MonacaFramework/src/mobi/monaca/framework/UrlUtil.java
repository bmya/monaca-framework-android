package mobi.monaca.framework;

import java.net.URI;


public class UrlUtil {
	public static final String DELIMITTER = "/assets/www/";
	
    static public boolean isUrl(String url) {
        return url.startsWith("http://") || url.startsWith("https://");
    }

    static public String resolve(String url, String relativePath) {
        try {
            String result = new URI(url).resolve(relativePath).toString();
            return result.contains("..") ? result.replace("../", "") : result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
