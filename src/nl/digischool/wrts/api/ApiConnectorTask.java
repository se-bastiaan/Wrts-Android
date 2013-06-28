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
        ApiConnector connector = null;
        if(this.API_DO_OUTPUT) {
            connector = new ApiConnector(this.API_METHOD, this.API_AUTH, this.API_OUTPUT);
        } else {
            connector = new ApiConnector(this.API_METHOD, this.API_AUTH);
        }

		return connector.execute();
    }    	
	
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if(this.CALLBACK != null) CALLBACK.apiResponseCallback(result);
    }
    
}