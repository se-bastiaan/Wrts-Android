package nl.digischool.wrts.api;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Map;

import nl.digischool.wrts.classes.Utilities;
import nl.digischool.wrts.objects.WordList;

import org.xmlpull.v1.XmlSerializer;

import android.os.AsyncTask;
import android.util.Xml;

public class XmlBuilder {
	
	private WordList mDataList;
	
	/**
	 * Create new XmlBuilder without specified WordLst
	 */
	public XmlBuilder() {
		mDataList = new WordList();
	}
	
	/**
	 * Create new XmlBuilder with specified WordList
	 * @param list containing all data
	 */
	public XmlBuilder(WordList list) {
		mDataList = list;
	}
	
	/**
	 * Specify WordList title
	 * @param title set title of list
	 */
	public void setTitle(String title) {
		mDataList.title = title;
	}
	
	/**
	 * Specify WordList id
	 * @param id
	 */
	public void setId(String id) {
		mDataList.id = id;
	}
	
	/**
	 * Specify WordList language using string lang_id
	 * @param lang_id (a to j)
	 * @param lang language value
	 */
	public void setLang(String lang_id, String lang) {
		this.setLang("abcdefghij".indexOf(lang_id), lang);
	}
	
	/**
	 * Specify WordList language using lang_index
	 * @param lang_index (0 to 9)
	 * @param lang
	 */
	public void setLang(Integer lang_index, String lang) {
        mDataList.languages.put(Utilities.getLanguageName(lang_index), lang);
	}
	
	/**
	 * Specify WordList words ArrayList containing all Word objects for the list
	 * @param words words to be added to WordList
	 */
	public void setWords(ArrayList<Map<String, String>> words) {
		mDataList.words = words;
	}
	
	/**
	 * Return WordList
	 * @return get the list which has been created
	 */
	public WordList getList() {
		return mDataList;
	}

	/**
	 * Return generated XML using WordList as source
	 * @return String containing XML
	 */
	public String buildXml() {
		String returnXml = null;
				
		try {
			returnXml = new AsyncTask<Void, Void, String>() {

				@Override
				protected String doInBackground(Void... params) {
					XmlSerializer serializer = Xml.newSerializer();
				    StringWriter writer = new StringWriter();
				    try {
				        serializer.setOutput(writer);
				        serializer.startTag("", "list");
				        
				        	if(mDataList.id != null) {
					        	serializer.startTag("", "id");
					        	serializer.text(mDataList.id);
					        	serializer.endTag("", "id");
				        	}
				        	
				        	serializer.startTag("", "title");
				        	serializer.text(mDataList.title);
				        	serializer.endTag("", "title");
				        
				        	for(int i = 0; i < 10; i++) {
				        		String languageName = Utilities.getLanguageName(i);
                                if(mDataList.languages.containsKey(languageName) && !mDataList.languages.get(languageName).isEmpty()) {
                                    serializer.startTag("", languageName);
                                    serializer.text(mDataList.languages.get(languageName));
                                    serializer.endTag("", languageName);
                                }
				        	}
				        	
				        	if(mDataList.words != null) {
					        	for(int i = 0; i < mDataList.words.size(); i++) {
					        		Map<String, String> word = mDataList.words.get(i);
					        		for(int j = 0; j < 10; j++) {
                                        String wordName = Utilities.getWordName(j);
                                        if(word.containsKey(wordName) && !word.get(wordName).isEmpty()) {
                                            serializer.startTag("", wordName);
                                            serializer.text(word.get(wordName));
                                            serializer.endTag("", wordName);
                                        }
						        	}
						        	
					        	}
				        	}
				        	
				        serializer.endTag("", "list");
				        serializer.endDocument();
				        return writer.toString();
				    } catch (Exception e) {
				        e.printStackTrace();
				    }
					return null;
				}
				
			}.execute().get();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	    return returnXml;
	}
	
}
