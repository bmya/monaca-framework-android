package mobi.monaca.framework.bootloader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mobi.monaca.framework.util.MyLog;
import mobi.monaca.utils.MyAsyncTask;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Handler;

/** This class make Monaca application running on local file. */
public class LocalFileBootloader {
    protected static final String BOOTLOADER_PREFERENCE_NAME = "bootloader";
    protected static final String BOOTLOADER_FILES_PREFERENCE_NAME = "bootloader_files";
	private static final String TAG = LocalFileBootloader.class.getSimpleName();

    protected Context context;
    protected Runnable success, fail;
    protected String dataDirPath;
    protected BootloaderPreferences bootloaderPreferences;
    public static boolean mShouldExtractAssets;

    protected LocalFileBootloader(Context context, Runnable success,
            Runnable fail) {
        this.context = context;
        this.success = success;
        this.fail = fail;
        this.bootloaderPreferences = new BootloaderPreferences(context);
        dataDirPath = context.getApplicationInfo().dataDir;
    }

    /** Get application's version string. */
    protected String getAppliationVersionCode() {
        try {
            return ""
                    + context.getPackageManager().getPackageInfo(
                            context.getPackageName(),
                            PackageManager.GET_META_DATA).versionCode;
        } catch (NameNotFoundException e) {
            return "0";
        }
    }

    protected void execute() {
    	MyLog.d(TAG, "using localFileBootloader");
        new BootloaderTask().execute();
    }

    public static void setup(Context context, Runnable runner, Runnable fail) {
    	MyLog.d(TAG, "using LocalFileBootloader");
        new LocalFileBootloader(context, runner, fail).execute();
    }

    protected boolean isAppVersionUpdated() {
        boolean updated = !bootloaderPreferences.getAppVersionCode().equals(getAppliationVersionCode());
        return updated;
    }

    protected boolean needsInitialization() {
    	MyLog.d(TAG, "AppVersionUpdated , AppPackageUpdated : " + isAppVersionUpdated()+ ", " + bootloaderPreferences.isAppPackageUpdated());
    	return isAppVersionUpdated() || bootloaderPreferences.isAppPackageUpdated();
    }

    protected List<String> getAssetsFileList() {
        ArrayList<String> result = new ArrayList<String>();
        aggregateAssetsFileList("www", result);

        Collections.sort(result);
        return result;
    }

    public static boolean needToUseLocalFileBootloader() {
	    return Build.VERSION.SDK_INT == 14 || Build.VERSION.SDK_INT == 15;
	}

    /**
     * returns new inputStream from assetPath.
     * if ver4.0.x, uses LocalFileBootloader.
     * else uses getAssets()
     * @param path this method removes file:///android_asset/ and file://android_asset/
     * @param context
     * @return
     * @throws IOException
     */
    public static InputStream openAsset(Context context, String path) throws IOException {
        String newPath = path.replaceFirst("(file:///android_asset/)|(file://android_asset/)", "");
        if(mShouldExtractAssets){
        	return loadFileUsingBootloader(context, path);
        }
    	if (needToUseLocalFileBootloader()) {
    		return loadFileUsingBootloader(context, newPath);
    	} else {
    		return context.getAssets().open(newPath);
    	}
    }

	private static InputStream loadFileUsingBootloader(Context context, String newPath) throws FileNotFoundException, IOException {
		String filePath = newPath.startsWith("file:///data/") ? newPath.substring(8) : context.getApplicationInfo().dataDir + "/" + newPath;
		File localAssetFile = new File(filePath);
		if (localAssetFile.exists()) {
			return new FileInputStream(localAssetFile);
		} else {
			return context.getAssets().open(newPath);
		}
	}

    public static String getFullPath(Context context, String path){
    	return context.getApplicationInfo().dataDir + "/" + path;
    }

	protected void aggregateAssetsFileList(String prefix, ArrayList<String> result) {
        try {
            for (String path : context.getAssets().list(prefix)) {
            	MyLog.d(TAG, "pathCheck :" + prefix + "/" + path);
                    if (existAsset(prefix + "/" + path)) {
                        result.add(prefix + "/" + path);
                    } else {
                        // may be directory
                        aggregateAssetsFileList(prefix + "/" + path, result);
                    }
            }
        } catch (Exception e) {
            MyLog.e(getClass().getSimpleName(), e.getMessage());
            throw new RuntimeException(e);
        }
    }

