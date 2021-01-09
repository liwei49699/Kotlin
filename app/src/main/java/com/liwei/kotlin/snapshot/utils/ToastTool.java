package com.liwei.kotlin.snapshot.utils;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.StringRes;

import com.liwei.kotlin.BaseApplication;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;


/**
 * # ***********************************************************************************************
 * # ClassName:      ToastTool.java
 * # Function:       统一toast调用方式，解决系统Toast缺陷
 * # @author         ylrong
 * # @version        Ver 1.0
 * # ***********************************************************************************************
 * # Modified By     ylrong     2019/6/27    11:37
 * # Modifications:  initial
 * # ***********************************************************************************************
 */
public class ToastTool {

    public static final int MESSAGE_TOAST = 970;

    private static Toast mToast;

    public static void showToast(String msg) {
        showToast(msg, Toast.LENGTH_SHORT);
    }

    public static void showToast(String msg, int duration) {
        showToast(msg, -1, duration);
    }

    public static void showToast(@StringRes int resId) {
        showToast(resId, Toast.LENGTH_SHORT);
    }

    public static void showToast(@StringRes int resId, int duration) {
        showToast(null, resId, duration);
    }

    public static void showToast(String msg, @StringRes int resId, int duration) {
        if (BaseApplication.getInstance() == null || (TextUtils.isEmpty(msg) && resId == -1)) {
            return;
        }
        if (Looper.getMainLooper() == Looper.myLooper()) {
            toastShow(msg, resId, duration);
            return;
        }
        Message message = Message.obtain();
        message.what = MESSAGE_TOAST;
        message.obj = msg;
        message.arg1 = resId;
        message.arg2 = duration;
        ActivityHandler.getInstance(new ActivityHandler.ActivityHandlerListener() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == MESSAGE_TOAST) {
                    toastShow((String)msg.obj, msg.arg1, msg.arg2);
                }
            }
        }).sendMessage(message);
    }

    private static void toastShow(String str, @StringRes int strId, int showTime) {
        if (!TextUtils.isEmpty(str)) {
            if (mToast == null || !checkHook(mToast)) {
                mToast = Toast.makeText(BaseApplication.getInstance(), str, showTime);
            } else {
                mToast.cancel();
                mToast = Toast.makeText(BaseApplication.getInstance(), str, showTime);
//                            mToast.setText(str);
//                            mToast.setDuration(showTime);
            }
        } else if (strId != -1) {
            if (mToast == null || !checkHook(mToast)) {
                mToast = Toast.makeText(BaseApplication.getInstance(), strId, showTime);
            } else {
                mToast.cancel();
                mToast = Toast.makeText(BaseApplication.getInstance(), strId, showTime);
//                            mToast.setText(strId);
//                            mToast.setDuration(showTime);
            }
        } else {
            return;
        }
        hook(mToast);
        mToast.show();
    }

    public static void cancalToast() {
        if (mToast != null) {
            mToast.cancel();
        }
    }

    /***************************************使用反射替换handler******************************************/
    private static Field mField_TN;
    private static Field mField_TN_Handler;

    static {
        try {
            mField_TN = Toast.class.getDeclaredField("mTN");
            mField_TN.setAccessible(true);
            mField_TN_Handler = mField_TN.getType().getDeclaredField("mHandler");
            mField_TN_Handler.setAccessible(true);
        } catch (Exception e) {
        }
    }

    /**
     * 检查是否系统Handler存在
     *
     * @param toast
     * @return
     */
    private static boolean checkHook(Toast toast) {
        try {
            Object tn = mField_TN.get(toast);
            Handler preHandler = (Handler) mField_TN_Handler.get(tn);
            if (preHandler instanceof SafelyHandlerWarpper
                    && ((SafelyHandlerWarpper) preHandler).impl.get() != null) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * 用自定义SafelyHandlerWarpper替换系统Handler
     *
     * @param toast
     */
    private static void hook(Toast toast) {
        try {
            Object tn = mField_TN.get(toast);
            Handler preHandler = (Handler) mField_TN_Handler.get(tn);
            if (!(preHandler instanceof SafelyHandlerWarpper)) {
                mField_TN_Handler.set(tn, new SafelyHandlerWarpper(preHandler));
            }
        } catch (Exception e) {
        }
    }

    private static class SafelyHandlerWarpper extends Handler {

        private WeakReference<Handler> impl;

        public SafelyHandlerWarpper(Handler impl) {
            this.impl = new WeakReference<>(impl);
        }

        @Override
        public void dispatchMessage(Message msg) {
            try {
                super.dispatchMessage(msg);
            } catch (Exception e) {
            }
        }

        @Override
        public void handleMessage(Message msg) {
            try {
                // 委托给原Handler执行
                Handler handler = impl.get();
                if (handler != null) {
                    handler.handleMessage(msg);
                }
            } catch (Exception e) {
            }
        }


    }
}
