package mobi.monaca.framework.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;

import mobi.monaca.framework.bootloader.AbortException;
import mobi.monaca.framework.bootloader.LocalFileBootloader;
import android.content.Context;

public class AssetUtils {
	private static final String TAG = AssetUtils.class.getSimpleName();

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
		BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"/* 文字コード指定 */));
		StringBuffer buf = new StringBuffer();
		String str;
		while ((str = reader.readLine()) != null) {
			buf.append(str);
			buf.append("\n");
		}
		return buf.toString();
	}
	
	protected void aggregateAssetsFileList(Context context, String prefix, ArrayList<String> result) {
        try {
            for (String path : context.getAssets().list(prefix)) {
            	MyLog.d(TAG, "pathCheck :" + prefix + "/" + path);
                    if (existsAsset(context, prefix + "/" + path)) {
                        result.add(prefix + "/" + path);
                    } else {
                        // may be directory
                        aggregateAssetsFileList(context, prefix + "/" + path, result);
                    }
            }
        } catch (Exception e) {
            MyLog.e(getClass().getSimpleName(), e.getMessage());
            throw new RuntimeException(e);
        }
    }
	
	protected void copyAssetToLocal(Context context, String assetPath) {
    	MyLog.d(TAG, "copyAssetToLocal()");
        byte[] buffer = new byte[1024 * 4];

        File file = new File(context.getApplicationInfo().dataDir + "/" + assetPath);
        file.getParentFile().mkdirs();
        try {
            OutputStream output = new FileOutputStream(file);
            InputStream input = context.getAssets().open(assetPath);

            int n = 0;
            while (-1 != (n = input.read(buffer))) {
                output.write(buffer, 0, n);
            }

            input.close();
            output.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new AbortException(e);
        }
    }
}
