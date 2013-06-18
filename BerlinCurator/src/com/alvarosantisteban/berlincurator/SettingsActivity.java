package com.alvarosantisteban.berlincurator;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

/**
 * 
 * 
 * @author Alvaro Santisteban 2013 - alvarosantisteban@gmail.com
 *
 */
public class SettingsActivity extends Activity  {

	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
	    // Enable the app's icon to act as home
	    ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);

    }
	
	/**
	 * Checks which item from the menu has been clicked
	 */
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
			// app icon in action bar clicked; go home
			Intent intent = new Intent(this, MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		}
 
        return true;
    }

}
