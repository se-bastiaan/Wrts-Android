package nl.sebmaakt.app.wrts_ds;

import java.util.ArrayList;
import java.util.Collections;

import nl.sebmaakt.app.wrts_ds.database.DbAdapter;
import WrtsMobile.com.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;

@SuppressLint("ShowToast")
public class TranslateActivity extends Activity {
	
	public SharedPreferences settings;
	public Cursor cursor, wordscursor;
	public Cursor answers;
	public DbAdapter dbHelper;
	public Bundle extras;
	public String language;
	public String language1;
	public String language2;
	public TextView Word1;
	public EditText Word2;
	public EditText Word3;
	public EditText Word4;
	public EditText Word5;
	public EditText Word6;
	public EditText Word7;
	public EditText Word8;
	public EditText Word9;
	public EditText Word10;
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
	public Boolean correctword;
	public Button CheckButton;
	private Boolean partlyok = false;
	private Boolean wordok = false;
	private Boolean wordfalse = false;
	private String correctedword;
	private Boolean correcting = false;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        FlurryAgent.onStartSession(this, "HELHFVX5C1HA5RQR48JJ");
        FlurryAgent.logEvent("Translate Test");
        setContentView(R.layout.translate);
        TextView Language1 = (TextView) findViewById(R.id.lang1);
        TextView Language2 = (TextView) findViewById(R.id.lang2);    
        correct = 0;
        incorrect = 0;
        selected = 0;
        words = null;
        extras = getIntent().getExtras();
        settings = getSharedPreferences("wrtsapp", 0);        
        Log.d("TranslateActivity", extras.getString("id")+"");
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
        
        wordscursor = dbHelper.fetchList(extras.getString("id"));
        CheckButton = (Button) findViewById(R.id.checkanswer);
        words = new ArrayList<String>();
        for(int i = 0; i < wordscursor.getCount(); i++) {
        	words.add(wordscursor.getString(wordscursor.getColumnIndex(language)));
        	if(!wordscursor.isLast()) {
        		wordscursor.moveToNext();
        	}
        }
        Collections.shuffle(words);
        //Log.i(getPackageName(), words.toString());
        Word1 = (TextView) findViewById(R.id.word1);
        Word2 = (EditText) findViewById(R.id.word2);
        final Drawable TextEditBG = Word2.getBackground();
        final ColorStateList TextColor = Word2.getTextColors();
        final ColorStateList HintColor = Word2.getHintTextColors();
        Word3 = (EditText) findViewById(R.id.word3);
        Word4 = (EditText) findViewById(R.id.word4);
        Word5 = (EditText) findViewById(R.id.word5);
        Word6 = (EditText) findViewById(R.id.word6);
        Word7 = (EditText) findViewById(R.id.word7);
        Word8 = (EditText) findViewById(R.id.word8);
        Word9 = (EditText) findViewById(R.id.word9);
        Word10 = (EditText) findViewById(R.id.word10);
        Correct = (TextView) findViewById(R.id.countcorrect);
        Incorrect = (TextView) findViewById(R.id.countincorrect);
        WordsToGo = (TextView) findViewById(R.id.wordstogo);
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
        
        String[] langs = new String[]{"langb","langc","langd","lange","langf","langg","langh","langi","langj"};
    	if (language == "worda") {
    		langs = new String[]{"langb","langc","langd","lange","langf","langg","langh","langi","langj"};
    	} else if (language == "wordb") {
    		langs = new String[]{"langa","langc","langd","lange","langf","langg","langh","langi","langj"};
    	} else if (language == "wordc") {
    		langs = new String[]{"langa","langb","langd","lange","langf","langg","langh","langi","langj"};
    	} else if (language == "wordd") {
    		langs = new String[]{"langa","langb","langc","lange","langf","langg","langh","langi","langj"};
    	} else if (language == "worde") {
    		langs = new String[]{"langa","langn","langc","langd","langf","langg","langh","langi","langj"};
    	} else if (language == "wordf") {
    		langs = new String[]{"langa","langb","langc","langd","lange","langg","langh","langi","langj"};
    	} else if (language == "wordg") {
    		langs = new String[]{"langa","langb","langc","langd","lange","langf","langh","langi","langj"};
    	} else if (language == "wordh") {
    		langs = new String[]{"langa","langb","langc","langd","lange","langf","langg","langi","langj"};
    	} else if (language == "wordi") {
    		langs = new String[]{"langa","langb","langc","langd","lange","langf","langg","langh","langj"};
    	} else if (language == "wordj") {
    		langs = new String[]{"langa","langb","langc","langd","lange","langf","langg","langh","langi"};
    	}
        
        dbHelper.open();
        cursor = dbHelper.fetchListData(extras.getString("id"));
        if(multiplelangs) {
        	Word2.setHint(cursor.getString(cursor.getColumnIndex(langs[0])));
        	Word3.setHint(cursor.getString(cursor.getColumnIndex(langs[1])));
        	Word4.setHint(cursor.getString(cursor.getColumnIndex(langs[2])));
        	Word5.setHint(cursor.getString(cursor.getColumnIndex(langs[3])));
        	Word6.setHint(cursor.getString(cursor.getColumnIndex(langs[4])));
        	Word7.setHint(cursor.getString(cursor.getColumnIndex(langs[5])));
        	Word8.setHint(cursor.getString(cursor.getColumnIndex(langs[6])));
        	Word9.setHint(cursor.getString(cursor.getColumnIndex(langs[7])));
        	Word10.setHint(cursor.getString(cursor.getColumnIndex(langs[8])));
        }
        cursor.close();
       
