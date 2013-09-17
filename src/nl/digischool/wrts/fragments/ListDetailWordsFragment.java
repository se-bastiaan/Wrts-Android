package nl.digischool.wrts.fragments;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.actionbarsherlock.app.SherlockFragment;
import nl.digischool.wrts.R;
import nl.digischool.wrts.activities.ListDetailActivity;
import nl.digischool.wrts.adapters.ListDetailPagerAdapter;
import nl.digischool.wrts.adapters.WordsListAdapter;
import nl.digischool.wrts.classes.ListViewListener;
import nl.digischool.wrts.classes.Utilities;
import nl.digischool.wrts.database.WordList;
import nl.digischool.wrts.views.ObservableListView;

import java.util.List;

/**
 * Sébastiaanmaakt
 * http://sebastiaanmaakt.nl/
 * Date: 4-9-13
 * Time: 23:48
 */
public class ListDetailWordsFragment extends SherlockFragment {

    private ObservableListView mListView;
    private WordList mList;
    private Integer mLanguageIndex;
    private ListDetailPagerAdapter mPagerAdapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle savedInstanceState) {
        ListDetailActivity activity = (ListDetailActivity) getActivity();
        mList = activity.getWordList();
        mPagerAdapter = activity.getPagerAdapter();

        //if(mList.words != null) Utilities.log("ListDetailWordsFragment", "not null");

        Bundle args = getArguments();
        mLanguageIndex = args.getInt("languageIndex");
        WordsListAdapter adapter = new WordsListAdapter(getActivity(), mList.getWords(), mLanguageIndex);

        View v = inflater.inflate(R.layout.activity_overview_list, group, false);

        mListView = (ObservableListView) v.findViewById(R.id.drawer_list);
        mListView.setChoiceMode(ListView.CHOICE_MODE_NONE);
        mListView.setClickable(false);
        mListView.setLanguageIndex(mLanguageIndex);
        mListView.setScrollListener(new ListViewListener() {
            @Override
            public void onScrollChanged(ObservableListView listView, int x, int y, int oldx, int oldy) {
                List<ObservableListView> listviews = mPagerAdapter.getListViews();

                int savedPosition = listView.getFirstVisiblePosition();
                View firstVisibleView = listView.getChildAt(0);
                int savedListTop = (firstVisibleView == null) ? 0 : firstVisibleView.getTop();

                mPagerAdapter.setScrollPosition(savedPosition);
                mPagerAdapter.setScrollY(savedListTop);

                Utilities.log("OnScrollChanged", "Y: "+savedListTop);
                for(ObservableListView listview : listviews) {
                    if (savedPosition >= 0) { //initialized to -1
                        listview.setSelectionFromTop(savedPosition, savedListTop);
                        listview.invalidate();
                        listview.requestLayout();
                    }
                }
            }
        });
        mListView.setSelectionFromTop(mPagerAdapter.getScrollPosition(), mPagerAdapter.getScrollY());
        mPagerAdapter.addToListViews(mListView);

        mListView.setAdapter(adapter);

        return v;
    }

}
