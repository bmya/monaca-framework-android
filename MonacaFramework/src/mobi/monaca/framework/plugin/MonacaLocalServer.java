package mobi.monaca.framework.plugin;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import mobi.monaca.framework.MonacaPageActivity;
import mobi.monaca.framework.util.MyLog;
import android.app.Activity;
import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.Response.Status;

public class MonacaLocalServer extends NanoHTTPD {
	private static final String TAG = MonacaLocalServer.class.getSimpleName();
	private MonacaPageActivity activity;
	private String mAppAssetPath;
	private String fullPath;

	public MonacaLocalServer(Activity activity, String rootDir, int port) {
		super(port);
		this.activity = (MonacaPageActivity) activity;
		this.setTempFileManagerFactory(new MonacaFileManagerFactory());
		mAppAssetPath = this.activity.getAppAssetsPath();
		if(mAppAssetPath.equalsIgnoreCase("assets")){
			fullPath = removeLeadingSlash(rootDir);
		}else{
			fullPath = mAppAssetPath + "/" + removeLeadingSlash(rootDir);
		}
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

	private class MonacaFileManagerFactory implements TempFileManagerFactory {

		@Override
		public TempFileManager create() {
			return new MonacaTempFileManager();
		}

	}

	private class MonacaTempFileManager implements TempFileManager {
		private final String TAG = MonacaTempFileManager.class.getSimpleName();
		private final List<TempFile> tempFiles = new ArrayList<NanoHTTPD.TempFile>();
		String temporaryDirectory = activity.getCacheDir().getAbsolutePath();

		@Override
		public TempFile createTempFile() throws Exception {
			DefaultTempFile tempFile = new DefaultTempFile(temporaryDirectory);
			tempFiles.add(tempFile);
			MyLog.i(TAG, "created temp file");
			return tempFile;
		}

		@Override
		public void clear() {
			if (!tempFiles.isEmpty()) {
				MyLog.v(TAG, "cleaning up");
			}
			for (TempFile file : tempFiles) {
                try {
                    System.out.println("   "+file.getName());
                    file.delete();
                } catch (Exception ignored) {
                	ignored.printStackTrace();
                }
            }
            tempFiles.clear();
		}

	}

	@Override
	public Response serve(String uri, Method method, Map<String, String> header, Map<String, String> parms, Map<String, String> files) {
		MyLog.v(TAG, "serve uri:" + uri + ", file:" + files);
		try {
			
			String guessedMimeType = URLConnection.guessContentTypeFromName(uri);
			MyLog.v(TAG, "guessed mime type: " + guessedMimeType);
			InputStream data = null;
			if(mAppAssetPath.equalsIgnoreCase("assets")){
				data = activity.getAssets().open(fullPath + uri);				
			}else{
				data = new FileInputStream(fullPath + uri);
			}
			
			return new Response(Status.ACCEPTED, guessedMimeType, data);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return new Response(Status.NOT_FOUND, MIME_PLAINTEXT, "Content not found!");
		} catch (IOException e) {
			e.printStackTrace();
			return new Response(Status.NOT_FOUND, MIME_PLAINTEXT, "Content not found!");
			
		}

	}

}
