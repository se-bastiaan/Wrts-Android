package nl.digischool.wrts.database;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import nl.digischool.wrts.objects.WordList;

import android.util.Log;

import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Predicate;

public class DbModel {

	public static void saveWordList(ObjectContainer db, WordList list) {
		db.store(list);
		db.commit();
	}
	
	public static List<String> getLanguages(ObjectContainer db) {
		HashSet<String> set = new HashSet<String>();
		 
        WordList type = new WordList(); 
        ObjectSet<WordList> result = db.queryByExample(type);
 
        while (result.hasNext()) {
        	WordList d = result.next();
        	Log.d("loj", "log");
        	String[] strings = new String[] { d.lang_a, d.lang_b, d.lang_c, d.lang_d, d.lang_e, d.lang_f, d.lang_g, d.lang_h, d.lang_i, d.lang_j };
        	set.addAll(Arrays.asList(strings));
        }
        
        ArrayList<String> list = new ArrayList<String>();
        list.addAll(set);
        Collections.sort(list);
        
        return list;
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
		return result.get(0);
	}
	
	public static void deleteWordList(ObjectContainer db, String id) {
		db.delete(DbModel.getWordList(db, id));
	}
}
