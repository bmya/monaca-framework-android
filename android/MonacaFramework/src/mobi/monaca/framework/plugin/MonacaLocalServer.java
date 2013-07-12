package mobi.monaca.framework.plugin;

import java.io.File;
import java.io.IOException;

import mobi.monaca.framework.MonacaPageActivity;
import android.app.Activity;
import fi.iki.elonen.SimpleWebServer;

public class MonacaLocalServer {
	private static final String TAG = MonacaLocalServer.class.getSimpleName();
	private MonacaPageActivity activity;
	private String mAppAssetPath;
	private String fullPath;
	private SimpleWebServer webServer;

	public MonacaLocalServer(Activity activity, String rootDir, int port) {
		
		this.activity = (MonacaPageActivity) activity;

		mAppAssetPath = this.activity.getAppAssetsPath();

		fullPath = mAppAssetPath + "/" + removeLeadingSlash(rootDir);
		File fullPathFile = new File(fullPath); 
		webServer = new SimpleWebServer(null, port, fullPathFile, true);
	}
	
	public void start() throws IOException{
		webServer.start();
	}
	
	public void stop(){
		webServer.stop();
	}

	public String getServerRoot(){
		return fullPath;
	}

	private String removeLeadingSlash(String string) {
		if(string.startsWith("/")){
			return string.replaceFirst("/", "");
		}
		return string;
	}
}
