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
     * Save a WordList to the database
     * @param db ObjectContainer
     * @param list WordList
     * @param commit Do commit?
     */
    public static void saveWordList(ObjectContainer db, WordList list, Boolean commit) {
        db.store(list);
        if(commit) db.commit();
    }

    /**
     * Returns list of all languages in the database
     * @param db ObjectContainer
     * @return List<String> containing all languages
     */
	@SuppressLint("DefaultLocale")
	public static List<Map<String, Object>> getLanguages(ObjectContainer db) {
        ArrayList<String> dataList = new ArrayList<String>();
        TreeSet<String> stringSet = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);

        WordList type = new WordList();
        ObjectSet<WordList> result = db.queryByExample(type);
        TreeSet<String> strings;
        WordList list;
        while (result.hasNext()) {
        	list = result.next();
        	strings = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
            Utilities.log("DbModel", list.languages.toString());

            for(int i = 0; i < 10; i++) {
                String languageName = Utilities.getLanguageName(i);
                Utilities.log("DbModel", languageName);
                if(list.languages.containsKey(languageName) && !list.languages.get(languageName).isEmpty())
                    strings.add(list.languages.get(languageName));
            }
            dataList.addAll(strings);
            stringSet.addAll(strings);
        }

        ArrayList<String> stringList = new ArrayList<String>();
        stringList.addAll(stringSet);
        stringSet.clear();
        ArrayList<Map<String, Object>> conversion = new ArrayList<Map<String, Object>>();

        for(int i = 0; i < stringList.size(); i++) {
            int count = 0;
            for(String item : dataList) {
                count += (item.equalsIgnoreCase(stringList.get(i)) ? 1 : 0);
            }
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

                for(int i = 0; i < 10; i++) {
                    String languageName = Utilities.getLanguageName(i);
                    if(obj.languages.containsValue(language))
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

    public static void deleteAllWordLists(ObjectContainer c) {
        WordList w = new WordList();
        ObjectSet<WordList> lists = c.queryByExample(w);
        for(WordList list: lists) {
            c.delete(list);
        }
        c.close();
    }
}
