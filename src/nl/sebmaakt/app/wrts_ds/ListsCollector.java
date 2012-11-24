package nl.sebmaakt.app.wrts_ds;

import java.util.ArrayList;
import java.util.HashMap;

import WrtsMobile.com.R;
import android.content.ContentValues;

public class ListsCollector {

		 /** Variables */
		 private ArrayList<String> id = new ArrayList<String>();
		 private Boolean idcollector = false;
		 private String str_id, str_updatedon, title, langa,langb,langc,langd,lange,langf,langg,langh,langi,langj,worda,wordb,wordc,wordd,worde,wordf,wordg,wordh,wordi,wordj;
		 private ArrayList<HashMap<String, String>> langs = new ArrayList<HashMap<String, String>>();
         private ArrayList<ContentValues> wordData = new ArrayList<ContentValues>();
         private ArrayList<ContentValues> listData = new ArrayList<ContentValues>();
         private int wordscount;

         public ListsCollector(Boolean sort) {
        	 super();
        	 idcollector = sort;
         }
         
         public ListsCollector() {
        	 super();
         }
         
		 /** In Setter method default it will return arraylist
		 * change that to add */

		 public ArrayList<String> getId() {
			 return id;
		 }

		 public void setId(String id) {
			 //Log.d("ListCollector", "ID = "+id);
			 if(idcollector) {
				 this.id.add(id);
			 }
			 this.str_id = id;
		 }

		 public void setLangA(String langa) {
			 //Log.w("ListCollector", "LANG-A = "+langa);
			 this.langa = langa;
		 }


		 public void setLangB(String langb) {
			 //Log.w("ListCollector", "LANG-B = "+langb);
			 this.langb = langb;
		 }


		 public void setLangC(String langc) {
			 //Log.w("ListCollector", "LANG-A = "+langa);
			 this.langc = langc;
		 }


		 public void setLangD(String langd) {
			 //Log.w("ListCollector", "LANG-B = "+langb);
			 this.langd = langd;
		 }


		 public void setLangE(String lange) {
			 //Log.w("ListCollector", "LANG-A = "+langa);
			 this.lange = lange;
		 }


		 public void setLangF(String langf) {
			 //Log.w("ListCollector", "LANG-B = "+langb);
			 this.langf = langf;
		 }

		 public void setLangG(String langg) {
			 //Log.w("ListCollector", "LANG-A = "+langa);
			 this.langg = langg;
		 }


		 public void setLangH(String langh) {
			 //Log.w("ListCollector", "LANG-B = "+langb);
			 this.langh = langh;
		 }


		 public void setLangI(String langi) {
			 //Log.w("ListCollector", "LANG-A = "+langa);
			 this.langi = langi;
		 }

		 public void setLangJ(String langj) {
			 //Log.w("ListCollector", "LANG-B = "+langb);
			 this.langj = langj;
		 }

		 public void setTitle(String title) {
			 //Log.w("ListCollector", "TITLE = "+title);
			 this.title = title;
		 }

		 public void setUpdated(String updated) {
			 //Log.d("ListCollector", "UPDATED-ON = "+updated);
			this.str_updatedon = updated;
		 }

	     public void setWordA(String word) {
	             //Log.d("ListCollector", "WORD-COUNT = "+count);
	             this.worda = word;
	     }

	
	     public void setWordB(String word) {
	             //Log.d("ListCollector", "WORD-COUNT = "+count);
	             this.wordb = word;
	     }

	
	     public void setWordC(String word) {
	             //Log.d("ListCollector", "WORD-COUNT = "+count);
	             this.wordc = word;
	     }

	
	     public void setWordD(String word) {
	             //Log.d("ListCollector", "WORD-COUNT = "+count);
	             this.wordd = word;
	     }

	
	     public void setWordE(String word) {
	             //Log.d("ListCollector", "WORD-COUNT = "+count);
	             this.worde = word;
	     }

	
	     public void setWordF(String word) {
	             //Log.d("ListCollector", "WORD-COUNT = "+count);
	             this.wordf = word;
	     }

	
	     public void setWordG(String word) {
	             //Log.d("ListCollector", "WORD-COUNT = "+count);
	             this.wordg = word;
	     }

	     public void setWordH(String word) {
	             //Log.d("ListCollector", "WORD-COUNT = "+count);

	             this.wordh = word;
	     }
	
	     public void setWordI(String word) {
	             //Log.d("ListCollector", "WORD-COUNT = "+count);
	             this.wordi = word;
	     }
	
	     public void setWordJ(String word) {
	             this.wordj = word;
	     }
		 
		 public ArrayList<HashMap<String, String>> getLangs() {
			 return langs;
		 }
		 
		 public void setList() {
			 //Log.d("ListCollector", "Set words");
			ContentValues listValues = new ContentValues();	    							
			listValues.put("id", str_id);
			listValues.put("title", title);
			listValues.put("langa", asUpperCaseFirstChar(langa));
			listValues.put("langb", asUpperCaseFirstChar(langb));
			listValues.put("langc", asUpperCaseFirstChar(langc));
			listValues.put("langd", asUpperCaseFirstChar(langd));
			listValues.put("lange", asUpperCaseFirstChar(lange));
			listValues.put("langf", asUpperCaseFirstChar(langf));
			listValues.put("langg", asUpperCaseFirstChar(langg));
			listValues.put("langh", asUpperCaseFirstChar(langh));
			listValues.put("langi", asUpperCaseFirstChar(langi));
			listValues.put("langj", asUpperCaseFirstChar(langj));
			listValues.put("updatedon", str_updatedon);
			listValues.put("wordscount", wordscount);
			wordscount = 0;
			listData.add(listValues);
		 }
		 
		 public void setWord() {
			wordscount++;
			//Log.d("Collector", worda);
			//Log.d("Collector", wordb);
			ContentValues wordValues = new ContentValues();	    							
			wordValues.put("id", str_id);
			wordValues.put("updatedon", str_updatedon);
			wordValues.put("worda", worda);
			wordValues.put("wordb", wordb);
			wordValues.put("wordc", wordc);
			wordValues.put("wordd", wordd);
			wordValues.put("worde", worde);
			wordValues.put("wordf", wordf);
			wordValues.put("wordg", wordg);
			wordValues.put("wordh", wordh);
			wordValues.put("wordi", wordi);
			wordValues.put("wordj", wordj);
			wordData.add(wordValues);
			worda=wordb=wordc=wordd=worde=wordf=wordg=wordh=wordi=wordj=null;
		 }
		 
		 public final static String asUpperCaseFirstChar(final String target) {

			    if ((target == null) || (target.length() == 0)) {
			        return target; // You could omit this check and simply live with an
			                       // exception if you like
			    }
			    return Character.toUpperCase(target.charAt(0))
			            + (target.length() > 1 ? target.substring(1) : "");
		}

		public ArrayList<ContentValues> getWordData() {
			return wordData;
		}

		public void setWordData(ArrayList<ContentValues> wordData) {
			this.wordData = wordData;
		}

		public ArrayList<ContentValues> getListData() {
			return listData;
		}

		public void setListData(ArrayList<ContentValues> listData) {
			this.listData = listData;
		}
}