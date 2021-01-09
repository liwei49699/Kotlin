package com.liwei.kotlin.snapshot.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.liwei.kotlin.BaseApplication;
import com.liwei.kotlin.BuildConfig;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


/**
 * # ***********************************************************************************************
 * # ClassName:      WindLog.java
 * # Function:       统一日志及文件输出方式
 * # @author         ylrong
 * # @version        Ver 1.0
 * # ***********************************************************************************************
 * # Modified By     ylrong     2018/11/9    17:16
 * # Modifications:  initial
 * # ***********************************************************************************************
 * 支持存储/data/data目录 context.getFilesDir()   context.getCacheDir()
 * 支持存储/sdcard/Android/data目录 context.getExternalFilesDir()  context.getExternalCacheDir()
 * 支持文件存储、支持日志输出
 */
public class WindLog {

    /**
     * LOG文件后缀
     */
    private static final String LOG_SUFFIX = ".log";
    /**
     * Wind专用路径
     */
    private static final String WIND_DIR = File.separator + "Wind" + File.separator;

    /**
     * 获取Cache存储路径
     *
     * @return Cache存储路径
     */
    public static String getCachePath() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File cacheDir = BaseApplication.getInstance().getExternalCacheDir();
            if (cacheDir != null) {
                return cacheDir.getPath();
            }
        } else {
            File cacheDir = BaseApplication.getInstance().getCacheDir();
            if (cacheDir != null) {
                return cacheDir.getPath();
            }
        }
        return null;
    }

    /**
     * 获取Cache存储路径 用于不同进程比如service调用
     *
     * @param context 上下文
     * @return Cache存储路径
     */
    public static String getCachePath(Context context) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File cacheDir = context.getExternalCacheDir();
            if (cacheDir != null) {
                return cacheDir.getPath();
            }
        } else {
            File cacheDir = context.getCacheDir();
            if (cacheDir != null) {
                return cacheDir.getPath();
            }
        }
        return null;
    }

    /**
     * 获取File存储路径
     *
     * @return File存储路径
     */
    public static String getFilePath() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File fileDir = BaseApplication.getInstance().getExternalFilesDir(null);
            if (fileDir != null) {
                return fileDir.getPath();
            }
        } else {
            File fileDir = BaseApplication.getInstance().getFilesDir();
            if (fileDir != null) {
                return fileDir.getPath();
            }
        }
        return null;
    }

    /**
     * 获取File存储路径 用于不同进程比如service调用
     *
     * @param context 上下文
     * @return File存储路径
     */
    public static String getFilePath(Context context) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File fileDir = context.getExternalFilesDir(null);
            if (fileDir != null) {
                return fileDir.getPath();
            }
        } else {
            File fileDir = context.getFilesDir();
            if (fileDir != null) {
                return fileDir.getPath();
            }
        }
        return null;
    }

    /**
     * 获取sd卡存储路径
     *
     * @return File存储路径
     */
    public static String getSDFilePath() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File fileDir = Environment.getExternalStorageDirectory();
            if (fileDir != null) {
                String dirPath = fileDir.getPath() + WIND_DIR + BaseApplication.getInstance().getPackageName();
                File dirFile = new File(dirPath);
                if (!dirFile.exists()) {
                    dirFile.mkdir();
                }
                return dirPath;
            }
        } else {
            File fileDir = BaseApplication.getInstance().getFilesDir();
            if (fileDir != null) {
                return fileDir.getPath();
            }
        }
        return null;
    }

    /**
     * 获取sd卡存储路径 用于不同进程比如service调用
     *
     * @param context 上下文
     * @return File存储路径
     */
    public static String getSDFilePath(Context context) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File fileDir = Environment.getExternalStorageDirectory();
            if (fileDir != null) {
                String dirPath = fileDir.getPath() + WIND_DIR + context.getPackageName();
                File dirFile = new File(dirPath);
                if (!dirFile.exists()) {
                    dirFile.mkdir();
                }
                return dirPath;
            }
        } else {
            File fileDir = context.getFilesDir();
            if (fileDir != null) {
                return fileDir.getPath();
            }
        }
        return null;
    }

