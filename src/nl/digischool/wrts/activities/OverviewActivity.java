package nl.digischool.wrts.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
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
import nl.digischool.wrts.R;
import nl.digischool.wrts.api.ApiBooleanCallback;
import nl.digischool.wrts.api.SyncListsTask;
import nl.digischool.wrts.fragments.OverviewDrawerFragment;
import nl.digischool.wrts.fragments.OverviewListFragment;

public class OverviewActivity extends BaseActivity implements ApiBooleanCallback {

    private DrawerLayout mDrawerLayout;
    private FrameLayout mMenu, mContent;
    private SherlockActionBarDrawerToggle mDrawerToggle;
    private Boolean mIsDrawerLayout = false;

    private FragmentManager mFragmentManager;
    private OverviewDrawerFragment mMenuFragment;
    private OverviewListFragment mContentFragment;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Crashlytics.start(this);
		setContentView(R.layout.activity_overview);

        if(!mSettings.contains("username") || !mSettings.contains("password") || !mSettings.contains("downloaded_lists")) {
            Intent i = new Intent(this, FirstStartActivity.class);
            startActivity(i);
            finish();
        }

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

        mFragmentManager = getSupportFragmentManager();

        mMenuFragment = new OverviewDrawerFragment();
        mFragmentManager.beginTransaction().replace(R.id.menu_frame, mMenuFragment).commit();

        mContentFragment = new OverviewListFragment();
        mFragmentManager.beginTransaction().replace(R.id.content_frame, mContentFragment).commit();
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

    /**
     * Set language of the lists that are being shown. Uses little UI hack to create smooth transition.
     * Passes the variable to the mContentFragment.
     * @param language String
     */
    public void setOverviewLanguage(final String language) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mContentFragment.setLanguage(language);
            }
            @Override
            protected Void doInBackground(Void... params) {
                return null;
            }
            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);
                mDrawerLayout.closeDrawer(GravityCompat.START);
            }
        }.execute();
    }

    public void closeDrawer() {
        if(mIsDrawerLayout) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
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