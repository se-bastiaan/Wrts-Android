package nl.digischool.wrts.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import com.actionbarsherlock.app.ActionBar;
import com.astuetz.viewpager.extensions.PagerSlidingTabStrip;
import com.db4o.ObjectContainer;
import nl.digischool.wrts.R;
import nl.digischool.wrts.adapters.ListDetailPagerAdapter;
import nl.digischool.wrts.classes.Utilities;
import nl.digischool.wrts.database.DbModel;
import nl.digischool.wrts.objects.WordList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Sébastiaanmaakt
 * http://sebastiaanmaakt.nl/
 * Date: 1-9-13
 * Time: 22:16
 */

public class ListDetailActivity extends BaseActivity {

    private ActionBar mActionBar;
    private int mId;
    private WordList mList;
    private PagerSlidingTabStrip mTabStrip;
    private ViewPager mViewPager;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_listdetail);

        mId = getIntent().getIntExtra("id", 0);

        mDb.openDatabase();
        ObjectContainer cont = mDb.openDbSession();
        mList = DbModel.getWordList(cont, mId);
        cont.close();
        mDb.closeDatabase();

        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);

        mTabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabStrip);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);

        ArrayList<Map<String, String>> checkedLanguages = new ArrayList<Map<String, String>>();
        for (int i = 0 ; i < 10; i++) {
            String languageName = Utilities.getLanguageName(i);
            if(mList.languages.containsKey(languageName) && !mList.languages.get(languageName).isEmpty()) {
                Map<String, String> map = new HashMap<String, String>();
                map.put("languageName", languageName);
                map.put("languageText", mList.languages.get(languageName));
                checkedLanguages.add(map);
            }
        }

        ListDetailPagerAdapter adapter = new ListDetailPagerAdapter(getSupportFragmentManager(), checkedLanguages);
        mViewPager.setAdapter(adapter);
        mTabStrip.setViewPager(mViewPager);

        mTabStrip.setIndicatorColorResource(R.color.pressed_wrts);
        mTabStrip.setTextColorResource(android.R.color.white);

    }

    public WordList getWordList() {
        return mList;
    }

}
