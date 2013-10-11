package nl.digischool.wrts.activities;

import android.os.Bundle;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.readspeaker.androidrsenterpriselibrary.AndroidRSEnterpriseLibraryCallback;
import nl.digischool.wrts.classes.ReadSpeaker;

/**
 * Sébastiaanmaakt
 * http://sebastiaanmaakt.nl/
 * Date: 11-10-13
 * Time: 15:46
 */
public class TestBaseActivity extends BaseActivity implements AndroidRSEnterpriseLibraryCallback {

    protected ReadSpeaker speaker;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        speaker = new ReadSpeaker(this, this);
    }

    @Override
    public void error(String s) {
    }

    @Override
    public void didFinishPlaying() {
    }

    @Override
    public void didStartPlaying() {
    }
}
