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
     * First character to uppercase
     * @param str Input string
     * @return String first uppercase
     */
    public static String uppercaseFirst(String str) {
        return Character.toUpperCase(str.charAt(0)) + str.substring(1).toLowerCase();
    }
	
}
