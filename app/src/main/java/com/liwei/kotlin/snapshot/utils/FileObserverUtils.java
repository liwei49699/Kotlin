package com.liwei.kotlin.snapshot.utils;

import android.os.Build;
import android.os.Environment;
import android.os.FileObserver;
import android.service.notification.NotificationListenerService;
import android.text.TextUtils;

import com.liwei.kotlin.snapshot.ISnapShotCallBack;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;

/**
 * # **********************************************************************************************
 * # ClassName:      FileObserverUtils.java
 * # Description:    文件监视器
 * # Author:         lysun
 * # Version:        Ver 1.0
 * # **********************************************************************************************
 * # Modified By:    lysun     2017/8/17    13:48
 * # Modifications:  initial
 * # **********************************************************************************************
 */
public class FileObserverUtils {
    /**
     * 文件目录 DCIM
     */
    public static final String DIR_DCIM = Environment.getExternalStorageDirectory() + File.separator + Environment.DIRECTORY_DCIM
            + File.separator + "Screenshots" + File.separator;
    /**
     * 文件目录 PICTURE/Screenshots
     */
    public static final String DIR_PICTURE = Environment.getExternalStorageDirectory() + File.separator + Environment.DIRECTORY_PICTURES
            + File.separator + "Screenshots" + File.separator;
    /**
     * 文件目录 Screenshots vivo多数在此目录
     */
    public static final String DIR_SCREENSHOTS = Environment.getExternalStorageDirectory() + File.separator + Environment.DIRECTORY_PICTURES
            + File.separator + "Screenshots" + File.separator;
    /**
     * 手机品牌小米
     */
    public static final String BRAND_XIAOMI = "XIAOMI";
    /**
     * 手机品牌魅族
     */
    public static final String BRAND_MEIZU = "MEIZU";
    /**
     * 手机品牌华为
     */
    public static final String BRAND_HUAWEI = "HUAWEI";
    /**
     * 手机品牌VIVO
     */
    public static final String BRAND_VIVO = "VIVO";
    /**
     * 手机品牌OPPO
     */
    public static final String BRAND_OPPO = "OPPO";
    /**
     * 手机品牌三星
     */
    public static final String BRAND_SAMSUNG = "SAMSUNG";
    /**
     * 手机品牌一加
     */
    public static final String BRAND_ONEPLUS = "ONEPLUS";

    /**
     * 获取合法的截屏文件
     */
    public static String getValidScreenShotsDir() {
        if (checkValidScreenShotsDir(DIR_PICTURE)) {
            return DIR_PICTURE;
        }
        if (checkValidScreenShotsDir(DIR_DCIM)) {
            return DIR_DCIM;
        }
        if (checkValidScreenShotsDir(DIR_SCREENSHOTS)) {
            return DIR_SCREENSHOTS;
        }
        if (BRAND_XIAOMI.equalsIgnoreCase(Build.MANUFACTURER)) {
            return DIR_DCIM;
        }
        if (BRAND_MEIZU.equalsIgnoreCase(Build.MANUFACTURER)
                || BRAND_ONEPLUS.equalsIgnoreCase(Build.MANUFACTURER)
                || BRAND_SAMSUNG.equalsIgnoreCase(Build.MANUFACTURER)
                || BRAND_HUAWEI.equalsIgnoreCase(Build.MANUFACTURER)
                || BRAND_OPPO.equalsIgnoreCase(Build.MANUFACTURER)) {
            return DIR_PICTURE;
        }
        if (BRAND_VIVO.equalsIgnoreCase(Build.MANUFACTURER)) {
            return DIR_SCREENSHOTS;
        }
        return DIR_PICTURE;
    }

    /**
     * 检测是否是合法的目录
     */
    public synchronized static boolean checkValidScreenShotsDir(String directory) {
        File mFile = new File(directory);
        // 有该目录
        if (mFile.exists()) {
            // 目录下暂无文件  还需要再检测
            if (mFile.list() == null || mFile.list().length == 0) {
                return false;
            } else {
                // 有文件
                return true;
            }
        }
        return false;
    }

    /**
     * 文件观察者
     */
    private static FileObserver fileObserver;
    /**
     * 截图回调
     */
    private static ISnapShotCallBack snapShotCallBack;
    /**
     * 截图文件夹
     */
    public static String SNAP_SHOT_FOLDER_PATH;
    /**
     * 最后一次截图地址
     */
    public static String lastShownSnapshot;
    /**
     * 最大尝试次数
     */
    private static final int MAX_TRY = 2;

    /**
     * 设置callback
     */
    public static void setSnapShotCallBack(ISnapShotCallBack callBack) {
        snapShotCallBack = callBack;
        initFileObserver();
        NotificationListenerService notificationListenerService;
    }

    private static void initFileObserver() {
        SNAP_SHOT_FOLDER_PATH = getValidScreenShotsDir();

        fileObserver = new FileObserver(SNAP_SHOT_FOLDER_PATH, FileObserver.CREATE) {
            @Override
            public void onEvent(int event, String path) {
                if (!TextUtils.isEmpty(path) && event == FileObserver.CREATE && (!path.equals(lastShownSnapshot))) {
                    // 有些手机同一张截图会触发多个CREATE事件，避免重复展示
                    lastShownSnapshot = path;
                    String snapShotFilePath = SNAP_SHOT_FOLDER_PATH + path;
                    int tryTimes = 0;
                    while (true) {
                        try {
                            // 收到CREATE事件后马上获取并不能获取到，需要延迟一段时间
                            Thread.sleep(1500);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            ImageLoader.getInstance().loadImageSync("file://" + path);
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
                    snapShotCallBack.onSnapShotTaken(SNAP_SHOT_FOLDER_PATH + path);
                }
            }
        };
    }


    /**
     * 开始监听
     */
    public static void startSnapshotWatching() {
        if (null == snapShotCallBack) {
            return;
        }
        try {
            fileObserver.startWatching();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 结束监听
     */
    public static void stopSnapshotWatching() {
        try {
            fileObserver.stopWatching();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
