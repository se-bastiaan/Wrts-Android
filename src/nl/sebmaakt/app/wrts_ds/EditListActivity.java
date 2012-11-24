package nl.sebmaakt.app.wrts_ds;

import nl.sebmaakt.app.wrts_ds.database.DatabaseProvider;
import nl.sebmaakt.app.wrts_ds.database.DbAdapter;
import WrtsMobile.com.R;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;

@SuppressWarnings("unused")
public class EditListActivity extends Activity {
	
	private Cursor cursor, list;
	private DbAdapter dbHelper;	
	private Bundle extras;
	private String listid, id;
	private ImageButton SaveButton, NewButton;
	private ScrollView EditTitle, EditWord;
	private EditText TitleEdit, LangaEdit, LangbEdit, LangcEdit, LangdEdit, LangeEdit, LangfEdit, LanggEdit, LanghEdit, LangiEdit, LangjEdit;
	private LinearLayout LangcLayout, LangdLayout, LangeLayout, LangfLayout, LanggLayout, LanghLayout, LangiLayout, LangjLayout;
	private TextView WordaTitle, WordbTitle, WordcTitle, WorddTitle, WordeTitle, WordfTitle, WordgTitle, WordhTitle, WordiTitle, WordjTitle, WordaEdit;
	private EditText WordbEdit, WordcEdit, WorddEdit, WordeEdit, WordfEdit, WordgEdit, WordhEdit, WordiEdit, WordjEdit;
	private LinearLayout WordcLayout, WorddLayout, WordeLayout, WordfLayout, WordgLayout, WordhLayout, WordiLayout, WordjLayout;
	private int count;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        FlurryAgent.onStartSession(this, "HELHFVX5C1HA5RQR48JJ");
	        setContentView(R.layout.item_editor);
	        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	        dbHelper = new DbAdapter(this);
	        SaveButton = (ImageButton) findViewById(R.id.SaveButton);
	        NewButton = (ImageButton) findViewById(R.id.NewButton);
	        EditTitle = (ScrollView) findViewById(R.id.EditTitle);
	        EditWord = (ScrollView) findViewById(R.id.EditWord);
	        TitleEdit = (EditText) findViewById(R.id.title);
	        LangaEdit = (EditText) findViewById(R.id.langa);
	        LangbEdit = (EditText) findViewById(R.id.langb);
	        LangcEdit = (EditText) findViewById(R.id.langc);
	        LangdEdit = (EditText) findViewById(R.id.langd);
	        LangeEdit = (EditText) findViewById(R.id.lange);
	        LangfEdit = (EditText) findViewById(R.id.langf);
	        LanggEdit = (EditText) findViewById(R.id.langg);
	        LanghEdit = (EditText) findViewById(R.id.langh);
	        LangiEdit = (EditText) findViewById(R.id.langi);
	        LangjEdit = (EditText) findViewById(R.id.langj);
	        LangcLayout = (LinearLayout) findViewById(R.id.langclayout);
	        LangdLayout = (LinearLayout) findViewById(R.id.langdlayout);
	        LangeLayout = (LinearLayout) findViewById(R.id.langelayout);
	        LangfLayout = (LinearLayout) findViewById(R.id.langflayout);
	        LanggLayout = (LinearLayout) findViewById(R.id.langglayout);
	        LanghLayout = (LinearLayout) findViewById(R.id.langhlayout);
	        LangiLayout = (LinearLayout) findViewById(R.id.langilayout);
	        LangjLayout = (LinearLayout) findViewById(R.id.langjlayout);
	        WordaTitle = (TextView) findViewById(R.id.wordatitle);
	        WordbTitle = (TextView) findViewById(R.id.wordbtitle);
	        WordcTitle = (TextView) findViewById(R.id.wordctitle);
	        WorddTitle = (TextView) findViewById(R.id.worddtitle);
	        WordeTitle = (TextView) findViewById(R.id.wordetitle);
	        WordfTitle = (TextView) findViewById(R.id.wordftitle);
	        WordgTitle = (TextView) findViewById(R.id.wordgtitle);
	        WordhTitle = (TextView) findViewById(R.id.wordhtitle);
	        WordiTitle = (TextView) findViewById(R.id.wordititle);
	        WordjTitle = (TextView) findViewById(R.id.wordjtitle);
	        WordaEdit = (EditText) findViewById(R.id.worda);
	        WordbEdit = (EditText) findViewById(R.id.wordb);
	        WordcEdit = (EditText) findViewById(R.id.wordc);
	        WorddEdit = (EditText) findViewById(R.id.wordd);
	        WordeEdit = (EditText) findViewById(R.id.worde);
	        WordfEdit = (EditText) findViewById(R.id.wordf);
	        WordgEdit = (EditText) findViewById(R.id.wordg);
	        WordhEdit = (EditText) findViewById(R.id.wordh);
	        WordiEdit = (EditText) findViewById(R.id.wordi);
	        WordjEdit = (EditText) findViewById(R.id.wordj);
	        WordcLayout = (LinearLayout) findViewById(R.id.wordclayout);
	        WorddLayout = (LinearLayout) findViewById(R.id.worddlayout);
	        WordeLayout = (LinearLayout) findViewById(R.id.wordelayout);
	        WordfLayout = (LinearLayout) findViewById(R.id.wordflayout);
	        WordgLayout = (LinearLayout) findViewById(R.id.wordglayout);
	        WordhLayout = (LinearLayout) findViewById(R.id.wordhlayout);
	        WordiLayout = (LinearLayout) findViewById(R.id.wordilayout);
	        WordjLayout = (LinearLayout) findViewById(R.id.wordjlayout);
	        dbHelper.open();
	        extras = getIntent().getExtras();    		
	        String type = extras.getString("type");
	        listid = extras.getString("listid");
	        id = extras.getString("id");	        
	        if(!extras.containsKey("type")) {
	        	Intent prepare = new Intent(getApplicationContext(), ShowListActivity.class);
            	Bundle b = new Bundle();
            	b.putString("id", listid);
				b.putBoolean("editmode", true);
				prepare.putExtras(b);
            	startActivity(prepare);
	        	finish();
	        } else if (type.equals("title")) {
	        	EditTitle.setVisibility(ScrollView.VISIBLE);
	        	NewButton.setVisibility(View.VISIBLE);
	        	//Log.i("TitleEdit", extras.getString("listid"));
	        	count = 1;
	        	if(!extras.containsKey("new")) {
	        		TitleEdit.setText("");
		    		LangaEdit.setText("");
		    		LangbEdit.setText("");
		    		LangcEdit.setText("");
	    			LangdEdit.setText("");
	    			LangeEdit.setText("");
	    			LangfEdit.setText("");
	    			LanggEdit.setText("");
	    			LanghEdit.setText("");
	    			LangiEdit.setText("");
	    			LangjEdit.setText("");
		        	cursor = getContentResolver().query(Uri.parse(DatabaseProvider.LISTDATA_URI.toString() + "/" + listid), null, null, null, null);
		    		TitleEdit.setText(cursor.getString(cursor.getColumnIndex("title")));
		    		LangaEdit.setText(cursor.getString(cursor.getColumnIndex("langa")));
		    		LangbEdit.setText(cursor.getString(cursor.getColumnIndex("langb")));
		    		LangcEdit.setText(cursor.getString(cursor.getColumnIndex("langc")));
	    			LangdEdit.setText(cursor.getString(cursor.getColumnIndex("langd")));
	    			LangeEdit.setText(cursor.getString(cursor.getColumnIndex("lange")));
	    			LangfEdit.setText(cursor.getString(cursor.getColumnIndex("langf")));
	    			LanggEdit.setText(cursor.getString(cursor.getColumnIndex("langg")));
	    			LanghEdit.setText(cursor.getString(cursor.getColumnIndex("langh")));
	    			LangiEdit.setText(cursor.getString(cursor.getColumnIndex("langi")));
	    			LangjEdit.setText(cursor.getString(cursor.getColumnIndex("langj")));
		    		if(!cursor.isNull(cursor.getColumnIndex("langc"))) {
		    			LangcLayout.setVisibility(View.VISIBLE);
		    			count++;
		    		}
		    		if(!cursor.isNull(cursor.getColumnIndex("langd"))) {
		    			LangdLayout.setVisibility(View.VISIBLE);
		    			count++;
		    		}
		    		if(!cursor.isNull(cursor.getColumnIndex("lange"))) {
		    			LangeLayout.setVisibility(View.VISIBLE);
		    			count++;
		    		}
		    		if(!cursor.isNull(cursor.getColumnIndex("langf"))) {
		    			LangfLayout.setVisibility(View.VISIBLE);
		    			count++;
		    		}
		    		if(!cursor.isNull(cursor.getColumnIndex("langg"))) {
		    			LanggLayout.setVisibility(View.VISIBLE);
		    			count++;
		    		}
		    		if(!cursor.isNull(cursor.getColumnIndex("langh"))) {
		    			LanghLayout.setVisibility(View.VISIBLE);
		    			count++;
		    		}
		    		if(!cursor.isNull(cursor.getColumnIndex("langi"))) {
		    			LangiLayout.setVisibility(View.VISIBLE);
		    			count++;
		    		}
		    		if(!cursor.isNull(cursor.getColumnIndex("langj"))) {
		    			LangjLayout.setVisibility(View.VISIBLE);
		    			NewButton.setVisibility(View.GONE);
		    			count++;
		    		}
		    		cursor.close();
	        	}
	        	if(extras.containsKey("new") && extras.containsKey("langa") && extras.containsKey("langb")) {
	        		LangaEdit.setText(extras.getString("langa"));
		    		LangbEdit.setText(extras.getString("langb"));
	        	}
	    		NewButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {  
                    	if(count < 9) {
                    		if(count == 1) {
                    			LangcLayout.setVisibility(View.VISIBLE);
            	    			count++;
                    		} else if (count == 2) {
                    			LangdLayout.setVisibility(View.VISIBLE);
            	    			count++;
                    		} else if (count == 3) {
                    			LangeLayout.setVisibility(View.VISIBLE);
            	    			count++;
                    		} else if (count == 4) {
                    			LangfLayout.setVisibility(View.VISIBLE);
            	    			count++;
                    		} else if (count == 5) {
                    			LanggLayout.setVisibility(View.VISIBLE);
            	    			count++;
                    		} else if (count == 6) {
                    			LanghLayout.setVisibility(View.VISIBLE);
            	    			count++;
                    		} else if (count == 7) {
                    			LangiLayout.setVisibility(View.VISIBLE);
            	    			count++;
                    		} else if (count == 8) {
                    			LangjLayout.setVisibility(View.VISIBLE);
            	    			NewButton.setVisibility(View.GONE);
            	    			count++;
                    		}
                    	}
                    }
	    		});
	    		
	    		SaveButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {                    	
                    	String title = TitleEdit.getText().toString();
                    	String langa = LangaEdit.getText().toString();
                    	String langb = LangbEdit.getText().toString();
                    	String langc = LangcEdit.getText().toString();
                    	String langd = LangdEdit.getText().toString();
                    	String lange = LangeEdit.getText().toString();
                    	String langf = LangfEdit.getText().toString();
                    	String langg = LanggEdit.getText().toString();
                    	String langh = LanghEdit.getText().toString();
                    	String langi = LangiEdit.getText().toString();
                    	String langj = LangjEdit.getText().toString();
                    	if(langa.equals("")) {
                    		langa = "Language 1";
                    	}
                    	if(langb.equals("")) {
                    		langb = "Language 2";
                    	}
                    	if(langc.equals("")) {
                    		langc = null;
                    	}
                    	if(langd.equals("")) {
                    		langd = null;
                    	}
                    	if(lange.equals("")) {
                    		lange = null;
                    	}
                    	if(langf.equals("")) {
                    		langf = null;
                    	}
                    	if(langg.equals("")) {
                    		langg = null;
                    	}
                    	if(langh.equals("")) {
                    		langh = null;
                    	}
                    	if(langi.equals("")) {
                    		langi = null;
                    	}                    	
                    	if(langj.equals("")) {
                    		langj = null;
                    	}
                    	dbHelper.open();
                    	if(!extras.containsKey("new")) {
                    		dbHelper.updateList(listid, title, langa, langb, langc, langd, lange, langf, langg, langh, langi, langj);
                    	} else {
                    		listid = dbHelper.newList(title, langa, langb, langc, langd, lange, langf, langg, langh, langi, langj);
                    	}
                    	dbHelper.close();
                    	Intent prepare = new Intent(v.getContext(), ShowListActivity.class);
                    	Bundle b = new Bundle();
                    	b.putString("id", listid);                    	
        				b.putBoolean("editmode", true);
        				if(extras.containsKey("new")) {
                    		b.putBoolean("new", true);
        				}
        				prepare.putExtras(b);
                    	startActivity(prepare);
                    	finish();
                   }
                });
	        } else if (type.equals("word")) {
	        	EditWord.setVisibility(ScrollView.VISIBLE);
	        	//Log.i("EDITOR", "wordtype");
	        	if(!extras.containsKey("new")) {
	        		cursor = dbHelper.fetchWord(listid, id);
	        	}
	        	list = getContentResolver().query(Uri.parse(DatabaseProvider.LISTDATA_URI.toString() + "/" + listid), null, null, null, null);
	        	WordaTitle.setText(list.getString(list.getColumnIndex("langa")));	        	
	        	WordbTitle.setText(list.getString(list.getColumnIndex("langb")));
	        	if(!extras.containsKey("new")) {
		        	WordaEdit.setText(cursor.getString(cursor.getColumnIndex("worda")));
		    		WordbEdit.setText(cursor.getString(cursor.getColumnIndex("wordb")));
	    			WordcEdit.setText(cursor.getString(cursor.getColumnIndex("wordc")));
	    			WorddEdit.setText(cursor.getString(cursor.getColumnIndex("wordd")));    	
	    			WordeEdit.setText(cursor.getString(cursor.getColumnIndex("worde")));
	    			WordfEdit.setText(cursor.getString(cursor.getColumnIndex("wordf")));	
	    			WordgEdit.setText(cursor.getString(cursor.getColumnIndex("wordg")));	
	    			WordhEdit.setText(cursor.getString(cursor.getColumnIndex("wordh")));
	    			WordiEdit.setText(cursor.getString(cursor.getColumnIndex("wordi")));
	    			WordjEdit.setText(cursor.getString(cursor.getColumnIndex("wordj")));
	        	}
	    		if(!list.isNull(list.getColumnIndex("wordc"))) {
	    			WordcTitle.setText(list.getString(list.getColumnIndex("langc")));
	    			WordcLayout.setVisibility(View.VISIBLE);
	    		}
	    		if(!list.isNull(list.getColumnIndex("langd"))) {
	    			WorddTitle.setText(list.getString(list.getColumnIndex("langd")));
	    			WorddLayout.setVisibility(View.VISIBLE);
	    		}
	    		if(!list.isNull(list.getColumnIndex("lange"))) {
	    			WordeTitle.setText(list.getString(list.getColumnIndex("lange")));
	    			WordeLayout.setVisibility(View.VISIBLE);
	    		}
	    		if(!list.isNull(list.getColumnIndex("langf"))) {
	    			WordfTitle.setText(list.getString(list.getColumnIndex("langf")));
	    			WordfLayout.setVisibility(View.VISIBLE);
	    		}
	    		if(!list.isNull(list.getColumnIndex("langg"))) {
	    			WordgTitle.setText(list.getString(list.getColumnIndex("langg")));
	    			WordgLayout.setVisibility(View.VISIBLE);
	    		}
	    		if(!list.isNull(list.getColumnIndex("langh"))) {
	    			WordhTitle.setText(list.getString(list.getColumnIndex("langh")));
	    			WordhLayout.setVisibility(View.VISIBLE);
	    		}
	    		if(!list.isNull(list.getColumnIndex("langi"))) {
	    			WordiTitle.setText(list.getString(list.getColumnIndex("langi")));
	    			WordiLayout.setVisibility(View.VISIBLE);
	    		}
	    		if(!list.isNull(list.getColumnIndex("langj"))) {
	    			WordjTitle.setText(list.getString(list.getColumnIndex("langj")));
	    			WordjLayout.setVisibility(View.VISIBLE);
	    		}
	    		if(!extras.containsKey("new")) {
	    			cursor.close();
	        	}
	        	list.close();
	        	SaveButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {                  	
                    	String worda = WordaEdit.getText().toString();
                    	String wordb = WordbEdit.getText().toString();
                    	String wordc = WordcEdit.getText().toString();
                    	String wordd = WorddEdit.getText().toString();
                    	String worde = WordeEdit.getText().toString();
                    	String wordf = WordfEdit.getText().toString();
                    	String wordg = WordgEdit.getText().toString();
                    	String wordh = WordhEdit.getText().toString();
                    	String wordi = WordiEdit.getText().toString();
                    	String wordj = WordjEdit.getText().toString();                    
                    	if(wordc.equals("")) {
                    		wordc = null;
                    	}
                    	if(wordd.equals("")) {
                    		wordd = null;
                    	}
                    	if(worde.equals("")) {
                    		worde = null;
                    	}
                    	if(wordf.equals("")) {
                    		wordf = null;
                    	}
                    	if(wordg.equals("")) {
                    		wordg = null;
                    	}
                    	if(wordh.equals("")) {
                    		wordh = null;
                    	}
                    	if(wordi.equals("")) {
                    		wordi = null;
                    	}                    	
                    	if(wordj.equals("")) {
                    		wordj = null;
                    	}
                    	if(extras.containsKey("new")) {
                    		String langa = WordaTitle.getText().toString();
                    		String langb = WordbTitle.getText().toString();
                    		String langc = WordcTitle.getText().toString();
                    		String langd = WorddTitle.getText().toString();
                    		String lange = WordeTitle.getText().toString();
                    		String langf = WordfTitle.getText().toString();
                    		String langg = WordgTitle.getText().toString();
                    		String langh = WordhTitle.getText().toString();
                    		String langi = WordiTitle.getText().toString();
                    		String langj = WordjTitle.getText().toString();
                    		if(langa.equals("")) {
                        		langa = "Language 1";
                        	}
                        	if(langb.equals("")) {
                        		langb = "Language 2";
                        	}
                        	if(langc.equals("")) {
                        		langc = null;
                        	}
                        	if(langd.equals("")) {
                        		langd = null;
                        	}
                        	if(lange.equals("")) {
                        		lange = null;
                        	}
                        	if(langf.equals("")) {
                        		langf = null;
                        	}
                        	if(langg.equals("")) {
                        		langg = null;
                        	}
                        	if(langh.equals("")) {
                        		langh = null;
                        	}
                        	if(langi.equals("")) {
                        		langi = null;
                        	}                    	
                        	if(langj.equals("")) {
                        		langj = null;
                        	}
                    		dbHelper.open();
                    		dbHelper.newWord(listid, langa, langb, langc, langd, lange, langf, langg, langh, langi, langj, worda, wordb, wordc, wordd, worde, wordf, wordg, wordh, wordi, wordj);
                    		dbHelper.close();
                    	} else {
                    		dbHelper.open();
                    		dbHelper.updateWord(id, listid, worda, wordb, wordc, wordd, worde, wordf, wordg, wordh, wordi, wordj);
                    		dbHelper.close();
                    	}
                    	Intent prepare = new Intent(v.getContext(), ShowListActivity.class);
                    	Bundle b = new Bundle();
                    	b.putString("id", listid);
        				b.putBoolean("editmode", true);
        				prepare.putExtras(b);
                    	startActivity(prepare);
                    	finish();
                    }
	        	});
	        }
	        dbHelper.close();
	}
	
	@Override
	public void onBackPressed() {
	   if(!extras.containsKey("new")) {
		  Toast.makeText(getApplicationContext(), getResources().getString(R.string.goback), 5000).show();
		  return;
	   } else {
		   Intent prepare = new Intent(getApplicationContext(), WrtsAppActivity.class);
		   Bundle b = new Bundle();
		   startActivity(prepare);
		   finish();
		   return;
	   }
	}
	
	public void onStop() {
		super.onStop();
		FlurryAgent.onEndSession(this);
	}
}
