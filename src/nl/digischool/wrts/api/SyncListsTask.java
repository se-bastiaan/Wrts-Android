package nl.digischool.wrts.api;

import android.content.Context;
import android.os.AsyncTask;
import com.db4o.ObjectContainer;
import nl.digischool.wrts.classes.Utilities;
import nl.digischool.wrts.database.DbHelper;
import nl.digischool.wrts.database.DbModel;
import nl.digischool.wrts.objects.WordList;

import java.util.List;

public class SyncListsTask extends AsyncTask<Void, Void, Boolean> {
	
	private String mAuthString, mSinceString = "";
	private DbHelper mDb;
    private ApiBooleanCallback mCallback;
    private final String LOG_TAG = getClass().getSimpleName();
	
	public SyncListsTask(Context context, String auth, String since) {
		mAuthString = auth;
		mSinceString = "?since="+since;
		mDb = new DbHelper(context);
	}
	
	public SyncListsTask(Context context, String auth) {
		mAuthString = auth;
		mDb = new DbHelper(context);
	}

    public void setCallBack(ApiBooleanCallback callback) {
        mCallback = callback;
    }

	@Override
	protected Boolean doInBackground(Void... params) {
		try {
			ApiConnector connector = new ApiConnector("lists/all" + mSinceString, mAuthString);
			String syncXml = connector.execute();
			List<WordList> data = XmlReader.readSyncXml(syncXml);
			mDb.openDatabase();
			ObjectContainer cont = mDb.openDbSession();
			for(int i = 0; i < data.size(); i++) {
                Utilities.log(LOG_TAG, data.get(i));
				WordList list = data.get(i);			
				DbModel.saveWordList(cont, list);
			}
			cont.close();
			mDb.closeDatabase();
            return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        mCallback.apiResponseCallback("SyncListsTask", aBoolean);
    }
}
