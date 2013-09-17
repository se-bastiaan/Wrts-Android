package nl.digischool.wrts.classes;

import nl.digischool.wrts.views.ObservableListView;

public interface ListViewListener {
    public void onScrollChanged(ObservableListView scrollView, int x, int y, int oldx, int oldy);
}