//    /**
//     * 存储在context.getExternalCacheDir()中
//     *
//     * @param folderName 文件夹
//     * @param fileName   文件名
//     * @param content    内容
//     */
//    public static void writeCache(String folderName, String fileName, String content) {
//        if (BuildConfig.LOG_ENABLE) {
//            // 输出到控制台
//            Log.v(fileName, content);
//        }
//        String root = getCachePath();
//        if (root != null) {
//            write(root, folderName, fileName, content);
//        }
//    }
//
//    /**
//     * 存储在context.getExternalFilesDir()中
//     *
//     * @param folderName 文件夹
//     * @param fileName   文件名
//     * @param content    内容
//     */
//    public static void writeFile(String folderName, String fileName, String content) {
//        if (BuildConfig.LOG_ENABLE) {
//            // 输出到控制台
//            Log.v(fileName, content);
//        }
//        String root = getFilePath();
//        if (root != null) {
//            write(root, folderName, fileName, content);
//        }
//    }

    /**
     * 存储在sd卡中
     *
     * @param folderName 文件夹
     * @param fileName   文件名
     * @param content    内容
     */
//    public static void writeSDFile(String folderName, String fileName, String content) {
//        if (BuildConfig.LOG_ENABLE) {
//            // 输出到控制台
//            Log.v(fileName, content);
//        }
//        String root = getSDFilePath();
//        if (root != null) {
//            write(root, folderName, fileName, content);
//        }
//    }

    /**
     * 存储在root中
     *
     * @param root       指定存储目录
     * @param folderName 文件夹
     * @param fileName   文件名
     * @param content    内容
     */
    private static void write(String root, String folderName, String fileName, String content) {
        FileOutputStream fos = null;
        OutputStreamWriter osw = null;
        try {
            String folderPath = root + File.separator + folderName + File.separator;
            File folderFile = new File(folderPath);
            if (!folderFile.exists()) {
                folderFile.mkdir();
            }
            String filePath = folderPath + fileName + LOG_SUFFIX;
            File file = new File(filePath);
            if (!folderFile.exists()) {
                file.createNewFile();
            }
            fos = new FileOutputStream(file, true);
            osw = new OutputStreamWriter(fos);
            osw.write(content);
            osw.write("\n");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (osw != null) {
                    osw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 读取sd卡中存储的内容
     *
     * @param folderName 文件夹
     * @param fileName   文件名
     */
    public static String readSDFile(String folderName, String fileName) {
        String root = getSDFilePath();
        if (root != null) {
            return read(root, folderName, fileName);
        }
        return null;
    }

    /**
     * 读取root中内容
     *
     * @param root       指定存储目录
     * @param folderName 文件夹
     * @param fileName   文件名
     */
    private static String read(String root, String folderName, String fileName) {
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            String folderPath = root + File.separator + folderName + File.separator;
            File folderFile = new File(folderPath);
            if (folderFile.exists()) {
                String filePath = folderPath + fileName + LOG_SUFFIX;
                File file = new File(filePath);
                if (file.exists()) {
                    fis = new FileInputStream(file);
                    isr = new InputStreamReader(fis);
                    br = new BufferedReader(isr);
                    String line = null;
                    String content = "";
                    while ((line = br.readLine()) != null) {
                        content = content + line;
                    }
                    return content;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (isr != null) {
                    isr.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 删除指定文件
     *
     * @param folderName 文件夹
     * @param fileName   文件名
     */
    public static void deleteSDFile(String folderName, String fileName) {
        String root = getSDFilePath();
        if (root != null) {
            delete(root, folderName, fileName);
        }
    }

    /**
     * 删除root中文件
     *
     * @param root       指定存储目录
     * @param folderName 文件夹
     * @param fileName   文件名
     */
    private static void delete(String root, String folderName, String fileName) {
        try {
            String folderPath = root + File.separator + folderName + File.separator;
            File folderFile = new File(folderPath);
            if (folderFile.exists()) {
                String filePath = folderPath + fileName + LOG_SUFFIX;
                File file = new File(filePath);
                if (folderFile.exists()) {
                    file.delete();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
