package com.alvarosantisteban.berlincurator;

import java.util.ArrayList;
import java.util.List;

public class ArtParasitesEventLoader implements EventLoader {
	
	private final static String URL = "http://www.berlin-artparasites.com/recommended";

	@Override
	public List<Event> load() {
		String html = WebUtils.downloadHtml(URL);
		// TODO Control that it's only done from thursday to sunday
		String subUrl = extractUrlFromMainArtParasites(html);
		System.out.println("subUrl"+subUrl);
		html = WebUtils.downloadHtml(subUrl);
		return extractEventsFromArtParasites(html);
	}

	
	/**
	 * Creates a set of events from the html of the Berlin Art Parasites website. 
	 * Each Event has name, day, description and a link.
	 * 
	 * @param theHtml the String containing the html from the Berlin Art Parasites website
	 * @return a List of Event with the name, day, hour, description and links set
	 */
	private List<Event> extractEventsFromArtParasites(String theHtml){  
		// First, get rid of everthing that goes after <em>
		String[] clearnerHtml = theHtml.split("<em>",2);
		String myPattern = "<strong>"; // Marks the number of days
		String[] result = clearnerHtml[0].split(myPattern);
		
		// Use an ArrayList because the number of events per day is unknown
		List<Event> events = new ArrayList<Event>(); 
		for (int i=1; i<result.length; i++){
			// Separate up to the first "</strong>"
			String[] dateAndRest = result[i].split("</strong>", 2);
			// Get the date
			String[] dayAndDate = dateAndRest[0].split(" ", 2);
			String[] eventsOfADay = dateAndRest[1].split("<a href=\""); // Marks the number of events
			for (int j=1; j<=(eventsOfADay.length-1)/2; j++){
				Event event = new Event();
				// Format the date and set it
				String day = Utils.formatDate(dayAndDate[1].replace(",", "").trim());
				event.setDay(day);
				
				System.out.println("eventsOfADay[(j*2)-1]"+eventsOfADay[(j*2)-1]);
				String[] linkAndPlace = eventsOfADay[(j*2)-1].split("</a>",2);
				// We will use the "place" for the description ---> place[1]
				String[] place = linkAndPlace[0].split("\">"); 
				String placeLink = place[0];
				// Check if the link still contains crap
				if(placeLink.contains("\"")){
					String[] cleanLink = placeLink.split("\"");
					placeLink = cleanLink[0];
				}
				// Check if the link belongs to the Art Parasites site or not
				if(placeLink.charAt(0) == '/'){
					event.setLink("http://www.berlin-artparasites.com"+placeLink);
				}else{
					String[] linkAndCrap = placeLink.split("\"",2);
					event.setLink(linkAndCrap[0]);
				}
				
				// Extract the name and a link and set them
				String[] linkNameAndRest = eventsOfADay[(j*2)].split("</a>",2);
				String[] linkAndName = linkNameAndRest[0].split("\">"); 
				event.setName(linkAndName[1]);
				// Check if the link still contains crap
				if(linkAndName[0].contains("\"")){
					String[] cleanLink = linkAndName[0].split("\"");
					linkAndName[0] = cleanLink[0];
				}
				// Check if the link belongs to the Art Parasites site or not
				if(linkAndName[0].charAt(0) == '/'){
					event.setLink("http://www.berlin-artparasites.com"+linkAndName[0]);
				}else{
					String[] linkAndCrap = linkAndName[0].split("\"",2);
					event.setLink(linkAndCrap[0]);
				}
				
				// Extract the time and set it
				String[] timeAndRest = linkNameAndRest[1].split("pm</p>"); 
				String[] crapTime = timeAndRest[0].split("-",2);
				event.setHour(crapTime[1].trim());
				
				String[] nothingAndDescription = timeAndRest[1].split(">", 2);
				String[] descriptionAndNothing = nothingAndDescription[1].split("</p>",2);
				
				// Create the description and set it
				String description = linkAndName[1] + " at the " +place[1] +":" +"<br>" + descriptionAndNothing[0].trim(); 
				event.setDescription(description);
				
				events.add(event);
			}
		}
		return events;
    }
	
	/**
	 * Reads the html from the main site of the Berlin Art Parasites website and looks for the first entry of the "Best Weekend Art Events" so it
	 * can get the url of the site which contains the events.
	 * 
	 * @param parasitesMainSite the html of the main site of Berlin Art Parasites
	 * @return the url of the site with the events for the weekend
	 */
	private String extractUrlFromMainArtParasites(String parasitesMainSite){
		// Look for the first entry of Best Weekend Art Events
		String myPattern = "Best Weekend Art Events";
		String[] result = parasitesMainSite.split(myPattern,2);
		// Set the left limit of the link
		String[] links = result[0].split("<h1><a href=\"");
		// Set the right limit of the link
		String[] link = links[links.length-1].split("\">");
		// Return the absolute link
		return "http://www.berlin-artparasites.com"+link[0];
	}
}