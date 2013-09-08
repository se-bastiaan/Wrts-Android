package nl.digischool.wrts.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.actionbarsherlock.app.SherlockFragment;
import de.greenrobot.dao.query.CloseableListIterator;
import de.greenrobot.dao.query.QueryBuilder;
import de.greenrobot.dao.query.WhereCondition;
import nl.digischool.wrts.R;
import nl.digischool.wrts.activities.BaseActivity;
import nl.digischool.wrts.activities.ListDetailActivity;
import nl.digischool.wrts.adapters.OverviewListAdapter;
import nl.digischool.wrts.classes.Utilities;
import nl.digischool.wrts.database.DaoMaster;
import nl.digischool.wrts.database.DaoSession;
import nl.digischool.wrts.database.WordList;
import nl.digischool.wrts.database.WordListDao;

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
        DaoMaster daoMaster = ((BaseActivity)getActivity()).mDaoMaster;
        DaoSession session = daoMaster.newSession();
        WordListDao wordListDao = session.getWordListDao();

        ArrayList<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
        List<WordList> listsList;
        QueryBuilder qb = wordListDao.queryBuilder();
        if(mLanguage != null) {
            String uLanguage = mLanguage.toUpperCase();
            String whereString = "";
            String abc = "ABCDEFGHIJ";
            for(int i = 0; i < 10; i++) {
                char lang = abc.charAt(i);
                whereString += "UPPER(LANG_"+lang+") = '"+uLanguage+"'";

                if(i != 9) whereString += " OR ";
            }
            listsList = qb.where(new WhereCondition.StringCondition(whereString)).orderAsc(WordListDao.Properties.Title).list();
        } else {
            listsList = qb.orderAsc(WordListDao.Properties.Title).list();
        }

        for(WordList list : listsList) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("string", list.getTitle());
            map.put("id", list.getId());
            dataList.add(map);
        }

        mAdapter = new OverviewListAdapter(getSherlockActivity(), dataList);
        mListView.setAdapter(mAdapter);

    }

    private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Map<String, Object> map = mAdapter.getItem(position);
            Long listId = (Long) map.get("id");
            Intent intent = new Intent(getActivity(), ListDetailActivity.class);
            intent.putExtra("id", listId);
            startActivity(intent);

        }
    };
}
