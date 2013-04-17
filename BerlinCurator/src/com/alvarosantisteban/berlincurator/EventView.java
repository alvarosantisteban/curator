package com.alvarosantisteban.berlincurator;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;
import android.widget.TextView;

public class EventView extends View {

	TextView text;
	
	String date;
	String time;
	String title;
	String image;
	String info;
	String category;
	String link;
	
	public EventView(Context context) {
		super(context);
		date = time = title = image = info = category = link = null;
		text = new TextView(context);
		text.setText("The text has not been set so far");
	}
	
	public EventView(Context context, String date, String time, String title, String image, String info, String category, String link){
		super(context);
		this.date = date;
		this.time = time;
		this.title = title;
		this.image = image;
		this.info = info;
		this.category = category;
		this.link = link;
		text = new TextView(context);
		text.setText(date +time +title +image +info +category +link);
	}
	
	/*
	protected void onDraw(Canvas canvas){
		
	}*/

}
