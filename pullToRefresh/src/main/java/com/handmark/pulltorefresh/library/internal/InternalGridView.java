package com.handmark.pulltorefresh.library.internal;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridView;

import com.handmark.pulltorefresh.library.OverscrollHelper;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;

public  class InternalGridView extends GridView implements EmptyViewMethodAccessor {
    private PullToRefreshGridView pullToRefreshGridView;

    public PullToRefreshGridView getPullToRefreshGridView() {
        return pullToRefreshGridView;
    }

    public void setPullToRefreshGridView(PullToRefreshGridView pullToRefreshGridView) {
        this.pullToRefreshGridView = pullToRefreshGridView;
    }

    public InternalGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setEmptyView(View emptyView) {
        pullToRefreshGridView.setEmptyView(emptyView);
    }
    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX,
                                   int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {

        final boolean returnValue = super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX,
                scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);

        // Does all of the hard work...
        OverscrollHelper.overScrollBy(pullToRefreshGridView, deltaX, scrollX, deltaY, scrollY, isTouchEvent);

        return returnValue;
    }
    @Override
    public void setEmptyViewInternal(View emptyView) {
        super.setEmptyView(emptyView);
    }
}
