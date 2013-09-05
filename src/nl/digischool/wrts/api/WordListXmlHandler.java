package nl.digischool.wrts.api;

import nl.digischool.wrts.classes.Utilities;
import nl.digischool.wrts.objects.WordList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.HashMap;
import java.util.Map;

public class WordListXmlHandler extends DefaultHandler {
	
	private Boolean mElementSelected = false, mInList = false, mInWords = false, inWord = false;
	private String mElementValue = null;
	private WordList mList = new WordList();
	private Map<String, String> mWord;
    private String LOG_TAG = getClass().getSimpleName();

    public WordList getList() {
    	return mList;
    }

    public void startDocument () {
        Utilities.log(LOG_TAG, "Start parser");
    }

    public void endDocument () {
        Utilities.log(LOG_TAG, "End parser");
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
    	mElementSelected = true;
    	mElementValue = null;
    	if(localName.equals("mList")) {
    		mInList = true;
    	}
    	if(mInList) {
    		if(localName.equals("words")) {
    			mInWords = false;
    		}
    		
    		if(mInWords && localName.equals("word")) {
    			inWord = true;
                mWord = new HashMap<String, String>();
    		}
    	}
    }
    
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
    	mElementSelected = false;    	
    	if(mInList) {
    		if(localName.equals("mList")) {
        		mInList = false;
        	} else if(localName.equals("id")) {
    			mList.id = mElementValue;
    		} else if(localName.equals("title")) {
    			mList.title = mElementValue;
    		} else if(localName.startsWith("lang-")) {
    			mList.languages.put(localName, mElementValue);
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
    				inWord = false;
    				mList.words.add(mWord);
    			}
    			
    			if(inWord) {
    				if(localName.startsWith("word-")) {
                        mWord.put(localName, mElementValue);
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
}
