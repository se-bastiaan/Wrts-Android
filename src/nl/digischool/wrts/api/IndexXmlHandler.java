package nl.digischool.wrts.api;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class IndexXmlHandler extends DefaultHandler {
	
	private ArrayList<String> data = new ArrayList<String>();
    
    public ArrayList<String> getData() {
    	return data;
    }
    
    public void startDocument () {
        //Log.d("Parser", "Start");
    }
    
    public void endDocument () {
        //Log.d("Parser", "Einde");     
    }
    
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if(localName.equals("lijst")) data.add(attributes.getValue("id"));
    }
	
}
