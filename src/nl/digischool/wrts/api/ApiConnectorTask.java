package nl.digischool.wrts.api;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLException;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class ApiConnectorTask extends AsyncTask<Map<String, Object>, Void, String> {
	
	private static String API_URL = "https://wrts.nl/api";
	private static Boolean logEnabled = true;
	private Context context;
	
	public ApiConnectorTask(Context context) {
		this.context = context;
	}
	
	protected String doInBackground(Map<String, Object>... params) {
		// Create result variable
		String result = "";
		try {
			// Create connection to server
		    URL url = new URL(API_URL + "/" + params[0].get("method"));
		    HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection(); 
		    urlConnection.setUseCaches(false);
			try {
			    // Set request as POST to post the parameters if they're set
			    if(params[0].containsKey("postParameters")) {
			    	urlConnection.setRequestMethod("POST");
			    	
 	 			    urlConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
	 				urlConnection.setDoOutput(true);
		 			urlConnection.setChunkedStreamingMode(0);
		
		 			// Post data to server
		 			String postParams = (String) params[0].get("postParameters");
		 			DataOutputStream writeStream = new DataOutputStream (urlConnection.getOutputStream());
		 			writeStream.writeBytes(postParams);
		 			writeStream.flush();
		 			writeStream.close();
			    }
			    	
	 			// Read result and return xml string
 				InputStreamReader inputStream = new InputStreamReader(urlConnection.getInputStream(), "UTF-8");
 				StringBuilder builder = new StringBuilder();
 				BufferedReader reader = new BufferedReader(inputStream);
 				String line;
 				while ((line = reader.readLine()) != null) {
 					builder.append(line);
 				} 				
 				result = builder.toString();
 				log(result);
 			} catch (Exception e) {
 				e.printStackTrace();
 			} finally {
 				urlConnection.disconnect();
 			}
			return result;
		} catch (SSLException e) {
			e.printStackTrace();				
			return null;
    	} catch (Exception e) {
    		e.printStackTrace();
    		return null;
    	}        	
    }    	
	
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
    }
    
    private void log(Object message) {
		if(logEnabled) Log.d(getClass().getSimpleName(), message.toString());
	}
}