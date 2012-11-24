package nl.sebmaakt.app.wrts_ds.database;

import nl.sebmaakt.app.wrts_ds.database.Database;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.database.sqlite.SQLiteStatement;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class DatabaseProvider extends ContentProvider {

	private Database DB;

    public static final String AUTHORITY = "nl.sebmaakt.app.wrts_ds.database.DatabaseProvider";
    public static final int LISTS = 100;
    public static final int FETCHLANGS = 310;
    public static final int LIST_ID = 110;
    public static final int INSERT_LIST = 120;
    public static final int UPDATE_LIST = 130;
    public static final int BULKINSERT_LISTS = 125;
    public static final int LISTDATA = 300;
    public static final int WORDS = 200;
    public static final int WORD_ID = 210;
    public static final int INSERT_WORD = 220;
    public static final int UPDATE_WORD = 230;
    public static final int BULKINSERT_WORDS = 225;
    public static final int RECREATE = 0;
    public static final int RECREATEUPDATE = 5;

    public static final String LISTS_BASE_PATH = "lists";
    public static final String WORDS_BASE_PATH = "words";
    public static final Uri LISTS_URI = Uri.parse("content://" + AUTHORITY
            + "/" + LISTS_BASE_PATH);
    public static final Uri LISTDATA_URI = Uri.parse("content://" + AUTHORITY
            + "/" + "LISTDATA");
    public static final Uri SAVE_LIST_URI = Uri.parse(LISTS_URI.getPath()+"/save");
    public static final Uri INSERT_WORDS_URI = Uri.parse("content://" + AUTHORITY
            + "/" + "insertwords");
    public static final Uri INSERT_LISTS_URI = Uri.parse("content://" + AUTHORITY
            + "/" + "insertlists");
    public static final Uri WORDS_URI = Uri.parse("content://" + AUTHORITY
            + "/" + WORDS_BASE_PATH);
    public static final Uri FETCHLANGS_URI = Uri.parse("content://" + AUTHORITY
            + "/" + "fetchlangs");    
    public static final Uri RECREATE_URI = Uri.parse("content://" + AUTHORITY
            + "/" + "RECREATEDATABASE");
    public static final Uri RECREATE_UPDATE_URI = Uri.parse("content://" + AUTHORITY
            + "/" + "RECREATEUPDATE");
    
    public static final String LISTS_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/lists";
    public static final String LISTS_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/lists";
    
    public static final String WORDS_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/words";
    public static final String WORDS_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/words";

    private static final UriMatcher sURIMatcher = new UriMatcher(
            UriMatcher.NO_MATCH);
    static {
        sURIMatcher.addURI(AUTHORITY, LISTS_BASE_PATH, LISTS);
        sURIMatcher.addURI(AUTHORITY, LISTS_BASE_PATH + "/#", LIST_ID);
        sURIMatcher.addURI(AUTHORITY, LISTS_BASE_PATH + "/*", LIST_ID);
        sURIMatcher.addURI(AUTHORITY, LISTS_BASE_PATH + "/save", INSERT_LIST);
        sURIMatcher.addURI(AUTHORITY, LISTS_BASE_PATH + "/save/#", UPDATE_LIST);
        sURIMatcher.addURI(AUTHORITY, WORDS_BASE_PATH, WORDS);
        sURIMatcher.addURI(AUTHORITY, WORDS_BASE_PATH + "/#", WORD_ID);
        sURIMatcher.addURI(AUTHORITY, WORDS_BASE_PATH + "/save", INSERT_WORD);
        sURIMatcher.addURI(AUTHORITY, WORDS_BASE_PATH + "/save/#", UPDATE_WORD);
        sURIMatcher.addURI(AUTHORITY, "fetchlangs", FETCHLANGS);
        sURIMatcher.addURI(AUTHORITY, "insertlists", BULKINSERT_LISTS);
        sURIMatcher.addURI(AUTHORITY, "insertwords", BULKINSERT_WORDS);
        sURIMatcher.addURI(AUTHORITY, "RECREATEDATABASE", RECREATE);
        sURIMatcher.addURI(AUTHORITY, "RECREATEUPDATE", RECREATEUPDATE);
        sURIMatcher.addURI(AUTHORITY, "LISTDATA", LISTDATA);
        sURIMatcher.addURI(AUTHORITY, "LISTDATA/#", LISTDATA);
        sURIMatcher.addURI(AUTHORITY, "LISTDATA/*", LISTDATA);
    }
    
    @Override
	public boolean onCreate() {
		DB = new Database(getContext());
		return true;
	}
	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int uriType = sURIMatcher.match(uri);
        SQLiteDatabase db = DB.getWritableDatabase();
        int rowsAffected = 0;
        switch (uriType) {
        case LISTS:
            rowsAffected = db.delete(Database.DATABASE_LISTS,
                    selection, selectionArgs);
            break;
        case WORDS:
            rowsAffected = db.delete(Database.DATABASE_WORDS,
                    selection, selectionArgs);
            break;
        default:
            throw new IllegalArgumentException("Unknown or Invalid URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsAffected;
	}

	@Override
	public String getType(Uri uri) {
		int uriType = sURIMatcher.match(uri);
        switch (uriType) {
        case LISTS:
            return LISTS_TYPE;
        case LIST_ID:
            return LISTS_ITEM_TYPE;
        case WORDS:
            return WORDS_TYPE;
        case WORD_ID:
            return WORDS_ITEM_TYPE;
        default:
            return null;
        }
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase db = DB.getWritableDatabase();
		long newID;
		switch (uriType) {
        case INSERT_LIST:
        	newID = db.insert(Database.DATABASE_LISTS, null, values);
        	break;
        case INSERT_WORD:
        	newID = db.insert(Database.DATABASE_WORDS, null, values);
        	break;
        default:
            throw new IllegalArgumentException("Unknown URI");
        }
        
        if (newID > 0) {
            Uri newUri = ContentUris.withAppendedId(uri, newID);
            getContext().getContentResolver().notifyChange(uri, null);
            return newUri;
        } else {
            throw new SQLException("Failed to insert row into " + uri);
        }
	}
	
	public int bulkInsert(Uri uri, ContentValues[] values) {
		final SQLiteDatabase db = DB.getWritableDatabase();
		int numInserted;
		int uriType = sURIMatcher.match(uri);
		switch(uriType){
		case BULKINSERT_WORDS:
			Log.d("BULKINSERT", "Insert words");
	        numInserted= 0;
			db.beginTransaction();
			try {
	            //standard SQL insert statement, that can be reused
				SQLiteStatement insert = 
					db.compileStatement("insert into words "
							+ "(id, updatedon, worda, wordb, wordc, wordd, worde, wordf, wordg, wordh, wordi, wordj)"
							+ " values " + "(?,?,?,?,?,?,?,?,?,?,?,?);");
				
				for (ContentValues value : values){
	                //bind the 1-indexed ?'s to the values specified
					insert.bindString(1, value.getAsString("id"));
					insert.bindString(2, value.getAsString("updatedon"));
					if(value.getAsString("worda") == null) {
						insert.bindNull(3);
					} else {
						insert.bindString(3, value.getAsString("worda"));
					}
					if(value.getAsString("wordb") == null) {
						insert.bindNull(4);
					} else {
						insert.bindString(4, value.getAsString("wordb"));
					}
					if(value.getAsString("wordc") == null) {
						insert.bindNull(5);
					} else {
						insert.bindString(5, value.getAsString("wordc"));
					}
					if(value.getAsString("wordd") == null) {
						insert.bindNull(6);
					} else {
						insert.bindString(6, value.getAsString("wordd"));
					}
					if(value.getAsString("worde") == null) {
						insert.bindNull(7);
					} else {
						insert.bindString(7, value.getAsString("worde"));
					}
					if(value.getAsString("wordf") == null) {
						insert.bindNull(8);
					} else {
						insert.bindString(8, value.getAsString("wordf"));
					}
					if(value.getAsString("wordg") == null) {
						insert.bindNull(9);
					} else {
						insert.bindString(9, value.getAsString("wordg"));
					}
					if(value.getAsString("wordh") == null) {
						insert.bindNull(10);
					} else {
						insert.bindString(10, value.getAsString("wordh"));
					}
					if(value.getAsString("wordi") == null) {
						insert.bindNull(11);
					} else {
						insert.bindString(11, value.getAsString("wordi"));
					}
					if(value.getAsString("wordj") == null) {
						insert.bindNull(12);
					} else {
						insert.bindString(12, value.getAsString("wordj"));
					}
					insert.execute();
				}
				db.setTransactionSuccessful();
	            numInserted = values.length;
			} finally {
				db.endTransaction();
			}
			return numInserted;
		case BULKINSERT_LISTS:
			Log.d("BULKINSERT", "Insert lists");
	        numInserted= 0;
			db.beginTransaction();
			try {
	            //standard SQL insert statement, that can be reused
				SQLiteStatement insert = 
					db.compileStatement( "insert into lists "
							+ "(id, langa, langb, langc, langd, lange, langf, langg, langh, langi, langj, title, updatedon, wordscount)"
							+ " values " + "(?,?,?,?,?,?,?,?,?,?,?,?,?,?);");
				
				for (ContentValues value : values){
	                //bind the 1-indexed ?'s to the values specified
					insert.bindString(1, value.getAsString("id"));					
					if(value.getAsString("langa") == null) {
						insert.bindString(2, "Unknown language");
					} else {
						insert.bindString(2, value.getAsString("langa"));
					}
					if(value.getAsString("langb") == null) {
						insert.bindString(3, "Unknown language");
					} else {
						insert.bindString(3, value.getAsString("langb"));
					}
					if(value.getAsString("langc") == null) {
						insert.bindNull(4);
					} else {
						insert.bindString(4, value.getAsString("langc"));
					}
					if(value.getAsString("langd") == null) {
						insert.bindNull(5);
					} else {
						insert.bindString(5, value.getAsString("langd"));
					}
					if(value.getAsString("lange") == null) {
						insert.bindNull(6);
					} else {
						insert.bindString(6, value.getAsString("lange"));
					}
					if(value.getAsString("langf") == null) {
						insert.bindNull(7);
					} else {
						insert.bindString(7, value.getAsString("langf"));
					}
					if(value.getAsString("langg") == null) {
						insert.bindNull(8);
					} else {
						insert.bindString(8, value.getAsString("langg"));
					}
					if(value.getAsString("langh") == null) {
						insert.bindNull(9);
					} else {
						insert.bindString(9, value.getAsString("langh"));
					}
					if(value.getAsString("langi") == null) {
						insert.bindNull(10);
					} else {
						insert.bindString(10, value.getAsString("langi"));
					}
					if(value.getAsString("langj") == null) {
						insert.bindNull(11);
					} else {
						insert.bindString(11, value.getAsString("langj"));
					}
					if(value.getAsString("title") == null) {
						insert.bindNull(12);
					} else {
						insert.bindString(12, value.getAsString("title"));
					}
					insert.bindString(13, value.getAsString("updatedon"));
					insert.bindString(14, value.getAsString("wordscount"));
					insert.execute();
				}
				db.setTransactionSuccessful();
	            numInserted = values.length;
			} finally {
				db.endTransaction();
			}
			return numInserted;
		default:
			throw new UnsupportedOperationException("unsupported uri: " + uri);
		}
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {  
		Cursor cursor = null;
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase db = DB.getWritableDatabase();
        switch (uriType) {
        case FETCHLANGS:        	
        	cursor = db.rawQuery("select langa, langb, '1' as _id from lists GROUP BY langa COLLATE NOCASE, langb COLLATE NOCASE", null);
        	//cursor = DB.rawQuery("select distinct langa, langb, '1' as _id from lists", null);
            break;
        case LIST_ID:
        	Log.d("LIST_ID", uri.getLastPathSegment());
        	cursor = db.query("words", null, "id = ?", new String[] { uri.getLastPathSegment() }, null, null, null);
            break;
        case LISTDATA:
        	Log.d("LISTDATA", uri.getLastPathSegment());
        	cursor = db.query("lists", null, "id = ?", new String[] { uri.getLastPathSegment() }, null, null, null);
            break;
        case LISTS:
        	queryBuilder.setTables("lists");
        	cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, "title");        	
            break;
        case WORD_ID:
        	queryBuilder.setTables("words");
        	//cursor = dbAdapter.query("words", columns, selection, selectionArgs, null, null, sortOrder);
            break;
        case WORDS:
        	queryBuilder.setTables("words");
        	//cursor = dbAdapter.query("words", columns, selection, selectionArgs, null, null, sortOrder);
            break;
        case RECREATE:
        	db.execSQL("DROP TABLE IF EXISTS lists;");
    		db.execSQL("create table if not exists lists " + "(_id integer primary key autoincrement, " + " id integer," + "langa text, "  + "langb text," + "langc text, " + "langd text," + "lange text, " + "langf text," + "langg text, " + "langh text," + "langi text, " + "langj text," + " title text," + " updatedon text," + " wordscount integer);");
    		db.execSQL("DROP TABLE IF EXISTS words;");
    		db.execSQL("create table if not exists words " + "(_id integer primary key autoincrement, " + "id integer, " + "langa text, "  + "langb text," + "langc text, " + "langd text," + "lange text, " + "langf text," + "langg text, "  + "langh text," + "langi text, " + "langj text," + "worda text, " + "wordb text, " + "wordc text, " + "wordd text, " + "worde text, " + "wordf text, " + "wordg text, " + "wordh text, " + "wordi text, " + "wordj text, " + " updatedon text);");
    		db.execSQL("DROP TABLE IF EXISTS updated;");
    		db.execSQL("create table if not exists updated (_id integer primary key autoincrement, id integer);");
    		db.execSQL("DROP TABLE IF EXISTS created;");
    		db.execSQL("create table if not exists created (_id integer primary key autoincrement, id integer);");
    		db.execSQL("DROP TABLE IF EXISTS deleted;");
    		db.execSQL("create table if not exists deleted (_id integer primary key autoincrement, id integer);");
            break;
        case RECREATEUPDATE:
        	db.execSQL("DROP TABLE IF EXISTS updated;");
    		db.execSQL("create table if not exists updated (_id integer primary key autoincrement, id integer);");
    		db.execSQL("DROP TABLE IF EXISTS created;");
    		db.execSQL("create table if not exists created (_id integer primary key autoincrement, id integer);");
    		db.execSQL("DROP TABLE IF EXISTS deleted;");
    		db.execSQL("create table if not exists deleted (_id integer primary key autoincrement, id integer);");
        	break;
        default:
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        if(cursor != null) {
        	if(cursor.getCount() > 0) {
        		cursor.moveToFirst();
        	}
        }
        return cursor;
	}
	
	

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		int uriType = sURIMatcher.match(uri);
        SQLiteDatabase db = DB.getWritableDatabase();
        String id;
        StringBuilder modSelection;
        int rowsAffected;

        switch (uriType) {
        case UPDATE_LIST:
            id = uri.getLastPathSegment();
            modSelection = new StringBuilder("_id" + "=" + id);

            if (!TextUtils.isEmpty(selection)) {
                modSelection.append(" AND " + selection);
            }

            rowsAffected = db.update(Database.DATABASE_LISTS,
                    values, modSelection.toString(), null);
            break;
        case UPDATE_WORD:
        	id = uri.getLastPathSegment();
            modSelection = new StringBuilder("_id" + "=" + id);

            if (!TextUtils.isEmpty(selection)) {
                modSelection.append(" AND " + selection);
            }
            
            rowsAffected = db.update(Database.DATABASE_WORDS,
                    values, selection, selectionArgs);
            break;
        default:
            throw new IllegalArgumentException("Unknown URI");
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsAffected;
	}

}
