package nl.digischool.wrts.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;
import de.greenrobot.dao.internal.SqlUtils;
import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table WORD.
*/
public class WordDao extends AbstractDao<Word, Long> {

    public static final String TABLENAME = "WORD";

    /**
     * Properties of entity Word.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property List_id = new Property(1, long.class, "list_id", false, "LIST_ID");
        public final static Property Word_a = new Property(2, String.class, "word_a", false, "WORD_A");
        public final static Property Word_b = new Property(3, String.class, "word_b", false, "WORD_B");
        public final static Property Word_c = new Property(4, String.class, "word_c", false, "WORD_C");
        public final static Property Word_d = new Property(5, String.class, "word_d", false, "WORD_D");
        public final static Property Word_e = new Property(6, String.class, "word_e", false, "WORD_E");
        public final static Property Word_f = new Property(7, String.class, "word_f", false, "WORD_F");
        public final static Property Word_g = new Property(8, String.class, "word_g", false, "WORD_G");
        public final static Property Word_h = new Property(9, String.class, "word_h", false, "WORD_H");
        public final static Property Word_i = new Property(10, String.class, "word_i", false, "WORD_I");
        public final static Property Word_j = new Property(11, String.class, "word_j", false, "WORD_J");
    };

    private DaoSession daoSession;

    private Query<Word> wordList_WordsQuery;

    public WordDao(DaoConfig config) {
        super(config);
    }
    
    public WordDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'WORD' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'LIST_ID' INTEGER NOT NULL ," + // 1: list_id
                "'WORD_A' TEXT," + // 2: word_a
                "'WORD_B' TEXT," + // 3: word_b
                "'WORD_C' TEXT," + // 4: word_c
                "'WORD_D' TEXT," + // 5: word_d
                "'WORD_E' TEXT," + // 6: word_e
                "'WORD_F' TEXT," + // 7: word_f
                "'WORD_G' TEXT," + // 8: word_g
                "'WORD_H' TEXT," + // 9: word_h
                "'WORD_I' TEXT," + // 10: word_i
                "'WORD_J' TEXT);"); // 11: word_j
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'WORD'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Word entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getList_id());
 
        String word_a = entity.getWord_a();
        if (word_a != null) {
            stmt.bindString(3, word_a);
        }
 
        String word_b = entity.getWord_b();
        if (word_b != null) {
            stmt.bindString(4, word_b);
        }
 
        String word_c = entity.getWord_c();
        if (word_c != null) {
            stmt.bindString(5, word_c);
        }
 
        String word_d = entity.getWord_d();
        if (word_d != null) {
            stmt.bindString(6, word_d);
        }
 
        String word_e = entity.getWord_e();
        if (word_e != null) {
            stmt.bindString(7, word_e);
        }
 
        String word_f = entity.getWord_f();
        if (word_f != null) {
            stmt.bindString(8, word_f);
        }
 
        String word_g = entity.getWord_g();
        if (word_g != null) {
            stmt.bindString(9, word_g);
        }
 
        String word_h = entity.getWord_h();
        if (word_h != null) {
            stmt.bindString(10, word_h);
        }
 
        String word_i = entity.getWord_i();
        if (word_i != null) {
            stmt.bindString(11, word_i);
        }
 
        String word_j = entity.getWord_j();
        if (word_j != null) {
            stmt.bindString(12, word_j);
        }
    }

    @Override
    protected void attachEntity(Word entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Word readEntity(Cursor cursor, int offset) {
        Word entity = new Word( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getLong(offset + 1), // list_id
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // word_a
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // word_b
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // word_c
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // word_d
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // word_e
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // word_f
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // word_g
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // word_h
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // word_i
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11) // word_j
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Word entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setList_id(cursor.getLong(offset + 1));
        entity.setWord_a(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setWord_b(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setWord_c(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setWord_d(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setWord_e(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setWord_f(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setWord_g(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setWord_h(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setWord_i(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setWord_j(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Word entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Word entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
    /** Internal query to resolve the "words" to-many relationship of WordList. */
    public List<Word> _queryWordList_Words(long list_id) {
        synchronized (this) {
            if (wordList_WordsQuery == null) {
                QueryBuilder<Word> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.List_id.eq(null));
                wordList_WordsQuery = queryBuilder.build();
            }
        }
        Query<Word> query = wordList_WordsQuery.forCurrentThread();
        query.setParameter(0, list_id);
        return query.list();
    }

    private String selectDeep;

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getWordListDao().getAllColumns());
            builder.append(" FROM WORD T");
            builder.append(" LEFT JOIN WORD_LIST T0 ON T.'LIST_ID'=T0.'_id'");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected Word loadCurrentDeep(Cursor cursor, boolean lock) {
        Word entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        WordList wordList = loadCurrentOther(daoSession.getWordListDao(), cursor, offset);
         if(wordList != null) {
            entity.setWordList(wordList);
        }

        return entity;    
    }

    public Word loadDeep(Long key) {
        assertSinglePk();
        if (key == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder(getSelectDeep());
        builder.append("WHERE ");
        SqlUtils.appendColumnsEqValue(builder, "T", getPkColumns());
        String sql = builder.toString();
        
        String[] keyArray = new String[] { key.toString() };
        Cursor cursor = db.rawQuery(sql, keyArray);
        
        try {
            boolean available = cursor.moveToFirst();
            if (!available) {
                return null;
            } else if (!cursor.isLast()) {
                throw new IllegalStateException("Expected unique result, but count was " + cursor.getCount());
            }
            return loadCurrentDeep(cursor, true);
        } finally {
            cursor.close();
        }
    }
    
    /** Reads all available rows from the given cursor and returns a list of new ImageTO objects. */
    public List<Word> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<Word> list = new ArrayList<Word>(count);
        
        if (cursor.moveToFirst()) {
            if (identityScope != null) {
                identityScope.lock();
                identityScope.reserveRoom(count);
            }
            try {
                do {
                    list.add(loadCurrentDeep(cursor, false));
                } while (cursor.moveToNext());
            } finally {
                if (identityScope != null) {
                    identityScope.unlock();
                }
            }
        }
        return list;
    }
    
    protected List<Word> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<Word> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}
