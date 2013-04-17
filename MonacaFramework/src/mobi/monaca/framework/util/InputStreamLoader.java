package mobi.monaca.framework.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import mobi.monaca.framework.bootloader.LocalFileBootloader;
import android.content.Context;
import android.util.Log;

public class InputStreamLoader {

	private static final String TAG = InputStreamLoader.class.getSimpleName();

	public static InputStream loadLocalFile(String path) throws FileNotFoundException {
		return new FileInputStream(new File(path));
	}

	public static InputStream loadAssetFile(Context context, String path) throws IOException {
		return LocalFileBootloader.openAsset(context, path);
	}

}
