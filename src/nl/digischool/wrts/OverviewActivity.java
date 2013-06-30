package nl.digischool.wrts;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.crashlytics.android.Crashlytics;
import com.sherlock.navigationdrawer.compat.SherlockActionBarDrawerToggle;
import nl.digischool.wrts.api.ApiBooleanCallback;
import nl.digischool.wrts.api.SyncListsTask;
import nl.digischool.wrts.fragments.OverviewDrawerFragment;

public class OverviewActivity extends BaseActivity implements ApiBooleanCallback {

    private DrawerLayout mDrawerLayout;
    private FrameLayout mMenu, mContent;
    private SherlockActionBarDrawerToggle mDrawerToggle;
    private Boolean mIsDrawerLayout = false;

    private OverviewDrawerFragment mMenuFragment;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Crashlytics.start(this);
		setContentView(R.layout.activity_overview);

        mSettings.edit().putString("username", "se_bastiaan@outlook.com").putString("password", "beest01").commit();

        mTitle = mDrawerTitle = getTitle();

        if(findViewById(R.id.drawer_layout) != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);

            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

            mIsDrawerLayout = true;
            mDrawerToggle = new SherlockActionBarDrawerToggle(
                this,
                mDrawerLayout,
                R.drawable.ic_drawer,
                R.string.drawer_open,
                R.string.drawer_close
            ) {
                public void onDrawerClosed(View view) {
                    getSupportActionBar().setTitle(mTitle);
                    invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                }

                public void onDrawerOpened(View drawerView) {
                    getSupportActionBar().setTitle(mDrawerTitle);
                    invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                }
            };
            mDrawerLayout.setDrawerListener(mDrawerToggle);

        }

        mMenu = (FrameLayout) findViewById(R.id.menu_frame);
        mContent = (FrameLayout) findViewById(R.id.content_frame);

        mMenuFragment = new OverviewDrawerFragment();

        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.menu_frame, mMenuFragment).commit();

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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.activity_overview_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        if(mIsDrawerLayout) {
            boolean drawerOpen = mDrawerLayout.isDrawerOpen(mMenu);
            //menu.findItem(R.id.syncAction).setVisible(!drawerOpen);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mIsDrawerLayout && mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        // Handle action buttons
        switch(item.getItemId()) {
            case R.id.syncAction:
                SyncListsTask task = new SyncListsTask(this, mApi.getAuthString());
                task.setCallBack(this);
                task.execute();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    public boolean isDrawerLayout() {
        return mIsDrawerLayout;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        if(mIsDrawerLayout) mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        if(mIsDrawerLayout) mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void apiResponseCallback(String method, Boolean result) {
        if(method.equals("SyncListsTask")) {
            if(result) {
                mMenuFragment.refreshList();
            } else {
                Toast.makeText(this, "Er kon geen contact gemaakt worden met de server van Wrts", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
