//package com.liwei.kotlin.snapshot;
//
//import android.app.Activity;
//import android.content.res.Resources;
//import android.widget.Toast;
//
//import com.windshare.ShareCallBack;
//import com.windshare.ShareTypeEnum;
//
//import java.util.HashMap;
//
//import base.BaseActivity;
//import base.MainUIActivity;
//import cn.sharesdk.framework.Platform;
//import ui.UIAlertView;
//import util.ToastTool;
//import wind.android.f5.R;
//
///**
// * 分享回调
// */
//public class WindShareCallBack implements ShareCallBack {
//
//    /**
//     * Activity
//     */
//    public Activity mActivity;
//
//    public WindShareCallBack(Activity activity) {
//        super();
//        this.mActivity = activity;
//    }
//
//    @Override
//    public void onCancel(Platform arg0, int arg1, ShareTypeEnum shareType) {
//        hideProgressMum();
//        // LogHelper.print(TAG, "WindShareCallBack---onCancel:" + arg1);
//        ToastTool.showToast(R.string.share_canceled);
//    }
//
//    @Override
//    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2, ShareTypeEnum shareType) {
//        hideProgressMum();
//        UIAlertView alertView = null;
//        switch (shareType) {
//            case SHARE_SINA_WEIBO:
//                alertView = new UIAlertView(mActivity, "提示", "发送新浪微博成功!", "确定");
//                alertView.show();
//                break;
//            case SHARE_QQ_ZONE:
//                alertView = new UIAlertView(mActivity, "提示", "发送QQ空间成功!", "确定");
//                alertView.show();
//                break;
//            case SHARE_QQ:
//                alertView = new UIAlertView(mActivity, "提示", "发送QQ好友成功!", "确定");
//                alertView.show();
//                break;
//            default:
//                break;
//        }
//    }
//
//    @Override
//    public void onError(Platform arg0, int arg1, Throwable arg2, ShareTypeEnum shareType) {
//        hideProgressMum();
//        Resources res = mActivity.getResources();
//        String failed = res.getString(R.string.share_failed);
//        String ok = res.getString(android.R.string.ok);
//        String title = res.getString(android.R.string.dialog_alert_title);
//        if (arg2 != null) {
//            ToastTool.showToast(failed + "  " + arg2.toString(), Toast.LENGTH_LONG);
//        }
//        switch (shareType) {
//            case SHARE_SINA_WEIBO:
//                UIAlertView mAlertView = new UIAlertView(mActivity, title, "发送新浪微博失败!", ok);
//                mAlertView.show();
//                break;
//            default:
//                break;
//        }
//    }
//
//    @Override
//    public void onStart(ShareTypeEnum shareType) {
//        showProgressMum();
//    }
//
//    public void dismiss() {
//        hideProgressMum();
//    }
//
//    private void showProgressMum() {
//        if (mActivity instanceof MainUIActivity) {
//            ((MainUIActivity) mActivity).showProgressMum();
//        } else if (mActivity instanceof BaseActivity) {
//            ((BaseActivity) mActivity).showProgressMum();
//        }
//    }
//
//    private void hideProgressMum() {
//        if (mActivity instanceof MainUIActivity) {
//            ((MainUIActivity) mActivity).hideProgressMum();
//        } else if (mActivity instanceof BaseActivity) {
//            ((BaseActivity) mActivity).hideProgressMum();
//        }
//    }
//}