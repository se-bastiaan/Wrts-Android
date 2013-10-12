package nl.digischool.wrts.activities;

import android.os.Bundle;
import android.view.View;
import eu.se_bastiaan.rslibrary.ReadSpeaker;
import eu.se_bastiaan.rslibrary.ReadSpeakerCallback;
import nl.digischool.wrts.R;
import nl.digischool.wrts.classes.Utilities;

/**
 * Sébastiaanmaakt
 * http://sebastiaanmaakt.nl/
 * Date: 23-9-13
 * Time: 0:32
 */
public class TestPrepareActivity extends TestBaseActivity implements ReadSpeakerCallback {

    private Integer lang;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testprepare);
        speaker.setCallback(this);
        lang = 0;
    }

    public void button1(View v) {
        speaker.read("I am happy", ReadSpeaker.ENGLISH_UK);
    }

    @Override
    public void error(String s) {}

    @Override
    public void didFinishReading() {
        lang++;
        if(lang < 8) {
            switch(lang) {
                case 1:
                    speaker.read("Ik ben blij", ReadSpeaker.DUTCH);
                    break;
                case 2:
                    speaker.read("Je suis content", ReadSpeaker.FRENCH);
                    break;
                case 3:
                    speaker.read("Ich bin froh", ReadSpeaker.GERMAN);
                    break;
                case 4:
                    speaker.read("ik bin bliid", ReadSpeaker.FRISIAN);
                    break;
                case 5:
                    speaker.read("Estoy feliz", ReadSpeaker.SPANISH_ES);
                    break;
                case 6:
                    speaker.read("Sono felice", ReadSpeaker.ITALIAN);
                    break;
                case 7:
                    speaker.read("Είμαι χαρούμενος", ReadSpeaker.GREEK);
                    break;
            }
        } else {
            lang = 0;
        }
    }

    @Override
    public void didStartReading() {

    }
}
