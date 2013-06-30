package nl.digischool.wrts.api;

import nl.digischool.wrts.classes.Base64;
import nl.digischool.wrts.classes.Params;
import nl.digischool.wrts.classes.Utilities;
import android.content.Context;
import android.content.SharedPreferences;

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
        mSettings = context.getSharedPreferences(Params.preferencesName, Context.MODE_PRIVATE);
    }

    /**
     * Constructor
     * @param context Context of the Application, Activity or Service in which the helper is constructed.
     * @param callback Callback function to use in onPostExecute of ApiConnectorTask
     */
    public ApiHelper(Context context, ApiCallback callback) {
        mSettings = context.getSharedPreferences(Params.preferencesName, Context.MODE_PRIVATE);
        mCallback = callback;
    }

    /**
     * Set mCallback which has to be used
     * @param callback
     */
    public void setCallback(ApiCallback callback) {
        mCallback = callback;
    }

    /**
     * Authenticate user on server. Returns true if userdata is correct.
     * @param email Email of user which has to be authenticated
     * @param password Password of user which has to be authenticated
     * @return Boolean True if userdata is OK
     */
    public Boolean authenticateUser(String email, String password) {
            String auth = Base64.encode((email+":"+password).getBytes());
            ApiConnectorTask connection = new ApiConnectorTask("users/me", auth);
            try {
                String result = connection.execute().get();
                if(!result.contains("error code=\"401\"")) return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
    }

    /**
     * Get Index of all lists
     * @return String XML containing the index of all lists
     */
    public String getIndex() {
        return this.getString("");
    }

    /**
     * Get index of all lists containing all data except the words
     * @return String XML containing all data except the words of all lists
     */
    public String getDetailIndex() {
        return this.getString("lists");
    }

    /**
     * Get index of all lists containing all data except for the words of all changed lists since @date
     * @param date Date in various timestamps. YYYY-MM-DD preferred.
     * @return String XML containing all data except for the words of all changed lists since @date
     */
    public String getDetailIndexSince(String date) {
        return this.getString("lists?since="+date);
    }

    /**
     * Get specific list containing all data
     * @param id Unique identifier of a list
     * @return String XML containing all data of a list
     */
    public String getList(Integer id) {
        return this.getList(Integer.toString(id));
    }

    /**
     * Get specific list containing all data
     * @param id Unique identifier of a list
     * @return String XML containing all data of a list
     */
    public String getList(String id) {
        return this.getString("lists/"+id);
    }

    /**
     * Post specific list to save on server
     * @param id Unique identifier of a list
     * @param output XML of the new list
     * @return String XML containing response code
     */
    public String postList(Integer id, String output) {
        return this.postList(Integer.toString(id), output);
    }

    /**
     * Post specific list to save on server
     * @param id Unique identifier of a list
     * @param output XML of the new list
     * @return String XML containing response code
     */
    public String postList(String id, String output) {
        return this.postString("lists/"+id, output);
    }

    /**
     * Post new list to server
     * @param output XML containing all data of the new list
     * @return String XML containing response code
     */
    public String postList(String output) {
        return this.postString("lists", output);
    }

    /**
     * Delete specific list from server
     * @param id Unique identifier of a list
     * @return String XML containing response code
     */
    public String deleteList(Integer id) {
        return this.deleteList(Integer.toString(id));
    }

    /**
     * Delete specific list from server
     * @param id Unique identifier of a list
     * @return String XML containing response code
     */
    public String deleteList(String id) {
        return this.postString("lists/"+id+"/delete", "");
    }

    /**
     * Post results of specific list to server
     * @param id Unique identifier of a list
     * @param output XML containing all results
     * @return String XML containing response code
     */
    public String postListResults(Integer id, String output) {
        return this.postListResults(Integer.toString(id), output);
    }

    /**
     * Post results of specific list to server
     * @param id Unique identifier of a list
     * @param output XML containing all results
     * @return String XML containing response code
     */
    private String postListResults(String id, String output) {
        return this.postString("lists/"+id+"/results", output);
    }

    /**
     * Main data get function
     * @param method String containing method to be executed
     * @return String
     */
    private String getString(String method) {
        ApiConnectorTask connection = new ApiConnectorTask(method, this.getAuthString());
        if(mCallback != null) {
            connection.setCallback(mCallback);
            connection.execute();
            return "Returned to mCallback";
        } else {
            try {
                String result = connection.execute().get();
                if(!result.contains("error code=\"401\"")) {
                    return result;
                } else {
                    return ApiHelper.NOT_AUTHENTICATED;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Main data post function
     * @param method String containing method to be executed
     * @param output String containing postdata
     * @return String
     */
    private String postString(String method, String output) {
        ApiConnectorTask connection = new ApiConnectorTask(method, this.getAuthString(), output);
        if(mCallback != null) {
            connection.setCallback(mCallback);
            connection.execute();
            return "Returned to mCallback";
        } else {
            try {
                String result = connection.execute().get();
                if(!result.contains("error code=\"401\"")) {
                    return result;
                } else {
                    return ApiHelper.NOT_AUTHENTICATED;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
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