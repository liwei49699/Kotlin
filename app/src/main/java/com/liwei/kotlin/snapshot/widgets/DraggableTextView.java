package com.liwei.kotlin.snapshot.widgets;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

import com.liwei.kotlin.R;
import com.liwei.kotlin.snapshot.utils.StringUtils;


/**
 * # **********************************************************************************************
 * # ClassName:      DragableTextView
 * # Description:    Stock_dev
 * # Author:         lysun
 * # Version:        Ver 1.0
 * # **********************************************************************************************
 * # Modified By:    lysun     2017/8/29    16:36
 * # Modifications:  initial
 * # **********************************************************************************************
 */
public class DraggableTextView extends TextView {

    /**
     * 文字颜色
     */
    public static final int mTextColor = Color.rgb(255, 204, 0);

    public DraggableTextView(Context context) {
        this(context, null);
    }

    public DraggableTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DraggableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initContentView();
    }


    private void initContentView() {
        setHint("点击输入文字");
        setHintTextColor(mTextColor);
        setTextColor(mTextColor);
        setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        setSelected(true);
        setPadding(StringUtils.dipToPx(8), StringUtils.dipToPx(8), StringUtils.dipToPx(8), StringUtils.dipToPx(8));
        setMinimumWidth(StringUtils.dipToPx(60));
        setMinimumHeight(StringUtils.dipToPx(40));
        //        setOnTouchListener(new OnTouchListener() {//设置按钮被触摸的时间
        //
        //            int lastX, lastY; // 记录移动的最后的位置
        //
        //            @Override
        //            public boolean onTouch(View v, MotionEvent event) {
        //                // TODO Auto-generated method stub
        //                int ea = event.getAction();//获取事件类型
        //                switch (ea) {
        //                    case MotionEvent.ACTION_DOWN: // 按下事件
        //                        lastX = (int) event.getRawX();
        //                        lastY = (int) event.getRawY();
        //                        downX = lastX;
        //                        downY = lastY;
        //                        break;
        //
        //                    case MotionEvent.ACTION_MOVE: // 拖动事件
        //
        //                        // 移动中动态设置位置
        //                        int dx = (int) event.getRawX() - lastX;//位移量X
        //                        int dy = (int) event.getRawY() - lastY;//位移量Y
        //                        int left = v.getLeft() + dx;
        //                        int top = v.getTop() + dy;
        //                        int right = v.getRight() + dx;
        //                        int bottom = v.getBottom() + dy;
        //
        //                        //++限定按钮被拖动的范围
        //                        if (left < 0) {
        //
        //                            left = 0;
        //                            right = left + v.getWidth();
        //
        //                        }
        //                        if (right > UIScreen.screenWidth) {
        //
        //                            right = UIScreen.screenWidth;
        //                            left = right - v.getWidth();
        //
        //                        }
        //                        if (top < 0) {
        //
        //                            top = 0;
        //                            bottom = top + v.getHeight();
        //
        //                        }
        //                        if (bottom > UIScreen.screenHeight) {
        //
        //                            bottom = UIScreen.screenHeight;
        //                            top = bottom - v.getHeight();
        //
        //                        }
        //
        //                        //--限定按钮被拖动的范围
        //
        //                        //按钮重画
        ////                        v.layout(left, top, right, bottom);
        //                        v.setTranslationX(dx);
        //                        v.setTranslationY(dy);
        //                        v.requestLayout();
        ////                        v.setX(left);
        ////                        v.setY(top);
        ////                        v.requestLayout();
        //                        // 记录当前的位置
        //                        lastX = (int) event.getRawX();
        //                        lastY = (int) event.getRawY();
        //                        return true;
        //
        //                    case MotionEvent.ACTION_UP: // 弹起事件
        //                        //判断是单击事件或是拖动事件，位移量大于5则断定为拖动事件
        //                        if (Math.abs((int) (event.getRawX() - downX)) > 5 || Math.abs((int) (event.getRawY() - downY)) > 5)
        //                            clickormove = false;
        //                        else
        //                            clickormove = true;
        //                }
        //                return false;
        //            }
        //        });
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        if (selected) {
            setBackgroundResource(R.drawable.shape_dash_rect_orange);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                setBackground(null);
            } else {
                setBackgroundDrawable(null);
            }
        }
        setPadding(StringUtils.dipToPx(8), StringUtils.dipToPx(8), StringUtils.dipToPx(8), StringUtils.dipToPx(8));
    }


}
