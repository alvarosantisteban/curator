package com.alvarosantisteban.berlincurator;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

/**
 * Displays all the information of an event.
 * 
 * @author Alvaro Santisteban 2013 - alvarosantisteban@gmail.com
 *
 */
public class EventActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.event, menu);
		return true;
	}

}
