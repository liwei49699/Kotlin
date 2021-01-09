package com.liwei.kotlin.snapshot;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;

import com.liwei.kotlin.BaseApplication;
import com.liwei.kotlin.snapshot.dialogs.SnapPopupWindow;
import com.liwei.kotlin.snapshot.utils.StringUtils;


/**
 * # **********************************************************************************************
 * # ClassName:      SnapShotTakenCallBack.java
 * # Description:    截屏事件回调
 * # Author:         lysun
 * # Version:        Ver 1.0
 * # **********************************************************************************************
 * # Modified By:    lysun     2017/8/17    13:49
 * # Modifications:  initial
 * # **********************************************************************************************
 */
//6.10.0分享编辑改为长图分享，先注释掉

public class SnapShotTakenCallBack implements ISnapShotCallBack {
    private Context context;

    public SnapShotTakenCallBack(Context context) {
        this.context = context;
    }

    @Override
    public void onSnapShotTaken(final String path) {
        final Context currentActivity = BaseApplication.getInstance().getCurrentActivity();
        if (currentActivity == null) {
            return;
        }
        // 若已经弹过该图片，直接返回
        if (SnapPopupWindow.detectedScreenShots != null && SnapPopupWindow.detectedScreenShots.contains(path)) {
            return;
        }
        ((Activity) currentActivity).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                SnapPopupWindow mF5MorePopWindow = new SnapPopupWindow(currentActivity, path);
                mF5MorePopWindow.showAtLocation(((Activity) currentActivity).getWindow().getDecorView(),
                        Gravity.RIGHT | Gravity.CENTER_VERTICAL, StringUtils.dipToPx(12), 0);
            }
        });
    }
}
