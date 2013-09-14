package nl.digischool.wrts.api;

import nl.digischool.wrts.classes.Utilities;
import nl.digischool.wrts.database.Word;
import nl.digischool.wrts.database.WordList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class WordListXmlHandler extends DefaultHandler {
	
	private Boolean mElementSelected = false, mInList = false, mInWords = false, mInWord = false;
	private String mElementValue = null;
	private WordList mList = new WordList();
	private Word mWord;
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
        if(localName.equals("list")) {
            mInList = true;
            mList = new WordList();
        }

        if(mInList) {
            if(localName.equals("words")) {
                mInWords = true;
            }

            if(mInWords && localName.equals("word")) {
                mInWord = true;
            }

            if(mInWord && localName.startsWith("word-")) {
                mWord = new Word();
                mWord.setList_id(mList.getId());
            }
        }
    }
    
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
    	mElementSelected = false;    	
    	if(mInList) {
            if(localName.equals("list")) {
                mInList = false;
                //Log.d("data", mList.lang_a);
            } else if(localName.equals("id")) {
                mList.setId(Long.getLong(mElementValue));
            } else if(localName.equals("title")) {
                mList.setTitle(mElementValue);
            } else if(localName.startsWith("lang-")) {
                if(localName.endsWith("a")) {
                    mList.setLang_a(mElementValue);
                } else if(localName.endsWith("b")) {
                    mList.setLang_b(mElementValue);
                } else if(localName.endsWith("c")) {
                    mList.setLang_c(mElementValue);
                } else if(localName.endsWith("d")) {
                    mList.setLang_d(mElementValue);
                } else if(localName.endsWith("e")) {
                    mList.setLang_e(mElementValue);
                } else if(localName.endsWith("f")) {
                    mList.setLang_f(mElementValue);
                } else if(localName.endsWith("g")) {
                    mList.setLang_g(mElementValue);
                } else if(localName.endsWith("h")) {
                    mList.setLang_h(mElementValue);
                } else if(localName.endsWith("i")) {
                    mList.setLang_i(mElementValue);
                } else if(localName.endsWith("j")) {
                    mList.setLang_j(mElementValue);
                }
            } else if(localName.equals("created-on")) {
                mList.setCreated_on(mElementValue);
            } else if(localName.equals("updated-on")) {
                mList.setUpdated_on(mElementValue);
            } else if(localName.equals("share")) {
                mList.setShared(mElementValue.equals("true"));
            } else if(localName.equals("result-count")) {
                mList.setResult_count(Integer.parseInt(mElementValue));
            }

            if(mInWords) {
                if(localName.equals("words")) {
                    mInWords = false;
                } else if(localName.equals("word")) {
                    mInWord = false;
                }

                if(mInWord) {
                    if(localName.startsWith("word-")) {
                        if(localName.endsWith("a")) {
                            mWord.setWord_a(mElementValue);
                        } else if(localName.endsWith("b")) {
                            mWord.setWord_b(mElementValue);
                        } else if(localName.endsWith("c")) {
                            mWord.setWord_c(mElementValue);
                        } else if(localName.endsWith("d")) {
                            mWord.setWord_d(mElementValue);
                        } else if(localName.endsWith("e")) {
                            mWord.setWord_e(mElementValue);
                        } else if(localName.endsWith("f")) {
                            mWord.setWord_f(mElementValue);
                        } else if(localName.endsWith("g")) {
                            mWord.setWord_g(mElementValue);
                        } else if(localName.endsWith("h")) {
                            mWord.setWord_h(mElementValue);
                        } else if(localName.endsWith("i")) {
                            mWord.setWord_i(mElementValue);
                        } else if(localName.endsWith("j")) {
                            mWord.setWord_j(mElementValue);
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
}
