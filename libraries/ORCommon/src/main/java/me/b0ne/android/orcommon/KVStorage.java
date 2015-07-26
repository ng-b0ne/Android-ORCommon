package me.b0ne.android.orcommon;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by b0ne on 14/01/06.
 */
public class KVStorage {

    private final String mSharedPrefName;
    private final SharedPreferences mSharedPreferences;

    private static final String DEFAULT_SHARE_PREF_NAME = "default_kvstorage_share_pref_name";

    /**
     * インスタンスの再生、Contextが必須
     * @param context
     */
    public KVStorage(Context context) {
        mSharedPrefName = DEFAULT_SHARE_PREF_NAME;
        mSharedPreferences = context.getSharedPreferences(
                mSharedPrefName, Context.MODE_PRIVATE);
    }

    /**
     * インスタンスの再生、sharedPerfNameを設定したいとき
     * @param context
     * @param sharedPrefName
     */
    public KVStorage(Context context, String sharedPrefName) {
        if (sharedPrefName == null || sharedPrefName.equals("")) {
            mSharedPrefName = DEFAULT_SHARE_PREF_NAME;
        } else {
            mSharedPrefName = sharedPrefName;
        }
        mSharedPreferences = context.getSharedPreferences(
                mSharedPrefName, Context.MODE_PRIVATE);
    }

    /**
     * SharedPreferencesのNameを取得する
     * @return
     */
    public String getSharePrefName() {
        return mSharedPrefName;
    }

    /**
     * SharedPreferencesのEditorを取得する
     * @return
     */
    private SharedPreferences.Editor getEditor() {
        return mSharedPreferences.edit();
    }

    public void saveString(String key, String value) {
        SharedPreferences.Editor editor = getEditor();
        editor.putString(key, value).commit();
    }

    public String getString(String key) {
        return mSharedPreferences.getString(key, null);
    }

    public void saveBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = getEditor();
        editor.putBoolean(key, value).commit();
    }

    public boolean getBoolean(String key) {
        return mSharedPreferences.getBoolean(key, false);
    }

    public void saveInt(String key, int value) {
        SharedPreferences.Editor editor = getEditor();
        editor.putInt(key, value).commit();
    }

    public int getInt(String key) {
        return mSharedPreferences.getInt(key, 0);
    }

    public void saveLong(String key, long value) {
        SharedPreferences.Editor editor = getEditor();
        editor.putLong(key, value).commit();
    }

    public long getLong(String key) {
        return mSharedPreferences.getLong(key, 0);
    }

    public void remove(String key) {
        getEditor().remove(key).commit();
    }

    /**
     * 保存しているデータを全て削除する
     */
    public void removeAll() {
        clearAll();
    }

    /**
     * 保存しているデータを全て削除する
     */
    public void clearAll() {
        getEditor().clear().commit();
    }

}
