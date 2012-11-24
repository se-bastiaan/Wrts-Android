package nl.sebmaakt.app.wrts_ds;

import WrtsMobile.com.R;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class IndexParser extends DefaultHandler {

	protected static ListsCollector Collector;
    
	public ListsCollector getCollector() {
		return Collector;
	}
	
    public void startDocument () {
        //Log.i("Parser", "Start");
    }
    
    public void endDocument () {
    	//Log.i("Parser", "Einde");    	
    }
    
    public void startElement (String uri, String name, String qName, Attributes atts) {
    	if (name.equalsIgnoreCase("response")) {
    		//Log.d("PARSER", "NEW INDEXCOLLECTOR");
    		Collector = new ListsCollector(true);
        } else if (name.equalsIgnoreCase("lijst")) {
        	//Log.d("PARSING", atts.getValue("id"));        	
        	Collector.setId(atts.getValue("id"));
        }
    }

    public void endElement (String uri, String name, String qName) {

    }
 	
    public void characters(char ch[], int start, int length) {    }
 }