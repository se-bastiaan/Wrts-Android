package nl.digischool.wrts.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.BaseAdapter;
import nl.digischool.wrts.fragments.ListDetailWordsFragment;
import nl.digischool.wrts.objects.WordList;

import java.util.ArrayList;
import java.util.Map;

/**
 * Sébastiaanmaakt
 * http://sebastiaanmaakt.nl/
 * Date: 4-9-13
 * Time: 23:50
 */
public class ListDetailPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Map<String, String>> mLanguages;

    public ListDetailPagerAdapter(FragmentManager fm, ArrayList<Map<String, String>> languages) {
        super(fm);
        mLanguages = languages;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Map<String, String> map = mLanguages.get(position);
        return map.get("languageText");
    }

    @Override
    public Fragment getItem(int position) {
        Map<String, String> map = mLanguages.get(position);
        Bundle b = new Bundle();
        b.putString("language", map.get("languageName"));
        ListDetailWordsFragment fragment = new ListDetailWordsFragment();
        fragment.setArguments(b);
        return fragment;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getCount() {
        return mLanguages.size();  //To change body of implemented methods use File | Settings | File Templates.
    }

}
