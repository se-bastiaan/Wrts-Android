package nl.sebmaakt.app.wrts_ds;

import java.util.ArrayList;
import java.util.Collections;

import nl.sebmaakt.app.wrts_ds.database.DbAdapter;
import WrtsMobile.com.R;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;

public class InYourMindActivity extends Activity {
	
	public SharedPreferences settings;
	public Cursor cursor, wordscursor;
	public Cursor answers;
	public DbAdapter dbHelper;
	public Bundle extras;
	public String language;
	public String language1;
	public String language2;
	public TextView Word1;
	public TextView Word2;
	public TextView Word3;
	public TextView Word4;
	public TextView Word5;
	public TextView Word6;
	public TextView Word7;
	public TextView Word8;
	public TextView Word9;
	public TextView Word10;
	public TextView Correct;
	public TextView Incorrect;
	public TextView WordsToGo;
	public ArrayList<String> words;
	public int selected;
	public boolean multiplelangs;
	public int correct;
	public int incorrect;
	public int total;
	public long time;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        FlurryAgent.onStartSession(this, "HELHFVX5C1HA5RQR48JJ");
        FlurryAgent.logEvent("InYourMind Test");
        setContentView(R.layout.inyourmind);
        TextView Language1 = (TextView) findViewById(R.id.lang1);
        TextView Language2 = (TextView) findViewById(R.id.lang2);    
        correct = 0;
        incorrect = 0;
        selected = 0;
        words = null;
        extras = getIntent().getExtras();
        settings = getSharedPreferences("wrtsapp", 0);
        dbHelper = new DbAdapter(this);
        dbHelper.open();
        language1 = extras.getString("language");
        Language1.setText(extras.getString("language").toUpperCase());
        cursor = dbHelper.fetchListData(extras.getString("id"));
        if(cursor.getString(cursor.getColumnIndex("langa")).equals(extras.getString("language"))) {
        	language = "worda";
        } else if (cursor.getString(cursor.getColumnIndex("langb")).equals(extras.getString("language"))) {
       	 	language = "wordb";
        } else if (cursor.getString(cursor.getColumnIndex("langc")).equals(extras.getString("language"))) {
        	language = "wordc";
        } else if (cursor.getString(cursor.getColumnIndex("langd")).equals(extras.getString("language"))) {
        	language = "wordd";
        } else if (cursor.getString(cursor.getColumnIndex("lange")).equals(extras.getString("language"))) {
        	language = "worde";
        } else if (cursor.getString(cursor.getColumnIndex("langf")).equals(extras.getString("language"))) {
        	language = "wordf";
        } else if (cursor.getString(cursor.getColumnIndex("langg")).equals(extras.getString("language"))) {
        	language = "wordg";
        } else if (cursor.getString(cursor.getColumnIndex("langh")).equals(extras.getString("language"))) {
        	language = "wordh";
        } else if (cursor.getString(cursor.getColumnIndex("langi")).equals(extras.getString("language"))) {
        	language = "wordi";
        } else if (cursor.getString(cursor.getColumnIndex("langj")).equals(extras.getString("language"))) {
        	language = "wordj";
        }
        
        if(cursor.isNull(cursor.getColumnIndex("langc")) && cursor.isNull(cursor.getColumnIndex("langd")) && cursor.isNull(cursor.getColumnIndex("lange")) && cursor.isNull(cursor.getColumnIndex("langf")) && cursor.isNull(cursor.getColumnIndex("langg")) && cursor.isNull(cursor.getColumnIndex("langh")) && cursor.isNull(cursor.getColumnIndex("langi")) && cursor.isNull(cursor.getColumnIndex("langj"))) {
        	if(language.equals("wordb")) {
        		Language2.setText(cursor.getString(cursor.getColumnIndex("langa")).toUpperCase());
        		language2 = cursor.getString(cursor.getColumnIndex("langa"));
        	} else if (language.equals("worda")) {
        		Language2.setText(cursor.getString(cursor.getColumnIndex("langb")).toUpperCase());
        		language2 = cursor.getString(cursor.getColumnIndex("langb"));
        	}    
        } else {
        	multiplelangs = true;
        	language2 = "Andere talen";
        	Language2.setText(getResources().getString(R.string.otherlangs));
        }
        
