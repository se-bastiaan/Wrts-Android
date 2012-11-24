package nl.sebmaakt.app.wrts_ds.database;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import nl.sebmaakt.app.wrts_ds.database.Database;

public class DbAdapter {

	// Database fields
	private Context context;
	private SQLiteDatabase db;
	private Database dbHelper;

	public DbAdapter(Context context) {		
		this.context = context;
	}

	public DbAdapter open() throws SQLException {
		dbHelper = new Database(context);
		db = dbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		dbHelper.close();
	}
	
	public ArrayList<String> fetchStatus(int type) throws SQLException {
		String dbname = "deleted";
		Cursor mCursor = null;
		ArrayList<String> list = new ArrayList<String>();		
		if(type == 1) {
			dbname = "deleted";
			db.execSQL("create table if not exists "+dbname+" (_id integer primary key autoincrement, id integer);");
			mCursor = db.query(dbname, null, null, null, null, null, null);
		} else if(type == 2) {
			dbname = "updated";
			db.execSQL("create table if not exists "+dbname+" (_id integer primary key autoincrement, id integer);");
			mCursor = db.rawQuery("select distinct id, '1' as _id from updated", null);
		} else if(type == 3) {
			dbname = "created";
			db.execSQL("create table if not exists "+dbname+" (_id integer primary key autoincrement, id integer);");
			mCursor = db.query(dbname, null, null, null, null, null, null);
		}
		//mCursor = db.rawQuery("select * from lists where langa = ? and langb = ?", new String[] { langa, langb });
		if (mCursor != null) {
			mCursor.moveToFirst();
		
			for(int i = 0; i < mCursor.getCount(); i++) {
				if(type == 2) {
					if(!mCursor.getString(mCursor.getColumnIndex("id")).startsWith("new")) {
						list.add(mCursor.getString(mCursor.getColumnIndex("id")));
						if(!mCursor.isLast()) {
							mCursor.moveToNext();
						}
					}
				} else {
					list.add(mCursor.getString(mCursor.getColumnIndex("id")));
					if(!mCursor.isLast()) {
						mCursor.moveToNext();
					}
				}
			}
			mCursor.close();
			return list;
		} else {
			return null;
		}		
	}
	
	public Cursor fetchListData(String id) throws SQLException {
		Cursor mCursor;
	    //  Return the full list
		mCursor = db.query("lists", null, "id = ?", new String[] { id }, null, null, null);
		//mCursor = db.rawQuery("select * from words where id = ?",new String[] { id });
		if (mCursor != null) {
			mCursor.moveToFirst();			
		} else {
			return null;
		}
		return mCursor;
	}
	
	public Cursor fetchList(String id) throws SQLException {
		Cursor mCursor;
	    //  Return the full list
		mCursor = db.query("words", null, "id = ?", new String[] { id }, null, null, null);
		//mCursor = db.rawQuery("select * from words where id = ?",new String[] { id });
		if (mCursor != null) {
			mCursor.moveToFirst();			
		} else {
			return null;
		}
		return mCursor;
	}
	
	public Cursor fetchWord(String listid, String _id) throws SQLException {
		Cursor mCursor;
	    //  Return the full list
		mCursor = db.query("words", null, "id = ? and _id = ?", new String[] { listid, _id }, null, null, null);
		//mCursor = db.rawQuery("select * from words where id = ?",new String[] { id });
		if (mCursor != null) {
			mCursor.moveToFirst();			
		}
		return mCursor;   
	}
	
	public Cursor fetchAnswers(String id, String language, String word) throws SQLException {
		Cursor mCursor;
	    //  Return the full list
		mCursor = db.query("words", new String[]{"worda","wordb","wordc","wordd","worde","wordf","wordg","wordh","wordi","wordj"}, language+" = ?" + " and id = ?", new String[]{word, id},  null, null, null);

		//mCursor = db.rawQuery("select worda, wordb, wordc, wordd, worde, wordf, wordg, wordh, wordi, wordj from words where id = '"+id+"' and "+language+" = '"+word+"'", null);
		if (mCursor != null) {
			mCursor.moveToFirst();			
		}
		return mCursor;   
	}
	
