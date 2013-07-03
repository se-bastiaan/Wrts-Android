package nl.digischool.wrts.database;

import java.util.*;

import nl.digischool.wrts.classes.Utilities;
import nl.digischool.wrts.objects.WordList;
import android.annotation.SuppressLint;

import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Predicate;

/**
 * DbModel can be used to access items in the database in a fast way
 */
public class DbModel {

    /**
     * Save a WordList to the database
     * @param db ObjectContainer
     * @param list WordList
     */
	public static void saveWordList(ObjectContainer db, WordList list) {
		db.store(list);
		db.commit();
	}

    /**
     * Returns list of all languages in the database
     * @param db ObjectContainer
     * @return List<String> containing all languages
     */
	@SuppressLint("DefaultLocale")
	public static List<Map<String, Object>> getLanguages(ObjectContainer db) {
        ArrayList<String> dataList = new ArrayList<String>();
		HashSet<String> stringSet = new HashSet<String>();
		 
        WordList type = new WordList(); 
        ObjectSet<WordList> result = db.queryByExample(type);
        HashSet<String> strings;
        WordList list;
        while (result.hasNext()) {
        	list = result.next();
        	strings = new HashSet<String>();
        	if(list.lang_a != null) strings.add(list.lang_a.toLowerCase());
        	if(list.lang_b != null) strings.add(list.lang_b.toLowerCase());
        	if(list.lang_c != null) strings.add(list.lang_c.toLowerCase());
        	if(list.lang_d != null) strings.add(list.lang_d.toLowerCase());
        	if(list.lang_e != null) strings.add(list.lang_e.toLowerCase());
        	if(list.lang_f != null) strings.add(list.lang_f.toLowerCase());
        	if(list.lang_g != null) strings.add(list.lang_g.toLowerCase());
        	if(list.lang_h != null) strings.add(list.lang_h.toLowerCase());
        	if(list.lang_i != null) strings.add(list.lang_i.toLowerCase());
        	if(list.lang_j != null) strings.add(list.lang_j.toLowerCase());
            dataList.addAll(strings);
            stringSet.addAll(strings);
        }

        ArrayList<String> stringList = new ArrayList<String>();
        stringList.addAll(stringSet);
        stringSet.clear();
        ArrayList<Map<String, Object>> conversion = new ArrayList<Map<String, Object>>();

        for(int i = 0; i < stringList.size(); i++) {
            int count = Collections.frequency(dataList, stringList.get(i));
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("string", stringList.get(i));
            map.put("count", count);
            conversion.add(map);
        }

        Utilities.log("DbModel", dataList.toString());

        stringList.clear();

        Collections.sort(conversion, new Comparator<Map<String, Object>>() {
            public int compare(Map<String, Object> a, Map<String, Object> b) {
                Integer countA = (Integer) a.get("count");
                Integer countB = (Integer) b.get("count");
                String strA = (String) a.get("string");
                String strB = (String) b.get("string");
                if(countA > countB) {
                    return -1;
                }
                if(countA < countB) {
                    return 1;
                }
                return strA.compareToIgnoreCase(strB);
            }
        });

        return conversion;
	}

    /**
     * Returns all WordList objects that has the language in one of the fields a-j
     * @param db ObjectContainer
     * @param language String
     * @return ObjectSet
     */
	public static ObjectSet<WordList> getWordListsByLanguage(ObjectContainer db, final String language) {

		ObjectSet<WordList> result = db.query(new Predicate<WordList>() {
			public boolean match(WordList obj) {
                if(language == null) {
                    return true;
                }
				if( obj.lang_a.equalsIgnoreCase(language) ||
                    obj.lang_b.equalsIgnoreCase(language) ||
                    obj.lang_c.equalsIgnoreCase(language) ||
                    obj.lang_d.equalsIgnoreCase(language) ||
                    obj.lang_e.equalsIgnoreCase(language) ||
                    obj.lang_f.equalsIgnoreCase(language) ||
                    obj.lang_g.equalsIgnoreCase(language) ||
                    obj.lang_h.equalsIgnoreCase(language) ||
                    obj.lang_i.equalsIgnoreCase(language) ||
                    obj.lang_j.equalsIgnoreCase(language)) {
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

    public static void deleteAllWordLists(ObjectContainer db) {
        WordList n = new WordList();
        ObjectSet result= db.queryByExample(n);
        if(result.hasNext()) {
            WordList d = (WordList) result.next();
            db.delete(d);
        }
    }
}
