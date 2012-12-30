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

	public static InputStream loadLocalFile(String path) {
        try {
            return new FileInputStream(new File(path));
        } catch (FileNotFoundException e) {
        	MyLog.d(InputStreamLoader.class.getSimpleName(), "file not found: "
                    + path);
            return new ByteArrayInputStream(new byte[0]);
        }
    }

    public static InputStream loadAssetFile(Context context, String path) {
        try {
        	return LocalFileBootloader.openAsset(context, path);
        } catch (IOException e) {
        	MyLog.e(TAG, e.getMessage());
            return new ByteArrayInputStream(new byte[0]);
        }
    }

}
