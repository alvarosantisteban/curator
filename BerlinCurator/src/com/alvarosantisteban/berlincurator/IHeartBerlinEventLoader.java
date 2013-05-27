package com.alvarosantisteban.berlincurator;

import java.util.ArrayList;
import java.util.List;

public class IHeartBerlinEventLoader implements EventLoader {
	
	private final static String URL = "http://www.iheartberlin.de/events/";

	@Override
	public List<Event> load() {
		String html = WebUtils.downloadHtml(URL);
		return extractEventsFromIHeartBerlin(html);
	}
	
	/**
	 * Creates a set of events from the html of the I Heart Berlin website. 
	 * Each Event has name, day, description and a link.
	 * 
	 * @param theHtml the String containing the html from the I Heart Berlin website
	 * @return a List of Event with the name, day and links set
	 */
	private List<Event> extractEventsFromIHeartBerlin(String theHtml){  
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
}