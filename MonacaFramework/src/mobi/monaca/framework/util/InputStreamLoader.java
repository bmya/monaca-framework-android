package mobi.monaca.framework.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import android.content.Context;
import android.util.Log;

public class InputStreamLoader {

    public static InputStream loadLocalFile(String path) {
        try {
            return new FileInputStream(new File(path));
        } catch (FileNotFoundException e) {
            Log.d(InputStreamLoader.class.getSimpleName(), "file not found: "
                    + path);
            return new ByteArrayInputStream(new byte[0]);
        }
    }

    public static InputStream loadAssetFile(Context context, String path) {
        try {
            return context.getAssets().open(path);
        } catch (IOException e) {
            e.printStackTrace();
            return new ByteArrayInputStream(new byte[0]);
        }
    }

}
