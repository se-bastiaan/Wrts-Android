package nl.digischool.wrts.database;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import nl.digischool.wrts.objects.WordList;
import android.annotation.SuppressLint;
import android.util.Log;

import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Predicate;

public class DbModel {

	public static void saveWordList(ObjectContainer db, WordList list) {
		db.store(list);
		db.commit();
	}
	
	@SuppressLint("DefaultLocale")
	public static List<String> getLanguages(ObjectContainer db) {
		HashSet<String> set = new HashSet<String>();
		 
        WordList type = new WordList(); 
        ObjectSet<WordList> result = db.queryByExample(type);
        int i = 0;
        ArrayList<String> strings;
        WordList list;
        while (result.hasNext()) {
        	list = result.next();
        	strings = new ArrayList<String>();
        	if(list.lang_a != null) strings.add(list.lang_a);
        	if(list.lang_b != null) strings.add(list.lang_b);
        	if(list.lang_c != null) strings.add(list.lang_c);
        	if(list.lang_d != null) strings.add(list.lang_d);
        	if(list.lang_e != null) strings.add(list.lang_e);
        	if(list.lang_f != null) strings.add(list.lang_f);
        	if(list.lang_g != null) strings.add(list.lang_g);
        	if(list.lang_h != null) strings.add(list.lang_h);
        	if(list.lang_i != null) strings.add(list.lang_i);
        	if(list.lang_j != null) strings.add(list.lang_j);
        	Log.d("t", strings.toString());
        	set.addAll(strings);
        }
        
        ArrayList<String> conversion = new ArrayList<String>();
        conversion.addAll(set);
        Collections.sort(conversion);
        
        db.close();
        
        return conversion;
	}
	
	public static ObjectSet<WordList> getWordListsByLanguage(ObjectContainer db, final String language) {
		ObjectSet<WordList> result = db.query(new Predicate<WordList>() {
			private static final long serialVersionUID = 7660551150119674532L;

			public boolean match(WordList obj) {
				String[] langs = new String[]{ obj.lang_a, obj.lang_b, obj.lang_c, obj.lang_d, obj.lang_e, obj.lang_f, obj.lang_g, obj.lang_h, obj.lang_i, obj.lang_j };
				if(Arrays.asList(langs).contains(language)) {
					return true;
				}
				return false;
			}
		});
		
		db.close();
		
		return result;
	}
	
	public static WordList getWordList(ObjectContainer db, Integer id) {
		return DbModel.getWordList(db, Integer.toString(id));
	}
	
	public static WordList getWordList(ObjectContainer db, final String id) {
		ObjectSet<WordList> result = db.query(new Predicate<WordList>() {
			private static final long serialVersionUID = 6875961860776893652L;

			public boolean match(WordList obj) {
				return (obj.id.equals(id));
			}
		});
		
		db.close();
		
		return result.get(0);
	}
	
	public static void deleteWordList(ObjectContainer db, String id) {
		db.delete(DbModel.getWordList(db, id));
		db.close();
	}
}
