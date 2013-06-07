package com.alvarosantisteban.berlincurator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

/**
 * Creates the main screen, from where the user can access the Settings, load the events of the current day or 
 * go to Calendar to select another day
 * 
 * @author Alvaro Santisteban 2013 - alvarosantisteban@gmail.com
 *
 */
public class MainActivity extends Activity {

	//public static final String EXTRA_HTML = "com.alvarosantisteban.berlincurator.html";
	//public static List<List<Event>> events = (ArrayList)new ArrayList <ArrayList<Event>>();
	/**
	 * The map with the events.
	 * The key is the name of the website and the value its corresponding list of events.
	 */
	public static Map<String, List<Event>> events = (Map<String, List<Event>>)(Map<String,?>) new HashMap <String, ArrayList<Event>>();
	
	/**
	 *  Settings
	 */
	private static final int RESULT_SETTINGS = 1;
	/**
	 *  User preferences
	 */
	SharedPreferences sharedPref;
	Context context;
	/**
	 * The total set of webs where the events can be extracted
	 */
	public static String[] webs = {"I Heart Berlin", "Berlin Art Parasites", "Metal Concerts", "White Trashs concerts", "Koepis activities", "Goth Datum", "Stress Faktor"};
	
	/**
	 * The set of urls from where the html will be downloaded
	 */
   	String[] stringUrls = {"http://www.iheartberlin.de/events/",
				   			"http://www.berlin-artparasites.com/recommended",
				   			"http://berlinmetal.lima-city.de/index.php/index.php?id=start",
				   			"http://www.whitetrashfastfood.com/events/",
				   			"http://www.koepi137.net/eventskonzerte.php",
				   			"http://www.goth-city-radio.com/dsb/dates.php",
				   			"http://stressfaktor.squat.net/termine.php?display=7",};
   	/**
   	 * The progress bar for downloading and extracting the events
   	 */
	ProgressBar loadProgressBar;
	/**
	 * The button that triggers the download and extraction of events
	 */
    Button loadButton;
	
	/**
	 * Loads the elements from the resources
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		System.out.println("--------------- BEGIN ------------");
		
		context = this;
		loadButton = (Button) findViewById(R.id.loadButton);
		loadProgressBar = (ProgressBar)findViewById(R.id.progressLoadHtml);		
		// Get the default shared preferences
		sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		
		// This is suppose to be used to clear the data from shared preferences
		/*
		Editor editor = sharedPref.edit();
		editor.clear();
		editor.commit();
		 */
		
		// Check which sites are meant to be shown
		Set<String> set = sharedPref.getStringSet("multilist", new HashSet<String>(Arrays.asList(webs)));
		webs = set.toArray(new String[0]);

		/*
		 * To check that the websites are there
		 * */
		System.out.println();
		for(int i=0;i<webs.length;i++){
			System.out.print(webs[i] + " / ");
		}
		System.out.println();
		
		
		/*
		calendarText.setOnClickListener(new OnClickListener() {
			
			// Goes to the Calendar activity
			public void onClick(View v) {
				Intent intent = new Intent(context, CalendarActivity.class);
				startActivity(intent);
			}
		});
		*/
		
