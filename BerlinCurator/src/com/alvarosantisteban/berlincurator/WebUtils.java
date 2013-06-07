package com.alvarosantisteban.berlincurator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class WebUtils {
	
	private static final String DEBUG_TAG = "HttpExample";
	//private static final String DEFAULT_ENCODING = "UTF-8";// "ISO-8859-1";
	private static final String DEFAULT_ENCODING = "ISO-8859-1";

	/**
	 * Given a URL, establishes an HttpUrlConnection and retrieves
     * the web page content as a InputStream, which it returns as
     *  a string.
     *  
	 * @param myurl The URL from where the html is downloaded
	 * @return the html from that url
	 * @throws IOException if there is a connecting problem
	 */
	public final static String downloadHtml(String myurl, Context context){
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
   	   		String contentType = conn.getContentType();
   	   		// Convert the InputStream into a string
   	   		String contentAsString = "";
   	   		if (contentType.equals("text/html; charset=ISO-8859-1") || myurl.equals("http://stressfaktor.squat.net/termine.php?display=7")){
   	   			contentAsString = convert(is);
   	   		}else{
   	   			//contentAsString = convertStreamToString(is);
   	   			contentAsString = convert(is,"UTF-8");
   	   		}
   	   		
   	   		//contentAsString = checkUTF(contentAsString);
   	   		return contentAsString;
   	   	} catch (Exception e){
   	   		System.out.println("Problems downloading the url: "+myurl +". Exception: "+e);
   	   		Toast.makeText(context, "There were problems downloading the content from: "+myurl +" It's events won't be displayed.", Toast.LENGTH_LONG).show();
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
	
	

	public static final String convert(final InputStream in) throws IOException {
		System.out.println("----RARUNO");
	  return convert(in, DEFAULT_ENCODING);
	}

	public static final String convert(final InputStream in, final String encoding) throws IOException {
	  final ByteArrayOutputStream out = new ByteArrayOutputStream();
	  final byte[] buf = new byte[2048];
	  int rd;
	  while ((rd = in.read(buf, 0, 2048)) >= 0) {
	    out.write(buf, 0, rd);
	  }
	  return new String(out.toByteArray(), encoding);
	}
		
	private static String checkUTF(String contentAsString) {
		if(contentAsString.contains("charset=ISO-8859-1")){
			try {
				byte[] utf8 = contentAsString.getBytes("UTF-8");
				return new String(utf8, "UTF-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
				return null;
			}
		}
		return contentAsString;
	}



	/**
	 * Converts a InputStream to String. 
	 * Taken from http://stackoverflow.com/questions/309424/read-convert-an-inputstream-to-a-string
	 * @param is the InputStream to be converted
	 * @return the resulting String
	 */
	public static String convertStreamToString(java.io.InputStream is) {
		System.out.println("GUAY ---------");
		java.util.Scanner s = new java.util.Scanner(is, "UTF-8").useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
	}
}