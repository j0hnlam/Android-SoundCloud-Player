package com.johnlam.cloudplayer.setting;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Setting Manager
 * 
 * @author Nov 16, 2012
 * 
 * 
 */
public class SettingManager implements ISettingConstants {

	public static final String TAG = SettingManager.class.getSimpleName();

	public static final String DOBAO_SHARPREFS = "dobao_prefs";

	public static void saveSetting(Context mContext, String mKey, String mValue) {
		SharedPreferences mSharedPreferences = mContext.getSharedPreferences(DOBAO_SHARPREFS, Context.MODE_PRIVATE);
		Editor editor = mSharedPreferences.edit();
		editor.putString(mKey, mValue);
		editor.commit();
	}

	public static String getSetting(Context mContext, String mKey, String mDefValue) {
		SharedPreferences mSharedPreferences = mContext.getSharedPreferences(DOBAO_SHARPREFS, Context.MODE_PRIVATE);
		return mSharedPreferences.getString(mKey, mDefValue);
	}

	public static boolean getFirstTime(Context mContext) {
		return Boolean.parseBoolean(getSetting(mContext, KEY_FIRST_TIME, "false"));
	}

	public static void setFirstTime(Context mContext, boolean mValue) {
		saveSetting(mContext, KEY_FIRST_TIME, String.valueOf(mValue));
	}

	public static String getLastKeyword(Context mContext) {
		return getSetting(mContext, KEY_LAST_KEYWORD, "dj");
	}

	public static void setLastKeyword(Context mContext, String mValue) {
		saveSetting(mContext, KEY_LAST_KEYWORD, mValue);
	}

}
