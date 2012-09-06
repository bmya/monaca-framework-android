package mobi.monaca.util.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileDownloader {

    private static final int DOWNLOAD_BUFFER_SIZE = 4096;

    // private static final String TAG = FileDownloader.class.getSimpleName();

    public static void saveInputSteamToFile(InputStream is, File outFile)
            throws FileNotFoundException, IOException {

        // Make folders for the file
        File dir_localfile = outFile.getParentFile();
        if (!dir_localfile.exists()) {
            dir_localfile.mkdirs();
            // boolean success = dir_localfile.mkdirs();
            // MyLog.d( TAG, FileDownloader.class.getSimpleName() +
            // ". parentFolder:" + dir_localfile.getAbsolutePath() +
            // " NotExist->mkDirs. success?:" + success);
        }
        // MyLog.d( TAG, FileDownloader.class.getSimpleName() + ". outputFile:"
        // + outFile.getAbsolutePath());

        BufferedInputStream bis = new BufferedInputStream(is);
        FileOutputStream fos = new FileOutputStream(outFile);
        BufferedOutputStream bos = new BufferedOutputStream(fos,
                DOWNLOAD_BUFFER_SIZE);
        byte[] data = new byte[DOWNLOAD_BUFFER_SIZE];
        int bytesRead = 0;

        // Start download
        while ((bytesRead = bis.read(data, 0, data.length)) >= 0) {
            bos.write(data, 0, bytesRead);
        }
        // close
        bos.close();
        fos.close();
        bis.close();
    }
}
