package mobi.monaca.framework.bootloader;

import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;

public class BootloaderPreferences {
    protected Context context;
    protected SharedPreferences bootloaderPreferences;
    protected SharedPreferences fileHashPreferences;

    protected static final String BOOTLOADER_PREFERENCES_NAME = "bootloader";
    protected static final String FILE_HASH_PREFERENCES_NAME = "file_hash";
    protected static final String APP_VERSION_CODE_KEY = "app_ver_code";
    protected static final String FILE_LIST_HASH_KEY = "file_list_hash";

    public BootloaderPreferences(Context context) {
        this.context = context;
        bootloaderPreferences = context.getSharedPreferences(
                BOOTLOADER_PREFERENCES_NAME, Context.MODE_PRIVATE);
        fileHashPreferences = context.getSharedPreferences(
                FILE_HASH_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public void clear() {
        bootloaderPreferences.edit().clear().commit();
        fileHashPreferences.edit().clear().commit();
    }

    public void saveAppVersionCode(String versionCode) {
        bootloaderPreferences.edit()
                .putString(APP_VERSION_CODE_KEY, "" + versionCode).commit();
    }

    public String getAppVersionCode() {
        return bootloaderPreferences.getString(APP_VERSION_CODE_KEY, "");
    }

    public void saveFileListHash(String hash) {
        bootloaderPreferences.edit().putString(FILE_LIST_HASH_KEY, hash)
                .commit();
    }

    public String getFileListHash() {
        return bootloaderPreferences.getString(FILE_LIST_HASH_KEY, "");
    }

    public void saveFileHashMap(Map<String, String> map) {
        SharedPreferences.Editor editor = fileHashPreferences.edit();
        for (String key : map.keySet()) {
            editor.putString(key, map.get(key));
        }
        editor.commit();
    }

    @SuppressWarnings("unchecked")
    public Map<String, String> getFileHashMap() {
        Map<String, String> map = (Map<String, String>) fileHashPreferences
                .getAll();

        if (map.keySet().size() == 0) {
            return null;
        }

        return map;
    }

}