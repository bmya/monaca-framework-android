package mobi.monaca.framework.bootloader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import mobi.monaca.framework.util.MyLog;
import android.content.Context;

public class Md5Util {

    private static final String TAG = Md5Util.class.getSimpleName();

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

    /**
     * this method removes high-order 4bit of each words.
     * (eg. hoge -> a0ea1fa04a5798be, normally ea703e7aa1efda0064eaa507d9e8ab7e)
     *
     * @param source
     * @return
     * @see Md5Util#toMD5(String)
     */
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
                sb.append(Integer.toHexString(hash[i] & 0x0F)); //remove high-order 4bit
            }

            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * convert string to md5 hashed string (eg. hoge -> ea703e7aa1efda0064eaa507d9e8ab7e)
     * @param source
     * @return
     * @see Md5Util#md5(String)
     */
    public static String toMD5(String source) {
    	MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			MyLog.e(TAG, e.getMessage());
		}
    	byte[] digest = null;
		try {
			digest = md.digest(source.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			MyLog.e(TAG, e.getMessage());
		}
    	StringBuffer buf = new StringBuffer();
    	for(int i= 0; i< digest.length; i++){
    	      int d = digest[i];
    	      if (d < 0) {
    	        d += 256;
    	      }
    	      if (d < 16) {
    	        buf.append("0");
    	      }
    	      buf.append(Integer.toString(d, 16));
    	      }
    	return buf.toString();
    }
}
