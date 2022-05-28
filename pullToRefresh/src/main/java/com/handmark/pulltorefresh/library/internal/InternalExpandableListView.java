package com.handmark.pulltorefresh.library.internal;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ExpandableListView;

import com.handmark.pulltorefresh.library.OverscrollHelper;
import com.handmark.pulltorefresh.library.PullToRefreshExpandableListView;

public class InternalExpandableListView extends ExpandableListView implements EmptyViewMethodAccessor {
    private PullToRefreshExpandableListView pullToRefreshExpandableListView;

    public PullToRefreshExpandableListView getPullToRefreshExpandableListView() {
        return pullToRefreshExpandableListView;
    }

    public void setPullToRefreshExpandableListView(PullToRefreshExpandableListView pullToRefreshExpandableListView) {
        this.pullToRefreshExpandableListView = pullToRefreshExpandableListView;
    }

    public InternalExpandableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setEmptyView(View emptyView) {
        pullToRefreshExpandableListView.setEmptyView(emptyView);
    }

    @Override
    public void setEmptyViewInternal(View emptyView) {
        super.setEmptyView(emptyView);
    }

    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX,
                                   int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {

        final boolean returnValue = super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX,
                scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);

        // Does all of the hard work...
        OverscrollHelper.overScrollBy(pullToRefreshExpandableListView, deltaX, scrollX, deltaY, scrollY,
                isTouchEvent);

        return returnValue;
    }
}

