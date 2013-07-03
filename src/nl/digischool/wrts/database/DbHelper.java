package nl.digischool.wrts.database;

import nl.digischool.wrts.classes.Params;
import android.content.Context;
import android.util.Log;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import nl.digischool.wrts.classes.Utilities;

public class DbHelper {

	private ObjectContainer mDatabase;
	private static final int DATABASE_MODE = 0;
	private Context mContext;

	public DbHelper(Context context) {
		mContext = context;
		mDatabase = null;
	}

	public void openDatabase() {
		try {
			if (mDatabase == null) {
			    mDatabase = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), getDbPath());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void closeDatabase() {
		if (mDatabase != null) {
			mDatabase.close();
		}
	}

	private String getDbPath() {
		return mContext.getDir("data", DATABASE_MODE).getPath() + "/" + Params.databaseName;
	}

	public ObjectContainer openDbSession() {
		try {
			return mDatabase.ext().openSession();
		} catch (Exception e) {
			Utilities.log("DbHelper", "Check if database is opened");
			e.printStackTrace();
		}
		return null;
	}

}