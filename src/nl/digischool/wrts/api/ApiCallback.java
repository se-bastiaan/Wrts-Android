package nl.digischool.wrts.api;

import org.xml.sax.InputSource;

/**
 * Sébastiaanmaakt
 * http://sebastiaanmaakt.nl/
 * Date: 28-6-13
 * Time: 17:36
 */
public interface ApiCallback {
    public void apiResponseCallback(String method, String result);
}
