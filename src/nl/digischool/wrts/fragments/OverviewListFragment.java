package nl.digischool.wrts.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.actionbarsherlock.app.SherlockFragment;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import nl.digischool.wrts.R;
import nl.digischool.wrts.adapters.OverviewDrawerListAdapter;
import nl.digischool.wrts.adapters.OverviewListAdapter;
import nl.digischool.wrts.classes.Utilities;
import nl.digischool.wrts.database.DbHelper;
import nl.digischool.wrts.database.DbModel;
import nl.digischool.wrts.objects.WordList;

import java.util.*;

/**
 * Sébastiaanmaakt
 * http://sebastiaanmaakt.nl/
 * Date: 28-6-13
 * Time: 23:03
 */
public class OverviewListFragment extends SherlockFragment {

    protected DbHelper mDb;
    private OverviewListAdapter mAdapter;
    private String mLanguage = null;
    private ListView mListView;
    private final String LOG_TAG = getClass().getSimpleName();

    public void OverviewListFragment() {

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_overview_drawer, group, false);

        mListView = (ListView) v.findViewById(R.id.drawer_list);
        mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        mListView.setOnItemClickListener(mOnItemClickListener);
        refreshList();
        return v;
    }

    public void setLanguage(String language) {
        mLanguage = language;
        if(mLanguage.equalsIgnoreCase("alle talen")) mLanguage = null;
        refreshList();
    }

    public void refreshList() {
        ArrayList<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
        mDb = new DbHelper(getActivity());
        mDb.openDatabase();
        ObjectContainer cont = mDb.openDbSession();
        ObjectSet<WordList> lists = DbModel.getWordListsByLanguage(cont, mLanguage);
        Utilities.log(LOG_TAG, "List size:" + lists.size());
        while(lists.hasNext()) {
            Map<String, Object> map = new HashMap<String, Object>();
            WordList list = lists.next();
            Utilities.log(LOG_TAG, list.title);
            map.put("string", list.title);
            //map.put("count", list.words.size());
            dataList.add(map);
        }
        cont.close();
        mDb.closeDatabase();

        Collections.sort(dataList, new Comparator<Map<String, Object>>() {
            public int compare(Map<String, Object> a, Map<String, Object> b) {
                String strA = (String) a.get("string");
                String strB = (String) b.get("string");
                return strA.compareToIgnoreCase(strB);
            }
        });

        mAdapter = new OverviewListAdapter(getSherlockActivity(), dataList);
        mListView.setAdapter(mAdapter);
    }

    private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        }
    };
}
