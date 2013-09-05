package nl.digischool.wrts.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import nl.digischool.wrts.classes.Utilities;
import nl.digischool.wrts.objects.WordList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.annotation.SuppressLint;

@SuppressLint("DefaultLocale")
public class SyncXmlHandler extends DefaultHandler {

	private Boolean mElementSelected = false, mInList = false, mInWords = false, mInWord = false;
	private String mElementValue = null;
	private ArrayList<WordList> mListData;
	private WordList mList;
	private Map<String, String> mWord;
    private final String LOG_TAG = getClass().getSimpleName();

    /**
     * Give data to requestor
     * @return ArrayList containing all mListdata
     */
	public ArrayList<WordList> getData() {
		return mListData;
	}

    /**
     * Create ArrayList on document start
     */
    public void startDocument () {
    	mListData = new ArrayList<WordList>();
        Utilities.log(LOG_TAG, "Start parser");
    }
    
    public void endDocument () {
        Utilities.log(LOG_TAG, "End parser");
    }
    
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
    	mElementSelected = true;
    	mElementValue = null;

    	if(localName.equals("list")) {
    		mInList = true;
    		mList = new WordList();
            mList.languages = new HashMap<String, String>();
            mList.words = new ArrayList<Map<String, String>>();
    	}

    	if(mInList) {    		
    		if(localName.equals("words")) {
    			mInWords = true;
    		}
    		
    		if(mInWords && localName.equals("word")) {
    			mInWord = true;
                mWord = new HashMap<String, String>();
    		}
    	}
    }
    
    @SuppressLint("DefaultLocale")
	@Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
    	mElementSelected = false;    	
    	if(mInList) {
    		if(localName.equals("list")) {
        		mInList = false;
        		mListData.add(mList);
        		//Log.d("data", mList.lang_a);
        	} else if(localName.equals("id")) {
    			mList.id = mElementValue;
    		} else if(localName.equals("title")) {
    			mList.title = mElementValue;
    		} else if(localName.startsWith("lang-")) {
                if(mElementValue != null && !mElementValue.isEmpty() && mElementValue.length() > 0) {
                    mList.languages.put(localName, mElementValue);
                }
    		} else if(localName.equals("created-on")) {
    			mList.created_on = mElementValue;
    		} else if(localName.equals("updated-on")) {
    			mList.updated_on = mElementValue;
    		} else if(localName.equals("share")) {
    			mList.shared = (mElementValue.equals("true"));
    		} else if(localName.equals("result-count")) {
    			mList.result_count = Integer.parseInt(mElementValue);
    		}
    		
    		if(mInWords) {
    			if(localName.equals("words")) {
        			mInWords = false;
        		} else if(localName.equals("word")) {
    				mInWord = false;
                    Utilities.log(LOG_TAG, mWord);
    				mList.words.add(mWord);
    			}
    			
    			if(mInWord) {
    				if(localName.startsWith("word-")) {
                        if(mElementValue != null && !mElementValue.isEmpty()) {
                            mWord.put(localName, mElementValue);
                        } else {
                            mWord.put(localName, "\"Leeg\"");
                        }

                    }
    			}
    		}
    	}
    }
    
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (mElementSelected) {
            mElementValue = new String(ch, start, length);
            mElementSelected = false;
        }
    }

    public void log(String obj) {
        Utilities.log(LOG_TAG, obj);
    }

}
