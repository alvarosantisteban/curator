package com.alvarosantisteban.berlincurator;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.util.Xml;

public class IHeartBerlinHtmlParser {
	
	// We don't use namespaces
    private static final String ns = null;
    Context context;
    
    public IHeartBerlinHtmlParser(Context c){
    	this.context = c;
    }
   
    public List<Entry> parse(InputStream in) throws XmlPullParserException, IOException {
    	System.out.println("IheartBerlinParser: parse()");
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            in.close();
        }
    }

    private List<Entry> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<Entry> entries = new ArrayList<Entry>();

        parser.require(XmlPullParser.START_TAG, ns, "feed");
        while (parser.next() != XmlPullParser.END_TAG) {
        	System.out.println("Todavia no hemos llegado al end tag");
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("entry")) {
            	System.out.println("Entry found");
                entries.add(readEntry(parser));
                //System.out.println(entries.get(0).date);
            } else {
                skip(parser);
            }
        }
        return entries;
    }

 // This class represents a single entry (post) in the XML feed.
    // It includes the data members "title," "link," and "summary."
    public static class Entry {
    	public final String date;
    	public final String time;
        public final String title;
        public final String image;
        public final String info;
        public final String category;
        public final String link;       
        

        private Entry(String date, String time, String title, String image, String info, String category, String link) {
            this.date = date;
            this.time = time;
            this.title = title;
            this.image = image;            
            this.info = info;
            this.category = category;
            this.link = link;
        }
        
        public void printEntry(){
        	System.out.println(this.date);
        	System.out.println(this.time);
        	System.out.println(this.title);
        	System.out.println(this.image);
        	System.out.println(this.info);
        	System.out.println(this.category);
        	System.out.println(this.link);
        }
    }

    // Parses the contents of an entry. If it encounters a title, summary, or link tag, hands them
    // off
    // to their respective &quot;read&quot; methods for processing. Otherwise, skips the tag.
    private Entry readEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "entry");
        String title = null;
        String time = null;
        String link = null;
        String image = null;
        String info = null;
        String date = null;
        String category = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("date")) {
            	System.out.println("Date found");
                date = readDate(parser);
            } else if (name.equals("time")) {
            	System.out.println("Time found");
            	time = readTime(parser);
            } else if (name.equals("title")) {
            	System.out.println("Title found");
            	title = readTitle(parser);
            } else if (name.equals("image")) {
            	System.out.println("Image found");
                image = readImage(parser);
            } else if (name.equals("info")) {
            	System.out.println("Info found");
                info = readInfo(parser);
            } else if (name.equals("category")) {
            	System.out.println("Category found");
            	category = readCategory(parser);
            } else if (name.equals("link")) {
            	System.out.println("Link found");
                link = readLink(parser);
            } else {
                skip(parser);
            }
        }
        return new Entry(date, time, title, image, info, category, link);
    }
    
    private String readDate(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "date");
        String date = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "date");
        return date;
    }
    
    private String readTime(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "time");
        String time = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "time");
        return time;
    }

    // Processes title tags in the feed.
    private String readTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "title");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "title");
        return title;
    }
    
    private String readImage(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "image");
        String image = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "image");
        return image;
    }
    
    private String readInfo(XmlPullParser parser)  throws IOException, XmlPullParserException{
		parser.require(XmlPullParser.START_TAG, ns, "info");
        String info = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "info");
        return info;
	}
	
    /*
	private Object readEventInfo(XmlPullParser parser)  throws IOException, XmlPullParserException{ // Change Object to Event????
		parser.require(XmlPullParser.START_TAG, ns, "info");
		String eventClass;
		String author;
		while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("class")) {
                eventClass = readEventClass(parser);
            } else if (name.equals("author")) {
                author = readAuthor(parser);
            } else {
                skip(parser);
            }
        }
		return null;
	}
	*/
	
	private String readCategory(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "category");
        String category = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "category");
        return category;
    }

    // Processes link tags in the feed.
    private String readLink(XmlPullParser parser) throws IOException, XmlPullParserException {
        String link = "";
        parser.require(XmlPullParser.START_TAG, ns, "link");
        String tag = parser.getName();
        String relType = parser.getAttributeValue(null, "rel");
        if (tag.equals("link")) {
            if (relType.equals("alternate")) {
                link = parser.getAttributeValue(null, "href");
                parser.nextTag();
            }
        }
        parser.require(XmlPullParser.END_TAG, ns, "link");
        return link;
    }

    // For the tags title and summary, extracts their text values.
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    // Skips tags the parser isn't interested in. Uses depth to handle nested tags. i.e.,
    // if the next tag after a START_TAG isn't a matching END_TAG, it keeps going until it
    // finds the matching END_TAG (as indicated by the value of "depth" being 0).
    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
            case XmlPullParser.END_TAG:
                    depth--;
                    break;
            case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
	
	InputStream readMyFile(String fileName){
		System.out.println("readMyFile()");
        InputStream in_s = context.getResources().openRawResource(R.raw.iheartberlinraw);
		try {
		    if (in_s != null) {
		    	//in_s.close();
		    }
		}catch(Exception e){
			System.out.println("Error opening the file. \n" +e);
		}
		return in_s;
	}
}
