package nl.sebmaakt.app.wrts_ds;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HostnameVerifier;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpProtocolParams;
import org.xml.sax.InputSource;
import org.xmlpull.v1.XmlSerializer;

import nl.sebmaakt.app.wrts_ds.database.DatabaseProvider;
import nl.sebmaakt.app.wrts_ds.database.DbAdapter;
import WrtsMobile.com.R;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;
import com.google.ads.AdView;
import com.crittercism.app.Crittercism;

public class WrtsAppActivity extends FragmentActivity implements LoaderManager.LoaderCallbacks<Cursor> {
	
	private SharedPreferences settings;
	private SimpleCursorAdapter adapter;
	private String error;
	private static Dialog Loading;
	private DbAdapter dbHelper;
	private ListsCollector Collector = null;
	private ArrayList<ContentValues> listData;
	private ArrayList<ContentValues> wordData;
	private int deletedcount = 0;
	private int updatedcount = 0;
	private int createdcount = 0;
	private static String downloadedString;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Crittercism.init(getApplicationContext(), "4f3c2e67b0931562c8000310");
        FlurryAgent.onStartSession(this, "HELHFVX5C1HA5RQR48JJ");
        setContentView(R.layout.main);
        
        //Log.w(getPackageName(), "Gestart!");
        settings = getSharedPreferences("wrtsapp", 0);
        if(settings.contains("no-ads")) {
        	AdView adview = (AdView) findViewById(R.id.adView);
        	if(settings.getBoolean("no-ads", false)) {
        		adview.setVisibility(AdView.GONE);
        	}
        }
        
        HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
		SchemeRegistry registry = new SchemeRegistry();
	    SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
	    socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
	    registry.register(new Scheme("https", socketFactory, 443));
        