		loadButton.setOnClickListener(new OnClickListener() {
			
			/**
			 * Downloads the html from the websites and goes to the DataActivity
			 */
			public void onClick(View v) {
				
				// prepare for a progress bar dialog	
				loadButton.setEnabled(false);
				
				ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
			    // Check if is possible to establish a connection
			    if (networkInfo != null && networkInfo.isConnected()) {
					DownloadWebpageTask download = new DownloadWebpageTask();
					// Execute the asyncronous task of downloading the websites
					download.execute(stringUrls);
			    } else {
			    	// Inform the user that there is no network connection available
			    	Toast.makeText(getBaseContext(), "No network connection available.", Toast.LENGTH_LONG).show();
			        System.out.println("No network connection available.");
			    }
			    
			}
		});	
	}
	
	/**
	 * Inflates the menu from the XML
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		//getMenuInflater().inflate(R.menu.preferences, menu);
		return true;
	}
	
	/**
	 * Checks which item from the menu has been clicked
	 */
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
 
        // Goes to the settings activity
        case R.id.menu_settings:
            Intent i = new Intent(this, SettingsActivity.class);
            startActivityForResult(i, RESULT_SETTINGS);
            break;
            /*
        case R.id.menu_calendar:
        	Intent i2 = new Intent(this, CalendarActivity.class);
        	startActivity(i2);
        	break; 
        	*/
        }
 
        return true;
    }
    
    /** 
     * Uses AsyncTask to create a task away from the main UI thread. 
     * This task takes the creates several HttpUrlConnection to download the html from different websites. 
     * Afterwards, the several lists of Events are created and the execution goes to the Date Activity.
    */
	private class DownloadWebpageTask extends AsyncTask<String, String, Map<String,List<Event>>> {
		
		/**
		 * Makes the progressBar visible
		 */
		protected void onPreExecute(){
	    	System.out.println("onPreExecute");
	    	loadProgressBar.setVisibility(View.VISIBLE);
		}
		
		/**
		 * Downloads the htmls and creates the lists of Events. 
		 * Detects any possible problem during the download of the website or the extraction of events.
		 * 
		 */
		protected Map<String,List<Event>> doInBackground(String... urls) { 	
			// Remove all the entries from the map
			events.clear();
			// TODO Instead of clearing all the events, maintain the ones that did not change
			// Load the events from the selected websites
			for (int i=0; i<webs.length; i++){
				List<Event> event = null;
				if (webs[i].equals("I Heart Berlin")){
					System.out.println("Ihearberlin dentro");
					event = EventLoaderFactory.newIHeartBerlinEventLoader().load(context);
				}else if(webs[i].equals("Berlin Art Parasites")){
					System.out.println("artParasites dentro");
					event = EventLoaderFactory.newArtParasitesEventLoader().load(context);
				}else if(webs[i].equals("Metal Concerts")){
					System.out.println("metalConcerts dentro");
					event = EventLoaderFactory.newMetalConcertsEventLoader().load(context);
				}else if(webs[i].equals("White Trashs concerts")){
					System.out.println("whitetrash dentro");
					event = EventLoaderFactory.newWhiteTrashEventLoader().load(context);
				}else if(webs[i].equals("Koepis activities")){
					System.out.println("koepi dentro");
					event = EventLoaderFactory.newKoepiEventLoader().load(context);
				}else if(webs[i].equals("Goth Datum")){
					System.out.println("goth dentro");
					event = EventLoaderFactory.newGothDatumEventLoader().load(context);
				}else if(webs[i].equals("Stress Faktor")){
					System.out.println("Stresssssss faktor dentro");
					event = EventLoaderFactory.newStressFaktorEventLoader().load(context);
				}else{
					return null;
				}
				// If there was a problem loading the events we tell the user
				if (event == null){
					System.out.println("Event is null");
					publishProgress("Exception", webs[i]);		
				}else{
					// If not, we store its events
					events.put(webs[i], event);
				}
			}
			return events;
		} 
       
		/**
		 * If there was a problem, inform the user
		 */
		protected void onProgressUpdate(String... progress) {
    		System.out.println("Estoy en onProgressUpdate:"+progress[0]);
    		if (progress[0].equals("Exception")){
    			Toast.makeText(context, "There were problems downloading the content from: " +progress[1] +" It's events won't be displayed.", Toast.LENGTH_LONG).show();
    		}
    		//loadProgressBar.setProgress(progress[0].intValue());
		}
		
		/**
		* Goes to the Date Activity and hides the progressBar.
        */
		protected void onPostExecute(Map<String, List<Event>> result) {
			System.out.println("onPostExecute de la primera tarea.");
			loadProgressBar.setVisibility(View.GONE);
			// Enable the button
			loadButton.setEnabled(true);
			// Go to the Date Activity
			Intent intent = new Intent(context, DateActivity.class);
			//intent.putParcelableArrayListExtra(EXTRA_HTML, result);
			startActivity(intent);
			
		}
	}
}