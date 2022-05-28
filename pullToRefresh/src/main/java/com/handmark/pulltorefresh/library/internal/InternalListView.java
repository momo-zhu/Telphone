package com.handmark.pulltorefresh.library.internal;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.OverscrollHelper;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class InternalListView extends ListView implements EmptyViewMethodAccessor {
    private PullToRefreshListView pullToRefreshListView;

    private boolean mAddedLvFooter = false;

    public PullToRefreshListView getPullToRefreshListView() {
        return pullToRefreshListView;
    }

    public void setPullToRefreshListView(PullToRefreshListView pullToRefreshListView) {
        this.pullToRefreshListView = pullToRefreshListView;
    }

    public InternalListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        /**
         * This is a bit hacky, but Samsung's ListView has got a bug in it
         * when using Header/Footer Views and the list is empty. This masks
         * the issue so that it doesn't cause an FC. See Issue #66.
         */
        try {
            super.dispatchDraw(canvas);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        /**
         * This is a bit hacky, but Samsung's ListView has got a bug in it
         * when using Header/Footer Views and the list is empty. This masks
         * the issue so that it doesn't cause an FC. See Issue #66.
         */
        try {
            return super.dispatchTouchEvent(ev);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        // Add the Footer View at the last possible moment
        if (null != pullToRefreshListView.mLvFooterLoadingFrame && !mAddedLvFooter) {
            addFooterView(pullToRefreshListView.mLvFooterLoadingFrame, null, false);
            mAddedLvFooter = true;
        }

        super.setAdapter(adapter);
    }
    @Override
    public void setEmptyView(View emptyView) {
        getPullToRefreshListView().setEmptyView(emptyView);
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
        OverscrollHelper.overScrollBy(pullToRefreshListView, deltaX, scrollX, deltaY, scrollY, isTouchEvent);

        return returnValue;
    }
}

