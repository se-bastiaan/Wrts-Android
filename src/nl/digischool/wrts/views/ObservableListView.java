package nl.digischool.wrts.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;
import nl.digischool.wrts.classes.ListViewListener;
import nl.digischool.wrts.classes.Utilities;

/**
 * Sébastiaanmaakt
 * http://sebastiaanmaakt.nl/
 * Date: 17-9-13
 * Time: 0:48
 */
public class ObservableListView extends ListView {

    private ListViewListener mListViewListener;
    private Integer mListViewIndex;

    public ObservableListView(Context context) {
        super(context);
    }

    public ObservableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ObservableListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setScrollListener(ListViewListener listViewListener) {
        mListViewListener = listViewListener;
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        if(mListViewListener != null) {
            mListViewListener.onScrollChanged(this, x, y, oldx, oldy);
        }
    }

    public void setLanguageIndex(Integer index) {
        mListViewIndex = index;
    }

    public Integer getLanguageIndex() {
        return mListViewIndex;
    }

}
