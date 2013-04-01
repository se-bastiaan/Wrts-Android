package nl.digischool.wrts.api;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class ApiHelper {

        private Context context;
        private SharedPreferences settings;
        
        public ApiHelper(Context context) {
                this.context = context;
        }
        
        /*
         * Method to authenticate user
         * 
         * Returns 'true' if user information is correct
         */
        @SuppressWarnings("unchecked")
        public Boolean authenticateUser(String email, String password) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("method", "users/me");
                ApiConnectorTask connection = new ApiConnectorTask(context);
                try {
                        String result = connection.execute(map).get();
                        if(!result.contains("error code=\"401\"")) return true;
                } catch (Exception e) {
                        e.printStackTrace();
                }
                return false;
        }
        
        public String getLoginData() {
                String username = settings.getString("username", "");
                String password = settings.getString("password", "");
                return username+":"+password;
        }
        
        @SuppressWarnings("unused")
        private void log(Object message) {
                Log.d(getClass().getSimpleName(), message.toString());
        }
}