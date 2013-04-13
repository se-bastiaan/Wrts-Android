package nl.digischool.wrts.api;

import nl.digischool.wrts.classes.Base64;
import nl.digischool.wrts.classes.Utilities;
import android.content.Context;
import android.content.SharedPreferences;

public class ApiHelper {

        private SharedPreferences settings;
        
        public ApiHelper(Context context) {
        	settings = context.getSharedPreferences("wrts_userdata", Context.MODE_PRIVATE);
        }
        
        /**
         * Authenticate user on server. Returns true if userdata is correct.
         * @param email
         * @param password
         * @return Boolean
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
         * @return
         */
        public String getIndex() {
        	return this.getString("");        	
        }
        
        /**
         * Get index of all lists containing all data except for the words
         * @return
         */
        public String getDetailIndex() {
        	return this.getString("lists");
        }
        
        /**
         * Get specific list containing all data
         * @param id
         * @return
         */
        public String getList(Integer id) {
        	return this.getList(Integer.toString(id));
        }
        
        /**
         * Get specific list containing all data
         * @param id
         * @return
         */
        public String getList(String id) {
        	return this.getString("lists/"+id);
        }
        
        /**
         * Post specific list to save on server
         * @param id
         * @param output
         * @return
         */
        public String postList(Integer id, String output) {
        	return this.postList(Integer.toString(id), output);
        }
        
        /**
         * Post specific list to save on server
         * @param id
         * @param output
         * @return
         */
        public String postList(String id, String output) {
        	return this.postString("lists/"+id, output);
        }
        
        /**
         * Post new list to server
         * @param output
         * @return
         */
        public String postList(String output) {
        	return this.postString("lists", output);
        }
        
        /**
         * Delete specific list from server
         * @param id
         * @return
         */
        public String deleteList(Integer id) {
        	return this.deleteList(Integer.toString(id));
        }

		/**
         * Delete specific list from server
         * @param id
         * @return
         */
        public String deleteList(String id) {
        	return this.postString("lists/"+id+"/delete", "");
        }
        
        /**
         * Post results of specific list to server
         * @param id
         * @param output
         * @return
         */
        public String postListResults(Integer id, String output) {
        	return this.postListResults(Integer.toString(id), output);
        }
        
        /**
         * Post results of specific list to server
         * @param id
         * @param output
         * @return
         */
        private String postListResults(String id, String output) {
			return this.postString("lists/"+id+"/results", output);
		}
        
        /**
         * Main data get function
         * @param method
         * @return
         */
        private String getString(String method) {
        	ApiConnectorTask connection = new ApiConnectorTask(method, this.getAuthString());
            try {
            	String result = connection.execute().get();
            	if(!result.contains("error code=\"401\"")) return result;
            } catch (Exception e) {
            	e.printStackTrace();
            }
            return null;
        }
        
        /**
         * Main data post function
         * @param method
         * @return
         */
        private String postString(String method, String output) {
        	ApiConnectorTask connection = new ApiConnectorTask(method, this.getAuthString(), output);
            try {
            	String result = connection.execute().get();
            	if(!result.contains("error code=\"401\"")) return result;
            } catch (Exception e) {
            	e.printStackTrace();
            }
            return null;
        }
        
        /**
         * Save username and password
         * @param username
         * @param password
         */
        public void saveUserData(String username, String password) {
        	SharedPreferences.Editor editor = settings.edit();
        	editor.putString("username", username);
        	editor.putString("password", password);
        	editor.commit();
        }
        
        /**
         * Get Base64 String containing the username and password to use in http basic authentication
         * @return String
         */
        public String getAuthString() {
        	String username = settings.getString("username", "");
        	String password = settings.getString("password", "");
        	if(username.equals("") || password.equals("")) return "Not authenticated";
        	String userpassString = username+":"+password;
        	log(userpassString);
        	return Base64.encode(userpassString.getBytes());
        }
        
        private void log(Object message) {
        	Utilities.log(getClass().getSimpleName(), message.toString());
        }
}