package nl.digischool.wrts.api;

import java.util.ArrayList;

import nl.digischool.wrts.classes.Utilities;
import nl.digischool.wrts.objects.Word;
import nl.digischool.wrts.objects.WordList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.annotation.SuppressLint;
import android.util.Log;

@SuppressLint("DefaultLocale")
public class SyncXmlHandler extends DefaultHandler {

	private Boolean mElementSelected = false, mInList = false, mInWords = false, mInWord = false;
	private String mElementValue = null;
	private ArrayList<WordList> mListData;
	private WordList mList;
	private Word mWord;
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
    	}

    	if(mInList) {    		
    		if(localName.equals("words")) {
    			mInWords = false;
    		}
    		
    		if(mInWords && localName.equals("word")) {
    			mInWord = true;
                mWord = new Word();
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
        		Log.d("data", mList.lang_a);
        	} else if(localName.equals("id")) {
    			mList.id = mElementValue;
    		} else if(localName.equals("title")) {
    			mList.title = mElementValue;
    		} else if(localName.equals("lang-a")) {
    			mList.lang_a = mElementValue;
    		} else if(localName.equals("lang-b")) {
    			mList.lang_b = mElementValue;
    		} else if(localName.equals("lang-c")) {
    			mList.lang_c = mElementValue;
    		} else if(localName.equals("lang-d")) {
    			mList.lang_d = mElementValue;
    		} else if(localName.equals("lang-e")) {
    			mList.lang_e = mElementValue;
    		} else if(localName.equals("lang-f")) {
    			mList.lang_f = mElementValue;
    		} else if(localName.equals("lang-g")) {
    			mList.lang_g = mElementValue;
    		} else if(localName.equals("lang-h")) {
    			mList.lang_h = mElementValue;
    		} else if(localName.equals("lang-i")) {
    			mList.lang_i = mElementValue;
    		} else if(localName.equals("lang-j")) {
    			mList.lang_j = mElementValue;
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
    				mList.words.add(mWord);
    			}
    			
    			if(mInWord) {
    				if(localName.equals("word-a")) {
                        mWord.word_a = mElementValue;
    				} else if(localName.equals("word-b")) {
                        mWord.word_b = mElementValue;
    				} else if(localName.equals("word-c")) {
                        mWord.word_c = mElementValue;
    				} else if(localName.equals("word-d")) {
                        mWord.word_d = mElementValue;
    				} else if(localName.equals("word-e")) {
                        mWord.word_e = mElementValue;
    				} else if(localName.equals("word-f")) {
                        mWord.word_f = mElementValue;
    				} else if(localName.equals("word-g")) {
                        mWord.word_g = mElementValue;
    				} else if(localName.equals("word-h")) {
                        mWord.word_h = mElementValue;
    				} else if(localName.equals("word-i")) {
                        mWord.word_i = mElementValue;
    				} else if(localName.equals("word-j")) {
                        mWord.word_j = mElementValue;
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
