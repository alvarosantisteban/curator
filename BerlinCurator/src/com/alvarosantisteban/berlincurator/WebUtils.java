package com.alvarosantisteban.berlincurator;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.util.Log;

public class WebUtils {
	
	private static final String DEBUG_TAG = "HttpExample";

	/**
	 * Given a URL, establishes an HttpUrlConnection and retrieves
     * the web page content as a InputStream, which it returns as
     *  a string.
     *  
	 * @param myurl The URL from where the html is downloaded
	 * @return the html from that url
	 * @throws IOException if there is a connecting problem
	 */
	public final static String downloadHtml(String myurl){
			InputStream is = null;
		   
   	   	try {
   	   		URL url = new URL(myurl);
   	   		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
   	   		conn.setReadTimeout(10000 /* milliseconds */);
   	   		conn.setConnectTimeout(15000 /* milliseconds */);
   	   		conn.setRequestMethod("GET");
   	   		conn.setDoInput(true);
   	   		// Starts the query
   	   		conn.connect();
   	   		int response = conn.getResponseCode();
   	   		Log.d(DEBUG_TAG, "The response is: " + response);
   	   		if (response == -1){
   	   			return "Invalid response code";
   	   		}
   	   		is = conn.getInputStream();
   	   		// Convert the InputStream into a string
   	   		String contentAsString = convertStreamToString(is);
   	   		return contentAsString;
   	   	} catch (Exception e){
   	   		System.out.println("Problems downloading the url: "+myurl +". Exception: "+e);
   	   		return "Exception";
   	    // Makes sure that the InputStream is closed after the app is finished using it.
   	   	}finally {
   	   		if (is != null) {
   	   			try {
					is.close();
				} catch (IOException e) {
					System.out.println("Problem closing the inputstream");
					e.printStackTrace();
				}
   	   		} 
   	   	}
	}
		
	/**
	 * Converts a InputStream to String. 
	 * Taken from http://stackoverflow.com/questions/309424/read-convert-an-inputstream-to-a-string
	 * @param is the InputStream to be converted
	 * @return the resulting String
	 */
	public static String convertStreamToString(java.io.InputStream is) {
		java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
	}
}