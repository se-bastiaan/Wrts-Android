package nl.digischool.wrts.api;

import java.util.List;

import nl.digischool.wrts.database.DbHelper;
import nl.digischool.wrts.database.DbModel;
import nl.digischool.wrts.objects.WordList;
import android.content.Context;
import android.os.AsyncTask;

import com.db4o.ObjectContainer;

public class SyncListsTask extends AsyncTask<Void, Void, Boolean> {
	
	private String authString, sinceString = "";
	private DbHelper db;
	
	public SyncListsTask(Context context, String auth, String since) {
		this.authString = auth;
		this.sinceString = "?since="+since;
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
			String syncXml = api.getDataFromServer("/lists/all"+sinceString);
			List<WordList> data = XmlReader.readSyncXml(syncXml);
			db.openDatabase();
			ObjectContainer cont = db.openDbSession();
			for(int i = 0; i < data.size(); i++) {
				WordList list = data.get(i);			
				DbModel.saveWordList(cont, list);
			}
		} catch (Exception e) {
			e.printStackTrace();
			// TODO
		}
		return true;
	}

}
