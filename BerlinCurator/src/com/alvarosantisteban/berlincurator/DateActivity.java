package com.alvarosantisteban.berlincurator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Displays a list with the events for a concrete day organized by the origin of the website and the time.
 * 
 * @author Alvaro Santisteban 2013 - alvarosantisteban@gmail.com
 * 
 *  implements OnClickListener
 *
 */
public class DateActivity extends Activity{

	private TextView date;
	ExpandableListView expandableSitesList;
	public static final String EXTRA_EVENT = "com.alvarosantisteban.berlincurator.event";
	
	/**
	 * A LinkedHashMap with the a String as key and a HeaderInfo as value
	 */
	private LinkedHashMap<String, HeaderInfo> websites = new LinkedHashMap<String, HeaderInfo>();
	/**
	 * An ArrayList with the HeaderInfo of each website
	 */
	private ArrayList<HeaderInfo> websitesList = new ArrayList<HeaderInfo>();
	 
	private ListAdapter listAdapter;
	
	final Context context = this;
		
	/**
	 *  Loads the elements from the resources, gets the data from the mainActitivy and calls the parsers to extract the information that
	 *  will be shown.
	 *  
	 * @param savedInstanceState
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_date);
		
		Intent intent = getIntent();
		String[] htmls = intent.getStringArrayExtra(MainActivity.EXTRA_HTML);
		
		// Get the actual date
		date = (TextView) findViewById(R.id.date);
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.GERMAN);
		Calendar calendar = Calendar.getInstance();
		date.setText(dateFormat.format(calendar.getTime()));
		
		// Get the names of the sites
		Resources res = this.getResources();
		String[] sitesNames = res.getStringArray(R.array.sites_array); 
		// Populate the ArrayList "websitesList" and the LinkedHashMap "websites"
		createHeaderGroups(sitesNames);
		
		expandableSitesList = (ExpandableListView) findViewById(R.id.expandableSitesList);
		// Create the adapter by passing the ArrayList data
		listAdapter = new ListAdapter(DateActivity.this, websitesList);
		// Attach the adapter to the expandableList
		expandableSitesList.setAdapter(listAdapter);
		
		// Add some random data to the expandableList
		loadData();
		
		// Add some extra data to the expandableList
		addEvent("I Heart Berlin", htmls[0]);
		addEvent("Berlin Art Parasites", htmls[1]);
		// Add the events from the Metal Concerts site
		// TODO Add only the ones that correspond to the day "date"
		Event[] metalEvents = extractEventFromMetalConcerts(htmls[2]);
		for (int i=0; i<metalEvents.length; i++){
			addEvent("Metal Concerts", metalEvents[i]);
		}
		addEvent("White Trashs concerts", htmls[3]);
		addEvent("Koepis activities", htmls[4]);
		
		// Expand all Groups
		expandAll();
		
		// Collapse all groups
		collapseAll();
		
		// Listener for the events
		expandableSitesList.setOnChildClickListener(myEventClicked);
		// Listener for the sites
		expandableSitesList.setOnGroupClickListener(myListGroupClicked);
	}
	
	/**
	 * Expand all groups
	 */
	private void expandAll() {
		int count = listAdapter.getGroupCount();
		for (int i = 0; i < count; i++){
			expandableSitesList.expandGroup(i);
		}
	}
	
	/**
	 * Collapse all groups
	 */
	private void collapseAll() {
		int count = listAdapter.getGroupCount();
		for (int i = 0; i < count; i++){
			expandableSitesList.collapseGroup(i);
		}
	}
	
	/**
	 * Loads some initial faked data into out list
	 */
	private void loadData(){ 
		  addEvent("I Heart Berlin","Kino Night");
		  addEvent("I Heart Berlin","Party party");
		  addEvent("I Heart Berlin","Running out of shorts");
		  addEvent("Metal Concerts","Manilla Road");
		  addEvent("Metal Concerts","Dream Theater");
	}
	
