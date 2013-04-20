package nl.digischool.wrts.api;

import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import nl.digischool.wrts.objects.WordList;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

public class XmlReader {

	public static ArrayList<String> readIndexXml(String xml) {
		try {
			SAXParserFactory saxPF = SAXParserFactory.newInstance();
		    SAXParser saxP = saxPF.newSAXParser();
		    XMLReader xmlR = saxP.getXMLReader();
		    IndexXmlHandler XMLHandler = new IndexXmlHandler();
		    xmlR.setContentHandler(XMLHandler);
		    xmlR.parse(new InputSource(xml));		    
			return XMLHandler.getData();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static WordList readListXml(String xml) {
		try {
			SAXParserFactory saxPF = SAXParserFactory.newInstance();
		    SAXParser saxP = saxPF.newSAXParser();
		    XMLReader xmlR = saxP.getXMLReader();
		    WordListXmlHandler XMLHandler = new WordListXmlHandler();
		    xmlR.setContentHandler(XMLHandler);
		    xmlR.parse(new InputSource(xml));		    
			return XMLHandler.getList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
