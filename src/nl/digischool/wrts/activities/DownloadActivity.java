package nl.digischool.wrts.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import nl.digischool.wrts.R;
import nl.digischool.wrts.api.ApiBooleanCallback;
import nl.digischool.wrts.api.SyncListsTask;

/**
 * Sébastiaanmaakt
 * http://sebastiaanmaakt.nl/
 * Date: 3-7-13
 * Time: 22:27
 */
public class DownloadActivity extends BaseActivity implements ApiBooleanCallback {

    private TextView mText;
    private ProgressBar mProgressBar;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstdownload);

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mText = (TextView) findViewById(R.id.savedText);
        executeDownload();
    }

    public void retryDownload(View v) {
        v.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
        mText.setVisibility(View.VISIBLE);
        findViewById(R.id.oneMoment).setVisibility(View.VISIBLE);
    }

    public void executeDownload() {
        SyncListsTask task = new SyncListsTask(this, mApi.getAuthString(), mDaoMaster);
        task.setProgressBar(mProgressBar);
        task.setTextView(mText);
        task.setCallBack(this);
        task.execute();
    }

    @Override
    public void apiResponseCallback(String method, Boolean result) {
        if(result) {
            mText.setText(mRes.getString(R.string.done));
            mSettings.edit().putBoolean("downloaded_lists", true).commit();
            Intent i = new Intent(this, OverviewActivity.class);
            startActivity(i);
            finish();
        } else {
            mProgressBar.setVisibility(View.GONE);
            mText.setText(mRes.getString(R.string.no_server_response));
            findViewById(R.id.retryButton).setVisibility(View.VISIBLE);
            findViewById(R.id.oneMoment).setVisibility(View.GONE);
        }

    }
}