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
	
}
