package nl.digischool.wrts.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Checkable;
import android.widget.ListView;
import android.widget.Toast;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import de.greenrobot.dao.query.QueryBuilder;
import de.greenrobot.dao.query.WhereCondition;
import nl.digischool.wrts.R;
import nl.digischool.wrts.activities.BaseActivity;
import nl.digischool.wrts.activities.ListDetailActivity;
import nl.digischool.wrts.activities.OverviewActivity;
import nl.digischool.wrts.adapters.OverviewListAdapter;
import nl.digischool.wrts.database.DaoMaster;
import nl.digischool.wrts.database.DaoSession;
import nl.digischool.wrts.database.WordList;
import nl.digischool.wrts.database.WordListDao;
import nl.digischool.wrts.views.CheckableRelativeLayout;

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
public class OverviewListFragment extends SherlockFragment {

    private OverviewListAdapter mAdapter;
    private String mLanguage = null;
    private ListView mListView;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;
    private WordListDao mWordListDao;
    private ActionMode actionMode = null;
    private OverviewActivity mActivity;
    //private final String LOG_TAG = this.getClass().getSimpleName();

    public OverviewListFragment() {

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_overview_list, group, false);

        mListView = (ListView) v.findViewById(R.id.drawer_list);
        mListView.setOnItemClickListener(mOnItemClickListener);
        mListView.setOnItemLongClickListener(mOnItemLongClickListener);
        mListView.setChoiceMode(ListView.CHOICE_MODE_NONE);
        refreshList();

        mActivity = (OverviewActivity) getSherlockActivity();

        return v;
    }

    public void setLanguage(String language) {
        mLanguage = language;
        if (mLanguage.equalsIgnoreCase("alle talen")) mLanguage = null;
        refreshList();
    }

    public void refreshList() {
        mDaoMaster = ((BaseActivity) getActivity()).mDaoMaster;
        mDaoSession = mDaoMaster.newSession();
        mWordListDao = mDaoSession.getWordListDao();

        ArrayList<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
        List<WordList> listsList;
        QueryBuilder qb = mWordListDao.queryBuilder();
        if (mLanguage != null) {
            String uLanguage = mLanguage.toUpperCase();
            String whereString = "";
            String abc = "ABCDEFGHIJ";
            for (int i = 0; i < 10; i++) {
                char lang = abc.charAt(i);
                whereString += "UPPER(LANG_" + lang + ") = '" + uLanguage + "'";

                if (i != 9) whereString += " OR ";
            }
            listsList = qb.where(new WhereCondition.StringCondition(whereString)).orderAsc(WordListDao.Properties.Title).list();
        } else {
            listsList = qb.orderAsc(WordListDao.Properties.Title).list();
        }

        for (WordList list : listsList) {
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
            if(actionMode != null) {
                mAdapter.toggleChecked(position);
                ((CheckableRelativeLayout) view).toggle();
                actionMode.invalidate();
                actionMode.setTitle(getResources().getQuantityString(R.plurals.selected_lists, mAdapter.getCheckedItemCount(), mAdapter.getCheckedItemCount()));
            } else {
                ((CheckableRelativeLayout) view).setChecked(false);
                mListView.setItemChecked(position, false);
                Map<String, Object> map = mAdapter.getItem(position);
                Long listId = (Long) map.get("id");
                Intent intent = new Intent(getActivity(), ListDetailActivity.class);
                intent.putExtra("id", listId);
                startActivity(intent);
            }
        }
    };

    private AdapterView.OnItemLongClickListener mOnItemLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            if(actionMode != null) {
                return false;
            }
            mAdapter.setChecked(position, true);
            getSherlockActivity().startActionMode(new OverviewActionModeCallback());
            actionMode.invalidate();
            ((CheckableRelativeLayout) view).setChecked(true);
            ((CheckableRelativeLayout) view).toggle();
            mListView.setItemChecked(position, true);
            actionMode.setTitle(getResources().getQuantityString(R.plurals.selected_lists, mAdapter.getCheckedItemCount(), mAdapter.getCheckedItemCount()));
            //getSherlockActivity().startActionMode(OverviewListFragment.this);
            return true;
        }
    };

    private class OverviewActionModeCallback implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            actionMode = mode;
            mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            mAdapter.startMultiMode();
            mActivity.toggleDrawerLocked();
            menu.add(0, 0, 0, R.string.delete).setIcon(R.drawable.ic_action_delete).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch(item.getItemId()) {
                case 0:
                    Toast.makeText(getSherlockActivity(), "Clicked delete", Toast.LENGTH_SHORT).show();
                    return true;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mAdapter.stopMultiMode();
            mListView.clearChoices();
            for (int i = 0; i < mListView.getChildCount(); i++) {
                ((Checkable) mListView.getChildAt(i)).setChecked(false);
            }
            mActivity.toggleDrawerLocked();
            actionMode = null;
            mListView.setChoiceMode(ListView.CHOICE_MODE_NONE);
        }
    }

    /*@Override
    public void deleteItem(int position) {
        Map<String, Object> map = mAdapter.getItem(position);
        Long listId = (Long) map.get("id");
        mWordListDao.deleteByKey(listId);
        mAdapter.remove(position);
        mAdapter.notifyDataSetChanged();
    }*/
}
