package nl.digischool.wrts;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import nl.digischool.wrts.api.ApiHelper;
import nl.digischool.wrts.classes.Params;
import nl.digischool.wrts.database.DbHelper;

public class BaseActivity extends SherlockFragmentActivity {

    protected SharedPreferences settings;
    protected ApiHelper api;
    protected DbHelper db;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        settings = getSharedPreferences(Params.preferencesName, Context.MODE_PRIVATE);
        api = new ApiHelper(this);
        db = new DbHelper(this);
	}
	
}
