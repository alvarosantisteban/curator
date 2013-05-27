package com.alvarosantisteban.berlincurator;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alvarosantisteban.berlincurator.IHeartBerlinHtmlParser.Entry;

/**
 * Creates the main screen, from where the user can access the Settings, load the events of the current day or 
 * go to Calendar to select another day
 * 
 * @author Alvaro Santisteban 2013 - alvarosantisteban@gmail.com
 *
 */
public class MainActivity extends Activity {

	public static final int MAX_NUMBER_OF_WEBSITES = 5;
	private static final String DEBUG_TAG = "HttpExample";
	public static final String EXTRA_HTML = "com.alvarosantisteban.berlincurator.html";
	public static List<List<Event>> events = (ArrayList)new ArrayList <ArrayList<Event>>();
	
	Context context;
	
	/**
	 * The set of urls from where the html will be downloaded
	 */
   	String[] stringUrls = {"http://www.iheartberlin.de/events/",
				   			"http://www.berlin-artparasites.com/recommended",
				   			"http://berlinmetal.lima-city.de/index.php/index.php?id=start",
				   			"http://www.whitetrashfastfood.com/events/",
				   			"http://www.koepi137.net/eventskonzerte.php",};
   	
   	/**
   	 * The set of htmls from the corresponding {@link stringUrls}
   	 */
   	String[] htmls = new String[MAX_NUMBER_OF_WEBSITES];

	ProgressBar loadProgressBar;
	RelativeLayout mainLayout;
    ImageButton loadButton;
    TextView calendarText;
    TextView settingsText;
    
    private int progressBarStatus = 0;

	List<Entry> entries;
	
	/**
	 * Loads the elements from the resources
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		System.out.println("--------------- BEGIN ------------");
		context = this;
		
		mainLayout = (RelativeLayout) findViewById(R.id.mainLayout);
		loadButton = (ImageButton) findViewById(R.id.loadButton);
		loadProgressBar = (ProgressBar)findViewById(R.id.progressLoadHtml);
		calendarText = (TextView)findViewById(R.id.textCalendar);
		
		//segundon = (DownloadWebpageSecondTask) new DownloadWebpageSecondTask();
		
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
				loadProgressBar.setProgress(0);
				loadProgressBar.setMax(MAX_NUMBER_OF_WEBSITES);
				loadProgressBar.setVisibility(View.VISIBLE);				
				
				ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
			    // Check if is possible to establish a connection
			    if (networkInfo != null && networkInfo.isConnected()) {
					DownloadWebpageTask download = new DownloadWebpageTask();
					// Execute the asyncronous task of downloading the websites
					// TODO Try to make it fail by giving ONE false url
					download.execute(stringUrls);
			    } else {
			    	// Inform the user that there is no network connection available
			    	Toast.makeText(getBaseContext(), "No network connection available.", Toast.LENGTH_LONG).show();
			        System.out.println("No network connection available.");
			    }
			}
		});	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
    
    /** 
     * Uses AsyncTask to create a task away from the main UI thread. This task takes the 
    * URL strings and uses them to create several HttpUrlConnection. Once the connection
    * has been established, the AsyncTask downloads the html of the webpage as
    * an InputStream. Finally, the InputStream is converted into a string.
    */
	//private class DownloadWebpageTask extends AsyncTask<String, Integer, String[]> {
	private class DownloadWebpageTask extends AsyncTask<String, Integer, List<List<Event>>> {
		
		/**
		 * Makes the progressBar visible
		 */
		protected void onPreExecute(){
	    	System.out.println("onPreExecute");
	    	loadProgressBar.setVisibility(View.VISIBLE);
		}
		
		/**
		 * Downloads the htmls. Updates the status of the progressBar.
		 * 
		 */
		protected List<List<Event>> doInBackground(String... urls) {  
			List<Event> iHearBerlinEvents = EventLoaderFactory.newIHeartBerlinEventLoader().load();
			List<Event> parasitesEvents = EventLoaderFactory.newArtParasitesEventLoader().load();
			List<Event> metalConcertsEvents = EventLoaderFactory.newMetalConcertsEventLoader().load();
			List<Event> whiteTrashEvents = EventLoaderFactory.newWhiteTrashEventLoader().load();
			List<Event> KoepisEvents = EventLoaderFactory.newKoepiEventLoader().load();
			events.add(iHearBerlinEvents); 
			events.add(parasitesEvents); 
			events.add(metalConcertsEvents); 
			events.add(whiteTrashEvents); 
			events.add(KoepisEvents); 
			return events;
		} 
       
		/**
		 * Sets the progress of the progressBar.
		 */
		protected void onProgressUpdate(Integer... progress) {
    		System.out.println("Estoy en onProgressUpdate:"+progress[0].intValue());
          	loadProgressBar.setProgress(progress[0].intValue());
		}
		
		/**
		* Hides the progressBar.
        */
		protected void onPostExecute(List<List<Event>> result) {
			System.out.println("onPostExecute de la primera tarea.");
			// CAMBIADO: AHORA HACEMOS EL INTENT AQUI
			Intent intent = new Intent(context, DateActivity.class);
			//intent.putParcelableArrayListExtra(EXTRA_HTML, result); // PAsar en el intent 
			startActivity(intent);
			
		}
	}
}