package nl.sebmaakt.app.wrts_ds;

import java.text.DecimalFormat;

import WrtsMobile.com.R;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;
import com.google.ads.AdView;

public class TestResultsActivity extends Activity {

	private SharedPreferences settings;
	private Bundle extras;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        FlurryAgent.onStartSession(this, "HELHFVX5C1HA5RQR48JJ");
        setContentView(R.layout.results);
        
        TextView Method = (TextView) findViewById(R.id.methodtext);
        TextView Order = (TextView) findViewById(R.id.ordertext);
        TextView Time = (TextView) findViewById(R.id.timetext);
        TextView Note = (TextView) findViewById(R.id.note);
        TextView Correct = (TextView) findViewById(R.id.countcorrect);
        TextView Incorrect = (TextView) findViewById(R.id.countincorrect);
        extras = getIntent().getExtras();
        Method.setText(extras.getString("method"));
        Order.setText(extras.getString("langa")+" - "+extras.getString("langb"));
        long time = extras.getLong("time") / 1000;
        String seconds = Integer.toString((int)(time % 60)) + " "+getResources().getString(R.string.seconds);
        String minutes = Integer.toString((int)((time % 3600) / 60));
        String hours = Integer.toString((int)(time / 3600));
        if (!hours.equals("0")) {
        	hours = hours + " "+getResources().getString(R.string.hours)+", ";
        } else if (hours.equals("1")) {
        	hours = hours + " "+getResources().getString(R.string.hour)+", ";
        } else {
        	hours = "";
        }
        if (!minutes.equals("0")) {
        	minutes = minutes + " "+getResources().getString(R.string.minutes)+" "+getResources().getString(R.string.and)+" ";
        } else if (minutes.equals("1")) {
        	minutes = minutes + " "+getResources().getString(R.string.minute)+" "+getResources().getString(R.string.and)+" ";
        } else {
        	minutes = "";
        }
        Time.setText(hours+minutes+seconds);
        double count = (double) 10 / (double) extras.getInt("total");
        double note = count * (double) extras.getInt("correct");
        if(note > 5.4 && note < 7.4) {
        	Note.setTextColor(getResources().getColor(R.color.orange));
        } else if(note < 5.5) {
        	Note.setTextColor(getResources().getColor(R.color.red));
        } else if(note > 7.4) {
        	Note.setTextColor(getResources().getColor(R.color.green));
        }
        settings = getSharedPreferences("wrtsapp", 0);
        DecimalFormat form = new DecimalFormat("#.#");
        DecimalFormat form1 = new DecimalFormat("#");
        if(settings.getInt("resultstype", 0) == 1) {
        	note = note * 10;
        	Note.setText(form1.format(note).toString() + "%");
        } else {
        	Note.setText(form.format(note).toString());
        }
        Correct.setText(Integer.toString(extras.getInt("correct")));
        Incorrect.setText(Integer.toString(extras.getInt("incorrect")));
        if(settings.contains("no-ads")) {
        	AdView adview = (AdView) findViewById(R.id.adView);
        	if(settings.getBoolean("no-ads", false)) {
        		adview.setVisibility(AdView.GONE);
        	}
        }
	}
	
	public void onStop() {
		super.onStop();
		FlurryAgent.onEndSession(this);
	}
}
