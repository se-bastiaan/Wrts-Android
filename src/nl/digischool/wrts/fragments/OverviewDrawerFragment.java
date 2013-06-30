package nl.digischool.wrts.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.db4o.ObjectContainer;
import nl.digischool.wrts.R;
import nl.digischool.wrts.adapters.DrawerListAdapter;
import nl.digischool.wrts.classes.Utilities;
import nl.digischool.wrts.database.DbHelper;
import nl.digischool.wrts.database.DbModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Sébastiaanmaakt
 * http://sebastiaanmaakt.nl/
 * Date: 28-6-13
 * Time: 23:03
 */
public class OverviewDrawerFragment extends SherlockFragment {

    protected DbHelper mDb;
    private DrawerListAdapter mAdapter;
    private ListView mListView;
    private final String LOG_TAG = getClass().getSimpleName();

    public void OverviewDrawerFragment() {
        mDb = new DbHelper(getSherlockActivity());
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_overview_drawer, group, false);

        mListView = (ListView) v.findViewById(R.id.drawer_list);
        mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        refreshList();
        return v;
    }

    public void refreshList() {
        ArrayList<Map<String, Object>> object = new ArrayList<Map<String, Object>>();
        mDb = new DbHelper(getSherlockActivity());
        mDb.openDatabase();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("header", true);
        map.put("text", "Talen");
        object.add(map);
        ObjectContainer cont = mDb.openDbSession();
        List<String> langs = DbModel.getLanguages(cont);
        Utilities.log(LOG_TAG, langs.toString());
        for(int i = 0; i < langs.size(); i++) {
            Utilities.log(LOG_TAG, langs.get(i));
            map = new HashMap<String, Object>();
            map.put("text", langs.get(i));
            object.add(map);
        }
        cont.close();
        mDb.closeDatabase();

        mAdapter = new DrawerListAdapter(getSherlockActivity(), object);
        mListView.setAdapter(mAdapter);
    }

}
