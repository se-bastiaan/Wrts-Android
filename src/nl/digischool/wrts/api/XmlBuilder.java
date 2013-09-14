package nl.digischool.wrts.api;

import android.os.AsyncTask;
import android.util.Xml;
import nl.digischool.wrts.classes.Utilities;
import nl.digischool.wrts.database.Word;
import nl.digischool.wrts.database.WordList;
import org.xmlpull.v1.XmlSerializer;

import java.io.StringWriter;

public class XmlBuilder {
	
	private WordList mList;
	
	/**
	 * Create new XmlBuilder without specified WordLst
	 */
	public XmlBuilder() {
		mList = new WordList();
	}
	
	/**
	 * Create new XmlBuilder with specified WordList
	 * @param list containing all data
	 */
	public XmlBuilder(WordList list) {
		mList = list;
	}
	
	/**
	 * Specify WordList title
	 * @param title set title of list
	 */
	public void setTitle(String title) {
		mList.setTitle(title);
	}
	
	/**
	 * Specify WordList id
	 * @param id
	 */
	public void setId(String id) {
		mList.setId(Long.getLong(id));
	}

    /**
     * Specify WordList id
     * @param id
     */
    public void setId(Long id) {
        mList.setId(id);
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
        switch(lang_index) {
            case 0:
                mList.setLang_a(lang);
                break;
            case 1:
                mList.setLang_b(lang);
                break;
            case 2:
                mList.setLang_c(lang);
                break;
            case 3:
                mList.setLang_d(lang);
                break;
            case 4:
                mList.setLang_e(lang);
                break;
            case 5:
                mList.setLang_f(lang);
                break;
            case 6:
                mList.setLang_g(lang);
                break;
            case 7:
                mList.setLang_h(lang);
                break;
            case 8:
                mList.setLang_i(lang);
                break;
            case 9:
                mList.setLang_j(lang);
                break;
        }
	}
	
	/**
	 * Return WordList
	 * @return get the list which has been created
	 */
	public WordList getList() {
		return mList;
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
				        
				        	if(mList.getId() != null) {
					        	serializer.startTag("", "id");
					        	serializer.text(mList.getId().toString());
					        	serializer.endTag("", "id");
				        	}
				        	
				        	serializer.startTag("", "title");
				        	serializer.text(mList.getTitle());
				        	serializer.endTag("", "title");

				        	for(int i = 0; i < 10; i++) {
                                String language = null;
                                switch(i) {
                                    case 0:
                                        language = mList.getLang_a();
                                        break;
                                    case 1:
                                        language = mList.getLang_b();
                                        break;
                                    case 2:
                                        language = mList.getLang_c();
                                        break;
                                    case 3:
                                        language = mList.getLang_d();
                                        break;
                                    case 4:
                                        language = mList.getLang_e();
                                        break;
                                    case 5:
                                        language = mList.getLang_f();
                                        break;
                                    case 6:
                                        language = mList.getLang_g();
                                        break;
                                    case 7:
                                        language = mList.getLang_h();
                                        break;
                                    case 8:
                                        language = mList.getLang_i();
                                        break;
                                    case 9:
                                        language = mList.getLang_j();
                                        break;
                                }
				        		String languageName = Utilities.getLanguageName(i);
                                if(language != null) {
                                    serializer.startTag("", languageName);
                                    serializer.text(language);
                                    serializer.endTag("", languageName);
                                }
				        	}

                            serializer.startTag("", "words");
                            for(Word word : mList.getWords()) {
                                String wordText = null;
                                for(int j = 0; j < 10; j++) {
                                    switch (j) {
                                        case 0:
                                            if(word.getWord_a() != null && !word.getWord_a().isEmpty()) wordText = word.getWord_a();
                                            break;
                                        case 1:
                                            if(word.getWord_b() != null && !word.getWord_b().isEmpty()) wordText = word.getWord_b();
                                            break;
                                        case 2:
                                            if(word.getWord_c() != null && !word.getWord_c().isEmpty()) wordText = word.getWord_c();
                                            break;
                                        case 3:
                                            if(word.getWord_d() != null && !word.getWord_d().isEmpty()) wordText = word.getWord_d();
                                            break;
                                        case 4:
                                            if(word.getWord_e() != null && !word.getWord_e().isEmpty()) wordText = word.getWord_e();
                                            break;
                                        case 5:
                                            if(word.getWord_f() != null && !word.getWord_f().isEmpty()) wordText = word.getWord_f();
                                            break;
                                        case 6:
                                            if(word.getWord_g() != null && !word.getWord_g().isEmpty()) wordText = word.getWord_g();
                                            break;
                                        case 7:
                                            if(word.getWord_h() != null && !word.getWord_h().isEmpty()) wordText = word.getWord_h();
                                            break;
                                        case 8:
                                            if(word.getWord_i() != null && !word.getWord_i().isEmpty()) wordText = word.getWord_i();
                                            break;
                                        case 9:
                                            if(word.getWord_j() != null && !word.getWord_j().isEmpty()) wordText = word.getWord_j();
                                            break;
                                    }
                                    String wordName = Utilities.getWordName(j);
                                    if(wordText != null && !wordText.isEmpty()) {
                                        serializer.startTag("", wordName);
                                        serializer.text(wordText);
                                        serializer.endTag("", wordName);
                                    }
                                }
                            }
                            serializer.endTag("", "words");
				        	
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
