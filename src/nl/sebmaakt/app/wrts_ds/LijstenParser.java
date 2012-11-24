package nl.sebmaakt.app.wrts_ds;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import WrtsMobile.com.R;
import android.app.Activity;
import android.util.Log;

public class LijstenParser extends DefaultHandler {

	protected static ListsCollector Collector;
	private Boolean currentElement = false, wordElement = false, wordsElement = false, listElement = false;
	private String currentValue;
	private StringBuilder buf;
	private int downloadedcount = 0;
	private Activity activity;
    
	public ListsCollector getCollector() {
		return Collector;
	}
	
	public LijstenParser(Activity activity) {
		this.activity = activity;
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
    	if (name.equalsIgnoreCase("list-index")) {
    		Log.d("PARSER", "NEW LISTSCOLLECTOR");
    		Collector = new ListsCollector();
        } else if (name.equalsIgnoreCase("list")) {
        	downloadedcount++;        	
        	activity.runOnUiThread(new Runnable() {
        	    public void run() {
        	    	if(activity.getLocalClassName().equals("WrtsAppActivity")) {
        	    		WrtsAppActivity.increaseDownloaded(downloadedcount);
        	    	} else if(activity.getLocalClassName().equals("FirstStartActivity")) {
        	    		FirstStartActivity.increaseDownloaded(downloadedcount);
        	    	} else if(activity.getLocalClassName().equals("SettingsActivity")) {
        	    		SettingsActivity.increaseDownloaded(downloadedcount);
         	    	}
        	    }
        	});
        	listElement = true;
        } else if (name.equalsIgnoreCase("word")) {
        	wordElement = true;
        } else if (name.equalsIgnoreCase("words")) {
        	//Log.d("Parser", "Wordselement");
        	wordsElement = true;
        }
    }

    public void endElement (String uri, String name, String qName) {
    	currentElement = false;    	
    	if (name.equalsIgnoreCase("list")) {
    		listElement = false;    
    		Collector.setList();
    	} else if (name.equalsIgnoreCase("id") && listElement) {
            //Log.i("PARSER", currentValue);
            Collector.setId(currentValue);
	    } else if (name.equalsIgnoreCase("title") && listElement) {
	            Collector.setTitle(currentValue);
	            //Log.i("PARSER", currentValue);
	    } else if (name.equalsIgnoreCase("lang-a") && listElement) {
	        Collector.setLangA(currentValue);
	    } else if (name.equalsIgnoreCase("lang-b") && listElement) {
	        Collector.setLangB(currentValue);
	    } else if (name.equalsIgnoreCase("lang-c") && listElement) {                    
	        Collector.setLangC(currentValue);
	    } else if (name.equalsIgnoreCase("lang-d") && listElement) {
	        Collector.setLangD(currentValue);
	    } else if (name.equalsIgnoreCase("lang-e") && listElement) {
	        Collector.setLangE(currentValue);
	    } else if (name.equalsIgnoreCase("lang-f") && listElement) {
	        Collector.setLangF(currentValue);
	    } else if (name.equalsIgnoreCase("lang-g") && listElement) {
	        Collector.setLangG(currentValue);
	    } else if (name.equalsIgnoreCase("lang-h") && listElement) {
	        Collector.setLangH(currentValue);
	    } else if (name.equalsIgnoreCase("lang-i") && listElement) {
	        Collector.setLangI(currentValue);
	    } else if (name.equalsIgnoreCase("lang-j") && listElement) {
	        Collector.setLangJ(currentValue);
	    } else if (name.equalsIgnoreCase("updated-on") && listElement) {
	       Collector.setUpdated(currentValue);
	    } else if (name.equalsIgnoreCase("word-a") & wordElement) {
	    	//Log.i("PARSER", currentValue);
    		Collector.setWordA(currentValue);
    	} else if (name.equalsIgnoreCase("word-b") & wordElement) {
    		//Log.i("PARSER", currentValue);
    		Collector.setWordB(currentValue);
    	} else if (name.equalsIgnoreCase("word-c") & wordElement) {
    		Collector.setWordC(currentValue);
    	} else if (name.equalsIgnoreCase("word-d") & wordElement) {
    		Collector.setWordD(currentValue);
    	} else if (name.equalsIgnoreCase("word-e") & wordElement) {
    		Collector.setWordE(currentValue);
    	} else if (name.equalsIgnoreCase("word-f") & wordElement) {
    		Collector.setWordF(currentValue);
    	} else if (name.equalsIgnoreCase("word-g") & wordElement) {
    		Collector.setWordG(currentValue);
    	} else if (name.equalsIgnoreCase("word-h") & wordElement) {
    		Collector.setWordH(currentValue);
    	} else if (name.equalsIgnoreCase("word-i") & wordElement) {
    		Collector.setWordI(currentValue);
    	} else if (name.equalsIgnoreCase("word-j") & wordElement) {
    		Collector.setWordJ(currentValue);
    	} else if (name.equalsIgnoreCase("word") && wordsElement) {
    		Collector.setWord();
    		wordElement = false;
    	} else if (name.equalsIgnoreCase("words") && listElement) {    		
    		wordsElement = false;
    	}
    	buf = null;
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