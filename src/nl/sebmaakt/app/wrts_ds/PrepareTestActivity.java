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
import android.widget.Button;
import android.widget.RadioButton;

import com.flurry.android.FlurryAgent;
import com.google.ads.AdView;

public class PrepareTestActivity extends Activity {
	
	private Cursor cursor;
	private DbAdapter dbHelper;	
	private String radiobutton;
	private Bundle extras;
	private SharedPreferences settings;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        FlurryAgent.onStartSession(this, "HELHFVX5C1HA5RQR48JJ");
	        setContentView(R.layout.preparetest);
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
	        lang1.setText(cursor.getString(cursor.getColumnIndex("langa")));
	        radiobutton = cursor.getString(cursor.getColumnIndex("langa"));
	        lang2.setText(cursor.getString(cursor.getColumnIndex("langb")));
	        if(!cursor.isNull(cursor.getColumnIndex("langc"))) {
	        	lang3.setText(cursor.getString(cursor.getColumnIndex("langc")));
	        	lang3.setVisibility(RadioButton.VISIBLE);
	        }
	        if(!cursor.isNull(cursor.getColumnIndex("langd"))) {
	        	lang4.setText(cursor.getString(cursor.getColumnIndex("langd")));
	        	lang4.setVisibility(RadioButton.VISIBLE);
	        }
	        if(!cursor.isNull(cursor.getColumnIndex("lange"))) {
	        	lang5.setText(cursor.getString(cursor.getColumnIndex("lange")));
	        	lang5.setVisibility(RadioButton.VISIBLE);
	        }
	        if(!cursor.isNull(cursor.getColumnIndex("langf"))) {
	        	lang6.setText(cursor.getString(cursor.getColumnIndex("langf")));
	        	lang6.setVisibility(RadioButton.VISIBLE);
	        }
	        if(!cursor.isNull(cursor.getColumnIndex("langg"))) {
	        	lang7.setText(cursor.getString(cursor.getColumnIndex("langg")));
	        	lang7.setVisibility(RadioButton.VISIBLE);
	        }
	        if(!cursor.isNull(cursor.getColumnIndex("langh"))) {
	        	lang8.setText(cursor.getString(cursor.getColumnIndex("langh")));
	        	lang8.setVisibility(RadioButton.VISIBLE);
	        }
	        if(!cursor.isNull(cursor.getColumnIndex("langi"))) {
	        	lang9.setText(cursor.getString(cursor.getColumnIndex("langi")));
	        	lang9.setVisibility(RadioButton.VISIBLE);
	        }
	        if(!cursor.isNull(cursor.getColumnIndex("langj"))) {
	        	lang10.setText(cursor.getString(cursor.getColumnIndex("langj")));
	        	lang10.setVisibility(RadioButton.VISIBLE);
	        }
	        cursor.close();
	        dbHelper.close();
	        Button Retype = (Button) findViewById(R.id.retypebutton);
	        Button MC = (Button) findViewById(R.id.mcbutton);
	        Button Mind = (Button) findViewById(R.id.mindbutton);
	        Button Translate = (Button) findViewById(R.id.translatebutton);
	        Retype.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                	//Toast.makeText(v.getContext(), radiobutton, Toast.LENGTH_SHORT).show();
                	Intent intent = new Intent(v.getContext(), RetypeActivity.class);
                	Bundle b = new Bundle();
    				b.putString("id", extras.getString("id"));
    				b.putString("language", radiobutton);
    				intent.putExtras(b);
    				startActivity(intent);
                }
        	});
	        MC.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                	RadioButton lang1 = (RadioButton) findViewById(R.id.lang1);
                	RadioButton lang2 = (RadioButton) findViewById(R.id.lang2);
                	RadioButton lang3 = (RadioButton) findViewById(R.id.lang3);
                	//Toast.makeText(v.getContext(), radiobutton, Toast.LENGTH_SHORT).show();
                	if(lang3.isShown()) {
                		Intent intent = new Intent(v.getContext(), PrepareMCActivity.class);
	                	Bundle b = new Bundle();
	    				b.putString("id", extras.getString("id"));
	    				b.putString("language", radiobutton);
	    				intent.putExtras(b);
	    				startActivity(intent);
                	} else {
	                	Intent intent = new Intent(v.getContext(), MultipleChoiceActivity.class);
	                	Bundle b = new Bundle();
	    				b.putString("id", extras.getString("id"));
	    				b.putString("language", radiobutton);
	    				if(radiobutton.equals(lang1.getText().toString())) {
	    					b.putString("language1", lang2.getText().toString());
	    				} else {
	    					b.putString("language1", lang1.getText().toString());
	    				}
	    				intent.putExtras(b);
	    				startActivity(intent);
                	}
                }
        	});
	        Mind.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                	//Toast.makeText(v.getContext(), radiobutton, Toast.LENGTH_SHORT).show();
                	Intent intent = new Intent(v.getContext(), InYourMindActivity.class);
                	Bundle b = new Bundle();
    				b.putString("id", extras.getString("id"));
    				b.putString("language", radiobutton);
    				intent.putExtras(b);
    				startActivity(intent);
                }
        	});
	        Translate.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                	Intent intent = new Intent(v.getContext(), TranslateActivity.class);
                	Bundle b = new Bundle();
    				b.putString("id", extras.getString("id"));
    				b.putString("language", radiobutton);
    				intent.putExtras(b);
    				startActivity(intent);
                }
        	});	        
	}
	
	private OnClickListener radio_listener = new OnClickListener() {
	    public void onClick(View v) {
	        // Perform action on clicks
	        RadioButton rb = (RadioButton) v;
	        radiobutton = rb.getText().toString();
	    }
	};
	
	@Override
	public void onBackPressed() {
		Intent intent = new Intent(getApplicationContext(), ShowListActivity.class);
		Bundle b = new Bundle();
		b.putString("id", extras.getString("id"));
		intent.putExtras(b);
		startActivity(intent);
		finish();
		return;
	}
	
	public void onStop() {
	   super.onStop();
	   FlurryAgent.onEndSession(this);
	}
	
}
