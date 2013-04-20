package nl.digischool.wrts.api;

import java.util.ArrayList;

import nl.digischool.wrts.classes.Utilities;
import nl.digischool.wrts.database.DbHelper;
import nl.digischool.wrts.database.DbModel;
import nl.digischool.wrts.objects.WordList;
import android.os.AsyncTask;

import com.db4o.ObjectContainer;

public class WordListDataSaverTask extends AsyncTask<Void, Void, String> {

	private ApiHelper api;
	private DbHelper db;
	private ArrayList<String> data;
	
	public WordListDataSaverTask(ApiHelper a, DbHelper d, ArrayList<String> string) {
		api = a;
		db = d;
		data = string;
	}
	
	@Override
	protected String doInBackground(Void... params) {
		for(int i = 0; i < data.size(); i++) {
			String xml = api.getList(data.get(i));
			Utilities.log("tag", xml);
			try {
				WordList list = new WordListReaderTask(xml).execute().get();
				Utilities.log("list", list.lang_a);
				ObjectContainer cont = db.openDbSession();
				DbModel.saveWordList(cont, list);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

}
