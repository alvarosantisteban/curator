package com.alvarosantisteban.berlincurator;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alvarosantisteban.berlincurator.IHeartBerlinHtmlParser.Entry;

public class MainActivity extends Activity {

	Button loadButton;
	TextView loadText;
	ProgressBar loadProgressBar;
	RelativeLayout mainLayout;
	
	List<Entry> entries;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		System.out.println("Empezamos");
		//final Context context = this;
		
		mainLayout = (RelativeLayout) findViewById(R.id.mainLayout);
		loadButton = (Button) findViewById(R.id.buttonLoad);
		loadText = (TextView) findViewById(R.id.textLoad);
		loadProgressBar = (ProgressBar)findViewById(R.id.progressBar1);
		
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
}
