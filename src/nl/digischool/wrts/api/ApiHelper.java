package nl.digischool.wrts.api;

import android.content.Context;
import android.content.SharedPreferences;
import nl.digischool.wrts.classes.Base64;
import nl.digischool.wrts.classes.Params;
import nl.digischool.wrts.classes.Utilities;

public class ApiHelper {

    private SharedPreferences mSettings;
    private ApiCallback mCallback = null;
    private static final String
            NOT_AUTHENTICATED = "401: User Not Authenticated",
            NOT_FOUND = "404: Page Not Found";

    /**
     * Constructor
     * @param context Context of the Application, Activity or Service in which the helper is constructed.
     */
    public ApiHelper(Context context) {
        mSettings = context.getSharedPreferences(Params.PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    /**
     * Constructor
     * @param context Context of the Application, Activity or Service in which the helper is constructed.
     * @param callback Callback function to use in onPostExecute of ApiConnectorTask
     */
    public ApiHelper(Context context, ApiCallback callback) {
        mSettings = context.getSharedPreferences(Params.PREFERENCES_NAME, Context.MODE_PRIVATE);
        mCallback = callback;
    }

    /**
     * Set mCallback which has to be used
     * @param callback ApiCallback used to validate request
     */
    public void setCallback(ApiCallback callback) {
        mCallback = callback;
    }

    /**
     * Authenticate user on server. Returns true if userdata is correct.
     * @param email Email of user which has to be authenticated
     * @param password Password of user which has to be authenticated
     */
    public void authenticateUser(String email, String password) {
            String auth = Base64.encode((email+":"+password).getBytes());
            ApiConnectorTask connection = new ApiConnectorTask("users/me", auth);
            connection.setCallback(mCallback);
            try {
                connection.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    /**
     * Get Index of all lists
     */
    public void getIndex() {
        this.getString("");
    }

    /**
     * Get index of all lists containing all data except the words
     */
    public void getDetailIndex() {
        this.getString("lists");
    }

    /**
     * Get index of all lists containing all data except for the words of all changed lists since @date
     * @param date Date in various timestamps. YYYY-MM-DD preferred.
     */
    public void getDetailIndexSince(String date) {
        this.getString("lists?since="+date);
    }

    /**
     * Get specific list containing all data
     * @param id Unique identifier of a list
     */
    public void getList(Integer id) {
        this.getList(Integer.toString(id));
    }

    /**
     * Get specific list containing all data
     * @param id Unique identifier of a list
     */
    public void getList(String id) {
        this.getString("lists/"+id);
    }

    /**
     * Post specific list to save on server
     * @param id Unique identifier of a list
     * @param output XML of the new list
     */
    public void postList(Integer id, String output) {
        this.postList(Integer.toString(id), output);
    }

    /**
     * Post specific list to save on server
     * @param id Unique identifier of a list
     * @param output XML of the new list
     */
    public void postList(String id, String output) {
        this.postString("lists/"+id, output);
    }

    /**
     * Post new list to server
     * @param output XML containing all data of the new list
     */
    public void postList(String output) {
        this.postString("lists", output);
    }

    /**
     * Delete specific list from server
     * @param id Unique identifier of a list
     */
    public void deleteList(Integer id) {
        this.deleteList(Integer.toString(id));
    }

    /**
     * Delete specific list from server
     * @param id Unique identifier of a list
     */
    public void deleteList(String id) {
        this.postString("lists/"+id+"/delete", "");
    }

    /**
     * Post results of specific list to server
     * @param id Unique identifier of a list
     * @param output XML containing all results
     */
    public void postListResults(Integer id, String output) {
        this.postListResults(Integer.toString(id), output);
    }

    /**
     * Post results of specific list to server
     * @param id Unique identifier of a list
     * @param output XML containing all results
     */
    private void postListResults(String id, String output) {
        this.postString("lists/"+id+"/results", output);
    }

    /**
     * Main data get function
     * @param method String containing method to be executed
     */
    private void getString(String method) {
        ApiConnectorTask connection = new ApiConnectorTask(method, this.getAuthString());
        if(mCallback != null) {
            connection.setCallback(mCallback);
            connection.execute();
        } else {
            try {
                connection.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Main data post function
     * @param method String containing method to be executed
     * @param output String containing postdata
     */
    private void postString(String method, String output) {
        ApiConnectorTask connection = new ApiConnectorTask(method, this.getAuthString(), output);
        if(mCallback != null) {
            connection.setCallback(mCallback);
            connection.execute();
        } else {
            try {
                connection.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Save username and password
     * @param username Username (is email) of the user
     * @param password Password of the user
     */
    public void saveUserData(String username, String password) {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString("username", username);
        editor.putString("password", password);
        editor.commit();
    }

    /**
     * Get Base64 String containing the username and password to use in http basic authentication
     * @return String
     */
    public String getAuthString() {
        String username = mSettings.getString("username", "");
        String password = mSettings.getString("password", "");
        if(username.equals("") || password.equals("")) return ApiHelper.NOT_AUTHENTICATED;
        String userPassString = username+":"+password;
        log(userPassString);
        return Base64.encode(userPassString.getBytes());
    }

    private void log(Object message) {
        Utilities.log(getClass().getSimpleName(), message.toString());
    }

}