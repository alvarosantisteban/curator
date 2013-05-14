package com.alvarosantisteban.berlincurator;

/**
 * The information of the event. 
 * 
 * @author Alvaro Santisteban 2013 - alvarosantisteban@gmail.com
 *
 */
public class Event {
	  
	/**
	 * The position of the event inside the list
	 */
	private String sequence = "";
	/**
	 * The name of the event
	 */
	private String name = "";
	  
	/**
	 * Returns the position of the event inside its list
	 * @return
	 */
	public String getSequence() {
		return sequence;
	}
	 
	public void setSequence(String sequence) {
		this.sequence = sequence;
	}
	 
	public String getName() {
		 return name;
	}
	 
	public void setName(String name) {
		this.name = name;
	}
}
