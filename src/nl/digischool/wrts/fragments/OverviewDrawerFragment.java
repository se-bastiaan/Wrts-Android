package nl.digischool.wrts.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.actionbarsherlock.app.SherlockFragment;
import com.db4o.ObjectContainer;
import nl.digischool.wrts.activities.OverviewActivity;
import nl.digischool.wrts.R;
import nl.digischool.wrts.adapters.OverviewDrawerListAdapter;
import nl.digischool.wrts.database.DbHelper;
import nl.digischool.wrts.database.DbModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.widget.AdapterView.*;

/**
 * Sébastiaanmaakt
 * http://sebastiaanmaakt.nl/
 * Date: 28-6-13
 * Time: 23:03
 */
public class OverviewDrawerFragment extends SherlockFragment {

    protected DbHelper mDb;
    private ListView mListView;
    //private final String LOG_TAG = getClass().getSimpleName();

    public OverviewDrawerFragment() {

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_overview_list, group, false);

        mListView = (ListView) v.findViewById(R.id.drawer_list);
        mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        mListView.setOnItemClickListener(mOnItemClickListener);
        refreshList();
        return v;
    }

    public void refreshList() {
        ArrayList<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
        mDb = new DbHelper(getActivity());
        mDb.openDatabase();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("header", true);
        map.put("string", "Talen");
        dataList.add(map);
        map = new HashMap<String, Object>();
        map.put("string", "Alle talen");
        dataList.add(map);
        ObjectContainer cont = mDb.openDbSession();
        List<Map<String, Object>> langs = DbModel.getLanguages(cont);
        dataList.addAll(langs);
        cont.close();
        mDb.closeDatabase();

        OverviewDrawerListAdapter mAdapter = new OverviewDrawerListAdapter(getSherlockActivity(), dataList);
        mListView.setAdapter(mAdapter);
    }

    private OnItemClickListener mOnItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Object item = parent.getAdapter().getItem(position);
            if(item instanceof HashMap) {
                HashMap<String, Object> data = (HashMap<String, Object>) item;
                if(!data.containsKey("header")) setOverviewLanguage(data.get("string").toString());
            }
        }
    };

    private void setOverviewLanguage(String language) {
        ((OverviewActivity) getSherlockActivity()).setOverviewLanguage(language);
    }
}
