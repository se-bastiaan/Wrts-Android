package nl.digischool.wrts.api;

import nl.digischool.wrts.classes.Utilities;
import nl.digischool.wrts.objects.Word;
import nl.digischool.wrts.objects.WordList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class WordListXmlHandler extends DefaultHandler {
	
	private Boolean mElementSelected = false, mInList = false, mInWords = false, inWord = false;
	private String mElementValue = null;
	private WordList mList = new WordList();
	private Word mWord = new Word();
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
                mWord = new Word();
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
    				inWord = false;
    				mList.words.add(mWord);
    			}
    			
    			if(inWord) {
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
}
