package com.alvarosantisteban.berlincurator;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Displays all the information of an event.
 * 
 * @author Alvaro Santisteban 2013 - alvarosantisteban@gmail.com
 *
 */
public class EventActivity extends Activity {
	
	RelativeLayout eventLayout;
	TextView name;
	TextView day;
	TextView time;
	TextView link;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event);
		
		// Get the intent with the Event
		Intent intent = getIntent();
		Event event = (Event)intent.getSerializableExtra(DateActivity.EXTRA_EVENT);
		
		eventLayout = (RelativeLayout) findViewById(R.id.eventLayout);
		name = (TextView)findViewById(R.id.events_name);
		day = (TextView)findViewById(R.id.date);
		time = (TextView)findViewById(R.id.events_time);
		link = (TextView)findViewById(R.id.events_link);
		
		// TODO Probably I will have to distinguise what I set depending where does the Event come from (which group = website)
		name.setText(event.getName());
		day.setText(event.getDay());
		link.setText(event.getLink().toString());
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.event, menu);
		return true;
	}

}
