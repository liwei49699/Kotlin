package com.liwei.kotlin.snapshot.dialogs;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.liwei.kotlin.R;
import com.liwei.kotlin.snapshot.activities.SnapShotEditActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.LinkedList;

import static com.liwei.kotlin.snapshot.activities.SnapShotEditActivity.SNAP_SHOT_PATH_KEY;


/**
 * # **********************************************************************************************
 * # ClassName:      SnapPopupWindow
 * # Description:    Stock_dev
 * # Author:         lysun
 * # Version:        Ver 1.0
 * # **********************************************************************************************
 * # Modified By:    lysun     2017/8/23    13:09
 * # Modifications:  initial
 * # **********************************************************************************************
 */
//6.10.0分享编辑改版改为长图，此处先注释
@Deprecated
public class SnapPopupWindow extends PopupWindow implements PopupWindow.OnDismissListener, View.OnClickListener {
    /**
     * 倒计时3秒
     */
    public static final int MILLIS_IN_FUTURE = 3000;
    /**
     * 倒计时间隔
     */
    public static final int COUNT_DOWN_INTERVAL = 1000;
    /**
     * 计时器
     */
    CountDownTimer countDownTimer;

    private String path;

    protected ImageView mImageView;

    /**
     * 已检测到的截屏文件缓存数据集合
     */
    public static LinkedList<String> detectedScreenShots = new LinkedList<>();

    /**
     * 构造函数
     */
    public SnapPopupWindow(Context context, String path) {
        super(context);
        this.path = path;
        detectedScreenShots.add(path);
        initContentView(context);
    }

    /**
     * 初始化
     */
    private void initContentView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.popup_snap, null);
        this.setContentView(view);
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setOutsideTouchable(true);
        view.setOnClickListener(this);
        mImageView = (ImageView) view.findViewById(R.id.image);
        Bitmap bitmap = ImageLoader.getInstance().loadImageSync("file://" + path);
        if (bitmap == null) {
            ImageLoader.getInstance().displayImage("file://" + path, mImageView);
        } else {
            mImageView.setImageBitmap(bitmap);
        }
        countDownTimer = new CountDownTimer(MILLIS_IN_FUTURE, COUNT_DOWN_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                try {
                    dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        setOnDismissListener(this);
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
        countDownTimer.start();
    }

    @Override
    public void onDismiss() {
        if (countDownTimer != null) {
            try {
                countDownTimer.cancel();
            } catch (Exception e) {
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v == null) {
            return;
        }
        Intent intent = new Intent(v.getContext(), SnapShotEditActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(SNAP_SHOT_PATH_KEY, path);
        v.getContext().startActivity(intent);
        try {
            dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
