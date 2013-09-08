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
	
	private String mApiMethod, mApiOutput, mApiAuth;
	private String LOG_TAG = getClass().getSimpleName();
	private Boolean mApiDoOutput;
    private ApiCallback mCallback = null;
	
	/**
	 * ApiConnectorTask without server post
	 * @param method
	 * @param auth
	 */
	public ApiConnectorTask(String method, String auth) {
		this.mApiMethod = method;
		this.mApiAuth = auth;
		this.mApiDoOutput = false;
	}
	
	/**
	 * ApiConnectorTask with post to server
	 * @param method
	 * @param auth
	 * @param output
	 */
	public ApiConnectorTask(String method, String auth, String output) {
		this.mApiMethod = method;
		this.mApiOutput = output;
		this.mApiAuth = auth;
		this.mApiDoOutput = true;
	}

    /**
     * set mCallback to perform in onPostExecute
     * @param callback Callback
     */
    public void setCallback(ApiCallback callback) {
        this.mCallback = callback;
    }
	
	/**
	 * doInBackground connecting to the server en performing the request (GET or POST)
	 * @param params
	 * @return String
	 */
	protected String doInBackground(Void... params) {
        ApiConnector connector = null;
        if(mApiDoOutput) {
            connector = new ApiConnector(mApiMethod, mApiAuth, mApiOutput);
        } else {
            connector = new ApiConnector(mApiMethod, mApiAuth);
        }
        String result = connector.execute();
		return result;
    }    	
	
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if(mCallback != null) mCallback.apiResponseCallback(mApiMethod, result);
    }
    
}