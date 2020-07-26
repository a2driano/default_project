package com.studio.project.data.prefs;

import android.content.Context;
import android.preference.PreferenceManager;

public class BasePreferences {

	public void putString(Context context, String key, String value) {
		PreferenceManager.getDefaultSharedPreferences(context)
				.edit()
				.putString(key, value)
				.apply();
	}

	public String getString(Context context, String key) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString(key, "");
	}

	public String getString(Context context, String key, String defaultStr) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString(key, defaultStr);
	}

	public void putLong(Context context, String key, long value) {
		PreferenceManager.getDefaultSharedPreferences(context)
				.edit()
				.putLong(key, value)
				.apply();
	}

	public long getLong(Context context, String key) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getLong(key, 0L);
	}

	public long getLong(Context context, String key, long def) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getLong(key, def);
	}

	public void putBoolean(Context context, String key, boolean value) {
		PreferenceManager.getDefaultSharedPreferences(context)
				.edit()
				.putBoolean(key, value)
				.apply();
	}

	public boolean getBoolean(Context context, String key) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean(key, false);
	}

	public boolean getBoolean(Context context, String key, boolean def) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean(key, def);
	}

	public void putFloat(Context context, String key, float value) {
		PreferenceManager.getDefaultSharedPreferences(context)
				.edit()
				.putFloat(key, value)
				.apply();
	}

	public float getFloat(Context context, String key) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getFloat(key, 0.0f);
	}

	public float getFloat(Context context, String key, float def) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getFloat(key, def);
	}

	public void putInt(Context context, String key, int value) {
		PreferenceManager.getDefaultSharedPreferences(context)
				.edit()
				.putInt(key, value)
				.apply();
	}

	public int getInt(Context context, String key) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getInt(key, 0);
	}

	public int getInt(Context context, String key, int def) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getInt(key, def);
	}

	public void removeRecord(Context context, String key) {
		PreferenceManager.getDefaultSharedPreferences(context)
				.edit()
				.remove(key)
				.apply();
	}
}
