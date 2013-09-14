package nl.digischool.wrts.api;

import nl.digischool.wrts.classes.Utilities;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

public class IndexXmlHandler extends DefaultHandler {
	
	private ArrayList<String> mData = new ArrayList<String>();
    private String LOG_TAG = getClass().getSimpleName();
    
    public ArrayList<String> getData() {
    	return mData;
    }
    
    public void startDocument () {
        Utilities.log(LOG_TAG, "Start parser");
    }
    
    public void endDocument () {
        Utilities.log(LOG_TAG, "End parser");
    }
    
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if(localName.equals("lijst")) mData.add(attributes.getValue("id"));
    }
	
}
