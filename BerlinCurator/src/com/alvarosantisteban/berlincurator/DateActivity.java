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
import android.view.View.OnClickListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class DateActivity extends Activity implements OnClickListener{

	private TextView date;
	ExpandableListView expandableSitesList;
	
	private LinkedHashMap<String, HeaderInfo> websites = new LinkedHashMap<String, HeaderInfo>();
	private ArrayList<HeaderInfo> websitesList = new ArrayList<HeaderInfo>();
	 
	private ListAdapter listAdapter;
	
	final Context context = this;
		
	@Override
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
		
		// Populate the ArrayList
		Resources res = this.getResources();
		String[] sitesNames = res.getStringArray(R.array.sites_array); 
		createHeaderGroups(sitesNames);
		
		expandableSitesList = (ExpandableListView) findViewById(R.id.expandableSitesList);
		//create the adapter by passing your ArrayList data
		listAdapter = new ListAdapter(DateActivity.this, websitesList);
		//attach the adapter to the list
		expandableSitesList.setAdapter(listAdapter);
		
		// Add some random data to start with
		loadData();
		
		addEvent("I Heart Berlin", htmls[0]);
		addEvent("Berlin Art Parasites", htmls[1]);
		addEvent("Metal Concerts", htmls[2]);
		addEvent("White Trashs concerts", htmls[3]);
		addEvent("Koepis activities", htmls[4]);
		
		//expand all Groups
		expandAll();
		
		collapseAll();
		
		//listener for child row click
		expandableSitesList.setOnChildClickListener(myListItemClicked);
		//listener for group heading click
		expandableSitesList.setOnGroupClickListener(myListGroupClicked);
		
		/* Create a progress bar to display while the list loads MAIN ACTIVITY
        ProgressBar progressBar = new ProgressBar(this);
        progressBar.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, Gravity.CENTER));
        progressBar.setIndeterminate(true);
        getListView().setEmptyView(progressBar);
        */


        // Must add the progress bar to the root of the layout
        //addView(progressBar);
	
		/*
		 * 
		 * 
		
		String[] sitesNames = {"IHeartBerlin", "Berlin Art Parasites", "Metal Concerts", "White Trash", "Koepi"};
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, sitesNames);
		setListAdapter(adapter);
		 */
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
	
	//method to expand all groups
	private void expandAll() {
		int count = listAdapter.getGroupCount();
		for (int i = 0; i < count; i++){
			expandableSitesList.expandGroup(i);
		}
	}
	
	//method to collapse all groups
	private void collapseAll() {
		int count = listAdapter.getGroupCount();
		for (int i = 0; i < count; i++){
			expandableSitesList.collapseGroup(i);
		}
	}
	
	//load some initial data into out list
	private void loadData(){ 
		  addEvent("I Heart Berlin","Kino Night");
		  addEvent("I Heart Berlin","Party party");
		  addEvent("I Heart Berlin","Running out of shorts");
	 
		  addEvent("Metal Concerts","Manilla Road");
		  addEvent("Metal Concerts","Dream Theater");
	}
	
	//our child listener
	private OnChildClickListener myListItemClicked =  new OnChildClickListener() {
		 
		  public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
	    
			  //get the group header
			  HeaderInfo headerInfo = websitesList.get(groupPosition);
			  //get the child info
			  DetailInfo detailInfo =  headerInfo.getEventsList().get(childPosition);
			  //display it or do something with it
			  Toast.makeText(getBaseContext(), "Clicked on Detail " + headerInfo.getName() + "/" + detailInfo.getName(), Toast.LENGTH_LONG).show();
			  Intent intent = new Intent(context, EventActivity.class);
			  startActivity(intent);
			  return false;
		  }
	};
	
	//our group listener
	private OnGroupClickListener myListGroupClicked =  new OnGroupClickListener() {
		 
		  public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
	    
			  //get the group header
			  HeaderInfo headerInfo = websitesList.get(groupPosition);
			  //display it or do something with it
			  Toast.makeText(getBaseContext(), "Child on Header " + headerInfo.getName(), Toast.LENGTH_LONG).show();
	     
			  return false;
		  }
	   
	};
	
	//here we maintain our products in various departments
	private int addEvent(String website, String event){
		int groupPosition = 0;
	   
		//check the hash map if the group already exists
		HeaderInfo headerInfo = websites.get(website);
		//add the group if doesn't exists
		if(headerInfo == null){
			headerInfo = new HeaderInfo();
			headerInfo.setName(website);
			websites.put(website, headerInfo);
			websitesList.add(headerInfo);
		}
	 
		//get the children for the group
		ArrayList<DetailInfo> productList = headerInfo.getEventsList();
		//size of the children list
		int listSize = productList.size();
		//add to the counter
		listSize++;
	 
		//create a new child and add that to the group
		DetailInfo detailInfo = new DetailInfo();
		detailInfo.setSequence(String.valueOf(listSize));
		detailInfo.setName(event);
		productList.add(detailInfo);
		headerInfo.setEventsList(productList);
		 
		//find the group position inside the list
		groupPosition = websitesList.indexOf(headerInfo);
		return groupPosition;
	}
	
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

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}
}