        ImageButton settingsb = (ImageButton) findViewById(R.id.SettingsButton);
        ImageButton newb = (ImageButton) findViewById(R.id.NewButton);
        ImageButton syncb = (ImageButton) findViewById(R.id.SyncButton);
        LinearLayout Lists = (LinearLayout) findViewById(R.id.Lists);
        ScrollView FirstStart = (ScrollView) findViewById(R.id.FirstStart);
        if(!settings.contains("username") && !settings.contains("downloaded")) {
        	this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        	settingsb.setVisibility(ImageButton.GONE);
        	newb.setVisibility(ImageButton.GONE);
        	syncb.setVisibility(ImageButton.GONE);
        	Lists.setVisibility(LinearLayout.GONE);
        	FirstStart.setVisibility(ScrollView.VISIBLE);
        	Button Login = (Button) findViewById(R.id.login);
        	Button Register = (Button) findViewById(R.id.register);
        	Register.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                	Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.registerurl)));
					startActivity(browserIntent);
                }
        	});
        	Login.setOnClickListener(new View.OnClickListener() {
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
                            	                	Button Register = (Button) findViewById(R.id.register);
                            	                	Register.setVisibility(Button.GONE);
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
	                						HttpUriRequest host = new HttpGet("http://www.wrts.nl/api/lists");
	                						UsernamePasswordCredentials creds = new UsernamePasswordCredentials(username, password);
	                						client.getCredentialsProvider().setCredentials(AuthScope.ANY, creds);
	                						HttpResponse responseSource = client.execute(host);
	                	    				int responseCode = responseSource.getStatusLine().getStatusCode();
	                	    				Log.i("HTTPresponse", ""+responseCode);
	                	    				if(responseCode == 200) {	                	    					
	                	    					SharedPreferences.Editor editor = settings.edit();
	                	                       	editor.putString("username", username.toLowerCase());
	                	                       	editor.putString("password", password);
	                	                       	editor.putInt("resultstype", 1);
	                	                       	editor.putInt("practicetype", 0);
	                	                       	editor.putBoolean("uppercase", true);
	                	                       	editor.putBoolean("accents", true);
	                	                       	editor.putBoolean("punctuation", true);
	                	                       	editor.commit();
	                	                       	Crittercism.setUsername(username.toLowerCase());
	                							Intent main = new Intent(getApplicationContext(), FirstStartActivity.class);
	                							startActivity(main);
	                							finish();
	                	    				} else if (responseCode == 500 || responseCode == 404) {
	                	    					runOnUiThread(new Runnable() {
	                							    public void run() {
	                							    	EditText Username = (EditText) findViewById(R.id.username);
	                            	                	EditText Password = (EditText) findViewById(R.id.password);
	                            	                	ProgressBar Loading = (ProgressBar) findViewById(R.id.loading);
	                            	                	Button Login = (Button) findViewById(R.id.login);
	                            	                	Button Register = (Button) findViewById(R.id.register);
	                            	                	Register.setVisibility(Button.VISIBLE);
	                            	                	Username.setVisibility(EditText.VISIBLE);
	    	                	                		Password.setVisibility(EditText.VISIBLE);
	    	                	                		Login.setVisibility(Button.VISIBLE);
	    	                	                		Loading.setVisibility(ProgressBar.GONE);
	                							    }
	                							});	                	    					
	                	    					runOnUiThread(new Runnable() {
	                							    public void run() {
	                							    	Toast.makeText(WrtsAppActivity.this, getResources().getString(R.string.serverfail), Toast.LENGTH_SHORT).show();
	                							    }
	                							}); 
	                    					} else {
	                    						runOnUiThread(new Runnable() {
	                							    public void run() {
	                							    	EditText Username = (EditText) findViewById(R.id.username);
	                            	                	EditText Password = (EditText) findViewById(R.id.password);
	                            	                	ProgressBar Loading = (ProgressBar) findViewById(R.id.loading);
	                            	                	Button Login = (Button) findViewById(R.id.login);
	                            	                	Button Register = (Button) findViewById(R.id.register);
	                            	                	Register.setVisibility(Button.VISIBLE);
	                            	                	Username.setVisibility(EditText.VISIBLE);
	    	                	                		Password.setVisibility(EditText.VISIBLE);
	    	                	                		Login.setVisibility(Button.VISIBLE);
	    	                	                		Loading.setVisibility(ProgressBar.GONE);
	                							    }
	                							});	
	                	    					runOnUiThread(new Runnable() {
	                							    public void run() {
	                							    	Toast.makeText(WrtsAppActivity.this, getResources().getString(R.string.incorrectlogin), Toast.LENGTH_SHORT).show();
	                							    }
	                							});                	    					
	                	    				}
                	                	} else {
                	                		runOnUiThread(new Runnable() {
                							    public void run() {
                							    	Toast.makeText(WrtsAppActivity.this, getResources().getString(R.string.emptylogin), Toast.LENGTH_SHORT).show();
                							    }
                							});
                	                	}
                    				} catch (Exception e) {
                    					Crittercism.logHandledException(e);
                					}
                    		}
                    	}).start();
                	} else {
                		Toast.makeText(WrtsAppActivity.this, getResources().getString(R.string.internetrequired), Toast.LENGTH_SHORT).show();
                	}
                }
            });
        } else if (!settings.contains("downloaded")) {
        	Crittercism.setUsername(settings.getString("username", "guest"));
        	Intent main = new Intent(getApplicationContext(), FirstStartActivity.class);
			startActivity(main);
			finish();
        } else {
        	//FlurryAgent.logEvent("Startup");
        	Crittercism.setUsername(settings.getString("username", "guest"));
        	settingsb.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                	Intent settings = new Intent(v.getContext(), SettingsActivity.class);
                	startActivityForResult(settings, 0);
                }
            });
        	newb.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
    				Intent intent = new Intent(v.getContext(), EditListActivity.class);
    				Bundle b = new Bundle();
    				b.putString("type", "title");
    				b.putString("new", "ja");
    				intent.putExtras(b);
    				startActivity(intent);
    			}
            });
        	try {
		        settingsb.setOnClickListener(new View.OnClickListener() {
		            public void onClick(View v) {
		            	Intent settings = new Intent(v.getContext(), SettingsActivity.class);
		            	startActivityForResult(settings, 0);
		            }
		        });
		        try {		
				String[] from = new String[] { "langa", "langb" };
				int[] to = new int[] { R.id.langa, R.id.langb };
				
				getSupportLoaderManager().initLoader(1, null, this);
				
				adapter = new SimpleCursorAdapter(this,
						R.layout.langs_listitem, null, from, to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
				ListView listview = (ListView) findViewById(R.id.ListsView);
				listview.setAdapter(adapter);
				
				listview.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						ListView listview = (ListView) findViewById(R.id.ListsView);
						View listsview = listview.getAdapter().getView(position,null,null);
						TextView langa = (TextView) listsview.findViewById(R.id.langa);
						TextView langb = (TextView) listsview.findViewById(R.id.langb);
						Intent intent = new Intent(view.getContext(), ListsActivity.class);
						Bundle b = new Bundle();
						b.putString("langa", (String) langa.getText());
						b.putString("langb", (String) langb.getText());
						intent.putExtras(b);
						startActivity(intent);
					}
		        });
		        } catch (Exception e) {
		        	Crittercism.logHandledException(e);
		        	Toast.makeText(getApplicationContext(), "Er is een fout opgetreden!", Toast.LENGTH_LONG).show();
		        }
        	} catch (Exception e) {
        		Crittercism.logHandledException(e);
        		Intent main = new Intent(getApplicationContext(), FirstStartActivity.class);
				startActivity(main);
        	}
        	syncb.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
    	            	if(isOnline()) {
    	            		new Thread(new Runnable(){
    	            			public void run() {
    	                				try {	                					
    	                					runOnUiThread(new Runnable() {
    										    public void run() {
    										    	Loading = new Dialog(WrtsAppActivity.this);
    										    	Loading.setTitle(getResources().getString(R.string.syncing));
    										    	Loading.setContentView(R.layout.syncing_dialog);
    										    	Loading.setCancelable(false);
    												Loading.show();
    												TextView text1 = (TextView) Loading.findViewById(R.id.text1);
    												text1.setText("0 "+getResources().getString(R.string.deletedlists));
    												TextView text2 = (TextView) Loading.findViewById(R.id.text2);
    												text2.setText("0 "+getResources().getString(R.string.editedlists));
    												TextView text3 = (TextView) Loading.findViewById(R.id.text3);
    												text3.setText("0 "+getResources().getString(R.string.madelists));
    												TextView text4 = (TextView) Loading.findViewById(R.id.text4);
    												text4.setText("0 "+getResources().getString(R.string.downloadedlists));
    										    }
    			    						});
    	                					
    					                	String username = settings.getString("username", null);
    					                	String password = settings.getString("password", null);
    					                	try {
    					                		dbHelper = new DbAdapter(WrtsAppActivity.this);
    						                	dbHelper.open();
    						                	ArrayList<String> deleted  = dbHelper.fetchStatus(1);
    						                	ArrayList<String> updated  = dbHelper.fetchStatus(2);
    						                	ArrayList<String> created  = dbHelper.fetchStatus(3);	
    						                	dbHelper.close();
    					                	StringWriter stringOut;
    					                	XmlSerializer serializer = Xml.newSerializer();
    						                	try {
    						                		
    							                	if(deleted.size() != 0 && deleted != null) {   							                		
    							                		for(int d = 0; d < deleted.size(); d++) {
    							                			runOnUiThread(new Runnable() {
        													    public void run() {
        													    	deletedcount++;
        							                				TextView text = (TextView) Loading.findViewById(R.id.text1);
        							                				text.setText(deletedcount+" "+getResources().getString(R.string.deletedlists));
        													    }
    							                			});
    							                			DefaultHttpClient client = new DefaultHttpClient();    							                		
    									                	String locale = getResources().getConfiguration().locale.getLanguage() + "-" + getResources().getConfiguration().locale.getCountry();
    									                	String appversion;
    														appversion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
    														HttpProtocolParams.setUserAgent(client.getParams(), "Mozilla/5.0 (Linux; U; Android "+Build.VERSION.RELEASE+"; "+locale+"; WrtsMobile.com; AndroidWrtsApp/"+appversion+")");
    														HttpPost post = new HttpPost("http://www.wrts.nl/api/lists/"+deleted.get(d)+"/delete");
    								    					post.setHeader("Content-Type", "text/xml");    								    					
    														UsernamePasswordCredentials creds = new UsernamePasswordCredentials(username, password);
    														client.getCredentialsProvider().setCredentials(AuthScope.ANY, creds);
    														HttpResponse responseSource = client.execute(post);
    														InputStreamReader inputstream = new InputStreamReader(responseSource.getEntity().getContent());
    								    					StringBuilder builder = new StringBuilder();
    														BufferedReader reader = new BufferedReader(inputstream);
    														inputstream.toString();
    														String line;
    														while ((line = reader.readLine()) != null) {
    															builder.append(line);
    														}
    														Log.i("deleted", builder.toString());
    							                		}
    							                	}

    							                	if(updated.size() != 0 && updated != null) {    							                		
    							                		for(int u = 0; u < updated.size(); u++) {
    							                			runOnUiThread(new Runnable() {
        													    public void run() {
        													    	updatedcount++;
        							                				TextView text = (TextView) Loading.findViewById(R.id.text2);
        							                				text.setText(updatedcount+" "+getResources().getString(R.string.editedlists));
        													    }
    							                			});
    							                			stringOut = null;
    							                			stringOut = new StringWriter();
    									                	serializer.setOutput(stringOut);
    							                			Log.i("created", updated.get(u));
    							                			Cursor list = getContentResolver().query(Uri.parse(DatabaseProvider.LISTDATA_URI.toString() + "/" + updated.get(u)), null, null, null, null);
    							                			Cursor cursor = getContentResolver().query(Uri.parse(DatabaseProvider.LISTS_URI.toString()+"/"+updated.get(u)), null, null, null, null);
    							                			serializer.startDocument(null, Boolean.valueOf(true));
    							                			serializer.startTag(null, "list");
    							                			serializer.startTag(null, "title");
    							                				serializer.text(list.getString(list.getColumnIndex("title")));
    							                			serializer.endTag(null, "title");
    							                			for(int i = 0; i < list.getCount(); i++) {
    						                					if(!list.isNull(list.getColumnIndex("langa"))) {
    						                						serializer.startTag(null, "lang-a");
    								                					serializer.text(list.getString(list.getColumnIndex("langa")));
    								                				serializer.endTag(null, "lang-a");
    						                					}
    						                					if(!list.isNull(list.getColumnIndex("langb"))) {
    						                						serializer.startTag(null, "lang-b");
    								                					serializer.text(list.getString(list.getColumnIndex("langb")));
    								                				serializer.endTag(null, "lang-b");
    						                					}
    						                					if(!list.isNull(list.getColumnIndex("langc"))) {
    						                						serializer.startTag(null, "lang-c");
    								                					serializer.text(list.getString(list.getColumnIndex("langc")));
    								                				serializer.endTag(null, "lang-c");
    						                					}
    						                					if(!list.isNull(list.getColumnIndex("langd"))) {
    						                						serializer.startTag(null, "lang-d");
    								                					serializer.text(list.getString(list.getColumnIndex("langd")));
    								                				serializer.endTag(null, "lang-d");
    						                					}
    						                					if(!list.isNull(list.getColumnIndex("lange"))) {
    						                						serializer.startTag(null, "lang-e");
    								                					serializer.text(list.getString(list.getColumnIndex("lange")));
    								                				serializer.endTag(null, "lang-e");
    						                					}
    						                					if(!list.isNull(list.getColumnIndex("langf"))) {
    						                						serializer.startTag(null, "lang-f");
    								                					serializer.text(list.getString(list.getColumnIndex("langf")));
    								                				serializer.endTag(null, "lang-f");
    						                					}
    						                					if(!list.isNull(list.getColumnIndex("langg"))) {
    						                						serializer.startTag(null, "lang-g");
    								                					serializer.text(list.getString(list.getColumnIndex("langg")));
    								                				serializer.endTag(null, "lang-g");
    						                					}
    						                					if(!list.isNull(list.getColumnIndex("langh"))) {
    						                						serializer.startTag(null, "lang-h");
    								                					serializer.text(list.getString(list.getColumnIndex("langh")));
    								                				serializer.endTag(null, "lang-h");
    						                					}
    						                					if(!list.isNull(list.getColumnIndex("langi"))) {
    						                						serializer.startTag(null, "lang-i");
    								                					serializer.text(list.getString(list.getColumnIndex("langi")));
    								                				serializer.endTag(null, "lang-i");
    						                					}
    						                					if(!list.isNull(list.getColumnIndex("langj"))) {
    						                						serializer.startTag(null, "lang-j");
    								                					serializer.text(list.getString(list.getColumnIndex("langj")));
    								                				serializer.endTag(null, "lang-j");
    						                					}
    						                				}
    							                			serializer.startTag(null, "shared");
    							                				serializer.text("false");
    							                			serializer.endTag(null, "shared");
    							                			serializer.startTag(null, "keywords");
    							                			serializer.endTag(null, "keywords");
    							                			serializer.startTag(null, "words");
    							                				for(int createlist = 0; createlist < cursor.getCount(); createlist++) {
    							                					serializer.startTag(null, "word");
    							                					if(!cursor.isNull(cursor.getColumnIndex("worda"))) {
    							                						serializer.startTag(null, "word-a");
    									                					serializer.text(cursor.getString(cursor.getColumnIndex("worda")));
    									                				serializer.endTag(null, "word-a");
    							                					}
    							                					if(!cursor.isNull(cursor.getColumnIndex("wordb"))) {
    							                						serializer.startTag(null, "word-b");
    									                					serializer.text(cursor.getString(cursor.getColumnIndex("wordb")));
    									                				serializer.endTag(null, "word-b");
    							                					}
    							                					if(!cursor.isNull(cursor.getColumnIndex("wordc"))) {
    							                						serializer.startTag(null, "word-c");
    									                					serializer.text(cursor.getString(cursor.getColumnIndex("wordc")));
    									                				serializer.endTag(null, "word-c");
    							                					}
    							                					if(!cursor.isNull(cursor.getColumnIndex("wordd"))) {
    							                						serializer.startTag(null, "word-d");
    									                					serializer.text(cursor.getString(cursor.getColumnIndex("wordd")));
    									                				serializer.endTag(null, "word-d");
    							                					}
    							                					if(!cursor.isNull(cursor.getColumnIndex("worde"))) {
    							                						serializer.startTag(null, "word-e");
    									                					serializer.text(cursor.getString(cursor.getColumnIndex("worde")));
    									                				serializer.endTag(null, "word-e");
    							                					}
    							                					if(!cursor.isNull(cursor.getColumnIndex("wordf"))) {
    							                						serializer.startTag(null, "word-f");
    									                					serializer.text(cursor.getString(cursor.getColumnIndex("wordf")));
    									                				serializer.endTag(null, "word-f");
    							                					}
    							                					if(!cursor.isNull(cursor.getColumnIndex("wordg"))) {
    							                						serializer.startTag(null, "word-g");
    									                					serializer.text(cursor.getString(cursor.getColumnIndex("wordg")));
    									                				serializer.endTag(null, "word-g");
    							                					}
    							                					if(!cursor.isNull(cursor.getColumnIndex("wordh"))) {
    							                						serializer.startTag(null, "word-h");
    									                					serializer.text(cursor.getString(cursor.getColumnIndex("wordh")));
    									                				serializer.endTag(null, "word-h");
    							                					}
    							                					if(!cursor.isNull(cursor.getColumnIndex("wordi"))) {
    							                						serializer.startTag(null, "word-i");
    									                					serializer.text(cursor.getString(cursor.getColumnIndex("wordi")));
    									                				serializer.endTag(null, "word-i");
    							                					}
    							                					if(!cursor.isNull(cursor.getColumnIndex("wordj"))) {
    							                						serializer.startTag(null, "word-j");
    									                					serializer.text(cursor.getString(cursor.getColumnIndex("wordj")));
    									                				serializer.endTag(null, "word-j");
    							                					}
    							                					serializer.endTag(null, "word");
    							                					cursor.moveToNext();
    							                				}
    							                			serializer.endTag(null, "words");
    							                			serializer.endTag(null, "list");
    							                			serializer.flush();
    							                			Log.i("created", stringOut.toString());
    							                			stringOut.close();
    							                			String requestXml = stringOut.toString();
    							                			DefaultHttpClient client = new DefaultHttpClient();
    									                	String locale = getResources().getConfiguration().locale.getLanguage() + "-" + getResources().getConfiguration().locale.getCountry();
    									                	String appversion;
    														appversion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
    														HttpProtocolParams.setUserAgent(client.getParams(), "Mozilla/5.0 (Linux; U; Android "+Build.VERSION.RELEASE+"; "+locale+"; WrtsMobile.com; AndroidWrtsApp/"+appversion+")");
    														HttpPost post = new HttpPost("http://www.wrts.nl/api/lists/"+updated.get(u));
    														post.setEntity(new StringEntity(requestXml));
    								    					post.setHeader("Content-Type", "text/xml");
    														UsernamePasswordCredentials creds = new UsernamePasswordCredentials(username, password);
    														client.getCredentialsProvider().setCredentials(AuthScope.ANY, creds);
    														HttpResponse responseSource = client.execute(post);
    														InputStreamReader inputstream = new InputStreamReader(responseSource.getEntity().getContent());
    								    					StringBuilder builder = new StringBuilder();
    														BufferedReader reader = new BufferedReader(inputstream);
    														String line;
    														while ((line = reader.readLine()) != null) {
    															builder.append(line);
    														}
    														Log.i("created", builder.toString());
    							                		}
    							                	}							                	
    							                	if(created.size() != 0 && created != null) {
    							                		runOnUiThread(new Runnable() {
    													    public void run() {
    													    	createdcount++;
    							                				TextView text = (TextView) Loading.findViewById(R.id.text3);
    							                				text.setText(createdcount+" "+getResources().getString(R.string.madelists));
    													    }
							                			});
    							                		for(int c = 0; c < created.size(); c++) {
    							                			stringOut = null;
    							                			stringOut = new StringWriter();
    									                	serializer.setOutput(stringOut);
    							                			Log.i("created", created.get(c));
    							                			Cursor list = getContentResolver().query(Uri.parse(DatabaseProvider.LISTDATA_URI.toString()+"/"+created.get(c)), null, null, null, null);
    							                			Cursor cursor = getContentResolver().query(Uri.parse(DatabaseProvider.LISTS_URI.toString()+"/"+created.get(c)), null, null, null, null);
    							                			Log.i("created", cursor.getCount() + "");
    							                			Log.i("created", list.getCount() + "");
    							                			serializer.startDocument(null, Boolean.valueOf(true));
    							                			serializer.startTag(null, "list");
    							                			serializer.startTag(null, "title");
    							                				serializer.text(list.getString(list.getColumnIndex("title")));
    							                			serializer.endTag(null, "title");
    							                			for(int i = 0; i < list.getCount(); i++) {
    						                					if(!list.isNull(list.getColumnIndex("langa"))) {
    						                						serializer.startTag(null, "lang-a");
    								                					serializer.text(list.getString(list.getColumnIndex("langa")));
    								                				serializer.endTag(null, "lang-a");
    						                					}
    						                					if(!list.isNull(list.getColumnIndex("langb"))) {
    						                						serializer.startTag(null, "lang-b");
    								                					serializer.text(list.getString(list.getColumnIndex("langb")));
    								                				serializer.endTag(null, "lang-b");
    						                					}
    						                					if(!list.isNull(list.getColumnIndex("langc"))) {
    						                						serializer.startTag(null, "lang-c");
    								                					serializer.text(list.getString(list.getColumnIndex("langc")));
    								                				serializer.endTag(null, "lang-c");
    						                					}
    						                					if(!list.isNull(list.getColumnIndex("langd"))) {
    						                						serializer.startTag(null, "lang-d");
    								                					serializer.text(list.getString(list.getColumnIndex("langd")));
    								                				serializer.endTag(null, "lang-d");
    						                					}
    						                					if(!list.isNull(list.getColumnIndex("lange"))) {
    						                						serializer.startTag(null, "lang-e");
    								                					serializer.text(list.getString(list.getColumnIndex("lange")));
    								                				serializer.endTag(null, "lang-e");
    						                					}
    						                					if(!list.isNull(list.getColumnIndex("langf"))) {
    						                						serializer.startTag(null, "lang-f");
    								                					serializer.text(list.getString(list.getColumnIndex("langf")));
    								                				serializer.endTag(null, "lang-f");
    						                					}
    						                					if(!list.isNull(list.getColumnIndex("langg"))) {
    						                						serializer.startTag(null, "lang-g");
    								                					serializer.text(list.getString(list.getColumnIndex("langg")));
    								                				serializer.endTag(null, "lang-g");
    						                					}
    						                					if(!list.isNull(list.getColumnIndex("langh"))) {
    						                						serializer.startTag(null, "lang-h");
    								                					serializer.text(list.getString(list.getColumnIndex("langh")));
    								                				serializer.endTag(null, "lang-h");
    						                					}
    						                					if(!list.isNull(list.getColumnIndex("langi"))) {
    						                						serializer.startTag(null, "lang-i");
    								                					serializer.text(list.getString(list.getColumnIndex("langi")));
    								                				serializer.endTag(null, "lang-i");
    						                					}
    						                					if(!list.isNull(list.getColumnIndex("langj"))) {
    						                						serializer.startTag(null, "lang-j");
    								                					serializer.text(list.getString(list.getColumnIndex("langj")));
    								                				serializer.endTag(null, "lang-j");
    						                					}
    						                				}
    							                			serializer.startTag(null, "shared");
    							                				serializer.text("false");
    							                			serializer.endTag(null, "shared");
    							                			serializer.startTag(null, "keywords");
    							                			serializer.endTag(null, "keywords");
    							                			serializer.startTag(null, "words");
    							                				for(int createlist = 0; createlist < cursor.getCount(); createlist++) {
    							                					serializer.startTag(null, "word");
    							                					if(!cursor.isNull(cursor.getColumnIndex("worda"))) {
    							                						serializer.startTag(null, "word-a");
    									                					serializer.text(cursor.getString(cursor.getColumnIndex("worda")));
    									                				serializer.endTag(null, "word-a");
    							                					}
    							                					if(!cursor.isNull(cursor.getColumnIndex("wordb"))) {
    							                						serializer.startTag(null, "word-b");
    									                					serializer.text(cursor.getString(cursor.getColumnIndex("wordb")));
    									                				serializer.endTag(null, "word-b");
    							                					}
    							                					if(!cursor.isNull(cursor.getColumnIndex("wordc"))) {
    							                						serializer.startTag(null, "word-c");
    									                					serializer.text(cursor.getString(cursor.getColumnIndex("wordc")));
    									                				serializer.endTag(null, "word-c");
    							                					}
    							                					if(!cursor.isNull(cursor.getColumnIndex("wordd"))) {
    							                						serializer.startTag(null, "word-d");
    									                					serializer.text(cursor.getString(cursor.getColumnIndex("wordd")));
    									                				serializer.endTag(null, "word-d");
    							                					}
    							                					if(!cursor.isNull(cursor.getColumnIndex("worde"))) {
    							                						serializer.startTag(null, "word-e");
    									                					serializer.text(cursor.getString(cursor.getColumnIndex("worde")));
    									                				serializer.endTag(null, "word-e");
    							                					}
    							                					if(!cursor.isNull(cursor.getColumnIndex("wordf"))) {
    							                						serializer.startTag(null, "word-f");
    									                					serializer.text(cursor.getString(cursor.getColumnIndex("wordf")));
    									                				serializer.endTag(null, "word-f");
    							                					}
    							                					if(!cursor.isNull(cursor.getColumnIndex("wordg"))) {
    							                						serializer.startTag(null, "word-g");
    									                					serializer.text(cursor.getString(cursor.getColumnIndex("wordg")));
    									                				serializer.endTag(null, "word-g");
    							                					}
    							                					if(!cursor.isNull(cursor.getColumnIndex("wordh"))) {
    							                						serializer.startTag(null, "word-h");
    									                					serializer.text(cursor.getString(cursor.getColumnIndex("wordh")));
    									                				serializer.endTag(null, "word-h");
    							                					}
    							                					if(!cursor.isNull(cursor.getColumnIndex("wordi"))) {
    							                						serializer.startTag(null, "word-i");
    									                					serializer.text(cursor.getString(cursor.getColumnIndex("wordi")));
    									                				serializer.endTag(null, "word-i");
    							                					}
    							                					if(!cursor.isNull(cursor.getColumnIndex("wordj"))) {
    							                						serializer.startTag(null, "word-j");
    									                					serializer.text(cursor.getString(cursor.getColumnIndex("wordj")));
    									                				serializer.endTag(null, "word-j");
    							                					}
    							                					serializer.endTag(null, "word");
    							                					cursor.moveToNext();
    							                				}
    							                			serializer.endTag(null, "words");
    							                			serializer.endTag(null, "list");
    							                			serializer.flush();
    							                			Log.i("created", stringOut.toString());
    							                			stringOut.close();
    							                			String requestXml = stringOut.toString();
    							                			DefaultHttpClient client = new DefaultHttpClient();
    									                	String locale = getResources().getConfiguration().locale.getLanguage() + "-" + getResources().getConfiguration().locale.getCountry();
    									                	String appversion;
    														appversion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
    														HttpProtocolParams.setUserAgent(client.getParams(), "Mozilla/5.0 (Linux; U; Android "+Build.VERSION.RELEASE+"; "+locale+"; WrtsMobile.com; AndroidWrtsApp/"+appversion+")");
    														HttpPost post = new HttpPost("http://www.wrts.nl/api/lists");
    														post.setEntity(new StringEntity(requestXml));
    								    					post.setHeader("Content-Type", "text/xml");
    														UsernamePasswordCredentials creds = new UsernamePasswordCredentials(username, password);
    														client.getCredentialsProvider().setCredentials(AuthScope.ANY, creds);
    														HttpResponse responseSource = client.execute(post);
    														InputStreamReader inputstream = new InputStreamReader(responseSource.getEntity().getContent());
    								    					StringBuilder builder = new StringBuilder();
    														BufferedReader reader = new BufferedReader(inputstream);
    														inputstream.toString();
    														String line;
    														while ((line = reader.readLine()) != null) {
    															builder.append(line);
    														}
    														Log.i("created", builder.toString());
    							                		}							                		
    							                	}
    							                	runOnUiThread(new Runnable() {
    												    public void run() {
    												    	
    												    }
    				    							});							                	
    						                	} catch (UnknownHostException e) {
    						     					runOnUiThread(new Runnable() {
    						 						    public void run() {
    						 						    	Toast.makeText(getApplicationContext(), getResources().getString(R.string.serverfail), Toast.LENGTH_SHORT).show();
    						 						    }
    						 						});  
    						     					//BugSenseHandler.log("WrtsApp", e);
    						                	} catch (Exception e) {
    						                		Crittercism.logHandledException(e);
    						                		error = e.getMessage();
    						                		runOnUiThread(new Runnable() {
    													public void run() {
    														Toast.makeText(getApplicationContext(), getResources().getString(R.string.error) + error, Toast.LENGTH_LONG).show();
    													}
    												});
    						                		e.printStackTrace();					                		
    						                	}
    					                	} catch (Exception e) {
    					                		Crittercism.logHandledException(e);
    					                		error = e.getMessage();
    					                		runOnUiThread(new Runnable() {
    												public void run() {
    													Toast.makeText(getApplicationContext(), getResources().getString(R.string.error) + error, Toast.LENGTH_LONG).show();
    												}
    											});
    					                		e.printStackTrace();					                		
    					                	}
    					                	try {	      		
    					                		getContentResolver().query(DatabaseProvider.RECREATE_UPDATE_URI, null, null, null, null);
    						                	DefaultHttpClient client = new DefaultHttpClient();
    						                	String locale = getResources().getConfiguration().locale.getLanguage() + "-" + getResources().getConfiguration().locale.getCountry();
    						                	String appversion;
    											appversion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
    											HttpProtocolParams.setUserAgent(client.getParams(), "Mozilla/5.0 (Linux; U; Android "+Build.VERSION.RELEASE+"; "+locale+"; WrtsMobile.com; AndroidWrtsApp/"+appversion+")");
    											HttpUriRequest host = new HttpGet("http://www.wrts.nl/api/");
    											downloadedString = getResources().getString(R.string.downloadedlists);
    											host.setHeader("Accept-Encoding", "gzip");
    											UsernamePasswordCredentials creds = new UsernamePasswordCredentials(username, password);
    											client.getCredentialsProvider().setCredentials(AuthScope.ANY, creds);
    											HttpResponse responseSource = client.execute(host);
    						    				int responseCode = responseSource.getStatusLine().getStatusCode();
    						    				//Log.i("HTTPresponse", ""+responseCode);
    						    				if(responseCode == 200) {
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
    					    						
    					    						IndexParser indexhandler = new IndexParser();
    					    						
    					    						saxParser.parse(source, indexhandler);
    					    						inputstream.close();
    					    						
    					    						Collector = IndexParser.Collector;		
    					    						Log.d("Syncing1", "Syncing1");	  
    					    						Log.d("Syncing2", Integer.toString(Collector.getId().size()));
    					    						String whereClause = "";
    					    						if(Collector.getId().size() > 0) {
	    					    						for(int i = 0; i < Collector.getId().size(); i++) {
	    					    							whereClause = whereClause  + Collector.getId().get(i).toString();
	    					    							if(i != (Collector.getId().size() - 1)) whereClause = whereClause + ",";
	    					    						}
	    					    						Log.d("Syncing1", "Syncing1");
	    					    						Log.d("Syncing1", whereClause);
	    					    						
	    					    						getContentResolver().delete(DatabaseProvider.WORDS_URI, " id NOT IN ("+whereClause+")", null);
	    					    						getContentResolver().delete(DatabaseProvider.LISTS_URI, " id NOT IN ("+whereClause+")", null);
    					    						}
    					    						
    					    						
    						    					host = new HttpGet("http://www.wrts.nl/api/lists/all?since="+settings.getString("downloaded", "1997-1-1"));
        											host.setHeader("Accept-Encoding", "gzip");
        											Log.d("date", settings.getString("downloaded", "1997-1-1"));
        											client.getCredentialsProvider().setCredentials(AuthScope.ANY, creds);
        											responseSource = client.execute(host);
        						    				responseCode = responseSource.getStatusLine().getStatusCode();
        						    				//Log.i("HTTPresponse", ""+responseCode);
        						    				if(responseCode == 200) {
        						    					instream = responseSource.getEntity().getContent();
    					    							contentEncoding = responseSource.getFirstHeader("Content-Encoding");
    					    							if (contentEncoding.getValue().equalsIgnoreCase("gzip")) {
    					    								instream = new GZIPInputStream(instream);
    					    							    inputstream = new InputStreamReader(instream);					    							    
    					    							} else {
    					    								inputstream = new InputStreamReader(instream);
    					    							}				    							
        						    					source = new InputSource(inputstream);
        						    					    					    						
        						    					factory = SAXParserFactory.newInstance();
        					    						saxParser = factory.newSAXParser();
        					    						
        					    						LijstenParser handler = new LijstenParser(WrtsAppActivity.this);
        					    						
        					    						saxParser.parse(source, handler);
        					    						inputstream.close();
        					    						
        					    						Collector = LijstenParser.Collector;
        					    						    					    						
        					    						listData = Collector.getListData();
        					    						wordData = Collector.getWordData();        					    						
        					    						
        					    						whereClause = "";
        					    						for(int i = 0; i < listData.size(); i++) {
        					    							whereClause = whereClause  + listData.get(i).getAsString("id");
        					    							if(i != (listData.size() - 1)) whereClause = whereClause + ",";
        					    						}
        					    						Log.d("Syncing2", whereClause);
        					    						
        					    						getContentResolver().delete(DatabaseProvider.WORDS_URI, " id IN ("+whereClause+")", null);
        					    						getContentResolver().delete(DatabaseProvider.LISTS_URI, " id IN ("+whereClause+")", null);
        					    						
        					    						ContentValues[] listValuesData = new ContentValues[listData.size()];
        					    						ContentValues[] wordValuesData = new ContentValues[wordData.size()];
        					    						Log.d("Array", wordData.toArray(wordValuesData).toString());
        					    						getContentResolver().bulkInsert(DatabaseProvider.INSERT_WORDS_URI, wordData.toArray(wordValuesData));
        					    						getContentResolver().bulkInsert(DatabaseProvider.INSERT_LISTS_URI, listData.toArray(listValuesData));
        					    						
        					    					} else {
        					    						runOnUiThread(new Runnable() {
            					 						    public void run() {
            					 						    	Toast.makeText(getApplicationContext(), getResources().getString(R.string.serverfail), Toast.LENGTH_SHORT).show();
            					 						    }
            					 						});
        					    					}
    					    					} else {
    					    						runOnUiThread(new Runnable() {
        					 						    public void run() {
        					 						    	Toast.makeText(getApplicationContext(), getResources().getString(R.string.serverfail), Toast.LENGTH_SHORT).show();
        					 						    }
        					 						});
    					    					}
    					                	} catch (UnknownHostException e) {
    					     					runOnUiThread(new Runnable() {
    					 						    public void run() {
    					 						    	Toast.makeText(getApplicationContext(), getResources().getString(R.string.serverfail), Toast.LENGTH_SHORT).show();
    					 						    }
    					 						});  
    					     					//BugSenseHandler.log("WrtsApp", e);
    										} catch (Exception e) {
    											Crittercism.logHandledException(e);
    					                		error = e.getMessage();
    					                		runOnUiThread(new Runnable() {
    												public void run() {
    													Toast.makeText(getApplicationContext(), getResources().getString(R.string.error) + error, Toast.LENGTH_LONG).show();
    												}
    											});
    					                		e.printStackTrace();
    										}
    					                	
    					                	Calendar c = Calendar.getInstance();
    					                	SharedPreferences.Editor editor = settings.edit();
    					                   	editor.putString("downloaded", c.get(Calendar.YEAR)+"-"+(int) ((int) c.get(Calendar.MONTH)+ (int)1)+"-"+c.get(Calendar.DAY_OF_MONTH)+"T"+c.get(Calendar.HOUR_OF_DAY)+":"+c.get(Calendar.MINUTE)+":"+c.get(Calendar.SECOND)+"Z");
    					                   	editor.commit();
    					                	runOnUiThread(new Runnable() {
    										    public void run() {
    										    	Loading.dismiss();
    										    	getSupportLoaderManager().restartLoader(1, null, WrtsAppActivity.this);
    										    	Toast.makeText(getApplicationContext(), getResources().getString(R.string.done), Toast.LENGTH_LONG).show();
    										    }
    					                	});	
    	                				} catch (Exception e) {
    	                					Crittercism.logHandledException(e);
    				                		error = e.getMessage();
    				                		runOnUiThread(new Runnable() {
    											public void run() {
    												Toast.makeText(getApplicationContext(), getResources().getString(R.string.error) + error, Toast.LENGTH_LONG).show();
    											}
    										});
    				                		e.printStackTrace();
    	            					}
    	                		}
    	                	}).start();
    	                } else {
    	            		Toast.makeText(WrtsAppActivity.this, getResources().getString(R.string.internetrequired), Toast.LENGTH_SHORT).show();
    	            	}
    	            }
            });
        }
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

	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		CursorLoader cursorLoader = new CursorLoader(this, DatabaseProvider.FETCHLANGS_URI, null, null, null, null);
	    return cursorLoader;
	}

	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		if(cursor.getCount() > 0) {
			adapter.swapCursor(cursor);
		}
	}

	public void onLoaderReset(Loader<Cursor> loader) {
		adapter.swapCursor(null);
	}
	
	public final static String asUpperCaseFirstChar(final String target) {

	    if ((target == null) || (target.length() == 0)) {
	        return target; // You could omit this check and simply live with an
	                       // exception if you like
	    }
	    return Character.toUpperCase(target.charAt(0))
	            + (target.length() > 1 ? target.substring(1) : "");
	}
	
	public final static void increaseDownloaded(int count) {
    	TextView text = (TextView) Loading.findViewById(R.id.text4);
		text.setText(count + " " + downloadedString);
	}	
}