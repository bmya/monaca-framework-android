package mobi.monaca.framework;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

import mobi.monaca.framework.template.TemplateResource;
import mobi.monaca.framework.util.MyLog;
import android.util.Log;

public class FileTemplateResource implements TemplateResource {

	private static final String TAG = FileTemplateResource.class.getSimpleName();

	@Override
	public boolean exists(String path) {
		Log.v(TAG, "exists?" + path);
		return new File(path).exists();
	}

	@Override
	public Reader get(String path) {
		String goodPath = path.replaceFirst("file://", "");
		Log.v(TAG, "get() path=" + path + ", goodPath:" + goodPath);
		try {
			return new FileReader(goodPath);
		} catch (FileNotFoundException e) {
			throw new RuntimeException("FILE NOT FOUND:" + cutHostInUri(goodPath), e);
		}
	}
	
	public static String cutHostInUri(String uri){
		if(uri.contains("/assets/www/")){
			int delimitterIndex = uri.indexOf("/assets/www/") + "/assets/".length();
			uri = uri.substring(delimitterIndex);
		}
		return uri;
	}

	@Override
	public String resolve(String path, String from) {
		Log.v(TAG, "resolve() path=" + path + ", from:" + from);
		String unResolvedPath = from + "/../" + path;
		return getResolvedUrl(unResolvedPath);
	}
	
	protected String getResolvedUrl(String url) {
		if (url.startsWith("file://")) {
			try {
				return new File(url.substring("file://".length())).getCanonicalPath();
			} catch (Exception e) {
			}
		}
		return url;
	}

}
