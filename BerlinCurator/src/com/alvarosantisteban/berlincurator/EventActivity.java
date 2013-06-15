package com.alvarosantisteban.berlincurator;


import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Displays all the information of an event: Day, name, hour, description and links
 * 
 * @author Alvaro Santisteban 2013 - alvarosantisteban@gmail.com
 *
 */
public class EventActivity extends Activity {
	
	LinearLayout eventLayout;
	TextView name;
	TextView day;
	TextView time;
	TextView link;
	TextView description;
	TextView addToCalendar;
	
	String tag = "EventActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event);
		
		
		// Get the intent with the Event
		Intent intent = getIntent();
		Event event = (Event)intent.getSerializableExtra(DateActivity.EXTRA_EVENT);
		
		// Enable the app's icon to act as home
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		eventLayout = (LinearLayout) findViewById(R.id.eventLayout);
		name = (TextView)findViewById(R.id.events_name);
		day = (TextView)findViewById(R.id.date);
		time = (TextView)findViewById(R.id.events_time);
		link = (TextView)findViewById(R.id.events_link);
		description = (TextView)findViewById(R.id.events_description);
		addToCalendar = (TextView)findViewById(R.id.events_add_to_calendar);
		
		name.setMovementMethod(LinkMovementMethod.getInstance());
		name.setText(Html.fromHtml(event.getName()));
		
		day.setText(event.getDay().trim());
		// Check if there is a description to show
		if (!event.getDescription().equals("")){
			description.setMovementMethod(LinkMovementMethod.getInstance());
			description.setText(Html.fromHtml(event.getDescription()));
		}
		// Check if there is a link to show
		if (!event.getLink().equals("")){
			String[] links = event.getLinks();
			
			link.setText(links[0] + " \n " +links[1]);
			// Make the link clickable. The links have no html, so we make them clickable this way
			Linkify.addLinks(link, Linkify.WEB_URLS);
			link.setMovementMethod(LinkMovementMethod.getInstance());
			
		}
		
		// Check if there is an hour to show
		if(!event.getHour().equals("")){
			time.setText(Html.fromHtml(event.getHour()));
			//time.setText(event.getHour());
		}
		
		/*
		if (!event.getImage().equals("")){
			image.setMovementMethod(LinkMovementMethod.getInstance());
			image.setText(Html.fromHtml(event.getImage()));
		}
		*/
		
		addToCalendar.setOnClickListener(new OnClickListener() {
			
			/**
			 * Adds the event to the Google calendar
			 */
			public void onClick(View v) {
				Calendar cal = new GregorianCalendar(); 
				cal.setTime(new Date()); 
				cal.add(Calendar.MONTH, 2); 
				Intent intent = new Intent(Intent.ACTION_INSERT); 
				intent.setData(Events.CONTENT_URI); 
				intent.putExtra(Events.TITLE, name.getText().toString()); 
				intent.putExtra(Events.DESCRIPTION, description.getText().toString()); 
				// If we know when will the event begin 
				if (!time.getText().toString().equals("")){
					long startEvent = getTimeInMilliseconds(day.getText().toString(),time.getText().toString());
					intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startEvent); 
				}else{
					// If not, we set it to happen during the whole day
					intent.putExtra(Events.ALL_DAY, true); 
					long startEvent = getTimeInMilliseconds(day.getText().toString(),"00:00");
					intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startEvent); 
				}
				
				startActivity(intent); 
			}

			/**
			 * Retrieves the time in milliseconds for a start time of an event on a concrete date
			 * 
			 * @param date the day when the event will take place in the format DD/MM/YYYY
			 * @param startTime the time when the event will take place in the format HH:MM
			 * @return a long with the number of milliseconds
			 */
			private long getTimeInMilliseconds(String date, String startTime) {
				int year, month, day;
				String[] dayMonthYear = date.split("/");
				day = Integer.parseInt(dayMonthYear[0]);
				month = Integer.parseInt(dayMonthYear[1]);
				year = Integer.parseInt(dayMonthYear[2]);
				
				int hour, minutes;
				String[] hourMinutes = startTime.split(":");
				hour = Integer.parseInt(hourMinutes[0]);
				minutes = Integer.parseInt(hourMinutes[1]);
				
				Calendar beginTime = Calendar.getInstance();
				beginTime.set(year, month-1, day, hour, minutes);
				// TODO TAKE CARE HERE, that month-1 could create problems
				return beginTime.getTimeInMillis();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.event, menu);
		return true;
	}
	
	/**
	 * Checks which item from the menu has been clicked
	 */
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
 
        /*
        // Goes to the settings activity
        case R.id.menu_settings:
            Intent i = new Intent(this, SettingsActivity.class);
            startActivityForResult(i, RESULT_SETTINGS);
            break;  
            */
         // Goes to the calendar activity
        case R.id.menu_calendar:
        	Intent i2 = new Intent(this, CalendarActivity.class);
        	startActivity(i2);
        	break; 
        // Goes to the Main Activity
        case android.R.id.home:
            // app icon in action bar clicked; go to the DateActivity
            Intent intent = new Intent(this, DateActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            break;
        }
 
        return true;
    }
	
	public void onStart() {
		super.onStart();
		System.out.println(tag +"In the onStart() event");
	}		   

	public void onRestart() {
		super.onRestart();
	    System.out.println(tag + "In the onRestart() event");
	}
	    
	public void onResume() {
		super.onResume();
	    System.out.println(tag +"In the onResume() event");
	}
	    
	public void onPause() {
	    super.onPause();
	    System.out.println(tag + "In the onPause() event");
	}
	    
	public void onStop() {
	    super.onStop();
	    System.out.println(tag + "In the onStop() event");
	}
	    
	public void onDestroy() {
	    super.onDestroy();
	    System.out.println(tag + "In the onDestroy() event");
	}
}
