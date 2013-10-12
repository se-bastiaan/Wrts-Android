package nl.digischool.wrts.classes;

import android.content.Context;
import com.readspeaker.androidrsenterpriselibrary.AndroidRSEnterpriseLibrary;
import com.readspeaker.androidrsenterpriselibrary.AndroidRSEnterpriseLibraryCallback;
import eu.se_bastiaan.rslibrary.ExtendedReadSpeakerCallback;
import eu.se_bastiaan.rslibrary.ReadSpeaker;
import eu.se_bastiaan.rslibrary.ReadSpeakerCallback;

import java.util.ArrayList;

/**
 * Sébastiaanmaakt
 * http://sebastiaanmaakt.nl/
 * Date: 11-10-13
 * Time: 21:46
 */
public class ReadSpeakerHelper implements ExtendedReadSpeakerCallback {

    private ReadSpeaker mReadSpeaker;
    private ReadSpeakerCallback mCallback;
    public static final String UNKNOWN = "unknown";
    public Boolean PLAYING;

    public ReadSpeakerHelper(Context context, ReadSpeakerCallback callback) {
        mCallback = callback;
        mReadSpeaker = new ReadSpeaker(context, false, this);
    }

    public void read(String text, String language) {
        if(!language.equals(UNKNOWN))
            mReadSpeaker.read(text, false, false, language);
    }

    public String recognizeLang(String string) {
        string = TextModifier.removeSpaces(TextModifier.normalizeText(string));

        if(string.equalsIgnoreCase("engels") ||
           string.equalsIgnoreCase("english") ||
           string.equalsIgnoreCase("anglais") ||
           string.equalsIgnoreCase("ingles") ||
           string.equalsIgnoreCase("englisch") ||
           string.equalsIgnoreCase("inglese"))
            return ReadSpeaker.ENGLISH_UK;

        if(string.equalsIgnoreCase("nederlands") ||
            string.equalsIgnoreCase("dutch") ||
            string.equalsIgnoreCase("hollandais") ||
            string.equalsIgnoreCase("holandes") ||
            string.equalsIgnoreCase("niederlandisch") ||
            string.equalsIgnoreCase("olandese"))
            return ReadSpeaker.DUTCH;

        if(string.equalsIgnoreCase("frans") ||
            string.equalsIgnoreCase("french") ||
            string.equalsIgnoreCase("francais") ||
            string.equalsIgnoreCase("frances") ||
            string.equalsIgnoreCase("franzosisch") ||
            string.equalsIgnoreCase("francese"))
            return ReadSpeaker.FRENCH;

        if(string.equalsIgnoreCase("duits") ||
            string.equalsIgnoreCase("german") ||
            string.equalsIgnoreCase("allemand") ||
            string.equalsIgnoreCase("aleman") ||
            string.equalsIgnoreCase("deutsch") ||
            string.equalsIgnoreCase("tedesco"))
                return ReadSpeaker.GERMAN;

        if(string.equalsIgnoreCase("spaans") ||
            string.equalsIgnoreCase("spanish") ||
            string.equalsIgnoreCase("espagnol") ||
            string.equalsIgnoreCase("espanol") ||
            string.equalsIgnoreCase("spanisch") ||
            string.equalsIgnoreCase("spagnolo"))
                return ReadSpeaker.SPANISH_ES;

        if(string.equalsIgnoreCase("italiaans") ||
            string.equalsIgnoreCase("italian") ||
            string.equalsIgnoreCase("italien") ||
            string.equalsIgnoreCase("italiano") || //ES + IT
            string.equalsIgnoreCase("italienisch"))
                return ReadSpeaker.ITALIAN;

        if(string.equalsIgnoreCase("fries") ||
            string.equalsIgnoreCase("frisian"))
                return ReadSpeaker.FRISIAN;

        if(string.equalsIgnoreCase("grieks") ||
            string.equalsIgnoreCase("greek") ||
            string.equalsIgnoreCase("grecque") ||
            string.equalsIgnoreCase("griego") ||
            string.equalsIgnoreCase("griechisch") ||
            string.equalsIgnoreCase("greco"))
                return ReadSpeaker.GREEK;

        return ReadSpeakerHelper.UNKNOWN;
    }

    public void setCallback(ReadSpeakerCallback callback) {
        mCallback = callback;
    }

    @Override
    public void error(String s) {
        Utilities.log("RS error", s);
        mCallback.error(s);
    }

    @Override
    public void didFinishReading() {
        PLAYING = false;
        mCallback.didFinishReading();
    }

    @Override
    public void didStartReading() {
        PLAYING = true;
        mCallback.didStartReading();
    }

    @Override
    public void obtainedAudioLocation(String location) {

    }
}
