package nl.digischool.wrts.activities;

import android.os.Bundle;
import eu.se_bastiaan.rslibrary.ReadSpeakerCallback;
import nl.digischool.wrts.classes.ReadSpeakerHelper;

/**
 * Sébastiaanmaakt
 * http://sebastiaanmaakt.nl/
 * Date: 11-10-13
 * Time: 15:46
 */
public abstract class TestBaseActivity extends BaseActivity implements ReadSpeakerCallback {

    protected ReadSpeakerHelper speaker;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        speaker = new ReadSpeakerHelper(this, this);
    }

    @Override
    public void error(String s) {
    }

    @Override
    public void didFinishReading() {
    }

    @Override
    public void didStartReading() {
    }
}
