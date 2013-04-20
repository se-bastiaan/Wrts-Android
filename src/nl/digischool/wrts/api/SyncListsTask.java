package nl.digischool.wrts.api;

import java.util.List;

import nl.digischool.wrts.classes.Utilities;
import nl.digischool.wrts.database.DbHelper;
import nl.digischool.wrts.database.DbModel;
import nl.digischool.wrts.objects.WordList;
import android.content.Context;
import android.os.AsyncTask;

import com.db4o.ObjectContainer;

public class SyncListsTask extends AsyncTask<Void, Void, Boolean> {
	
	private String authString, sinceString;
	private DbHelper db;
	
	public SyncListsTask(Context context, String auth, String since) {
		this.authString = auth;
		this.sinceString = since;
		this.db = new DbHelper(context);
	}
	
	public SyncListsTask(Context context, String auth) {
		this.authString = auth;
		this.db = new DbHelper(context);
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		try {
			ApiConnector api = new ApiConnector(this.authString);
			String indexXml = api.getDataFromServer("");
			List<String> index = XmlReader.readIndexXml(indexXml);
			db.openDatabase();
			for(int i = 0; i < index.size(); i++) {
				String listId = index.get(i);
				String listXml = api.getDataFromServer("lists/" + listId);
				WordList list = XmlReader.readListXml(listXml);
				ObjectContainer cont = db.openDbSession();
				DbModel.saveWordList(cont, list);
			}
		} catch (Exception e) {
			e.printStackTrace();
			// TODO
		}
		return true;
	}

}
