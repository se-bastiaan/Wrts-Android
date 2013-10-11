package nl.digischool.wrts.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.astuetz.viewpager.extensions.PagerSlidingTabStrip;
import nl.digischool.wrts.R;
import nl.digischool.wrts.adapters.ListDetailPagerAdapter;
import nl.digischool.wrts.database.WordList;
import nl.digischool.wrts.fragments.ListDetailWordsFragment;
import nl.digischool.wrts.views.ObservableListView;

import java.util.*;

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
    private List<ObservableListView> mListViews;
    private Integer mScrollPosition = 0, mScrollY = 0;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_listdetail);

        mListViews = new ArrayList<ObservableListView>();

        mId = getIntent().getLongExtra("id", 0);

        mDaoSession = mDaoMaster.newSession();
        mList = mDaoSession.getWordListDao().load(mId);

        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setTitle(mList.getTitle());

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

        if(findViewById(R.id.scrollLayout) != null) {

            LinearLayout layout = (LinearLayout) findViewById(R.id.scrollLayout);
            for(int i = 0; i < checkedLanguages.size(); i++) {
                Map<String, Object> map = checkedLanguages.get(i);

                if(i != 0) {
                    LinearLayout divider = new LinearLayout(this);
                    divider.setBackgroundResource(R.color.tab_bg);
                    int dv_wt_px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics());
                    divider.setLayoutParams(new LinearLayout.LayoutParams(dv_wt_px, LinearLayout.LayoutParams.MATCH_PARENT));
                    layout.addView(divider);
                }

                LinearLayout itemLayout = new LinearLayout(this);
                int wt_px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, getResources().getDisplayMetrics());
                itemLayout.setLayoutParams(new LinearLayout.LayoutParams(wt_px, LinearLayout.LayoutParams.MATCH_PARENT));
                itemLayout.setOrientation(LinearLayout.VERTICAL);


                TextView textLayout = new TextView(this);
                textLayout.setText(map.get("languageText").toString().toUpperCase());
                int ht_px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());
                textLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ht_px));
                int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, getResources().getDisplayMetrics());
                textLayout.setPadding(padding, padding, padding, padding);
                textLayout.setBackgroundResource(R.color.tab_bg);
                textLayout.setTextColor(Color.WHITE);

                itemLayout.addView(textLayout);

                Random rnd = new Random();

                FrameLayout frameLayout = new FrameLayout(this);
                frameLayout.setId(i+123456789);
                frameLayout.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));

                Bundle b = new Bundle();
                Integer langIndex = (Integer) map.get("languageIndex");
                b.putInt("languageIndex", langIndex);
                ListDetailWordsFragment fragment = new ListDetailWordsFragment();
                fragment.setArguments(b);

                itemLayout.addView(frameLayout);
                layout.addView(itemLayout);

                getSupportFragmentManager().beginTransaction().add(i+123456789, fragment).commit();
            }

        } else {

            mTabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabStrip);
            mViewPager = (ViewPager) findViewById(R.id.viewPager);

            ListDetailPagerAdapter adapter = new ListDetailPagerAdapter(getSupportFragmentManager(), checkedLanguages);
            mViewPager.setAdapter(adapter);
            mTabStrip.setViewPager(mViewPager);

            mTabStrip.setIndicatorColorResource(R.color.pressed_wrts);
            mTabStrip.setTextColorResource(android.R.color.white);

        }

    }

    public WordList getWordList() {
        return mList;
    }

    public ListDetailPagerAdapter getPagerAdapter() {
        return (ListDetailPagerAdapter) mViewPager.getAdapter();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.activity_listdetail_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        // Handle action buttons
        switch(item.getItemId()) {
            case android.R.id.home:
                intent = new Intent(this, OverviewActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            case R.id.testAction:
                intent = new Intent(this, TestPrepareActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