	/**
	 * Add a event to its corresponding group (site where it comes from)
	 * 
	 * @param websiteName String with the name of the website from where the event comes from
	 * @param eventName String with the name of the event to be attached
	 * @return the position of group where the event was added
	 */
	private int addEvent(String websiteName, String eventName){
		int groupPosition = 0;
	   
		// Check in the hash map if the group already exists
		HeaderInfo headerInfo = websites.get(websiteName);
		// Add the group if doesn't exists
		if(headerInfo == null){
			headerInfo = new HeaderInfo();
			headerInfo.setName(websiteName);
			websites.put(websiteName, headerInfo);
			websitesList.add(headerInfo);
		}
	 
		// Get the children (events) for the group
		ArrayList<Event> eventsList = headerInfo.getEventsList();
		// Get the size of the children list
		int listSize = eventsList.size();
		// Add to the counter
		listSize++;
	 
		// Create a new event
		Event newEvent = new Event();
		newEvent.setSequence(String.valueOf(listSize));
		newEvent.setName(eventName);
		// Add it to its list of events
		eventsList.add(newEvent);
		// Update the site with the "new" eventsList
		headerInfo.setEventsList(eventsList);
		 
		//find the group position inside the list
		groupPosition = websitesList.indexOf(headerInfo);
		return groupPosition;
	}
	/**
	 * Add a event to its corresponding group (site where it comes from)
	 * 
	 * @param websiteName String with the name of the website from where the event comes from
	 * @param newEvent The event to be attached
	 * @return the position of group where the event was added
	 */
	private int addEvent(String websiteName, Event newEvent){
		int groupPosition = 0;
	   
		// Check in the hash map if the group already exists
		HeaderInfo headerInfo = websites.get(websiteName);
		// Add the group if doesn't exists
		if(headerInfo == null){
			headerInfo = new HeaderInfo();
			headerInfo.setName(websiteName);
			websites.put(websiteName, headerInfo);
			websitesList.add(headerInfo);
		}
	 
		// Get the children (events) for the group
		ArrayList<Event> eventsList = headerInfo.getEventsList();
		// Get the size of the children list
		int listSize = eventsList.size();
		// Add to the counter
		listSize++;
	 
		// Set the sequence for the event
		newEvent.setSequence(String.valueOf(listSize));
		// Add it to its list of events
		eventsList.add(newEvent);
		// Update the site with the "new" eventsList
		headerInfo.setEventsList(eventsList);
		 
		//find the group position inside the list
		groupPosition = websitesList.indexOf(headerInfo);
		return groupPosition;
	}
	