        Button CheckButton = (Button) findViewById(R.id.checkanswer);
        Button CorrectButton = (Button) findViewById(R.id.correct);
        Button IncorrectButton = (Button) findViewById(R.id.incorrect);
        CorrectButton.getBackground().setColorFilter(0xFF00D51E, PorterDuff.Mode.SRC_ATOP);
        IncorrectButton.getBackground().setColorFilter(0xFFD70000, PorterDuff.Mode.SRC_ATOP);
        wordscursor = dbHelper.fetchList(extras.getString("id"));
        words = new ArrayList<String>();
        for(int i = 0; i < wordscursor.getCount(); i++) {
        	words.add(wordscursor.getString(wordscursor.getColumnIndex(language)));
        	if(!wordscursor.isLast()) {
        		wordscursor.moveToNext();
        	}
        }
        Collections.shuffle(words);
        Log.i(getPackageName(), words.toString());
        Word1 = (TextView) findViewById(R.id.word1);
        Word2 = (TextView) findViewById(R.id.word2);
        Word3 = (TextView) findViewById(R.id.word3);
        Word4 = (TextView) findViewById(R.id.word4);
        Word5 = (TextView) findViewById(R.id.word5);
        Word6 = (TextView) findViewById(R.id.word6);
        Word7 = (TextView) findViewById(R.id.word7);
        Word8 = (TextView) findViewById(R.id.word8);
        Word9 = (TextView) findViewById(R.id.word9);
        Word10 = (TextView) findViewById(R.id.word10);
        int textsize = settings.getInt("textsize", 1);
        float sp = (float) 13.33 + textsize;
        Word1.setTextSize(TypedValue.COMPLEX_UNIT_DIP, sp);
        Word2.setTextSize(TypedValue.COMPLEX_UNIT_DIP, sp);
        Word3.setTextSize(TypedValue.COMPLEX_UNIT_DIP, sp);
        Word4.setTextSize(TypedValue.COMPLEX_UNIT_DIP, sp);
        Word5.setTextSize(TypedValue.COMPLEX_UNIT_DIP, sp);
        Word6.setTextSize(TypedValue.COMPLEX_UNIT_DIP, sp);
        Word7.setTextSize(TypedValue.COMPLEX_UNIT_DIP, sp);
        Word8.setTextSize(TypedValue.COMPLEX_UNIT_DIP, sp);
        Word9.setTextSize(TypedValue.COMPLEX_UNIT_DIP, sp);
        Word10.setTextSize(TypedValue.COMPLEX_UNIT_DIP, sp);
        
        Correct = (TextView) findViewById(R.id.countcorrect);
        Incorrect = (TextView) findViewById(R.id.countincorrect);
        WordsToGo = (TextView) findViewById(R.id.wordstogo);
        time = System.currentTimeMillis();
        if(!cursor.isNull(cursor.getColumnIndex("wordc"))) {
    		Word3.setVisibility(TextView.VISIBLE);
    	}
    	if(!cursor.isNull(cursor.getColumnIndex("wordd"))) {
    		Word4.setVisibility(TextView.VISIBLE);
    	}
    	if(!cursor.isNull(cursor.getColumnIndex("worde"))) {
    		Word5.setVisibility(TextView.VISIBLE);
    	}
    	if(!cursor.isNull(cursor.getColumnIndex("wordf"))) {
    		Word6.setVisibility(TextView.VISIBLE);
    	}
    	if(!cursor.isNull(cursor.getColumnIndex("wordg"))) {
    		Word7.setVisibility(TextView.VISIBLE);
    	}
    	if(!cursor.isNull(cursor.getColumnIndex("wordh"))) {
    		Word8.setVisibility(TextView.VISIBLE);
    	}
    	if(!cursor.isNull(cursor.getColumnIndex("wordi"))) {
    		Word9.setVisibility(TextView.VISIBLE);
    	}
    	if(!cursor.isNull(cursor.getColumnIndex("wordj"))) {
    		Word10.setVisibility(TextView.VISIBLE);
    	}

