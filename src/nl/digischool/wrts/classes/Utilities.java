package nl.digischool.wrts.classes;

import android.util.Log;

public class Utilities {

	/**
     * Send debug LogCat message
     * @param message
     */
	public static void log(Object tag, Object message) {
		if(Params.logEnabled) Log.d(tag.toString(), message.toString());
	}

    /**
     * First character to uppercase, other characters lowercase
     * @param str Input string
     * @return String first uppercase
     */
    public static String uppercaseFirst(String str) {
        return Character.toUpperCase(str.charAt(0)) + str.substring(1).toLowerCase();
    }

    /**
     * First character to uppercase, other characters the same
     * @param str Input string
     * @return String first uppercase
     */
    public static String uppercaseOnlyFirst(String str) {
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }

    public static String getLanguageName(Integer i) {
        String alpha = "abcdefghij";
        return "lang-" + alpha.charAt(i);
    }

    public static String getWordName(Integer i) {
        String alpha = "abcdefghij";
        return "word-" + alpha.charAt(i);
    }

    public static String getWordNameByLanguage(String language) {
        Utilities.log("getWordNameByLanguage", language);
        String postfix = language.substring(language.length() - 1);
        return "word-" + postfix;
    }
	
}