        wordscursor = dbHelper.fetchList(extras.getString("id"));
        time = System.currentTimeMillis();
        if (wordscursor.isNull(wordscursor.getColumnIndex("wordc"))) {
        	Word2.setImeOptions(1);
        	Word2.setOnEditorActionListener(editoraction);        	
        } else if (wordscursor.isNull(wordscursor.getColumnIndex("wordd"))) {
        	Word3.setImeOptions(1);
        	Word3.setOnEditorActionListener(editoraction);
        } else if (wordscursor.isNull(wordscursor.getColumnIndex("worde"))) {
        	Word5.setImeOptions(1);
        	Word4.setOnEditorActionListener(editoraction);
        } else if (wordscursor.isNull(wordscursor.getColumnIndex("wordf"))) {
        	Word5.setImeOptions(1);
        	Word5.setOnEditorActionListener(editoraction);
        } else if (wordscursor.isNull(wordscursor.getColumnIndex("wordg"))) {
        	Word6.setImeOptions(1);
        	Word6.setOnEditorActionListener(editoraction);
        } else if (wordscursor.isNull(wordscursor.getColumnIndex("wordh"))) {
        	Word7.setImeOptions(1);
        	Word7.setOnEditorActionListener(editoraction);
        } else if (wordscursor.isNull(wordscursor.getColumnIndex("wordi"))) {
        	Word8.setImeOptions(1);
        	Word8.setOnEditorActionListener(editoraction);
        } else if (wordscursor.isNull(wordscursor.getColumnIndex("wordj"))) {
        	Word9.setImeOptions(1);
        	Word9.setOnEditorActionListener(editoraction);
        } else {
        	Word10.setImeOptions(1);
        	Word10.setOnEditorActionListener(editoraction);
        }
        if(!wordscursor.isNull(wordscursor.getColumnIndex("wordc"))) {
    		Word3.setVisibility(EditText.VISIBLE);
    	}
    	if(!wordscursor.isNull(wordscursor.getColumnIndex("wordd"))) {
    		Word4.setVisibility(EditText.VISIBLE);
    	}
    	if(!wordscursor.isNull(wordscursor.getColumnIndex("worde"))) {
    		Word5.setVisibility(EditText.VISIBLE);
    	}
    	if(!wordscursor.isNull(wordscursor.getColumnIndex("wordf"))) {
    		Word6.setVisibility(EditText.VISIBLE);
    	}
    	if(!wordscursor.isNull(wordscursor.getColumnIndex("wordg"))) {
    		Word7.setVisibility(EditText.VISIBLE);
    	}
    	if(!wordscursor.isNull(wordscursor.getColumnIndex("wordh"))) {
    		Word8.setVisibility(EditText.VISIBLE);
    	}
    	if(!wordscursor.isNull(wordscursor.getColumnIndex("wordi"))) {
    		Word9.setVisibility(EditText.VISIBLE);
    	}
    	if(!wordscursor.isNull(wordscursor.getColumnIndex("wordj"))) {
    		Word10.setVisibility(EditText.VISIBLE);
    	}
    	wordscursor.close();
    	dbHelper.close();
    	
        Word1.setText(words.get(selected));
        Correct.setText("0");
        Incorrect.setText("0");
        total = words.size() - 1;
        WordsToGo.setText((getResources().getString(R.string.nog) + " " + total + " " + getResources().getString(R.string.wordsafter)).trim());
        CheckButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	partlyok = false;
        		wordok = false;
        		wordfalse = false;
            	Log.d("Showtime", settings.getInt("showanswer", 3)+"");
            	//Log.i(getPackageName(), "focusable? : "+CheckButton.isFocusable() + " focusable in tm? : "+CheckButton.isFocusableInTouchMode()+ " focused? : " + CheckButton.isFocused());
            	//Toast.makeText(v.getContext(), radiobutton, Toast.LENGTH_SHORT).show();
            	
            	String word = Word1.getText().toString();
            	dbHelper.open();
            	answers = dbHelper.fetchAnswers(extras.getString("id"), language, word);
            	correctword = true;
            	Boolean correctfirst = false;
            	String[] langs = new String[]{"wordb","wordc","wordd","worde","wordf","wordg","wordh","wordi","wordj"};
            	if (answers.getString(answers.getColumnIndex("worda")).equals(word)) {
            		langs = new String[]{"wordb","wordc","wordd","worde","wordf","wordg","wordh","wordi","wordj"};
            	} else if (answers.getString(answers.getColumnIndex("wordb")).equals(word)) {
            		langs = new String[]{"worda","wordc","wordd","worde","wordf","wordg","wordh","wordi","wordj"};
            	} else if (answers.getString(answers.getColumnIndex("wordc")).equals(word)) {
            		langs = new String[]{"worda","wordb","wordd","worde","wordf","wordg","wordh","wordi","wordj"};
            	} else if (answers.getString(answers.getColumnIndex("wordd")).equals(word)) {
            		langs = new String[]{"worda","wordb","wordc","worde","wordf","wordg","wordh","wordi","wordj"};
            	} else if (answers.getString(answers.getColumnIndex("worde")).equals(word)) {
            		langs = new String[]{"worda","wordn","wordc","wordd","wordf","wordg","wordh","wordi","wordj"};
            	} else if (answers.getString(answers.getColumnIndex("wordf")).equals(word)) {
            		langs = new String[]{"worda","wordb","wordc","wordd","worde","wordg","wordh","wordi","wordj"};
            	} else if (answers.getString(answers.getColumnIndex("wordg")).equals(word)) {
            		langs = new String[]{"worda","wordb","wordc","wordd","worde","wordf","wordh","wordi","wordj"};
            	} else if (answers.getString(answers.getColumnIndex("wordh")).equals(word)) {
            		langs = new String[]{"worda","wordb","wordc","wordd","worde","wordf","wordg","wordi","wordj"};
            	} else if (answers.getString(answers.getColumnIndex("wordi")).equals(word)) {
            		langs = new String[]{"worda","wordb","wordc","wordd","worde","wordf","wordg","wordh","wordj"};
            	} else if (answers.getString(answers.getColumnIndex("wordj")).equals(word)) {
            		langs = new String[]{"worda","wordb","wordc","wordd","worde","wordf","wordg","wordh","wordi"};
            	}
            	
