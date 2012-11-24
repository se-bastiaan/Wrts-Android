package nl.sebmaakt.app.wrts_ds;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpProtocolParams;

import WrtsMobile.com.R;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;

public class LoginSettingsActivity extends Activity {
	
	SharedPreferences settings;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FlurryAgent.onStartSession(this, "HELHFVX5C1HA5RQR48JJ");
        setContentView(R.layout.logindatasettings);
        settings = getSharedPreferences("wrtsapp", 0);
        EditText Username = (EditText) findViewById(R.id.username);
    	EditText Password = (EditText) findViewById(R.id.password);    	
    	Username.setText(settings.getString("username", null));
    	Password.setText(settings.getString("password", null));
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Button Change = (Button) findViewById(R.id.login);
        Change.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	if(isOnline()) {
            		new Thread(new Runnable(){
            			public void run(){
                				try {
                					EditText Username = (EditText) findViewById(R.id.username);
            	                	EditText Password = (EditText) findViewById(R.id.password);    	
            	                	String username = Username.getText().toString();
            	                	String password = Password.getText().toString();
            	                	if (!username.equalsIgnoreCase("") && !password.equalsIgnoreCase("")) {
            	                		runOnUiThread(new Runnable() {
            							    public void run() {
            							    	EditText Username = (EditText) findViewById(R.id.username);
                        	                	EditText Password = (EditText) findViewById(R.id.password);
                        	                	ProgressBar Loading = (ProgressBar) findViewById(R.id.loading);
                        	                	Button Login = (Button) findViewById(R.id.login);
            							    	Username.setVisibility(EditText.GONE);
            							    	Password.setVisibility(EditText.GONE);
            							    	Login.setVisibility(Button.GONE);                							    	
            							    	Loading.setVisibility(ProgressBar.VISIBLE);
            							    }
            							}); 
                	                	DefaultHttpClient client = new DefaultHttpClient();
                	                	String locale = getResources().getConfiguration().locale.getLanguage() + "-" + getResources().getConfiguration().locale.getCountry();
                	                	String appversion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
                	                	HttpProtocolParams.setUserAgent(client.getParams(), "Mozilla/5.0 (Linux; U; Android "+Build.VERSION.RELEASE+"; "+locale+"; WrtsMobile.com; AndroidWrtsApp/"+appversion+")");
                						HttpUriRequest host = new HttpGet("http://www.wrds.eu/api/lists");
                						UsernamePasswordCredentials creds = new UsernamePasswordCredentials(username, password);
                						client.getCredentialsProvider().setCredentials(AuthScope.ANY, creds);
                						HttpResponse responseSource = client.execute(host);
                	    				int responseCode = responseSource.getStatusLine().getStatusCode();
                	    				Log.i("HTTPresponse", ""+responseCode);
                	    				if(responseCode == 200) {    
                	    					Map<String, String> Params = new HashMap<String, String>();
                	    					Params.put("email", username.toLowerCase());
                	    					//FlurryAgent.logEvent("Loggedin", Params);
                	    					SharedPreferences.Editor editor = settings.edit();
                	                       	editor.putString("username", username.toLowerCase());
                	                       	editor.putString("password", password);
                	                       	editor.commit();
                	                       	runOnUiThread(new Runnable() {
                							    public void run() {
                							    	EditText Username = (EditText) findViewById(R.id.username);
                            	                	EditText Password = (EditText) findViewById(R.id.password);
                            	                	ProgressBar Loading = (ProgressBar) findViewById(R.id.loading);
                            	                	Button Login = (Button) findViewById(R.id.login);
                            	                	Username.setVisibility(EditText.VISIBLE);
    	                	                		Password.setVisibility(EditText.VISIBLE);
    	                	                		Login.setVisibility(Button.VISIBLE);
    	                	                		Loading.setVisibility(ProgressBar.GONE);
                							    }
                							});	                	    					
                	    					runOnUiThread(new Runnable() {
                							    public void run() {
                							    	Toast.makeText(LoginSettingsActivity.this, getResources().getString(R.string.datachanged), Toast.LENGTH_SHORT).show();
                							    }
                							});
                	    				} else if (responseCode == 500 || responseCode == 404) {
                	    					runOnUiThread(new Runnable() {
                							    public void run() {
                							    	EditText Username = (EditText) findViewById(R.id.username);
                            	                	EditText Password = (EditText) findViewById(R.id.password);
                            	                	ProgressBar Loading = (ProgressBar) findViewById(R.id.loading);
                            	                	Button Login = (Button) findViewById(R.id.login);
                            	                	Username.setVisibility(EditText.VISIBLE);
    	                	                		Password.setVisibility(EditText.VISIBLE);
    	                	                		Login.setVisibility(Button.VISIBLE);
    	                	                		Loading.setVisibility(ProgressBar.GONE);
                							    }
                							});	                	    					
                	    					runOnUiThread(new Runnable() {
                							    public void run() {
                							    	Toast.makeText(LoginSettingsActivity.this, getResources().getString(R.string.serverfail), Toast.LENGTH_SHORT).show();
                							    }
                							}); 
                    					} else {
                    						runOnUiThread(new Runnable() {
                							    public void run() {
                							    	EditText Username = (EditText) findViewById(R.id.username);
                            	                	EditText Password = (EditText) findViewById(R.id.password);
                            	                	ProgressBar Loading = (ProgressBar) findViewById(R.id.loading);
                            	                	Button Login = (Button) findViewById(R.id.login);
                            	                	Username.setVisibility(EditText.VISIBLE);
    	                	                		Password.setVisibility(EditText.VISIBLE);
    	                	                		Login.setVisibility(Button.VISIBLE);
    	                	                		Loading.setVisibility(ProgressBar.GONE);
                							    }
                							});	
                	    					runOnUiThread(new Runnable() {
                							    public void run() {
                							    	Toast.makeText(LoginSettingsActivity.this, getResources().getString(R.string.incorrectlogin), Toast.LENGTH_SHORT).show();
                							    }
                							});                	    					
                	    				}
            	                	} else {
            	                		runOnUiThread(new Runnable() {
            							    public void run() {
            							    	Toast.makeText(LoginSettingsActivity.this, getResources().getString(R.string.emptylogin), Toast.LENGTH_SHORT).show();
            							    }
            							});
            	                	}
                				} catch (Exception e) {
                					//BugSenseHandler.log("WrtsApp", e);
            						e.printStackTrace();
            					}
                		}
                	}).start();
            	} else {
            		Toast.makeText(LoginSettingsActivity.this, getResources().getString(R.string.internetrequired), Toast.LENGTH_SHORT).show();
            	}
            }
        });
    }
    
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }
    
    public void onStop() {
		super.onStop();
		FlurryAgent.onEndSession(this);
	}
}