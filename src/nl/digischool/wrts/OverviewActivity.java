package nl.digischool.wrts;

import com.crashlytics.android.Crashlytics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.digischool.wrts.adapters.SlidingMenuListAdapter;
import nl.digischool.wrts.api.ApiHelper;
import nl.digischool.wrts.api.SyncListsTask;
import nl.digischool.wrts.database.DbHelper;
import nl.digischool.wrts.database.DbModel;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.actionbarsherlock.view.MenuItem;
import com.db4o.ObjectContainer;

public class OverviewActivity extends BaseActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Crashlytics.start(this);
		setContentView(R.layout.activity_overview);
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);


		/* Generate data *//*
		ArrayList<Map<String, Object>> object = new ArrayList<Map<String, Object>>();
		db.openDatabase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("header", true);
		map.put("text", "Talen");
		object.add(map);
		ObjectContainer cont = db.openDbSession();
		List<String> langs = DbModel.getLanguages(cont);
		for(int i = 0; i < langs.size(); i++) {
			map = new HashMap<String, Object>();
			map.put("text", langs.get(i));
			object.add(map);
		}
		cont.close();
		db.closeDatabase();
		
		SlidingMenuListAdapter adapter = new SlidingMenuListAdapter(this, object);
		ListView sm_list = (ListView) findViewById(R.id.slidingmenu_list);
		sm_list.setAdapter(adapter);*/
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:

			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void settingsAction(View v) {
		try {
			new SyncListsTask(this, api.getAuthString()).execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
