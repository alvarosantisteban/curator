package com.alvarosantisteban.berlincurator;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.Menu;
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
}
