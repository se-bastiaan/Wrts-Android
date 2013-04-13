package nl.digischool.wrts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import nl.digischool.wrts.adapters.SlidingMenuListAdapter;
import nl.digischool.wrts.api.ApiHelper;
import nl.digischool.wrts.api.XmlBuilder;
import nl.digischool.wrts.classes.Utilities;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.view.MenuItem;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingActivity;

public class OverviewActivity extends SlidingActivity {
	
	private SlidingMenu slidingMenu;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(R.style.Theme_Wrts_SlidingMenu);
		setContentView(R.layout.activity_overview);
		setBehindContentView(R.layout.activity_overview_slidingmenu);
		
		/* Generate testdata */
		ArrayList<Map<String, Object>> object = new ArrayList<Map<String, Object>>();
		for(int i = 0; i < 26; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			if(i == 0 || i == 5 || i == 9 || i == 15 || i == 20) {
				map.put("header", true);
			}
			map.put("text", "Item "+i);
			object.add(map);
		}
		
		SlidingMenuListAdapter adapter = new SlidingMenuListAdapter(this, object);
		ListView sm_list = (ListView) findViewById(R.id.slidingmenu_list);
		sm_list.setAdapter(adapter);
		
		slidingMenu = getSlidingMenu();
		slidingMenu.setFadeEnabled(true);
		slidingMenu.setFadeDegree(0.20f);
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		slidingMenu.setShadowDrawable(R.drawable.wrts_slidingmenu_shadow);
		slidingMenu.setShadowWidthRes(R.dimen.shadow_width_slidingmenu);
		slidingMenu.setBehindOffsetRes(R.dimen.wrts_slidingmenu_offset);
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
		ApiHelper api = new ApiHelper(this);
		api.saveUserData("se_bastiaan@outlook.com", "beest01");
		Utilities.log("API result", api.getDetailIndex());
	}

}
