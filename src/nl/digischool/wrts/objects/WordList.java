package nl.digischool.wrts.objects;

import java.util.ArrayList;
import java.util.Map;

public class WordList {
	public Integer result_count;
	public String id, title, created_on, updated_on;
	public Map<String, String> languages;
    public Boolean shared;
	public ArrayList<Map<String, String>> words;
}
