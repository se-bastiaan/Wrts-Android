package nl.digischool.wrts.api;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.db4o.ObjectContainer;
import nl.digischool.wrts.R;
import nl.digischool.wrts.classes.Utilities;
import nl.digischool.wrts.dao.DaoMaster;
import nl.digischool.wrts.dao.DaoSession;
import nl.digischool.wrts.dao.Word;
import nl.digischool.wrts.dao.WordList;

import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class SyncListsTask extends AsyncTask<Void, Integer, Boolean> implements ProgressUpdateCallback {

    private DaoMaster mDaoMaster;
    private String mAuthString, mSinceString = "";
    private ProgressBar mProgressBar;
    private TextView mTextView;
    private Integer mProgress = 0, mMaxProgress = 0;
    private ApiBooleanCallback mCallback;
    private Activity mActivity;
    private final String LOG_TAG = getClass().getSimpleName();
	
	public SyncListsTask(Activity activity, String auth, String since, DaoMaster daoMaster) {

        mActivity = activity;
		mAuthString = auth;
		mSinceString = "?since="+since;
		mDaoMaster = daoMaster;

	}
	
	public SyncListsTask(Activity activity, String auth, DaoMaster daoMaster) {

        mActivity = activity;
		mAuthString = auth;
		mDaoMaster = daoMaster;

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
            int lastIndex = 0;
            mMaxProgress = 0;
            String findStr = "<id>";
            while(lastIndex != -1){
                lastIndex = syncXml.indexOf(findStr, lastIndex);
                if(lastIndex != -1){
                    mMaxProgress++;
                    lastIndex += findStr.length();
                }
            }
            StringReader reader = new StringReader(syncXml);
            super.publishProgress(0);
			SyncXmlHandler handler = XmlReader.readSyncXml(reader, this);
            ArrayList<WordList> lists = handler.getLists();
            ArrayList<Word> words = handler.getWords();
            DaoSession session = mDaoMaster.newSession();
            session.getWordListDao().insertInTx(lists);
            session.getWordDao().insertInTx(words);
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
            Utilities.log(LOG_TAG, "Saving list: " + mProgress);
            mProgressBar.setProgress(mProgress);
        }

        if(mTextView != null) {
            mTextView.setText(mActivity.getResources().getQuantityString(R.plurals.saved_lists, mProgress, mProgress));
        }

    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if(mCallback != null) mCallback.apiResponseCallback("SyncListsTask", aBoolean);

    }

    @Override
    public void updateProgress(int progress) {
        super.publishProgress(progress);
    }
}
