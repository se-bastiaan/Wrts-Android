package nl.sebmaakt.app.wrts_ds;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
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
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpProtocolParams;
import org.xml.sax.InputSource;
import org.xmlpull.v1.XmlSerializer;

import nl.sebmaakt.app.wrts_ds.database.DatabaseProvider;
import nl.sebmaakt.app.wrts_ds.database.DbAdapter;
import WrtsMobile.com.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.crittercism.app.Crittercism;
import com.flurry.android.FlurryAgent;

public class SettingsActivity extends Activity {
	
	private String error;
	private DbAdapter dbHelper;
	private SharedPreferences settings;
	private static String downloadedString;
	private static Dialog Loading;
	private ListsCollector Collector = null;
	private ArrayList<ContentValues> listData;
	private ArrayList<ContentValues> wordData;
	private int deletedcount = 0;
	private int updatedcount = 0;
	private int createdcount = 0;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FlurryAgent.onStartSession(this, "HELHFVX5C1HA5RQR48JJ");
        FlurryAgent.logEvent("Settings");
        setContentView(R.layout.settings);
        settings = getSharedPreferences("wrtsapp", 0);    
        Button LegalButton = (Button) findViewById(R.id.legalinfo);
        Button LoginSetButton = (Button) findViewById(R.id.loginsettings);
        Button TestSetButton = (Button) findViewById(R.id.testsettings);
        Button FullSyncButton = (Button) findViewById(R.id.fullsync);
        LoginSetButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent intent = new Intent(getApplicationContext(), LoginSettingsActivity.class);
            	startActivity(intent);
            }
        });
        TestSetButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent intent = new Intent(getApplicationContext(), TestSettingsActivity.class);
            	startActivity(intent);
            }
        });
        FullSyncButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	fullsync();
            }
        });
        LegalButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
            	builder.setMessage(getResources().getString(R.string.information))
            		   .setTitle(getResources().getString(R.string.legal))
            	       .setCancelable(true)
            	       .setNeutralButton("Ok!", new DialogInterface.OnClickListener() {
            	           public void onClick(DialogInterface dialog, int id) {
            	                dialog.cancel();
            	           }
            	       });
            	AlertDialog alert = builder.create();
            	alert.show();
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
    
    @Override
	public void onBackPressed() {
		Intent intent = new Intent(getApplicationContext(), WrtsAppActivity.class);
		startActivity(intent);
		finish();
		return;
	}
    
    public void onStop() {
		super.onStop();
		FlurryAgent.onEndSession(this);
	}
    
    public void fullsync() {
	    if(isOnline()) {
	        new Thread(new Runnable(){
	                    public void run() {
	                                    try {                                                           
	                                            runOnUiThread(new Runnable() {
	                                                                public void run() {
	                                                                    Loading = new Dialog(SettingsActivity.this);
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
	                                                    dbHelper = new DbAdapter(SettingsActivity.this);
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
	                                                                                            HttpPost post = new HttpPost("https://www.wrts.nl/api/lists/"+deleted.get(d)+"/delete");
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
	                                                                                            HttpPost post = new HttpPost("https://www.wrts.nl/api/lists/"+updated.get(u));
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
	                                                                                            HttpPost post = new HttpPost("https://www.wrts.nl/api/lists");
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
	                                                                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.serverfail),Toast.LENGTH_LONG).show();
	                                                                                }
	                                                                            });  
	                                                                    //BugSenseHandler.log("WrtsApp", e);
	                                                    } catch (Exception e) {
	                                                            error = e.getMessage();
	                                                            runOnUiThread(new Runnable() {
	                                                                                    public void run() {
	                                                                                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.error) + error, Toast.LENGTH_LONG).show();
	                                                                                    }
	                                                                            });
	                                                            e.printStackTrace();
	                                                            Crittercism.logHandledException(e);
	                                                    }
	                                            } catch (Exception e) {
	                                                    error = e.getMessage();
	                                                    runOnUiThread(new Runnable() {
	                                                                            public void run() {
	                                                                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.error) + error, Toast.LENGTH_LONG).show();
	                                                                            }
	                                                                    });
	                                                    e.printStackTrace();   
	                                                    Crittercism.logHandledException(e);
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
	                                                                    
	                                                                    LijstenParser handler = new LijstenParser(SettingsActivity.this);
	                                                                    
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
	                                                            Crittercism.logHandledException(e);
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
	                                            editor.putString("downloaded", c.get(Calendar.YEAR)+"-"+c.get(Calendar.MONTH)+"-"+c.get(Calendar.DAY_OF_MONTH)+"T"+c.get(Calendar.HOUR_OF_DAY)+":"+c.get(Calendar.MINUTE)+":"+c.get(Calendar.SECOND)+"Z");
	    					                   	editor.commit();
	                                            runOnUiThread(new Runnable() {
	                                                                public void run() {
	                                                                    Loading.dismiss();	                                                                    
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
	            Toast.makeText(SettingsActivity.this, getResources().getString(R.string.internetrequired), Toast.LENGTH_SHORT).show();
	    }
    }
    
    public final static void increaseDownloaded(int count) {
    	TextView text = (TextView) Loading.findViewById(R.id.text4);
		text.setText(count + " " + downloadedString);
	}
}