            	String ans = Word2.getText().toString();
            	String word2 = answers.getString(answers.getColumnIndex(langs[0]));
            	checkAnswer(ans, word2);
            	if(partlyok) {
            		Word2.setText(correctedword);
            		correctfirst = true;
            	} else if (wordok) {
            		Word2.setEnabled(false);
            		Word2.setBackgroundColor(getResources().getColor(R.color.green));
            		Word2.setTextColor(Color.BLACK);
            	} else if (wordfalse) {
            		Word2.setEnabled(false);
            		Word2.setBackgroundColor(getResources().getColor(R.color.red));
            		Word2.setHintTextColor(Color.BLACK);
            		Word2.setTextColor(Color.BLACK);
            	}
            	if(!answers.isNull(answers.getColumnIndex(langs[1]))) {
            		String ans3 = Word3.getText().toString();
                	String word3 = answers.getString(answers.getColumnIndex(langs[1]));            		
                	checkAnswer(ans3, word3);
                	if(partlyok) {
                		Word3.setText(correctedword);
                		correctfirst = true;
                	} else if (wordok) {
                		Word3.setEnabled(false);
                		Word3.setBackgroundColor(getResources().getColor(R.color.green));
                		Word3.setTextColor(Color.BLACK);
                	} else if (wordfalse) {
                		Word3.setEnabled(false);
                		Word3.setBackgroundColor(getResources().getColor(R.color.red));
                		Word3.setHintTextColor(Color.BLACK);
                		Word3.setTextColor(Color.BLACK);
                	}
                }
                if(!answers.isNull(answers.getColumnIndex(langs[2]))) {
                	String ans4 = Word4.getText().toString();
                	String word4 = answers.getString(answers.getColumnIndex(langs[2]));            		
                	checkAnswer(ans4, word4);
                	if(partlyok) {
                		Word4.setText(correctedword);
                		correctfirst = true;
                	} else if (wordok) {
                		Word4.setEnabled(false);
                		Word4.setBackgroundColor(getResources().getColor(R.color.green));
                		Word4.setTextColor(Color.BLACK);
                	} else if (wordfalse) {
                		Word4.setEnabled(false);
                		Word4.setBackgroundColor(getResources().getColor(R.color.red));
                		Word4.setHintTextColor(Color.BLACK);
                		Word4.setTextColor(Color.BLACK);
                	}
                }
                if(!answers.isNull(answers.getColumnIndex(langs[3]))) {
                	String ans5 = Word5.getText().toString();
                	String word5 = answers.getString(answers.getColumnIndex(langs[3]));            		
                	checkAnswer(ans5, word5);
                	if(partlyok) {
                		Word5.setText(correctedword);
                		correctfirst = true;
                	} else if (wordok) {
                		Word5.setEnabled(false);
                		Word5.setBackgroundColor(getResources().getColor(R.color.green));
                		Word5.setTextColor(Color.BLACK);
                	} else if (wordfalse) {
                		Word5.setEnabled(false);
                		Word5.setBackgroundColor(getResources().getColor(R.color.red));
                		Word5.setHintTextColor(Color.BLACK);
                		Word5.setTextColor(Color.BLACK);
                	}
                }
                if(!answers.isNull(answers.getColumnIndex(langs[4]))) {
                	String ans6 = Word6.getText().toString();
                	String word6 = answers.getString(answers.getColumnIndex(langs[4]));            		
                	checkAnswer(ans6, word6);
                	if(partlyok) {
                		Word6.setText(correctedword);
                		correctfirst = true;
                	} else if (wordok) {
                		Word6.setEnabled(false);
                		Word6.setBackgroundColor(getResources().getColor(R.color.green));
                		Word6.setTextColor(Color.BLACK);
                	} else if (wordfalse) {
                		Word6.setEnabled(false);
                		Word6.setBackgroundColor(getResources().getColor(R.color.red));
                		Word6.setHintTextColor(Color.BLACK);
                		Word6.setTextColor(Color.BLACK);
                	}
                }
                if(!answers.isNull(answers.getColumnIndex(langs[5]))) {
                	String ans7 = Word7.getText().toString();
                	String word7 = answers.getString(answers.getColumnIndex(langs[5]));            		
                	checkAnswer(ans7, word7);
                	if(partlyok) {
                		Word7.setText(correctedword);
                		correctfirst = true;
                	} else if (wordok) {
                		Word7.setEnabled(false);
                		Word7.setBackgroundColor(getResources().getColor(R.color.green));
                		Word7.setTextColor(Color.BLACK);
                	} else if (wordfalse) {
                		Word7.setEnabled(false);
                		Word7.setBackgroundColor(getResources().getColor(R.color.red));
                		Word7.setHintTextColor(Color.BLACK);
                		Word7.setTextColor(Color.BLACK);
                	}
                }
                if(!answers.isNull(answers.getColumnIndex(langs[6]))) {
                	String ans8 = Word8.getText().toString();
                	String word8 = answers.getString(answers.getColumnIndex(langs[6]));            		
                	checkAnswer(ans8, word8);
                	if(partlyok) {
                		Word8.setText(correctedword);
                		correctfirst = true;
                	} else if (wordok) {
                		Word8.setEnabled(false);
                		Word8.setBackgroundColor(getResources().getColor(R.color.green));
                		Word8.setTextColor(Color.BLACK);
                	} else if (wordfalse) {
                		Word8.setEnabled(false);
                		Word8.setBackgroundColor(getResources().getColor(R.color.red));
                		Word8.setHintTextColor(Color.BLACK);
                		Word8.setTextColor(Color.BLACK);
                	}
                }
                if(!answers.isNull(answers.getColumnIndex(langs[7]))) {
                	String ans9 = Word9.getText().toString();
                	String word9 = answers.getString(answers.getColumnIndex(langs[7]));            		
                	checkAnswer(ans9, word9);
                	if(partlyok) {
                		Word9.setText(correctedword);
                		correctfirst = true;
                	} else if (wordok) {
                		Word9.setEnabled(false);
                		Word9.setBackgroundColor(getResources().getColor(R.color.green));
                		Word9.setTextColor(Color.BLACK);
                	} else if (wordfalse) {
                		Word9.setEnabled(false);
                		Word9.setBackgroundColor(getResources().getColor(R.color.red));
                		Word9.setTextColor(Color.BLACK);
                		Word9.setHintTextColor(Color.BLACK);
                	}
                }
                if(!answers.isNull(answers.getColumnIndex(langs[8]))) {
                	String ans10 = Word10.getText().toString();
                	String word10 = answers.getString(answers.getColumnIndex(langs[8]));            		
                	checkAnswer(ans10, word10);
                	if(partlyok) {
                		Word10.setText(correctedword);
                		correctfirst = true;
                	} else if (wordok) {
                		Word10.setEnabled(false);
                		Word10.setBackgroundColor(getResources().getColor(R.color.green));
                		Word10.setTextColor(Color.BLACK);
                	} else if (wordfalse) {
                		Word10.setEnabled(false);
                		Word10.setBackgroundColor(getResources().getColor(R.color.red));
                		Word10.setHintTextColor(Color.BLACK);
                		Word10.setTextColor(Color.BLACK);
                	}
                }
                	
