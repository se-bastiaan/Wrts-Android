package nl.digischool.wrts.api;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.SSLException;

import nl.digischool.wrts.classes.Params;
import nl.digischool.wrts.classes.Utilities;
import android.os.AsyncTask;

public class ApiConnectorTask extends AsyncTask<Void, Void, String> {
	
	private String API_METHOD, API_OUTPUT, API_AUTH;
	private String LOG_TAG = getClass().getSimpleName();
	private Boolean API_DO_OUTPUT;
    private ApiCallback CALLBACK = null;
	
	/**
	 * ApiConnectorTask without server post
	 * @param method
	 * @param auth
	 */
	public ApiConnectorTask(String method, String auth) {
		this.API_METHOD = method;
		this.API_AUTH = auth;
		this.API_DO_OUTPUT = false;
	}
	
	/**
	 * ApiConnectorTask with post to server
	 * @param method
	 * @param auth
	 * @param output
	 */
	public ApiConnectorTask(String method, String auth, String output) {
		this.API_METHOD = method;
		this.API_OUTPUT = output;
		this.API_AUTH = auth;
		this.API_DO_OUTPUT = true;
	}

    /**
     * set callback to perform in onPostExecute
     * @param callback
     */
    public void setCallback(ApiCallback callback) {
        this.CALLBACK = callback;
    }
	
	/**
	 * doInBackground connecting to the server en performing the request (GET or POST)
	 * @param params
	 * @return String
	 */
	protected String doInBackground(Void... params) {
		// Create result variable
		String result = "";
		try {
			// Create connection to server
		    URL url = new URL(Params.apiUrl + "/" + this.API_METHOD);
		    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection(); 
		    urlConnection.setRequestProperty("Authorization", "Basic " + this.API_AUTH);
		    urlConnection.setRequestProperty("User-Agent", Params.userAgent);
		    
		    urlConnection.setUseCaches(false);
			try {
			    // Set request as POST to post the parameters if they're set
			    if(this.API_DO_OUTPUT) {
			    	Utilities.log(this.LOG_TAG, "Do API Output");
			    	urlConnection.setRequestMethod("POST");
			    	
 	 			    urlConnection.setRequestProperty("Content-Type","application/xml");
	 				urlConnection.setDoOutput(true);
		 			urlConnection.setChunkedStreamingMode(0);
		
		 			// Post data to server
		 			DataOutputStream writeStream = new DataOutputStream (urlConnection.getOutputStream());
		 			writeStream.writeBytes(API_OUTPUT);
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
 				//Utilities.log(this.LOG_TAG, result);
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
        if(this.CALLBACK != null) CALLBACK.apiResponseCallback(result);
    }
    
}