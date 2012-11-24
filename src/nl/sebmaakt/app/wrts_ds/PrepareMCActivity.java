package nl.sebmaakt.app.wrts_ds;

import nl.sebmaakt.app.wrts_ds.database.DbAdapter;
import WrtsMobile.com.R;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;
import com.google.ads.AdView;

public class PrepareMCActivity extends Activity {
	
	private Cursor cursor;
	private DbAdapter dbHelper;	
	private Bundle extras;
	private SharedPreferences settings;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        FlurryAgent.onStartSession(this, "HELHFVX5C1HA5RQR48JJ");
	        setContentView(R.layout.preparemc);
	        extras = getIntent().getExtras();
	        settings = getSharedPreferences("wrtsapp", 0);
	        if(settings.contains("no-ads")) {
	        	AdView adview = (AdView) findViewById(R.id.adView);
	        	if(settings.getBoolean("no-ads", false)) {
	        		adview.setVisibility(AdView.GONE);
	        	}
	        }
	        dbHelper = new DbAdapter(this);
	        dbHelper.open();
	        cursor = dbHelper.fetchListData(extras.getString("id"));
	        RadioButton lang1 = (RadioButton) findViewById(R.id.lang1);
	        RadioButton lang2 = (RadioButton) findViewById(R.id.lang2);
	        RadioButton lang3 = (RadioButton) findViewById(R.id.lang3);
	        RadioButton lang4 = (RadioButton) findViewById(R.id.lang4);
	        RadioButton lang5 = (RadioButton) findViewById(R.id.lang5);
	        RadioButton lang6 = (RadioButton) findViewById(R.id.lang6);
	        RadioButton lang7 = (RadioButton) findViewById(R.id.lang7);
	        RadioButton lang8 = (RadioButton) findViewById(R.id.lang8);
	        RadioButton lang9 = (RadioButton) findViewById(R.id.lang9);
	        RadioButton lang10 = (RadioButton) findViewById(R.id.lang10);
	        lang1.setOnClickListener(radio_listener);
	        lang2.setOnClickListener(radio_listener);
	        lang3.setOnClickListener(radio_listener);
	        lang4.setOnClickListener(radio_listener);
	        lang5.setOnClickListener(radio_listener);
	        lang6.setOnClickListener(radio_listener);
	        lang7.setOnClickListener(radio_listener);
	        lang8.setOnClickListener(radio_listener);
	        lang9.setOnClickListener(radio_listener);
	        lang10.setOnClickListener(radio_listener);
	        if(!extras.getString("language").equals(cursor.getString(cursor.getColumnIndex("langa")))) { 
	        	lang1.setText(cursor.getString(cursor.getColumnIndex("langa")));
	        	lang1.setVisibility(TextView.VISIBLE);
	        }
	        if(!extras.getString("language").equals(cursor.getString(cursor.getColumnIndex("langb")))) { 
	        	lang2.setText(cursor.getString(cursor.getColumnIndex("langb")));
	        	lang2.setVisibility(TextView.VISIBLE);
	        }
	        if(!cursor.isNull(cursor.getColumnIndex("langc")) && !extras.getString("language").equals(cursor.getString(cursor.getColumnIndex("langc")))) {
	        	lang3.setText(cursor.getString(cursor.getColumnIndex("langc")));
	        	lang3.setVisibility(RadioButton.VISIBLE);
	        }
	        if(!cursor.isNull(cursor.getColumnIndex("langd")) && !extras.getString("language").equals(cursor.getString(cursor.getColumnIndex("langd")))) {
	        	lang4.setText(cursor.getString(cursor.getColumnIndex("langd")));
	        	lang4.setVisibility(RadioButton.VISIBLE);
	        }
	        if(!cursor.isNull(cursor.getColumnIndex("lange")) && !extras.getString("language").equals(cursor.getString(cursor.getColumnIndex("lange")))) {
	        	lang5.setText(cursor.getString(cursor.getColumnIndex("lange")));
	        	lang5.setVisibility(RadioButton.VISIBLE);
	        }
	        if(!cursor.isNull(cursor.getColumnIndex("langf")) && !extras.getString("language").equals(cursor.getString(cursor.getColumnIndex("langf")))) {
	        	lang6.setText(cursor.getString(cursor.getColumnIndex("langf")));
	        	lang6.setVisibility(RadioButton.VISIBLE);
	        }
	        if(!cursor.isNull(cursor.getColumnIndex("langg")) && !extras.getString("language").equals(cursor.getString(cursor.getColumnIndex("langg")))) {
	        	lang7.setText(cursor.getString(cursor.getColumnIndex("langg")));
	        	lang7.setVisibility(RadioButton.VISIBLE);
	        }
	        if(!cursor.isNull(cursor.getColumnIndex("langh")) && !extras.getString("language").equals(cursor.getString(cursor.getColumnIndex("langh")))) {
	        	lang8.setText(cursor.getString(cursor.getColumnIndex("langh")));
	        	lang8.setVisibility(RadioButton.VISIBLE);
	        }
	        if(!cursor.isNull(cursor.getColumnIndex("langi")) && !extras.getString("language").equals(cursor.getString(cursor.getColumnIndex("langi")))) {
	        	lang9.setText(cursor.getString(cursor.getColumnIndex("langi")));
	        	lang9.setVisibility(RadioButton.VISIBLE);
	        }
	        if(!cursor.isNull(cursor.getColumnIndex("langj")) && !extras.getString("language").equals(cursor.getString(cursor.getColumnIndex("langj")))) {
	        	lang10.setText(cursor.getString(cursor.getColumnIndex("langj")));
	        	lang10.setVisibility(RadioButton.VISIBLE);
	        }
	        cursor.close();
	        dbHelper.close();        
	}
	
	private OnClickListener radio_listener = new OnClickListener() {
	    public void onClick(View v) {
	        // Perform action on clicks
	        RadioButton rb = (RadioButton) v;
	        Intent intent = new Intent(v.getContext(), MultipleChoiceActivity.class);
        	Bundle b = new Bundle();
			b.putString("id", extras.getString("id"));
			b.putString("language", extras.getString("language"));
			b.putString("language1", rb.getText().toString());
			intent.putExtras(b);
			startActivity(intent);
			finish();
	    }
	};
	
	public void onStop() {
	   super.onStop();
	   FlurryAgent.onEndSession(this);
	}
	
}