                if(correctfirst && !correcting) {
                	correcting = true;
                	incorrect = incorrect + 1;
            		Incorrect.setText(Integer.toString(incorrect));            		
                } else {
                	if(correcting) {
                		correcting = false;
                		Word2.setText(null);
	                	Word3.setText(null);
	                	Word4.setText(null);
	                	Word5.setText(null);
	                	Word6.setText(null);
	                	Word7.setText(null);
	                	Word8.setText(null);
	                	Word9.setText(null);
	                	Word10.setText(null);
	                	Word2.setBackgroundDrawable(TextEditBG);
	                	Word3.setBackgroundDrawable(TextEditBG);
	                	Word4.setBackgroundDrawable(TextEditBG);
	                	Word5.setBackgroundDrawable(TextEditBG);
	                	Word6.setBackgroundDrawable(TextEditBG);
	                	Word7.setBackgroundDrawable(TextEditBG);
	                	Word8.setBackgroundDrawable(TextEditBG);
	                	Word9.setBackgroundDrawable(TextEditBG);
	                	Word10.setBackgroundDrawable(TextEditBG);
	                	Word2.setTextColor(TextColor);
	                	Word3.setTextColor(TextColor);
	                	Word4.setTextColor(TextColor);
	                	Word5.setTextColor(TextColor);
	                	Word6.setTextColor(TextColor);
	                	Word7.setTextColor(TextColor);
	                	Word8.setTextColor(TextColor);
	                	Word9.setTextColor(TextColor);
	                	Word10.setTextColor(TextColor);
	                	Word2.setHintTextColor(HintColor);
	                	Word3.setHintTextColor(HintColor);
	                	Word4.setHintTextColor(HintColor);
	                	Word5.setHintTextColor(HintColor);
	                	Word6.setHintTextColor(HintColor);
	                	Word7.setHintTextColor(HintColor);
	                	Word8.setHintTextColor(HintColor);
	                	Word9.setHintTextColor(HintColor);
	                	Word10.setHintTextColor(HintColor);
	                	Word2.setEnabled(true);
	                	Word3.setEnabled(true);
	                	Word4.setEnabled(true);
	                	Word5.setEnabled(true);
	                	Word6.setEnabled(true);
	                	Word7.setEnabled(true);
	                	Word8.setEnabled(true);
	                	Word9.setEnabled(true);
	                	Word10.setEnabled(true);
                		if(!correctword) {
                			String toaststring = null;
		            		
		            		if(!answers.isNull(answers.getColumnIndex("worda")) && !answers.getString(answers.getColumnIndex("worda")).equals(word)) {
		                		toaststring = answers.getString(answers.getColumnIndex("worda"));
		                	}
		            		if(!answers.isNull(answers.getColumnIndex("wordb")) && !answers.getString(answers.getColumnIndex("wordb")).equals(word)) {
		                		String word3 = answers.getString(answers.getColumnIndex("wordb"));
		                		if(toaststring == null) {
		                			toaststring = word3;
		                		} else {
		                			toaststring = toaststring + System.getProperty("line.separator") + word3;
		                		}                		
		                	}
		                	if(!answers.isNull(answers.getColumnIndex("wordc")) && !answers.getString(answers.getColumnIndex("wordc")).equals(word)) {
		                		String word4 = answers.getString(answers.getColumnIndex("wordc"));
		                		if(toaststring == null) {
		                			toaststring = word4;
		                		} else {
		                			toaststring = toaststring + System.getProperty("line.separator") + word4;
		                		} 
		                	}
		                	if(!answers.isNull(answers.getColumnIndex("wordd")) && !answers.getString(answers.getColumnIndex("wordd")).equals(word)) {
		                		String word5 = answers.getString(answers.getColumnIndex("wordd"));  
		                		if(toaststring == null) {
		                			toaststring = word5;
		                		} else {
		                			toaststring = toaststring + System.getProperty("line.separator") + word5;
		                		} 
		                	}
		                	if(!answers.isNull(answers.getColumnIndex("worde")) && !answers.getString(answers.getColumnIndex("worde")).equals(word)) {
		                		String word6 = answers.getString(answers.getColumnIndex("worde"));
		                		if(toaststring == null) {
		                			toaststring = word6;
		                		} else {
		                			toaststring = toaststring + System.getProperty("line.separator") + word6;
		                		} 
		                	}
		                	if(!answers.isNull(answers.getColumnIndex("wordf")) && !answers.getString(answers.getColumnIndex("wordf")).equals(word)) {
		                		String word7 = answers.getString(answers.getColumnIndex("wordf"));
		                		if(toaststring == null) {
		                			toaststring = word7;
		                		} else {
		                			toaststring = toaststring + System.getProperty("line.separator") + word7;
		                		} 
		                	}
		                	if(!answers.isNull(answers.getColumnIndex("wordg")) && !answers.getString(answers.getColumnIndex("wordg")).equals(word)) {
		                		String word8 = answers.getString(answers.getColumnIndex("wordg"));
		                		if(toaststring == null) {
		                			toaststring = word8;
		                		} else {
		                			toaststring = toaststring + System.getProperty("line.separator") + word8;
		                		} 
		                	}
		                	if(!answers.isNull(answers.getColumnIndex("wordh")) && !answers.getString(answers.getColumnIndex("wordh")).equals(word)) {
		                		String word9 = answers.getString(answers.getColumnIndex("wordh"));
		                		if(toaststring == null) {
		                			toaststring = word9;
		                		} else {
		                			toaststring = toaststring + System.getProperty("line.separator") + word9;
		                		} 
		                	}
		                	if(!answers.isNull(answers.getColumnIndex("wordi")) && !answers.getString(answers.getColumnIndex("wordi")).equals(word)) {
		                		String word10 = answers.getString(answers.getColumnIndex("wordi"));
		                		if(toaststring == null) {
		                			toaststring = word10;
		                		} else {
		                			toaststring = toaststring + System.getProperty("line.separator") + word10;
		                		} 
		                	}
		                	if(!answers.isNull(answers.getColumnIndex("wordj")) && !answers.getString(answers.getColumnIndex("wordj")).equals(word)) {
		                		String word11 = answers.getString(answers.getColumnIndex("wordj"));
		                		if(toaststring == null) {
		                			toaststring = word11;
		                		} else {
		                			toaststring = toaststring + System.getProperty("line.separator") + word11;
		                		} 
		                	}
		                	int showtime = 1000 * settings.getInt("showanswer", 3);
		                	Toast toast = Toast.makeText(v.getContext(), toaststring, Toast.LENGTH_SHORT);
		                	ToastExpander.showFor(toast, showtime);
                		}                		
	            		total = total - 1;
	            		selected = selected + 1;
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
	                	Word1.setText(words.get(selected));
	                	WordsToGo.setText((getResources().getString(R.string.nog) + " " + total + " " + getResources().getString(R.string.wordsafter)).trim());
                	} else {
                		correcting = false;
	                	selected = selected + 1;
	                	Word2.setText(null);
	                	Word3.setText(null);
	                	Word4.setText(null);
	                	Word5.setText(null);
	                	Word6.setText(null);
	                	Word7.setText(null);
	                	Word8.setText(null);
	                	Word9.setText(null);
	                	Word10.setText(null);
	                	Word2.setBackgroundDrawable(TextEditBG);
	                	Word3.setBackgroundDrawable(TextEditBG);
	                	Word4.setBackgroundDrawable(TextEditBG);
	                	Word5.setBackgroundDrawable(TextEditBG);
	                	Word6.setBackgroundDrawable(TextEditBG);
	                	Word7.setBackgroundDrawable(TextEditBG);
	                	Word8.setBackgroundDrawable(TextEditBG);
	                	Word9.setBackgroundDrawable(TextEditBG);
	                	Word10.setBackgroundDrawable(TextEditBG);
	                	Word2.setTextColor(TextColor);
	                	Word3.setTextColor(TextColor);
	                	Word4.setTextColor(TextColor);
	                	Word5.setTextColor(TextColor);
	                	Word6.setTextColor(TextColor);
	                	Word7.setTextColor(TextColor);
	                	Word8.setTextColor(TextColor);
	                	Word9.setTextColor(TextColor);
	                	Word10.setTextColor(TextColor);
	                	Word2.setHintTextColor(HintColor);
	                	Word3.setHintTextColor(HintColor);
	                	Word4.setHintTextColor(HintColor);
	                	Word5.setHintTextColor(HintColor);
	                	Word6.setHintTextColor(HintColor);
	                	Word7.setHintTextColor(HintColor);
	                	Word8.setHintTextColor(HintColor);
	                	Word9.setHintTextColor(HintColor);
	                	Word10.setHintTextColor(HintColor);
	                	Word2.setEnabled(true);
	                	Word3.setEnabled(true);
	                	Word4.setEnabled(true);
	                	Word5.setEnabled(true);
	                	Word6.setEnabled(true);
	                	Word7.setEnabled(true);
	                	Word8.setEnabled(true);
	                	Word9.setEnabled(true);
	                	Word10.setEnabled(true);
		            	if(correctword) {
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
		                		Word1.setText("Klaar!");
		                		Intent intent = new Intent(v.getContext(), TestResultsActivity.class);
		                    	Bundle b = new Bundle();
		        				b.putString("id", extras.getString("id"));
		        				b.putString("method", getResources().getString(R.string.translate));
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
		            	} else {
		            		String toaststring = null;
		            		
		            		if(!answers.isNull(answers.getColumnIndex("worda")) && !answers.getString(answers.getColumnIndex("worda")).equals(word)) {
		                		toaststring = answers.getString(answers.getColumnIndex("worda"));
		                	}
		            		if(!answers.isNull(answers.getColumnIndex("wordb")) && !answers.getString(answers.getColumnIndex("wordb")).equals(word)) {
		                		String word3 = answers.getString(answers.getColumnIndex("wordb"));
		                		if(toaststring == null) {
		                			toaststring = word3;
		                		} else {
		                			toaststring = toaststring + System.getProperty("line.separator") + word3;
		                		}                		
		                	}
		                	if(!answers.isNull(answers.getColumnIndex("wordc")) && !answers.getString(answers.getColumnIndex("wordc")).equals(word)) {
		                		String word4 = answers.getString(answers.getColumnIndex("wordc"));
		                		if(toaststring == null) {
		                			toaststring = word4;
		                		} else {
		                			toaststring = toaststring + System.getProperty("line.separator") + word4;
		                		} 
		                	}
		                	if(!answers.isNull(answers.getColumnIndex("wordd")) && !answers.getString(answers.getColumnIndex("wordd")).equals(word)) {
		                		String word5 = answers.getString(answers.getColumnIndex("wordd"));  
		                		if(toaststring == null) {
		                			toaststring = word5;
		                		} else {
		                			toaststring = toaststring + System.getProperty("line.separator") + word5;
		                		} 
		                	}
		                	if(!answers.isNull(answers.getColumnIndex("worde")) && !answers.getString(answers.getColumnIndex("worde")).equals(word)) {
		                		String word6 = answers.getString(answers.getColumnIndex("worde"));
		                		if(toaststring == null) {
		                			toaststring = word6;
		                		} else {
		                			toaststring = toaststring + System.getProperty("line.separator") + word6;
		                		} 
		                	}
		                	if(!answers.isNull(answers.getColumnIndex("wordf")) && !answers.getString(answers.getColumnIndex("wordf")).equals(word)) {
		                		String word7 = answers.getString(answers.getColumnIndex("wordf"));
		                		if(toaststring == null) {
		                			toaststring = word7;
		                		} else {
		                			toaststring = toaststring + System.getProperty("line.separator") + word7;
		                		} 
		                	}
		                	if(!answers.isNull(answers.getColumnIndex("wordg")) && !answers.getString(answers.getColumnIndex("wordg")).equals(word)) {
		                		String word8 = answers.getString(answers.getColumnIndex("wordg"));
		                		if(toaststring == null) {
		                			toaststring = word8;
		                		} else {
		                			toaststring = toaststring + System.getProperty("line.separator") + word8;
		                		} 
		                	}
		                	if(!answers.isNull(answers.getColumnIndex("wordh")) && !answers.getString(answers.getColumnIndex("wordh")).equals(word)) {
		                		String word9 = answers.getString(answers.getColumnIndex("wordh"));
		                		if(toaststring == null) {
		                			toaststring = word9;
		                		} else {
		                			toaststring = toaststring + System.getProperty("line.separator") + word9;
		                		} 
		                	}
		                	if(!answers.isNull(answers.getColumnIndex("wordi")) && !answers.getString(answers.getColumnIndex("wordi")).equals(word)) {
		                		String word10 = answers.getString(answers.getColumnIndex("wordi"));
		                		if(toaststring == null) {
		                			toaststring = word10;
		                		} else {
		                			toaststring = toaststring + System.getProperty("line.separator") + word10;
		                		} 
		                	}
		                	if(!answers.isNull(answers.getColumnIndex("wordj")) && !answers.getString(answers.getColumnIndex("wordj")).equals(word)) {
		                		String word11 = answers.getString(answers.getColumnIndex("wordj"));
		                		if(toaststring == null) {
		                			toaststring = word11;
		                		} else {
		                			toaststring = toaststring + System.getProperty("line.separator") + word11;
		                		} 
		                	}
		                	int showtime = 1000 * settings.getInt("showanswer", 3);
		                	Toast toast = Toast.makeText(v.getContext(), toaststring, Toast.LENGTH_SHORT);
		                	ToastExpander.showFor(toast, showtime);
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
		            	}
                	}
		            answers.close();
		            dbHelper.close();                	
	            }
            }
    	});
        dbHelper.close();
	}
	
	public String stripPunctuation(String sentence){
		try {
			return sentence.replaceAll("\\p{Punct}+", "");
		} catch (Exception e) {
			e.printStackTrace();
			return sentence;
		}
	}
	
	private static final String PLAIN_ASCII = "AaEeIiOoUu" // grave
            + "AaEeIiOoUuYy" // acute
            + "AaEeIiOoUuYy" // circumflex
            + "AaOoNn" // tilde
            + "AaEeIiOoUuYy" // umlaut
            + "Aa" // ring
            + "Cc" // cedilla
            + "OoUu" // double acute
    ;

    private static final String UNICODE = "\u00C0\u00E0\u00C8\u00E8\u00CC\u00EC\u00D2\u00F2\u00D9\u00F9"
            + "\u00C1\u00E1\u00C9\u00E9\u00CD\u00ED\u00D3\u00F3\u00DA\u00FA\u00DD\u00FD"
            + "\u00C2\u00E2\u00CA\u00EA\u00CE\u00EE\u00D4\u00F4\u00DB\u00FB\u0176\u0177"
            + "\u00C3\u00E3\u00D5\u00F5\u00D1\u00F1"
            + "\u00C4\u00E4\u00CB\u00EB\u00CF\u00EF\u00D6\u00F6\u00DC\u00FC\u0178\u00FF"
            + "\u00C5\u00E5" + "\u00C7\u00E7" + "\u0150\u0151\u0170\u0171";

    /**
     * remove accented from a string and replace with ascii equivalent
     */
    public static String stripAccents(String s) {
        if (s == null)
            return null;
        StringBuilder sb = new StringBuilder(s.length());
        int n = s.length();
        int pos = -1;
        char c;
        boolean found = false;
        for (int i = 0; i < n; i++) {
            pos = -1;
            c = s.charAt(i);
            pos = (c <= 126) ? -1 : UNICODE.indexOf(c);
            if (pos > -1) {
                found = true;
                sb.append(PLAIN_ASCII.charAt(pos));
            } else {
                sb.append(c);
            }
        }
        if (!found) {
            return s;
        } else {
            return sb.toString();
        }
    }
	
	public String checkAnswerCharacters(String user, String answer, boolean checkupper) {
		Log.d("checkAnswerChar", "User said: "+user+", answer is: "+answer);
        char[] useranswer = user.toCharArray();
        char[] useranswerlowercase = user.toLowerCase().toCharArray();
        char[] realanswer = answer.toCharArray();
        char[] realanswerlowercase = answer.toLowerCase().toCharArray();
        
        int error = 0;
        StringBuilder sb = new StringBuilder();
        if(useranswer.length == realanswer.length) {
        	if(checkupper == true) {
	        	for(int i = 0; i < useranswer.length; i++) {
	        		if(useranswer[i] == realanswer[i]) {
	        			sb.append(useranswer[i]);
	        		} else {
	        			error++;
	        			sb.append(".");
	        		}
	        	}
        	} else {
        		for(int i = 0; i < useranswerlowercase.length; i++) {
	        		if(useranswerlowercase[i] == realanswerlowercase[i]) {
	        			sb.append(useranswerlowercase[i]);
	        		} else {
	        			error++;
	        			sb.append(".");
	        		}
	        	}
        	}
        	if(error < 4) {
        		return sb.toString();
        	}
        }
        Log.i("Translate Activity", "Error");
		return "Error";
	}

	public void checkAnswer(String ans2, String word2) {
		partlyok = false;
		wordok = false;
		wordfalse = false;
		correctedword = null;
		Boolean uppercase = settings.getBoolean("uppercase", false);
    	Boolean accents = settings.getBoolean("accents", false);
    	Boolean punctuation = settings.getBoolean("punctuation", false);    	
    	String ans2p = stripPunctuation(ans2);
		String word2p = stripPunctuation(word2);
		String ans2a = ans2;
		String ans2ap = ans2;
		String word2a = word2;
		String word2ap = word2;		
    	ans2a = stripAccents(ans2);
    	ans2ap = stripAccents(ans2p);
    	word2a = stripAccents(word2);
    	word2ap = stripAccents(word2);
		/*if(ans2.equals("No-Ads")) {
			SharedPreferences.Editor editor = settings.edit();
			editor.putBoolean("no-ads", true);
			editor.commit();
			Toast.makeText(this, "Advertentie's uitgeschakeld!", Toast.LENGTH_LONG).show();
		}*/
		if(uppercase) {
			if(accents) {
				if(punctuation) {
					if(word2.equals(ans2)) {
						//Log.i("CHECK WORD", "u-a-p");
						wordok = true;
					} else {
						//Log.i("CHECK WORD", "!(u-a-p)");
						String charcheck = checkAnswerCharacters(ans2, word2, true);
						if(!charcheck.equals("Error")) {							
							partlyok = true;
							correctedword = charcheck;
						}
						correctword = false;
						wordfalse = true;
					}
				} else if (!punctuation) {
					if(word2.equals(ans2) || word2p.equals(ans2) || word2.equals(ans2p)) {
						//Log.i("CHECK WORD", "u-a-!p");
						wordok = true;
					} else {
						//Log.i("CHECK WORD", "!(u-a-!p)");
						String charcheck = checkAnswerCharacters(ans2, word2, true);
						String charcheck1 = checkAnswerCharacters(ans2, word2p, true);
						String charcheck2 = checkAnswerCharacters(ans2p, word2, true);						
						if(!charcheck.equals("Error")) {
							partlyok = true;
							correctedword = charcheck;
						}
						if(!charcheck1.equals("Error")) {
							partlyok = true;
							correctedword = charcheck1;
						}
						if(!charcheck2.equals("Error")) {
							partlyok = true;
							correctedword = charcheck2;
						}		
						correctword = false;						
					}
				}
			} else if(!accents) {
				if(punctuation) {
					if(word2.equals(ans2) || word2a.equals(ans2) || word2.equals(ans2a)) {
						//Log.i("CHECK WORD", "u-!a-p");
						wordok = true;
					} else {
						//Log.i("CHECK WORD", "!(u-!a-p)");
						String charcheck = checkAnswerCharacters(ans2, word2, true);
						String charcheck1 = checkAnswerCharacters(ans2, word2a, true);
						String charcheck2 = checkAnswerCharacters(ans2a, word2, true);						
						if(!charcheck.equals("Error")) {
							partlyok = true;
							correctedword = charcheck;
						}
						if(!charcheck1.equals("Error")) {
							partlyok = true;
							correctedword = charcheck1;
						}
						if(!charcheck2.equals("Error")) {
							partlyok = true;
							correctedword = charcheck2;
						}		
						correctword = false;
						wordfalse = true;
					}
				} else if (!punctuation) {
					if(word2.equals(ans2) || word2p.equals(ans2) || word2.equals(ans2p) || word2a.equals(ans2) || word2.equals(ans2a) || word2ap.equals(ans2) || word2.equals(ans2ap)) {
						//Log.i("CHECK WORD", "u-!a-!p");
						wordok = true;
					} else {
						//Log.i("CHECK WORD", "!(u-!a-!p)");
						String charcheck = checkAnswerCharacters(ans2, word2, true);
						String charcheck1 = checkAnswerCharacters(ans2, word2p, true);
						String charcheck2 = checkAnswerCharacters(ans2p, word2, true);
						String charcheck3 = checkAnswerCharacters(ans2, word2a, true);
						String charcheck4 = checkAnswerCharacters(ans2a, word2, true);
						String charcheck5 = checkAnswerCharacters(ans2, word2ap, true);	
						String charcheck6 = checkAnswerCharacters(ans2ap, word2, true);	
						if(!charcheck.equals("Error")) {
							partlyok = true;
							correctedword = charcheck;
						}
						if(!charcheck1.equals("Error")) {
							partlyok = true;
							correctedword = charcheck1;
						}
						if(!charcheck2.equals("Error")) {
							partlyok = true;
							correctedword = charcheck2;
						}	
						if(!charcheck3.equals("Error")) {
							partlyok = true;
							correctedword = charcheck3;
						}
						if(!charcheck4.equals("Error")) {
							partlyok = true;
							correctedword = charcheck4;
						}
						if(!charcheck5.equals("Error")) {
							partlyok = true;
							correctedword = charcheck5;
						}
						if(!charcheck6.equals("Error")) {
							partlyok = true;
							correctedword = charcheck6;
						}
						correctword = false;
						wordfalse = true;
					}
				}
			}
		} else if(!uppercase) {
			if(accents) {
				if(punctuation) {
					if(word2.equalsIgnoreCase(ans2)) {
						Log.i("CHECK WORD", "!u-a-p");
						wordok = true;
					} else {
						Log.i("CHECK WORD", "!(!u-a-p)");
						String charcheck = checkAnswerCharacters(ans2, word2, false);
						if(!charcheck.equals("Error")) {							
							partlyok = true;
							correctedword = charcheck;
						}
						correctword = false;
						wordfalse = true;
					}
				} else if (!punctuation) {
					if(word2.equalsIgnoreCase(ans2) || word2p.equalsIgnoreCase(ans2) || word2.equalsIgnoreCase(ans2p)) {
						Log.i("CHECK WORD", "!u-a-!p");
						wordok = true;
					} else {
						Log.i("CHECK WORD", "!(!u-a-!p)");
						String charcheck = checkAnswerCharacters(ans2, word2, false);
						String charcheck1 = checkAnswerCharacters(ans2, word2p, false);
						String charcheck2 = checkAnswerCharacters(ans2p, word2, false);						
						if(!charcheck.equals("Error")) {
							partlyok = true;
							correctedword = charcheck;
						}
						if(!charcheck1.equals("Error")) {
							partlyok = true;
							correctedword = charcheck1;
						}
						if(!charcheck2.equals("Error")) {
							partlyok = true;
							correctedword = charcheck2;
						}
						correctword = false;
						wordfalse = true;
					}
				}
			} else if(!accents) {
				if(punctuation) {
					if(word2.equalsIgnoreCase(ans2) || word2a.equalsIgnoreCase(ans2) || word2.equalsIgnoreCase(ans2a)) {
						Log.i("CHECK WORD", "!u-!a-p");
						wordok = true;
					} else {
						Log.i("CHECK WORD", "!(!u-!a-p)");
						String charcheck = checkAnswerCharacters(ans2, word2, false);
						String charcheck1 = checkAnswerCharacters(ans2, word2a, false);
						String charcheck2 = checkAnswerCharacters(ans2a, word2, false);						
						if(!charcheck.equals("Error")) {
							partlyok = true;
							correctedword = charcheck;
						}
						if(!charcheck1.equals("Error")) {
							partlyok = true;
							correctedword = charcheck1;
						}
						if(!charcheck2.equals("Error")) {
							partlyok = true;
							correctedword = charcheck2;
						}		
						correctword = false;
						wordfalse = true;
					}
				} else if (!punctuation) {
					if(word2.equalsIgnoreCase(ans2) || word2p.equalsIgnoreCase(ans2) || word2.equalsIgnoreCase(ans2p) || word2a.equalsIgnoreCase(ans2) || word2.equalsIgnoreCase(ans2a) || word2ap.equalsIgnoreCase(ans2) || word2.equalsIgnoreCase(ans2ap)) {
						Log.i("CHECK WORD", "!u-!a-!p");
						wordok = true;
					} else {
						Log.i("CHECK WORD", "!(!u-!a-!p)");
						String charcheck = checkAnswerCharacters(ans2, word2, true);
						String charcheck1 = checkAnswerCharacters(ans2, word2p, true);
						String charcheck2 = checkAnswerCharacters(ans2p, word2, true);
						String charcheck3 = checkAnswerCharacters(ans2, word2a, true);
						String charcheck4 = checkAnswerCharacters(ans2a, word2, true);
						String charcheck5 = checkAnswerCharacters(ans2, word2ap, true);	
						String charcheck6 = checkAnswerCharacters(ans2ap, word2, true);	
						if(!charcheck.equals("Error")) {
							partlyok = true;
							correctedword = charcheck;
						}
						if(!charcheck1.equals("Error")) {
							partlyok = true;
							correctedword = charcheck1;
						}
						if(!charcheck2.equals("Error")) {
							partlyok = true;
							correctedword = charcheck2;
						}	
						if(!charcheck3.equals("Error")) {
							partlyok = true;
							correctedword = charcheck3;
						}
						if(!charcheck4.equals("Error")) {
							partlyok = true;
							correctedword = charcheck4;
						}
						if(!charcheck5.equals("Error")) {
							partlyok = true;
							correctedword = charcheck5;
						}
						if(!charcheck6.equals("Error")) {
							partlyok = true;
							correctedword = charcheck6;
						}
						correctword = false;
						wordfalse = true;
					}
				}
			}
		}
	}
	
	public OnEditorActionListener editoraction = new OnEditorActionListener() {
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
            	CheckButton.performClick();       	
            }
            Word2.requestFocus();
            return false;
        }
    };
    
    public void onStop() {
       super.onStop();
       FlurryAgent.onEndSession(this);
    }
}
