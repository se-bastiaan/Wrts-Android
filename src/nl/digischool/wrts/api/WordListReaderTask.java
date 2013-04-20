package nl.digischool.wrts.api;

import java.io.StringReader;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import nl.digischool.wrts.objects.WordList;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.os.AsyncTask;

public class WordListReaderTask extends AsyncTask<Void, Void, WordList> {

	private StringReader data;
	
	public WordListReaderTask(String xml) {
		this.data = new StringReader(xml);
	}
	
	@Override
	protected WordList doInBackground(Void... params) {
		try {
			SAXParserFactory saxPF = SAXParserFactory.newInstance();
		    SAXParser saxP = saxPF.newSAXParser();
		    XMLReader xmlR = saxP.getXMLReader();
		    WordListXmlHandler XMLHandler = new WordListXmlHandler();
		    xmlR.setContentHandler(XMLHandler);
		    xmlR.parse(new InputSource(data));		    
			return XMLHandler.getList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public void onPostExecute(WordList result) {
		super.onPostExecute(result);
	}

}
