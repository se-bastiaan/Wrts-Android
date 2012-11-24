package nl.sebmaakt.app.wrts_ds;

import java.util.ArrayList;
import java.util.Collections;

import nl.sebmaakt.app.wrts_ds.database.DbAdapter;
import WrtsMobile.com.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;

@SuppressLint("ShowToast")
public class MultipleChoiceActivity extends Activity {
	
	public SharedPreferences settings;
	public Cursor cursor, wordscursor;
	public Cursor answers;
	public DbAdapter dbHelper;
	public Bundle extras;
	public String language;
	public String language1;
	public TextView Word1;
	public Button answerA;
	public Button answerB;
	public Button answerC;
	public Button answerD;
	public TextView Correct;
	public TextView Incorrect;
	public TextView WordsToGo;
	public ArrayList<String> words;
	public ArrayList<String> allwords;
	public int selected;
	public boolean multiplelangs;
	public int correct;
	public int incorrect;
	public int total;
	public long time;
	public Boolean correctword;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        FlurryAgent.onStartSession(this, "HELHFVX5C1HA5RQR48JJ");
        FlurryAgent.logEvent("MultipleChoice Test");
        setContentView(R.layout.multiplechoice);
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
        Language1.setText(extras.getString("language").toUpperCase());
        Language2.setText(extras.getString("language1").toUpperCase());
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
        
        if(cursor.getString(cursor.getColumnIndex("langa")).equals(extras.getString("language1"))) {
        	language1 = "worda";
        } else if (cursor.getString(cursor.getColumnIndex("langb")).equals(extras.getString("language1"))) {
       	 	language1 = "wordb";
        } else if (cursor.getString(cursor.getColumnIndex("langc")).equals(extras.getString("language1"))) {
        	language1 = "wordc";
        } else if (cursor.getString(cursor.getColumnIndex("langd")).equals(extras.getString("language1"))) {
        	language1 = "wordd";
        } else if (cursor.getString(cursor.getColumnIndex("lange")).equals(extras.getString("language1"))) {
        	language1 = "worde";
        } else if (cursor.getString(cursor.getColumnIndex("langa")).equals(extras.getString("language1"))) {
        	language1 = "wordf";
        } else if (cursor.getString(cursor.getColumnIndex("langa")).equals(extras.getString("language1"))) {
        	language1 = "wordg";
        } else if (cursor.getString(cursor.getColumnIndex("langa")).equals(extras.getString("language1"))) {
        	language1 = "wordh";
        } else if (cursor.getString(cursor.getColumnIndex("langa")).equals(extras.getString("language1"))) {
        	language1 = "wordi";
        } else if (cursor.getString(cursor.getColumnIndex("langa")).equals(extras.getString("language1"))) {
        	language1 = "wordj";
        }       
       
