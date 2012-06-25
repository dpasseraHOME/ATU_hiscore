package com.smashingboxes.utilities;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;

public class ManageSharedPrefs {
	
	private static final String PREFS_NAME = "myPrefs";
	private static final String LOG_TAG = "log_ManageSharedPrefs";

	public static void setPreferences(Context context, Map<String, String> pref) {
		// get reference to shared preferences and open editing
		SharedPreferences myPrefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor prefsEditor = myPrefs.edit();
		
		// iterate map and set preferences
		Set<?> set = pref.entrySet();
		Iterator<?> i = set.iterator();
		
		while(i.hasNext()) {
			Map.Entry m = (Map.Entry)i.next();
			String key = (String)m.getKey();
			String value = (String)m.getValue();
			
			prefsEditor.putString(key, value);
		}
		
		//commit preference edits
		prefsEditor.commit();
	}
	
	public static String getPreference(Context context, String prefName) {
		SharedPreferences myPrefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		return myPrefs.getString(prefName, null);
	}
	
}
