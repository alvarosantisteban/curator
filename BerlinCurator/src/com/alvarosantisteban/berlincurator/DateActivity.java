package com.alvarosantisteban.berlincurator;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Locale;

import android.annotation.SuppressLint;
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
	Calendar calendar = Calendar.getInstance();
	
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
		//String[] htmls = intent.getStringArrayExtra(MainActivity.EXTRA_HTML);
		
		// Get the actual date
		date = (TextView) findViewById(R.id.date);
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.GERMAN);
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
		
		// TODO �Quizas mejor ya abajo? Ver si se ahorra algo haciendolo aqui o vuelve a generar el array de Event
		
		//loadEvents(htmls);
		loadEvents();
		
		
		// Expand all Groups
		//expandAll();
		
		// Collapse all groups
		//collapseAll();
		
		// Expand the groups with events
		expandGroupsWithEvents();
		
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
	 * Expand the groups with events on it
	 */
	private void expandGroupsWithEvents(){
		int count = listAdapter.getGroupCount();
		for (int i = 0; i < count; i++){
			if(listAdapter.getChildrenCount(i) > 0){
				expandableSitesList.expandGroup(i);
			}
		}
	}
	
	/**
	 * Loads the events from the different websites into out list
	 */
	private void loadEvents(){ 
		// I Heart Berlin
		for (int i=0; i<MainActivity.events.get(0).size(); i++){
			// Add the events from the I Heart Berlin site of the selected day
			if(MainActivity.events.get(0).get(i).getDay().equals(date.getText().toString())){
				addEvent("I Heart Berlin", MainActivity.events.get(0).get(i));
			}
		}
		
		// Berlin Art Parasites
		for (int i=0; i<MainActivity.events.get(1).size(); i++){
			// Add the events from the Metal Concerts site of the selected day
			if(MainActivity.events.get(1).get(i).getDay().equals(date.getText().toString())){
			//if(metalEvents[i].getDay().equals("17/05/2013")){ Used to check if it works on a day that has a concert
				addEvent("Berlin Art Parasites", MainActivity.events.get(1).get(i));
			}
		}
		
		// Metal concerts
		for (int i=0; i<MainActivity.events.get(2).size(); i++){
			// Add the events from the Metal Concerts site of the selected day
			if(MainActivity.events.get(2).get(i).getDay().equals(date.getText().toString())){
			//if(metalEvents[i].getDay().equals("17/05/2013")){ Used to check if it works on a day that has a concert
				addEvent("Metal Concerts", MainActivity.events.get(2).get(i));
			}
		}
		
		// White Trash's concerts
		for (int i=0; i<MainActivity.events.get(3).size(); i++){
			// Add the events from the White Trash site of the selected day
			if(MainActivity.events.get(3).get(i).getDay().equals(date.getText().toString())){
				addEvent("White Trashs concerts", MainActivity.events.get(3).get(i));
			}
		}

		// Koepi's events
		for (int i=0; i<MainActivity.events.get(4).size(); i++){
			// Add the events from the Metal Concerts site of the selected day
			if(MainActivity.events.get(4).get(i).getDay().equals(date.getText().toString())){
				addEvent("Koepis activities", MainActivity.events.get(4).get(i));
			}
		}
	}
	
	/**
	 * Loads the events from the different websites into out list
	 *
	private void loadEvents(String[] htmls){ 
		// I Heart Berlin
		Event[] iHeartBerlinEvents = extractEventFromIHeartBerlin(htmls[0]);
		for (int i=0; i<iHeartBerlinEvents.length; i++){
			// Add the events from the I Heart Berlin site of the selected day
			if(iHeartBerlinEvents[i].getDay().equals(date.getText().toString())){
				addEvent("I Heart Berlin", iHeartBerlinEvents[i]);
			}
		}
		
		// Berlin Art Parasites
		Event[] parasitesEvents = extractEventFromArtParasites(htmls[1]);
		for (int i=0; i<parasitesEvents.length; i++){
			// Add the events from the Metal Concerts site of the selected day
			if(parasitesEvents[i].getDay().equals(date.getText().toString())){
			//if(metalEvents[i].getDay().equals("17/05/2013")){ Used to check if it works on a day that has a concert
				addEvent("Berlin Art Parasites", parasitesEvents[i]);
			}
		}
		
		// Metal concerts
		Event[] metalEvents = extractEventFromMetalConcerts(htmls[2]);
		for (int i=0; i<metalEvents.length; i++){
			// Add the events from the Metal Concerts site of the selected day
			if(metalEvents[i].getDay().equals(date.getText().toString())){
			//if(metalEvents[i].getDay().equals("17/05/2013")){ Used to check if it works on a day that has a concert
				addEvent("Metal Concerts", metalEvents[i]);
			}
		}
		
		// White Trash's concerts
		Event[] whiteTrashEvent = extractEventFromWhiteTrash(htmls[3]);
		for (int i=0; i<whiteTrashEvent.length; i++){
			// Add the events from the White Trash site of the selected day
			if(whiteTrashEvent[i].getDay().equals(date.getText().toString())){
				addEvent("White Trashs concerts", whiteTrashEvent[i]);
			}
		}

		// Koepi's events
		Event[] koepiEvents = extractEventFromKoepi(htmls[4]);
		for (int i=0; i<koepiEvents.length; i++){
			// Add the events from the Metal Concerts site of the selected day
			if(koepiEvents[i].getDay().equals(date.getText().toString())){
				addEvent("Koepis activities", koepiEvents[i]);
			}
		}
	}
	*/
	
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
			  //Toast.makeText(getBaseContext(), "Clicked on Detail " + headerInfo.getName() + "/" + clickedEvent.getName(), Toast.LENGTH_LONG).show();
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
			  //Toast.makeText(getBaseContext(), "Child on Header " + headerInfo.getName(), Toast.LENGTH_LONG).show();
	     
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
	
}
