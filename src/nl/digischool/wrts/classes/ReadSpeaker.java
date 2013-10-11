package nl.digischool.wrts.classes;

import android.content.Context;
import com.readspeaker.androidrsenterpriselibrary.AndroidRSEnterpriseLibrary;
import com.readspeaker.androidrsenterpriselibrary.AndroidRSEnterpriseLibraryCallback;

import java.util.ArrayList;

/**
 * Sébastiaanmaakt
 * http://sebastiaanmaakt.nl/
 * Date: 11-10-13
 * Time: 21:46
 */
public class ReadSpeaker implements AndroidRSEnterpriseLibraryCallback {

    private AndroidRSEnterpriseLibrary mAudioMobile;
    private AndroidRSEnterpriseLibraryCallback mCallback;
    public static String UNKNOWN = "unknown", ENGLISH = "en_uk", DUTCH = "nl_nl", FRISIAN = "fy_nl", GERMAN = "de_de", GREEK = "el_gr", SPANISH = "es_es", ITALIAN = "it_it", FRENCH = "fr_fr";
    public static Integer AVAILABLE_LANGUAGE_COUNT = 8;
    public Boolean PLAYING;

    public ReadSpeaker(Context context, AndroidRSEnterpriseLibraryCallback callback) {
        mCallback = callback;
        mAudioMobile = new AndroidRSEnterpriseLibrary(context, this);
    }

    public void read(String text, String language) {
        if(!language.equals(UNKNOWN))
        mAudioMobile.readText(text, false, false, Params.READSPEAKER_ID, Params.READSPEAKER_URL, language, null, "AndroidLibrary");
    }

    public String recognizeLang(String string) {
        string = TextModifier.removeSpaces(TextModifier.normalizeText(string));

        if(string.equalsIgnoreCase("engels") ||
           string.equalsIgnoreCase("english") ||
           string.equalsIgnoreCase("anglais") ||
           string.equalsIgnoreCase("ingles") ||
           string.equalsIgnoreCase("englisch") ||
           string.equalsIgnoreCase("inglese"))
            return ReadSpeaker.ENGLISH;

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
                return ReadSpeaker.SPANISH;

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

        return ReadSpeaker.UNKNOWN;
    }

    public void setCallback(AndroidRSEnterpriseLibraryCallback callback) {
        mCallback = callback;
    }

    @Override
    public void error(String s) {
        Utilities.log("RS error", s);
        mCallback.error(s);
    }

    @Override
    public void didFinishPlaying() {
        PLAYING = false;
        mCallback.didFinishPlaying();
    }

    @Override
    public void didStartPlaying() {
        PLAYING = true;
        mCallback.didStartPlaying();
    }
}
