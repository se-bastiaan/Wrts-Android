package nl.digischool.wrts;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import nl.digischool.wrts.api.ApiHelper;
import nl.digischool.wrts.classes.Params;
import nl.digischool.wrts.database.DbHelper;

public class BaseActivity extends SherlockFragmentActivity {

    protected SharedPreferences mSettings;
    protected ApiHelper mApi;
    protected DbHelper mDb;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        mSettings = getSharedPreferences(Params.preferencesName, Context.MODE_PRIVATE);
        mApi = new ApiHelper(this);
        mDb = new DbHelper(this);
	}
	
}
