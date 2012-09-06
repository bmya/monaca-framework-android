package mobi.monaca.framework.util;

import java.net.URI;

public class AssetUriUtil {

    public static boolean isAssetUri(String uri) {
        return uri.startsWith("file:///android_asset/");
    }

    public static String resolve(String uri, String relative) {
        try {
            return "file://" + new URI(uri).resolve(relative).getPath();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String normalize(String uri) {
        try {
            return "file://" + new URI(uri).normalize().getPath();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
