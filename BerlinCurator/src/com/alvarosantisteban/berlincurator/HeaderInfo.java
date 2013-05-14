package com.alvarosantisteban.berlincurator;

import java.util.ArrayList;

public class HeaderInfo {
  
	private String name;
	private ArrayList<DetailInfo> eventsList = new ArrayList<DetailInfo>();;
  
	public String getName() {
		return name;
	}
 
	public void setName(String name) {
		this.name = name;
	}
 
	public ArrayList<DetailInfo> getEventsList() {
		return eventsList;
	}
 
	public void setEventsList(ArrayList<DetailInfo> eventsList) {
		this.eventsList = eventsList;
	}
}