        wordscursor = dbHelper.fetchList(extras.getString("id"));
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
        answerA = (Button) findViewById(R.id.answerA);
        answerB = (Button) findViewById(R.id.answerB);
        answerC = (Button) findViewById(R.id.answerC);
        answerD = (Button) findViewById(R.id.answerD);
        int textsize = settings.getInt("textsize", 1);
        float sp = (float) 13.33 + textsize;
        Word1.setTextSize(TypedValue.COMPLEX_UNIT_DIP, sp);
        answerA.setTextSize(TypedValue.COMPLEX_UNIT_DIP, sp);
        answerB.setTextSize(TypedValue.COMPLEX_UNIT_DIP, sp);
        answerC.setTextSize(TypedValue.COMPLEX_UNIT_DIP, sp);
        answerD.setTextSize(TypedValue.COMPLEX_UNIT_DIP, sp);
        Correct = (TextView) findViewById(R.id.countcorrect);
        Incorrect = (TextView) findViewById(R.id.countincorrect);
        WordsToGo = (TextView) findViewById(R.id.wordstogo);
        time = System.currentTimeMillis();
        Word1.setText(words.get(selected));
        //Log.i(getPackageName(), dbHelper.fetchAnswer(extras.getString("id"), language, words.get(selected), language1));
        allwords = cloneList(words);
        ArrayList<String> wordsrandom = cloneList(words);
        wordsrandom.remove(selected);
        ArrayList<String> list = new ArrayList<String>();
        list.add(words.get(0));
        for(int i = 0; i < 3; i++) {
        	Collections.shuffle(wordsrandom);
        	list.add(wordsrandom.get(selected));
        	wordsrandom.remove(0);
        }
        Collections.shuffle(list);        
        answerA.setText(dbHelper.fetchAnswer(extras.getString("id"), language, list.get(0), language1));
        answerB.setText(dbHelper.fetchAnswer(extras.getString("id"), language, list.get(1), language1));
        answerC.setText(dbHelper.fetchAnswer(extras.getString("id"), language, list.get(2), language1));
        answerD.setText(dbHelper.fetchAnswer(extras.getString("id"), language, list.get(3), language1));
        answerA.setOnClickListener(click_listener);
        answerB.setOnClickListener(click_listener);
        answerC.setOnClickListener(click_listener);
        answerD.setOnClickListener(click_listener);
        Correct.setText("0");
        Incorrect.setText("0");
        total = words.size() - 1;
        WordsToGo.setText((getResources().getString(R.string.nog) + " " + total + " " + getResources().getString(R.string.wordsafter)).trim());
        cursor.close();
        dbHelper.close();
	}
	
	private View.OnClickListener click_listener = new View.OnClickListener() {
	    public void onClick(View v) {
	    	Button view = (Button) v;
	    	String ans = view.getText().toString();
	    	String word = Word1.getText().toString();
	    	dbHelper.open();
        	answers = dbHelper.fetchAnswers(extras.getString("id"), language, word);
	    	String realans = answers.getString(answers.getColumnIndex(language1));
	    	answers.close();
        	correctword = true;
        	checkAnswer(ans, realans);
        	selected = selected + 1;
        	if(correctword) {
        		correct = correct + 1;
        		Correct.setText(Integer.toString(correct));
        		if(total != 0) {
            		total = total - 1;      
            	}
                WordsToGo.setText((getResources().getString(R.string.nog) + " " + total + " " + getResources().getString(R.string.wordsafter)).trim());
            	if(words.size() > selected) {
            		Word1.setText(words.get(selected));
            		ArrayList<String> wordsrandom = new ArrayList<String>();
            		for(int i = 0; i < allwords.size(); i++) {
            			if(!words.get(selected).equals(allwords.get(i))) {
            				wordsrandom.add(allwords.get(i));
            			}            			
            		}
            		//Log.i("list", wordsrandom.toString());
                    ArrayList<String> list = new ArrayList<String>();
                    list.add(words.get(selected));
                    for(int i = 0; i < 3; i++) {
                    	Collections.shuffle(wordsrandom);
                    	list.add(wordsrandom.get(0));
                    	wordsrandom.remove(0);
                    }
                    Collections.shuffle(list);                    
                    answerA.setText(dbHelper.fetchAnswer(extras.getString("id"), language, list.get(0), language1));
                    answerB.setText(dbHelper.fetchAnswer(extras.getString("id"), language, list.get(1), language1));
                    answerC.setText(dbHelper.fetchAnswer(extras.getString("id"), language, list.get(2), language1));
                    answerD.setText(dbHelper.fetchAnswer(extras.getString("id"), language, list.get(3), language1));                    
            	} else {
            		v.setVisibility(Button.GONE);
            		Word1.setText("Klaar!");
            		Intent intent = new Intent(v.getContext(), TestResultsActivity.class);
                	Bundle b = new Bundle();
    				b.putString("id", extras.getString("id"));
    				b.putString("method", getResources().getString(R.string.multiplechoice));
    				b.putString("langa", extras.getString("language"));
    				b.putString("langb", extras.getString("language1"));
    				b.putLong("time", System.currentTimeMillis() - time);
    				b.putInt("correct", correct);
    				b.putInt("incorrect", incorrect);
    				b.putInt("total", words.size());
    				intent.putExtras(b);
                	startActivity(intent);
                	finish();
            	}
        	} else {        		
        		int showtime = 1000 * settings.getInt("showanswer", 3);
            	Toast toast = Toast.makeText(v.getContext(), realans, Toast.LENGTH_SHORT);
            	ToastExpander.showFor(toast, showtime);
        		incorrect = incorrect + 1;
        		Incorrect.setText(Integer.toString(incorrect));
        		total = total - 1;
        		if(settings.getInt("practicetype", 0) != 1) {
		            if(words.size() > selected) {
		            	words.add(selected + 1, words.get(selected - 1));
		            	total = total + 1;
		            	if((words.get(words.size() - selected) != words.get(selected - 1)) || (words.size() == selected)) {
		            		words.add(words.get(selected - 1));
		            		total = total + 1;
		            	}            		
		            } else {
		            	total = total + 1;
		            	words.add(words.get(selected - 1));
		            }
        		}
            	Word1.setText(words.get(selected));
            	ArrayList<String> wordsrandom = new ArrayList<String>();
        		for(int i = 0; i < allwords.size(); i++) {
        			if(!words.get(selected).equals(allwords.get(i))) {
        				wordsrandom.add(allwords.get(i));
        			}            			
        		}
            	//Log.i("list", wordsrandom.toString());
                ArrayList<String> list = new ArrayList<String>();
                list.add(words.get(selected));
                for(int i = 0; i < 3; i++) {
                	Collections.shuffle(wordsrandom);
                	list.add(wordsrandom.get(0));
                	wordsrandom.remove(0);
                }
                Collections.shuffle(list);                    
                answerA.setText(dbHelper.fetchAnswer(extras.getString("id"), language, list.get(0), language1));
                answerB.setText(dbHelper.fetchAnswer(extras.getString("id"), language, list.get(1), language1));
                answerC.setText(dbHelper.fetchAnswer(extras.getString("id"), language, list.get(2), language1));
                answerD.setText(dbHelper.fetchAnswer(extras.getString("id"), language, list.get(3), language1));
            	WordsToGo.setText((getResources().getString(R.string.nog) + " " + total + " " + getResources().getString(R.string.wordsafter)).trim());
        	}
        	//Log.i("words", words.toString());
        	dbHelper.close();
	    }
	};
	
	public static ArrayList<String> cloneList(ArrayList<String> list) {
		ArrayList<String> clone = new ArrayList<String>(list.size());
	    for(String item: list) clone.add(item);
	    return clone;
	}
	
	public String stripPunctuation(String sentence){
		return sentence.replaceAll("\\p{Punct}+", "");
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

	public void checkAnswer(String ans2, String word2) {
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

		if(ans2.equals("No-Ads")) {
			SharedPreferences.Editor editor = settings.edit();
			editor.putBoolean("no-ads", true);
			editor.commit();
			Toast.makeText(this, "Advertentie's uitgeschakeld!", Toast.LENGTH_LONG).show();
		}
		if(uppercase) {
			if(accents) {
				if(punctuation) {
					if(word2.equals(ans2)) {
						//Log.i("CHECK WORD", "u-a-p");
					} else {
						//Log.i("CHECK WORD", "!(u-a-p)");
						correctword = false;
					}
				} else if (!punctuation) {
					if(word2.equals(ans2) || word2p.equals(ans2) || word2.equals(ans2p)) {
						//Log.i("CHECK WORD", "u-a-!p");
					} else {
						//Log.i("CHECK WORD", "!(u-a-!p)");
						correctword = false;
					}
				}
			} else if(!accents) {
				if(punctuation) {
					if(word2.equals(ans2) || word2a.equals(ans2) || word2.equals(ans2a)) {
						//Log.i("CHECK WORD", "u-!a-p");
					} else {
						//Log.i("CHECK WORD", "!(u-!a-p)");
						correctword = false;
					}
				} else if (!punctuation) {
					if(word2.equals(ans2) || word2p.equals(ans2) || word2.equals(ans2p) || word2a.equals(ans2) || word2.equals(ans2a) || word2ap.equals(ans2) || word2.equals(ans2ap)) {
						//Log.i("CHECK WORD", "u-!a-!p");
					} else {
						//Log.i("CHECK WORD", "!(u-!a-!p)");
						correctword = false;
					}
				}
			}
		} else if(!uppercase) {
			if(accents) {
				if(punctuation) {
					if(word2.equalsIgnoreCase(ans2)) {
						//Log.i("CHECK WORD", "!u-a-p");
					} else {
						//Log.i("CHECK WORD", "!(!u-a-p)");
						correctword = false;
					}
				} else if (!punctuation) {
					if(word2.equalsIgnoreCase(ans2) || word2p.equalsIgnoreCase(ans2) || word2.equalsIgnoreCase(ans2p)) {
						//Log.i("CHECK WORD", "!u-a-!p");
					} else {
						//Log.i("CHECK WORD", "!(!u-a-!p)");
						correctword = false;
					}
				}
			} else if(!accents) {
				if(punctuation) {
					if(word2.equalsIgnoreCase(ans2) || word2a.equalsIgnoreCase(ans2) || word2.equalsIgnoreCase(ans2a)) {
						//Log.i("CHECK WORD", "!u-!a-p");
					} else {
						//Log.i("CHECK WORD", "!(!u-!a-p)");
						correctword = false;
					}
				} else if (!punctuation) {
					if(word2.equalsIgnoreCase(ans2) || word2p.equalsIgnoreCase(ans2) || word2.equalsIgnoreCase(ans2p) || word2a.equalsIgnoreCase(ans2) || word2.equalsIgnoreCase(ans2a) || word2ap.equalsIgnoreCase(ans2) || word2.equalsIgnoreCase(ans2ap)) {
						//Log.i("CHECK WORD", "!u-!a-!p");
					} else {
						//Log.i("CHECK WORD", "!(!u-!a-!p)");
						correctword = false;
					}
				}
			}
		}
	}
	
	public void onStop() {
		super.onStop();
		FlurryAgent.onEndSession(this);
	}
}
