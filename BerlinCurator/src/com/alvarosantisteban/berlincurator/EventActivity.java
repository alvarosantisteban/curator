package com.alvarosantisteban.berlincurator;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.Menu;
import android.view.MenuItem;
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
	//TextView image;
	
	String tag = "EventActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event);
		
		
		// Get the intent with the Event
		Intent intent = getIntent();
		Event event = (Event)intent.getSerializableExtra(DateActivity.EXTRA_EVENT);
		
		eventLayout = (LinearLayout) findViewById(R.id.eventLayout);
		name = (TextView)findViewById(R.id.events_name);
		day = (TextView)findViewById(R.id.date);
		time = (TextView)findViewById(R.id.events_time);
		link = (TextView)findViewById(R.id.events_link);
		description = (TextView)findViewById(R.id.events_description);
		//image = (TextView)findViewById(R.id.events_image_in_text);
		
		name.setMovementMethod(LinkMovementMethod.getInstance());
		name.setText(Html.fromHtml(event.getName()));
		
		day.setText(event.getDay());
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
