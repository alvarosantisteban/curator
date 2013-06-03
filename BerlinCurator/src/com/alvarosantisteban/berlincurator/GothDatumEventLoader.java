package com.alvarosantisteban.berlincurator;

import java.util.ArrayList;
import java.util.List;

public class GothDatumEventLoader implements EventLoader {
	
	private final static String URL = "http://www.goth-city-radio.com/dsb/dates.php";

	@Override
	public List<Event> load() {
		String html = WebUtils.downloadHtml(URL);
		return extractEventsFromGothDatum(html);
	}

	/**
	 * Creates a set of events from the html of the Goth Datum website. 
	 * Each Event has name, day, time, a sometimes a description and a link.
	 * 
	 * @param theHtml the String containing the html from the Goth Datum website
	 * @return a List of Event with the name, day, time, description and link
	 */
	private List<Event> extractEventsFromGothDatum(String theHtml) {
		String myPattern = "<TD ALIGN=\"LEFT\" VALIGN=\"TOP\"><hr /><b><i>";
		String[] result = theHtml.split(myPattern);
		
		// Use an ArrayList because the number of events is unknown
		List<Event> events = new ArrayList<Event>(); 
		for (int i=1; i<result.length; i++){
			// Separate up to the first "</b>"
			String[] dateAndRest = result[i].split("</b>", 2);
			// Get the date
			String[] dayAndDate = dateAndRest[0].split("  ", 2);
			String[] eventsOfADay = dateAndRest[1].split("<TR>");
			for (int j=0; j<eventsOfADay.length; j++){
				Event event = new Event();
				// Format the date and set it
				event.setDay(dayAndDate[1].replace('.', '/').trim());
				String[] nothingTimeAndRest = eventsOfADay[j].split("<b>",2);
				String[] timeAndRest = nothingTimeAndRest[1].split(" ",2);	
				// Set the time
				event.setHour(timeAndRest[0].trim());
				String[] placeAndRest = timeAndRest[1].split("<",2);
				// Get the place for the description
				String place = "The event will take place in " +placeAndRest[0] +"<br>"; // In many cases includes the word "Berlin"	
				String[] nothingNameAndRest = placeAndRest[1].split("<i>",2);
				String[] nameAndRest = nothingNameAndRest[1].split("<",2);
				// Set the name
				event.setName(nameAndRest[0].trim());	
				// For each <BR> there is a part of the description
				String[] nothingAndDescription = nameAndRest[1].split("<BR>",2); // Si quito el 2, lo puedo tener dividido en parrafos e iterar con for
				String[] description = nothingAndDescription[1].split("</TD>");
				event.setDescription(place +description[0]);
				events.add(event);
			}
		}
		return events;		
	}
}
