package nl.digischool.wrts.api;

import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.os.AsyncTask;

public class IndexReaderTask extends AsyncTask<Void, Void, ArrayList<String>> {

	private StringReader data;
	
	public IndexReaderTask(String xml) {
		this.data = new StringReader(xml);
	}
	
	@Override
	protected ArrayList<String> doInBackground(Void... params) {
		try {
			SAXParserFactory saxPF = SAXParserFactory.newInstance();
		    SAXParser saxP = saxPF.newSAXParser();
		    XMLReader xmlR = saxP.getXMLReader();
		    IndexXmlHandler XMLHandler = new IndexXmlHandler();
		    xmlR.setContentHandler(XMLHandler);
		    xmlR.parse(new InputSource(data));		    
			return XMLHandler.getData();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public void onPostExecute(ArrayList<String> result) {
		super.onPostExecute(result);
	}

}
