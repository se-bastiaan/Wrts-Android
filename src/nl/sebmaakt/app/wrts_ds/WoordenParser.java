package nl.sebmaakt.app.wrts_ds;

import WrtsMobile.com.R;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class WoordenParser extends DefaultHandler {

	protected static WordsCollector Collector;
	Boolean currentElement = false;
	Boolean wordsElement = false;
	Boolean wordElement = false;
	String currentValue = null;
	StringBuilder buf;
    
	public WordsCollector getCollector() {
		return Collector;
	}
	
    public void startDocument () {
        //Log.i("Parser", "Start");
    }
    
    public void endDocument () {
    	//Log.i("Parser", "Einde");
    }
    
    public void startElement (String uri, String name, String qName, Attributes atts) {
    	currentElement = true;
    	buf = new StringBuilder();
    	if (name.equalsIgnoreCase("list")) {
    		//Log.i("PARSER", "NEW LISTSCOLLECTOR");
    		Collector = new WordsCollector();
        } else if (name.equalsIgnoreCase("words")) {
        	
        }
    }

    public void endElement (String uri, String name, String qName) {
    	currentElement = false;
    	if (name.equalsIgnoreCase("id")) {
    		Collector.setId(currentValue);
    	} else if (name.equalsIgnoreCase("title")) {
    		Collector.setTitle(currentValue);
    	} else if (name.equalsIgnoreCase("updated-on")) {
    		Collector.setUpdated(currentValue);
    	} else if (name.equalsIgnoreCase("lang-a")) {
    		Collector.setLangA(currentValue);
    	} else if (name.equalsIgnoreCase("lang-b")) {
    		Collector.setLangB(currentValue);
    	} else if (name.equalsIgnoreCase("lang-c")) {    		
    		Collector.setLangC(currentValue);
    	} else if (name.equalsIgnoreCase("lang-d")) {
    		Collector.setLangD(currentValue);
    	} else if (name.equalsIgnoreCase("lang-e")) {
    		Collector.setLangE(currentValue);
    	} else if (name.equalsIgnoreCase("lang-f")) {
    		Collector.setLangF(currentValue);
    	} else if (name.equalsIgnoreCase("lang-g")) {
    		Collector.setLangG(currentValue);
    	} else if (name.equalsIgnoreCase("lang-h")) {
    		Collector.setLangH(currentValue);
    	} else if (name.equalsIgnoreCase("lang-i")) {
    		Collector.setLangI(currentValue);
    	} else if (name.equalsIgnoreCase("lang-j")) {
    		Collector.setLangJ(currentValue);
    	} else if (name.equalsIgnoreCase("word-a")) {
    		Collector.setWordA(currentValue);
    	} else if (name.equalsIgnoreCase("word-b")) {
    		Collector.setWordB(currentValue);
    	} else if (name.equalsIgnoreCase("word-c")) {
    		Collector.setWordC(currentValue);
    	} else if (name.equalsIgnoreCase("word-d")) {
    		Collector.setWordD(currentValue);
    	} else if (name.equalsIgnoreCase("word-e")) {
    		Collector.setWordE(currentValue);
    	} else if (name.equalsIgnoreCase("word-f")) {
    		Collector.setWordF(currentValue);
    	} else if (name.equalsIgnoreCase("word-g")) {
    		Collector.setWordG(currentValue);
    	} else if (name.equalsIgnoreCase("word-h")) {
    		Collector.setWordH(currentValue);
    	} else if (name.equalsIgnoreCase("word-i")) {
    		Collector.setWordI(currentValue);
    	} else if (name.equalsIgnoreCase("word-j")) {
    		Collector.setWordJ(currentValue);
    	}
    	currentValue = null;
    	//Afhandeling eind element hier
    }
 	
    public void characters (char ch[], int start, int length) {
    	if (currentElement) {			
			if (buf!=null) {
		        for (int i=start; i<start+length; i++) {
		            buf.append(ch[i]);
		        }
		    }
			currentValue = buf.toString();
		}
    }
 }