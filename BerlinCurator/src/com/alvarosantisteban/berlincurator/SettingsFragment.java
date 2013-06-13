package com.alvarosantisteban.berlincurator;

import java.util.Set;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.MultiSelectListPreference;
import android.preference.PreferenceFragment;

/**
 * Creates the settings 
 * 
 * @author Alvaro Santisteban 2013 - alvarosantisteban@gmail.com
 *
 */
public class SettingsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener {

	 public static final String KEY_PREF_SYNC_CONN = "multilist";
	
	 /**
	  * Loads the preferences from the XML file
	  */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("ON CREATE THE SETTINGS FRAGMENT");
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
        
        findPreference("about").setIntent(new Intent(getActivity(), AboutActivity.class));
    }

	/**
	 * Listens for a change in the preferences
	 */
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		System.out.println("GUEEEEE ON SHARED PREFERENCE CHANGED");
		if (key.equals(KEY_PREF_SYNC_CONN)) {
        	System.out.println("key multilist changed");
            MultiSelectListPreference connectionPref = (MultiSelectListPreference) findPreference(key);
            // Get the new values
            Set<String> set = sharedPreferences.getStringSet(key, null);
            // Set the new values on the list
    		MainActivity.webs = set.toArray(new String[0]);
    		// Set the values on the list
            connectionPref.setValues(set);
        }
		
	}
	
	@Override
	public void onResume() {
	    super.onResume();
        System.out.println("ON Resume THE SETTINGS FRAGMENT");
	    // Set up a listener whenever a key changes
	    getPreferenceScreen().getSharedPreferences()
	            .registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onPause() {
	    super.onPause();
        System.out.println("ON Pause THE SETTINGS FRAGMENT");
	    // Unregister the listener whenever a key changes
	    getPreferenceScreen().getSharedPreferences()
	            .unregisterOnSharedPreferenceChangeListener(this);
	}

}
