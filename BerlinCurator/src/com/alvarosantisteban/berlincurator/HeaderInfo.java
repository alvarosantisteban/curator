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
	 * The ArrayList of events
	 */
	private ArrayList<Event> eventsList = new ArrayList<Event>();;
  
	public String getName() {
		return name;
	}
 
	public void setName(String name) {
		this.name = name;
	}
 
	public ArrayList<Event> getEventsList() {
		return eventsList;
	}
 
	public void setEventsList(ArrayList<Event> eventsList) {
		this.eventsList = eventsList;
	}
}