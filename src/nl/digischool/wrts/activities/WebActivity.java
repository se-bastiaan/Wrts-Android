package nl.digischool.wrts.activities;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import nl.digischool.wrts.R;

/**
 * Sébastiaanmaakt
 * http://sebastiaanmaakt.nl/
 * Date: 3-7-13
 * Time: 20:28
 */
public class WebActivity extends SherlockActivity {

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent i = getIntent();
        if(i.hasExtra("url") && i.hasExtra("title")) {
            WebView view = new WebView(this);
            WebSettings settings = view.getSettings();
            settings.setBuiltInZoomControls(true);
            settings.setJavaScriptEnabled(true);
            setContentView(view);
            view.loadUrl(i.getStringExtra("url"));
            getSupportActionBar().setTitle(i.getStringExtra("title"));
        } else {
            Toast.makeText(this, getResources().getString(R.string.cantfindurl), Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

}
