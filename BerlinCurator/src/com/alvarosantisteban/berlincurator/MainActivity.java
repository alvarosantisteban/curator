package com.alvarosantisteban.berlincurator;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutionException;

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
    
    private int progressBarStatus = 0;

	List<Entry> entries;
	
	/**
	 * Loads the elements from the resources
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		System.out.println("--------------- Empezamos ------------");
		final Context context = this;
		
		mainLayout = (RelativeLayout) findViewById(R.id.mainLayout);
		loadButton = (ImageButton) findViewById(R.id.loadButton);
		loadProgressBar = (ProgressBar)findViewById(R.id.progressLoadHtml);
		
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
					download.execute(stringUrls);
					try {
						// Get the downloaded htmls
						htmls = download.get();
					} catch (InterruptedException e) {
						System.out.println("interrupted exception");
						e.printStackTrace();
					} catch (ExecutionException e) {
						System.out.println("execution exception");
						e.printStackTrace();
					}
			    } else {
			        System.out.println("No network connection available.");
			    }
				// Go to the DateActivity and pass it the downloaded data
				Intent intent = new Intent(context, DateActivity.class);
				intent.putExtra(EXTRA_HTML, htmls);
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/* Loads a XML from disk corresponding to the iheartberlin website and parses it
    private void loadXml() throws XmlPullParserException, IOException {
    	System.out.println("loadXml()");
        InputStream stream = null;
        IHeartBerlinHtmlParser iHeartBerlinXmlParser = new IHeartBerlinHtmlParser((Context)this);
        entries = null;
        //String info = null;
        //String image = null;

        try {
            stream = iHeartBerlinXmlParser.readMyFile(null);
            entries = iHeartBerlinXmlParser.parse(stream);
        // Makes sure that the InputStream is closed after the app is
        // finished using it.
        } finally {
            if (stream != null) {
                stream.close();
            }
        }

        // StackOverflowXmlParser returns a List (called "entries") of Entry objects.
        // Each Entry object represents a single post in the XML feed.
        // This section processes the entries list to combine each entry with HTML markup.
        // Each entry is displayed in the UI as a link that optionally includes
        // a text summary.
        for (Entry entry : entries) {
        	entry.printEntry();
        }
    }
    */
    
    /** 
     * Uses AsyncTask to create a task away from the main UI thread. This task takes the 
    * URL strings and uses them to create several HttpUrlConnection. Once the connection
    * has been established, the AsyncTask downloads the html of the webpage as
    * an InputStream. Finally, the InputStream is converted into a string.
    */
	private class DownloadWebpageTask extends AsyncTask<String, Integer, String[]> {
		
		/**
		 * Makes the progressBar visible
		 */
		protected void onPreExecute(){
	    	System.out.println("onPreExecute");
	    	loadProgressBar.setVisibility(View.VISIBLE);
		}
	       
		/**
		 * Downloads the htmls. Updates the status of the progressBar.
		 */
		protected String[] doInBackground(String... urls) {   
        	// params comes from the execute() call: params[0] is the url.
			try {
				String[] webpages = new String[MAX_NUMBER_OF_WEBSITES];
				for (int i = 0; i < urls.length; i++) {
					webpages[i] = downloadUrl(urls[i]);
					progressBarStatus++;
					System.out.println("Estoy en doInBackgroung:"+progressBarStatus);
					publishProgress(progressBarStatus);
				}
				return webpages;
			} catch (IOException e) {
				return null;
			}
		}
       
		/**
		 * Sets the progress of the progressBar.
		 */
		protected void onProgressUpdate(Integer... progress) {
    		System.out.println("Estoy en onProgressUpdate:"+progress[0].intValue());
        	//setProgressPercent(progress);
          	loadProgressBar.setProgress(progress[0].intValue());
		}
       
		/**
        * Hides the progressBar.
        */
		protected void onPostExecute(String[] result) {
			System.out.println("onPostExecute:" +result[0] +".");
			loadProgressBar.setVisibility(View.GONE);
		}
       
       /*
       private void setProgressPercent(int progress){
    	   System.out.println("Estoy en setProgressPercent:"+progress);
    	   loadProgressBar.setProgress(progress);
       }*/
       
       /*
       private EventView extractEventFromHtml(String theHtml){    	   
    	   return null;
       }*/
       
		/** 
        * Given a URL, establishes an HttpUrlConnection and retrieves
        * the web page content as a InputStream, which it returns as
        *  a string.
        */
		private String downloadUrl(String myurl) throws IOException {
			InputStream is = null;
		   
      	   	try {
      	   		URL url = new URL(myurl);
      	   		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      	   		conn.setReadTimeout(10000 /* milliseconds */);
      	   		conn.setConnectTimeout(15000 /* milliseconds */);
      	   		conn.setRequestMethod("GET");
      	   		conn.setDoInput(true);
      	   		// Starts the query
      	   		conn.connect();
      	   		int response = conn.getResponseCode();
      	   		Log.d(DEBUG_TAG, "The response is: " + response);
      	   		is = conn.getInputStream();
      	   		// Convert the InputStream into a string
      	   		String contentAsString = convertStreamToString(is);
      	   		return contentAsString;
      	         
      	   		// Makes sure that the InputStream is closed after the app is
      	   		// finished using it.
      	   	} finally {
      	   		if (is != null) {
      	   			is.close();
      	   		} 
      	   	}
		}
          
		/**
		 * Converts a InputStream to String. 
		 * Taken from http://stackoverflow.com/questions/309424/read-convert-an-inputstream-to-a-string
		 * @param is the InputStream to be converted
		 * @return the resulting String
		 */
		public String convertStreamToString(java.io.InputStream is) {
			java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
			return s.hasNext() ? s.next() : "";
		}
	}
}


