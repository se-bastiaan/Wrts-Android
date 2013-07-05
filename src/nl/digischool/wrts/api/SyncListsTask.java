package nl.digischool.wrts.api;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.db4o.ObjectContainer;
import nl.digischool.wrts.R;
import nl.digischool.wrts.classes.Utilities;
import nl.digischool.wrts.database.DbHelper;
import nl.digischool.wrts.database.DbModel;
import nl.digischool.wrts.objects.WordList;

import java.util.List;

public class SyncListsTask extends AsyncTask<Void, Integer, Boolean> {
	
	private String mAuthString, mSinceString = "";
    private ProgressBar mProgressBar;
    private TextView mTextView;
    private Integer mProgress, mMaxProgress;
	private DbHelper mDb;
    private ApiBooleanCallback mCallback;
    private Activity mActivity;
    private final String LOG_TAG = getClass().getSimpleName();
	
	public SyncListsTask(Activity activity, String auth, String since) {
        mActivity = activity;
		mAuthString = auth;
		mSinceString = "?since="+since;
		mDb = new DbHelper(activity);
	}
	
	public SyncListsTask(Activity activity, String auth) {
        mActivity = activity;
		mAuthString = auth;
		mDb = new DbHelper(activity);
	}

    public void setCallBack(ApiBooleanCallback callback) {
        mCallback = callback;
    }

    public void setProgressBar(ProgressBar v) {
        mProgressBar = v;
    }

    public void setTextView(TextView v) {
        mTextView = v;
    }

	@Override
	protected Boolean doInBackground(Void... params) {
		try {
			ApiConnector connector = new ApiConnector("lists/all" + mSinceString, mAuthString);
			String syncXml = connector.execute();
			List<WordList> data = XmlReader.readSyncXml(syncXml);
            mMaxProgress = data.size();
            super.publishProgress(0);
			mDb.openDatabase();
			ObjectContainer cont = mDb.openDbSession();
            DbModel.deleteAllWordLists(cont);
			for(int i = 0; i < data.size(); i++) {
                super.publishProgress(mProgress + 1);
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
    protected void onProgressUpdate(Integer... progress) {
        super.onProgressUpdate(progress);
        mProgress = progress[0];
        if(mProgressBar != null) {
            if(mProgress == 0) {
                mProgressBar.setIndeterminate(false);
                mProgressBar.setMax(mMaxProgress);
            }
            mProgressBar.setProgress(mProgress);
        }
        if(mTextView != null) {
            mTextView.setText(mActivity.getResources().getString(R.string.saved_lists).replace("%s", Integer.toString(mProgress)));
        }
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if(mCallback != null) mCallback.apiResponseCallback("SyncListsTask", aBoolean);
    }
}
