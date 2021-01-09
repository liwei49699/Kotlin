package com.liwei.kotlin.snapshot.services;

import android.app.Service;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.provider.MediaStore;

import androidx.annotation.Nullable;

import com.liwei.kotlin.snapshot.SnapShotTakenCallBack;
import com.liwei.kotlin.snapshot.utils.FileObserverUtils;
import com.liwei.kotlin.snapshot.utils.ToastTool;
import com.liwei.kotlin.snapshot.utils.UIScreen;
import com.nostra13.universalimageloader.core.ImageLoader;


/**
 * # **********************************************************************************************
 * # ClassName:      ScreenShotService
 * # Description:    ${TODO}  ADD Description
 * # Author:         lysun
 * # Version:        Ver 1.0
 * # **********************************************************************************************
 * # Modified By:    lysun     2017/8/11    9:40
 * # Modifications:  initial
 * # **********************************************************************************************
 */
public class ScreenShotService extends Service {
    /**
     * 截屏回调
     */
    public static SnapShotTakenCallBack callBack;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private HandlerThread mHandlerThread;
    private Handler mHandler;
    /**
     * 外部存储器内容观察者
     */
    private ContentObserver mExternalObserver;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (callBack == null) {
            callBack = new SnapShotTakenCallBack(getApplicationContext());
        }
        // 针对华为手机无法收到事件的问题
        //        if (BRAND_HUAWEI.equalsIgnoreCase(Build.MANUFACTURER)) {
        mHandlerThread = new HandlerThread("Screenshot_Observer");
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper());

        // 初始化
        mExternalObserver = new MediaContentObserver(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, mHandler);
        this.getContentResolver().registerContentObserver(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, false, mExternalObserver);
        //        } else {
        FileObserverUtils.setSnapShotCallBack(callBack);
        FileObserverUtils.startSnapshotWatching();
        //        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        FileObserverUtils.stopSnapshotWatching();
        // 注销监听
        this.getContentResolver().unregisterContentObserver(mExternalObserver);
        mHandlerThread.quit();
    }

    /**
     * 媒体内容观察者(观察媒体数据库的改变)
     */
    public class MediaContentObserver extends ContentObserver {

        private Uri mContentUri;

        public MediaContentObserver(Uri contentUri, Handler handler) {
            super(handler);
            mContentUri = contentUri;
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            handleMediaContentChange(mContentUri);
        }
    }

    private static final String[] KEYWORDS = {
            "screenshot", "screen_shot", "screen-shot", "screen shot",
            "screencapture", "screen_capture", "screen-capture", "screen capture",
            "screencap", "screen_cap", "screen-cap", "screen cap",
            "截屏"
    };

    /**
     * 读取媒体数据库时需要读取的列
     */
    private static final String[] MEDIA_PROJECTIONS = {
            MediaStore.Images.ImageColumns.DATA,
            MediaStore.Images.ImageColumns.DATE_TAKEN,
    };

    private void handleMediaContentChange(Uri contentUri) {
        Cursor cursor = null;
        try {
            // 数据改变时查询数据库中最后加入的一条数据
            cursor = this.getContentResolver().query(
                    contentUri, MEDIA_PROJECTIONS, null, null, MediaStore.Images.ImageColumns.DATE_ADDED + " desc limit 1");

            if (cursor == null) {
                return;
            }
            if (!cursor.moveToFirst()) {
                return;
            }

            // 获取各列的索引
            int dataIndex = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            int dateTakenIndex = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATE_TAKEN);

            // 获取行数据
            String data = cursor.getString(dataIndex);
            long dateTaken = cursor.getLong(dateTakenIndex);

            // 处理获取到的第一行数据
            handleMediaRowData(data, dateTaken);

        } catch (Exception e) {
            e.printStackTrace();
            ToastTool.showToast(e.getMessage());
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
    }

    /**
     * 处理监听到的资源
     */
    private void handleMediaRowData(String filePath, long dateTaken) {
        if (checkScreenShot(filePath, dateTaken)) {
            if (matchAddTime(dateTaken)) {
                if (matchSize(filePath)) {
                    int MAX_TRY = 3;
                    int tryTimes = 0;
                    while (true) {
                        try {
                            // 收到CREATE事件后马上获取并不能获取到，需要延迟一段时间
                            Thread.sleep(1500);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            Bitmap bitmap = ImageLoader.getInstance().loadImageSync("file://" + filePath);
                            break;
                        } catch (Exception e) {
                            e.printStackTrace();
                            tryTimes++;
                            if (tryTimes >= MAX_TRY) {
                                // 尝试MAX_TRY次失败后，放弃
                                return;
                            }
                        }
                    }
                    if (callBack != null) {
                        callBack.onSnapShotTaken(filePath);
                    }
                }
            }

        }
    }

    /**
     * 添加时间与当前时间不超过1.5s,大部分时候不超过1s。
     *
     * @param addTime 图片添加时间，单位:秒
     */
    private boolean matchAddTime(long addTime) {
        return System.currentTimeMillis() - addTime * 1000 < 1500;
    }

    /**
     * 判断是否是截屏
     */
    private boolean checkScreenShot(String data, long dateTaken) {
        data = data.toLowerCase();
        // 判断图片路径是否含有指定的关键字之一, 如果有, 则认为当前截屏了
        for (String keyWork : KEYWORDS) {
            if (data.contains(keyWork)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 尺寸不大于屏幕尺寸（发现360奇酷手机可以对截屏进行裁剪）
     */
    private boolean matchSize(String filePath) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);


        return UIScreen.screenWidth >= options.outWidth && UIScreen.screenHeight + 200 >= options.outHeight;
    }
}
