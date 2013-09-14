package nl.digischool.wrts.api;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HttpsURLConnection;
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
            URL url = new URL(Params.API_URL + "/" + this.mApiMethod);
            HttpsURLConnection urlConnection = null;
            if(url.getProtocol().toLowerCase().equals("https")) {
                urlConnection = (HttpsURLConnection) url.openConnection();
                urlConnection.setHostnameVerifier(Utilities.DO_NOT_VERIFY);
                Utilities.trustAllHosts();
            } else {
                urlConnection = (HttpsURLConnection) url.openConnection();
            }
            urlConnection.setRequestProperty("Authorization", "Basic " + this.mApiAuth);
            urlConnection.setRequestProperty("Accept-Encoding", "gzip");
            urlConnection.setRequestProperty("User-Agent", Params.USER_AGENT);
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
                InputStream inputStream = urlConnection.getInputStream();
                if(urlConnection.getContentEncoding().equals("gzip")) {
                    inputStream = new GZIPInputStream(inputStream);
                }
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                result = Utilities.convertToString(inputStreamReader);
                Utilities.log(LOG_TAG, result);
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
                result = builder.toString();
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