/*

loadButton.setOnClickListener(new OnClickListener() {

public void onClick(View v) {
	//loadProgressBar = new ProgressBar(context);
	loadButton.setText("Loading");
	loadProgressBar.setVisibility(0);
	try {
		loadXml();
		loadButton.setVisibility(4);
		//((RelativeLayout)loadButton.getParent()).removeView(loadButton); Another way of deleting views
		mainLayout.removeView(loadProgressBar);
		
		/*
		TextView date = new TextView(getBaseContext());
		date.setText(entries.get(0).date);
		mainLayout.addView(date);
		*
		EventView event = new EventView(getBaseContext(), entries.get(0).date, entries.get(0).time, entries.get(0).title, entries.get(0).image, entries.get(0).info, entries.get(0).category, entries.get(0).link);
		mainLayout.addView(event.text);
	}catch(Exception e) {
		System.out.println("Error loading the xml." +e);
	}
}
});

*/

/*
 * 
 * INTENTO CON EL PUTO WEBHARVEST
 *
try{
	System.out.println("Loco");
	String strPageURL = "http://www.iheartberlin.com/events";
	//InputStream in_s = context.getResources().openRawResource(R.xml.iheartwebharvestconfig);
	InputStream in_s = context.getResources().openRawResource(R.raw.iheartwebharvestconfig2);
	
	BufferedReader r = new BufferedReader(new InputStreamReader(in_s));
	StringBuilder total = new StringBuilder();
	String line;
	while ((line = r.readLine()) != null) {
	    total.append(line);
	}
	r.close();
	System.out.println("----");
	System.out.println(total);
	System.out.println("----");
	
	InputSource inputSource = new InputSource(in_s);
	
	//inputSource.setEncoding("UTF-8");
	//inputSource.setPublicId("Soy el id publico");
	
	//System.out.println(inputSource.getEncoding());
	//System.out.println(inputSource.getPublicId());
	//System.out.println(inputSource.getSystemId());				
	System.out.println("----");
	
	/*
	 * 
	 * Writing directly the xml *
	InputStream in = new ByteArrayInputStream("<html-to-xml><http url=\"http://www.iheartberlin.com/events\"/></html-to-xml>".getBytes());
    InputSource inputSource2 = new InputSource(in);
    ScraperConfiguration config = new ScraperConfiguration(inputSource2);
    
	
	//ScraperConfiguration config = new ScraperConfiguration(inputSource);
	Scraper scraper = new Scraper(config, System.getProperty("user.dir"));
	scraper.addVariableToContext("url",strPageURL);
	scraper.setDebug(true);
	scraper.execute();
	Variable varScrappedContent = (Variable)scraper.getContext().getVar("scrappedContent");
	 
	// Printing the scraped data here
	System.out.println(varScrappedContent.toString());
}catch(Exception e){
    e.printStackTrace();
}
	*/