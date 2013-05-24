package mobi.monaca.framework.bootloader;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class BootloaderPreferences {
    protected Context context;
    protected SharedPreferences bootloaderPreferences;

    protected static final String BOOTLOADER_PREFERENCES_NAME = "bootloader";
    protected static final String APP_VERSION_CODE_KEY = "app_ver_code";
    protected static final String KEY_LAST_UPDATE = "last_update_time";

    public BootloaderPreferences(Context context) {
        this.context = context;
        bootloaderPreferences = context.getSharedPreferences(
                BOOTLOADER_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public void clear() {
        bootloaderPreferences.edit().clear().commit();
    }

    public void updateLastPackageUpdatedTime() {
    	bootloaderPreferences.edit().putLong(KEY_LAST_UPDATE, getCurrentPackageLastUpdated()).commit();
    }

    public boolean isAppPackageUpdated() {
		long savedLastUpdate = bootloaderPreferences.getLong(KEY_LAST_UPDATE, 0);
		return getCurrentPackageLastUpdated() != savedLastUpdate;
    }

    private long getCurrentPackageLastUpdated() {
    	try {
			PackageInfo p = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_META_DATA);
			return p.lastUpdateTime;
    	} catch (Exception e) {
    		throw new RuntimeException(e);
    	}
    }

    public void saveAppVersionCode(String versionCode) {
        bootloaderPreferences.edit()
                .putString(APP_VERSION_CODE_KEY, "" + versionCode).commit();
    }

    public String getAppVersionCode() {
        return bootloaderPreferences.getString(APP_VERSION_CODE_KEY, "");
    }

}