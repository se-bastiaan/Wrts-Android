package nl.sebmaakt.app.wrts_ds;

import WrtsMobile.com.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

public class ReplacementActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.replacement);
        VideoView view = (VideoView) findViewById(R.id.videoView1);
        Log.d("WrtsApp", getPackageName());
        String uri = "android.resource://" + getPackageName() + "/" + R.raw.comp2;
        view.setVideoPath(uri);        
        view.start();
        Button button = (Button) findViewById(R.id.button1);
        button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if(isOnline()) {
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setData(Uri.parse("market://details?id=WrtsMobile.com"));
					startActivity(intent);
					Uri packageURI = Uri.parse("package:nl.sebmaakt.app.wrts_ds"); 
					Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI); 
					startActivity(uninstallIntent);
				} else {
					Toast.makeText(getApplicationContext(), "Er is geen internetverbinding!", Toast.LENGTH_LONG).show();
				}
			}        	
        });
    }
    
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }
}