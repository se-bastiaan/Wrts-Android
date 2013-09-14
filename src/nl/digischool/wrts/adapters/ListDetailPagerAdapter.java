package nl.digischool.wrts.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import nl.digischool.wrts.fragments.ListDetailWordsFragment;

import java.util.ArrayList;
import java.util.Map;

/**
 * Sébastiaanmaakt
 * http://sebastiaanmaakt.nl/
 * Date: 4-9-13
 * Time: 23:50
 */
public class ListDetailPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Map<String, Object>> mLanguages;

    public ListDetailPagerAdapter(FragmentManager fm, ArrayList<Map<String, Object>> languages) {
        super(fm);
        mLanguages = languages;
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

}
