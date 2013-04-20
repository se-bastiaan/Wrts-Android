package nl.digischool.wrts.api;

import nl.digischool.wrts.objects.Word;
import nl.digischool.wrts.objects.WordList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class WordListXmlHandler extends DefaultHandler {
	
	private Boolean elementSelected = false, inList = false, inWords = false, inWord = false;
	private String elementValue = null;
	private WordList list = new WordList();
	private Word word = new Word();
    
    public WordList getList() {
    	return list;
    }
    
    public void startDocument () {
        //Log.d("Parser", "Start");
    }
    
    public void endDocument () {
        //Log.d("Parser", "Einde");     
    }
    
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
    	elementSelected = true;
    	elementValue = null;
    	if(inList) {
    		if(localName.equals("list")) {
        		inList = false;
        	}
    		
    		if(localName.equals("words")) {
    			inWords = false;
    		}
    		
    		if(inWords && localName.equals("word")) {
    			inWord = true;
    			word = new Word();
    		}
    	}
    }
    
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
    	elementSelected = false;    	
    	if(inList) {
    		if(localName.equals("list")) {
        		inList = false;
        	} else if(localName.equals("id")) {
    			list.id = elementValue;
    		} else if(localName.equals("title")) {
    			list.title = elementValue;
    		} else if(localName.equals("lang-a")) {
    			list.lang_a = elementValue;
    		} else if(localName.equals("lang-b")) {
    			list.lang_b = elementValue;
    		} else if(localName.equals("lang-c")) {
    			list.lang_c = elementValue;
    		} else if(localName.equals("lang-d")) {
    			list.lang_d = elementValue;
    		} else if(localName.equals("lang-e")) {
    			list.lang_e = elementValue;
    		} else if(localName.equals("lang-f")) {
    			list.lang_f = elementValue;
    		} else if(localName.equals("lang-g")) {
    			list.lang_g = elementValue;
    		} else if(localName.equals("lang-h")) {
    			list.lang_h = elementValue;
    		} else if(localName.equals("lang-i")) {
    			list.lang_i = elementValue;
    		} else if(localName.equals("lang-j")) {
    			list.lang_j = elementValue;
    		} else if(localName.equals("created-on")) {
    			list.created_on = elementValue;
    		} else if(localName.equals("updated-on")) {
    			list.updated_on = elementValue;
    		} else if(localName.equals("share")) {
    			list.shared = (elementValue.equals("true"));
    		} else if(localName.equals("result-count")) {
    			list.result_count = Integer.parseInt(elementValue);
    		}
    		
    		if(inWords) {
    			if(localName.equals("words")) {
        			inWords = false;
        		} else if(localName.equals("word")) {
    				inWord = false;
    				list.words.add(word);
    			}
    			
    			if(inWord) {
    				if(localName.equals("word-a")) {
    					word.word_a = elementValue;
    				} else if(localName.equals("word-b")) {
    					word.word_b = elementValue;
    				} else if(localName.equals("word-c")) {
    					word.word_c = elementValue;
    				} else if(localName.equals("word-d")) {
    					word.word_d = elementValue;
    				} else if(localName.equals("word-e")) {
    					word.word_e = elementValue;
    				} else if(localName.equals("word-f")) {
    					word.word_f = elementValue;
    				} else if(localName.equals("word-g")) {
    					word.word_g = elementValue;
    				} else if(localName.equals("word-h")) {
    					word.word_h = elementValue;
    				} else if(localName.equals("word-i")) {
    					word.word_i = elementValue;
    				} else if(localName.equals("word-j")) {
    					word.word_j = elementValue;
    				}
    			}
    		}
    	}
    }
    
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (elementSelected) {
            elementValue = new String(ch, start, length);
            elementSelected = false;
        }
    }
}
