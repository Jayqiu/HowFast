package com.threehalf.howfast.util;

import java.util.HashMap;

import android.R.bool;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class AppCache {
	private static AppCache uniqueInstance = null;
	public static final String GRID = "GRID";
	public static final String SOUND = "SOUND";
	public static final String HINT = "HINT";
	public static final String HIDDEN = "HIDDEN";
	public static final String SHAKE = "SHAKE";
	public static final String BEST = "BEST";
	public static final String GAME_MODE = "GAME_MODE";
	public static final String CHALLENGE_MODE_BEST = "CHALLENGE_MODE_BEST";

	private Context mContext;
	private String APPSTOREXML = "howfast";

	private SharedPreferences sharedPreferences;

	public AppCache(Context context) {
		this.mContext = context;
		sharedPreferences = mContext.getSharedPreferences(APPSTOREXML,
				Context.MODE_PRIVATE);
	}

	public static AppCache getInstance(Context context) {

		if (uniqueInstance == null) {
			uniqueInstance = new AppCache(context);
		}
		return uniqueInstance;
	}

	public void saveSetting(int grid, boolean sound, boolean hint,
			boolean hidden, boolean shake) {
		Editor editor = sharedPreferences.edit();
		editor.putInt(GRID, grid);
		editor.putBoolean(SOUND, sound);
		editor.putBoolean(HINT, hint);
		editor.putBoolean(HIDDEN, hidden);
		editor.putBoolean(SHAKE, shake);
		editor.commit();
		editor.clear();
	}

	public HashMap<String, Object> getSetting() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put(GRID, sharedPreferences.getInt(GRID, 3));
		map.put(GAME_MODE, sharedPreferences.getInt(GAME_MODE, 0));
		map.put(SOUND, sharedPreferences.getBoolean(SOUND, true));
		map.put(HINT, sharedPreferences.getBoolean(HINT, true));
		map.put(HIDDEN, sharedPreferences.getBoolean(HIDDEN, true));
		map.put(SHAKE, sharedPreferences.getBoolean(SHAKE, true));
		map.put(BEST, sharedPreferences.getLong(BEST, 0));
		map.put(CHALLENGE_MODE_BEST, sharedPreferences.getLong(CHALLENGE_MODE_BEST, 0));
		return map;
	}

	public void saveGrid(int grid) {
		Editor editor = sharedPreferences.edit();
		editor.putInt(GRID, grid);
		editor.commit();
		editor.clear();
	}

	public void saveBest(long best) {
		Editor editor = sharedPreferences.edit();
		editor.putLong(BEST, best);
		editor.commit();
		editor.clear();
	}

	public void saveChallengeModeBest(long best) {
		Editor editor = sharedPreferences.edit();
		editor.putLong(CHALLENGE_MODE_BEST, best);
		editor.commit();
		editor.clear();
	}

	public void saveSound(boolean sound) {
		Editor editor = sharedPreferences.edit();
		editor.putBoolean(SOUND, sound);
		editor.commit();
		editor.clear();
	}

	public void saveHint(boolean hint) {
		Editor editor = sharedPreferences.edit();
		editor.putBoolean(HINT, hint);
		editor.commit();
		editor.clear();
	}

	public void saveHidden(boolean hidden) {
		Editor editor = sharedPreferences.edit();
		editor.putBoolean(HIDDEN, hidden);
		editor.commit();
		editor.clear();
	}

	public void saveShake(boolean shake) {
		Editor editor = sharedPreferences.edit();
		editor.putBoolean(SHAKE, shake);
		editor.commit();
		editor.clear();
	}

	public void saveGameMode(int mode) {
		Editor editor = sharedPreferences.edit();
		editor.putInt(GAME_MODE, mode);
		editor.commit();
		editor.clear();
	}

}
