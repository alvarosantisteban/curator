package com.alvarosantisteban.berlincurator;

import java.io.Serializable;


/**
 * The information of the event. Implements Serializable to allow this objects to be passes as extras in an Intent
 * 
 * Make it faster:
 * http://stackoverflow.com/questions/2139134/how-to-send-an-object-from-one-android-activity-to-another-using-intents
 * http://stackoverflow.com/questions/2736389/how-to-pass-object-from-one-activity-to-another-in-android
 * 
 * @author Alvaro Santisteban 2013 - alvarosantisteban@gmail.com
 *
 */
public class Event implements Serializable{
	  
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * The position of the event inside the list
	 */
	private String sequence = "";
	/**
	 * The name of the event
	 */
	private String name = "";
	
	/**
	 * The day when the event will take place
	 */
	private String day = "";
	
	/**
	 * The time when the event begins
	 */
	private String hour = "";
	
	/**
	 * The description of the event
	 */
	private String description = "";
	
	/**
	 * The html code corresponding to a image stored in the web
	 */
	private String image="";
	
	/**
	 * The links with more information
	 */
	private String[] links = {"",""};
	  
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
	
	public String getLink(){
		return links[0];
	}
	
	public String[] getLinks(){
		return links;
	}
	
	/**
	 * Set the two links of the event.
	 * @param links the Strings containing the links of the event
	 */
	public void setLinks(String[] links){
		this.links[0] = links[0];
		this.links[1] = links[1];
	}
	
	/**
	 * Sets a link for the event.
	 * @param link the String containing one link of the event
	 */
	public void setLink(String link){
		if (this.links[0] == ""){
			this.links[0] = link;
		}else{
			this.links[1] = link;
		}
	}
	
	public String getDay(){
		return day;
	}
	
	public void setDay(String day){
		this.day = day;
	}
	
	public String getHour(){
		return hour;
	}
	
	public void setHour(String hour){
		this.hour = hour;
	}

	public void setDescription(String description){
		this.description = description;
	}
	
	public String getDescription(){
		return description;
	}
	
	public String getImage(){
		return image;
	}
	
	public void setImage(String image){
		this.image = image;
	}
	
	public void print() {
		System.out.println("Name:"+getName());
		System.out.println("Day:"+getDay());
		printLinks();	
	}
	
	public void printLinks(){
		for (int i=0; i<links.length;i++){
			System.out.println("Link["+i +"]:"+links[i]);
		}
	}
}
