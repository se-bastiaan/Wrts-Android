package nl.digischool.wrts;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import nl.digischool.wrts.classes.Params;
import nl.digischool.wrts.dao.DaoMaster;

/**
 * Sébastiaanmaakt
 * http://sebastiaanmaakt.nl/
 * Date: 7-9-13
 * Time: 15:34
 */
public class WrtsApplication extends Application {

    private SQLiteDatabase mDb;
    private DaoMaster mDaoMaster;

    public void onCreate() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, Params.databaseName, null);
        mDb = helper.getWritableDatabase();
        mDaoMaster = new DaoMaster(mDb);
    }

    public SQLiteDatabase getDatabase() {
        return mDb;
    }

    public DaoMaster getDaoMaster() {
        return mDaoMaster;
    }

}
