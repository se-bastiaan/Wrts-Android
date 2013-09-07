package nl.digischool.wrts.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.actionbarsherlock.app.SherlockFragment;
import nl.digischool.wrts.R;
import nl.digischool.wrts.activities.ListDetailActivity;
import nl.digischool.wrts.adapters.WordsListAdapter;
import nl.digischool.wrts.classes.Utilities;
import nl.digischool.wrts.dao.WordList;

/**
 * Sébastiaanmaakt
 * http://sebastiaanmaakt.nl/
 * Date: 4-9-13
 * Time: 23:48
 */
public class ListDetailWordsFragment extends SherlockFragment {

    private ListView mListView;
    private WordList mList;
    private String mLanguage;

    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle savedInstanceState) {
        ListDetailActivity activity = (ListDetailActivity) getActivity();
        mList = activity.getWordList();
        //if(mList.words != null) Utilities.log("ListDetailWordsFragment", "not null");

        Bundle args = getArguments();
        mLanguage = args.getString("language");
        String wordName = Utilities.getWordNameByLanguage(mLanguage);
        //WordsListAdapter adapter = new WordsListAdapter(getActivity(), mList.words, wordName);

        View v = inflater.inflate(R.layout.activity_overview_list, group, false);

        mListView = (ListView) v.findViewById(R.id.drawer_list);
        mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        //mListView.setOnItemClickListener(mOnItemClickListener);

        //mListView.setAdapter(adapter);

        return v;
    }

}
