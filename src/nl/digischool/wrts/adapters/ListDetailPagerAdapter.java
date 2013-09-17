package nl.digischool.wrts.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.ListView;
import nl.digischool.wrts.fragments.ListDetailWordsFragment;
import nl.digischool.wrts.views.ObservableListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Sébastiaanmaakt
 * http://sebastiaanmaakt.nl/
 * Date: 4-9-13
 * Time: 23:50
 */
public class ListDetailPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Map<String, Object>> mLanguages;
    private List<ObservableListView> mListViews;
    private Integer mScrollPosition = 0, mScrollY = 0;

    public ListDetailPagerAdapter(FragmentManager fm, ArrayList<Map<String, Object>> languages) {
        super(fm);
        mLanguages = languages;
        mListViews = new ArrayList<ObservableListView>();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Map<String, Object> map = mLanguages.get(position);
        return map.get("languageText").toString();
    }

    @Override
    public Fragment getItem(int position) {
        Map<String, Object> map = mLanguages.get(position);
        Bundle b = new Bundle();
        Integer langIndex = (Integer) map.get("languageIndex");
        b.putInt("languageIndex", langIndex);
        ListDetailWordsFragment fragment = new ListDetailWordsFragment();
        fragment.setArguments(b);
        return fragment;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getCount() {
        return mLanguages.size();  //To change body of implemented methods use File | Settings | File Templates.
    }

    public List<ObservableListView> getListViews() {
        return mListViews;
    }

    public void addToListViews(ObservableListView listView) {
        mListViews.add(listView);
    }

    public void setScrollPosition(int scrollPosition) {
        mScrollPosition = scrollPosition;
    }

    public void setScrollY(int scrollY) {
        mScrollY = scrollY;
    }

    public Integer getScrollPosition() {
        return mScrollPosition;
    }

    public Integer getScrollY() {
        return mScrollY;
    }

}