	/**
	 * The child listener for the events
	 */
	private OnChildClickListener myEventClicked =  new OnChildClickListener() {
		 
		  public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
	    
			  // Get the group header 
			  HeaderInfo headerInfo = websitesList.get(groupPosition);
			  // Get the child info
			  Event clickedEvent =  headerInfo.getEventsList().get(childPosition);
			  // Display it or do something with it
			  Toast.makeText(getBaseContext(), "Clicked on Detail " + headerInfo.getName() + "/" + clickedEvent.getName(), Toast.LENGTH_LONG).show();
			  Intent intent = new Intent(context, EventActivity.class);
			  intent.putExtra(EXTRA_EVENT, clickedEvent);
			  startActivity(intent);
			  return false;
		  }
	};
	
	/**
	 * The group listener for the sites
	 */
	private OnGroupClickListener myListGroupClicked =  new OnGroupClickListener() {
		 
		  public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
	    
			  // Get the group header
			  HeaderInfo headerInfo = websitesList.get(groupPosition);
			  // Display it or do something with it
			  Toast.makeText(getBaseContext(), "Child on Header " + headerInfo.getName(), Toast.LENGTH_LONG).show();
	     
			  return false;
		  }
	   
	};
	
	/**
	 * Populates the {@link websites} and the {@link websitesList} by creating {@link HeaderInfo} using the array of names of sites passed
	 * in the parameter.
	 * 
	 * @param sitesNames the names of the sites which contain the events
	 */
	private void createHeaderGroups(String[] sitesNames) {
		for (int i=0; i<sitesNames.length; i++){
			HeaderInfo headerInfo = new HeaderInfo();
			headerInfo.setName(sitesNames[i]);
			websites.put(sitesNames[i], headerInfo);
			websitesList.add(headerInfo);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.date, menu);
		return true;
	}

	/**
	 * Creates a set of events from the html of the Metal Concerts website. 
	 * Each Event has name, day and a link.
	 * 
	 * @param theHtml the String containing the html from the Metal Concerts website
	 * @return an array of Event with the name, day and links set
	 */
	private Event[] extractEventFromMetalConcerts(String theHtml){  
		String myPattern = "<p class=\"konzerte\">"; //<p class=\"konzerte\">.*?</p>
		String[] result = theHtml.split(myPattern);
		/*
		for (int i=0; i<result.length; i++){
			System.out.println("result[i]:"+result[i]);
		}*/
		
		// Throw away the first entry of the array because it does not contain a concert
		Event[] events = new Event[result.length-1]; 
		for (int i=1; i<result.length; i++){
			// Separate up to the "@"
			String[] twoParts = result[i].split("@");
			// Separate the date and the name of the band
			String[] dateAndName = twoParts[0].split("\\. ");
			Event event = new Event();
			event.setName(dateAndName[1]);
			event.setDay(dateAndName[0]);
			
			// Remove useless code
			String htmlLink = twoParts[1].replaceFirst("</p>", "");
			// Check if there is a link
			if(htmlLink.charAt(0) == '<'){
				// Remove useless code
				htmlLink = htmlLink.replaceFirst("</a>", "");
				String[] pureLink = htmlLink.split("\""); // Get link
				String[] concertPlace = htmlLink.split(">"); // Get name
				event.setLink(pureLink[1]);
				event.setDescription("The concert will take place at the " +concertPlace[1]);
			}else{
				String concertPlace = htmlLink;
				event.setDescription("The concert will take place at the " +concertPlace);
			}
			events[i-1] = event;
		}
		return events;
    }
	
	/*
	public void onClick(View v) {
	 
		switch (v.getId()) {
		//add entry to the List
			case R.id.add:
	 
				Spinner spinner = (Spinner) findViewById(R.id.department);
				String department = spinner.getSelectedItem().toString();
				EditText editText = (EditText) findViewById(R.id.product);
				String product = editText.getText().toString();
				editText.setText("");
				    
				//add a new item to the list
				int groupPosition = addProduct(department,product);
				//notify the list so that changes can take effect
				listAdapter.notifyDataSetChanged();
				       
				//collapse all groups
				collapseAll();
				//expand the group where item was just added
				myList.expandGroup(groupPosition);
				//set the current group to be selected so that it becomes visible
				myList.setSelectedGroup(groupPosition);
				    
				break;
	 
	   // More buttons go here (if any) ...
		}
	}
*/
	
	/*
	 * 
	 * USING PATTERNS 
	 * 
    Pattern p = Pattern.compile(myPattern,Pattern.DOTALL);
    Matcher m = p.matcher(example);
    String[] result = null;

    if ( m.matches() ){
        Log.d("Matcher", "PATTERN MATCHES!");
    	System.out.println("PATTERN MATCHES!");
    	result = p.split(example);
    	System.out.println(result.length);
    	System.out.println(result.toString());
    	System.out.println("Start:" +m.start());
    	System.out.println("End:" +m.end());
    } else{
    	System.out.println("PATTERN DOES NOT MATCH");
        Log.d("MATCHER", "PATTERN DOES NOT MATCH!");
    }*/
	
}
