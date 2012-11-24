package nl.sebmaakt.app.wrts_ds;

import nl.sebmaakt.app.wrts_ds.database.DatabaseProvider;
import nl.sebmaakt.app.wrts_ds.database.DbAdapter;
import WrtsMobile.com.R;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.crittercism.app.Crittercism;
import com.flurry.android.FlurryAgent;

public class ShowListActivity extends FragmentActivity implements
LoaderManager.LoaderCallbacks<Cursor> {
	
	public Boolean cursor = false;
	public Cursor list;
	public DbAdapter dbHelper;	
	public Bundle extras;
	public ImageButton testb;
	public ImageButton editb;
	public ImageButton doneb;
	public ImageButton helpb;
	public ImageButton newb;
	public Button titlebutton;
	public TextView titletext;
	public ListView listview;
	public String langa;
	public String langb;
	SimpleCursorAdapter listadapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        FlurryAgent.onStartSession(this, "HELHFVX5C1HA5RQR48JJ");
	        setContentView(R.layout.list);   
	        dbHelper = new DbAdapter(this);
	        extras = getIntent().getExtras();
	        if(extras.containsKey("new")) {
    			AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
				alertDialog
				.setMessage(getResources().getString(R.string.editlisthelp))
				.setCancelable(false)
				.setNeutralButton("Ok!", new DialogInterface.OnClickListener() {
     	           public void onClick(DialogInterface dialog, int id) {
     	        	   dialog.cancel();
     	           }
				});		           
				AlertDialog alert = alertDialog.create();
            	alert.show();
	        }
	};
	
	@Override
	public void onResume() {
	        super.onResume();
	        Log.d("DEBUG", "Resume ShowList");
	        listview = (ListView) findViewById(R.id.ListView);
    		testb = (ImageButton) findViewById(R.id.TestButton);
    		editb = (ImageButton) findViewById(R.id.EditButton);
    		doneb = (ImageButton) findViewById(R.id.DoneButton);
    		helpb = (ImageButton) findViewById(R.id.HelpButton);
    		newb = (ImageButton) findViewById(R.id.NewButton);
    		titlebutton = (Button) findViewById(R.id.titlebutton);    
    		titletext = (TextView) findViewById(R.id.title);
    		extras = getIntent().getExtras();
    		try {
		        list = getContentResolver().query(Uri.parse(DatabaseProvider.LISTDATA_URI.toString() + "/" + extras.getString("id")), null, null, null, null);
		        Log.i("Content", "Cursor count= "+list.getCount());
		        langa = list.getString(list.getColumnIndex("langa"));
		        langb = list.getString(list.getColumnIndex("langb"));
	    		titletext.setText(list.getString(list.getColumnIndex("title")));    		
	    		if(!extras.containsKey("new")) {
	    				getSupportLoaderManager().restartLoader(1, null, this);
	    			
			    		String[] from = new String[] { "worda", "wordb" };
			    		int[] to = new int[] { R.id.worda, R.id.wordb };
			    		listadapter = new SimpleCursorAdapter(ShowListActivity.this,
			    				R.layout.list_listitem, null, from, to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
			    		listview.setAdapter(listadapter);
			    		Log.i("LOG", "Listview filled");
			    		testb.setOnClickListener(new View.OnClickListener() {
			                public void onClick(View v) {
			                	Intent prepare = new Intent(v.getContext(), PrepareTestActivity.class);
			                	Bundle b = new Bundle();
			    				b.putString("id", extras.getString("id"));
			    				prepare.putExtras(b);
			                	startActivity(prepare);
			                	finish();
			                }
			            });
	    		}
    		} catch (Exception e) {
    			e.printStackTrace();
    			Crittercism.logHandledException(e);
    			Toast.makeText(this, "Er is een fout opgetreden.", Toast.LENGTH_SHORT).show();
    		}
    		
    		editb.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                	editfunction();
                }
            });

    		doneb.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                	getSupportLoaderManager().restartLoader(1, null, ShowListActivity.this);
                	list = getContentResolver().query(Uri.parse(DatabaseProvider.LISTDATA_URI.toString() + "/" + extras.getString("id")), null, null, null, null);
                	titletext.setVisibility(TextView.VISIBLE);
            		titlebutton.setVisibility(Button.GONE);
            		titletext.setText(list.getString(list.getColumnIndex("title")));            		
            		testb.setVisibility(ImageButton.VISIBLE);
            		editb.setVisibility(ImageButton.VISIBLE);
            		doneb.setVisibility(ImageButton.GONE);
            		helpb.setVisibility(ImageButton.GONE);
            		newb.setVisibility(ImageButton.GONE);
            		if(!extras.containsKey("new") && cursor) {
            			Log.i("test", "test");            			
            			
    		    		String[] from = new String[] { "worda", "wordb" };
    		    		int[] to = new int[] { R.id.worda, R.id.wordb };
    		    		listadapter = new SimpleCursorAdapter(ShowListActivity.this,
    		    				R.layout.list_listitem, null, from, to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
    		    		listview.setAdapter(listadapter);
            		}
	            		listview.setOnItemClickListener(null);
	            		listview.setOnItemLongClickListener(null);
	            		newb.setOnClickListener(null);
	            	
	            	list.close();
                }
    		});
    		
    		helpb.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {    		
		    		AlertDialog.Builder alertDialog = new AlertDialog.Builder(ShowListActivity.this);
					alertDialog
					.setMessage(getResources().getString(R.string.editlisthelp))
					.setCancelable(false)
					.setNeutralButton("Ok!", new DialogInterface.OnClickListener() {
         	           public void onClick(DialogInterface dialog, int id) {
         	        	   dialog.cancel();
         	           }
					});		           
					AlertDialog alert = alertDialog.create();
	            	alert.show();
                }
    		});

    		list.close();

    		if(extras.containsKey("editmode")) {
    			Log.i("Editmode", "yes");
    			editfunction();
    		} else {
    			doneb.performClick();
    		}
	}
	
	public void editfunction() {
		if(!extras.containsKey("new")) {
			getSupportLoaderManager().restartLoader(1, null, ShowListActivity.this);
        }
    	list = getContentResolver().query(Uri.parse(DatabaseProvider.LISTDATA_URI.toString() + "/" + extras.getString("id")), null, null, null, null);
		titletext.setVisibility(TextView.GONE);
		titlebutton.setVisibility(Button.VISIBLE);
		titlebutton.setText(list.getString(list.getColumnIndex("title")));
		list.close();
		testb.setVisibility(ImageButton.GONE);
		editb.setVisibility(ImageButton.GONE);
		doneb.setVisibility(ImageButton.VISIBLE);
		helpb.setVisibility(ImageButton.VISIBLE);
		newb.setVisibility(ImageButton.VISIBLE);
		titlebutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent prepare = new Intent(v.getContext(), EditListActivity.class);
            	Bundle b = new Bundle();
				b.putString("listid", extras.getString("id"));
				b.putString("type", "title");
				prepare.putExtras(b);
            	startActivity(prepare);
            	finish();
            }
        });
		
			Log.i("edit test", "check");
			String[] from = new String[] { "worda", "wordb", "_id" };
			int[] to = new int[] { R.id.worda, R.id.wordb, R.id.wordid };
    		listadapter = new SimpleCursorAdapter(ShowListActivity.this,
    				R.layout.edit_listitem, null, from, to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
    		listview.setAdapter(listadapter);
			listview.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					TextView _id = (TextView) view.findViewById(R.id.wordid);
					Intent intent = new Intent(view.getContext(), EditListActivity.class);
					Bundle b = new Bundle();
					b.putString("listid", (String) extras.getString("id"));
					b.putString("id", (String) _id.getText());
					b.putString("type", "word");
					intent.putExtras(b);
					startActivity(intent);
					finish();
				}
	        });
			
			listview.setOnItemLongClickListener(new OnItemLongClickListener() {
	            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
	            	final View itemview = view;            	
	            	AlertDialog.Builder alertDialog = new AlertDialog.Builder(ShowListActivity.this);
					alertDialog.create();
					alertDialog.setTitle(getResources().getString(R.string.deleteword));
					alertDialog.setMessage(getResources().getString(R.string.deletesure));
					alertDialog.setCancelable(false);
					alertDialog.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
		                public void onClick(DialogInterface dialog, int whichButton) {
		                	//Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://roosterapp.tk/RoosterApp.apk"));
							//startActivity(browserIntent);	                	
		    				TextView _id = (TextView) itemview.findViewById(R.id.wordid);		    				
		                	dbHelper.open();
		                	dbHelper.deleteWord(extras.getString("id"), _id.getText().toString());
		                	String[] from = new String[] { "worda", "wordb", "_id" };
		        			int[] to = new int[] { R.id.worda, R.id.wordb, R.id.wordid };
		        			getSupportLoaderManager().restartLoader(1, null, ShowListActivity.this);
		            		listadapter = new SimpleCursorAdapter(ShowListActivity.this,
		            				R.layout.edit_listitem, null, from, to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		            		listview.setAdapter(listadapter);
		                	dbHelper.close();
		                }
		            });
					alertDialog.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
		                public void onClick(DialogInterface dialog, int whichButton) {
		                	dialog.cancel();
		                }
		            });
					alertDialog.show();
					return true;
	            }
	        });
		
		newb.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), EditListActivity.class);
				Bundle b = new Bundle();
				b.putString("listid", (String) extras.getString("id"));
				b.putString("type", "word");
				b.putString("new", "ja");
				intent.putExtras(b);
				startActivity(intent);
				finish();
			}
        });
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(getApplicationContext(), ListsActivity.class);
		Bundle b = new Bundle();
		b.putString("langa", langa);
		b.putString("langb", langb);
		intent.putExtras(b);
		startActivity(intent);
		finish();
		return;
	}
	
	public void onStop() {
		super.onStop();
		FlurryAgent.onEndSession(this);
	}
	
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		CursorLoader cursorLoader = new CursorLoader(this, Uri.parse(DatabaseProvider.LISTS_URI.toString() + "/" +extras.getString("id")), null, null, null, null);
	    return cursorLoader;
	}

	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor1) {
		if(cursor1.getCount() == 0) {
			testb.setClickable(false);
			testb.setEnabled(false);
			cursor = false;
		} else {
			cursor = true;
			listadapter.swapCursor(cursor1);
		}		
	}

	public void onLoaderReset(Loader<Cursor> loader) {
		cursor = false;
		listadapter.swapCursor(null);
	}
}
