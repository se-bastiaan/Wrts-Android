package nl.sebmaakt.app.wrts_ds;

import nl.sebmaakt.app.wrts_ds.database.DatabaseProvider;
import nl.sebmaakt.app.wrts_ds.database.DbAdapter;
import WrtsMobile.com.R;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;
import com.google.ads.AdView;

public class ListsActivity  extends FragmentActivity implements LoaderManager.LoaderCallbacks<Cursor> {

	private DbAdapter dbHelper;	
	private SharedPreferences settings;
	private Bundle extras;
	SimpleCursorAdapter adapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        FlurryAgent.onStartSession(this, "HELHFVX5C1HA5RQR48JJ");
	        setContentView(R.layout.lists);
	        settings = getSharedPreferences("wrtsapp", 0);
	        if(settings.contains("no-ads")) {
	        	AdView adview = (AdView) findViewById(R.id.adView);
	        	if(settings.getBoolean("no-ads", false)) {
	        		adview.setVisibility(AdView.GONE);
	        	}
	        }
	        ImageButton newb = (ImageButton) findViewById(R.id.NewButton);
	        newb.setVisibility(ImageButton.VISIBLE);
	        
	        dbHelper = new DbAdapter(this);
	        
	        extras = getIntent().getExtras();

	        getSupportLoaderManager().initLoader(1, null, this);
	        
    		String[] from = new String[] { "title", "id" };
    		int[] to = new int[] { R.id.title, R.id.id };
    		adapter = new SimpleCursorAdapter(this,
    				R.layout.lists_listitem, null, from, to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
    		ListView listview = (ListView) findViewById(R.id.ListsView);
    		listview.setAdapter(adapter);
    		
    		listview.setOnItemClickListener(new OnItemClickListener() {
    			public void onItemClick(AdapterView<?> parent, View view,
    					int position, long id) {
    				TextView idtext = (TextView) view.findViewById(R.id.id);
    				Intent intent = new Intent(view.getContext(), ShowListActivity.class);
    				Bundle b = new Bundle();
    				b.putString("id", (String) idtext.getText());
    				intent.putExtras(b);
    				startActivity(intent);
    			}
            });
    		
    		listview.setOnItemLongClickListener(new OnItemLongClickListener() {
	            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
	            	final View itemview = view;            	
	            	AlertDialog.Builder alertDialog = new AlertDialog.Builder(ListsActivity.this);
					alertDialog.create();
					alertDialog.setTitle(getResources().getString(R.string.deletelist));
					alertDialog.setMessage(getResources().getString(R.string.deletelistsure));
					alertDialog.setCancelable(false);
					alertDialog.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
		                public void onClick(DialogInterface dialog, int whichButton) {
			            	TextView idtext = (TextView) itemview.findViewById(R.id.id);
		                	dbHelper.open();
		                	dbHelper.deleteList(idtext.getText().toString());		                	
		                	getSupportLoaderManager().initLoader(1, null, ListsActivity.this);
		            	        
		                	String[] from = new String[] { "title", "id" };
		                	int[] to = new int[] { R.id.title, R.id.id };
		                	adapter = new SimpleCursorAdapter(ListsActivity.this,
		                			R.layout.lists_listitem, null, from, to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		                	ListView listview = (ListView) findViewById(R.id.ListsView);
		                	listview.setAdapter(adapter);
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
    		
    		newb.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					Intent intent = new Intent(v.getContext(), EditListActivity.class);
    				Bundle b = new Bundle();
    				b.putString("type", "title");
    				b.putString("langa", extras.getString("langa"));
    				b.putString("langb", extras.getString("langb"));
    				b.putString("new", "ja");
    				intent.putExtras(b);
    				startActivity(intent);
				}
    			
    		});		
	}
	
	public void onStop() {
		super.onStop();
		FlurryAgent.onEndSession(this);
	}
	
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		CursorLoader cursorLoader = new CursorLoader(this, DatabaseProvider.LISTS_URI, null, "langa = ? and langb = ?", new String[] { extras.getString("langa"), extras.getString("langb") }, null);
	    return cursorLoader;
	}

	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		Log.i("CURSOR", ""+cursor.getCount());
		if(cursor.getCount() > 0) {
			adapter.swapCursor(cursor);
		} else {
			onBackPressed();
		}
	}

	public void onLoaderReset(Loader<Cursor> loader) {
		adapter.swapCursor(null);
	}
}
