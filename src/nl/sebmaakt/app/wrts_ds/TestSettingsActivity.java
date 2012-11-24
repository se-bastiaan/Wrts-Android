package nl.sebmaakt.app.wrts_ds;

import WrtsMobile.com.R;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;

import com.flurry.android.FlurryAgent;
import com.michaelnovakjr.numberpicker.NumberPickerDialog;

public class TestSettingsActivity extends Activity {
	
	SharedPreferences settings;
	CheckBox uppercase;
    CheckBox accents;
    CheckBox punctuation;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FlurryAgent.onStartSession(this, "HELHFVX5C1HA5RQR48JJ");
        setContentView(R.layout.testsettings);
        settings = getSharedPreferences("wrtsapp", 0);
        RadioButton testgood = (RadioButton) findViewById(R.id.radioGood);
        RadioButton testfast = (RadioButton) findViewById(R.id.radioFast);
        uppercase = (CheckBox) findViewById(R.id.uppercase);
        accents = (CheckBox) findViewById(R.id.accents);
        punctuation = (CheckBox) findViewById(R.id.punctuation);
        RadioButton scoreNL = (RadioButton) findViewById(R.id.scoreNL);
        RadioButton scoreproc = (RadioButton) findViewById(R.id.scoreproc);
        Button showtime = (Button) findViewById(R.id.showtime);
        Button textsizeB = (Button) findViewById(R.id.textsizeB);
        showtime.setText(settings.getInt("showanswer", 3)+" sec.");
        textsizeB.setText(Integer.toString(settings.getInt("textsize", 1)));
        if(settings.getInt("practicetype", 0) == 0) {
        	testgood.setChecked(true);
        } else {
        	testfast.setChecked(true);
        }
        if(settings.getInt("resultstype", 0) == 0) {
        	scoreNL.setChecked(true);
        } else {
        	scoreproc.setChecked(true);
        }
        uppercase.setChecked(settings.getBoolean("uppercase", false));
        accents.setChecked(settings.getBoolean("accents", false));
        punctuation.setChecked(settings.getBoolean("punctuation", false));
        testgood.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	SharedPreferences.Editor editor = settings.edit();
               	editor.putInt("practicetype", 0);
               	editor.commit();
            }
        });       
        testfast.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	SharedPreferences.Editor editor = settings.edit();
               	editor.putInt("practicetype", 1);
               	editor.commit();
            }
        });
        showtime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	NumberPickerDialog dialog = new NumberPickerDialog(TestSettingsActivity.this, 3, settings.getInt("showanswer", 3), 1, 10);
                dialog.setTitle(getResources().getString(R.string.secondsamount));
                dialog.setOnNumberSetListener(new NumberPickerDialog.OnNumberSetListener() {
					public void onNumberSet(int selectedNumber) {
						SharedPreferences.Editor editor = settings.edit();
		               	editor.putInt("showanswer", selectedNumber);
		               	editor.commit();
		               	Button showtime = (Button) findViewById(R.id.showtime);
		               	showtime.setText(settings.getInt("showanswer", 3)+" sec.");
					}                	
                });
                dialog.show();
            }
        });
        textsizeB.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	NumberPickerDialog dialog = new NumberPickerDialog(TestSettingsActivity.this, 3, settings.getInt("textsize", 1), 1, 10);
                dialog.setTitle(getResources().getString(R.string.textsize));
                dialog.setOnNumberSetListener(new NumberPickerDialog.OnNumberSetListener() {
					public void onNumberSet(int selectedNumber) {
						SharedPreferences.Editor editor = settings.edit();
		               	editor.putInt("textsize", selectedNumber);
		               	editor.commit();
		               	Button textsizeB = (Button) findViewById(R.id.textsizeB);
		               	textsizeB.setText(Integer.toString(settings.getInt("textsize", 1)));
					}                	
                });
                dialog.show();
            }
        });
        scoreNL.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	SharedPreferences.Editor editor = settings.edit();
               	editor.putInt("resultstype", 0);
               	editor.commit();
            }
        });
        scoreproc.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	SharedPreferences.Editor editor = settings.edit();
               	editor.putInt("resultstype", 1);
               	editor.commit();
            }
        });
        uppercase.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	SharedPreferences.Editor editor = settings.edit();
               	editor.putBoolean("uppercase", uppercase.isChecked());
               	editor.commit();
            }
        });
        accents.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	SharedPreferences.Editor editor = settings.edit();            	
               	editor.putBoolean("accents", accents.isChecked());
               	editor.commit();
            }
        });
        punctuation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	SharedPreferences.Editor editor = settings.edit();
               	editor.putBoolean("punctuation", punctuation.isChecked());
               	editor.commit();
            }
        });
    }
    
    public void onStop() {
		super.onStop();
		FlurryAgent.onEndSession(this);
	}
}