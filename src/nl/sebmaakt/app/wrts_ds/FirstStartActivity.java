package nl.sebmaakt.app.wrts_ds;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.zip.GZIPInputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpProtocolParams;
import org.xml.sax.InputSource;

import nl.sebmaakt.app.wrts_ds.database.DatabaseProvider;
import nl.sebmaakt.app.wrts_ds.database.DbAdapter;
import WrtsMobile.com.R;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crittercism.app.Crittercism;
import com.flurry.android.FlurryAgent;

public class FirstStartActivity extends Activity {
	
	private SharedPreferences settings;
	private DbAdapter dbHelper;
	private ListsCollector Collector = null;
	private ArrayList<ContentValues> listData;
	private ArrayList<ContentValues> wordData;
	private static String downloadedString;
	private static LinearLayout Loading;

	 @Override
	 public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);	        
	        FlurryAgent.onStartSession(this, "HELHFVX5C1HA5RQR48JJ");
	        FlurryAgent.logEvent("Logged in");
	        setContentView(R.layout.first_start);
	        settings = getSharedPreferences("wrtsapp", 0);
	        dbHelper = new DbAdapter(this);
	        Button Download = (Button) findViewById(R.id.download);
	        Download.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                	Button Download = (Button) findViewById(R.id.download);
                	Loading = (LinearLayout) findViewById(R.id.DialogLayout);
                	TextView Text1 = (TextView) findViewById(R.id.Text1);
                	Download.setVisibility(Button.GONE);
                	Loading.setVisibility(LinearLayout.VISIBLE);
                	Text1.setText(getResources().getString(R.string.downloadinglists));
                	if(isOnline()) {
                		new Thread(new Runnable(){
                			public void run(){
                    				try {
                    					dbHelper.open();
					                	String username = settings.getString("username", null);
					                	String password = settings.getString("password", null);
					                	if(username.equals(null) || password.equals(null)) {
					                		Intent main = new Intent(getApplicationContext(), WrtsAppActivity.class);
                							startActivity(main);
					                	}
					                	try {					                		
					                		DefaultHttpClient client = new DefaultHttpClient();
						                	String locale = getResources().getConfiguration().locale.getLanguage() + "-" + getResources().getConfiguration().locale.getCountry();
						                	String appversion;
											appversion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
											HttpProtocolParams.setUserAgent(client.getParams(), "Mozilla/5.0 (Linux; U; Android "+Build.VERSION.RELEASE+"; "+locale+"; WrtsMobile.com; AndroidWrtsApp/"+appversion+")");
											HttpUriRequest host = new HttpGet("https://www.wrts.nl/api/lists/all");
											downloadedString = getResources().getString(R.string.downloadedlists);
											host.setHeader("Accept-Encoding", "gzip");
											UsernamePasswordCredentials creds = new UsernamePasswordCredentials(username, password);
											client.getCredentialsProvider().setCredentials(AuthScope.ANY, creds);
											HttpResponse responseSource = client.execute(host);
						    				int responseCode = responseSource.getStatusLine().getStatusCode();
						    				//Log.i("HTTPresponse", ""+responseCode);
						    				if(responseCode == 200) {
						    					
						    					getContentResolver().query(DatabaseProvider.RECREATE_UPDATE_URI, null, null, null, null);
						    					InputStream instream = responseSource.getEntity().getContent();
				    							Header contentEncoding = responseSource.getFirstHeader("Content-Encoding");
				    							InputStreamReader inputstream;
				    							if (contentEncoding.getValue().equalsIgnoreCase("gzip")) {
				    								instream = new GZIPInputStream(instream);
				    							    inputstream = new InputStreamReader(instream);					    							    
				    							} else {
				    								inputstream = new InputStreamReader(instream);
				    							}				    							
						    					InputSource source = new InputSource(inputstream);
						    					    					    						
						    					SAXParserFactory factory = SAXParserFactory.newInstance();
					    						SAXParser saxParser = factory.newSAXParser();
					    						
					    						LijstenParser handler = new LijstenParser(FirstStartActivity.this);
					    						
					    						saxParser.parse(source, handler);
					    						inputstream.close();
					    						
					    						Collector = LijstenParser.Collector;		
					    						    					    						
					    						listData = Collector.getListData();
					    						wordData = Collector.getWordData();
					    						ContentValues[] listValuesData = new ContentValues[listData.size()];
					    						ContentValues[] wordValuesData = new ContentValues[wordData.size()];
					    						getContentResolver().query(DatabaseProvider.RECREATE_URI, null, null, null, null);
					    						Log.d("Array", wordData.toArray(wordValuesData).toString());
					    						getContentResolver().bulkInsert(DatabaseProvider.INSERT_WORDS_URI, wordData.toArray(wordValuesData));
					    						getContentResolver().bulkInsert(DatabaseProvider.INSERT_LISTS_URI, listData.toArray(listValuesData));
					    					} 	
					                	} catch (UnknownHostException e) {
						     					runOnUiThread(new Runnable() {
						 						    public void run() {
						 						    	Toast.makeText(getApplicationContext(), "Er kan geen contact gemaakt worden met de server.", Toast.LENGTH_SHORT).show();
						 						    }
						 						});  
						     					e.printStackTrace();
										} catch (Exception e) {											
											e.printStackTrace();
											Crittercism.logHandledException(e);
										}					                	
					                	SharedPreferences.Editor editor = settings.edit();
					                   	Calendar c = Calendar.getInstance(); 
					                   	editor.putString("downloaded", c.get(Calendar.YEAR)+"-"+c.get(Calendar.MONTH)+"-"+c.get(Calendar.DAY_OF_MONTH)+"T"+c.get(Calendar.HOUR_OF_DAY)+":"+c.get(Calendar.MINUTE)+":"+c.get(Calendar.SECOND)+"Z");
					                   	editor.commit();					                	
					                	dbHelper.close();
					                	runOnUiThread(new Runnable() {
            							    public void run() {
            							    	LinearLayout Loading = (LinearLayout) findViewById(R.id.DialogLayout);
            				                	TextView Text1 = (TextView) findViewById(R.id.Text1);
            							    	Loading.setVisibility(LinearLayout.GONE);
            							    	Text1.setText(getResources().getString(R.string.done));
            							    	Intent main = new Intent(getApplicationContext(), WrtsAppActivity.class);
            									startActivity(main);
            									finish();
            							    }
            							});
                    				} catch (Exception e) {
                						e.printStackTrace();
                						Crittercism.logHandledException(e);
                					}
                    		}
                    	}).start();
	                } else {
	            		Toast.makeText(FirstStartActivity.this, getResources().getString(R.string.internetrequired), Toast.LENGTH_SHORT).show();
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

	public static void increaseDownloaded(int count) {
		TextView text = (TextView) Loading.findViewById(R.id.text1);
		text.setText(count + " " + downloadedString);		
	}
}
