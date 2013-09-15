package nl.digischool.wrts.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Sébastiaanmaakt
 * http://sebastiaanmaakt.nl/
 * Date: 15-9-13
 * Time: 13:45
 */
public class CheckableRelativeLayout extends RelativeLayout implements Checkable {

    private boolean mIsChecked;
    private List<Checkable> mCheckableViews;

    public CheckableRelativeLayout(Context context) {
        super(context);
        initialise(null);
    }

    public CheckableRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialise(attrs);
    }

    public CheckableRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialise(attrs);
    }

    private void initialise(AttributeSet attrs) {
        mIsChecked = false;
        mCheckableViews = new ArrayList<Checkable>();
    }

    public boolean isChecked() {
        return mIsChecked;
    }

    public void setChecked(boolean check) {
        mIsChecked = check;
        for (Checkable c : mCheckableViews) {
            c.setChecked(check);
        }
        refreshDrawableState();
    }

    public void toggle() {
        mIsChecked = !mIsChecked;
        for (Checkable c : mCheckableViews) {
            c.toggle();
        }
    }

    private static final int[] CheckedStateSet = {
            android.R.attr.state_checked
    };

    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            mergeDrawableStates(drawableState, CheckedStateSet);
        }
        return drawableState;
    }
}
