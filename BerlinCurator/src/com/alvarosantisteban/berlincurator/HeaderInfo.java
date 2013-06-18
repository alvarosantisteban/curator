package com.alvarosantisteban.berlincurator;

import java.util.ArrayList;

/**
 * The information of the website and its List of events.
 * 
 * @author Alvaro Santisteban 2013 - alvarosantisteban@gmail.com
 *
 */
public class HeaderInfo {
  
	/**
	 * Name of the website
	 */
	private String name;
	
	/**
	 * The number of events that eventsList contains
	 */
	private int eventsNumber;
	
	/**
	 * The ArrayList of events
	 */
	private ArrayList<Event> eventsList = new ArrayList<Event>();;
  
	public String getName() {
		return name;
	}
 
	public void setName(String name) {
		this.name = name;
	}
	
	public int getEventsNumber(){
		return eventsNumber;
	}
	
	public void setEventsNumber(int theNumberEvents){
		this.eventsNumber = theNumberEvents;
	}
 
	public ArrayList<Event> getEventsList() {
		return eventsList;
	}
 
	public void setEventsList(ArrayList<Event> eventsList) {
		this.eventsList = eventsList;
	}
}