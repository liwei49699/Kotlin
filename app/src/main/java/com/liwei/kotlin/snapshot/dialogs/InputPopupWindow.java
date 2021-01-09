package com.liwei.kotlin.snapshot.dialogs;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.inputmethodservice.InputMethodService;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.liwei.kotlin.R;


/**
 * # **********************************************************************************************
 * # ClassName:      InputPopupWindow
 * # Description:    Stock_dev
 * # Author:         lysun
 * # Version:        Ver 1.0
 * # **********************************************************************************************
 * # Modified By:    lysun     2017/8/30    9:02
 * # Modifications:  initial
 * # **********************************************************************************************
 *
 * @param <TV>
 */

public class InputPopupWindow<TV extends TextView> extends PopupWindow {
    //PopupWindow上面装载的View
    private View mMenuView;

    /**
     * 保存
     */
    protected TextView mImageView_Save;
    /**
     * 输入框
     */
    protected EditText mEditText;
    protected ImageView mImageView_Clear;

    private TV currentTextView;

    /**
     * 设置当前的关联的textview
     */
    public void setCurrentTextView(TV currentTextView) {
        this.currentTextView = currentTextView;
        mEditText.setText(currentTextView.getText());
        if (TextUtils.isEmpty(currentTextView.getText())) {
            return;
        }
        mEditText.setSelection(currentTextView.getText().length());
    }

    @Override
    public void dismiss() {
        try {
            InputMethodManager imm = (InputMethodManager) getContentView().getContext().getSystemService(InputMethodService.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(getContentView().getWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.dismiss();
    }

    /**
     * 构造
     */
    public InputPopupWindow(final Activity context) {
        super(context);
        /*将xml布局初始化为View,并初始化上面的控件*/
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.popup_input, null);
        mEditText = (EditText) mMenuView.findViewById(R.id.edit);
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                // 切显隐
                mImageView_Clear.setVisibility(TextUtils.isEmpty(mEditText.getText()) ? View.GONE
                        : mEditText.getText().toString().length() > 0 ? View.VISIBLE : View.GONE);
            }
        });
        mImageView_Clear = (ImageView) mMenuView.findViewById(R.id.btn_clear);
        mImageView_Clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditText.setText("");
            }
        });
        mImageView_Save = (TextView) mMenuView.findViewById(R.id.btn_save);
        mImageView_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 保存
                if (currentTextView == null) {
                    return;
                }
                currentTextView.setText(mEditText.getText());
                currentTextView.requestLayout();
                dismiss();
            }
        });
        //设置SignPopupWindow的View
        this.setContentView(mMenuView);
        //设置SignPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SignPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置SignPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SignPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimBottom);
        //        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable colorDrawable = new ColorDrawable(Color.TRANSPARENT);
        //        //设置SignPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(colorDrawable);
        //防止虚拟软键盘被弹出菜单遮住
        this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mMenuView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                int height = mMenuView.findViewById(R.id.pop_layout).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });
    }
}

