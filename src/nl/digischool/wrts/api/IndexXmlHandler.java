package nl.digischool.wrts.api;

import java.util.ArrayList;

import nl.digischool.wrts.classes.Utilities;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class IndexXmlHandler extends DefaultHandler {
	
	private ArrayList<String> data = new ArrayList<String>();
    private String LOG_TAG = getClass().getSimpleName();
    
    public ArrayList<String> getData() {
    	return data;
    }
    
    public void startDocument () {
        Utilities.log(LOG_TAG, "Start parser");
    }
    
    public void endDocument () {
        Utilities.log(LOG_TAG, "End parser");
    }
    
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if(localName.equals("lijst")) data.add(attributes.getValue("id"));
    }
	
}
