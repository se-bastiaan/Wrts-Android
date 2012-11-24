package nl.sebmaakt.app.wrts_ds.database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Database extends SQLiteOpenHelper {
	// Database creation SQL statement
	public static String DATABASE_LISTS = "lists";
	public static String DATABASE_WORDS = "words";
	private static final int DB_VERSION = 2;
	private static final String DB_NAME = "WrtsApp";
	
	private static final String DATABASE_CREATE = "create table if not exists lists "
			+ "(_id integer primary key autoincrement, "
			+ " id integer,"
			+ "langa text, " 
			+ "langb text,"
			+ "langc text, " 
			+ "langd text,"
			+ "lange text, " 
			+ "langf text,"
			+ "langg text, " 
			+ "langh text,"
			+ "langi text, " 
			+ "langj text,"
			+ " title text,"
			+ " updatedon text,"
			+ " wordscount integer);";
	private static final String DATABASE_CREATE1 = "create table if not exists words "
				+ "(_id integer primary key autoincrement, "
				+ "id integer, " 				
				+ "worda text, "
				+ "wordb text, "
				+ "wordc text, "
				+ "wordd text, "
				+ "worde text, "
				+ "wordf text, "
				+ "wordg text, "
				+ "wordh text, "
				+ "wordi text, "
				+ "wordj text, "
				+ " updatedon text);";

	public Database(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
	
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
		database.execSQL(DATABASE_CREATE1);
		database.execSQL("create table if not exists updated (_id integer primary key autoincrement, id integer);");
		database.execSQL("create table if not exists created (_id integer primary key autoincrement, id integer);");
		database.execSQL("create table if not exists deleted (_id integer primary key autoincrement, id integer);");
	}

	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		Log.w("DEBUG_DB", "Upgrading database. Existing contents will be lost. ["
	            + oldVersion + "]->[" + newVersion + "]");
		if(oldVersion == 1) {
			Cursor c = database.query("words", new String[]{"_id", "id", "worda","wordb", "wordc", "wordd", "worde", "wordf", "wordg", "wordh", "wordi", "wordj"}, null, null, null, null, null);
			ArrayList<ContentValues> values = new ArrayList<ContentValues>();

			if(c.moveToFirst()) {       
			   do {
			        ContentValues map = new ContentValues();
			        DatabaseUtils.cursorRowToContentValues(c, map);                 
			        values.add(map);
			    } while(c.moveToNext());
			}

			c.close(); 
			database.delete("words", null, null);
			database.execSQL(DATABASE_CREATE1);
			for(ContentValues value : values) {
				database.insert("words", null, value);
			}			
		} else {
			database.delete("lists", null, null);
			database.delete("words", null, null);
			database.delete("updated", null, null);
			database.delete("created", null, null);
			database.delete("deleted", null, null);		
			onCreate(database);
		}
	}
}