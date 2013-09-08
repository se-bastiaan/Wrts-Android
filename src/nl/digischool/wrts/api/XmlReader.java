package nl.digischool.wrts.api;

import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

public class XmlReader {

	public static ArrayList<String> readIndexXml(String xml) {
		try {
			StringReader reader = new StringReader(xml);
			SAXParserFactory saxPF = SAXParserFactory.newInstance();
		    SAXParser saxP = saxPF.newSAXParser();
		    XMLReader xmlR = saxP.getXMLReader();
		    IndexXmlHandler XMLHandler = new IndexXmlHandler();
		    xmlR.setContentHandler(XMLHandler);
		    xmlR.parse(new InputSource(reader));		    
			return XMLHandler.getData();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Boolean readListXml(StringReader stringReader, ProgressUpdateCallback callback) {
		try {
            SAXParserFactory saxPF = SAXParserFactory.newInstance();
		    SAXParser saxP = saxPF.newSAXParser();
		    XMLReader xmlR = saxP.getXMLReader();
		    WordListXmlHandler XMLHandler = new WordListXmlHandler();
		    xmlR.setContentHandler(XMLHandler);
		    xmlR.parse(new InputSource(stringReader));
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static SyncXmlHandler readSyncXml(StringReader stringReader, ProgressUpdateCallback callback) {
		try {
            SAXParserFactory saxPF = SAXParserFactory.newInstance();
		    SAXParser saxP = saxPF.newSAXParser();
		    XMLReader xmlR = saxP.getXMLReader();
		    SyncXmlHandler XMLHandler = new SyncXmlHandler(callback);
		    xmlR.setContentHandler(XMLHandler);
		    xmlR.parse(new InputSource(stringReader));
		    return XMLHandler;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
