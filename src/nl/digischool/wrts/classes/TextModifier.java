package nl.digischool.wrts.classes;

import java.text.Normalizer;
import java.text.Normalizer.Form;

/**
 * Sébastiaanmaakt
 * http://sebastiaanmaakt.nl/
 * Date: 11-10-13
 * Time: 22:44
 */
public class TextModifier {

    /**
     * Remove all diacritical marks
     * @param text Text
     * @return Cleaned text
     */
    public static String normalizeText(String text) {
        return Normalizer.normalize(text, Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }

    /**
     * Remove all spaces
     * @param text Text
     * @return Cleaned text
     */
    public static String removeSpaces(String text) {
        return text.replace(" ", "");
    }

    /**
     * Remove all punctuation
     * @param text Text
     * @return Cleaned text
     */
    public static String removePunctuation(String text) {
        return text.replaceAll("\\p{Punct}+", "");
    }

}
