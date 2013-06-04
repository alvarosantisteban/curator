package com.alvarosantisteban.berlincurator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.Toast;

public class CalendarActivity extends Activity {
	
	CalendarView calendar;
	public static int selectedDay = 0;
	Context context = this;
	public static final String EXTRA_DATE = "date";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calendar);
		
		calendar = (CalendarView) findViewById(R.id.calendarView);
		calendar.setOnDateChangeListener(new OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                    int dayOfMonth) {
                 Toast.makeText(getApplicationContext(), "You selected to see the events for the day: "+dayOfMonth +"/" +(++month) +"/" +year, Toast.LENGTH_SHORT).show();
                 selectedDay = dayOfMonth;
                 Intent intent = new Intent(context, DateActivity.class);
                 String day;
                 String monthString;
                 //month++;
                 if (dayOfMonth < 10){
                	 day = "0"+String.valueOf(dayOfMonth);
                 }else{
                	 day = String.valueOf(dayOfMonth);
                 }
                 if (month <10){
                	 monthString = "0"+String.valueOf(month);
                 }else{
                	 monthString = String.valueOf(month);
                 }
                 String choosenDate = day +"/" + monthString +"/" +String.valueOf(year);
                 intent.putExtra(EXTRA_DATE, choosenDate);
             	 startActivity(intent);
            }
        });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.calendar, menu);
		return true;
	}

}
