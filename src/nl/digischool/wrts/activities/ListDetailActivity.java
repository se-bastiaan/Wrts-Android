package nl.digischool.wrts.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.astuetz.viewpager.extensions.PagerSlidingTabStrip;
import nl.digischool.wrts.R;
import nl.digischool.wrts.adapters.ListDetailPagerAdapter;
import nl.digischool.wrts.database.WordList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Sébastiaanmaakt
 * http://sebastiaanmaakt.nl/
 * Date: 1-9-13
 * Time: 22:16
 */

public class ListDetailActivity extends BaseActivity {

    private ActionBar mActionBar;
    private Long mId;
    private WordList mList;
    private PagerSlidingTabStrip mTabStrip;
    private ViewPager mViewPager;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_listdetail);

        mId = getIntent().getLongExtra("id", 0);

        mDaoSession = mDaoMaster.newSession();
        mList = mDaoSession.getWordListDao().load(mId);

        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setTitle(mList.getTitle());

        mTabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabStrip);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);

        ArrayList<Map<String, Object>> checkedLanguages = new ArrayList<Map<String, Object>>();

        if(mList.getLang_a() != null && !mList.getLang_a().isEmpty()) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("languageIndex", 0);
            map.put("languageText", mList.getLang_a());
            checkedLanguages.add(map);
        }

        if(mList.getLang_b() != null && !mList.getLang_b().isEmpty()) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("languageIndex", 1);
            map.put("languageText", mList.getLang_b());
            checkedLanguages.add(map);
        }

        if(mList.getLang_c() != null && !mList.getLang_c().isEmpty()) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("languageIndex", 2);
            map.put("languageText", mList.getLang_c());
            checkedLanguages.add(map);
        }

        if(mList.getLang_d() != null && !mList.getLang_d().isEmpty()) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("languageIndex", 3);
            map.put("languageText", mList.getLang_d());
            checkedLanguages.add(map);
        }

        if(mList.getLang_e() != null && !mList.getLang_e().isEmpty()) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("languageIndex", 4);
            map.put("languageText", mList.getLang_e());
            checkedLanguages.add(map);
        }

        if(mList.getLang_f() != null && !mList.getLang_f().isEmpty()) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("languageIndex", 5);
            map.put("languageText", mList.getLang_f());
            checkedLanguages.add(map);
        }

        if(mList.getLang_g() != null && !mList.getLang_g().isEmpty()) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("languageIndex", 6);
            map.put("languageText", mList.getLang_g());
            checkedLanguages.add(map);
        }

        if(mList.getLang_h() != null && !mList.getLang_h().isEmpty()) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("languageIndex", 7);
            map.put("languageText", mList.getLang_h());
            checkedLanguages.add(map);
        }

        if(mList.getLang_i() != null && !mList.getLang_i().isEmpty()) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("languageIndex", 8);
            map.put("languageText", mList.getLang_i());
            checkedLanguages.add(map);
        }

        if(mList.getLang_j() != null && !mList.getLang_j().isEmpty()) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("languageIndex", 9);
            map.put("languageText", mList.getLang_j());
            checkedLanguages.add(map);
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

    public ListDetailPagerAdapter getPagerAdapter() {
        return (ListDetailPagerAdapter) mViewPager.getAdapter();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action buttons
        switch(item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, OverviewActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