        Word1.setText(words.get(selected));
        Correct.setText("0");
        Incorrect.setText("0");
        total = words.size() - 1;
        WordsToGo.setText((getResources().getString(R.string.nog) + " " + total + " " + getResources().getString(R.string.wordsafter)).trim());
        CheckButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	//Toast.makeText(v.getContext(), radiobutton, Toast.LENGTH_SHORT).show();
            	String word = Word1.getText().toString();
            	dbHelper.open();
            	answers = dbHelper.fetchAnswers(extras.getString("id"), language, word);
            	if (answers.getString(answers.getColumnIndex("worda")).equals(word)) {
                	Word2.setVisibility(TextView.VISIBLE);
                	Word2.setText(answers.getString(answers.getColumnIndex("wordb")));
                	if(!answers.isNull(answers.getColumnIndex("wordc"))) {
                		Word3.setVisibility(TextView.VISIBLE);
                		Word3.setText(answers.getString(answers.getColumnIndex("wordc")));
                	}
                	if(!answers.isNull(answers.getColumnIndex("wordd"))) {
                		Word4.setVisibility(TextView.VISIBLE);
                		Word4.setText(answers.getString(answers.getColumnIndex("wordd")));
                	}
                	if(!answers.isNull(answers.getColumnIndex("worde"))) {
                		Word5.setVisibility(TextView.VISIBLE);
                		Word5.setText(answers.getString(answers.getColumnIndex("worde")));
                	}
                	if(!answers.isNull(answers.getColumnIndex("wordf"))) {
                		Word6.setVisibility(TextView.VISIBLE);
                		Word6.setText(answers.getString(answers.getColumnIndex("wordf")));
                	}
                	if(!answers.isNull(answers.getColumnIndex("wordg"))) {
                		Word7.setVisibility(TextView.VISIBLE);
                		Word7.setText(answers.getString(answers.getColumnIndex("wordg")));
                	}
                	if(!answers.isNull(answers.getColumnIndex("wordh"))) {
                		Word8.setVisibility(TextView.VISIBLE);
                		Word8.setText(answers.getString(answers.getColumnIndex("wordh")));
                	}
                	if(!answers.isNull(answers.getColumnIndex("wordi"))) {
                		Word9.setVisibility(TextView.VISIBLE);
                		Word9.setText(answers.getString(answers.getColumnIndex("wordi")));
                	}
                	if(!answers.isNull(answers.getColumnIndex("wordj"))) {
                		Word10.setVisibility(TextView.VISIBLE);
                		Word10.setText(answers.getString(answers.getColumnIndex("wordj")));
                	}
            	} else if (answers.getString(answers.getColumnIndex("wordb")).equals(word)) {
            		Word2.setVisibility(TextView.VISIBLE);
                	Word2.setText(answers.getString(answers.getColumnIndex("worda")));
                	if(!answers.isNull(answers.getColumnIndex("wordc"))) {
                		Word3.setVisibility(TextView.VISIBLE);
                		Word3.setText(answers.getString(answers.getColumnIndex("wordc")));
                	}
                	if(!answers.isNull(answers.getColumnIndex("wordd"))) {
                		Word4.setVisibility(TextView.VISIBLE);
                		Word4.setText(answers.getString(answers.getColumnIndex("wordd")));
                	}
                	if(!answers.isNull(answers.getColumnIndex("worde"))) {
                		Word5.setVisibility(TextView.VISIBLE);
                		Word5.setText(answers.getString(answers.getColumnIndex("worde")));
                	}
                	if(!answers.isNull(answers.getColumnIndex("wordf"))) {
                		Word6.setVisibility(TextView.VISIBLE);
                		Word6.setText(answers.getString(answers.getColumnIndex("wordf")));
                	}
                	if(!answers.isNull(answers.getColumnIndex("wordg"))) {
                		Word7.setVisibility(TextView.VISIBLE);
                		Word7.setText(answers.getString(answers.getColumnIndex("wordg")));
                	}
                	if(!answers.isNull(answers.getColumnIndex("wordh"))) {
                		Word8.setVisibility(TextView.VISIBLE);
                		Word8.setText(answers.getString(answers.getColumnIndex("wordh")));
                	}
                	if(!answers.isNull(answers.getColumnIndex("wordi"))) {
                		Word9.setVisibility(TextView.VISIBLE);
                		Word9.setText(answers.getString(answers.getColumnIndex("wordi")));
                	}
                	if(!answers.isNull(answers.getColumnIndex("wordj"))) {
                		Word10.setVisibility(TextView.VISIBLE);
                		Word10.setText(answers.getString(answers.getColumnIndex("wordj")));
                	}
            	} else if (answers.getString(answers.getColumnIndex("wordc")).equals(word)) {
            		Word2.setVisibility(TextView.VISIBLE);
                	Word2.setText(answers.getString(answers.getColumnIndex("worda")));
                	Word3.setVisibility(TextView.VISIBLE);
                	Word3.setText(answers.getString(answers.getColumnIndex("wordb")));
                	if(!answers.isNull(answers.getColumnIndex("wordd"))) {
                		Word4.setVisibility(TextView.VISIBLE);
                		Word4.setText(answers.getString(answers.getColumnIndex("wordd")));
                	}
                	if(!answers.isNull(answers.getColumnIndex("worde"))) {
                		Word5.setVisibility(TextView.VISIBLE);
                		Word5.setText(answers.getString(answers.getColumnIndex("worde")));
                	}
                	if(!answers.isNull(answers.getColumnIndex("wordf"))) {
                		Word6.setVisibility(TextView.VISIBLE);
                		Word6.setText(answers.getString(answers.getColumnIndex("wordf")));
                	}
                	if(!answers.isNull(answers.getColumnIndex("wordg"))) {
                		Word7.setVisibility(TextView.VISIBLE);
                		Word7.setText(answers.getString(answers.getColumnIndex("wordg")));
                	}
                	if(!answers.isNull(answers.getColumnIndex("wordh"))) {
                		Word8.setVisibility(TextView.VISIBLE);
                		Word8.setText(answers.getString(answers.getColumnIndex("wordh")));
                	}
                	if(!answers.isNull(answers.getColumnIndex("wordi"))) {
                		Word9.setVisibility(TextView.VISIBLE);
                		Word9.setText(answers.getString(answers.getColumnIndex("wordi")));
                	}
                	if(!answers.isNull(answers.getColumnIndex("wordj"))) {
                		Word10.setVisibility(TextView.VISIBLE);
                		Word10.setText(answers.getString(answers.getColumnIndex("wordj")));
                	}
            	} else if (answers.getString(answers.getColumnIndex("wordd")).equals(word)) {
            		Word2.setVisibility(TextView.VISIBLE);
                	Word2.setText(answers.getString(answers.getColumnIndex("worda")));
                	Word3.setVisibility(TextView.VISIBLE);
                	Word3.setText(answers.getString(answers.getColumnIndex("wordb")));
                	if(!answers.isNull(answers.getColumnIndex("wordc"))) {
                		Word4.setVisibility(TextView.VISIBLE);
                		Word4.setText(answers.getString(answers.getColumnIndex("wordc")));
                	}
                	if(!answers.isNull(answers.getColumnIndex("worde"))) {
                		Word5.setVisibility(TextView.VISIBLE);
                		Word5.setText(answers.getString(answers.getColumnIndex("worde")));
                	}
                	if(!answers.isNull(answers.getColumnIndex("wordf"))) {
                		Word6.setVisibility(TextView.VISIBLE);
                		Word6.setText(answers.getString(answers.getColumnIndex("wordf")));
                	}
                	if(!answers.isNull(answers.getColumnIndex("wordg"))) {
                		Word7.setVisibility(TextView.VISIBLE);
                		Word7.setText(answers.getString(answers.getColumnIndex("wordg")));
                	}
                	if(!answers.isNull(answers.getColumnIndex("wordh"))) {
                		Word8.setVisibility(TextView.VISIBLE);
                		Word8.setText(answers.getString(answers.getColumnIndex("wordh")));
                	}
                	if(!answers.isNull(answers.getColumnIndex("wordi"))) {
                		Word9.setVisibility(TextView.VISIBLE);
                		Word9.setText(answers.getString(answers.getColumnIndex("wordi")));
                	}
                	if(!answers.isNull(answers.getColumnIndex("wordj"))) {
                		Word10.setVisibility(TextView.VISIBLE);
                		Word10.setText(answers.getString(answers.getColumnIndex("wordj")));
                	}
            	} else if (answers.getString(answers.getColumnIndex("worde")).equals(word)) {
            		Word2.setVisibility(TextView.VISIBLE);
                	Word2.setText(answers.getString(answers.getColumnIndex("worda")));
                	if(!answers.isNull(answers.getColumnIndex("wordb"))) {
                		Word3.setVisibility(TextView.VISIBLE);
                		Word3.setText(answers.getString(answers.getColumnIndex("wordb")));
                	}
                	if(!answers.isNull(answers.getColumnIndex("wordc"))) {
                		Word4.setVisibility(TextView.VISIBLE);
                		Word4.setText(answers.getString(answers.getColumnIndex("wordc")));
                	}
                	if(!answers.isNull(answers.getColumnIndex("wordd"))) {
                		Word5.setVisibility(TextView.VISIBLE);
                		Word5.setText(answers.getString(answers.getColumnIndex("wordd")));
                	}
                	if(!answers.isNull(answers.getColumnIndex("wordf"))) {
                		Word6.setVisibility(TextView.VISIBLE);
                		Word6.setText(answers.getString(answers.getColumnIndex("wordf")));
                	}
                	if(!answers.isNull(answers.getColumnIndex("wordg"))) {
                		Word7.setVisibility(TextView.VISIBLE);
                		Word7.setText(answers.getString(answers.getColumnIndex("wordg")));
                	}
                	if(!answers.isNull(answers.getColumnIndex("wordh"))) {
                		Word8.setVisibility(TextView.VISIBLE);
                		Word8.setText(answers.getString(answers.getColumnIndex("wordh")));
                	}
                	if(!answers.isNull(answers.getColumnIndex("wordi"))) {
                		Word9.setVisibility(TextView.VISIBLE);
                		Word9.setText(answers.getString(answers.getColumnIndex("wordi")));
                	}
                	if(!answers.isNull(answers.getColumnIndex("wordj"))) {
                		Word10.setVisibility(TextView.VISIBLE);
                		Word10.setText(answers.getString(answers.getColumnIndex("wordj")));
                	}
            	} else if (answers.getString(answers.getColumnIndex("wordf")).equals(word)) {
            		Word2.setVisibility(TextView.VISIBLE);
                	Word2.setText(answers.getString(answers.getColumnIndex("worda")));
                	if(!answers.isNull(answers.getColumnIndex("wordb"))) {
                		Word3.setVisibility(TextView.VISIBLE);
                		Word3.setText(answers.getString(answers.getColumnIndex("wordb")));
                	}
                	if(!answers.isNull(answers.getColumnIndex("wordc"))) {
                		Word4.setVisibility(TextView.VISIBLE);
                		Word4.setText(answers.getString(answers.getColumnIndex("wordc")));
                	}
                	if(!answers.isNull(answers.getColumnIndex("wordd"))) {
                		Word5.setVisibility(TextView.VISIBLE);
                		Word5.setText(answers.getString(answers.getColumnIndex("wordd")));
                	}
                	if(!answers.isNull(answers.getColumnIndex("worde"))) {
                		Word6.setVisibility(TextView.VISIBLE);
                		Word6.setText(answers.getString(answers.getColumnIndex("worde")));
                	}
                	if(!answers.isNull(answers.getColumnIndex("wordg"))) {
                		Word7.setVisibility(TextView.VISIBLE);
                		Word7.setText(answers.getString(answers.getColumnIndex("wordg")));
                	}
                	if(!answers.isNull(answers.getColumnIndex("wordh"))) {
                		Word8.setVisibility(TextView.VISIBLE);
                		Word8.setText(answers.getString(answers.getColumnIndex("wordh")));
                	}
                	if(!answers.isNull(answers.getColumnIndex("wordi"))) {
                		Word9.setVisibility(TextView.VISIBLE);
                		Word9.setText(answers.getString(answers.getColumnIndex("wordi")));
                	}
                	if(!answers.isNull(answers.getColumnIndex("wordj"))) {
                		Word10.setVisibility(TextView.VISIBLE);
                		Word10.setText(answers.getString(answers.getColumnIndex("wordj")));
                	}
            	} else if (answers.getString(answers.getColumnIndex("wordg")).equals(word)) {
            		Word2.setVisibility(TextView.VISIBLE);
                	Word2.setText(answers.getString(answers.getColumnIndex("worda")));
                	if(!answers.isNull(answers.getColumnIndex("wordb"))) {
                		Word3.setVisibility(TextView.VISIBLE);
                		Word3.setText(answers.getString(answers.getColumnIndex("wordb")));
                	}
                	if(!answers.isNull(answers.getColumnIndex("wordc"))) {
                		Word4.setVisibility(TextView.VISIBLE);
                		Word4.setText(answers.getString(answers.getColumnIndex("wordc")));
                	}
                	if(!answers.isNull(answers.getColumnIndex("wordd"))) {
                		Word5.setVisibility(TextView.VISIBLE);
                		Word5.setText(answers.getString(answers.getColumnIndex("wordd")));
                	}
                	if(!answers.isNull(answers.getColumnIndex("worde"))) {
                		Word6.setVisibility(TextView.VISIBLE);
                		Word6.setText(answers.getString(answers.getColumnIndex("worde")));
                	}
                	if(!answers.isNull(answers.getColumnIndex("wordf"))) {
                		Word7.setVisibility(TextView.VISIBLE);
                		Word7.setText(answers.getString(answers.getColumnIndex("wordf")));
                	}
                	if(!answers.isNull(answers.getColumnIndex("wordh"))) {
                		Word8.setVisibility(TextView.VISIBLE);
                		Word8.setText(answers.getString(answers.getColumnIndex("wordh")));
                	}
                	if(!answers.isNull(answers.getColumnIndex("wordi"))) {
                		Word9.setVisibility(TextView.VISIBLE);
                		Word9.setText(answers.getString(answers.getColumnIndex("wordi")));
                	}
                	if(!answers.isNull(answers.getColumnIndex("wordj"))) {
                		Word10.setVisibility(TextView.VISIBLE);
                		Word10.setText(answers.getString(answers.getColumnIndex("wordj")));
                	}
            	} else if (answers.getString(answers.getColumnIndex("wordh")).equals(word)) {
            		Word2.setVisibility(TextView.VISIBLE);
                	Word2.setText(answers.getString(answers.getColumnIndex("worda")));
                	if(!answers.isNull(answers.getColumnIndex("wordb"))) {
                		Word3.setVisibility(TextView.VISIBLE);
                		Word3.setText(answers.getString(answers.getColumnIndex("wordb")));
                	}
                	if(!answers.isNull(answers.getColumnIndex("wordc"))) {
                		Word4.setVisibility(TextView.VISIBLE);
                		Word4.setText(answers.getString(answers.getColumnIndex("wordc")));
                	}
                	if(!answers.isNull(answers.getColumnIndex("wordd"))) {
                		Word5.setVisibility(TextView.VISIBLE);
                		Word5.setText(answers.getString(answers.getColumnIndex("wordd")));
                	}
                	if(!answers.isNull(answers.getColumnIndex("worde"))) {
                		Word6.setVisibility(TextView.VISIBLE);
                		Word6.setText(answers.getString(answers.getColumnIndex("worde")));
                	}
                	if(!answers.isNull(answers.getColumnIndex("wordf"))) {
                		Word7.setVisibility(TextView.VISIBLE);
                		Word7.setText(answers.getString(answers.getColumnIndex("wordf")));
                	}
                	if(!answers.isNull(answers.getColumnIndex("wordg"))) {
                		Word8.setVisibility(TextView.VISIBLE);
                		Word8.setText(answers.getString(answers.getColumnIndex("wordg")));
                	}
                	if(!answers.isNull(answers.getColumnIndex("wordi"))) {
                		Word9.setVisibility(TextView.VISIBLE);
                		Word9.setText(answers.getString(answers.getColumnIndex("wordi")));
                	}
                	if(!answers.isNull(answers.getColumnIndex("wordj"))) {
                		Word10.setVisibility(TextView.VISIBLE);
                		Word10.setText(answers.getString(answers.getColumnIndex("wordj")));
                	}
            	} else if (answers.getString(answers.getColumnIndex("wordi")).equals(word)) {
            		Word2.setVisibility(TextView.VISIBLE);
                	Word2.setText(answers.getString(answers.getColumnIndex("worda")));
                	if(!answers.isNull(answers.getColumnIndex("wordb"))) {
                		Word3.setVisibility(TextView.VISIBLE);
                		Word3.setText(answers.getString(answers.getColumnIndex("wordb")));
                	}
                	if(!answers.isNull(answers.getColumnIndex("wordc"))) {
                		Word4.setVisibility(TextView.VISIBLE);
                		Word4.setText(answers.getString(answers.getColumnIndex("wordc")));
                	}
                	if(!answers.isNull(answers.getColumnIndex("wordd"))) {
                		Word5.setVisibility(TextView.VISIBLE);
                		Word5.setText(answers.getString(answers.getColumnIndex("wordd")));
                	}
                	if(!answers.isNull(answers.getColumnIndex("worde"))) {
                		Word6.setVisibility(TextView.VISIBLE);
                		Word6.setText(answers.getString(answers.getColumnIndex("worde")));
                	}
                	if(!answers.isNull(answers.getColumnIndex("wordf"))) {
                		Word7.setVisibility(TextView.VISIBLE);
                		Word7.setText(answers.getString(answers.getColumnIndex("wordf")));
                	}
                	if(!answers.isNull(answers.getColumnIndex("wordg"))) {
                		Word8.setVisibility(TextView.VISIBLE);
                		Word8.setText(answers.getString(answers.getColumnIndex("wordg")));
                	}
                	if(!answers.isNull(answers.getColumnIndex("wordh"))) {
                		Word9.setVisibility(TextView.VISIBLE);
                		Word9.setText(answers.getString(answers.getColumnIndex("wordh")));
                	}
                	if(!answers.isNull(answers.getColumnIndex("wordj"))) {
                		Word10.setVisibility(TextView.VISIBLE);
                		Word10.setText(answers.getString(answers.getColumnIndex("wordj")));
                	}
            	} else if (answers.getString(answers.getColumnIndex("wordj")).equals(word)) {
            		Word2.setVisibility(TextView.VISIBLE);
                	Word2.setText(answers.getString(answers.getColumnIndex("worda")));
                	if(!answers.isNull(answers.getColumnIndex("wordb"))) {
                		Word3.setVisibility(TextView.VISIBLE);
                		Word3.setText(answers.getString(answers.getColumnIndex("wordb")));
                	}
                	if(!answers.isNull(answers.getColumnIndex("wordc"))) {
                		Word4.setVisibility(TextView.VISIBLE);
                		Word4.setText(answers.getString(answers.getColumnIndex("wordc")));
                	}
                	if(!answers.isNull(answers.getColumnIndex("wordd"))) {
                		Word5.setVisibility(TextView.VISIBLE);
                		Word5.setText(answers.getString(answers.getColumnIndex("wordd")));
                	}
                	if(!answers.isNull(answers.getColumnIndex("worde"))) {
                		Word6.setVisibility(TextView.VISIBLE);
                		Word6.setText(answers.getString(answers.getColumnIndex("worde")));
                	}
                	if(!answers.isNull(answers.getColumnIndex("wordf"))) {
                		Word7.setVisibility(TextView.VISIBLE);
                		Word7.setText(answers.getString(answers.getColumnIndex("wordf")));
                	}
                	if(!answers.isNull(answers.getColumnIndex("wordg"))) {
                		Word8.setVisibility(TextView.VISIBLE);
                		Word8.setText(answers.getString(answers.getColumnIndex("wordg")));
                	}
                	if(!answers.isNull(answers.getColumnIndex("wordh"))) {
                		Word9.setVisibility(TextView.VISIBLE);
                		Word9.setText(answers.getString(answers.getColumnIndex("wordh")));
                	}
                	if(!answers.isNull(answers.getColumnIndex("wordi"))) {
                		Word10.setVisibility(TextView.VISIBLE);
                		Word10.setText(answers.getString(answers.getColumnIndex("wordi")));
                	}
            	}            	            	
            	dbHelper.close();
            	/*if(language.equals("wordb")) {
            		Word2.setText();
            	} else if (language.equals("worda")) {
            		Word2.setText(words.get(selected + 1));
            	}*/           	
            }
    	});
        CorrectButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	//Toast.makeText(v.getContext(), radiobutton, Toast.LENGTH_SHORT).show();
            	Word2.setText(null);
            	Word3.setText(null);
            	Word4.setText(null);
            	Word5.setText(null);
            	Word6.setText(null);
            	Word7.setText(null);
            	Word8.setText(null);
            	Word9.setText(null);
            	Word10.setText(null);
            	selected = selected + 1;
            	correct = correct + 1;
            	Correct.setText(Integer.toString(correct));
            	if(total != 0) {
            		total = total - 1;      
            	}
                WordsToGo.setText((getResources().getString(R.string.nog) + " " + total + " " + getResources().getString(R.string.wordsafter)).trim());
            	if(words.size() > selected) {
            		Word1.setText(words.get(selected));
            	} else {
            		v.setVisibility(Button.GONE);
            		Button IncorrectButton = (Button) findViewById(R.id.incorrect);
            		IncorrectButton.setVisibility(Button.GONE);
            		Button CheckButton = (Button) findViewById(R.id.checkanswer);
            		CheckButton.setVisibility(Button.GONE);
            		Word1.setText("Klaar!");
            		Word2.setText("Klaar!");
            		Intent intent = new Intent(v.getContext(), TestResultsActivity.class);
                	Bundle b = new Bundle();
    				b.putString("id", extras.getString("id"));
    				b.putString("method", getResources().getString(R.string.inyourmind));
    				b.putString("langa", language1);
    				b.putString("langb", language2);
    				b.putLong("time", System.currentTimeMillis() - time);
    				b.putInt("correct", correct);
    				b.putInt("incorrect", incorrect);
    				b.putInt("total", words.size());
    				intent.putExtras(b);
                	startActivity(intent);
                	finish();
            	}            	
            	//Log.i(getPackageName(), words.toString());            	
            }
    	});
        IncorrectButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	//Toast.makeText(v.getContext(), radiobutton, Toast.LENGTH_SHORT).show();
            	Word2.setText(null);
            	Word3.setText(null);
            	Word4.setText(null);
            	Word5.setText(null);
            	Word6.setText(null);
            	Word7.setText(null);
            	Word8.setText(null);
            	Word9.setText(null);
            	Word10.setText(null);
            	selected = selected + 1;
            	incorrect = incorrect + 1;
            	Incorrect.setText(Integer.toString(incorrect));            	
            	total = total - 1;
            	if(settings.getInt("practicetype", 0) != 1) {
		            if(words.size() > selected) {
		            	words.add(selected + 1, words.get(selected - 1));
		            	total = total + 1;
		            	if((words.get(words.size() - 1) != words.get(selected - 1)) || (words.size() == selected)) {
		            		words.add(words.get(selected - 1));
		            		total = total + 1;
		            	}            		
		            } else {
		            	total = total + 1;
		            	words.add(words.get(selected - 1));
		            }
            	}
            	Word1.setText(words.get(selected));
            	WordsToGo.setText((getResources().getString(R.string.nog) + " " + total + " " + getResources().getString(R.string.wordsafter)).trim());
            	//Log.i(getPackageName(), words.toString());
            }
    	});
        cursor.close();
        dbHelper.close();
	}
	
	public void onStop() {
		super.onStop();
		FlurryAgent.onEndSession(this);
	}
}
