package mobi.monaca.framework.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;

import mobi.monaca.framework.bootloader.LocalFileBootloader;
import android.content.Context;

public class AssetUriUtil {
    public static boolean existsAsset(Context context, String assetPath) {
		try {
			InputStream stream = LocalFileBootloader.openAsset(context, assetPath);
			stream.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

    public static String assetToString(Context context, String assetName) throws IOException {
    		InputStream in = LocalFileBootloader.openAsset(context, assetName);
    	    BufferedReader reader =
    	        new BufferedReader(new InputStreamReader(in, "UTF-8"/* 文字コード指定 */));
    	    StringBuffer buf = new StringBuffer();
    	    String str;
    	    while ((str = reader.readLine()) != null) {
    	            buf.append(str);
    	            buf.append("\n");
    	    }
    	    return buf.toString();
    	}
}
