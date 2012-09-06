package mobi.monaca.framework.bootloader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.content.Context;

public class Md5Util {

    public static String getAssetFileHash(Context context, String path) {
        InputStream asset = null;
        String hash = "";
        try {
            asset = context.getAssets().open(path);
            hash = md5(asset);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (asset != null) {
                    asset.close();
                }
            } catch (Exception e) {
            }
        }

        return hash;
    }

    public static String getLocalFileHash(String path) {
        InputStream stream = null;
        String hash = "";
        try {
            stream = new FileInputStream(new File(path));
            hash = md5(stream);
        } catch (IOException e) {
            throw new AbortException(e);
        } finally {
            try {
                if (stream != null) {
                    stream.close();
                }
            } catch (Exception e) {
            }
        }

        return hash;
    }

    public static String md5(InputStream stream) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
            byte[] buff = new byte[4096];
            int len = 0;
            while ((len = stream.read(buff, 0, buff.length)) >= 0) {
                digest.update(buff, 0, len);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        byte[] hash = digest.digest();

        StringBuffer sb = new StringBuffer();
        int cnt = hash.length;
        for (int i = 0; i < cnt; i++) {
            sb.append(Integer.toHexString(hash[i] & 0x0F));
        }

        return sb.toString();

    }

    public static String md5(String source) {
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");

            md5.reset();
            md5.update(source.getBytes("UTF-8"));
            byte[] hash = md5.digest();

            StringBuffer sb = new StringBuffer();
            int cnt = hash.length;
            for (int i = 0; i < cnt; i++) {
                sb.append(Integer.toHexString(hash[i] & 0x0F));
            }

            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

}
