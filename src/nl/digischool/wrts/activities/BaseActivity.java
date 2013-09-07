package nl.digischool.wrts.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import nl.digischool.wrts.WrtsApplication;
import nl.digischool.wrts.api.ApiHelper;
import nl.digischool.wrts.classes.Params;
import nl.digischool.wrts.dao.DaoMaster;
import nl.digischool.wrts.dao.DaoSession;
import nl.digischool.wrts.dao.WordDao;
import nl.digischool.wrts.dao.WordListDao;

public class BaseActivity extends SherlockFragmentActivity {

    protected SharedPreferences mSettings;
    protected ApiHelper mApi;
    protected Resources mRes;
    protected SQLiteDatabase mDb;
    protected DaoMaster mDaoMaster;
    protected DaoSession mDaoSession;
    protected WordListDao mWordListDao;
    protected WordDao mWordDao;

    @Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

        mSettings = getSharedPreferences(Params.preferencesName, Context.MODE_PRIVATE);
        mApi = new ApiHelper(this);
        mRes = getResources();

        WrtsApplication app = (WrtsApplication) getApplication();
        mDb = app.getDatabase();
        mDaoMaster = app.getDaoMaster();

	}
	
}