    protected List<String> getApplicationLocalFileList() {
        ArrayList<String> temp = new ArrayList<String>();
        File dir = new File(context.getApplicationInfo().dataDir + "/www");
        dir.mkdir();
        LocalFileUtil.aggregateApplicationLocalFileList(new File(
                context.getApplicationInfo().dataDir + "/www"), temp);

        ArrayList<String> result = new ArrayList<String>();
        int start = context.getApplicationInfo().dataDir.length() + 1;
        for (String path : temp) {
            result.add(path.substring(start));
        }

        Collections.sort(result);
        return result;
    }

    protected static String join(List<String> list) {
        StringBuffer buffer = new StringBuffer();

        for (String elt : list) {
            buffer.append(elt);
            buffer.append(":");
        }

        String temp = buffer.toString();
        if(temp.length() == 0){
        	MyLog.e(TAG, "Warning: temp.length=0");
        	return "";
        }

        return temp.substring(0, temp.length() - 1);
    }

    protected boolean existAsset(String path) {
        try {
            InputStream stream = context.getAssets().open(path);
            stream.close();
        } catch (Exception e) {
        	MyLog.e(TAG, path + " not exist");
            return false;
        }
        return true;
    }

    /** Copy assets to local data directory. */
    protected void copyAssetToLocal(String path) {
    	MyLog.d(TAG, "copyAssetToLocal()");
        byte[] buffer = new byte[1024 * 4];

        File file = new File(context.getApplicationInfo().dataDir + "/" + path);
        file.getParentFile().mkdirs();
        try {
            OutputStream output = new FileOutputStream(file);
            InputStream input = context.getAssets().open(path);

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

    /** Clean local application files and the application preferences. */
    protected void clean() {
        bootloaderPreferences.clear();
        cleanFiles(context.getApplicationInfo().dataDir + "/www");
    }

    protected void cleanFiles(String path) {
        File file = new File(path);
        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                cleanFiles(child.getAbsolutePath());
            }
            file.delete();
        } else {
            file.delete();
        }
    }

    protected class BootloaderTask extends MyAsyncTask<Void, Void, Boolean> {

        protected Handler handler = new Handler();
        protected ProgressDialog loadingDialog = null;

        @Override
        protected Boolean doInBackground(Void ...a) {
            boolean needInit = true;

            try {
                needInit = needsInitialization();
                MyLog.v(TAG, "needInit = " + needInit);
            } catch (AbortException e) {
                MyLog.e(getClass().getSimpleName(), "bootloader task aborted." + e);
                return false;
            } catch (RuntimeException e) {
                MyLog.e(getClass().getSimpleName(), "bootloader task fail." + e);
                return false;
            }

            try {
                if (needInit) {
                	showProgressDialog();
                    clean();
                    MyLog.v(TAG, "assetFiles size=" + getAssetsFileList().size());
                    for (String path : getAssetsFileList()) {
                        copyAssetToLocal(path);
                    }
                    bootloaderPreferences
                            .saveAppVersionCode(getAppliationVersionCode());
                    bootloaderPreferences.updateLastPackageUpdatedTime();
                }
            } catch (AbortException e) {
                MyLog.e(getClass().getSimpleName(),
                        "local file bootloader abort." + e.getMessage());
                return false;
            } catch (RuntimeException e) {
                MyLog.e(getClass().getSimpleName(), "local file bootloader fail" + e.getMessage());
                return false;
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean isSuccess) {
            dismissProgressDialog();
            if (isSuccess) {
                success.run();
            } else {
                fail.run();
            }
        }

        protected void showProgressDialog() {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    loadingDialog = new ProgressDialog(context);
                    loadingDialog.setMessage("Loading...");
                    loadingDialog.show();
                    loadingDialog.setCancelable(false);
                }
            });
        }

        protected void dismissProgressDialog() {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (loadingDialog != null) {
                        loadingDialog.dismiss();
                        loadingDialog = null;
                    }
                }
            });
        }

        protected void showAbortAlert() {
            showAlert("インストールに失敗しました。ディスクの容量を増やして再度実行してください。");
        }

        protected void showAlert(final String message) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    new AlertDialog.Builder(context).setTitle("")
                            .setMessage(message).setCancelable(true);
                }
            });
        }
    }

}
