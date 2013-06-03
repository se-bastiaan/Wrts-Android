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
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingActivity;

public class OverviewActivity extends SlidingActivity {
	
	private SlidingMenu slidingMenu;
	private ApiHelper api;
	private DbHelper db;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Crashlytics.start(this);
		setTheme(R.style.Theme_Wrts_SlidingMenu);
		setContentView(R.layout.activity_overview);
		setBehindContentView(R.layout.activity_overview_slidingmenu);
		
		slidingMenu = getSlidingMenu();
		slidingMenu.setFadeEnabled(true);
		slidingMenu.setFadeDegree(0.20f);
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		slidingMenu.setShadowDrawable(R.drawable.wrts_slidingmenu_shadow);
		slidingMenu.setShadowWidthRes(R.dimen.shadow_width_slidingmenu);
		slidingMenu.setBehindOffsetRes(R.dimen.wrts_slidingmenu_offset);
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		api = new ApiHelper(this);
		db = new DbHelper(this);

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
			toggle();
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
