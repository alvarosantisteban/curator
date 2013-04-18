package com.alvarosantisteban.berlincurator;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alvarosantisteban.berlincurator.IHeartBerlinHtmlParser.Entry;

public class MainActivity extends Activity {

	private static final String DEBUG_TAG = "HttpExample";
   	String stringUrl = "http://www.iheartberlin.de/events/";
	
	Button loadButton, downloadButton;
	TextView loadText;
	ProgressBar loadProgressBar;
	RelativeLayout mainLayout;
    private TextView downloadTextView;

	
	List<Entry> entries;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		System.out.println("Empezamos");
		//final Context context = this;
		
		mainLayout = (RelativeLayout) findViewById(R.id.mainLayout);
		loadButton = (Button) findViewById(R.id.buttonLoad);
		downloadButton = (Button) findViewById(R.id.buttonDownload);
		loadText = (TextView) findViewById(R.id.textLoad);
		loadProgressBar = (ProgressBar)findViewById(R.id.progressBar1);
		downloadTextView = (TextView) findViewById(R.id.myText);
		
		downloadButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ConnectivityManager connMgr = (ConnectivityManager) 
			            getSystemService(Context.CONNECTIVITY_SERVICE);
			        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
			        if (networkInfo != null && networkInfo.isConnected()) {
			            new DownloadWebpageTask().execute(stringUrl);
			        } else {
			            downloadTextView.setText("No network connection available.");
			        }

			}
		});
		
		loadButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				//loadProgressBar = new ProgressBar(context);
				loadText.setText("Loading events...");
				loadButton.setText("Loading");
				loadProgressBar.setVisibility(0);
				try {
					loadXml();
					loadText.setText("Events loaded");
					loadButton.setVisibility(4);
					//((RelativeLayout)loadButton.getParent()).removeView(loadButton); Another way of deleting views
					mainLayout.removeView(loadProgressBar);
					
					/*
					TextView date = new TextView(getBaseContext());
					date.setText(entries.get(0).date);
					mainLayout.addView(date);
					*/
					EventView event = new EventView(getBaseContext(), entries.get(0).date, entries.get(0).time, entries.get(0).title, entries.get(0).image, entries.get(0).info, entries.get(0).category, entries.get(0).link);
					mainLayout.addView(event.text);
				}catch(Exception e) {
					System.out.println("Error loading the xml." +e);
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

	// Loads a XML from disk corresponding to the iheartberlin website and parses it
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
    
    // Uses AsyncTask to create a task away from the main UI thread. This task takes a 
    // URL string and uses it to create an HttpUrlConnection. Once the connection
    // has been established, the AsyncTask downloads the contents of the webpage as
    // an InputStream. Finally, the InputStream is converted into a string, which is
    // displayed in the UI by the AsyncTask's onPostExecute method.
	private class DownloadWebpageTask extends AsyncTask<String, Void, String> {
       @Override
       protected String doInBackground(String... urls) {   
           // params comes from the execute() call: params[0] is the url.
           try {
               return downloadUrl(urls[0]);
           } catch (IOException e) {
               return "Unable to retrieve web page. URL may be invalid.";
           }
       }
       // onPostExecute displays the results of the AsyncTask.
       @Override
       protected void onPostExecute(String result) {
           downloadTextView.setText(result);
      }
       
       
    // Given a URL, establishes an HttpUrlConnection and retrieves
       // the web page content as a InputStream, which it returns as
       // a string.
          private String downloadUrl(String myurl) throws IOException {
          	InputStream is = null;
      	     // Only display the first 500 characters of the retrieved
      	     // web page content.
      	     int len = 500;
      	         
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
      	         String contentAsString = readIt(is, len);
      	         return contentAsString;
      	         
      	     // Makes sure that the InputStream is closed after the app is
      	     // finished using it.
      	     } finally {
      	         if (is != null) {
      	             is.close();
      	         } 
      	     }
          }
          
      	// Reads an InputStream and converts it to a String.
          public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
              Reader reader = null;
              reader = new InputStreamReader(stream, "UTF-8");        
              char[] buffer = new char[len];
              reader.read(buffer);
              return new String(buffer);
          }
   }
	
}
