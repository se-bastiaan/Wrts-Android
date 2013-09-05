package nl.digischool.wrts.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import nl.digischool.wrts.R;
import nl.digischool.wrts.api.ApiBooleanCallback;
import nl.digischool.wrts.api.ApiCallback;
import nl.digischool.wrts.api.SyncListsTask;

/**
 * Sébastiaanmaakt
 * http://sebastiaanmaakt.nl/
 * Date: 3-7-13
 * Time: 22:27
 */
public class DownloadActivity extends BaseActivity implements ApiBooleanCallback {

    private TextView mText;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstdownload);

        ProgressBar bar = (ProgressBar) findViewById(R.id.progressbar);
        mText = (TextView) findViewById(R.id.savedtext);
        SyncListsTask task = new SyncListsTask(this, mApi.getAuthString());
        task.setProgressBar(bar);
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
            Toast.makeText(this, mRes.getString(R.string.no_server_response), Toast.LENGTH_SHORT).show();
        }

    }
}