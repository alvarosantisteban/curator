package com.alvarosantisteban.berlincurator;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;

public class IHeartBerlinEventLoader implements EventLoader {
	
	private final static String URL = "http://www.iheartberlin.de/events/";

	@Override
	public List<Event> load(Context context) {
		String html = WebUtils.downloadHtml(URL, context);
		if(html.equals("Exception")){
			return null;
		}
		try{
			return extractEventsFromIHeartBerlin(html);
		}catch(ArrayIndexOutOfBoundsException exception){
			System.out.println("Exception catched!!!");
			return null;
		}
	}
	
	/**
	 * Creates a set of events from the html of the I Heart Berlin website. 
	 * Each Event has name, day, description and a link.
	 * 
	 * @param theHtml the String containing the html from the I Heart Berlin website
	 * @return a List of Event with the name, day and links set
	 */
	private List<Event> extractEventsFromIHeartBerlin(String theHtml) throws ArrayIndexOutOfBoundsException{  
		String myPattern = "<div class=\"event_date\">";
		String[] result = theHtml.split(myPattern);
		
		// Use an ArrayList because the number of events is unknown
		List<Event> events = new ArrayList<Event>(); 
		for (int i=1; i<result.length; i++){
			// Separate up to the first "</div>"
			String[] dateAndRest = result[i].split("</div>", 2);
			// Get the date
			String[] dayAndDate = dateAndRest[0].split(", ", 2);
			String[] eventsOfADay = dateAndRest[1].split("<div class=\"event_entry clearfix\">");
			for (int j=1; j<eventsOfADay.length; j++){
				Event event = new Event();
				// Format the date and set it
				event.setDay(Utils.formatDate(dayAndDate[1].replace(",", "").trim()));
				String[] imageAndRest = eventsOfADay[j].split("<div class=\"event_image\">");
				String[] imageAndRest2 = imageAndRest[1].split("<div class=\"event_info\">");
				// Set the image with all its html code 				
				event.setImage(imageAndRest2[0].replaceFirst("</div>", ""));
				String[] other = imageAndRest2[1].split("<div class=\"event_time\">");
				String[] timeAndRest = other[1].split("</div>",2);
				// Set the time when the event begins
				event.setHour(timeAndRest[0].replace("h", "").trim()); // Remove the "h" from 22:00h
				String[] nameAndRest = timeAndRest[1].split("<h3>");
				String[] nameAndRest2 = nameAndRest[1].split("</h3");
				// Set the name of the event
				event.setName(nameAndRest2[0].trim());
				String[] descriptionAndLinks = nameAndRest2[1].split("</p>");
				// Set the description
				String description = descriptionAndLinks[0].replaceFirst("<p>", "").replaceFirst(">", "").trim();
				event.setDescription(description);
				event.setLocation(extractLocation(description));
				// Set the links
				String[] trashAndLinks = descriptionAndLinks[1].split("<div class=\"event_links\">");
				// Check if there are links
				String[] htmlLink = trashAndLinks[1].split("<a href=\"");
				if(htmlLink.length > 0){
					String links = "";
					// Search for the links
					for (int z=0; z<htmlLink.length; z++){
						String[] pureLink = htmlLink[z].split("\"",2); // Get link
						links = pureLink[0].trim() +"\n" +links;
					}
					event.setLink(links);
				}
				events.add(event);
			}
		}
		return events;
    }

	/**
	 * Extract the street name from the description.
	 * It does not get it if it's written like "NAME Str" or "NAMEstrasse".
	 * It also misses the posible letter that it might go after the number of the street.
	 * 
	 * @param description the string with the street name on it
	 * @return the street or an empty string if it was not found
	 */
	private String extractLocation(String description){
		String pattern="";
		int street = description.indexOf("str.");
		if(street > -1){
			pattern = "str.";
		}else {
			street = description.indexOf("damm");
			if(street > -1){
				pattern = "damm";
			}
		}
		if (!pattern.equals("")){
			try{
				//System.out.println("description:" +description);
				String streetName = description.substring(0, street);
				String streetNumber = description.substring(street);
				//System.out.println("streetName:" +streetName);
				//System.out.println("streetNumber:" +streetNumber);
				int startOfStreet = streetName.lastIndexOf(" ");
				//System.out.println("startOfStreet:" +startOfStreet);
				int i = 0;
				while (!Character.isDigit(streetNumber.charAt(i))) i++;
				//System.out.println("i:"+i);
				int j = i;
				while (j < streetNumber.length() && Character.isDigit(streetNumber.charAt(j))) j++;
				//System.out.println("startOfStreet:"+startOfStreet +" j:" +j);
				String fullStreet = description.substring(startOfStreet,street+j);
				return "<a href=\"https://maps.google.es/maps?q="+fullStreet.replace(' ', '+') +",+Berlin\">" +fullStreet +"</a>";
			}catch(Exception e){
				System.out.println("\nException in extractLocation.\n" +e);
			}
		}
		return "";
	}
}