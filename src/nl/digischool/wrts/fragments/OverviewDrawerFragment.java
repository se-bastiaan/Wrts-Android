package nl.digischool.wrts.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.actionbarsherlock.app.SherlockFragment;
import de.greenrobot.dao.query.LazyList;
import nl.digischool.wrts.R;
import nl.digischool.wrts.activities.BaseActivity;
import nl.digischool.wrts.activities.OverviewActivity;
import nl.digischool.wrts.adapters.OverviewDrawerListAdapter;
import nl.digischool.wrts.classes.Utilities;
import nl.digischool.wrts.database.DaoMaster;
import nl.digischool.wrts.database.DaoSession;
import nl.digischool.wrts.database.WordList;
import nl.digischool.wrts.database.WordListDao;

import java.util.*;

import static android.widget.AdapterView.OnItemClickListener;

/**
 * Sébastiaanmaakt
 * http://sebastiaanmaakt.nl/
 * Date: 28-6-13
 * Time: 23:03
 */
public class OverviewDrawerFragment extends SherlockFragment {

    private ListView mListView;
    //private final String LOG_TAG = getClass().getSimpleName();

    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_overview_list, group, false);

        mListView = (ListView) v.findViewById(R.id.drawer_list);
        mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        mListView.setOnItemClickListener(mOnItemClickListener);
        refreshList();
        return v;
    }

    public void refreshList() {
        DaoMaster daoMaster = ((BaseActivity)getActivity()).mDaoMaster;
        DaoSession session = daoMaster.newSession();
        WordListDao wordListDao = session.getWordListDao();

        ArrayList<String> stringsList = new ArrayList<String>();
        TreeSet<String> stringSet = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
        TreeSet<String> strings = new TreeSet<String>();
        LazyList<WordList> lazyList = wordListDao.queryBuilder().listLazyUncached();

        for(WordList list : lazyList) {
            strings = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);

            if(list.getLang_a() != null && !list.getLang_a().isEmpty())
                strings.add(list.getLang_a());

            if(list.getLang_b() != null && !list.getLang_b().isEmpty())
                strings.add(list.getLang_b());

            if(list.getLang_c() != null && !list.getLang_c().isEmpty())
                strings.add(list.getLang_c());

            if(list.getLang_d() != null && !list.getLang_d().isEmpty())
                strings.add(list.getLang_d());

            if(list.getLang_e() != null && !list.getLang_e().isEmpty())
                strings.add(list.getLang_e());

            if(list.getLang_f() != null && !list.getLang_f().isEmpty())
                strings.add(list.getLang_f());

            if(list.getLang_g() != null && !list.getLang_g().isEmpty())
                strings.add(list.getLang_g());

            if(list.getLang_h() != null && !list.getLang_h().isEmpty())
                strings.add(list.getLang_h());

            if(list.getLang_i() != null && !list.getLang_i().isEmpty())
                strings.add(list.getLang_i());

            if(list.getLang_j() != null && !list.getLang_j().isEmpty())
                strings.add(list.getLang_j());

            stringsList.addAll(strings);
            stringSet.addAll(strings);
            strings.clear();
        }

        lazyList.close();

        ArrayList<String> setList = new ArrayList<String>();
        setList.addAll(stringSet);
        stringSet.clear();

        ArrayList<Map<String, Object>> conversionList = new ArrayList<Map<String, Object>>();

        for(int i = 0; i < setList.size(); i++) {
            int count = 0;
            for(String item : stringsList) {
                count += (item.equalsIgnoreCase(setList.get(i)) ? 1 : 0);
            }
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("string", Utilities.uppercaseOnlyFirst(setList.get(i)));
            map.put("count", count);
            conversionList.add(map);
        }

        stringsList.clear();
        setList.clear();

        Collections.sort(conversionList, new Comparator<Map<String, Object>>() {
            public int compare(Map<String, Object> a, Map<String, Object> b) {
                Integer countA = (Integer) a.get("count");
                Integer countB = (Integer) b.get("count");
                String strA = (String) a.get("string");
                String strB = (String) b.get("string");
                if(countA > countB) {
                    return -1;
                }
                if(countA < countB) {
                    return 1;
                }
                return strA.compareToIgnoreCase(strB);
            }
        });

        ArrayList<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("header", true);
        map.put("string", "Talen");
        dataList.add(map);
        map = new HashMap<String, Object>();
        map.put("string", "Alle talen");
        dataList.add(map);
        dataList.addAll(conversionList);
        conversionList.clear();

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
