package com.alvarosantisteban.berlincurator;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;

public class MetalConcertsEventLoader implements EventLoader{
	
	private final static String URL = "http://berlinmetal.lima-city.de/index.php/index.php?id=start";
	private final static String ACTUAL_YEAR = "2013";

	@Override
	public List<Event> load(Context context) {
		String html = WebUtils.downloadHtml(URL, context);
		if(html.equals("Exception")){
			return null;
		}
		try{
			return extractEventsFromMetalConcerts(html);
		}catch(ArrayIndexOutOfBoundsException exception){
			System.out.println("Exception catched!!!");
			return null;
		}
	}

	/**
	 * Creates a set of events from the html of the Metal Concerts website. 
	 * Each Event has name, day, description and a link.
	 * 
	 * @param theHtml the String containing the html from the Metal Concerts website
	 * @return a List of Event with the name, day and links set
	 */
	private List<Event> extractEventsFromMetalConcerts(String theHtml) throws ArrayIndexOutOfBoundsException{  
		String myPattern = "<p class=\"konzerte\">"; //<p class=\"konzerte\">.*?</p>
		String[] result = theHtml.split(myPattern);
		
		// Throw away the first entry of the array because it does not contain a concert
		List<Event> events = new ArrayList<Event>(result.length-1); 
		for (int i=1; i<result.length; i++){
			// Separate up to the "@"
			String[] twoParts = result[i].split("@");
			// Separate the date and the name of the band
			String[] dateAndName = twoParts[0].split("\\. ");
			Event event = new Event();
			event.setName(dateAndName[1]);
			String eventDate = dateAndName[0].replace('.', '/');
			//DateFormat dateFormat = new SimpleDateFormat("yyyy", Locale.GERMAN);
			//eventDate = eventDate.concat("/" +dateFormat.format(calendar.getTime()));
			event.setDay(eventDate + "/" +ACTUAL_YEAR);
			
			// Remove useless code
			String htmlLink = twoParts[1].replaceFirst("</p>", "");
			// Check if there is a link
			if(htmlLink.charAt(0) == '<'){
				// Remove useless code
				htmlLink = htmlLink.replaceFirst("</a>", "");
				String[] pureLink = htmlLink.split("\""); // Get link
				String[] concertPlace = htmlLink.split(">"); // Get name
				event.setLink(pureLink[1]);
				event.setDescription("The concert will take place at the " +concertPlace[1]);
			}else{
				String concertPlace = htmlLink;
				event.setDescription("The concert will take place at the " +concertPlace);
			}
			events.add(event);
		}
		return events;
    }
}
