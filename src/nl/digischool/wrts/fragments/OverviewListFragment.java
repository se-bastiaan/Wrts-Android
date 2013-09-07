package nl.digischool.wrts.fragments;

import android.content.Intent;
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
import nl.digischool.wrts.activities.ListDetailActivity;
import nl.digischool.wrts.adapters.OverviewListAdapter;
import nl.digischool.wrts.classes.Utilities;

import java.util.*;

/**
 * Sébastiaanmaakt
 * http://sebastiaanmaakt.nl/
 * Date: 28-6-13
 * Time: 23:03
 */
public class OverviewListFragment extends SherlockFragment {

    private OverviewListAdapter mAdapter;
    private String mLanguage = null;
    private ListView mListView;
    private final String LOG_TAG = getClass().getSimpleName();

    public OverviewListFragment() {

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.activity_overview_list, group, false);

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
        // TODO: Refresh list using GreenDAO (new orm)
        /*ArrayList<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
        mDb = new DbHelper(getActivity());
        mDb.openDatabase();
        ObjectContainer cont = mDb.openDbSession();
        ObjectSet<WordList> lists = DbModel.getWordListsByLanguage(cont, mLanguage);
        Utilities.log(LOG_TAG, "List size:" + lists.size());
        while(lists.hasNext()) {
            Map<String, Object> map = new HashMap<String, Object>();
            WordList list = lists.next();
            map.put("string", list.title);
            map.put("id", list.id);
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
        mListView.setAdapter(mAdapter);*/

    }

    private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Map<String, Object> map = mAdapter.getItem(position);
            Integer listId = Integer.parseInt((String)map.get("id"));
            Intent intent = new Intent(getActivity(), ListDetailActivity.class);
            intent.putExtra("id", listId);
            startActivity(intent);

        }
    };
}
