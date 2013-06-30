package nl.digischool.wrts.api;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.SSLException;

import nl.digischool.wrts.classes.Params;
import nl.digischool.wrts.classes.Utilities;

public class ApiConnector {

    private String mApiMethod, mApiOutput, mApiAuth;
    private final String LOG_TAG = getClass().getSimpleName();
    private Boolean mApiDoOutput;

    /**
     * ApiConnector without server post
     * @param method Method to be executed
     * @param auth Authentication Base64 string
     */
    public ApiConnector(String method, String auth) {
        this.mApiMethod = method;
        this.mApiAuth = auth;
        this.mApiDoOutput = false;
    }

    /**
     * ApiConnectorTask with post to server
     * @param method Method to be executed
     * @param auth Authentication Base64 string
     * @param output Postdata
     */
    public ApiConnector(String method, String auth, String output) {
        this.mApiMethod = method;
        this.mApiOutput = output;
        this.mApiAuth = auth;
        this.mApiDoOutput = true;
    }

    /**
     * Execute the HTTP request
     * @return String containg
     */
    public String execute() {
        // Create result variable
        String result = "";
        try {
            // Create connection to server
            URL url = new URL(Params.apiUrl + "/" + this.mApiMethod);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Authorization", "Basic " + this.mApiAuth);
            //urlConnection.setRequestProperty("User-Agent", Params.userAgent);

            urlConnection.setUseCaches(false);
            try {
                // Set request as POST to post the parameters if they're set
                if(this.mApiDoOutput) {
                    urlConnection.setRequestMethod("POST");

                    urlConnection.setRequestProperty("Content-Type","application/xml");
                    urlConnection.setDoOutput(true);
                    urlConnection.setChunkedStreamingMode(0);

                    // Post data to server
                    DataOutputStream writeStream = new DataOutputStream (urlConnection.getOutputStream());
                    writeStream.writeBytes(mApiOutput);
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
                Utilities.log(this.LOG_TAG, result);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                InputStreamReader inputStream = new InputStreamReader(urlConnection.getErrorStream(), "UTF-8");
                StringBuilder builder = new StringBuilder();
                BufferedReader reader = new BufferedReader(inputStream);
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                Utilities.log(LOG_TAG, "Errorstream: " + builder.toString());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                urlConnection.disconnect();
            }
            return result;
        } catch (SSLException e) {
            // TODO: Proper exception handling
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            // TODO: Proper exception handling
            return null;
        }
    }

}
