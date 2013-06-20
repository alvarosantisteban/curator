package com.alvarosantisteban.berlincurator;


import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.google.android.gms.maps.MapView;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


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
	MapView mapita;
	CheckBox interestingCheck;
	
	String tag = "EventActivity";
	
	/**
	 * The event being displayed
	 */
	Event event;
	
	public static final String EXTRA_DATE = "date";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event);
		
		
		// Get the intent with the Event
		Intent intent = getIntent();
		event = (Event)intent.getSerializableExtra(DateActivity.EXTRA_EVENT);
		
		// Enable the app's icon to act as home
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		eventLayout = (LinearLayout) findViewById(R.id.eventLayout);
		interestingCheck = (CheckBox)findViewById(R.id.checkbox_interesting);
		name = (TextView)findViewById(R.id.events_name);
		day = (TextView)findViewById(R.id.date);
		time = (TextView)findViewById(R.id.events_time);
		link = (TextView)findViewById(R.id.events_link);
		description = (TextView)findViewById(R.id.events_description);
		addToCalendar = (TextView)findViewById(R.id.events_add_to_calendar);

		//mapita = new MapView(this);
		
		// Get the date
		day.setText(event.getDay().trim());
		
		// Get the state of the check
		interestingCheck.setChecked(getFromSP("cb" +event.getId()));
		//interestingCheck.setChecked(event.isTheEventMarked()); 
		
		// Get the name
		name.setMovementMethod(LinkMovementMethod.getInstance());
		name.setText(Html.fromHtml(event.getName()));
		
		// Get the hour, if any
		if(!event.getHour().equals("")){
			time.setText(Html.fromHtml(event.getHour()));
			//time.setText(event.getHour());
		}
		
		// Get the description, if any
		if (!event.getDescription().equals("")){
			description.setMovementMethod(LinkMovementMethod.getInstance());
			description.setText(Html.fromHtml(event.getDescription()));
		}
		
		// Get the the links, if any
		if (!event.getLink().equals("")){
			String[] links = event.getLinks();
			
			link.setText(links[0] + " \n " +links[1]);
			// Make the link clickable. The links have no html, so we make them clickable this way
			Linkify.addLinks(link, Linkify.WEB_URLS);
			link.setMovementMethod(LinkMovementMethod.getInstance());
			
		}
		
		// Get the location, if any
		if(!event.getLocation().equals("")){
			//location.setText(event.getLocation());
		}

		// Set the listener to add a event on the google calendar
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
	
	/**
	 * Listener for the checkbox that determines if an event is interesting to a user or not.
	 * 
	 * @param view the CheckBox clicked
	 */
	public void onCheckboxClicked(View view) {
	    // Is the view now checked?
	    boolean checked = ((CheckBox) view).isChecked();
	    event.markEvent(checked);
	    // Check which checkbox was clicked
	    switch(view.getId()) {
	        case R.id.checkbox_interesting:
	            if (checked){
	            	Toast toast = Toast.makeText(getBaseContext(), "You marked this event as interesting", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.TOP, 0, MainActivity.actionBarHeight);
					toast.show();
					saveInSp("cb"+event.getId(), true);
	            }else{
	            	saveInSp("cb"+event.getId(), false);
	            }
	            break;
	    }
	}
	
	/**
	 * Saves the state of the checkbox into the SharedPreferences
	 * 
	 * @param key the key to be saved 
	 * @param value the value to be saved
	 */
	private void saveInSp(String key, boolean value){
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("PROJECT_NAME", android.content.Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }
	
	/**
	 * Returns the state of the checkbox with the key given as parameter
	 * 
	 * @param key the checkbox key
	 * @return the state of the checkbox 
	 */
	private boolean getFromSP(String key){
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("PROJECT_NAME", android.content.Context.MODE_PRIVATE);
        return preferences.getBoolean(key, false);
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
        if (item.getItemId() == R.id.menu_calendar) {
			Intent i2 = new Intent(this, CalendarActivity.class);
			startActivity(i2);
		} else if (item.getItemId() == android.R.id.home) {
			// app icon in action bar clicked; go to the DateActivity
            Intent intent = new Intent(this, DateActivity.class);
            intent.putExtra(EXTRA_DATE, day.getText().toString());
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
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