	public String fetchAnswer(String id, String language, String word, String word2) throws SQLException {
		Cursor mCursor;
	    //  Return the full list
		mCursor = db.query("words", new String[]{"worda","wordb","wordc","wordd","worde","wordf","wordg","wordh","wordi","wordj"}, language+" = ?" + " and id = ?", new String[]{word, id},  null, null, null);

		//mCursor = db.rawQuery("select worda, wordb, wordc, wordd, worde, wordf, wordg, wordh, wordi, wordj from words where id = '"+id+"' and "+language+" = '"+word+"'", null);
		if (mCursor != null) {
			mCursor.moveToFirst();			
		}
		String ans = mCursor.getString(mCursor.getColumnIndex(word2));
		mCursor.close();
		return ans;   
	}
	
	public Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
		Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
		if (cursor != null) {
			cursor.moveToFirst();			
		}
		return cursor;   
	}

	public void insertList(String listid, String title, String langa, String langb, String langc, String langd, String lange, String langf, String langg, String langh, String langi, String langj, String updatedon, String wordscount) {
			ContentValues listValues = new ContentValues();
			listValues.put("id", listid);
			listValues.put("title", title);
			listValues.put("langa", langa);
			listValues.put("langb", langb);
			listValues.put("langc", langc);
			listValues.put("langd", langd);
			listValues.put("lange", lange);
			listValues.put("langf", langf);
			listValues.put("langg", langg);
			listValues.put("langh", langh);
			listValues.put("langi", langi);
			listValues.put("langj", langj);
			listValues.put("updatedon", updatedon);
			listValues.put("wordscount", wordscount);
			db.insert("lists", null, listValues);
	}
	
	public void updateList(String listid, String title, String langa, String langb, String langc, String langd, String lange, String langf, String langg, String langh, String langi, String langj) {
		ContentValues listValues = new ContentValues();
		ContentValues titleValue = new ContentValues();
		titleValue.put("title", title);
		listValues.put("langa", langa);
		listValues.put("langb", langb);
		listValues.put("langc", langc);
		listValues.put("langd", langd);
		listValues.put("lange", lange);
		listValues.put("langf", langf);
		listValues.put("langg", langg);
		listValues.put("langh", langh);
		listValues.put("langi", langi);
		listValues.put("langj", langj);
		Format format = new SimpleDateFormat("yyyy-MM-dd");
		Format format2 = new SimpleDateFormat("HH:mm:ssZ");
		Date date = new Date();
		listValues.put("updatedon", format.format(date)+"T"+format2.format(date));
		Log.d("tag", format.format(date));
		db.update("lists", titleValue, "id = ?", new String[] {listid});
		db.update("lists", listValues, "id = ?", new String[] {listid});
		ContentValues id = new ContentValues();
		id.put("id", listid);
		db.insert("updated", null, id);
	}
	
	public String newList(String title, String langa, String langb, String langc, String langd, String lange, String langf, String langg, String langh, String langi, String langj) {
		Cursor mCursor;
		ContentValues listValues = new ContentValues();
		listValues.put("title", title);
		listValues.put("langa", langa);
		listValues.put("langb", langb);
		listValues.put("langc", langc);
		listValues.put("langd", langd);
		listValues.put("lange", lange);
		listValues.put("langf", langf);
		listValues.put("langg", langg);
		listValues.put("langh", langh);
		listValues.put("langi", langi);
		listValues.put("langj", langj);
		Format format = new SimpleDateFormat("yyyy-MM-dd");
		Format format2 = new SimpleDateFormat("HH:mm:ssZ");
		Date date = new Date();
		listValues.put("updatedon", format.format(date)+"T"+format2.format(date));
		//Log.d("tag", format.format(date));
		db.insert("lists", null, listValues);
		mCursor = db.query("lists", new String[]{"_id"}, null, null, null, null, "_id desc", "1");
		if (mCursor != null) {
			mCursor.moveToFirst();			
		}
		String _id = mCursor.getString(mCursor.getColumnIndex("_id"));
		db.execSQL("create table if not exists created (_id integer primary key autoincrement, id integer);");
		ContentValues Values = new ContentValues();
		Values.put("id", "new"+_id);
		db.update("lists", Values, "_id=?", new String[] { _id });
		db.insert("created", null, Values);
		mCursor.close();
		return "new"+_id;		
	}
	
	public void insertWords(String listid, String langa, String langb, String langc, String langd, String lange, String langf, String langg, String langh, String langi, String langj, String updatedon, String wordsa, String wordsb, String wordsc, String wordsd, String wordse, String wordsf, String wordsg, String wordsh, String wordsi, String wordsj) {
		ContentValues listValues = new ContentValues();
		listValues.put("id", listid);
		listValues.put("updatedon", updatedon);
		listValues.put("worda", wordsa);
		listValues.put("wordb", wordsb);
		listValues.put("wordc", wordsc);
		listValues.put("wordd", wordsd);
		listValues.put("worde", wordse);
		listValues.put("wordf", wordsf);
		listValues.put("wordg", wordsg);
		listValues.put("wordh", wordsh);
		listValues.put("wordi", wordsi);
		listValues.put("wordj", wordsj);
		db.insert("words", null, listValues);
	}
	
	public void updateWord(String _id, String listid, String wordsa, String wordsb, String wordsc, String wordsd, String wordse, String wordsf, String wordsg, String wordsh, String wordsi, String wordsj) {
		ContentValues listValues = new ContentValues();
		ContentValues updateValues = new ContentValues();
		Format format = new SimpleDateFormat("yyyy-MM-dd");
		Format format2 = new SimpleDateFormat("HH:mm:ssZ");
		Date date = new Date();
		updateValues.put("updatedon", format.format(date)+"T"+format2.format(date));
		listValues.put("worda", wordsa);
		listValues.put("wordb", wordsb);
		listValues.put("wordc", wordsc);
		listValues.put("wordd", wordsd);
		listValues.put("worde", wordse);
		listValues.put("wordf", wordsf);
		listValues.put("wordg", wordsg);
		listValues.put("wordh", wordsh);
		listValues.put("wordi", wordsi);
		listValues.put("wordj", wordsj);
		db.update("lists", updateValues, "id = ?", new String[] {listid});
		db.update("words", updateValues, "id = ?", new String[] {listid});
		db.update("words", listValues, "id = ? and _id = ?", new String[] {listid,_id});
		db.execSQL("create table if not exists updated (_id integer primary key autoincrement, id integer);");
		ContentValues id = new ContentValues();
		id.put("id", listid);
		db.insert("updated", null, id);
	}
	
	public void newWord(String listid, String langa, String langb, String langc, String langd, String lange, String langf, String langg, String langh, String langi, String langj, String wordsa, String wordsb, String wordsc, String wordsd, String wordse, String wordsf, String wordsg, String wordsh, String wordsi, String wordsj) {
		ContentValues listValues = new ContentValues();
		ContentValues updateValues = new ContentValues();
		Format format = new SimpleDateFormat("yyyy-MM-dd");
		Format format2 = new SimpleDateFormat("HH:mm:ssZ");
		Date date = new Date();
		updateValues.put("updatedon", format.format(date)+"T"+format2.format(date));
		listValues.put("id", listid);
		listValues.put("worda", wordsa);
		listValues.put("wordb", wordsb);
		listValues.put("wordc", wordsc);
		listValues.put("wordd", wordsd);
		listValues.put("worde", wordse);
		listValues.put("wordf", wordsf);
		listValues.put("wordg", wordsg);
		listValues.put("wordh", wordsh);
		listValues.put("wordi", wordsi);
		listValues.put("wordj", wordsj);
		db.update("lists", updateValues, "id = ?", new String[] {listid});
		db.update("words", updateValues, "id = ?", new String[] {listid});
		db.insert("words", null, listValues);
		db.execSQL("create table if not exists updated (_id integer primary key autoincrement, id integer);");
		ContentValues id = new ContentValues();
		id.put("id", listid);
		db.insert("updated", null, id);
	}
	
	public void deleteWord(String listid, String id) {
		db.delete("words", "_id = ? and id = ?", new String[] { id, listid });
		db.execSQL("create table if not exists updated (_id integer primary key autoincrement, id integer);");
		ContentValues ids = new ContentValues();
		ids.put("id", listid);
		db.insert("updated", null, ids);
	}
	
	public void deleteList(String listid) {
			db.delete("lists", "id = ?", new String[] { listid });
			try {
				if(!listid.startsWith("new")) {
					db.delete("created", "id = ?", new String[] { listid });
				}
				db.delete("updated", "id = ?", new String[] { listid });
			} catch(Exception e) {
				e.printStackTrace();
			}
			db.execSQL("create table if not exists deleted (_id integer primary key autoincrement, id integer);");
			ContentValues ids = new ContentValues();
			ids.put("id", listid);
			db.insert("deleted", null, ids);
	}
}