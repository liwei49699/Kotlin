package com.liwei.kotlin.snapshot.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.customview.widget.ViewDragHelper;

/**
 * # **********************************************************************************************
 * # ClassName:      DragableFrameLayout
 * # Description:    Stock_dev
 * # Author:         lysun
 * # Version:        Ver 1.0
 * # **********************************************************************************************
 * # Modified By:    lysun     2017/8/30    10:05
 * # Modifications:  initial
 * # **********************************************************************************************
 */
public class DraggableFrameLayout extends FrameLayout {
    /**
     * 标记位是否可拖动
     */
    protected boolean canDrag;
    /**
     * 拖动工具
     */
    protected ViewDragHelper mViewDragHelper;

    public DraggableFrameLayout(Context context) {
        this(context, null);
    }

    public DraggableFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DraggableFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 是否可拖动
     */
    public void setCanDrag(boolean canDrag) {
        this.canDrag = canDrag;
    }

    /**
     * 初始化
     */
    public void init() {
        mViewDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                //mEdgeTrackerView禁止直接移动
                return canDrag && child instanceof TextView;
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                final int leftBound = getPaddingLeft();
                final int rightBound = getWidth() - child.getWidth() - leftBound;
                final int newLeft = Math.min(Math.max(left, leftBound), rightBound);
                return newLeft;
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                final int topBound = getPaddingTop();
                final int bottomBound = getHeight() - child.getHeight() - topBound;
                final int newTop = Math.min(Math.max(top, topBound), bottomBound);
                return newTop;
            }

            @Override
            public int getViewHorizontalDragRange(View child) {
                return getMeasuredWidth() - child.getMeasuredWidth();
            }

            @Override
            public int getViewVerticalDragRange(View child) {
                return getMeasuredHeight() - child.getMeasuredHeight();
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                super.onViewReleased(releasedChild, xvel, yvel);
                if (releasedChild == null) {
                    return;
                }
                ((TextView) releasedChild).setMaxWidth(getRight() - releasedChild.getLeft());
            }
        });
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return mViewDragHelper.shouldInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mViewDragHelper.processTouchEvent(event);
        return canDrag;
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                int width = child.getMeasuredWidth();
                int height = child.getMeasuredHeight();
                MarginLayoutParams marginLayoutParams = null;
                try {
                    marginLayoutParams = (MarginLayoutParams) getLayoutParams();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                int childLeft = l + child.getLeft() + getPaddingLeft() - (marginLayoutParams == null ? 0 : marginLayoutParams.leftMargin);
                int childTop = t + child.getTop() + getPaddingTop() - (marginLayoutParams == null ? 0 : marginLayoutParams.topMargin);
                child.layout(childLeft, childTop, childLeft + width, childTop + height);
            }
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }
}
